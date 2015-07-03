/*
 * adlibr - Library for mobile AD mediation.
 * http://adlibr.com
 * Copyright (c) 2012-2013 Mocoplex, Inc.  All rights reserved.
 * Licensed under the BSD open source license.
 */

/*
 * confirmed compatible with mopub SDK 3.8.0
*/

package test.adlib.project.ads;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.LinearLayout;

import com.mocoplex.adlib.AdlibConfig;
import com.mocoplex.adlib.AdlibManager;
import com.mocoplex.adlib.SubAdlibAdViewCore;
import com.mopub.mobileads.MoPubErrorCode;
import com.mopub.mobileads.MoPubInterstitial;
import com.mopub.mobileads.MoPubView;

/*
 AndroidManifest.xml 에 아래 내용을 추가해주세요.
 
<activity android:name="com.mopub.mobileads.MoPubActivity" android:configChanges="keyboardHidden|orientation"/>
<activity android:name="com.mopub.mobileads.MraidActivity" android:configChanges="keyboardHidden|orientation"/>
<activity android:name="com.mopub.common.MoPubBrowser" android:configChanges="keyboardHidden|orientation"/>
<activity android:name="com.mopub.mobileads.MraidVideoPlayerActivity" android:configChanges="keyboardHidden|orientation"/>
		 
 */

public class SubAdlibAdViewMopub extends SubAdlibAdViewCore {

	protected boolean bGotAd = false;
	protected MoPubView ad;
	
	// 여기에 MOPUB ID 를 입력하세요.
	protected String mopubID = "MOPUB_ID";
	protected static String mopubInterstitialID = "MOPUB_INTERSTITIAL_ID";
	
	protected static Handler intersHandler = null;
	
	protected void initMobpubView() {
		ad = new MoPubView(getContext());
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		ad.setLayoutParams(params);
		
		ad.setAdUnitId(mopubID); // Enter your Ad Unit ID from www.mopub.com
		
		// 광고 뷰의 위치 속성을 제어할 수 있습니다.
		this.setGravity(Gravity.CENTER);
		
		ad.setBannerAdListener(new com.mopub.mobileads.MoPubView.BannerAdListener(){

			@Override
			public void onBannerLoaded(MoPubView banner) {
				queryAd();
				bGotAd = true;
				// 광고를 받아왔으면 이를 알려 화면에 표시합니다.
				gotAd();
				
				// 미디에이션 통계 정보
				AdlibConfig.getInstance().bannerImp(SubAdlibAdViewMopub.this);
			}
			
			@Override
			public void onBannerFailed(MoPubView arg0, MoPubErrorCode arg1) {
				bGotAd = true;
				failed();
			}
			
			@Override
			public void onBannerClicked(MoPubView banner) {
				// 미디에이션 통계 정보
				AdlibConfig.getInstance().bannerClk(SubAdlibAdViewMopub.this);
			}

			@Override
			public void onBannerExpanded(MoPubView banner) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onBannerCollapsed(MoPubView banner) {
				// TODO Auto-generated method stub
				
			}
		});		
	}

	public SubAdlibAdViewMopub(Context context) {
		this(context,null);		
	}	
	
	public SubAdlibAdViewMopub(Context context, AttributeSet attrs) {
		super(context, attrs);

		initMobpubView();
	}
	
	// 스케줄러에의해 자동으로 호출됩니다.
	// 실제로 광고를 보여주기 위하여 요청합니다.
	public void query() {
		bGotAd = false;
		if(ad == null)
			initMobpubView();
		
		this.removeAllViews();
		this.addView(ad);
		
		ad.loadAd();
		
		// 3초 이상 리스너 응답이 없으면 다음 플랫폼으로 넘어갑니다.
		Handler adHandler = new Handler();
		adHandler.postDelayed(new Runnable() {

			@Override
			public void run() {
				if(bGotAd){
					return;
				}else{
					failed();
				}
			}
				
		}, 3000);
	}

	// 광고뷰가 사라지는 경우 호출됩니다. 
	public void clearAdView() {
		if(ad != null){
			this.removeView(ad);
			ad.destroy();
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
			ad.destroy();
			ad = null;
		}
		
		this.removeAllViews();
		
        super.onDestroy();
	}

	// 전면광고가 호출되는 경우
	public static void loadInterstitial(Context ctx, final Handler h, final String adlibKey) {
		final MoPubInterstitial mInterstitial = new MoPubInterstitial((Activity) ctx, mopubInterstitialID);
		
		intersHandler = h;
		
	    mInterstitial.setInterstitialAdListener( new com.mopub.mobileads.MoPubInterstitial.InterstitialAdListener() {

			@Override
			public void onInterstitialLoaded(MoPubInterstitial interstitial) {
				try{
					if(intersHandler != null){							
	 					intersHandler.sendMessage(Message.obtain(intersHandler, AdlibManager.DID_SUCCEED, "MOPUB"));
	 				}
					
					if(mInterstitial.isReady()){
						mInterstitial.show();
					}
				}catch(Exception e){
				}
			}

			@Override
			public void onInterstitialFailed( MoPubInterstitial interstitial, MoPubErrorCode errorCode) {
				try{
					if(intersHandler != null){
	 					intersHandler.sendMessage(Message.obtain(intersHandler, AdlibManager.DID_ERROR, "MOPUB"));
	 				}
					
					if(mInterstitial != null){
						mInterstitial.destroy();
					}
				}catch(Exception e){
				}
			}

			@Override
			public void onInterstitialShown(MoPubInterstitial interstitial) {
				// 미디에이션 통계 정보
				AdlibConfig.getInstance().interstitialImp(adlibKey, "MOPUB");
			}

			@Override
			public void onInterstitialClicked(MoPubInterstitial interstitial) {
				// 미디에이션 통계 정보
				AdlibConfig.getInstance().interstitialClk(adlibKey, "MOPUB");
			}

			@Override
			public void onInterstitialDismissed(MoPubInterstitial interstitial) {
				try{
					if(intersHandler != null){
	 					intersHandler.sendMessage(Message.obtain(intersHandler, AdlibManager.INTERSTITIAL_CLOSED, "MOPUB"));
	 				}
					
					if(mInterstitial != null){
						mInterstitial.destroy();
					}
				}catch(Exception e){
				}
			}
	    	
	    });
	    
	    mInterstitial.load();	
	}
}