/*
 * adlibr - Library for mobile AD mediation.
 * http://adlibr.com
 * Copyright (c) 2012-2013 Mocoplex, Inc.  All rights reserved.
 * Licensed under the BSD open source license.
 */

/*
 * confirmed compatible with ShallWeAd SDK 20151016
 */

package test.adlib.project.ads;

import com.co.shallwead.sdk.ShallWeAdBanner;
import com.co.shallwead.sdk.ShallWeAdBanner.ShallWeAdBannerListener;
import com.mocoplex.adlib.SubAdlibAdViewCore;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

/*
 AndroidManifest.xml 에 아래 내용을 추가해주세요.

 <receiver android:name="com.co.shallwead.sdk.ShallWeAdReceiver" >
    <intent-filter>
        <action android:name="android.intent.action.BOOT_COMPLETED" />
    </intent-filter>
    <intent-filter>
        <action android:name="android.intent.action.PACKAGE_ADDED" />
        <data android:scheme="package" />
    </intent-filter>
 </receiver>
 <meta-data
    android:name="ShallWeAd_Application_Key"
    android:value="발급받은 ShallWeAd 등록키" />
 <activity
    android:name="com.co.shallwead.sdk.activity.ShallWeAdActivity"
    android:excludeFromRecents="true"
    android:launchMode="singleInstance"
    android:taskAffinity=""
    android:theme="@android:style/Theme.Translucent.NoTitleBar" />
 */

public class SubAdlibAdViewShallWeAd extends SubAdlibAdViewCore {
	
	protected ShallWeAdBanner ad;
	protected boolean bGotAd = false;
	
	public SubAdlibAdViewShallWeAd(Context context) {
		this(context, null);
	}	
	
	public SubAdlibAdViewShallWeAd(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		ad = new ShallWeAdBanner(context);
		ad.setShallWeAdBannerListener(new ShallWeAdBannerListener() {
			@Override
			public void onShowBannerResult(boolean pResult) {
				
				bGotAd = true;
				if(!pResult) {
                    
					// 무료광고를 받아왔으면 다음 플랫폼으로 넘깁니다.
					failed();
				}
				else {
					ad.setShow(View.VISIBLE);
				}
			}
		});
	}

	// 스케줄러에의해 자동으로 호출됩니다.
	// 실제로 광고를 보여주기 위하여 요청합니다.	
	public void query() {
		bGotAd = false;
		
		this.removeAllViews();
		this.addView(ad);
        
		queryAd();
		gotAd();
        
		ad.start();
		
		// 5초 이상 리스너 응답이 없으면 다음 플랫폼으로 넘어갑니다.
		Handler adHandler = new Handler();
		adHandler.postDelayed(new Runnable() {

			@Override
			public void run() {
				if(bGotAd){
					return;
				}else{
					failed();
				}
			}
				
		}, 5000);
	}

	// 광고뷰를 삭제하는 경우 호출됩니다. 
	public void clearAdView() {
		if(ad != null){
			this.removeView(ad);
		}

		super.clearAdView();
	}
	
	public void onResume() {
		super.onResume();
	}
	
	public void onPause() {
		super.onPause();
	}
	
	public void onDestroy() {
		if(ad != null){
			this.removeView(ad);
			ad = null;
		}
		
		super.onDestroy();
	}
}