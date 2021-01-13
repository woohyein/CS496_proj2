var express = require("express");
var app = express();
var multer, storage, path, crypto;
multer = require('multer')
path = require('path');
crypto = require('crypto');
var Client = require('mongodb').MongoClient;

var form = "<!DOCTYPE HTML><html><body>" +
"<form method='post' action='/upload' enctype='multipart/form-data'>" +
"<input type='file' name='upload'/>" +
"<input type='submit' /></form>" +
"</body></html>";

app.get('/', function (req, res){
  res.writeHead(200, {'Content-Type': 'text/html' });
  res.end(form);
});

// Include the node file module
var fs = require('fs');

storage = multer.diskStorage({
  destination: './uploads/',
  filename: function(req, file, cb) {
    return crypto.pseudoRandomBytes(16, function(err, raw) {
      if (err) {
        return cb(err);
      }
      return cb(null, "" + (raw.toString('hex')) + (path.extname(file.originalname)));
    });
  }
});



// Post files
app.post( "/upload", multer({storage: storage}).single('upload'), function(req, res) {

  Client.connect('mongodb://localhost:27017', function(error, client){
		var db = client.db('test');

		if(error) {
		  console.log(error);
		} else {
      var tmp = {"user": req.body.upload, "filename": req.file.filename};
      db.collection('album1').insert(tmp);
      console.log("Good");

		  client.close();
    }

  });

  console.log(req.file);
  console.log(req.body);
  res.redirect("/uploads/" + req.file.filename);
  return res.status(200).end();
});



app.get('/uploads/:upload', function (req, res){
  file = req.params.upload;
  // console.log(req.params.upload);
  var img = fs.readFileSync(__dirname + "/uploads/" + file);
  res.writeHead(200, {'Content-Type': 'image/png' });
  res.end(img, 'binary');
});



app.get("/load/:user", (req, res) => {
  var currentUser = req.params.user;
  console.log(currentUser);
  var stringList = [];

  Client.connect('mongodb://localhost:27017', function(error, client){
		var db = client.db('test');

		if(error) {
		  console.log(error);
		} else {
      var cursor = db.collection("album1").find({user: currentUser})
      cursor.each(function(err,doc){
        if(err){
          console.log(err);
        }else{
          if(doc != null){
            stringList.push(doc.filename);
          }
        }
      });
      console.log("Good");

		  client.close();
    }


  });

  setTimeout(function() {
    res.json(stringList);
    res.end();
  }, 300);
});



app.post("/del", (req, res) => {
  console.log("삭제 요청이 도착 하였습니다.")
  req.on('data', data => {
    var imageName = JSON.parse(data);
    var filePath = "uploads/" + imageName;
    // 파일시스템 삭제
    var localPath = path.join(__dirname, filePath)
    console.log(localPath)
    fs.unlink(localPath, (err) => {
      if (err) throw err;
      console.log(filePath + ' was deleted');
    });
    // MongoDB 데이터 삭제
    Client.connect('mongodb://localhost:27017', (err, client) => {
      if (err) throw err
      var db = client.db("test")
      db.collection("album1").deleteOne({filename: imageName}, (err, result) => {
        if (err) throw err
        console.log(result + " is delete")
      })
    })
  })
  res.end()
})



app.listen(3001);
