/*
 * adlibr - Library for mobile AD mediation.
 * http://adlibr.com
 * Copyright (c) 2012 Mocoplex, Inc.  All rights reserved.
 * Licensed under the BSD open source license.
 */

/*
 * confirmed compatible with NaverAdPost SDK 1.0
 */

package test.adlib.project.ads;

import com.mocoplex.adlib.SubAdlibAdViewCore;
import com.nbpcorp.mobilead.sdk.MobileAdListener;
import com.nbpcorp.mobilead.sdk.MobileAdView;

import android.content.Context;
import android.util.AttributeSet;


public class SubAdlibAdViewNaverAdPost extends SubAdlibAdViewCore  {
	
	protected MobileAdView ad;
	protected boolean bShowed = false;
    	
	public SubAdlibAdViewNaverAdPost(Context context) {
		this(context,null);
	}	
	
	public SubAdlibAdViewNaverAdPost(Context context, AttributeSet attrs) {
		super(context, attrs);
	
		// 여기에 네이버에서 발급받은 key 를 입력하세요.
		String naverAdPostKey = "NAVER - API - KEY";		
		
		ad = new MobileAdView(context);
		ad.setChannelID(naverAdPostKey);
		ad.setTest(true);
		
		this.addView(ad);
		
	}
	
	// 스케줄러에의해 자동으로 호출됩니다.
	// 실제로 광고를 보여주기 위하여 요청합니다.	
	public void query()
	{		
		ad.setListener(new MobileAdListener() {

			@Override
			public void onReceive(int arg0) {
				
				if(arg0 == 0)
				{
					gotAd();
				}
				else
				{
					failed();
				}
				
			}});
		
		ad.start();
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
			ad.start();
		}
	}
	public void onPause()
	{
		super.onPause();
		
		if(ad != null)
		{
			ad.stop();
		}		
	}
}