# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# 保留你的应用程序包名
-keep class com.example.yi.** { *; }
-keepclassmembers class com.example.yi.** { *; }

# 保留 AndroidX 库
-keep class androidx.** { *; }
-keep class androidx.lifecycle.** { *; }
-keep class androidx.activity.** { *; }

# 保留 Application 信息
-keep class android.app.Application { *; }
-keep class android.content.Context { *; }
-keep class android.content.pm.PackageInfo { *; }

# 保留 MainActivity
-keep class com.example.timecoins.MainActivity { *; }