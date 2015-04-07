# **PicsArt SDK for Android**

This open-source library allows you to integrate PicsArt's social activity into your Android app.

Learn more about provided samples, documentation, integrating the SDK into your app and more at  http://picsart.com/dev/#android-sdk 


##  FEATURES
- AccessToken - Login and OAuth 2.0 Authorization - provides you OAuth 2 token which is needed for PicsArt's RESTful API. You basicly shuld set listener on AccessToken object and call requestAccessToken with params and on listener if the code is success, call getAccessToken to obtain it. Note that there is no expired token feature yet. For details refer to sample app at https://github.com/ZetBrush/picsart-android-sdk  

- PhotoController handles all provided methodes described at http://picsart.com/dev/#photos . After method is called, PhotoController object will notify it's listener either with success code or fail code, both with message. After get() can be called to obtain requested data. For building nested and complex requests you can register Listeners  whit unique ID and notify them whenever you need by calling notifyListener(id,code,"message") or notifyListeners(code,"message")- which will notify all registered listeners. For more details refer to SDK's javadoc and sample app at http://picsart.com/dev/#android-sdk   
- UserContoller handles all provided methods described at http://picsart.com/dev/#users. It's code flow and logic is the same as PhotoController, so set listeners, call methodes, get() needed data, handle your complex dependencies by registering listeners and notifying them. For more details refer to SDK's javadoc and sample app at http://picsart.com/dev/#android-sdk 



