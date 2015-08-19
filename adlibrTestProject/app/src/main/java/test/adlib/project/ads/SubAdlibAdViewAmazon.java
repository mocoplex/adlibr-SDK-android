/*
 * adlibr - Library for mobile AD mediation.
 * http://adlibr.com
 * Copyright (c) 2012-2013 Mocoplex, Inc.  All rights reserved.
 * Licensed under the BSD open source license.
 */

/*
 * confirmed compatible with amazon mobile ads SDK 5.6.20
 */

package test.adlib.project.ads;

import com.amazon.device.ads.Ad;
import com.amazon.device.ads.AdError;
import com.amazon.device.ads.AdLayout;
import com.amazon.device.ads.AdListener;
import com.amazon.device.ads.AdProperties;
import com.amazon.device.ads.AdRegistration;
import com.amazon.device.ads.AdSize;
import com.amazon.device.ads.AdTargetingOptions;
import com.mocoplex.adlib.SubAdlibAdViewCore;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.Gravity;

/*
 AndroidManifest.xml 에 아래 내용을 추가해주세요.
 
 <activity 
	android:name="com.amazon.device.ads.AdActivity"
	android:configChanges="keyboardHidden|orientation|screenSize"/>
 */
public class SubAdlibAdViewAmazon extends SubAdlibAdViewCore {
	
	protected AdLayout ad;
	protected boolean bGotAd = false;
	
	// 여기에 AMAZON ID 를 입력하세요.
	protected String amazonID = "AMAZON_ID";
	
	private AdTargetingOptions adOptions = new AdTargetingOptions();
    
	public SubAdlibAdViewAmazon(Context context) {
		this(context,null);
	}
	
	public SubAdlibAdViewAmazon(Context context, AttributeSet attrs) {
		
		super(context, attrs);
		
		initAmazonView();
	}
	
	public void initAmazonView() {
		// For debugging purposes enable logging, but disable for production builds
        AdRegistration.enableLogging(false);
        // For debugging purposes flag all ad requests as tests, but set to false for production builds
        AdRegistration.enableTesting(false);
        
		ad = new AdLayout((Activity) this.getContext(), AdSize.SIZE_320x50);
		ad.setListener(new AdListener() {

			@Override
			public void onAdCollapsed(Ad arg0) {
				
			}

			@Override
			public void onAdDismissed(Ad arg0) {
				
			}

			@Override
			public void onAdExpanded(Ad arg0) {
				
			}

			@Override
			public void onAdFailedToLoad(Ad arg0, AdError arg1) {
				
				bGotAd = true;
				failed();
			}

			@Override
			public void onAdLoaded(Ad arg0, AdProperties arg1) {
				
				bGotAd = true;
				queryAd();
				// 광고를 받아왔으면 이를 알려 화면에 표시합니다.
				gotAd();
			}
        });
        
        try {
            AdRegistration.setAppKey(amazonID);
        } catch (Exception e) {
        	failed();
            return;
        }
		
		// 광고 뷰의 위치 속성을 제어할 수 있습니다.
		this.setGravity(Gravity.CENTER);
	}
	
	// 스케줄러에의해 자동으로 호출됩니다.
	// 실제로 광고를 보여주기 위하여 요청합니다.
	public void query() {
		if(ad == null)
			initAmazonView();
		
        this.removeAllViews();
		this.addView(ad);
		
        ad.loadAd(adOptions);
        
        // 3초 이상 리스너 응답이 없으면 다음 플랫폼으로 넘어갑니다.
		Handler adHandler = new Handler();
		adHandler.postDelayed(new Runnable() {
            
			@Override
			public void run() {
				if(bGotAd)
					return;
				else{
					failed();
					if(ad != null){
						SubAdlibAdViewAmazon.this.removeView(ad);
						ad.destroy();
						ad = null;
					}
                    bGotAd = false;
				}
			}
            
		}, 3000);
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
	
	public void onResume(){
        super.onResume();
	}
	
	public void onPause(){
        super.onPause();
	}
}