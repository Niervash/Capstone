const express = require("express");
const app = express();
const functions = require('firebase-functions')
const bodyParser = require("body-parser");
const admin = require("firebase-admin");
const akunRouter = require('./route/router')
const port = 4000;

app.get('/', (req, res) => {
  res.json({error:false,message:"SUCCES"});
  res.status(200);
});

app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: true }));

app.use(akunRouter)

app.listen(port, () => {
  console.log(`server berjalan di port ${port}`);
});

exports.app = functions.https.onRequest(app);