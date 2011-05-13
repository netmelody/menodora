var setTimeout,
    clearTimeout,
    setInterval,
    clearInterval;

(function () {
    var timer = new java.util.Timer(),
        counter = 1,
        ids = {};
        
    function task(fn) {
        var id = counter++;
        ids[id] = new JavaAdapter(java.util.TimerTask, {run: fn});
        return id;
    }

    setTimeout = function (fn, delay) {
        var id = task(fn);
        timer.schedule(ids[id], delay);
        return id;
    };

    clearTimeout = function (id) {
        ids[id].cancel();
        timer.purge();
        delete ids[id];
    };

    setInterval = function (fn, delay) {
        var id = task(fn);
        timer.schedule(ids[id], delay, delay);
        return id;
    };

    clearInterval = clearTimeout;

})();