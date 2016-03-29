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
import android.view.Gravity;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.mocoplex.adlib.AdlibConfig;
import com.mocoplex.adlib.AdlibManager;
import com.mocoplex.adlib.SubAdlibAdViewCore;

/*
AndroidManifest.xml 에 아래 내용을 추가해주세요.

 <meta-data android:name="com.google.android.gms.version"
	android:value="@integer/google_play_services_version"/>
 <activity android:name="com.google.android.gms.ads.AdActivity"
	android:configChanges="keyboard|keyboardHidden|orientation|screenLayout|uiMode|screenSize|smallestScreenSize"/>
*/

public class SubAdlibAdViewAdmob extends SubAdlibAdViewCore {
	
	protected AdView ad;
	protected boolean bGotAd = false;
	
	// 여기에 ADMOB ID 를 입력하세요. (여기서 ADMOB ID는 AD_UNIT_ID, 광고 단위 ID를 지칭합니다.)
	protected String admobID = "ADMOB_ID";
	protected static String admobInterstitialID = "ADMOB_Interstitial_ID";
    
    private AdRequest request = new AdRequest.Builder().build();
    
    public SubAdlibAdViewAdmob(Context context) {
		this(context,null);
	}
	
	public SubAdlibAdViewAdmob(Context context, AttributeSet attrs) {
		
		super(context, attrs);
		
		initAdmobView();
	}
	
	public void initAdmobView() {
		ad = new AdView(getContext());
		ad.setAdUnitId(admobID);
		ad.setAdSize(AdSize.SMART_BANNER);
		
		// 광고 뷰의 위치 속성을 제어할 수 있습니다.
		this.setGravity(Gravity.CENTER);
		
		ad.setAdListener( new AdListener() {

			@Override
			public void onAdFailedToLoad(int errorCode) {
				
				bGotAd = true;
				failed();
			}

			@Override
			public void onAdLoaded() {
				
				bGotAd = true;
				queryAd();
				// 광고를 받아왔으면 이를 알려 화면에 표시합니다.
				gotAd();
				
				// 미디에이션 통계 정보
				AdlibConfig.getInstance().bannerImp(SubAdlibAdViewAdmob.this);
			}
			
			@Override
			public void onAdOpened() {
			}
			
			@Override
			public void onAdLeftApplication() {
				// 미디에이션 통계 정보
				AdlibConfig.getInstance().bannerClk(SubAdlibAdViewAdmob.this);
			}
		});
	}
	
	// 스케줄러에의해 자동으로 호출됩니다.
	// 실제로 광고를 보여주기 위하여 요청합니다.
	public void query() {
		if(ad == null)
			initAdmobView();
		
        this.removeAllViews();
		this.addView(ad);
		
		ad.loadAd(request);
        
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
                    	SubAdlibAdViewAdmob.this.removeView(ad);
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
		// Create the interstitial
		final InterstitialAd interstitial = new InterstitialAd(ctx);
		interstitial.setAdUnitId(admobInterstitialID);

	    // Create ad request
	    AdRequest adRequest = new AdRequest.Builder().build();

	    // Begin loading your interstitial
	    interstitial.loadAd(adRequest);

	    // Set Ad Listener to use the callbacks below
	    interstitial.setAdListener(new AdListener() {
	    	
			@Override
			public void onAdClosed() {
				try{
					if(h != null){
		 				h.sendMessage(Message.obtain(h, AdlibManager.INTERSTITIAL_CLOSED, "ADMOB"));
		 			}
				}catch(Exception e){
				}
			}

			@Override
			public void onAdFailedToLoad(int errorCode) {
				try{
					if(h != null){
		 				h.sendMessage(Message.obtain(h, AdlibManager.DID_ERROR, "ADMOB"));
		 			}
				}catch(Exception e){
				}
			}

			@Override
			public void onAdLoaded() {
				try{
					if(interstitial.isLoaded()){
						if(h != null){
			 				h.sendMessage(Message.obtain(h, AdlibManager.DID_SUCCEED, "ADMOB"));
			 			}
						
						// 미디에이션 통계 정보
						AdlibConfig.getInstance().interstitialImp(adlibKey, "ADMOB");
						
						interstitial.show();
					}
				}catch(Exception e){
				}
			}
			
			@Override
			public void onAdOpened() {
			}
			
			@Override
			public void onAdLeftApplication() {
				try{
					// 미디에이션 통계 정보
					AdlibConfig.getInstance().interstitialClk(adlibKey, "ADMOB");
				}catch(Exception e){
				}
			}
	    	
	    });
	}
}