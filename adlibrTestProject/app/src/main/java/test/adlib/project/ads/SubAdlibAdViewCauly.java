/*
 * adlibr - Library for mobile AD mediation.
 * http://adlibr.com
 * Copyright (c) 2012-2013 Mocoplex, Inc.  All rights reserved.
 * Licensed under the BSD open source license.
 */

/*
 * confirmed compatible with cauly SDK 3.3.30
 */

package test.adlib.project.ads;

import com.fsn.cauly.CaulyAdInfo;
import com.fsn.cauly.CaulyAdInfoBuilder;
import com.fsn.cauly.CaulyAdView;
import com.fsn.cauly.CaulyInterstitialAd;
import com.fsn.cauly.CaulyInterstitialAdListener;
import com.mocoplex.adlib.AdlibManager;
import com.mocoplex.adlib.SubAdlibAdViewCore;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;

// 자세한 세부 내용은 CAULY SDK 개발 문서를 참조해주세요.
public class SubAdlibAdViewCauly extends SubAdlibAdViewCore implements com.fsn.cauly.CaulyAdViewListener {

	protected CaulyAdView ad;
	protected boolean bGotAd = false;
	protected boolean isAdAvailable = false;
	
	// 여기에 CAULY ID를 입력합니다.
	protected String caulyID = "CAULY_ID";
	protected static String caulyInterstitialID = "CAULY_Interstitial_ID";
	
	protected static Handler intersHandler = null;
	
	public SubAdlibAdViewCauly(Context context) {
		this(context,null);
	}

	public SubAdlibAdViewCauly(Context context, AttributeSet attrs) {
		super(context, attrs);

		initCaulyView();
	}
	
	public void initCaulyView() {
		/* 애니메이션 effect
		 * LeftSlide(기본) : 왼쪽에서 오른쪽으로 슬라이드 
		 * RightSlide     : 오른쪽에서 왼쪽으로 슬라이드 
		 * TopSlide       : 위에서 아래로 슬라이드 
		 * BottomSlide    : 아래서 위로 슬라이드 
		 * FadeIn         : 전에 있던 광고가 서서히 사라지는 효과 
		 * Circle         : 한 바퀴 롤링 
		 * None           : 애니메이션 효과 없이 바로 광고 교체
		 */

		CaulyAdInfo ai = new CaulyAdInfoBuilder(caulyID).effect("None").bannerHeight("Proportional").build();
        
		ad = new CaulyAdView(this.getContext());
		ad.setAdInfo(ai);
		ad.setVisibility(View.GONE);
		ad.setAdViewListener(this);

		this.addView(ad);
	}
	
	@Override
	public void onReceiveAd(CaulyAdView adView, boolean isChargeableAd) {
		
		isAdAvailable = true;
		bGotAd = true;
		if(isChargeableAd){
			try{
				if(ad != null){
					ad.setVisibility(View.VISIBLE);
				}

				// 유료광고를 받아왔으면 이를 알려 화면에 표시합니다.
				gotAd();
			}catch(Exception e){
				failed();
			}
		}else{
			// 무료광고는 보여주지 않습니다.
			failed();
		}
        
	}

	@Override
	public void onCloseLandingScreen(CaulyAdView arg0) {
	}
    
	@Override
	public void onFailedToReceiveAd(CaulyAdView arg0, int arg1, 
			String arg2) {
		
		bGotAd = true;
		failed();
	}
    
	@Override
	public void onShowLandingScreen(CaulyAdView arg0) {
	}

	// 스케줄러에의해 자동으로 호출됩니다.
	// 실제로 광고를 보여주기 위하여 요청합니다.
	public void query() {
		bGotAd = false;
		
		if(ad == null)
            initCaulyView();
		
		queryAd();
		
		ad.reload();
		
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
			ad.setVisibility(View.GONE);
			this.removeView(ad);
			ad.destroy();
            ad = null;
		}
		
		super.clearAdView();
	}

	// destroy ad view
	public void onDestroy() {
		if(ad != null){
			this.removeView(ad);
			ad.destroy();
			ad = null;
		}
		
		super.onDestroy();
	}
	
	public void onResume() {
		if(ad != null){
			// 최초 리스너 응답을 받지 못한 상태에서 액티비티 전환이 일어나면 광고뷰가 보이지 않는 현상을 방지합니다.
			if(!isAdAvailable){
				this.removeView(ad);
				ad.destroy();
				ad = null;
				
				initCaulyView();
			}
            
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
	
	static CaulyInterstitialAdListener intersListener = new CaulyInterstitialAdListener() {

		@Override
		public void onReceiveInterstitialAd(CaulyInterstitialAd ad, boolean arg1) {
			
			try{
 				if(intersHandler != null){
 					intersHandler.sendMessage(Message.obtain(intersHandler, AdlibManager.DID_SUCCEED, "CAULY"));
 				}
 				
 				ad.show();
			}catch(Exception e){
			}
		}
		
		@Override
		public void onFailedToReceiveInterstitialAd(CaulyInterstitialAd ad, int errCode, String errMsg) {
			
			try{
 				if(intersHandler != null){
 					intersHandler.sendMessage(Message.obtain(intersHandler, AdlibManager.DID_ERROR, "CAULY"));
 				}
			}catch(Exception e){
			}
		}
		
		@Override
		public void onClosedInterstitialAd(CaulyInterstitialAd ad) {
			
			try{
 				// 전면광고 닫혔다.
 				if(intersHandler != null){
 					intersHandler.sendMessage(Message.obtain(intersHandler, AdlibManager.INTERSTITIAL_CLOSED, "CAULY"));
 				} 				   		 					
			}catch(Exception e){
			}					
			
		}
		
		@Override
		public void onLeaveInterstitialAd(CaulyInterstitialAd ad) {
			
		}
    };
	
	public static void loadInterstitial(Context ctx, final Handler h, final String adlibKey) {
		// CaulyAdInfo 생성
	    CaulyAdInfo adInfo = new CaulyAdInfoBuilder(caulyInterstitialID).build();
	    // 전면 광고 생성
	    CaulyInterstitialAd interstial = new CaulyInterstitialAd();
	    interstial.setAdInfo(adInfo);
	    intersHandler = h;
	    interstial.setInterstialAdListener(intersListener);
	    
	    // 광고 요청. 광고 노출은 CaulyInterstitialAdListener의 onReceiveInterstitialAd에서 처리한다.
	    interstial.requestInterstitialAd((Activity)ctx); // 전면 광고 노출 플래그 활성화
	}
}