/*
 * adlibr - Library for mobile AD mediation.
 * http://adlibr.com
 * Copyright (c) 2012 Mocoplex, Inc.  All rights reserved.
 * Licensed under the BSD open source license.
 */

/*
 * confirmed compatible with adcube SDK 1.6
 */

package test.adlib.project.ads;

import com.mocoplex.adlib.SubAdlibAdViewCore;

import android.content.Context;
import android.util.AttributeSet;

public class SubAdlibAdViewAdcube extends SubAdlibAdViewCore  {
	
	protected boolean bGotAd = false;
	protected com.zetacube.libzc.AdView ad;

	private int dpToPx(int dp)
	{
	    float density = getContext().getResources().getDisplayMetrics().density;
	    return Math.round((float)dp * density);
	}
	
	public SubAdlibAdViewAdcube(Context context) {
		this(context,null);
	}	
	
	public SubAdlibAdViewAdcube(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		ad = new com.zetacube.libzc.AdView(this.getContext());
		this.addView(ad);
		
		LayoutParams l = new LayoutParams(LayoutParams.FILL_PARENT,dpToPx(50));
		ad.setLayoutParams(l);				
	}
		
	// 스케줄러에의해 자동으로 호출됩니다.
	// 실제로 광고를 보여주기 위하여 요청합니다.	
	public void query()
	{
		ad.setOnAdViewListener(new com.zetacube.libzc.AdView.OnAdViewListener() {

			public void receiveAdError(int errorCode, String 
			description, 
			String failingUrl) {
				failed();
			}
			
			public void receiveAd(String url) {
				// 광고를 받아왔으면 이를 알려 화면에 표시합니다.
				bGotAd = true;
				gotAd();
			}
		});
		
		if(!bGotAd)
		{
			// 여기에 ADCUBE ID 를 입력하세요.
			String adcubeID = "ADCUBE ID";
			ad.loadAd(adcubeID, null, true);
			ad.startAd();
		}
		
		gotAd();
	}

	// 광고뷰가 사라지는 경우 호출됩니다.
	public void clearAdView()
	{
		super.clearAdView();
	}
	public void onResume()
	{
		super.onResume();
		
		if(ad != null)
		{
			ad.onResume();
		}
	}
	public void onPause()
	{
		super.onPause();
		
		if(ad != null)
		{
			ad.onPause();
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
