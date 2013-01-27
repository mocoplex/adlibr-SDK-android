/*
 * adlibr - Library for mobile AD mediation.
 * http://adlibr.com
 * Copyright (c) 2012 Mocoplex, Inc.  All rights reserved.
 * Licensed under the BSD open source license.
 */

/*
 * confirmed compatible with admob SDK 6.2.1
 */

package test.adlib.project.ads;

import com.google.ads.Ad;
import com.google.ads.AdRequest.ErrorCode;
import com.mocoplex.adlib.SubAdlibAdViewCore;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;

public class SubAdlibAdViewAdmob extends SubAdlibAdViewCore  {
	
	protected com.google.ads.AdView ad;
	protected boolean bGotAd = false;
    
	public SubAdlibAdViewAdmob(Context context) {
		this(context,null);
	}
	
	public SubAdlibAdViewAdmob(Context context, AttributeSet attrs) {
		
		super(context, attrs);
        
		// 여기에 ADMOB ID 를 입력하세요.
		String admobID = "ADMOB ID";
        
		ad = new com.google.ads.AdView((Activity) this.getContext(), com.google.ads.AdSize.BANNER, admobID);
        
		// 광고 뷰의 위치 속성을 제어할 수 있습니다.
		this.setGravity(Gravity.CENTER);
		
		ad.setAdListener( new com.google.ads.AdListener() {
            
			
			public void onDismissScreen(Ad arg0) {
                
				
			}
            
			public void onFailedToReceiveAd(Ad arg0, ErrorCode arg1) {
				if(!bGotAd)
					failed();
			}
            
			public void onLeaveApplication(Ad arg0) {
                
				
			}
            
			public void onPresentScreen(Ad arg0) {
                
				
			}
            
			public void onReceiveAd(Ad arg0) {
				
				if(bGotAd)
					return;
				
				bGotAd = true;
				
				// 광고를 받아왔으면 이를 알려 화면에 표시합니다.
				gotAd();
			}
            
		});
        
		this.addView(ad);
	}
    
	// 스케줄러에의해 자동으로 호출됩니다.
	// 실제로 광고를 보여주기 위하여 요청합니다.
	public void query()
	{
		bGotAd = false;
		ad.loadAd(request);
	}
	private com.google.ads.AdRequest request = new com.google.ads.AdRequest();
	
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
			ad.stopLoading();
		}
		
		super.clearAdView();
	}
	
	public void onResume()
	{
		if(ad != null)
			ad.loadAd(request);
		
		super.onResume();
	}
	
	public void onPause()
	{
		if(ad != null)
			ad.stopLoading();
		
		super.onPause();
	}
	
}