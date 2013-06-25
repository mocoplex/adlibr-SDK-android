/*
 * adlibr - Library for mobile AD mediation.
 * http://adlibr.com
 * Copyright (c) 2012-2013 Mocoplex, Inc.  All rights reserved.
 * Licensed under the BSD open source license.
 */

/*
 * confirmed compatible with ad@m SDK 2.2.1
 */

package test.adlib.project.ads;

import net.daum.adam.publisher.AdView.OnAdFailedListener;
import net.daum.adam.publisher.AdView.OnAdLoadedListener;
import net.daum.adam.publisher.impl.AdError;

import com.mocoplex.adlib.SubAdlibAdViewCore;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.LinearLayout;

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
		String adamID = "ADAM_ID";
		
		// 할당 받은 clientId 설정
		ad.setClientId(adamID);
		// 광고 갱신 시간 : 기본 60초
		ad.setRequestInterval(12);
        
        int adHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (float) 48.0, getResources().getDisplayMetrics());
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, adHeight);
		ad.setLayoutParams(params);
		
		ad.setOnAdLoadedListener(new OnAdLoadedListener() {
			@Override
			public void OnAdLoaded() {				
				// query 당시 미리 배너를 화면에 보이게 합니다.
				bGotAd = true;
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
		// background request 를 지원하지 않는 플랫폼입니다.
		// 먼저 광고뷰가 화면에 보여진 상태에서만 응답을 받을 수 있습니다. 		
		gotAd();

		ad.resume();
		
		if(!bGotAd)
		{
			// 3초 이상 리스너 응답이 없으면 다음 플랫폼으로 넘어갑니다.
			Handler adHandler = new Handler();
			adHandler.postDelayed(new Runnable() {

				@Override
				public void run() {
					if(bGotAd)
						return;
					else
					{
						if(ad != null)
							ad.pause();
						failed();
					}
				}
				
			}, 3000);
		}
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
		if(ad != null)
		{
			ad.resume();
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
	public void onDestroy()
	{
		if(ad != null)
		{
			ad.destroy();
			ad = null;
		}
        
        super.onDestroy();
	}	
}