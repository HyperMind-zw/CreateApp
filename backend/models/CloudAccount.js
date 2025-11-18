const mongoose = require('mongoose');

const cloudAccountSchema = new mongoose.Schema({
  userId: {
    type: mongoose.Schema.Types.ObjectId,
    ref: 'User',
    required: true
  },
  providerId: {
    type: String,
    required: true,
    enum: ['aliyundrive', 'baidunetdisk', 'tencentcos', 'huaweiobs', 'yun123']
  },
  displayName: {
    type: String,
    required: true
  },
  accessToken: {
    type: String,
    required: true
  },
  refreshToken: {
    type: String
  },
  expiresAt: {
    type: Date
  },
  scopes: [{
    type: String
  }],
  isEncrypted: {
    type: Boolean,
    default: true
  },
  isActive: {
    type: Boolean,
    default: true
  },
  lastRefreshed: {
    type: Date
  },
  createdAt: {
    type: Date,
    default: Date.now
  },
  updatedAt: {
    type: Date,
    default: Date.now
  }
});

// Update updatedAt field before saving
cloudAccountSchema.pre('save', function (next) {
  this.updatedAt = Date.now();
  next();
});

// Index for efficient queries
cloudAccountSchema.index({ userId: 1, providerId: 1 });
cloudAccountSchema.index({ expiresAt: 1 });

const CloudAccount = mongoose.model('CloudAccount', cloudAccountSchema);

module.exports = CloudAccount;