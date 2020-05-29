var http = require('http');
var url = require('url');
var fs = require('fs');
var screenshot = require('desktop-screenshot');
var robot = require("robotjs");


/* Windows */
powerOff = 'shutdown -s -t 60';
sleep = '%windir%/System32/rundll32.exe powrprof.dll,SetSuspendState 0,1,0';
restart = 'shutdown -r -t 60';

// Send command to CMD
function sendCommand(comString) {
    const exec = require("child_process").exec;

    exec(comString, function(error, stdout, stderr) {
        console.log('stdout: ' + stdout);
        if (error !== null) {
            console.log('exec error: ' + error);
        }
    });
}

function openPlayer(name) {
    player = name + " ";
}

http.createServer(function (req, res) {
    // print request
    console.log('----------------REQUEST: ' + req.url);

    // favicon
    if (req.url === '/favicon.ico') {
        res.writeHead(200, {'Content-Type': 'image/x-icon'} );
        res.end();
        console.log('favicon requested');
        return;
    }

    // font
    if (req.url === '/Kano.otf') {
        console.log('font requested');
        fs.readFile('.' + req.url, function(err, data) {
            if (err){
                throw err;
            }
            // send data
            res.writeHead(200, {'Content-Type': 'font/opentype'} );
            return res.end(data);
        });
    }

    // screenshot
    else if (req.url.startsWith('/images/screenshot.jpg')) {
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
    }
    
    // images
    else if (req.url.endsWith('.png') && req.url.startsWith('/images/')) {
        fs.readFile('.' + req.url, function(err, data) {
            if (err){
                throw err;
            }
            // send data
            res.writeHead(200, {'Content-Type': 'image/png'} );
            return res.end(data);
        });
    }

    // styles
    else if (req.url === '/style.css') {
        fs.readFile('style.css', function(err, data) {
            if (err){
                throw err;
            }
            // send data
            res.writeHead(200, {'Content-Type': 'text/css'});
            return res.end(data);
        });
    }

    // players
    else if (req.url === '/players.json') {
        fs.readFile('players.json', function(err, data) {
            if (err){
                throw err;
            }
            // send data
            res.writeHead(200, {'Content-Type': 'application/json'});
            return res.end(data);
        });
    }

    // main script
    else if (req.url === '/script.js') {
        fs.readFile('script.js', function(err, data) {
            if (err){
                throw err;
            }
            // send data
            res.writeHead(200, {'Content-Type': 'text/javascript'});
            return res.end(data);
        });
    }

    // panel script
    else if (req.url === '/panel.js') {
        fs.readFile('panel.js', function(err, data) {
            if (err){
                throw err;
            }
            // send data
            res.writeHead(200, {'Content-Type': 'text/javascript'});
            return res.end(data);
        });
    }

    // hammer
    else if (req.url === '/ext/hammer.min.js') {
        fs.readFile('ext/hammer.min.js', function(err, data) {
            if (err){
                throw err;
            }
            // send data
            res.writeHead(200, {'Content-Type': 'text/javascript'});
            return res.end(data);
        });
    }

    // main page
    else if (req.url === '/') {
        fs.readFile('web.html', function(err, data) {
            page = false;
            res.writeHead(200, {'Content-Type': 'text/html'});
            res.write(data);
            return res.end();
        });
    }

    // codes
    else {
        var code = parseInt(url.parse(req.url, true).query.action);
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
                for (let i = 0; i < 5; i++) {
                    robot.keyTap('audio_vol_up');
                    
                }
                break;
            case 4:
                // volume down
                for (let i = 0; i < 5; i++) {
                    robot.keyTap('audio_vol_down');
                }
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
                // sendCommand(sleep);
                break;
            case 9:
                // restart
                sendCommand(restart);
                break;

                // START PLAYERS
            case 101:
                // restart
                // changePlayer('potplayermini64');
                break;
            case 102:
                // restart
                // changePlayer('musicbee');
                break;
            case 103:
                // restart
                // changePlayer('winamp');
                break;

                // MOUSE
            case 20:
                // move mouse
                var speed = parseInt(url.parse(req.url, true).query.speed);
                var x = parseInt(url.parse(req.url, true).query.x) * speed;
                var y = parseInt(url.parse(req.url, true).query.y) * speed;
                console.log('x ' + x + ' y ' + y);

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
                var y = parseInt(url.parse(req.url, true).query.y) * speed;
                console.log('y ' + y);
                robot.scrollMouse(100, 0);
                break;
            case 24:
                // move drag
                var speed = parseInt(url.parse(req.url, true).query.speed);
                var x = parseInt(url.parse(req.url, true).query.x) * speed;
                var y = parseInt(url.parse(req.url, true).query.y) * speed;
                console.log('x ' + x + ' y ' + y);

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
                var text = url.parse(req.url, true).query.text;
                text = text.replace("%20", " ");
                console.log(text);
                robot.typeStringDelayed(text, 1200);
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
            default:
                break;
        }
        res.writeHead(200, {'Content-Type': 'text/html'});
        res.write("done");
        return res.end();
    }

}).listen(6252);
