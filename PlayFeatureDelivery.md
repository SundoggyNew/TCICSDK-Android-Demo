## Introduction to Play Feature Delivery
Play Feature Delivery is a feature provided by Google Play to help developers deliver application features and resources on demand. It allows developers to divide applications into base modules and dynamic feature modules (Dynamic Feature Modules), and download and install these feature modules on demand according to user needs and device conditions.

The low-code android-side sdk package is relatively large, mainly because the static kernel library of the tbs sdk that it relies on is relatively large, accounting for about 80% of the entire package size. Therefore, we can separately put the static kernel library of tbs sdk that the low-code android-side sdk depends on into the dynamic feature module, and download and load the static kernel library of tbs sdk according to user needs, which will reduce the size of the initial download package Much smaller.
## Play Feature Delivery experience
### Step 1: Create Dynamic Feature in Android Studio
File -> New -> New Module
![Create a Dynamic Feature module](https://github.com/SundoggyNew/TCICSDK-Android-Demo/assets/16092720/61400fa5-48c4-4e1d-a0b4-fafd385e4f85)

In the drop-down menu under Install-time inclusion, select the distribution timing, Do not include module at install-time distribution on demand
### Step 2: Mutual reference between App and Dynamic Feature Module
Set dynamicFeatures = [':your Dynamic Feature module name'] in app/build.gradle
```
android {
    ...
    dynamicFeatures = [':dynamicfeature']
}
```
 
Depend on base module implementation project(":app") in dynamicfeature/build.gradle
```
dependencies {
    implementation project(":app")
    ...
}
```

### Step 3: Configure the Dynamic Feature type
Refer to：https://developer.android.com/guide/playcore/feature-delivery#feature-module-manifest

Location：dynamicfeature/src/main/AndroidManifest.xml
```
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:dist="http://schemas.android.com/apk/distribution"
    package="com.tencent.tcic.dynamicfeature">

    <dist:module
        dist:instant="false"
        dist:title="@string/title_dynamicfeature">
        <dist:delivery>
            <dist:on-demand />
        </dist:delivery>
        <dist:fusing dist:include="true" />
    </dist:module>
</manifest>
```

Parameter description: dist:instant="true" means to specify whether the instant experience should be enabled for the module through the Google Play instant experience.

dist:title="@string/title_dynamicfeature" means to give the module a user-facing name.

< dist:on-demand/> means Specifies that the module should be distributed as an on-demand download. That is, modules are not downloaded when they are installed, but the app can request a download later.

### Step 4: Set the dynamic feature pull in the app

Refer to：https://developer.android.com/guide/playcore/feature-delivery/on-demand
Depend on the following libraries in app/build.gradle
```
dependencies {
    ...
    implementation 'com.google.android.play:core:1.10.3'
}
```
 
In the logic that requires module download and installation, add the installation code

```
    private void loadAndLaunchModule(String name) {
        // Skip loading if the module already is installed. Perform success action directly.
        SplitInstallManager splitInstallManager =
                SplitInstallManagerFactory.create(this);
        if (splitInstallManager.getInstalledModules().contains(name)) {
            return;
        }

        SplitInstallRequest request = SplitInstallRequest.newBuilder()
                .addModule("dynamicfeature")
                .build();

        splitInstallManager.registerListener(new SplitInstallStateUpdatedListener() {
            @Override
            public void onStateUpdate(SplitInstallSessionState state) {
                if (state.sessionId() == mySessionId) {
                    switch (state.status()) {
                        case SplitInstallSessionStatus.DOWNLOADING:
                            break;
                        case SplitInstallSessionStatus.INSTALLING:
                            break;
                        case SplitInstallSessionStatus.INSTALLED:
                            // Call the x5 kernel installation interface
                            break;
                        case SplitInstallSessionStatus.FAILED:
                            break;
                    }
                }
            }
        });

        splitInstallManager.startInstall(request)
                .addOnSuccessListener(sessionId -> {
                    mySessionId = sessionId;
                })
                .addOnFailureListener(exception -> {
                });
    }
```

### Step 5: Debug App locally

In **Run/Debug Configurations** select:

<img width="1071" alt="image" src="https://github.com/SundoggyNew/TCICSDK-Android-Demo/assets/16092720/3e478abe-6209-4c92-8827-4edede4fa931">
Then click Run

### Step 6: Sign and generate Android App Bundle (.aab)
Android Studio -> Build -> Generate Signed Bundle / Apk -> Android App Bundle -> Create/Select Key -> release

After completion, you can see an .aab file in the app/release/ directory, which is the complete App file that needs to be uploaded to Google Play, which includes the App's ontology base package (base.apk) and Dynamic Feature. Resource pack (dynamicfeature.apk)

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

![aab package size allocation](https://github.com/SundoggyNew/TCICSDK-Android-Demo/assets/16092720/abcc6519-39b2-483c-bfbe-ff73a62d6793)
As can be seen from the uploaded aab package, the aab package is divided into two parts, consisting of base and dynamicfeature. The size downloaded in the Google Play store is close to the size of the base. After the installation is complete, enter the application and call ··· splitInstallManager.startInstall(request) ··· to download the resources under the dynamicfeature module.

Despite using the Dynamic Feature, Google Play still has a limit on the APK file size of your app. For most devices, this limit is 150MB. This is because Google Play wants to ensure that users can download and install apps quickly and smoothly without problems caused by large file sizes.

However, if your application requires larger asset files, you can use Play Asset Delivery (PAD). PAD is designed for large asset files, which allows you to package these assets into Asset Packs and distribute them to users in an efficient manner. PAD offers three delivery modes: install-time, fast-follow, and on-demand. This means you can control when assets are downloaded and installed.

Using PAD, you can split your asset files into multiple Asset Packs, each Asset Pack can reach up to 1GB in size. This way, you can bypass the APK file size limit while still providing a better user experience.

![150MB Description](https://github.com/SundoggyNew/TCICSDK-Android-Demo/assets/16092720/8c2c09b2-5532-4e09-b84b-7074795af381)
