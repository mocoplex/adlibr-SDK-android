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
import com.google.ads.AdView;
import com.mocoplex.adlib.SubAdlibAdViewCore;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;

/* 
 Admob Mediation 사용을 위해서는, Admob 사이트에서 설정하신 플랫폼들의 SDK 및 Adapter, Manifest 설정 등이 필요합니다.
 https://developers.google.com/mobile-ads-sdk/docs/admob/mediation 
 위 링크를 참고하세요.
 */

public class SubAdlibAdViewAdmobECPM extends SubAdlibAdViewCore  {
	
	protected AdView ad;
	protected boolean bGotAd = false;
	
	// 여기에 ADMOB Mediation ID 를 입력하세요.
	String admobMediationID = "ADMOB_MEDIATION_ID";
    
	public SubAdlibAdViewAdmobECPM(Context context) {
		this(context,null);
	}
	
	public SubAdlibAdViewAdmobECPM(Context context, AttributeSet attrs) {
		
		super(context, attrs);
		
		initAdmobView();
	}
	
	public void initAdmobView()
	{
		ad = new AdView((Activity) this.getContext(), com.google.ads.AdSize.BANNER, admobMediationID);
		
		// 광고 뷰의 위치 속성을 제어할 수 있습니다.
		this.setGravity(Gravity.CENTER);
		
		ad.setAdListener(new AdListener() {

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
	
	private AdRequest request = new AdRequest();
    
	// 스케줄러에의해 자동으로 호출됩니다.
	// 실제로 광고를 보여주기 위하여 요청합니다.
	public void query()
	{
		bGotAd = false;
		
		this.addView(ad);
		
		ad.loadAd(request);
	}
	
	public void onDestroy()
	{
		if(ad != null)
		{
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
}