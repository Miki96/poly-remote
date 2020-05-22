//  scroll pages
function page(n) {
    console.log(n);
    var pages = document.getElementsByClassName('page');

    for (let i = 0; i < pages.length; i++) {
        const el = pages[i];
        el.style.transform = 'translateX(-' + (100*(n-1)) +'vw)';
    }
}

function send(val) {
    var xhttp = new XMLHttpRequest();
    xhttp.open("GET", "?action=" + val, true);
    xhttp.send();

    // change selected player
    if (parseInt(val) > 100) {
        playerSelected(val);
    }
}

// clear text
function del() {
    document.getElementById('text').value = '';
}

// speak
function text(val) {
    var text = document.getElementById('text').value;
    console.log(text);
    if (text != "") {
        var xhttp = new XMLHttpRequest();
        xhttp.open("GET", "?action=" + val + "&text=" + text, true);
        xhttp.send();
    }
}


// getPlayers
var players;
function getPlayers() {
    var xhttp = new XMLHttpRequest();
    xhttp.open("GET", "/players.json", true);
    xhttp.send();
    xhttp.onreadystatechange = function() {
        if ( xhttp.readyState === 4 && xhttp.status === 200 ) {
            players = JSON.parse(xhttp.responseText);
            console.log(players);
            updatePlayers();
        }
    }
}

getPlayers();

function updatePlayers() {
    
}

// player change
function playerSelected(val) {
    var n = val % 100;
    var text = document.getElementsByClassName("player")[n-1].innerHTML;
    var sel = document.getElementById('selected');
    sel.innerHTML = text;
    // unfocus element
    var content = document.getElementById('content');
    content.classList.remove('show');
}

function show() {
    var content = document.getElementById('content');
    content.classList.add('show');
}

// Detect touch movement
var area = document.getElementById('area');
var mc = new Hammer(area);
mc.get('pan').set({ direction: Hammer.DIRECTION_ALL });

// for multitouch
var singlepan = new Hammer.Pan({ 
    event: 'pan',
    direction: Hammer.DIRECTION_ALL, 
    threshold: 5,
    pointers: 1
});
var multipan = new Hammer.Pan({ 
    event: 'multipan',
    direction: Hammer.DIRECTION_ALL, 
    threshold: 5,
    pointers: 2
});
mc.add(singlepan);
mc.add(multipan);
// singlepan.recognizeWith(multipan);
// multipan.requireFailure(singlepan);


// move mouse
var x = 0;
var y = 0;
var h = 0;

mc.on("panleft panright panup pandown press", function(ev) {
    var deltax = ev.deltaX - x;
    var deltay = ev.deltaY - y;
    // reset x and y
    x = ev.deltaX;
    y = ev.deltaY;
    // send command
    move(deltax, deltay, 2);
});

// end move
mc.on("panend", function (ev) {
    x = 0;
    y = 0;
    h = 0;
})

// mouse left click
mc.on("tap", function (ev) {
    send(21);
})

// mouse right click
mc.on("press", function (ev) {
    send(22);
})

// scroll
mc.on("multipanup multipandown", function (ev) {
    ev.srcEvent.stopPropagation();
    var deltaH = ev.deltaY - h;
    h = ev.deltaY;
    x = ev.deltaX;
    y = ev.deltaY;
    scroll(deltaH);
});

mc.on('multipanend', function (ev) {
    h = 0;
});


function move(xx, yy, speed) {
    var xhttp = new XMLHttpRequest();
    xhttp.open("GET", "?action=20&x=" + xx + "&y=" + yy + "&speed=" + speed, true);
    xhttp.send();
}

function scroll(h) {
    var xhttp = new XMLHttpRequest();
    xhttp.open("GET", "?action=23&y=" + h, true);
    xhttp.send();
}