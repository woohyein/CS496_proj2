const express = require('express');
const app = express();
const mongoose = require('mongoose')
const https = require('https');
const querystring = require('querystring');
const { error } = require('console');
let db = mongoose.connection
db.on("error", console.error)
db.once("open", function(){
  console.log("Connected to mongod server")
})
mongoose.connect(
  "mongodb://localhost:27017/facebookLogin", { useUnifiedTopology: true, useNewUrlParser: true }
)
app.get('/login/:userid/:token/:appId', (req, res) => {
    var appId = req.params.appId
    var token = req.params.token
    var userid = req.params.userid
    //console.log(token)
    console.log(userid)
    console.log(token)
    console.log(appId)

    req.on('error', error => {
        console.log(error)
    })
    var query = querystring.stringify({input_token : token, access_token : "152673103315111|yMfEW_HDDpyGYhpDHPCnhSu2Wy8"})
    //console.log(query)
    https.get('https://graph.facebook.com/debug_token?'+query, res => {
        res.on('data', data => {
            console.log(userid)
        })
    })
    res.on('error', error => {
        console.log(error)
    })
    console.log('in login part');
    res.status(200).end()
});
app.post('/post', (req, res) => {
   console.log('who get in here post /users');
   var inputData;
  // console.log(req.data)
   req.on('data', (data) => {
     console.log("데이터 도착")
     //console.log(data)
     //inputData = JSON.parse(data)
     //console.log(inputData.image)
    //  inputData = JSON.parse(data);
    //  console.log(inputData)
     //db.collection('image').insertOne(inputData)
   });
  //  req.on('end', () => {
  //    console.log("user_id : "+inputData.user_id + " , name : "+inputData.name)
  //  });
   res.write(JSON.stringify({a:"OK!"}));
   res.end();
});
app.listen(3006, () => {
  console.log('Example app listening on port 3006!');
});