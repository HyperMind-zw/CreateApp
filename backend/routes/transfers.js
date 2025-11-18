const express = require('express');
const router = express.Router();
const transferController = require('../controllers/transferController');
const authenticate = require('../middleware/auth');

router.get('/', authenticate, transferController.listTransfers);

module.exports = router;