const robot = require("robotjs");
var fs = require('fs');
var screenshot = require('desktop-screenshot');

const express = require('express');
const app = express();
const port = 3333;

// power commands
const powerOff = 'shutdown -s -t 60';
const sleep = '%windir%/System32/rundll32.exe powrprof.dll,SetSuspendState 0,1,0';
const restart = 'shutdown -r -t 60';

// command
function sendCommand(comString) {
    //console.log(comString);
    var exec = require("child_process").exec;

    exec(comString, function(error, stdout, stderr) {
        console.log('stdout: ' + stdout);
        if (error !== null) {
            console.log('exec error: ' + error);
        }
    });
}

app.get('/', function (req, res) {
    var code = parseInt(req.query.action);
    //console.log(code);

    switch (code) {
        case 1:
            // next track
            robot.keyTap('audio_next');
            break;
        case 2:
            // prev track
            robot.keyTap('audio_prev');
            break;
        case 3:
            // volume up
            robot.keyTap('audio_vol_up');  
            break;
        case 4:
            // volume down
            robot.keyTap('audio_vol_down');
            break;
        case 5:
            // pause/play
            robot.keyTap('audio_play');
            break;
        case 6:
            // mute
            robot.keyTap('audio_mute');
            break;
        case 7:
            // poweroff
            sendCommand(powerOff);
            break;
        case 8:
            // sleep
            sendCommand(sleep);
            break;
        case 9:
            // restart
            sendCommand(restart);
            break;

            // COMMAND
        case 100:
            var command = req.query.command;
            sendCommand(command);
            break;

            // MOUSE
        case 20:
            // move mouse
            var speed = parseInt(req.query.speed);
            var x = parseInt(req.query.x) * speed;
            var y = parseInt(req.query.y) * speed;
            // get position
            var mouse = robot.getMousePos();
            // change position
            robot.setMouseDelay(2);
            robot.moveMouse(mouse.x + x, mouse.y + y);
            break;
        case 21:
            // mouse left click
            robot.mouseClick();
            break;
        case 22:
            // mouse right click
            robot.mouseClick('right');
            break;
        case 23:
            // scroll
            var speed = 4;
            var y = parseInt(req.query.y) * speed;
            robot.scrollMouse(y, 0);
            break;
        case 24:
            // move drag
            var speed = parseInt(req.query.speed);
            var x = parseInt(req.query.x) * speed;
            var y = parseInt(req.query.y) * speed;
            // get position
            var mouse = robot.getMousePos();
            // change position
            robot.setMouseDelay(2);
            robot.dragMouse(mouse.x + x, mouse.y + y);
            break;
            // KEYBOARD
        case 30:
            // removed speak
            break;
        case 31:
            // type text
            var text = req.query.text;
            text = text.replace("%20", " ");
            robot.typeString(text);
            break;
        case 32:
            // backspace
            robot.keyTap('backspace');
            break;
        case 33:
            // backspace
            robot.keyTap('enter');
            break;
        case 34:
            // backspace
            robot.keyTap('tab');
            break;
        case 35:
            // backspace
            robot.keyTap('escape');
            break;
            // DESKTOP
        case 40:
            screenshot("images/screenshot.jpg", function(error, complete) {
                if(error)
                    console.log("Screenshot failed", error);
                else {
                    fs.readFile('images/screenshot.jpg', function(err, data) {
                        if (err){
                            throw err;
                        }
                        // send data
                        res.writeHead(200, {'Content-Type': 'image/jpg'} );
                        return res.end(data);
                    });
                }
            });
            return;
        default:
            break;
    }
    res.send('ok');
});

app.listen(port, () => console.log(`Server active at http://localhost:${port}`));
