# clover-authenticator
Project 2. Authenticate to a website on your laptop via Touch ID/Android Fingerprint (iOS or Android)

Background: Logging in on the web with email address, password, and a 2nd-factor of authentication is a pain: you have to type in email/password, launch the Google authenticator app on your phone, and type in the TOTP code.

Goal: Build a website/server ("ACME website") and an iOS/Android app (called "Authenticator") that works as follows: Browse to ACME website on your laptop. Simply browsing to that page launches the Authenticator app. Users use Touch ID/Imprint on the app. User is magically logged in on the ACME site.

Recommendations: Use Bluetooth Low Energy advertisement from browser (there are Chrome extensions for this) to wake up and send a bit of data to the Authenticator iOS/Android app. Use websockets from web browser to web server to get instant notification of fingerprint authentication from Authenticator app.
