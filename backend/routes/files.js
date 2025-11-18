const express = require('express');
const router = express.Router();
const fileController = require('../controllers/fileController');
const authenticate = require('../middleware/auth');

router.get('/:providerId/files', authenticate, fileController.listFiles);

module.exports = router;