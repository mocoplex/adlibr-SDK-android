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


/*
 AndroidManifest.xml 에 아래 내용을 추가해주세요.
 
 <activity android:name="com.nbpcorp.mobilead.sdk.MobileAdBrowserActivity" />
 */

public class SubAdlibAdViewNaverAdPost extends SubAdlibAdViewCore  {
	
	protected MobileAdView ad;
	protected static boolean bGotAd = false;
    	
	public SubAdlibAdViewNaverAdPost(Context context) {
		this(context,null);
	}	
	
	public SubAdlibAdViewNaverAdPost(Context context, AttributeSet attrs) {
		super(context, attrs);
	
		// 여기에 네이버에서 발급받은 key 를 입력하세요.
		String naverAdPostKey = "NAVER ID";		
		
		ad = new MobileAdView(context);
		ad.setChannelID(naverAdPostKey);
		
		// 샘플 광고를 확인하기 위해서는 ad.setTest(true); 로 변경하여 적용해주세요.
		ad.setTest(false);

		this.addView(ad);
		
		LayoutParams l = new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT);
		ad.setLayoutParams(l);
		
		ad.setListener(new MobileAdListener() {

			@Override
			public void onReceive(int arg0) {
				
				if(arg0 == 0 || arg0 == 104 || arg0 == 101)
				{
					// 광고 수신 성공인 경우나, 검수중인 경우만 화면에 보입니다.
					bGotAd = true;
					gotAd();
				}
				else
				{
					if(!bGotAd)
						failed();
				}
				
			}});
	}
	
	// 스케줄러에의해 자동으로 호출됩니다.
	// 실제로 광고를 보여주기 위하여 요청합니다.	
	public void query()
	{
		ad.start();
		if(bGotAd)
			gotAd();
	}
	
	// 광고뷰가 사라지는 경우 호출됩니다. 
	public void clearAdView()
	{
		if(ad != null)
		{
			ad.stop();
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
	public void onDestroy()
	{
		super.onDestroy();
		
		if(ad != null)
		{			
			ad.stop();
			ad.destroy();
			ad = null;
		}				
	}
}