/*
 * adlibr - Library for mobile AD mediation.
 * http://adlibr.com
 * Copyright (c) 2012 Mocoplex, Inc.  All rights reserved.
 * Licensed under the BSD open source license.
 */

/*
 * confirmed compatible with ad@m SDK 2.0.3
 */

package test.adlib.project.ads;

import net.daum.adam.publisher.AdView.OnAdFailedListener;
import net.daum.adam.publisher.AdView.OnAdLoadedListener;
import net.daum.adam.publisher.impl.AdError;

import com.mocoplex.adlib.SubAdlibAdViewCore;

import android.content.Context;
import android.util.AttributeSet;

public class SubAdlibAdViewAdam extends SubAdlibAdViewCore  {
	
	protected net.daum.adam.publisher.AdView ad;
	protected boolean bGotAd = false;

	public SubAdlibAdViewAdam(Context context) {
		this(context,null);
	}	
	
	public SubAdlibAdViewAdam(Context context, AttributeSet attrs) {
		super(context, attrs);
				
		ad = new net.daum.adam.publisher.AdView(context);
		
		// 여기에 ADAM ID 를 입력하세요.
		String adamID = "TestClientId";
		
		// 할당 받은 clientId 설정
		ad.setClientId(adamID);
		// 광고 갱신 시간 : 기본 60초
		ad.setRequestInterval(12);		
		
		ad.setOnAdLoadedListener(new OnAdLoadedListener() {
			@Override
			public void OnAdLoaded() {
				bGotAd = true;
				gotAd();
			} });
		
		ad.setOnAdFailedListener(new OnAdFailedListener() {
			@Override
			public void OnAdFailed(AdError arg0, String arg1) {
				if(!bGotAd)
					failed();
			} });

		this.addView(ad);
	}
	
	// 스케줄러에의해 자동으로 호출됩니다.
	// 실제로 광고를 보여주기 위하여 요청합니다.	
	public void query()
	{
		ad.resume();

		// ad@m 화면에 보이는 상태에서 결과를 받아올 수 있습니다.		
		this.gotAd();
	}
	
	// 광고뷰가 사라지는 경우 호출됩니다. 
	public void clearAdView()
	{
		if(ad != null)
		{
			ad.pause();
		}

		super.clearAdView();
	}
	
	public void onResume()
	{
		super.onResume();
		
		if(ad != null)
		{
			ad.resume();
		}
	}
	public void onPause()
	{
		super.onPause();
		
		if(ad != null)
		{
			ad.pause();
		}		
	}
	public void onDestroy()
	{
		super.onDestroy();
		
		if(ad != null)
		{
			ad.destroy();
			ad = null;
		}		
	}	
}
