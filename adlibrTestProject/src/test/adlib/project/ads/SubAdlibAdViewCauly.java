/*
 * adlibr - Library for mobile AD mediation.
 * http://adlibr.com
 * Copyright (c) 2012 Mocoplex, Inc.  All rights reserved.
 * Licensed under the BSD open source license.
 */

/*
 * confirmed compatible with cauly SDK 3.0
 */

package test.adlib.project.ads;

import com.fsn.cauly.CaulyAdInfo;
import com.fsn.cauly.CaulyAdInfoBuilder;
import com.fsn.cauly.CaulyAdView;
import com.mocoplex.adlib.SubAdlibAdViewCore;

import android.content.Context;
import android.util.AttributeSet;

// 자세한 세부 내용은 CAULY SDK 개발 문서를 참조해주세요.
public class SubAdlibAdViewCauly extends SubAdlibAdViewCore  {
	
	protected CaulyAdView ad;
	protected boolean bGotAd = false;
	
	public SubAdlibAdViewCauly(Context context) {
		this(context,null);
	}	

	public SubAdlibAdViewCauly(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		// 여기에 CAULY ID를 입력합니다.
		String caulyID = "CAULY ID";
		
		CaulyAdInfo ai = new CaulyAdInfoBuilder(caulyID).effect("RightSlide").bannerHeight("Proportional").build();

		ad = new CaulyAdView(this.getContext());
		ad.setAdInfo(ai);
		ad.setAdViewListener(new com.fsn.cauly.CaulyAdViewListener() {

			@Override
			public void onReceiveAd(CaulyAdView adView, boolean isChargeableAd) {
				if(isChargeableAd)
				{
					bGotAd = true;
				}
				else
					failed();

			}
			
			@Override
			public void onCloseLandingScreen(CaulyAdView arg0) {
			}

			@Override
			public void onFailedToReceiveAd(CaulyAdView arg0, int arg1,
					String arg2) {
				
				if(!bGotAd)
					failed();				
			}

			@Override
			public void onShowLandingScreen(CaulyAdView arg0) {
			}
		});		
		
		this.addView(ad);		
	}
		
	// 스케줄러에의해 자동으로 호출됩니다.
	// 실제로 광고를 보여주기 위하여 요청합니다.		
	public void query()
	{		
		if(ad != null)
		{
			// background request 를 지원하지 않는 플랫폼입니다.
			// 먼저 광고뷰가 화면에 보여진 상태에서만 응답을 받을 수 있습니다.
            bGotAd = false;	
			gotAd();
			
			ad.reload();
		}
	}
	
	public void clearAdView()
	{
		if(ad != null)
		{
			ad.pause();
		}		
		super.clearAdView();
	}
	
	// destroy ad view
	public void onDestroy()
	{
		if(ad != null)
		{
			ad.destroy();
			ad = null;
		}
		super.onDestroy();
	}	
	public void onResume()
	{
		if(ad != null)
		{
			ad.reload();
		}
		
		super.onResume();
	}
	public void onPause()
	{
		if(ad != null)
		{
			ad.pause();
		}

		super.onPause();
	}	
}
