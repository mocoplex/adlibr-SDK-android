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

	public SubAdlibAdViewAdam(Context context) {
		this(context,null);
	}	
	
	public SubAdlibAdViewAdam(Context context, AttributeSet attrs) {
		super(context, attrs);
				
		ad = new net.daum.adam.publisher.AdView(context);
		
		ad.setOnAdLoadedListener(new OnAdLoadedListener() {
			@Override
			public void OnAdLoaded() {
				gotAd();
			} });
		
		ad.setOnAdFailedListener(new OnAdFailedListener() {
			@Override
			public void OnAdFailed(AdError arg0, String arg1) {
				failed();
			} });
	}
	
	// 스케줄러에의해 자동으로 호출됩니다.
	// 실제로 광고를 보여주기 위하여 요청합니다.	
	public void query()
	{
		// 여기에 ADAM ID 를 입력하세요.
		String adamID = "TestClientId";
		
		// 할당 받은 clientId 설정
		ad.setClientId(adamID);
		// 광고 갱신 시간 : 기본 60초
		ad.setRequestInterval(12);

		this.addView(ad);
		
		this.gotAd();
	}
	
	// 광고뷰를 삭제하는 경우 호출됩니다. 
	public void clearAdView()
	{
		if(ad != null)
		{
			ad.destroy();
			ad = null;			
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
}
