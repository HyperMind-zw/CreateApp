const mongoose = require('mongoose');

const backupSchema = new mongoose.Schema({
  userId: {
    type: mongoose.Schema.Types.ObjectId,
    ref: 'User',
    required: true
  },
  backupId: {
    type: String,
    unique: true,
    required: true
  },
  name: {
    type: String,
    required: true
  },
  description: {
    type: String
  },
  source: {
    providerId: {
      type: String,
      required: true
    },
    path: {
      type: String,
      required: true
    }
  },
  destination: {
    providerId: {
      type: String,
      required: true
    },
    path: {
      type: String,
      required: true
    }
  },
  schedule: {
    type: {
      type: String,
      enum: ['manual', 'daily', 'weekly', 'monthly'],
      default: 'manual'
    },
    time: {
      type: String // For scheduled backups (e.g., "02:00")
    },
    dayOfWeek: {
      type: Number // 0-6 (Sunday-Saturday) for weekly backups
    },
    dayOfMonth: {
      type: Number // 1-31 for monthly backups
    }
  },
  type: {
    type: String,
    enum: ['full', 'incremental'],
    default: 'full'
  },
  status: {
    type: String,
    enum: ['active', 'inactive', 'paused'],
    default: 'active'
  },
  lastRun: {
    type: Date
  },
  nextRun: {
    type: Date
  },
  retention: {
    type: Number, // Number of backups to keep
    default: 30
  },
  encryptionEnabled: {
    type: Boolean,
    default: false
  },
  compressionEnabled: {
    type: Boolean,
    default: true
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
backupSchema.pre('save', function (next) {
  this.updatedAt = Date.now();
  next();
});

// Indexes for efficient queries
backupSchema.index({ userId: 1 });
backupSchema.index({ backupId: 1 });
backupSchema.index({ status: 1 });
backupSchema.index({ nextRun: 1 });

const Backup = mongoose.model('Backup', backupSchema);

module.exports = Backup;