const express = require('express');
const router = express.Router();
const providerController = require('../controllers/providerController');
const authenticate = require('../middleware/auth');

router.get('/', authenticate, providerController.listProviders);
router.get('/accounts', authenticate, providerController.listAccounts);

module.exports = router;