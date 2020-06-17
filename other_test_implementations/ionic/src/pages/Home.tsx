import { IonContent, IonHeader, IonPage, IonTitle, IonToolbar, IonButton, IonTextarea } from '@ionic/react';
import React, { useState } from 'react';

import { Plugins, FilesystemDirectory, FilesystemEncoding, HapticsImpactStyle } from '@capacitor/core';
const { Storage, Filesystem, Haptics } = Plugins;

const Home: React.FC = () => {
    const [progress, setProgress] = useState();
    const [results, setResults] = useState();
    const runTests = function () {
        var text = '';

        setProgress('0 %');
        setResults(text);

        new Test((values: Array<number>) => {
            text += getPrintable('FileWrite 1000x 16B', values);
            setResults(text);
            new Test((values: Array<number>) => {
                text += getPrintable('FileRead 1000x 16B', values);
                setResults(text);
                new Test((values: Array<number>) => {
                    text += getPrintable('FileWrite 1000x 1024', values);
                    setResults(text);
                    new Test((values: Array<number>) => {
                        text += getPrintable('FileRead 1000x 1024B', values);
                        setResults(text);
                        new Test((values: Array<number>) => {
                            text += getPrintable('Vibrate', values);
                            setResults(text);
                            new Test((values: Array<number>) => {
                                text += getPrintable('Ackermann 3/9', values);
                                setResults(text);
                                new Test((values: Array<number>) => {
                                    text += getPrintable('Ackermann 3/11', values);
                                    setResults(text);


                                }, (accept) => {
                                    var now = Time.now();
                                    Ackermann(3, 11);
                                    accept(Time.now() - now);
                                }).onProgress((a, b) => setProgress(getPrintableProgress(a, b))).execute();
                            }, (accept) => {
                                var now = Time.now();
                                Ackermann(3, 9);
                                accept(Time.now() - now);
                            }).onProgress((a, b) => setProgress(getPrintableProgress(a, b))).execute();
                        }, (accept) => {
                            var now = Time.now();
                            Haptics.vibrate();
                            accept(Time.now() - now);
                        }).onProgress((a, b) => setProgress(getPrintableProgress(a, b))).execute();
                    }, async (accept) => {
                        var now = Time.now();
                        for (var i = 0; i < 10; i++) await Filesystem.readFile({ path: 'a.txt', directory: FilesystemDirectory.Documents, encoding: FilesystemEncoding.UTF8 });
                        accept(Time.now() - now);
                    }).onProgress((a, b) => setProgress(getPrintableProgress(a, b))).execute();
                }, async (accept) => {
                    var now = Time.now();
                    for (var i = 0; i < 10; i++) await Filesystem.writeFile({ path: 'a.txt', data: STRING1024B, directory: FilesystemDirectory.Documents, encoding: FilesystemEncoding.UTF8 });
                    accept(Time.now() - now);
                }).onProgress((a, b) => setProgress(getPrintableProgress(a, b))).execute();
            }, async (accept) => {
                var now = Time.now();
                for (var i = 0; i < 10; i++) await Filesystem.readFile({ path: 'a.txt', directory: FilesystemDirectory.Documents, encoding: FilesystemEncoding.UTF8 });
                accept(Time.now() - now);
            }).onProgress((a, b) => setProgress(getPrintableProgress(a, b))).execute();
        }, async (accept) => {
            var now = Time.now();
            for (var i = 0; i < 10; i++) await Filesystem.writeFile({ path: 'a.txt', data: STRING16B, directory: FilesystemDirectory.Documents, encoding: FilesystemEncoding.UTF8 });
            accept(Time.now() - now);
        }).onProgress((a, b) => setProgress(getPrintableProgress(a, b))).execute();
    };

    /*
    new Test((values: Array<number>) => {
        text += getPrintable('PreferencesRead 1000x 16B', values);
        setResults(text);


    }, async (accept) => {
        var now = Time.now();

        for (var i = 0; i < 1000; i++) await Storage.set({ key: 'a', value: STRING16B });

        accept(Time.now() - now);
    }).onProgress((a, b) => setProgress(getPrintableProgress(a, b))).execute();
    */

    return (
        <IonPage>
            <IonHeader>
                <IonToolbar>
                    <IonTitle>Ionic Blank</IonTitle>
                </IonToolbar>
            </IonHeader>
            <IonContent className="ion-padding">
                <IonButton onClick={() => runTests()}>
                    RUN TESTS
                </IonButton>
                <div>{ progress }</div>
                <IonTextarea readonly autoGrow={true}>{ results }</IonTextarea>
            </IonContent>
        </IonPage>
    );
};

export default Home;

function Ackermann (m: number, n: number) : number {
    return m === 0 ? n + 1 : Ackermann(m - 1, n === 0 ? 1 : Ackermann(m, n - 1));
}

function Median (values: Array<number>) : number {
    values.sort((a, b) => a - b);
    if (values.length % 2 === 0) {
        var mid = values.length / 2;
        return (values[mid] + values[mid - 1]) / 2;
    } else {
        return values[values.length - 0.5];
    }
};

function Sum (values: Array<number>) : number {
    return values.reduce((a, b) => a + b, 0);
}

function Min (values: Array<number>) : number {
    var least = values[0];
    for (var i = 1; i < values.length; i++) {
        if (values[i] < least) least = values[i];
    }
    return least;
}

function Max (values: Array<number>) : number {
    var most = values[0];
    for (var i = 1; i < values.length; i++) {
        if (values[i] > most) most = values[i];
    }
    return most;
}

function getPrintable(label: string, values: Array<number>) : string {
    return `${ label }\nMIN:     ${ Min(values) } \tMAX:      ${ Max(values) } \tAVG:      ${ Sum(values) / 100 } \tMED:      ${ Median(values) }\n`;
}

function getPrintableProgress(values: Array<number>, count: number) : string {
    return (values.length / count * 100).toFixed(0) + '%';
}

const STRING16B = 'ABCDABCDABCDABCD';
const STRING1024B = 'HoVakeVqaPkgbJSTUQJopxVLrRCMtbTHOMNOyeZiHQuyaYmsLcftJPRoDGVUaPWWECuXvQyyrkKTogsewbaIDZHukRftcCThrzSIERrbwaGZvJNQajOLQuZLivFoPdmb'.repeat(8);

const Time = (function () {
    if (window.performance && Date) {
        let bp = window.performance.now() % 1000 > 0;
        let bd = Date.now() % 1000 > 0;
        return ((bp && bd) || bd) ? Date : window.performance;
    } else return window.performance || Date;
})();

class Test {
    callback: (x: Array<number>) => void;
    test: (x: (y: number) => void) => void;
    values: Array<number>;
    loops: number;
    progress: (x: Array<number>, y: number) => void;

    constructor (callback: (x: Array<number>) => void, test: (y: (z: number) => void) => void) {
        this.callback = callback;
        this.test = test;
        this.values = [];
        this.loops = 100;
        this.progress = (x: Array<number>, y: number) => { console.log(x.length, y) };
    }

    onProgress(value: (x: Array<number>, y: number) => void) : Test {
        this.progress = value;
        return this;
    }

    repeats (value: number) : Test {
        this.loops = value;
        return this;
    }

    execute () {
        console.log(this.values.length);
        if (this.values.length < this.loops) {
            this.test((value) => {
                this.values.push(value || 1);
                this.progress(this.values, this.loops);
                this.execute();
            });
        } else {
            this.callback(this.values);
        }
    }
}
