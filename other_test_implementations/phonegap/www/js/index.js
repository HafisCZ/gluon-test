window.onerror = function (e) {
  document.getElementById('results').innerHTML += `<div style="color: red;">${ e }</div>`;
};

window.print = function (label, values) {
    var content = `<b>${ label }</b><br>MIN: ${ Math.min(... values) }<br>MAX: ${ Math.max(... values) }<br>AVG: ${ Math.sum(... values) / 100 }<br>MED: ${ Math.median(... values) }<br>`;
    document.getElementById('results').innerHTML += content;
}

window.showException = function (value) {
    if (typeof(value) == 'object') {
        if (value.code && value.message) {
            value = `${ value.code }: ${ value.message }`;
        }
    }
    document.getElementById('results').innerHTML += `<b style="color: red;">${ value }</b>`;
}

window.setProgress = function (values, count) {
    document.getElementById('progress').value = values.length / count * 100;
    document.getElementById('estimate').innerHTML = `${ ((Math.sum(... values) / values.length * (count - values.length)) / 1000).toFixed(1) } seconds remaining`;
}

Math.ackermann = function (m, n) {
    return m === 0 ? n + 1 : Math.ackermann(m - 1, n === 0 ? 1 : Math.ackermann(m, n - 1));
}

Math.median = function (... values) {
    values.sort((a, b) => a - b);
    if (values.length % 2 == 0) {
        var mid = values.length / 2;
        return (values[mid] + values[mid - 1]) / 2;
    } else {
        return values[values.length - 0.5];
    }
};

Math.sum = function (... values) {
    return values.reduce((a, b) => a + b, 0);
}

const Path = new (class {
    constructor () {
        this.path = navigator.userAgent.toLowerCase().indexOf('android') <= -1 ? '../' : `file://${ window.location.pathname.substring(0, window.location.pathname.lastIndexOf('/') + 1) }/`;
    }
    to (filename) {
        return this.path + filename;
    }
})();

String.random = function (kbytes) {
    return 'HoVakeVqaPkgbJSTUQJopxVLrRCMtbTHOMNOyeZiHQuyaYmsLcftJPRoDGVUaPWWECuXvQyyrkKTogsewbaIDZHukRftcCThrzSIERrbwaGZvJNQajOLQuZLivFoPdmb'.repeat(8 * kbytes);
};

const Time = (function () {
    if (window.performance && Date) {
        let bp = window.performance.now() % 1000 > 0;
        let bd = Date.now() % 1000 > 0;
        return ((bp && bd) || bd) ? Date : window.performance;
    } else return window.performance || Date;
})();

const StackResolver = new (class {
    constructor () {
        this.tasks = [];
        this.current = null;
    }
    queue (task, ... args) {
        StackResolver.tasks.push({
            task: task,
            args: [ ... args ]
        });
    }
    run () {
        if (StackResolver.current == null && StackResolver.tasks.length) {
            StackResolver.current = StackResolver.tasks.shift();
            StackResolver.current.task(... StackResolver.current.args);
        }
    }
    next () {
        StackResolver.current = null;
    }
    repeat (task, count, callback) {
        StackResolver.repeatcount = count;
        StackResolver.repeattask = task;
        StackResolver.repeatcallback = callback;
        StackResolver.runRepeatedTask();
    }
    runRepeatedTask() {
        if (StackResolver.repeatcount-- > 0) {
            StackResolver.repeattask(StackResolver.runRepeatedTask);
        } else {
            StackResolver.repeatcallback();
        }
    }
})();

StackResolver.handle = setInterval(StackResolver.run, 1);

class Test {
    constructor (func) {
        this.values = [];
        this.count = 1;
        this.func = func;
        this.finished = x => { };
        this.exception = x => { throw x; };
        this.progress = x => { };
        this.exit = x => { };
    }

    onExit (func) {
        this.exit = func;
        return this;
    }

    onFinished (func) {
        this.finished = func;
        return this;
    }

    onProgress (func) {
        this.progress = func;
        return this;
    }

    onException (func) {
        this.exception = func;
        return this;
    }

    repeat (count) {
        this.count = count;
        return this;
    }

    execute () {
        if (this.values.length < this.count) {
            try {
                StackResolver.queue(this.func, value => {
                    if (isNaN(value)) {
                        this.exit();
                        this.exception(value);
                    } else {
                        this.values.push(value);
                        this.progress(this.values, this.count);
                        StackResolver.queue(() => this.execute());
                    }
                    StackResolver.next();
                });
                StackResolver.next();
            } catch (ex) {
                this.exit();
                this.exception(ex);
                StackResolver.next();
                return;
            }
        } else {
            this.exit();
            StackResolver.next();
            this.finished(this.values);
        }
    }
}

function testVibration (callback) {
    new Test(accept => {
        let begin = Time.now();
        navigator.vibrate(500);
        accept(Time.now() - begin);
    }).repeat(100).onFinished(callback).onException(showException).onProgress(setProgress).execute();
}

function testAudioPlay (callback) {
    new Test(accept => {
        let begin = Time.now();
        new Media(Path.to('s.mp3')).play();
        accept(Time.now() - begin);
    }).repeat(100).onFinished(callback).onException(showException).onProgress(setProgress).execute();
}

function testPrefsWrite (kbytes, callback) {
    var string = String.random(kbytes);
    new Test(accept => {
        let begin = Time.now();
        for (var i = 0; i < 1000; i++) {
            window.localStorage.setItem('test', string);
            delete window.localStorage['test'];
        }
        accept(Time.now() - begin);
    }).repeat(100).onFinished(callback).onException(showException).onProgress(setProgress).execute();
}

function testPrefsRead (kbytes, callback) {
    var string = String.random(kbytes);
    window.localStorage.setItem('test', string);
    new Test(accept => {
        let begin = Time.now();
        for (var i = 0; i < 1000; i++) {
            let read = window.localStorage.getItem('test', '');
        }
        accept(Time.now() - begin);
    }).onExit(() => {
        delete window.localStorage['test'];
    }).repeat(100).onFinished(callback).onException(showException).onProgress(setProgress).execute();
}

function testFileWrite (kbytes, callback) {
    var string = String.random(kbytes);
    new Test(accept => {
        let begin = Time.now();
        StackResolver.repeat(next => {
            window.requestFileSystem(LocalFileSystem.PERSISTENT, 0, filesystem => {
                filesystem.root.getFile('test.txt', { create: true, exclusive: false }, fileentry => {
                    fileentry.createWriter(writer => {
                        writer.write(string);
                        writer.onwriteend = event => {
                            fileentry.remove(function () {
                                next();
                            });
                        }
                    });
                });
            });
        }, 100, () => accept(Time.now() - begin));
    }).repeat(100).onFinished(callback).onException(showException).onProgress(setProgress).execute();
}

function testFileRead (kbytes, callback) {
    var string = String.random(kbytes);
    StackResolver.queue(() => {
        window.requestFileSystem(LocalFileSystem.PERSISTENT, 0, filesystem => {
            filesystem.root.getFile('test.txt', { create: true, exclusive: false }, fileentry => {
                fileentry.createWriter(writer => {
                    writer.write(string);
                    writer.onwriteend = event => {
                        StackResolver.next();
                    }
                });
            });
        });
    });
    StackResolver.queue(() => {
        new Test(accept => {
            let begin = Time.now();
            StackResolver.repeat(next => {
                window.requestFileSystem(LocalFileSystem.PERSISTENT, 0, (filesystem) => {
                    filesystem.root.getFile('test.txt', null, (fileentry) => {
                        fileentry.file((file) => {
                            var reader = new FileReader(file);
                            reader.onloadend = () => next();
                            reader.readAsText(file);
                        });
                    });
                });
            }, 100, () => accept(Time.now() - begin));
        }).repeat(100).onExit(() => {
            window.requestFileSystem(LocalFileSystem.PERSISTENT, 0, filesystem => {
                filesystem.root.getFile('test.txt', null, fileentry => {
                    fileentry.remove();
                });
            });
        }).onFinished(callback).onException(showException).onProgress(setProgress).execute();
    });
}

function testGeolocation (callback) {
    new Test(accept => {
        let begin = Time.now();
        navigator.geolocation.getCurrentPosition(() => accept(Time.now() - begin), e => accept(e), { timeout: 2500 });
    }).repeat(10).onFinished(callback).onException(showException).onProgress(setProgress).execute();
}

function testAckermann (a, b, callback) {
    new Test(accept => {
        let begin = Time.now();
        Math.ackermann(a, b);
        accept(Time.now() - begin);
    }).repeat(100).onFinished(callback).onException(showException).onProgress(setProgress).execute();
}

function run () {
    testPrefsWrite(1, values => {
        print('PrefsWrite 1000 @ 1KB (100 samples)', values);
        testPrefsRead(1, values => {
            print('PrefsRead 1000 @ 1KB (100 samples)', values);
            testFileWrite(1, values => {
                print('FileWrite 100 @ 1KB (100 samples)', values);
                testFileRead(1, values => {
                    print('FileRead 100 @ 1KB (100 samples)', values);
                    testGeolocation(values => {
                        print('Geolocation (10 samples)', values);
                        testVibration(values => {
                            print('Vibration (100 samples)', values);
                            testAudioPlay(values => {
                                print('AudioPlay (100 samples)', values);
                                testAckermann(3, 9, values => {
                                    print('Ackermann 3/9 (100 samples)', values);
                                    testAckermann(3, 11, values => {
                                        print('Ackermann 3/11 (100 samples)', values);
                                    });
                                });
                            });
                        });
                    });
                });
            });
        });
    });
}
