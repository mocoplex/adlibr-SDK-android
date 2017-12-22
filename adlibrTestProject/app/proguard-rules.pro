# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/yongsunkim/Work/adt-bundle-mac-x86_64-20140702/sdk/tools/proguard/proguard-android.txt
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

# 아래는 애드립 사용을 위한 필수 설정입니다.

-keep class com.mocoplex.adlib.** { *; }
-dontwarn com.mocoplex.adlib.**

# 빌드 SDK 버전이 킷캣(Android 19) 이하 일 경우 추가해주세요.
-dontwarn android.os.Build

# 유니티를 이용한 경우 classes.jar 경로를 확인하고 넣어주시기 바랍니다.
# Mac OS 경로 예시 : /Applications/Unity/Unity.app/Contents/PlaybackEngines/AndroidPlayer/development/bin/classes.jar
# Windows 경로 예시 : 'C:\Program Files (x86)\Unity\Editor\Data\PlaybackEngines\androidplayer\bin\classes.jar'
#-libraryjars /Applications/Unity/Unity.app/Contents/PlaybackEngines/AndroidPlayer/development/bin/classes.jar

# 여기까지가 필수 설정입니다.

# 아래부터 끝까지는 광고 플랫폼 사용을 위한 설정입니다.
# 사용하시는 플랫폼에 필요한 부분 추가 부탁드립니다.
# 사용하지 않을 광고 플랫폼 설정은 삭제하셔도 됩니다.

# Inmobi 사용을 위한 설정입니다.
-keep class com.inmobi.** { *; }
-dontwarn com.inmobi.**
-keep public class com.google.android.gms.**
-dontwarn com.google.android.gms.**
-dontwarn com.squareup.picasso.**
-keep class com.google.android.gms.ads.identifier.AdvertisingIdClient{
     public *;
}
-keep class com.google.android.gms.ads.identifier.AdvertisingIdClient$Info{
     public *;
}

## skip the Picasso library classes
-keep class com.squareup.picasso.** {*;}
-dontwarn com.squareup.picasso.**
-dontwarn com.squareup.okhttp.**

## skip Moat classes
-keep class com.moat.** {*;}
-dontwarn com.moat.**

## skip AVID classes
-keep class com.integralads.avid.library.* {*;}

# Inmobi 사용을 위한 설정입니다. -- END

# Adam(Adfit) 사용을 위한 설정입니다.
-keep class com.kakao.adfit.ads.* { public *; }
-keep class com.kakao.adfit.ads.ba.* { public *; }
-keep class test.adlib.project.ads.SubAdlibAdViewAdam { *; }

# AdMob 사용을 위한 설정입니다.
-keep class test.adlib.project.ads.SubAdlibAdViewAdmob { *; }
-keep public class com.google.android.gms.ads.**{
    public *;
}
-keep public class com.google.ads.**{
    public *;
}

# Cauly 사용을 위한 설정입니다.
-keep public class com.fsn.cauly.** {
	public protected *;
}
-keep public class com.trid.tridad.** {
	public protected *;
}
-dontwarn android.webkit.**
-keep class test.adlib.project.ads.SubAdlibAdViewCauly { *; }

# MillennialMedia 사용을 위한 설정입니다.
-keep class members class com.millennialmedia** {
	public *;
}
-keep class com.millennialmedia**
-keep class test.adlib.project.ads.SubAdlibAdViewMMedia { *; }

# Mobclix 사용을 위한 설정입니다.
-keep public class com.mobclix.android.sdk.* { *; }
-keep class test.adlib.project.ads.SubAdlibAdViewMobclix { *; }

# ShallWeAd 사용을 위한 설정입니다.
-keep class com.jm.co.shallwead.sdk.** {
  public *;
}
-keep class test.adlib.project.ads.SubAdlibAdViewShallWeAd { *; }

# Tad 사용을 위한 설정입니다.
-keep class com.skplanet.tad.** { *; }
-keep class test.adlib.project.ads.SubAdlibAdViewTAD { *; }

# Mediba Ad 사용을 위한 설정입니다.
-keepclasseswithmembers class mediba.ad.sdk.android.openx.** { *; }
-keepclasseswithmembers class com.mediba.jp.* { *; }
-keep class test.adlib.project.ads.SubAdlibAdViewMedibaAd { *; }

# AdMixer Ad 사용을 위한 설정입니다.
-keep class com.admixer.** { *; }
-keep class test.adlib.project.ads.SubAdlibAdViewAdmixer { *; }

# TNK Ad 사용을 위한 설정입니다.
-keep class com.tnkfactory.** { *;}
-keep class test.adlib.project.ads.SubAdlibAdViewTNK { *; }

# Facebook Audience Network 사용을 위한 설정입니다.
-keep class test.adlib.project.ads.SubAdlibAdViewFacebook { *; }

# MobFox 사용을 위한 설정입니다.
-keep class com.mobfox.** { *; } 
-keep class com.mobfox.adapter.** {*;} 
-keep class com.mobfox.sdk.** {*;}
