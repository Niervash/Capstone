const admin = require('firebase-admin')
const serviceAccount = require("../google-services.json");
const jwt = require('jsonwebtoken');
const { error } = require('firebase-functions/logger');
admin.initializeApp({
  credential: admin.credential.cert(serviceAccount)
  // Add other Firebase configurations here
});

const db = admin.firestore()
module.exports={
  //menampilkan daftar akun yang tersedia
daftar_akun:(req, res) => {
    db.collection('akun')
      .get()
      .then(snapshot => {
        const data = [];
        snapshot.forEach(doc => {
          data.push({id: doc.id,...doc.data()});

          
        });
        res.json({message:'daftar akun yang terdaftar : ',data});
      })
      .catch(error => {
        res.status(500).json({ message: 'Terjadi kesalahan', error: error.message });
      });
  },

  //membuat akun baru
  register: (req, res) => {
    const { name, email, password } = req.body;
  
    // Cek apakah pengguna sudah terdaftar
    db.collection('akun')
      .where('email', '==', email)
      .get()
      .then(snapshot => {
        if (snapshot.empty) {
          // Jika pengguna belum terdaftar, buat data pengguna baru
          const user = {
            name,
            email,
            password,
          };
  
          // Simpan data pengguna ke Firestore
          db.collection('akun')
            .add(user)
            .then(docRef => {
              res.json({ error:false,message: "User Created" });
            })
            .catch(error => {
              res.status(500).json({ message: 'Terjadi kesalahan', error: error.message });
            });
        } 
        else {
          // Jika pengguna sudah terdaftar, kirimkan respons bahwa email sudah digunakan
          res.status(400).json({ message: 'Email sudah digunakan' });
        }
      })
      .catch(error => {
        res.status(500).json({ message: 'Terjadi kesalahan', error: error.message });
      });
  },
  
  //login akun
  login:(req, res) => {
    const { email, password } = req.body;
  
    // Cek apakah pengguna terdaftar dengan email dan password yang valid
    db.collection('akun')
      .where('email', '==', email)
      .limit(1)
      .get()
      .then(snapshot => {
        if (snapshot.empty) {
          // Jika tidak ditemukan pengguna dengan email yang cocok, kirimkan respons gagal login
          res.status(401).json({ message: 'Login gagal,email tidak tersedia' });
        } else {
          // Jika pengguna ditemukan, kirimkan respons berhasil login
          const user = snapshot.docs[0].data();
            // Periksa apakah password cocok
            if (user.password === password) {
            // Jika password cocok, kirimkan respons berhasil login
            const token = jwt.sign({ email: user.email }, 'secret-key', { expiresIn: '3h' });
            const yo =db.collection('akun').doc()
            const loginResult = {
              userId:yo.id,
              name:user.name,
              token:token
            }
            res.json({error:false,message: 'success',loginResult});
            } else {
            // Jika password salah, kirimkan respons gagal login
            res.status(401).json({message: 'password salah' });
            }
        }
      
      })
      .catch(error => {
        res.status(500).json({message: 'Terjadi kesalahan', error: error.message });
      });
  },

  //menghapus akun
  delete:async (req,res)=>{
    try {
      const akunRef = db.collection('akun').doc(req.params.id);
      const akunDoc = await akunRef.get();
  
      if (!akunDoc.exists) {
        return res.status(404).json({ message: 'Akun tidak ditemukan.' });
      }
  
      await akunRef.delete();
  
      res.json({ message: 'Akun berhasil dihapus.' });
    } catch (error) {
      console.log(error);
      res.status(500).send('Terjadi kesalahan saat menghapus akun.');
    }
  },
  
  buildchat: async (req, res) => {
    try {
      const { groupName, members } = req.body;
  
      // Memeriksa apakah semua anggota terdaftar dalam koleksi akun
      const isAllMembersRegistered = await Promise.all(
        members.map(async (member) => {
          const userSnapshot = await admin.firestore().collection('akun').doc(member).get();
          return userSnapshot.exists;
          res.json({message:"anggota harus 2"})
        })
      );
  
      if (!isAllMembersRegistered.every((isRegistered) => isRegistered)) {
        throw new Error('Akun tidak terdaftar');
        res.json({message:"akun tidak terdaftar"})
      }
  
      // Memeriksa apakah obrolan dengan anggota yang sama sudah tersedia
      const existingChatGroups = await admin.firestore().collection('obrolan')
        .where('members', '==', members)
        .get();
  
      if (!existingChatGroups.empty) {
        throw new Error('Obrolan sudah tersedia');
        res.json({message:"obrolan sudah tersedia"})
      }
  
      // Membuat referensi koleksi 'chatGroups'
      const chatGroupsRef = admin.firestore().collection('obrolan').doc();
  
      // Membuat grup obrolan baru
      const newChatGroup = {
        id: chatGroupsRef.id,
        groupName,
        members,
        createdAt: admin.firestore.FieldValue.serverTimestamp()
      };
  
      await chatGroupsRef.set(newChatGroup);
  
      const docSnapshot = await chatGroupsRef.get();
      const createdChatGroup = docSnapshot.data();
  
      res.status(201).json({
        message: 'Chat Berhasil Dibuat',
        id: createdChatGroup.id,
        groupName: createdChatGroup.groupName,
        members: createdChatGroup.members,
        createdAt: createdChatGroup.createdAt.toDate()
      });
    } catch (error) {
      console.error('Error creating chat group:', error);
      res.status(500).json({ error: 'Gagal membuat grup obrolan' });
    }
  },

  postsend: async (req, res) => {
    try {
      const { idChat } = req.params;
      const { message, members } = req.body;
      if (!idChat || !members || !message) {
        throw new Error('Missing required parameters.');
      }
  
      // Membuat dokumen baru untuk pesan dalam subkoleksi "id chat"
      const chatRef = db.collection('obrolan').doc(idChat).collection('id chat').doc();
      const pengirim = members[0]
      const penerima = members[1]
      const id = chatRef.id
      await chatRef.set({
        pengirim,
        penerima,
        message,
        timestamp: admin.firestore.FieldValue.serverTimestamp()
      });
      console.log({pengirim:pengirim,penerima:penerima,idchat:id})
  
      res.json({ message: 'Pesan berhasil dikirim.'});
    } catch (error) {
      console.log(error);
      res.status(500).send('Terjadi kesalahan saat mengirim pesan.');
    }
  },

  daftar_chat:(req, res) => {
    db.collection('obrolan')
      .get()
      .then(snapshot => {
        const data = [];
        snapshot.forEach(doc => {
          data.push({id: doc.id,...doc.data()});
        });
        res.json({message:'daftar chat yang terdaftar : ',data});
      })
      .catch(error => {
        res.status(500).json({ message: 'Terjadi kesalahan', error: error.message });
      });
  },

  getsend:async (req, res) => {
    try {
      const { idChat, idAkun } = req.params;
  
      // Memeriksa apakah akun terdaftar dalam koleksi "akun"
      const userSnapshot = await admin.firestore().collection('akun').doc(idAkun).get();
      if (!userSnapshot.exists) {
        throw new Error('Akun tidak terdaftar');
        res.json({ message: 'Akun tidak terdaftar' });
      }
  
      // Memeriksa apakah obrolan dengan idChat dan member yang sesuai ada
      const chatGroupSnapshot = await admin.firestore().collection('obrolan').doc(idChat).get();
      if (!chatGroupSnapshot.exists) {
        throw new Error('Obrolan tidak ditemukan');
        res.json({ message: 'Obrolan tidak ditemukan' });
      }
  
      const chatGroupData = chatGroupSnapshot.data();
      const { members } = chatGroupData;
  
      // Memeriksa apakah idAkun ada dalam members obrolan
      if (!members.includes(idAkun)) {
        throw new Error('Anda tidak memiliki akses ke obrolan ini');
        res.json({ message: 'Anda tidak memiliki akses ke obrolan ini' });
      }
  
      // Mendapatkan koleksi "id chat" dari Firestore
      const chatRef = admin.firestore().collection('obrolan').doc(idChat).collection('id chat');
      const snapshot = await chatRef.orderBy('timestamp').get();
  
      // Mengumpulkan data pesan dalam array
      const chatMessages = [];
      snapshot.forEach(doc => {
        chatMessages.push(doc.data());
      });
  
      res.json(chatMessages);
    } catch (error) {
      console.log(error);
      res.status(500).send('Terjadi kesalahan dalam mengambil isi chat.');
    }
  }

}


