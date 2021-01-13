const express = require('express');
const app = express();

var Client = require('mongodb').MongoClient;

var inputWords = [];
var level = 0;

app.get('/game/:str', (req, res) => {
    var str = req.params.str;
    inputWords.push(str);
    console.log("들어옴! / " + str);
    console.log(inputWords);

    var output = "NULL";
    var endWord = str.charAt(str.length-1);

    Client.connect('mongodb://localhost:27017', (err, client) => {
        if (err) throw err
        var db = client.db("test")
        var flag = 0;

        if(level == 0){
            db.collection('word1').find({"word": {$regex: new RegExp('^'+endWord)}}).count({}, function(error, numOfDocs) {
                console.log(numOfDocs + "!");

                var cursor = db.collection('word1').aggregate([
                    {$match: {"word": {$regex: new RegExp('^'+endWord)}}},
                    {$sample: {size: 100}}
                ]);
                cursor.each(function(err,doc){ // document 가 예약어이기 때문에 변수명을 doc로 변경
                    if(err){
                    console.log(err);
                    }else{
                        if(doc != null){
                            // inputWords에 있는지 확인
                            // 있으면 넘어가고 없으면 넣기
                            console.log(doc.word);

                            if(inputWords.indexOf(doc.word) == -1 && flag == 0){
                                output = doc.word;
                                inputWords.push(doc.word);
                                flag = 1;
                            }
                        }
                    }
                });

                client.close();
            });
        } else if(level == 1){
            db.collection('word2').find({"word": {$regex: new RegExp('^'+endWord)}}).count({}, function(error, numOfDocs) {
                console.log(numOfDocs + "!");

                var cursor = db.collection('word2').aggregate([
                    {$match: {"word": {$regex: new RegExp('^'+endWord)}}},
                    {$sample: {size: 2}}
                ]);
                cursor.each(function(err,doc){ // document 가 예약어이기 때문에 변수명을 doc로 변경
                    if(err){
                    console.log(err);
                    }else{
                        if(doc != null){
                            // inputWords에 있는지 확인
                            // 있으면 넘어가고 없으면 넣기
                            console.log(doc.word);

                            if(inputWords.indexOf(doc.word) == -1 && flag == 0){
                                output = doc.word;
                                inputWords.push(doc.word);
                                flag = 1;
                            }
                        }
                    }
                });

                client.close();
            });
        } else if(level == 2){
            db.collection('word2').find({"word": {$regex: new RegExp('^'+endWord)}}).count({}, function(error, numOfDocs) {
                console.log(numOfDocs + "!");

                var cursor = db.collection('word2').aggregate([
                    {$match: {"word": {$regex: new RegExp('^'+endWord)}}},
                    {$sample: {size: 100}}
                ]);
                cursor.each(function(err,doc){ // document 가 예약어이기 때문에 변수명을 doc로 변경
                    if(err){
                    console.log(err);
                    }else{
                        if(doc != null){
                            // inputWords에 있는지 확인
                            // 있으면 넘어가고 없으면 넣기
                            console.log(doc.word);

                            if(inputWords.indexOf(doc.word) == -1 && flag == 0){
                                output = doc.word;
                                inputWords.push(doc.word);
                                flag = 1;
                            }
                        }
                    }
                });

                client.close();
            });
        }
    })

    setTimeout(() => {
        console.log(inputWords);
        res.send(output)
    }, 700);
});

app.get('/start/:level', (req, res) => {
    console.log("초기화");
    inputWords = [];
    console.log(inputWords);
    level = Number(req.params.level);
    res.end();
});

app.get('/game/:player/:score/:level', (req, res) => {
    console.log("What the fuck!");

    var player = req.params.player;
    var score = req.params.score;
    var level = req.params.level;

    console.log("들어옴! / " + player + " / " + score + " / " + level);

    Client.connect('mongodb://localhost:27017', (err, client) => {
        if (err) throw err
        var db = client.db("test")

        var tmp = {"player": player, "score": Number(score), "level": Number(level)};
        db.collection('score1').insert(tmp);

        client.close();
    })

    res.end()
});

app.listen(3002, () => {
    console.log('Example app listening on port 3002!');
});