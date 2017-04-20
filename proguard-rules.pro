# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/owant/Library/Android/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

#基本模型不能混淆
-keep class com.owant.thinkmap.model.*{*;}
-keep public class * extends com.owant.thinkmap.model.NodeModel
-keep public class * extends com.owant.thinkmap.model.TreeModel

#使用了fastJSON不能混淆泛型，注解
-keepattributes Signature
-keepattributes *Annotation*


#支付宝混淆
-libraryjars libs/alipaySDK-20150602.jar
-keep class com.alipay.android.app.IAlixPay{*;}
-keep class com.alipay.android.app.IAlixPay$Stub{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback$Stub{*;}
-keep class com.alipay.sdk.app.PayTask{ public *;}
-keep class com.alipay.sdk.app.AuthTask{ public *;}
