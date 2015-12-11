/*
 * adlibr - Library for mobile AD mediation.
 * http://adlibr.com
 * Copyright (c) 2012-2013 Mocoplex, Inc.  All rights reserved.
 * Licensed under the BSD open source license.
 */

/*
 * confirmed compatible with AdMixer SDK 1.3.11
 */

package test.adlib.project.ads;

import com.admixer.AdAdapter;
import com.admixer.AdInfo;
import com.admixer.AdView;
import com.admixer.AdViewListener;
import com.admixer.InterstitialAd;
import com.admixer.InterstitialAdListener;
import com.mocoplex.adlib.AdlibManager;
import com.mocoplex.adlib.SubAdlibAdViewCore;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.LinearLayout;

public class SubAdlibAdViewAdmixer extends SubAdlibAdViewCore {
	
	protected AdView ad;
	protected boolean bGotAd = false;
	
	// 여기에 AdMixer ID 를 입력하세요.
	protected String admixerID = "AdMixer_ID";
	protected static String admixerInterstitialID = "AdMixer_Interstitial_ID";

	public SubAdlibAdViewAdmixer(Context context) {
		this(context,null);
	}	
	
	public SubAdlibAdViewAdmixer(Context context, AttributeSet attrs) {
		super(context, attrs);
		initAdmixerView();
	}
	
	public void initAdmixerView() {
		AdInfo adInfo = new AdInfo(admixerID); // AxKey 값 설정
		//adInfo.setTestMode(true); // 테스트 모드 설정
		adInfo.setDefaultAdTime(0);    // 최소 디폴트 광고 표시 시간(milliseconds)
		adInfo.setMaxRetryCountInSlot(-1);  // 리로드 시간 내에 전체 AdNetwork 반복 최대 횟수(-1 : 무한, 0 : 반복 없음, n : n번 반복)
		
		ad = new AdView(this.getContext());
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		ad.setLayoutParams(params);
		ad.setAdInfo(adInfo, (Activity) this.getContext());
		ad.setAdViewListener(new AdViewListener() {

			@Override
			public void onClickedAd(String arg0, AdView arg1) {
				
			}

			@Override
			public void onFailedToReceiveAd(int arg0, String arg1, AdView arg2) {
				bGotAd = true;
				failed();
			}

			@Override
			public void onReceivedAd(String arg0, AdView arg1) {
				Log.d("ADMIXER", "[B_ADMIXER] onReceiveAd ");
				bGotAd = true;
				// 광고를 받아왔으면 이를 알려 화면에 표시합니다.
				gotAd();
			}
		
		});
//		ad.setAdapterOption(AdAdapter.ADAPTER_CAULY, "allow_call", "yes"); // Cauly 콜 광고 여부 설정
		ad.setAdapterOption(AdAdapter.ADAPTER_ADMIXER_RTB, "bannerHeight", "fixed");
		ad.setAlwaysShowAdView(false); // 광고 로딩 전에도 영역을 차지할 것인지 설정(false – 기본값)
		
		this.addView(ad);
	}
	
	// 스케줄러에의해 자동으로 호출됩니다.
	// 실제로 광고를 보여주기 위하여 요청합니다.	
	public void query() {
		bGotAd = false;
		
		if(ad == null)
			initAdmixerView();
		
		queryAd();

		ad.resume();
		
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
			//ad.destroy();
			ad = null;
		}

		super.clearAdView();
	}
	
	public void onResume() {		
		if(ad != null){
			ad.resume();
		}
        
        super.onResume();
	}
	
	public void onPause() {
		if(ad != null){
			ad.pause();
		}
        
        super.onPause();
	}
	
	public void onDestroy() {
		if(ad != null){
			//ad.destroy();
			ad = null;
		}
        
        super.onDestroy();
	}
	
	public static void loadInterstitial(Context ctx, final Handler h, final String adlibKey) {
		AdInfo adInfo = new AdInfo(admixerInterstitialID); // AxKey 값 설정
		adInfo.setInterstitialTimeout(0);   // 초단위로 전면 광고 타이아웃 설정(기본값 : 0, 0 이면 서버 지정 시간으로 처리됨)
		adInfo.setMaxRetryCountInSlot(-1);  // 리로드 시간 내에 전체 AdNetwork 반복 최대 횟수(-1 : 무한, 0 : 반복 없음, n : n번 반복)
		//adInfo.setTestMode(true); // 테스트 모드 설정
		InterstitialAd interstitialAd = new InterstitialAd(ctx);// 전면 광고 생성
		interstitialAd.setAdInfo(adInfo, (Activity)ctx);
		interstitialAd.setInterstitialAdListener(new InterstitialAdListener() {

			@Override
			public void onInterstitialAdClosed(InterstitialAd arg0) {
				
				try{
	 				if(h != null){
	 					h.sendMessage(Message.obtain(h, AdlibManager.INTERSTITIAL_CLOSED, "ADMIXER"));
	 				}
	 			}catch(Exception e){
	 			}
			}

			@Override
			public void onInterstitialAdFailedToReceive(int arg0, String arg1, InterstitialAd arg2) {
				
				try{
	 				if(h != null){
	 	 				h.sendMessage(Message.obtain(h, AdlibManager.DID_ERROR, "ADMIXER"));
	 				}
	 			}catch(Exception e){
	 			}
			}

			@Override
			public void onInterstitialAdReceived(String arg0, InterstitialAd arg1) {
				
				try{
	 				if(h != null){
	 					h.sendMessage(Message.obtain(h, AdlibManager.DID_SUCCEED, "ADMIXER"));
	 				}
	 			}catch(Exception e){
	 			}
			}

			@Override
			public void onInterstitialAdShown(String arg0, InterstitialAd arg1) {
				
			}

			@Override
			public void onLeftClicked(String arg0, InterstitialAd arg1) {
				
			}

			@Override
			public void onRightClicked(String arg0, InterstitialAd arg1) {
				
			}
		});
		interstitialAd.startInterstitial();
	}
}