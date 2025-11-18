const User = require('../models/User');
const { body, validationResult } = require('express-validator');
const AuditLog = require('../models/AuditLog');

/**
 * Register a new user
 */
async function register(req, res) {
  try {
    // Validate request body
    const errors = validationResult(req);
    if (!errors.isEmpty()) {
      return res.status(400).json({ 
        error: 'Validation failed', 
        details: errors.array() 
      });
    }

    const { email, password, phoneNumber, nickname } = req.body;

    // Check if user already exists
    const existingUser = await User.findOne({ email });
    if (existingUser) {
      return res.status(400).json({ error: 'User with this email already exists.' });
    }

    // Create new user
    const user = new User({
      email,
      password,
      phoneNumber,
      nickname
    });

    // Save user to database
    await user.save();

    // Generate tokens
    const token = user.generateAuthToken();
    const refreshToken = user.generateRefreshToken();
    await user.save();

    // Log the registration
    await AuditLog.create({
      userId: user._id,
      action: 'register',
      resourceType: 'user',
      ipAddress: req.ip,
      userAgent: req.get('User-Agent'),
      deviceId: req.headers['device-id'],
      statusCode: 201
    });

    // Return user data and tokens
    res.status(201).json({
      user: {
        id: user._id,
        email: user.email,
        nickname: user.nickname,
        phoneNumber: user.phoneNumber,
        avatar: user.avatar,
        defaultProvider: user.defaultProvider
      },
      token,
      refreshToken
    });
  } catch (error) {
    console.error('Registration error:', error);
    res.status(500).json({ error: 'Internal server error during registration.' });
  }
}

/**
 * Login user
 */
async function login(req, res) {
  try {
    // Validate request body
    const errors = validationResult(req);
    if (!errors.isEmpty()) {
      return res.status(400).json({ 
        error: 'Validation failed', 
        details: errors.array() 
      });
    }

    const { email, password } = req.body;

    // Find user by email
    const user = await User.findOne({ email });
    if (!user) {
      return res.status(400).json({ error: 'Invalid email or password.' });
    }

    // Check if user is active
    if (!user.isActive) {
      return res.status(400).json({ error: 'Account is deactivated.' });
    }

    // Check if user is frozen
    if (user.isFrozen) {
      return res.status(403).json({ error: 'Account is frozen due to suspicious activity.' });
    }

    // Compare password
    const isMatch = await user.comparePassword(password);
    if (!isMatch) {
      return res.status(400).json({ error: 'Invalid email or password.' });
    }

    // Update last login
    user.lastLogin = new Date();
    await user.save();

    // Generate tokens
    const token = user.generateAuthToken();
    const refreshToken = user.generateRefreshToken();
    await user.save();

    // Log the login
    await AuditLog.create({
      userId: user._id,
      action: 'login',
      resourceType: 'user',
      ipAddress: req.ip,
      userAgent: req.get('User-Agent'),
      deviceId: req.headers['device-id'],
      statusCode: 200
    });

    // Return user data and tokens
    res.json({
      user: {
        id: user._id,
        email: user.email,
        nickname: user.nickname,
        phoneNumber: user.phoneNumber,
        avatar: user.avatar,
        defaultProvider: user.defaultProvider
      },
      token,
      refreshToken
    });
  } catch (error) {
    console.error('Login error:', error);
    res.status(500).json({ error: 'Internal server error during login.' });
  }
}

/**
 * Refresh authentication token
 */
async function refresh(req, res) {
  try {
    const { refreshToken } = req.body;

    if (!refreshToken) {
      return res.status(400).json({ error: 'Refresh token is required.' });
    }

    // Verify refresh token
    const decoded = jwt.verify(refreshToken, process.env.JWT_SECRET);
    
    // Find user
    const user = await User.findById(decoded.userId);
    if (!user) {
      return res.status(401).json({ error: 'Invalid refresh token.' });
    }

    // Check if refresh token matches
    if (user.refreshToken !== refreshToken) {
      return res.status(401).json({ error: 'Invalid refresh token.' });
    }

    // Generate new tokens
    const token = user.generateAuthToken();
    const newRefreshToken = user.generateRefreshToken();
    await user.save();

    // Log the token refresh
    await AuditLog.create({
      userId: user._id,
      action: 'refresh_token',
      resourceType: 'user',
      ipAddress: req.ip,
      userAgent: req.get('User-Agent'),
      deviceId: req.headers['device-id'],
      statusCode: 200
    });

    res.json({
      token,
      refreshToken: newRefreshToken
    });
  } catch (error) {
    if (error.name === 'JsonWebTokenError' || error.name === 'TokenExpiredError') {
      return res.status(401).json({ error: 'Invalid or expired refresh token.' });
    }

    console.error('Token refresh error:', error);
    res.status(500).json({ error: 'Internal server error during token refresh.' });
  }
}

/**
 * Logout user
 */
async function logout(req, res) {
  try {
    const { refreshToken } = req.body;

    if (!refreshToken) {
      return res.status(400).json({ error: 'Refresh token is required.' });
    }

    // Verify refresh token
    const decoded = jwt.verify(refreshToken, process.env.JWT_SECRET);
    
    // Find user
    const user = await User.findById(decoded.userId);
    if (user) {
      // Revoke refresh token
      user.revokeRefreshToken();
      await user.save();
    }

    // Log the logout
    if (req.user) {
      await AuditLog.create({
        userId: req.user._id,
        action: 'logout',
        resourceType: 'user',
        ipAddress: req.ip,
        userAgent: req.get('User-Agent'),
        deviceId: req.headers['device-id'],
        statusCode: 200
      });
    }

    res.json({ message: 'Logged out successfully.' });
  } catch (error) {
    console.error('Logout error:', error);
    res.status(500).json({ error: 'Internal server error during logout.' });
  }
}

/**
 * Get current user profile
 */
async function getProfile(req, res) {
  try {
    const user = await User.findById(req.user._id).select('-password -refreshToken');
    res.json(user);
  } catch (error) {
    console.error('Get profile error:', error);
    res.status(500).json({ error: 'Internal server error while fetching profile.' });
  }
}

/**
 * Update user profile
 */
async function updateProfile(req, res) {
  try {
    const { nickname, phoneNumber, avatar, defaultProvider, syncDirectories } = req.body;
    
    const updates = {};
    if (nickname !== undefined) updates.nickname = nickname;
    if (phoneNumber !== undefined) updates.phoneNumber = phoneNumber;
    if (avatar !== undefined) updates.avatar = avatar;
    if (defaultProvider !== undefined) updates.defaultProvider = defaultProvider;
    if (syncDirectories !== undefined) updates.syncDirectories = syncDirectories;

    const user = await User.findByIdAndUpdate(
      req.user._id,
      { $set: updates },
      { new: true, runValidators: true }
    ).select('-password -refreshToken');

    // Log the profile update
    await AuditLog.create({
      userId: req.user._id,
      action: 'update_profile',
      resourceType: 'user',
      ipAddress: req.ip,
      userAgent: req.get('User-Agent'),
      deviceId: req.headers['device-id'],
      statusCode: 200
    });

    res.json(user);
  } catch (error) {
    console.error('Update profile error:', error);
    res.status(500).json({ error: 'Internal server error while updating profile.' });
  }
}

module.exports = {
  register: [
    body('email').isEmail().normalizeEmail(),
    body('password').isLength({ min: 6 }),
    body('phoneNumber').optional().isMobilePhone(),
    register
  ],
  login: [
    body('email').isEmail().normalizeEmail(),
    body('password').exists(),
    login
  ],
  refresh,
  logout,
  getProfile,
  updateProfile
};