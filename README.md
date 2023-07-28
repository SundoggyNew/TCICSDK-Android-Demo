# TCICSDK-Android-Demo

The following introduces two solutions proposed by Google Play to reduce the size of the base APK and increase the download speed.

When integrating the implementation 'com.tencent.edu:TCICSDK:1.8.0.20230727-SNAPSHOT', the kernel file of the tbs browsing service SDK is not included, and the kernel file can be loaded separately in the following way.

The naming rule of the kernel file is "[tbs_core_{kernel version number}_{time stamp}_nolog_fs_obfs_{running architecture}.tbs](assetpack/src/main/assets/tbs_core_046471_20230726172002_nolog_fs_obfs_arm64-v8a_release.tbs)"

Call the following interface in TCICSDK to load the kernel

```
/**
  * load the kernel
  * @param licenseKey               Authorized licenseKey
  * @param TBSSdkManageCallback     Kernel loading related callbacks
  * @param coreFile                 kernel file
  * @param X5Version                kernel version number. Refer to the version number on the kernel file naming 
  */
public void initX5Core(String licenseKey, TBSSdkManageCallback tbsCallback, File coreFile, Integer X5Version)；
```

Update the version of the Android Gradle plugin in your project’s build.gradle file to 4.0.0 or later.

Update the version of Android Studio 4.2.0 or later

## [Introduction to Play Asset Delivery](PlayAssetDelivery.md)

## [Introduction to Play Feature Delivery](PlayFeatureDelivery.md)