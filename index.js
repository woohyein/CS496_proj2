const express = require('express');

const app = express();
var Client = require('mongodb').MongoClient;

var inputData;
var outputData = [];

app.post('/receive', (req, res) => {
  console.log('receive');
  res.json({contact: outputData})
});

app.post('/remove', (req, res) => {
  console.log('remove');

  req.on('data', (data) => {
    inputData = JSON.parse(data);
  });

  req.on('end', () => {
	  Client.connect('mongodb://localhost:27017', function(error, client){
		var db = client.db('test');

		if(error) {
		  console.log(error);
		} else {
      db.collection('contact2').deleteMany({"user": inputData.user});
      console.log("Good");

		  client.close();
    }

    });
  });

  res.end();
});

app.post('/send', (req, res) => {
  console.log('send');

  req.on('data', (data) => {
    inputData = JSON.parse(data);
  });

  req.on('end', () => {
	  Client.connect('mongodb://localhost:27017', function(error, client){
		var db = client.db('test');

		if(error) {
		  console.log(error);
		} else {
      var tmp = {"user": inputData.user, "phone": inputData.phone, "fullName": inputData.fullName, "image": inputData.image};
      db.collection('contact2').insert(tmp);
      console.log("Good");

		  client.close();
    }

    });
  });

  res.end();
});

app.post('/take', (req, res) => {
  console.log('take');

  req.on('data', (data) => {
    inputData = JSON.parse(data);
  });

  req.on('end', () => {
	  Client.connect('mongodb://localhost:27017', function(error, client){
    var db = client.db('test');
    outputData = []

		if(error) {
		  console.log(error);
		} else {
      var cursor = db.collection('contact2').find({"user": inputData.user});
      var i = 0;
      cursor.each(function(err,doc){ // document 가 예약어이기 때문에 변수명을 doc로 변경
        if(err){
          console.log(err);
        }else{
          if(doc != null){
            outputData[i] = {
              user: doc.user,
              phone: doc.phone,
              fullName: doc.fullName,
              image: doc.image
            }
            console.log(outputData[i]);

            i++;
          }
        }
      });

		  client.close();
    }

    });
  });

  res.end();
});

app.listen(3000, () => {
  console.log('Example app listening on port 3000!');
});
