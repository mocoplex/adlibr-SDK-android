/*
 * adlibr - Library for mobile AD mediation.
 * http://adlibr.com
 * Copyright (c) 2012-2013 Mocoplex, Inc.  All rights reserved.
 * Licensed under the BSD open source license.
 */

/*
 * confirmed compatible with Inmobi SDK 4.1.1
 */

package test.adlib.project.ads;

import java.util.Map;

import com.inmobi.commons.InMobi;
import com.inmobi.monetization.IMBanner;
import com.inmobi.monetization.IMBannerListener;
import com.inmobi.monetization.IMErrorCode;
import com.inmobi.monetization.IMInterstitial;
import com.inmobi.monetization.IMInterstitialListener;
import com.mocoplex.adlib.AdlibManager;
import com.mocoplex.adlib.SubAdlibAdViewCore;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;

/*
 AndroidManifest.xml 에 아래 내용을 추가해주세요.

 <activity android:name="com.inmobi.androidsdk.IMBrowserActivity"
 android:configChanges="keyboardHidden|orientation|keyboard|smallestScreenSize|screenSize"
 android:hardwareAccelerated="true" /> 
 */

public class SubAdlibAdViewInmobi extends SubAdlibAdViewCore  {
	
	protected IMBanner ad;
	protected boolean bGotAd = false;

	// 여기에 인모비에서 발급받은 key 를 입력하세요.
	static String inmobiKey = "INMOBI_APP_ID";
	static String inmobiInterstitialKey = "INMOBI_INTERSTITIAL_ID";

	private int getPixels(int dipValue) {
        Resources r = getResources();
        int px = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, 
        r.getDisplayMetrics());
        return px;
	}
	
	public SubAdlibAdViewInmobi(Context context) {
		this(context,null);
	}	
	
	public SubAdlibAdViewInmobi(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		// Insert your InMobi App Id here
		InMobi.initialize((Activity) context, inmobiKey);
	}
	
	public void initInmobiView()
	{
		// 원하는 크기의 배너 크기를 설정하세요.
		ad = new IMBanner((Activity) this.getContext(), inmobiKey, IMBanner.INMOBI_AD_UNIT_320X50);
		ad.disableHardwareAcceleration();
		LayoutParams params = new LayoutParams(getPixels(320),getPixels(50));
		ad.setLayoutParams(params);

		// 광고 뷰의 위치 속성을 제어할 수 있습니다. 
		this.setGravity(Gravity.CENTER);

		// set the listener if the app has to know ad status notifications
		ad.setIMBannerListener(new IMBannerListener() {
			
			@Override
			public void onBannerInteraction(IMBanner arg0, Map<String, String> arg1) {
				
			}

			@Override
			public void onBannerRequestFailed(IMBanner arg0, IMErrorCode arg1) {
				
				bGotAd = true;
				failed();
			}

			@Override
			public void onBannerRequestSucceeded(IMBanner arg0) {
				
				bGotAd = true;
				// 광고를 받아왔으면 이를 알려 화면에 표시합니다.
				gotAd();
			}

			@Override
			public void onDismissBannerScreen(IMBanner arg0) {
				
			}

			@Override
			public void onLeaveApplication(IMBanner arg0) {
				
			}

			@Override
			public void onShowBannerScreen(IMBanner arg0) {
				
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
			initInmobiView();
		
		queryAd();
		
		ad.loadBanner();
		
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
                    if(ad != null)
                    {
                        SubAdlibAdViewInmobi.this.removeView(ad);
                        ad.destroy();
                        ad = null;
                    }
                    bGotAd = false;
				}
			}
		
		}, 3000);
	}
	
	// 광고뷰가 사라지는 경우 호출됩니다.
	public void clearAdView()
	{
		if(ad != null)
		{
			this.removeView(ad);
			ad.destroy();
			ad = null;
		}
		
		super.clearAdView();
	}
	
	public void onDestory()
	{
		if(ad != null)
		{
			this.removeView(ad);
			ad.destroy();
			ad = null;
		}
		
		super.onDestroy();
	}
	
	public void onResume()
	{
		super.onResume();
	}
	
	public void onPause()
	{
		super.onPause();
	}
	
	static Handler intersHandler = null;
	static IMInterstitialListener intersListener = new IMInterstitialListener() {

		@Override
		public void onDismissInterstitialScreen(IMInterstitial arg0) {

			try
			{
	 			// 전면광고 닫혔다.
	 			if(intersHandler != null)
	 			{
	 				intersHandler.sendMessage(Message.obtain(intersHandler, AdlibManager.INTERSTITIAL_CLOSED, "INMOBI"));
	 			} 				   		 					
			}
			catch(Exception e)
			{
				
			}
		}

		@Override
		public void onInterstitialFailed(IMInterstitial arg0, IMErrorCode arg1) {
			
			try
			{
	 			if(intersHandler != null)
	 			{
	 				intersHandler.sendMessage(Message.obtain(intersHandler, AdlibManager.DID_ERROR, "INMOBI"));
	 			}
			}
			catch(Exception e)
			{
				
			}
		}

		@Override
		public void onInterstitialInteraction(IMInterstitial arg0,
				Map<String, String> arg1) {
			
		}

		@Override
		public void onInterstitialLoaded(IMInterstitial ad) {
			
			try
			{
	 			if(intersHandler != null)
	 			{
	 				intersHandler.sendMessage(Message.obtain(intersHandler, AdlibManager.DID_SUCCEED, "INMOBI"));
	 			}
			}
			catch(Exception e)
			{
				
			}
			ad.show();
		}

		@Override
		public void onLeaveApplication(IMInterstitial arg0) {
			
		}

		@Override
		public void onShowInterstitialScreen(IMInterstitial arg0) {
			
		}
    };
    
	public static void loadInterstitial(Context ctx, final Handler h)
	{
		InMobi.initialize((Activity) ctx, inmobiInterstitialKey);
		
		final IMInterstitial interstitial = new IMInterstitial((Activity) ctx, inmobiInterstitialKey);
		interstitial.loadInterstitial();
		intersHandler = h;
		interstitial.setIMInterstitialListener(intersListener);
	}
}
