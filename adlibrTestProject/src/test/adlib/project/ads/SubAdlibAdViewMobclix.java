/*
 * adlibr - Library for mobile AD mediation.
 * http://adlibr.com
 * Copyright (c) 2012-2013 Mocoplex, Inc.  All rights reserved.
 * Licensed under the BSD open source license.
 */

/*
 * confirmed compatible with mobclix SDK 4.1.0
 */

package test.adlib.project.ads;

import com.mobclix.android.sdk.MobclixAdView;
import com.mobclix.android.sdk.MobclixAdViewListener;
import com.mobclix.android.sdk.MobclixMMABannerXLAdView;
import com.mocoplex.adlib.SubAdlibAdViewCore;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.Gravity;

/*
 AndroidManifest.xml 에 아래 내용을 추가해주세요.

 <meta-data android:name="com.mobclix.APPLICATION_ID"
	android:value="insert-your-application-key" />
 <activity android:name="com.mobclix.android.sdk.MobclixBrowserActivity"
	android:theme="@android:style/Theme.Translucent.NoTitleBar"
	android:hardwareAccelerated="true" />
 */

public class SubAdlibAdViewMobclix extends SubAdlibAdViewCore  {
	
	protected MobclixAdView ad;
	protected boolean bGotAd = false;

	public SubAdlibAdViewMobclix(Context context) {
		this(context,null);
	}	
	
	public SubAdlibAdViewMobclix(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		initMobclixView();
	}
	
	public void initMobclixView()
	{
		ad = new MobclixMMABannerXLAdView(getContext());
		
		ad.addMobclixAdViewListener(new MobclixAdViewListener() {

			@Override
			public String keywords() {
				return null;
			}

			@Override
			public void onAdClick(MobclixAdView arg0) {
				
			}

			@Override
			public void onCustomAdTouchThrough(MobclixAdView arg0, String arg1) {
				
			}

			@Override
			public void onFailedLoad(MobclixAdView arg0, int arg1) {
				
				bGotAd = true;
				failed();
			}

			@Override
			public boolean onOpenAllocationLoad(MobclixAdView arg0, int arg1) {
				return false;
			}

			@Override
			public void onSuccessfulLoad(MobclixAdView arg0) {
				
				bGotAd = true;
				// 광고를 받아왔으면 이를 알려 화면에 표시합니다.
				gotAd();
			}

			@Override
			public String query() {
				return null;
			}
			
		});
		
		// 광고 뷰의 위치 속성을 제어할 수 있습니다.
		this.setGravity(Gravity.CENTER);

		this.addView(ad);
	}
	
	// 스케줄러에의해 자동으로 호출됩니다.
	// 실제로 광고를 보여주기 위하여 요청합니다.	
	public void query()
	{
		bGotAd = false;
		
		if(ad == null)
			initMobclixView();
		
		queryAd();
		
		ad.getAd();
		
		// 3초 이상 리스너 응답이 없으면 다음 플랫폼으로 넘어갑니다.
		Handler adHandler = new Handler();
		adHandler.postDelayed(new Runnable() {

			@Override
			public void run() {
				if(bGotAd)
					return;
				else
				{
					failed();
				}
			}
		
		}, 3000);
	}

	// 광고뷰가 사라지는 경우 호출됩니다. 
	public void clearAdView()
	{
		if(ad != null)
		{
			ad.pause();
			this.removeView(ad);
			ad = null;
		}
		
		super.clearAdView();
	}
	
	public void onResume()
	{
		if(ad != null)
		{
			ad.getAd();
		}
		
        super.onResume();
	}
	
	public void onPause()
	{
		if(ad != null)
		{
			ad.pause();
		}
		
        super.onPause();
	}
	
	public void onDestroy()
	{
		if(ad != null)
		{
			ad.pause();
			this.removeView(ad);
			ad = null;
		}
        
        super.onDestroy();
	}
}