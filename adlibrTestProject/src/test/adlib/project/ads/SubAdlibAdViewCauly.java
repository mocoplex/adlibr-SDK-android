/*
 * adlibr - Library for mobile AD mediation.
 * http://adlibr.com
 * Copyright (c) 2012 Mocoplex, Inc.  All rights reserved.
 * Licensed under the BSD open source license.
 */

/*
 * confirmed compatible with cauly SDK 1.4.7
 */

package test.adlib.project.ads;

import com.cauly.android.ad.AdListener;
import com.mocoplex.adlib.AdlibConfig;
import com.mocoplex.adlib.SubAdlibAdViewCore;

import android.content.Context;
import android.util.AttributeSet;

// 자세한 세부 내용은 CAULY SDK 개발 문서를 참조해주세요.
public class SubAdlibAdViewCauly extends SubAdlibAdViewCore  {
	
	protected com.cauly.android.ad.AdView ad;

	public SubAdlibAdViewCauly(Context context) {
		this(context,null);
	}	
	
	public SubAdlibAdViewCauly(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		// 여기에 CAULY ID를 입력합니다.
		String caulyID = "CAULY";
		
		com.cauly.android.ad.AdInfo ai = new com.cauly.android.ad.AdInfo();
		ai.initData(caulyID, "cpc",
				AdlibConfig.getInstance().getCaulyGender(),
				AdlibConfig.getInstance().getCaulyAge(),
				AdlibConfig.getInstance().getCaulyGPS(),
				"default",
				"yes",30,true);

		ad = new com.cauly.android.ad.AdView(this.getContext());				
	}
		
	// 스케줄러에의해 자동으로 호출됩니다.
	// 실제로 광고를 보여주기 위하여 요청합니다.		
	public void query()
	{
		ad.setAdListener(new AdListener(){

			@Override
			public void onCloseInterstitialAd() {
				
			}

			@Override
			public void onFailedToReceiveAd(boolean arg0) {

				failed();
			}

			@Override
			public void onReceiveAd() {
				
				// 광고수신 성공				
				// 유료광고일 경우만 보이게 CAULY SDK 문서 참조
				if(ad.isChargeableAd())
				{
					// 광고를 수신하였으면 이를 알려서 화면에 표시합니다.
					gotAd();					
				}
				else
				{
					failed();
				}				
			}
			
		});
		
		this.addView(ad);
		
		// 화면에 먼저 보여 광고가 있는지 확인합니다.
		gotAd();
	}
	
	public void clearAdView()
	{
		if(ad != null)
		{
			ad.stopLoading();
			ad.destroy();
			ad = null;			
		}
		
		super.clearAdView();
	}
	
	public void onResume()
	{
		ad.startLoading();
		super.onResume();
	}
	public void onPause()
	{
		ad.stopLoading();
		super.onPause();
	}	
}
