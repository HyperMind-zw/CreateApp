const mongoose = require('mongoose');

const transferTaskSchema = new mongoose.Schema({
  userId: {
    type: mongoose.Schema.Types.ObjectId,
    ref: 'User',
    required: true
  },
  taskId: {
    type: String,
    unique: true,
    required: true
  },
  source: {
    providerId: {
      type: String,
      required: true
    },
    fileId: {
      type: String,
      required: true
    },
    fileName: {
      type: String,
      required: true
    },
    filePath: {
      type: String,
      required: true
    }
  },
  destination: {
    providerId: {
      type: String,
      required: true
    },
    directoryId: {
      type: String,
      required: true
    },
    directoryPath: {
      type: String,
      required: true
    }
  },
  status: {
    type: String,
    enum: ['pending', 'running', 'completed', 'failed', 'cancelled', 'paused'],
    default: 'pending'
  },
  progress: {
    type: Number,
    default: 0, // 0-100
    min: 0,
    max: 100
  },
  fileSize: {
    type: Number // in bytes
  },
  transferredBytes: {
    type: Number,
    default: 0
  },
  retryCount: {
    type: Number,
    default: 0
  },
  maxRetries: {
    type: Number,
    default: 3
  },
  errorMessage: {
    type: String
  },
  startedAt: {
    type: Date
  },
  completedAt: {
    type: Date
  },
  cancelledAt: {
    type: Date
  },
  pausedAt: {
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
transferTaskSchema.pre('save', function (next) {
  this.updatedAt = Date.now();
  next();
});

// Indexes for efficient queries
transferTaskSchema.index({ userId: 1 });
transferTaskSchema.index({ status: 1 });
transferTaskSchema.index({ createdAt: -1 });
transferTaskSchema.index({ taskId: 1 });

const TransferTask = mongoose.model('TransferTask', transferTaskSchema);

module.exports = TransferTask;