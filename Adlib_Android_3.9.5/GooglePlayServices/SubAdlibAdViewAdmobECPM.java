/*
 * adlibr - Library for mobile AD mediation.
 * http://adlibr.com
 * Copyright (c) 2012-2013 Mocoplex, Inc.  All rights reserved.
 * Licensed under the BSD open source license.
 */


package test.adlib.project.ads;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.mocoplex.adlib.SubAdlibAdViewCore;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
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
		ad = new AdView((Activity)getContext());
		ad.setAdUnitId(admobMediationID);
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
			}
			
		});
	}
	
	private AdRequest request = new AdRequest.Builder().build();
    
	// 스케줄러에의해 자동으로 호출됩니다.
	// 실제로 광고를 보여주기 위하여 요청합니다.
	public void query()
	{
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
				if(bGotAd)
					return;
				else
				{
					failed();
					if(ad != null)
					{
                    	SubAdlibAdViewAdmobECPM.this.removeView(ad);
                    	ad.destroy();
                    	ad = null;
                    }
                    bGotAd = false;
				}
			}
            
		}, 5000);
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
}