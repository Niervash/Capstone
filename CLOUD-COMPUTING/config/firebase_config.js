const firebase = require('firebase');
const firebaseConfig = {
    apiKey: "AIzaSyB5fJ60UPRnaUC9EerPXkYN9-B3ipngS4w",
    authDomain: "bljr-untuk-capstone.firebaseapp.com",
    databaseURL: "https://bljr-untuk-capstone-default-rtdb.firebaseio.com",
    projectId: "bljr-untuk-capstone",
    storageBucket: "bljr-untuk-capstone.appspot.com",
    messagingSenderId: "416464550504",
    appId: "1:416464550504:web:0e1db03ed680941398d9dd",
    measurementId: "G-329P6K5C0R"
  };
// Initialize Firebase
let firebase_config = firebase.initializeApp(firebaseConfig);


module.exports = firebase_config
