Activr Frontend
===============

## About
The Activr frontend is a Cordova app built using the Ionic framework.

## Installation / Dependencies

In order to participate in the development of this app, you will need to install the following:
- [Node.js](https://nodejs.org/) (Javascript platform, includes npm)
- [Bower](http://bower.io/) (dependency management tool)
- [Cordova](https://cordova.apache.org/) (cross-platform mobile APIs in Javascript)
- [Ionic](http://ionicframework.com/) (HTML5 mobile app framework built on Angular)

The following Cordova plugins are required. They should be included automatically when you start an Ionic project, but are NOT part of the Git repo.
- com.ionic.keyboard
- org.apache.cordova.console
- org.apache.cordova.device
- org.apache.cordova.camera
- org.apache.cordova.file
- org.apache.cordova.file-transfer
- n1.x-services.plugins.toast

The following ionic modules are also required, but are included in the Git repo.
- [ionic-contrib-tinder-cards](https://github.com/driftyco/ionic-ion-tinder-cards)

## Testing

#### Browser
1. Run "ionic serve" from the project root directory. It should open a browser windoe with the app (be sure to resize it appropriately). All changes to the source should be reflected immediately in the browser. (Note that if using Cordova plugins that depend on native functionality, this will not work in the browser.)

#### Android Device / Windows
1. Install the [Android SDK Tools](https://developer.android.com/sdk/index.html).
2. In the SDK Manager, download and install "platform-tools".
3. Add "[your_sdk_root]/platform-tools" to your PATH variable (optional I guess, but do it).
4. Install compatibe adb drivers for your device (just Google "[your_device] adb drivers").
5. Plug your device into your computer and let the adb drivers install.
6. Type "adb devices" and ensure that your device is recognized.
7. From the Activr root directory, run "ionic platform add android".
8. From the Activr root directory run "ionic run android".

#### iOS Simulator / Mac OSX
1. Download and install XCode from the App Store on your Mac.
2. From the Activr root directory, run "ionic platform add ios".
3. Navigate to "[activr_root]/platforms/ios/" and double-click "activr.xcodeproj" to open the project in XCode.
4. In XCode, select "Product" > "Build for" > [select a device].
5. Select "Product" > "Run". The simulator should start.

#### Other
- There are actually a few other ways Ionic provides to test apps, including with the Android emulator included with Android SDK Tools. Check out Ionic's documentation for more info.
- If you have an iPhone and a Windows computer, I don't really know how to help you. There is probably some way to test on your device but Apple makes it hard and you will have to figure that out.
- If you have an Android phone and a Mac or Linux machine, you can follow similar instructions as for Windows above. Adb is available for all three platforms ([tutorial](http://lifehacker.com/the-easiest-way-to-install-androids-adb-and-fastboot-to-1586992378)).