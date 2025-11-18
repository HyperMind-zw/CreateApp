const CloudAccount = require('../models/CloudAccount');

// Mock list of cloud providers
const providers = [
  { id: 'aliyundrive', name: '阿里云盘' },
  { id: 'baidunetdisk', name: '百度网盘' },
  { id: 'tencentcos', name: '腾讯云COS' },
  { id: 'huaweiobs', name: '华为云OBS' },
  { id: 'yun123', name: '123云盘' }
];

/**
 * List all cloud providers
 */
async function listProviders(req, res) {
  try {
    res.json(providers);
  } catch (error) {
    console.error('List providers error:', error);
    res.status(500).json({ error: 'Internal server error while listing providers.' });
  }
}

/**
 * List user's cloud accounts
 */
async function listAccounts(req, res) {
  try {
    const accounts = await CloudAccount.find({ userId: req.user._id });
    
    const accountList = accounts.map(account => ({
      id: account._id,
      providerId: account.providerId,
      displayName: account.displayName,
      connected: account.isActive && account.expiresAt > new Date()
    }));
    
    res.json(accountList);
  } catch (error) {
    console.error('List accounts error:', error);
    res.status(500).json({ error: 'Internal server error while listing accounts.' });
  }
}

module.exports = {
  listProviders,
  listAccounts
};