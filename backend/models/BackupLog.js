const mongoose = require('mongoose');

const backupLogSchema = new mongoose.Schema({
  userId: {
    type: mongoose.Schema.Types.ObjectId,
    ref: 'User',
    required: true
  },
  backupId: {
    type: String,
    required: true
  },
  logId: {
    type: String,
    unique: true,
    required: true
  },
  status: {
    type: String,
    enum: ['started', 'completed', 'failed', 'cancelled'],
    required: true
  },
  startTime: {
    type: Date,
    required: true
  },
  endTime: {
    type: Date
  },
  duration: {
    type: Number // in milliseconds
  },
  filesProcessed: {
    type: Number,
    default: 0
  },
  totalSize: {
    type: Number // in bytes
  },
  successCount: {
    type: Number,
    default: 0
  },
  failureCount: {
    type: Number,
    default: 0
  },
  failedFiles: [{
    fileName: String,
    error: String
  }],
  errorMessage: {
    type: String
  },
  createdAt: {
    type: Date,
    default: Date.now
  }
});

// Indexes for efficient queries
backupLogSchema.index({ userId: 1 });
backupLogSchema.index({ backupId: 1 });
backupLogSchema.index({ startTime: -1 });
backupLogSchema.index({ logId: 1 });

const BackupLog = mongoose.model('BackupLog', backupLogSchema);

module.exports = BackupLog;