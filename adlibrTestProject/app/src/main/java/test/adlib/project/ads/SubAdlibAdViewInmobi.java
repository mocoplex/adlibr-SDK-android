/*
 * adlibr - Library for mobile AD mediation.
 * http://adlibr.com
 * Copyright (c) 2012-2013 Mocoplex, Inc.  All rights reserved.
 * Licensed under the BSD open source license.
 */

/*
 * confirmed compatible with Inmobi SDK 5.1.1
 */

package test.adlib.project.ads;

import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;

import com.inmobi.ads.InMobiAdRequestStatus;
import com.inmobi.ads.InMobiBanner;
import com.inmobi.ads.InMobiBanner.BannerAdListener;
import com.inmobi.ads.InMobiInterstitial;
import com.inmobi.ads.InMobiInterstitial.InterstitialAdListener;
import com.inmobi.sdk.InMobiSdk;
import com.mocoplex.adlib.AdlibConfig;
import com.mocoplex.adlib.AdlibManager;
import com.mocoplex.adlib.SubAdlibAdViewCore;

/*
 AndroidManifest.xml 에 아래 내용을 추가해주세요.

 <activity android:name="com.inmobi.rendering.InMobiAdActivity"
	android:configChanges="keyboardHidden|orientation|keyboard|smallestScreenSize|screenSize"
	android:theme="@android:style/Theme.Translucent.NoTitleBar"
	android:hardwareAccelerated="true" />
*/

public class SubAdlibAdViewInmobi extends SubAdlibAdViewCore {
	
	protected InMobiBanner ad;
	protected boolean bGotAd = false;

	// 여기에 인모비에서 발급받은 key 를 입력하세요.
	protected static String inmobiKey = "INMOBI_ID";
    protected static String inmobiInterstitialKey = "INMOBI_INTERSTITIAL_ID";
	// 여기에 인모비에서 발급받은 Placement Id 를 입력하세요.
	protected static long inmobiPlacementId = 0L;
	protected static long inmobiInterstitialPlacementId = 0L;
	
	protected static Handler intersHandler = null;

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
		InMobiSdk.init(context, inmobiKey);
	}
	
	public void initInmobiView() {
		// 원하는 크기의 배너 크기를 설정하세요.
		ad = new InMobiBanner((Activity)this.getContext(), inmobiPlacementId);
		ad.disableHardwareAcceleration();
		LayoutParams params = new LayoutParams(getPixels(320),getPixels(50));
		ad.setLayoutParams(params);

		// 광고 뷰의 위치 속성을 제어할 수 있습니다. 
		this.setGravity(Gravity.CENTER);

		// set the listener if the app has to know ad status notifications
		ad.setListener(new BannerAdListener() {
			
			@Override
			public void onAdDismissed(InMobiBanner arg0) {
				
			}

			@Override
			public void onAdDisplayed(InMobiBanner arg0) {
				
			}

			@Override
			public void onAdInteraction(InMobiBanner arg0, Map<Object, Object> arg1) {
				
				// 미디에이션 통계 정보
				AdlibConfig.getInstance().bannerClk(SubAdlibAdViewInmobi.this);
			}

			@Override
			public void onAdLoadFailed(InMobiBanner arg0, InMobiAdRequestStatus arg1) {
				
				bGotAd = true;
				failed();
			}

			@Override
			public void onAdLoadSucceeded(InMobiBanner arg0) {
				
				bGotAd = true;
				// 광고를 받아왔으면 이를 알려 화면에 표시합니다.
				gotAd();
				
				// 미디에이션 통계 정보
				AdlibConfig.getInstance().bannerImp(SubAdlibAdViewInmobi.this);
			}

			@Override
			public void onAdRewardActionCompleted(InMobiBanner arg0, Map<Object, Object> arg1) {
				
			}

			@Override
			public void onUserLeftApplication(InMobiBanner arg0) {
				
			}
		});
		
		this.addView(ad);
	}
	
	// 스케줄러에의해 자동으로 호출됩니다.
	// 실제로 광고를 보여주기 위하여 요청합니다.	
	public void query() {
		bGotAd = false;
		
		if(ad == null)
			initInmobiView();
		
		queryAd();
		
		ad.load();
		
		// 3초 이상 리스너 응답이 없으면 다음 플랫폼으로 넘어갑니다.
		Handler adHandler = new Handler();
		adHandler.postDelayed(new Runnable() {

			@Override
			public void run() {
				if(bGotAd){
					return;
				}else{
					failed();
                    if(ad != null){
                        SubAdlibAdViewInmobi.this.removeView(ad);
                        ad = null;
                    }
                    bGotAd = false;
				}
			}
		
		}, 3000);
	}
	
	// 광고뷰가 사라지는 경우 호출됩니다.
	public void clearAdView() {
		if(ad != null){
			this.removeView(ad);
			ad = null;
		}
		
		super.clearAdView();
	}
	
	public void onDestory() {
		if(ad != null){
			this.removeView(ad);
			ad = null;
		}
		
		super.onDestroy();
	}
	
	public void onResume() {
		super.onResume();
	}
	
	public void onPause() {
		super.onPause();
	}
	
	public static void loadInterstitial(Context ctx, final Handler h, final String adlibKey) {
		InMobiSdk.init(ctx, inmobiInterstitialKey);
		
		InterstitialAdListener intersListener = new InterstitialAdListener() {

			@Override
			public void onAdDismissed(InMobiInterstitial ad) {
				
				try{
		 			// 전면광고 닫혔다.
		 			if(intersHandler != null){
		 				intersHandler.sendMessage(Message.obtain(intersHandler, AdlibManager.INTERSTITIAL_CLOSED, "INMOBI"));
		 			} 				   		 					
				}catch(Exception e){
				}
			}

			@Override
			public void onAdDisplayed(InMobiInterstitial ad) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onAdInteraction(InMobiInterstitial ad, Map<Object, Object> arg1) {
				
				// 미디에이션 통계 정보
				AdlibConfig.getInstance().interstitialClk(adlibKey, "INMOBI");
			}

			@Override
			public void onAdLoadFailed(InMobiInterstitial ad, InMobiAdRequestStatus arg1) {
				
				try{
		 			if(intersHandler != null){
		 				intersHandler.sendMessage(Message.obtain(intersHandler, AdlibManager.DID_ERROR, "INMOBI"));
		 			}
				}catch(Exception e){
				}
			}

			@Override
			public void onAdLoadSucceeded(InMobiInterstitial ad) {
				
				try{
		 			if(intersHandler != null){
		 				intersHandler.sendMessage(Message.obtain(intersHandler, AdlibManager.DID_SUCCEED, "INMOBI"));
		 			}
		 			
		 			// 미디에이션 통계 정보
		 			AdlibConfig.getInstance().interstitialImp(adlibKey, "INMOBI");
		 			
		 			if(ad.isReady()){
		 				ad.show();
		 			}
				}catch(Exception e){
				}
			}

			@Override
			public void onAdRewardActionCompleted(InMobiInterstitial ad, Map<Object, Object> arg1) {
				
			}

			@Override
			public void onUserLeftApplication(InMobiInterstitial ad) {
				
			}
	    };
		
		final InMobiInterstitial interstitial = new InMobiInterstitial((Activity) ctx, inmobiInterstitialPlacementId, intersListener);
		interstitial.load();
		intersHandler = h;
	}
}
