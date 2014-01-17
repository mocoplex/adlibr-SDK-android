/*
 * adlibr - Library for mobile AD mediation.
 * http://adlibr.com
 * Copyright (c) 2012-2013 Mocoplex, Inc.  All rights reserved.
 * Licensed under the BSD open source license.
 */

/*
 * confirmed compatible with admob SDK 6.4.1
 */

package test.adlib.project.ads;

import com.google.ads.Ad;
import com.google.ads.AdListener;
import com.google.ads.AdRequest;
import com.google.ads.AdRequest.ErrorCode;
import com.google.ads.InterstitialAd;
import com.mocoplex.adlib.AdlibManager;
import com.mocoplex.adlib.SubAdlibAdViewCore;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.Gravity;

public class SubAdlibAdViewAdmob extends SubAdlibAdViewCore  {
	
	protected com.google.ads.AdView ad;
	protected boolean bGotAd = false;
	
	// 여기에 ADMOB ID 를 입력하세요.
	static String admobID = "ADMOB_ID";
    
	public SubAdlibAdViewAdmob(Context context) {
		this(context,null);
	}
	
	public SubAdlibAdViewAdmob(Context context, AttributeSet attrs) {
		
		super(context, attrs);
		
		initAdmobView();
	}
	
	public void initAdmobView()
	{
        // 320x50 사이즈 배너
        ad = new com.google.ads.AdView((Activity) this.getContext(), com.google.ads.AdSize.BANNER, admobID);
        // 디바이스 width에 맞춘 사이즈의 배너
		//ad = new com.google.ads.AdView((Activity) this.getContext(), com.google.ads.AdSize.SMART_BANNER, admobID);
		
		// 광고 뷰의 위치 속성을 제어할 수 있습니다.
		this.setGravity(Gravity.CENTER);
		
		ad.setAdListener( new com.google.ads.AdListener() {

			@Override
			public void onDismissScreen(Ad arg0) {
				
			}

			@Override
			public void onFailedToReceiveAd(Ad arg0, ErrorCode arg1) {
				
				bGotAd = true;
				failed();
			}

			@Override
			public void onLeaveApplication(Ad arg0) {
				
			}

			@Override
			public void onPresentScreen(Ad arg0) {
				
			}

			@Override
			public void onReceiveAd(Ad arg0) {
				
				bGotAd = true;
				queryAd();
				// 광고를 받아왔으면 이를 알려 화면에 표시합니다.
				gotAd();
			}
			
		});
	}
	
	private com.google.ads.AdRequest request = new com.google.ads.AdRequest();
    
	// 스케줄러에의해 자동으로 호출됩니다.
	// 실제로 광고를 보여주기 위하여 요청합니다.
	public void query()
	{
		if(ad == null)
			initAdmobView();
		
        this.removeAllViews();
		this.addView(ad);
		
		ad.loadAd(request);
        
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
                        SubAdlibAdViewAdmob.this.removeView(ad);
                        ad.destroy();
                        ad = null;
                    }
                    bGotAd = false;
				}
			}
            
		}, 3000);
	}
	
	public void onDestroy()
	{
		if(ad != null)
		{
			this.removeView(ad);
			ad.destroy();
			ad = null;
		}
		
		super.onDestroy();
	}
	
	public void clearAdView()
	{
		if(ad != null)
		{
        	this.removeView(ad);
		}
		
        super.clearAdView();
	}
	
	public void onResume()
	{
        super.onResume();
	}
	
	public void onPause()
	{
        super.onPause();
	}
	
	public static void loadInterstitial(Context ctx, final Handler h)
	{
		// Create the interstitial
		final InterstitialAd interstitial = new InterstitialAd((Activity)ctx, admobID);

	    // Create ad request
	    AdRequest adRequest = new AdRequest();

	    // Begin loading your interstitial
	    interstitial.loadAd(adRequest);

	    // Set Ad Listener to use the callbacks below
	    interstitial.setAdListener(new AdListener() {

			@Override
			public void onDismissScreen(Ad arg0) {
				
				if(h != null)
	 			{
	 				h.sendMessage(Message.obtain(h, AdlibManager.INTERSTITIAL_CLOSED, "ADMOB"));
	 			}
			}

			@Override
			public void onFailedToReceiveAd(Ad ad, ErrorCode arg1) {
				
				if(h != null)
	 			{
	 				h.sendMessage(Message.obtain(h, AdlibManager.DID_ERROR, "ADMOB"));
	 			}
			}

			@Override
			public void onLeaveApplication(Ad ad) {
				
			}

			@Override
			public void onPresentScreen(Ad ad) {
				
			}

			@Override
			public void onReceiveAd(Ad ad) {
				
				if(ad == interstitial)
				{
					if(h != null)
		 			{
		 				h.sendMessage(Message.obtain(h, AdlibManager.DID_SUCCEED, "ADMOB"));
		 			}
					
					interstitial.show();
				}
			}
	    	
	    });
	}
}