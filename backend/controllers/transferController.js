const TransferTask = require('../models/TransferTask');
const { v4: uuidv4 } = require('uuid');

/**
 * List user's transfer tasks
 */
async function listTransfers(req, res) {
  try {
    const transfers = await TransferTask.find({ userId: req.user._id })
      .sort({ createdAt: -1 })
      .limit(50); // Limit to last 50 transfers
    
    const transferList = transfers.map(transfer => ({
      id: transfer.taskId,
      fileName: transfer.source.fileName,
      progress: transfer.progress / 100, // Convert to 0-1 scale for frontend
      status: transfer.status
    }));
    
    res.json(transferList);
  } catch (error) {
    console.error('List transfers error:', error);
    res.status(500).json({ error: 'Internal server error while listing transfers.' });
  }
}

module.exports = {
  listTransfers
};