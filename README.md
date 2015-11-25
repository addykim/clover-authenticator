# Clover Authenticator

This project won Honorable mentions at the first Clover Network Invitational.

This is the codebase for the Android app. Find the server code [here](https://github.com/SterlinggTH/CloverWebServer).

Currently this project only works for a stacic IP on our test phone. The code to find the registration id to send to another device is available but commented out.

We also were not successful in getting the Bluetooth Low Energy to work for this project. It seems it was doable, as our mentor Narb was able to figure it out, but we decided not to implement it in order to have a working demo.

Click [Photo Demo][here] to skip to the photo demo.

## Project specifications
### Project 2. Authenticate to a website on your laptop via Android Fingerprint.

### Background
Logging in on the web with email address, password, and a 2nd-factor of authentication is a pain: you have to type in email/password, launch the Google authenticator app on your phone, and type in the TOTP code.

### Goal
Build a website/server ("ACME website") and an iOS/Android app (called "Authenticator") that works as follows: Browse to ACME website on your laptop. Simply browsing to that page launches the Authenticator app. Users use Touch ID/Imprint on the app. User is magically logged in on the ACME site.

### Recommendations
Use Bluetooth Low Energy advertisement from browser (there are Chrome extensions for this) to wake up and send a bit of data to the Authenticator iOS/Android app. Use websockets from web browser to web server to get instant notification of fingerprint authentication from Authenticator app.

## Links
[Android websocket](http://www.elabs.se/blog/66-using-websockets-in-native-ios-and-android-apps) we used.

The code for the backend can be found [here](https://github.com/SterlinggTH/CloverWebServer).

### Photo Demo
