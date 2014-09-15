/*
 * adlibr - Library for mobile AD mediation.
 * http://adlibr.com
 * Copyright (c) 2012-2013 Mocoplex, Inc.  All rights reserved.
 * Licensed under the BSD open source license.
 */

/*
 * confirmed compatible with MezzoMediaMAN SDK 3.6
 */

package test.adlib.project.ads;

import com.mapps.android.view.AdView;
import com.mapps.android.view.ManAdListner;
import com.mocoplex.adlib.SubAdlibAdViewCore;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

/*
 AndroidManifest.xml 에 아래 내용을 추가해주세요.
 
 <meta-data android:name="MEZZO_WINDOW_ID" android:value="banner" />
 <!-- MEZZO GPS 사용여부 0:사용안함 1:사용 -->
 <meta-data android:name="GPS_USE" android:value="0" />
 <!-- MEZZO DEBUG 모드 사용여부 0:사용안함 1:사용 -->
 <meta-data android:name="DEBUG_MODE" android:value="0" />
 <activity
 	android:name="com.mapps.android.view.InterstitialView" />
 <activity
	android:name="com.mapps.android.view.MultimediaView" /> 
 <activity
	android:name="com.mapps.android.view.AlertNotiView" 
    android:theme="@android:style/Theme.Translucent"/>
 */

public class SubAdlibAdViewMezzo extends SubAdlibAdViewCore  {
	
	protected AdView ad;
	protected boolean bPassAd = false;
	
	// 여기에 MMEDIA ID 를 입력하세요.
	String mezzoID = "MEZZO_ID";

	public SubAdlibAdViewMezzo(Context context) {
		this(context,null);
	}	
	
	public SubAdlibAdViewMezzo(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		ad = new AdView(context, mezzoID, 1, 0);
		ad.setManAdListner(new ManAdListner() {

			@Override
			public void onChargeableBannerType(View v, boolean bcharge) {
				
				if(ad == v)
				{
					// 무료광고 일 경우 다음광고로 넘깁니다.
					if(!bcharge)
					{
						bPassAd = true;
						failed();
					}
				}
			}

			@Override
			public void onFailedToReceive(View v, int errCode) {
				
				if(ad == v)
				{
					// 광고 수신 성공
					if(errCode == 0)
					{
						if(!bPassAd)
						{
							queryAd();
							gotAd();
						}
					}
					// 광고 수신 실패
					else
					{
						failed();
					}
				}
			}
		});
	}
	
	// 스케줄러에의해 자동으로 호출됩니다.
	// 실제로 광고를 보여주기 위하여 요청합니다.	
	public void query()
	{
		bPassAd = false;
		
		this.removeAllViews();
		this.addView(ad);
		
		ad.StartService();
	}

	// 광고뷰가 사라지는 경우 호출됩니다. 
	public void clearAdView()
	{
		if(ad != null)
		{
			this.removeView(ad);
			ad.StopService();
		}
		
		super.clearAdView();
	}
	
	public void onResume()
	{
		if(ad != null)
		{
			ad.StartService();
		}
		
        super.onResume();
	}
	
	public void onPause()
	{
		if(ad != null)
		{
			ad.StopService();
		}
		
        super.onPause();
	}
	
	public void onDestroy()
	{
		if(ad != null)
		{
			this.removeView(ad);
			ad.StopService();
			ad = null;
		}
        
        super.onDestroy();
	}
}