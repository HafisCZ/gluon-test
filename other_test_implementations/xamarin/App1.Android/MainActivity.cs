using System;

using Android.App;
using Android.Content.PM;
using Android.Runtime;
using Android.Views;
using Android.Widget;
using Android.OS;
using Java.Interop;
using Android.Webkit;
using Xamarin.Forms;
using Android.Media;
using System.Collections.Generic;
using System.Threading.Tasks;
using Android.Provider;
using System.Linq;

[assembly: Dependency(typeof(App1.Droid.AndroidService))]
namespace App1.Droid
{
    [Activity(Label = "App1", Icon = "@mipmap/icon", Theme = "@style/MainTheme", MainLauncher = true, ConfigurationChanges = ConfigChanges.ScreenSize | ConfigChanges.Orientation)]
    public class MainActivity : global::Xamarin.Forms.Platform.Android.FormsAppCompatActivity
    {
        protected override void OnCreate(Bundle savedInstanceState)
        {
            TabLayoutResource = Resource.Layout.Tabbar;
            ToolbarResource = Resource.Layout.Toolbar;

            base.OnCreate(savedInstanceState);

            Xamarin.Essentials.Platform.Init(this, savedInstanceState);
            global::Xamarin.Forms.Forms.Init(this, savedInstanceState);
            LoadApplication(new App());
        }
        public override void OnRequestPermissionsResult(int requestCode, string[] permissions, [GeneratedEnum] Android.Content.PM.Permission[] grantResults)
        {
            Xamarin.Essentials.Platform.OnRequestPermissionsResult(requestCode, permissions, grantResults);

            base.OnRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public class AndroidService : IService
    {
        public void ShowToast(String s)
        {
            Toast.MakeText(Android.App.Application.Context, s, ToastLength.Long).Show();
        }

        public void PlayAudioFile(string fileName)
        {
            var player = MediaPlayer.Create(Android.App.Application.Context, Resource.Raw.s);

            player.Start();
        }

        public void GetAllContacts()
        {

            var contactList = new List<PhoneContact>();
            var uri = ContactsContract.CommonDataKinds.Phone.ContentUri;
            string[] projection = { ContactsContract.Contacts.InterfaceConsts.Id,
            ContactsContract.Contacts.InterfaceConsts.DisplayName,
            ContactsContract.CommonDataKinds.Phone.Number };
            Android.App.Application.Context.ContentResolver.Query(uri, projection, null, null, null);
        }
    }
}