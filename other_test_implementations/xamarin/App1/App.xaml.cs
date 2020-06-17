using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Threading.Tasks;
using Xamarin.Essentials;
using Xamarin.Forms;
using Xamarin.Forms.Xaml;

namespace App1
{


    public class PhoneContact
    {
        public string _id;
        public string LocalName;
    }

    public interface IService
    {
        void ShowToast(string s);
        void PlayAudioFile(string s);
        void GetAllContacts();
    }

    public class Test
    {
        public delegate void Action();
        public delegate void ParamAction<T>(T param);
        public delegate void ParamAction<T, U>(T param, U param2);

        public String Name { get; set; }
        public ParamAction<Object[]> Callback { get; set; }
        public ParamAction<ParamAction<long>> TestFunction { get; set; }
        public long[] Values { get; } = new long[100];
        public int Counter { get; private set; } = 0;

        private void TestInternal() 
        {
            Console.WriteLine(Counter);
            if (Counter < 100)
            {
                this.TestFunction((long val) => {
                    Values[Counter++] = val;
                    TestInternal();
                });
            }
            else 
            {
                long min = Values.Min();
                long max = Values.Max();
                double avg = Values.Average();
                var list = Values.ToList();
                list.Sort();
                long med = list[50];
                Callback(new object[] { 
                    min, max, (long) avg, med, Name
                });
            }
        }

        public void Run()
        {
            this.TestInternal();
        }
    }


    public partial class App : Application
    {
        private string myStringProperty;
        public string MyStringProperty
        {
            get { return myStringProperty; }
            set {
                myStringProperty = value;
                OnPropertyChanged(nameof(MyStringProperty)); // Notify that there was a change on this property
            }
        }

        public App()
        {
            InitializeComponent();
            BindingContext = this;

            MyStringProperty = "lol";

            MainPage = new MainPage();
        }

        private void runWriteStorageTest(int kb, Test.ParamAction<Object[]> callback) {
            string xx = "ABCDABCDABCDABCD";
            Test test = new Test {
                Name = "WriteStorageTest" + kb + "KB",
                Callback = callback,
                TestFunction = (Test.ParamAction<long> retval) => {
                    var time = DateTime.Now.Ticks;
                    for (int i = 0; i < 1000; i++)
                    {
                        Preferences.Set("test", xx);
                    }
                    retval(DateTime.Now.Ticks - time);
                }
            };
            test.Run();
        }

        private void runReadStorageTest(Test.ParamAction<object[]> callback) {
            Test test = new Test {
                Name = "ReadStorageTest",
                Callback = callback,
                TestFunction = (Test.ParamAction<long> retval) => {
                    var time = DateTime.Now.Ticks;
                    for (int i = 0; i < 1000; i++)
                    {
                        var x = Preferences.Get("test", "lol");
                    }
                    retval(DateTime.Now.Ticks - time);
                }
            };
            test.Run();
        }

        private void runWriteTest(int kb, Test.ParamAction<Object[]> callback) {
            string xx = "ABCDABCDABCDABCD";
            Test test = new Test {
                Name = "WriteFileTest" + kb + "KB",
                Callback = callback,
                TestFunction = (Test.ParamAction<long> retval) => {
                    var time = DateTime.Now.Ticks;
                    for (int j = 0; j < 1000; j++)
                    {
                        string path = Environment.GetFolderPath(Environment.SpecialFolder.Personal);
                        string filename = Path.Combine(path, "file.txt");
                        using (var streamWriter = new StreamWriter(filename, false)) {
                            streamWriter.WriteLine(xx);
                        }
                        File.Delete(filename);
                    }
                    retval(DateTime.Now.Ticks - time);
                }
            };
            test.Run();
        }

        private void runReadTest(Test.ParamAction<Object[]> callback) {
            string xx = "ABCDABCDABCDABCD";
            string fpath = Environment.GetFolderPath(Environment.SpecialFolder.Personal);
            string ffilename = Path.Combine(fpath, "file.txt");
            using (var streamWriter = new StreamWriter(ffilename, false))
            {
                streamWriter.WriteLine(xx);
            }
            Test test = new Test { 
                Name = "ReadFileTest",
                Callback = callback,
                TestFunction = (Test.ParamAction<long> retval) => {
                    var time = DateTime.Now.Ticks;
                    for (int i = 0; i < 1000; i++)
                    {
                        string path = Environment.GetFolderPath(Environment.SpecialFolder.Personal);
                        string filename = Path.Combine(path, "file.txt");
                        using (var streamReader = new StreamReader(filename))
                        {
                            while (!streamReader.EndOfStream)
                            {
                                streamReader.ReadLine();
                            }
                        }
                    }

                    retval(DateTime.Now.Ticks - time);
                }
            };
            test.Run();
        }

        private void runVibrateTest(Test.ParamAction<Object[]> callback) {
            Test test = new Test {
                Name = "VibrateTest",
                Callback = callback,
                TestFunction = (Test.ParamAction<long> retval) => {
                    var time = DateTime.Now.Ticks;
                    Vibration.Vibrate();
                    retval(DateTime.Now.Ticks - time);
                }
            };
            test.Run();
        }

        private void runAudioTest(Test.ParamAction<Object[]> callback)
        {
            Test test = new Test
            {
                Name = "AudioTest",
                Callback = callback,
                TestFunction = (Test.ParamAction<long> retval) => {
                    var time = DateTime.Now.Ticks;
                    DependencyService.Get<IService>().PlayAudioFile("s.mp3");
                    retval(DateTime.Now.Ticks - time);
                }
            };
            test.Run();
        }

        public static int ackermann(int m, int n)
        {
            return m == 0 ? n + 1 : ackermann(m - 1, n == 0 ? 1 : ackermann(m, n - 1));
        }

        private void runGeolocationTest(int a, int b, Test.ParamAction<Object[]> callback)
        {
            Test test = new Test
            {
                Name = "Ackermann39",
                Callback = callback,
                TestFunction = (Test.ParamAction<long> retval) => {
                    var time = DateTime.Now.Ticks;

                    ackermann(a, b);

                    retval(DateTime.Now.Ticks - time);
                }
            };
            test.Run();
        }

        protected override void OnStart()
        {
            clear();
            var service = DependencyService.Get<IService>();
            
            runWriteTest(1, (object[] a) => 
            {
                show(a);
            });
            runReadTest((object[] b) =>
            {
                show(b);
            }); 
            runWriteStorageTest(1, (object[] e) =>
            {
                show(e);
            });
            runReadStorageTest((object[] f) =>
            {
                show(f);
            });/*
            runVibrateTest((object[] i) =>
            {
                show(i);
            });
            runAudioTest((object[] j) =>
            {
                show(j);
            });
            runGeolocationTest(3, 9, (object[] k) =>
            {
                show(k);
            }); 
            runGeolocationTest(3, 11, (object[] l) =>
            {
                show(l);
            });*/
        }

        public void show(object[] values) {
            string name = values[4] as string;
            double min = new TimeSpan((long)values[0]).TotalMilliseconds;
            double max = new TimeSpan((long)values[1]).TotalMilliseconds;
            double avg = new TimeSpan((long)values[2]).TotalMilliseconds;
            double med = new TimeSpan((long)values[3]).TotalMilliseconds;
            MyStringProperty = MyStringProperty + String.Format("<b>{0}</b><br>Avg: {1} ms<br>Min: {2} ms<br>Max: {3} ms<br>Med: {4} ms<br><br>", name, avg, min, max, med);
        }

        public void clear() {
            MyStringProperty = "";
        }

        protected override void OnSleep()
        {
        }

        protected override void OnResume()
        {
        }
    }
}
