/*
 * adlibr - Library for mobile AD mediation.
 * http://adlibr.com
 * Copyright (c) 2012-2013 Mocoplex, Inc.  All rights reserved.
 * Licensed under the BSD open source license.
 */

/*
 * confirmed compatible with AdHub SDK 2.5.4
 */

package test.adlib.project.ads;

import com.mocoplex.adlib.AdlibManager;
import com.mocoplex.adlib.SubAdlibAdViewCore;
import com.sec.android.ad.AdHubInterstitial;
import com.sec.android.ad.AdHubView;
import com.sec.android.ad.AdInterstitialListener;
import com.sec.android.ad.AdNotificationListener;
import com.sec.android.ad.info.AdSize;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.Gravity;

/*
 AndroidManifest.xml 에 아래 내용을 추가해주세요

￼ <activity 
	android:name="com.sec.android.ad.AdActivity"
	android:configChanges="keyboardHidden|orientation|screenSize" />
 */
public class SubAdlibAdViewAdHub extends SubAdlibAdViewCore  {
	
	protected AdHubView ad;
	protected boolean bGotAd = false;
	
	// 여기에 ADHUB ID 를 입력하세요. 아래는 테스트키 입니다.
	static String adhubID = "xv0c00000001ak";
	static String adhubInterstitialID = "xv0c00000001aw";
    
	public SubAdlibAdViewAdHub(Context context) {
		this(context,null);
	}
	
	public SubAdlibAdViewAdHub(Context context, AttributeSet attrs) {
		
		super(context, attrs);
		
		initAdHubView();
	}
	
	public void initAdHubView()
	{
		ad = new AdHubView(getContext());
		ad.init(getContext(), adhubID, AdSize.BANNER);
		// 최소 15sec
		ad.setRefreshRate(15*1000);
		
		// 광고 뷰의 위치 속성을 제어할 수 있습니다.
		this.setGravity(Gravity.CENTER);
		
		ad.setListener( new AdNotificationListener() {
			
			@Override
			public void onAdFailed(AdHubView arg0, Exception arg1) {
				
				bGotAd = true;
				failed();
			}

			@Override
			public void onAdReceived(AdHubView arg0) {
				
				bGotAd = true;
				queryAd();
				// 광고를 받아왔으면 이를 알려 화면에 표시합니다.
				gotAd();
			}
			
		});
		
		this.addView(ad);
	}
	
	// 스케줄러에의해 자동으로 호출됩니다.
	// 실제로 광고를 보여주기 위하여 요청합니다.
	public void query()
	{
		bGotAd = false;
		
        if(ad == null)
        	initAdHubView();
        
        queryAd();
		
		ad.startAd();
		
		// 3초 이상 리스너 응답이 없으면 다음 플랫폼으로 넘어갑니다.
		Handler adHandler = new Handler();
		adHandler.postDelayed(new Runnable() {
	            
			@Override
			public void run() {
				if(bGotAd)
					return;
				else
				{
					if(ad != null)
					{
						SubAdlibAdViewAdHub.this.removeView(ad);
						ad.stopAd();
						ad = null;
					}
					failed();
				}
			}
	            
		}, 3000);
	}
	
	public void onDestroy()
	{
		if(ad != null)
		{
			this.removeView(ad);
			ad.removeAllViews();
			ad.stopAd();
			ad = null;
		}
		
		super.onDestroy();
	}
	
	public void clearAdView()
	{
		if(ad != null)
		{
			this.removeView(ad);
			ad.removeAllViews();
			ad.stopAd();
			ad = null;
		}
		
        super.clearAdView();
	}
	
	public void onResume()
	{
		if(ad != null)
		{
        	ad.startAd();
		}
		
        super.onResume();
	}
	
	public void onPause()
	{
		if(ad != null)
		{
        	ad.stopAd();
		}
		
        super.onPause();
	}
	
	public static void loadInterstitial(Context ctx, final Handler h)
	{
		final AdHubInterstitial interstitial = new AdHubInterstitial((Activity)ctx, adhubInterstitialID);

	    interstitial.setListener(new AdInterstitialListener() {

			@Override
			public void onAdInterstitialClosed(AdHubInterstitial ad) {
				
				if(h != null)
	 			{
	 				h.sendMessage(Message.obtain(h, AdlibManager.INTERSTITIAL_CLOSED, "ADHUB"));
	 			}
			}

			@Override
			public void onAdInterstitialFailed(AdHubInterstitial ad, Exception e) {
				
				if(h != null)
	 			{
	 				h.sendMessage(Message.obtain(h, AdlibManager.DID_ERROR, "ADHUB"));
	 			}
			}

			@Override
			public void onAdInterstitialReceived(AdHubInterstitial ad) {
				
				if(h != null)
	 			{
	 				h.sendMessage(Message.obtain(h, AdlibManager.DID_SUCCEED, "ADHUB"));
	 			}
			}
	    	
	    });
	    
	    interstitial.startAd();
	}
}