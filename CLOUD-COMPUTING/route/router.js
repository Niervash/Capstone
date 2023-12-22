const express = require('express');
const router = express.Router();
const akunController = require('../controller/function')

router.get('/daftarakun', akunController.daftar_akun)
router.get('/daftarchat',akunController.daftar_chat)
router.post('/register',akunController.register)
router.post('/login',akunController.login)
router.delete('/login/:idChat/pesan',akunController.delete)
router.post('/chat',akunController.buildchat)
router.post('/chat/:idChat/messages',akunController.postsend)
router.get('/chat/:idChat/:idAkun',akunController.getsend)
// router.get('/chat/:pengirim/:penerima',akunController.getsend)



'/chat/:sender/:recipient'
module.exports=router