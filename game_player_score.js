const express = require('express');

const app = express();
var Client = require('mongodb').MongoClient;

var inputData;
var outputData = [];

app.post('/receive', (req, res) => {
    setTimeout(() => {
        console.log('receive');
        res.json({player_score: outputData})
    }, 100);
});

app.post('/take', (req, res) => {
    console.log('take');

    req.on('data', (data) => {
        inputData = JSON.parse(data);
        console.log(inputData.level);
    });

    req.on('end', () => {
        Client.connect('mongodb://localhost:27017', function(error, client){
        var db = client.db('test');
        outputData = []
        console.log(inputData.level);

            if(error) {
            console.log(error);
            } else {
                var cursor = db.collection('score1').find({"level": inputData.level}).sort({"score": -1, "player": 1});
                var i = 0;
                cursor.each(function(err,doc){ // document 가 예약어이기 때문에 변수명을 doc로 변경
                    if(err){
                        console.log(err);
                    }else{
                        if(doc != null && i < 10){
                            outputData[i] = {
                                player: doc.player,
                                score: doc.score
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

app.listen(3003, () => {
    console.log('Example app listening on port 3003!');
});
