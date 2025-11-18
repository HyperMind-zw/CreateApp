const mongoose = require('mongoose');

const cloudFileSchema = new mongoose.Schema({
  fileId: {
    type: String,
    required: true
  },
  providerId: {
    type: String,
    required: true
  },
  userId: {
    type: mongoose.Schema.Types.ObjectId,
    ref: 'User',
    required: true
  },
  parentId: {
    type: String,
    default: null
  },
  name: {
    type: String,
    required: true
  },
  path: {
    type: String,
    required: true
  },
  isFolder: {
    type: Boolean,
    default: false
  },
  size: {
    type: Number, // in bytes
    default: 0
  },
  mimeType: {
    type: String
  },
  hash: {
    type: String // MD5 or SHA256 hash for integrity check
  },
  lastModified: {
    type: Date,
    required: true
  },
  createdAt: {
    type: Date,
    default: Date.now
  },
  updatedAt: {
    type: Date,
    default: Date.now
  },
  isSynced: {
    type: Boolean,
    default: false
  },
  version: {
    type: Number,
    default: 1
  }
});

// Update updatedAt field before saving
cloudFileSchema.pre('save', function (next) {
  this.updatedAt = Date.now();
  next();
});

// Indexes for efficient queries
cloudFileSchema.index({ userId: 1, providerId: 1 });
cloudFileSchema.index({ userId: 1, path: 1 });
cloudFileSchema.index({ userId: 1, lastModified: 1 });
cloudFileSchema.index({ hash: 1 });
cloudFileSchema.index({ fileId: 1, providerId: 1 });

const CloudFile = mongoose.model('CloudFile', cloudFileSchema);

module.exports = CloudFile;