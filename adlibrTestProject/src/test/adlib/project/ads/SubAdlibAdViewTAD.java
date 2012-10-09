/*
 * adlibr - Library for mobile AD mediation.
 * http://adlibr.com
 * Copyright (c) 2012 Mocoplex, Inc.  All rights reserved.
 * Licensed under the BSD open source license.
 */

/*
 * confirmed compatible with cauly SDK 2.54.0.6
 */

package test.adlib.project.ads;

import com.sktelecom.tad.sdk.AdListener;
import com.sktelecom.tad.sdk.AdListenerResponse;

import com.mocoplex.adlib.SubAdlibAdViewCore;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;

public class SubAdlibAdViewTAD extends SubAdlibAdViewCore  {
	
	protected com.sktelecom.tad.AdView ad;
	protected boolean bGotAd = false;
	
	public SubAdlibAdViewTAD(Context context) {
		this(context,null);
		
		// T-AD ID 는 MANIFEST 파일에 입력해주세요. (T-AD SDK 문서 참조)
		
		// T-AD is only showing on below resolution.
		int h = 0;
		DisplayMetrics d = this.getResources().getDisplayMetrics();
		if(d.widthPixels == 800 && d.heightPixels == 1280 || d.widthPixels == 1280 && d.heightPixels == 800)
			h = 120;
		else if(d.widthPixels == 720 && d.heightPixels == 1280 || d.widthPixels == 1280 && d.heightPixels == 720)
			h = 108;
		else if(d.widthPixels == 600 && d.heightPixels == 1024 || d.widthPixels == 1024 && d.heightPixels == 600)
			h = 90;
		else if(d.widthPixels == 540 && d.heightPixels == 960 || d.widthPixels == 960 && d.heightPixels == 540)
			h = 81;
		else if(d.widthPixels == 480 && d.heightPixels == 854 || d.widthPixels == 854 && d.heightPixels == 480)
			h = 72;		
		else if(d.widthPixels == 480 && d.heightPixels == 800 || d.widthPixels == 800 && d.heightPixels == 480)
			h = 72;
		else if(d.widthPixels == 320 && d.heightPixels == 480 || d.widthPixels == 480 && d.heightPixels == 320)
			h = 48;
		else if(d.widthPixels == 240 && d.heightPixels == 320 || d.widthPixels == 320 && d.heightPixels == 240)
			h = 36;
		
		if (h == 0)
		{
			failed();
			return;
		}
		
		ad = com.sktelecom.tad.AdView.createAdView(this.getContext());
		ad.setAdListener(new AdListener(){

			@Override
			public void onFailedToReceiveAd(AdListenerResponse arg0) {
				// 광고 수신에 실패했다. 바로 다음 플랫폼을 보인다.
				if(!bGotAd)
					failed();
			}

			@Override
			public void onReceiveAd() {
				gotAd();
				bGotAd = true;
			}});
		
		this.addView(ad);		
	}
	
	public SubAdlibAdViewTAD(Context context, AttributeSet attrs) {
		super(context, attrs);
		
	}
		
	public void query()
	{
		if(bGotAd)
			gotAd();
	}
	
	public void clearAdView()
	{
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