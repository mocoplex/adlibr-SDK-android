/*
 * adlibr - Library for mobile AD mediation.
 * http://adlibr.com
 * Copyright (c) 2012-2013 Mocoplex, Inc.  All rights reserved.
 * Licensed under the BSD open source license.
 */

/*
 * confirmed compatible with mobfox SDK 5.0.0
*/
package test.adlib.project.ads;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.Gravity;

import com.adsdk.sdk.Ad;
import com.adsdk.sdk.AdListener;
import com.adsdk.sdk.AdManager;
import com.adsdk.sdk.banner.AdView;
import com.mocoplex.adlib.AdlibConfig;
import com.mocoplex.adlib.AdlibManager;
import com.mocoplex.adlib.SubAdlibAdViewCore;

/*
AndroidManifest.xml 에 아래 내용을 추가해주세요.

<activity android:name="com.adsdk.sdk.banner.InAppWebView"
            android:configChanges="keyboard|keyboardHidden|orientation|screenLayout|uiMode|screenSize|smallestScreenSize" />
<activity android:name="com.adsdk.sdk.video.RichMediaActivity"
    android:configChanges="keyboard|keyboardHidden|orientation|screenLayout|uiMode|screenSize|smallestScreenSize"
    android:hardwareAccelerated="false" />
<activity android:name="com.adsdk.sdk.mraid.MraidBrowser"
    android:configChanges="keyboard|keyboardHidden|orientation|screenLayout|uiMode|screenSize|smallestScreenSize" />
<activity android:name="com.google.android.gms.ads.AdActivity"
     android:configChanges="keyboard|keyboardHidden|orientation|screenLayout|uiMode|screenSize|smallestScreenSize"/>
*/

public class SubAdlibAdViewMobfox extends SubAdlibAdViewCore {

	protected boolean bGotAd = false;
	
	protected AdView ad;
	protected static String url = "http://my.mobfox.com/request.php";
	
	// 여기에 MOBFOX ID 를 입력하세요.
	protected String mofoxID = "MOBFOX_ID";
	protected static String mofoxInterstitialID = "MOBFOX_INTERSTITIAL_ID";
    
	protected static Handler intersHandler = null;
	
	public SubAdlibAdViewMobfox(Context context) {
		this(context,null);		
	}	
	
	public SubAdlibAdViewMobfox(Context context, AttributeSet attrs) {
		super(context, attrs);

		initMobfoxView();
	}
	
	public void initMobfoxView() {
		ad = new AdView(getContext(), url, mofoxID, true, true);
		
		ad.setAdspaceWidth(320); //optional, used to set the custom size of banner placement. Without setting it, the SDK will use default sizes.
		ad.setAdspaceHeight(50);  
		ad.setAdspaceStrict(false); //optional, tells the server to only supply banners that are exactly of desired size. Without setting it, the server could also supply smaller ads when no ad of desired size is available.
		
		// 광고 뷰의 위치 속성을 제어할 수 있습니다.
		this.setGravity(Gravity.CENTER);
		
		ad.setAdListener( new AdListener(){

			@Override
			public void adClicked() {
				// 미디에이션 통계 정보
				AdlibConfig.getInstance().bannerClk(SubAdlibAdViewMobfox.this);
			}

			@Override
			public void adClosed(Ad arg0, boolean arg1) {
				// TODO Auto-generated method stub
			}

			@Override
			public void adLoadSucceeded(Ad arg0) {
				bGotAd = true;
				// 광고를 받아왔으면 이를 알려 화면에 표시합니다.
				gotAd();
				
				// 미디에이션 통계 정보
				AdlibConfig.getInstance().bannerImp(SubAdlibAdViewMobfox.this);
			}

			@Override
			public void adShown(Ad arg0, boolean arg1) {
			}

			@Override
			public void noAdFound() {
				bGotAd = true;
				failed();		
			}
			
		});
	}
	
	// 스케줄러에의해 자동으로 호출됩니다.
	// 실제로 광고를 보여주기 위하여 요청합니다.	
	public void query() {
		bGotAd = false;
		if(ad == null)
			initMobfoxView();
		
		this.removeAllViews();
		this.addView(ad);
		
		queryAd();
		ad.loadNextAd();
		
		// 3초 이상 리스너 응답이 없으면 다음 플랫폼으로 넘어갑니다.
		Handler adHandler = new Handler();
		adHandler.postDelayed(new Runnable() {

			@Override
			public void run() {
				if(bGotAd){
					return;
				}else{
					if(ad != null)
						ad.pause();
					failed();
				}
			}
				
		}, 3000);
	}

	// 광고뷰가 사라지는 경우 호출됩니다. 
	public void clearAdView() {
		if(ad != null){
			this.removeView(ad);
			ad.release();
			ad = null;
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
			ad.release();
			ad = null;
		}
		this.removeAllViews();
		
        super.onDestroy();
	}

	
	// 전면광고가 호출되는 경우
	public static void loadInterstitial(Context ctx, final Handler h, final String adlibKey) {
		final AdManager mManager;
		mManager = new AdManager(ctx, url, mofoxInterstitialID, true);
		
		mManager.setInterstitialAdsEnabled(true); //enabled by default. Allows the SDK to request static interstitial ads.
		mManager.setVideoAdsEnabled(true); //disabled by default. Allows the SDK to request video fullscreen ads.
		mManager.setPrioritizeVideoAds(true); //disabled by default. If enabled, indicates that SDK should request video ads first, and only if there is no video request a static interstitial (if they are enabled).
		
		intersHandler = h;
		
		mManager.setListener( new AdListener(){

			@Override
			public void adClicked() {
				// 미디에이션 통계 정보
				AdlibConfig.getInstance().interstitialClk(adlibKey, "MOBFOX");
			}

			@Override
			public void adClosed(Ad arg0, boolean arg1) {
				try{
					if(intersHandler != null){
	 					intersHandler.sendMessage(Message.obtain(intersHandler, AdlibManager.INTERSTITIAL_CLOSED, "MOBFOX"));
	 				}
					
					if(mManager != null){
						mManager.release();
					}
				}catch(Exception e){
				}
			}

			@Override
			public void adLoadSucceeded(Ad arg0) {
				if(mManager != null && mManager.isAdLoaded()){
					try{
						if(intersHandler != null){							
		 					intersHandler.sendMessage(Message.obtain(intersHandler, AdlibManager.DID_SUCCEED, "MOBFOX"));
		 				}
						
						// 미디에이션 통계 정보
						AdlibConfig.getInstance().interstitialImp(adlibKey, "MOBFOX");
						
						mManager.showAd();
					}catch(Exception e){
					}	
				}
			}

			@Override
			public void adShown(Ad arg0, boolean arg1) {
			}

			@Override
			public void noAdFound() {
				try{
					if(intersHandler != null){
	 					intersHandler.sendMessage(Message.obtain(intersHandler, AdlibManager.DID_ERROR, "MOBFOX"));
	 				}
					
					// release.
					mManager.release();
				}catch(Exception e){
				}
							
			}
			
		});
		
		mManager.requestAd();
		
	}

}
