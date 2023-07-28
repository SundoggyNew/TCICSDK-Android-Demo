## Introduction to Play Asset Delivery

Play Asset Delivery (PAD) is a new method for distributing Android app and game assets based on Google Play's App Bundle format. The main goal of PAD is to provide more efficient and reliable resource delivery in order to provide a better experience for users.

PAD allows developers to package application resources (such as game assets, native libraries, etc.) into Asset Packs. These Asset Packs can be downloaded on-demand at install time or runtime, reducing the size of the base APK and increasing download speed.

## Play Asset Delivery experience
### Step 1: Create Asset Pack in Android Studio
1. In the top-level directory of your project, create a directory for the asset pack. This directory name is used as the asset pack name. Asset pack names must start with a letter and can only contain letters, numbers, and underscores.
2. In the asset pack directory, create a build.gradle file and add the following code. Make sure to specify the name of the asset pack and only one delivery type:
```
// In the asset pack’s build.gradle file:
plugins {
  id 'com.android.asset-pack'
}

assetPack {
    packName = "assetpack" // Directory name for the asset pack
    dynamicDelivery {
        deliveryType = "install-time"  // Choose one of the three distribution types "[ install-time | fast-follow | on-demand ]"
    }
}
```

3. In the project’s app build.gradle file, add the name of every asset pack in your project as shown below:
```
// In the app build.gradle file:
android {
    ...
    assetPacks = [":assetpack"]
}
```

4. In the project’s settings.gradle file, include all asset packs in your project as shown below:
```
// In the settings.gradle file:
include ':app'
include ':assetpack'
...
```

5. In the asset pack directory, create the following subdirectory: src/main/assets.
6. Place assets in the src/main/assets directory. You can create subdirectories in here as well. The directory structure for your app should now look like the following:
```
build.gradle
settings.gradle
app/
assetpack/build.gradle
assetpack/src/main/assets/your-asset-directories
```

<img width="1099" alt="image" src="https://github.com/SundoggyNew/TCICSDK-Android-Demo/assets/16092720/0e66de9a-9320-4572-9451-df50d045bb33">

7. Build the Android App Bundle with Gradle. In the generated app bundle, the root-level directory now includes the following:
```
assetpack/manifest/AndroidManifest.xml：Configures the asset pack’s identifier and delivery mode
assetpack/assets/your-asset-directories：Directory that contains all assets delivered as part of the asset pack
```

<img width="1074" alt="image" src="https://github.com/SundoggyNew/TCICSDK-Android-Demo/assets/16092720/498cc7d8-8bdd-4d18-8169-73dd99ed76d6">

8. (Optional) Include the Play Asset Delivery Library if you plan to use fast-follow and on-demand delivery
```
implementation "com.google.android.play:asset-delivery:2.1.0"
// For Kotlin use asset-delivery-ktx
implementation "com.google.android.play:asset-delivery-ktx:2.1.0"
```

## Google Play Release Test
### Step 1: Create a Google Play developer account
### Step 2: Create App in Google Play Console
### Step 3: Create the first test version and upload the release application
![Internal testing creates a new version](https://github.com/SundoggyNew/TCICSDK-Android-Demo/assets/16092720/18603a03-3ba3-4fe3-a331-e75605b0cfb7)
### Step 4: Open the test track (track) and invite test users
![copy test link](https://github.com/SundoggyNew/TCICSDK-Android-Demo/assets/16092720/a0ca09fa-4321-4ae6-be74-fcbaf9dc2831)

Open a browser on the test phone to access the copied link and accept the test invitation

<img width="338" alt="image" src="https://github.com/SundoggyNew/TCICSDK-Android-Demo/assets/16092720/3f014c6f-0e1b-4342-af47-f83be7a2bd57">

After accepting the invitation, you can find the corresponding test application in the Google Play Store --> Manage Apps and Devices --> Ratings and Reviews

<img width="338" alt="image" src="https://github.com/SundoggyNew/TCICSDK-Android-Demo/assets/16092720/f3bf8b0e-0082-49b8-bb71-4ca4a007e293">

## process result display

![aab packet size allocation](https://github.com/SundoggyNew/TCICSDK-Android-Demo/assets/16092720/cde711a3-1881-4666-82da-036143939b04)
As can be seen from the uploaded aab package, the aab package is divided into two parts, consisting of Feature modules and Asset packs. The size downloaded in the Google Play store is close to the base of the Install time type plus the Asset packs of the Install time type. After the installation is complete, enter the application to use the resources placed under the Asset packs module.

Note that the total size of functional modules in the Play Asset Delivery solution cannot exceed 150MB, and each module resource of Asset packs also has a corresponding size limit according to different distribution types. The download size of the application using the Play Asset Delivery solution can exceed 150M.

![image](https://github.com/SundoggyNew/TCICSDK-Android-Demo/assets/16092720/197560b8-fc7d-499f-9856-692788e0fcc0)

![image](https://github.com/SundoggyNew/TCICSDK-Android-Demo/assets/16092720/3e27d63a-5fdc-4477-bde6-5973d6ffb002)

The size limit of Asset Packs depends on their delivery type. The following are the size limits for the different delivery types:

Install-time Asset Packs: These assets are downloaded when the application is installed. For Install-time Asset Packs, each Asset Pack has a size limit of 1GB. Note that this does not affect the base APK's 150MB size limit.

Fast-follow Asset Packs: These assets are downloaded immediately after the app is installed. For Fast-follow Asset Packs, each Asset Pack is limited to 1GB in size.

On-demand Asset Packs: These assets are downloaded on-demand while the app is running. For On-demand Asset Packs, there is a size limit of 512MB per Asset Pack.

Overall, Asset Packs have a relatively large size limit, allowing you to distribute large amounts of assets to users. However, when designing your application, you should consider the user's network conditions and storage space constraints, and try to optimize the resource size and download strategy.

![image](https://github.com/SundoggyNew/TCICSDK-Android-Demo/assets/16092720/51c6f19e-1de1-40c4-8158-23e6b38823f2)
