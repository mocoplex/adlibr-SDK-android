/*
 * adlibr - Library for mobile AD mediation.
 * http://adlibr.com
 * Copyright (c) 2012-2013 Mocoplex, Inc.  All rights reserved.
 * Licensed under the BSD open source license.
 */

package test.adlib.project.ads;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdListener;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;
import com.mocoplex.adlib.AdlibConfig;
import com.mocoplex.adlib.AdlibManager;
import com.mocoplex.adlib.SubAdlibAdViewCore;

/*
AndroidManifest.xml 에 아래 내용을 추가해주세요.

 <activity android:name="com.facebook.ads.InterstitialAdActivity"
	android:configChanges="keyboardHidden|orientation|screenSize" />
*/

public class SubAdlibAdViewFacebook extends SubAdlibAdViewCore {
	
	protected AdView ad;
	protected boolean bGotAd = false;
	
	// 여기에 FACEBOOK ID 를 입력하세요. (여기서 FACEBOOK ID는 Audience Network의 Placement ID를 지칭합니다.)
	protected String facebookID = "FACEBOOK_ID";
	protected static String facebookInterstitialID = "FACEBOOK_Interstitial_ID";
    
    public SubAdlibAdViewFacebook(Context context) {
		this(context,null);
	}
	
	public SubAdlibAdViewFacebook(Context context, AttributeSet attrs) {
		
		super(context, attrs);
		
		initFacebookView();
	}
	
	public void initFacebookView() {
		ad = new AdView(getContext(), facebookID, AdSize.BANNER_HEIGHT_50);
		ad.setAdListener( new AdListener() {

			@Override
			public void onAdClicked(Ad arg0) {
				// 미디에이션 통계 정보
				AdlibConfig.getInstance().bannerClk(SubAdlibAdViewFacebook.this);
			}

			@Override
			public void onAdLoaded(Ad arg0) {
				bGotAd = true;
				queryAd();
				// 광고를 받아왔으면 이를 알려 화면에 표시합니다.
				gotAd();
				
				// 미디에이션 통계 정보
				AdlibConfig.getInstance().bannerImp(SubAdlibAdViewFacebook.this);
			}

			@Override
			public void onError(Ad arg0, AdError arg1) {
				bGotAd = true;
				failed();
			}
		});
	}
	
	// 스케줄러에의해 자동으로 호출됩니다.
	// 실제로 광고를 보여주기 위하여 요청합니다.
	public void query() {
		if(ad == null)
			initFacebookView();
		
        this.removeAllViews();
		this.addView(ad);
		
		ad.loadAd();
        
        // 5초 이상 리스너 응답이 없으면 다음 플랫폼으로 넘어갑니다.
		Handler adHandler = new Handler();
		adHandler.postDelayed(new Runnable() {
            
			@Override
			public void run() {
				if(bGotAd){
					return;
				}else{
					failed();
					if(ad != null) {
                    	SubAdlibAdViewFacebook.this.removeView(ad);
                    	ad.destroy();
                    	ad = null;
                    }
                    bGotAd = false;
				}
			}
            
		}, 5000);
	}
	
	public void onDestroy() {
		if(ad != null){
			this.removeView(ad);
			ad.destroy();
			ad = null;
		}
		
		super.onDestroy();
	}
	
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
	
	public static void loadInterstitial(Context ctx, final Handler h, final String adlibKey) {
		final InterstitialAd interstitialAd = new InterstitialAd(ctx, facebookInterstitialID);

		// Set a listener to get notified on changes or when the user interact with the ad.
        interstitialAd.setAdListener(new InterstitialAdListener() {

			@Override
			public void onAdClicked(Ad ad) {
				try{
					// 미디에이션 통계 정보
					AdlibConfig.getInstance().interstitialClk(adlibKey, "FACEBOOK");
				}catch(Exception e){
				}
			}

			@Override
			public void onAdLoaded(Ad ad) {
				try{
					if (interstitialAd != null && interstitialAd.isAdLoaded()) {
						if(h != null){
			 				h.sendMessage(Message.obtain(h, AdlibManager.DID_SUCCEED, "FACEBOOK"));
			 			}
						
						// 미디에이션 통계 정보
						AdlibConfig.getInstance().interstitialImp(adlibKey, "FACEBOOK");
						
						interstitialAd.show();
					}
				}catch(Exception e){
				}
			}

			@Override
			public void onError(Ad ad, AdError error) {
				try{
					if(h != null){
		 				h.sendMessage(Message.obtain(h, AdlibManager.DID_ERROR, "FACEBOOK"));
		 			}
				}catch(Exception e){
				}
			}

			@Override
			public void onInterstitialDismissed(Ad ad) {
				try{
					if(h != null){
		 				h.sendMessage(Message.obtain(h, AdlibManager.INTERSTITIAL_CLOSED, "FACEBOOK"));
		 			}
				}catch(Exception e){
				}
			}

			@Override
			public void onInterstitialDisplayed(Ad ad) {
				
			}
        });
        interstitialAd.loadAd();
	}
}