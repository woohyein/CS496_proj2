const express = require('express');
const app = express();
const https = require('https');
const  error  = require('console');
const  parse  = require('path');
const { resolve } = require('path');
app.get('/game/:str', (req, res) => {
    var str = req.params.str
    var output = "b";
    req.on('error', error => {
        console.log(error)
    })
    https.get(`https://en.wikipedia.org/w/api.php?action=query&titles=${str}&format=json&formatversion=2`, res =>{
        res.setEncoding('utf8')
        res.on('data', data => {
            const parsedData = JSON.parse(data)
            console.log(parsedData["query"]["pages"][0])
            if (parsedData["query"]["pages"][0]["missing"]){
                output = "a"
            }
        });
    })
    res.on('error', error => {
        console.log(error)
    })
    setTimeout(() => {
        res.send(output)
    }, 700);
});

app.listen(3005, () => {
    console.log('Example app listening on port 3005!');
});