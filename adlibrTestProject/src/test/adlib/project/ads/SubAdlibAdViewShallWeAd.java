/*
 * adlibr - Library for mobile AD mediation.
 * http://adlibr.com
 * Copyright (c) 2012 Mocoplex, Inc.  All rights reserved.
 * Licensed under the BSD open source license.
 */

package test.adlib.project.ads;

import com.jm.co.shallwead.sdk.ShallWeAdBanner;
import com.jm.co.shallwead.sdk.ShallWeAdBannerListener;
import com.mocoplex.adlib.SubAdlibAdViewCore;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;

/*
 AndroidManifest.xml 에 아래 내용을 추가해주세요.

		<activity
			android:name="com.jm.co.shallwead.sdk.ShallWeAdActivity"
			android:configChanges="orientation|keyboard|keyboardHidden" />
		<meta-data
			android:name="ShallWeAd_AppKey"
			android:value="" />
 */

public class SubAdlibAdViewShallWeAd extends SubAdlibAdViewCore  {
	
	protected ShallWeAdBanner ad;
	protected boolean bGotAd = false;
	
	public SubAdlibAdViewShallWeAd(Context context) {
		this(context, null);
	}	
	
	public SubAdlibAdViewShallWeAd(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		ad = new ShallWeAdBanner(context);
		ad.setBannerListener(new ShallWeAdBannerListener() {
			@Override
			public void onShowBannerResult(boolean pResult) {
				Log.d("ShallWeAd_TestApp", "ShallWeAd_TestAppActivity_Java onShowBannerResult "+pResult);
				if(pResult) {
					gotAd();
					bGotAd = true;
				} else {
					failed();
				}
			}
		});
				
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		ad.setLayoutParams(params);

		this.addView(ad);
	}

	// 스케줄러에의해 자동으로 호출됩니다.
	// 실제로 광고를 보여주기 위하여 요청합니다.	
	public void query() {
		if(bGotAd)
			gotAd();		
	}

	// 광고뷰를 삭제하는 경우 호출됩니다. 
	public void clearAdView()
	{
		if(ad != null) {
			ad = null;			
		}

		super.clearAdView();
	}
	
	public void onResume()
	{
		super.onResume();
		
		if(ad != null) {
		}
	}
	public void onPause()
	{
		super.onPause();
		
		if(ad != null) {
		}		
	}
}
