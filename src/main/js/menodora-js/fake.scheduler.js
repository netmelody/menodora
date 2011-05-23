var setTimeout,
    clearTimeout,
    setInterval,
    clearInterval;

(function () {
    var counter = 1,
        ids = {};
        
    function task(fn) {
        var id = counter++;
        ids[id] = new JavaAdapter(java.util.TimerTask, {run: fn});
        return id;
    }

    setTimeout = function (fn, delay) {
        var id = task(fn);
        __timer__.schedule(ids[id], delay);
        return id;
    };

    clearTimeout = function (id) {
        ids[id].cancel();
        __timer__.purge();
        delete ids[id];
    };

    setInterval = function (fn, delay) {
        var id = task(fn);
        __timer__.schedule(ids[id], delay, delay);
        return id;
    };

    clearInterval = clearTimeout;

})();