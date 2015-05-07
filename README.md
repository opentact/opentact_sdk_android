opentact_sdk_android
====================

##Download and Import
---

 - download **[opentact-sdk-android-[version].jar](https://github.com/opentact/opentact_sdk_android/releases)** and **[armeabi/libpjsua2.so](https://github.com/opentact/opentact_sdk_android/releases)** from releases page and copy it into the `libs` folder of your
 application project.

 **or**
 
 - download/clone repository and import hellocharts-library project into your workspace

##Usage
---

    Note that this has only been tested using ​Eclipse with ADT, however it might also be applicable for ​Android Studio.

you can find mord detail from the **[OpeantactSampleForAndroid]()** and **[OpentactAPI Doc](http://opentact-api-documentation.readthedocs.org/en/latest/index.html#)**.

###1.AndroidManifest.xml

add these uses-permission to your **AndroidManifest.xml**

```xml
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.RECORD_AUDIO" />
    <uses-permission android:name="android.permission.MODIFY_AUDIO_SETTINGS" />
    <uses-permission android:name="android.permission.PROCESS_OUTGOING_CALLS" />
    <uses-permission android:name="android.permission.WRITE_SETTINGS" />
    <uses-permission android:name="android.permission.READ_PHONE_STATE" />
    <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    <uses-permission android:name="android.permission.WAKE_LOCK" />
    <uses-permission android:name="android.permission.VIBRATE" />
    <uses-permission android:name="android.permission.READ_LOGS" />
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.CHANGE_NETWORK_STATE"/>
    <uses-permission android:name="android.permission.CHANGE_WIFI_STATE"/>
```

and add this '<'service '/>' in your '<'application '/>'

```xml
<service android:name="org.eclipse.paho.android.service.MqttService" />

```

###2.opentact configuration

setting configure by default. e.g:

```java
    OpentactConfig cfg = new OpentactConfig();
```

or setting configure by yourself. e.g:

```java
    cfg.getSipConfig().setIceEnable(true);
    cfg.getSipConfig().setStunEnable(true);
	cfg.getSipConfig().setTurnEnable(true);
```

###3.opentact callback

application needs to implement the OnSipCallback to get the notification,such as <br>
 - incoming call<br>
 - call state<br>
 - sip register state<br>

and also needs to extend the IMCallback to listen into<br>
 - IM Action<br>
 - message arrived<br>

###4.start opentact

The OpentactManager is a singleton class, and application MUST create one and at most one of this class instance before it can do anything else,
and similarly, once this class is destroyed, application must NOT call any opentact API.

Before call OpentactManager.startWork(Context context,String SID,String AUTHTOKEN,String SSID,OpentactConfig cfg,OnSipCallback onSipCallback);
 You must get the SID,AUTHTOKEN,SSID ([Opentact API Doc](http://opentact-api-documentation.readthedocs.org/en/latest/index.html#))  ,and created the OpentactConfig,OnSipCall. 

and then, starting opentact like this:
```java
OpentactManager opentactManager = OpentactManager.startWork(getApplicationContext(), SID, AUTHTOKEN, SSID, cfg, onSipCallback);
```

###5.sip API

**make call** e.g

you can make a call by [Country Codes](https://countrycode.org/) + phone number, like this:
```java
    opentactManager.makeCallToTermination("86159xxxxxxxx");
```

###6.IM API 

**Connect** 

you MUST call opentactManager.imDoConnect(IMCallback callback) before call any anther IM API:
```java
    myIMCallback.setAction(ACTION.CONNECT);
    opentactManager.imDoConnect(myIMCallback);
```

**Subscribe**

Call opentactManager.imSubscribe(IMCallback callback) so that others send message to you:
```java
    myIMCallback.setAction(ACTION.SUBSCRIBE);
    opentactManager.imSubscribe(myIMCallback);
```

**Publish**

Call opentactManager.imPublish(String frient_ssid,String msg,IMCallback callback) to send message to friends,
see the sample for more detail.


Edit By [MaHua](http://mahua.jser.me)