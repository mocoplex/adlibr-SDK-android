/*
 * adlibr - Library for mobile AD mediation.
 * http://adlibr.com
 * Copyright (c) 2012-2013 Mocoplex, Inc.  All rights reserved.
 * Licensed under the BSD open source license.
 */

/*
 * confirmed compatible with NaverAdPost SDK 1.2.1
 */

package test.adlib.project.ads;

import com.mocoplex.adlib.SubAdlibAdViewCore;
import com.nbpcorp.mobilead.sdk.MobileAdListener;
import com.nbpcorp.mobilead.sdk.MobileAdView;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;


/*
 AndroidManifest.xml 에 아래 내용을 추가해주세요.
 
 <activity android:name="com.nbpcorp.mobilead.sdk.MobileAdBrowserActivity" />
 */

public class SubAdlibAdViewNaverAdPost extends SubAdlibAdViewCore  {

	protected MobileAdView ad;
	protected static boolean bGotAd = false;
	
	// 여기에 네이버에서 발급받은 key 를 입력하세요.
	String naverAdPostKey = "NAVER_ID";
    	
	public SubAdlibAdViewNaverAdPost(Context context) {
		this(context,null);
	}	

	public SubAdlibAdViewNaverAdPost(Context context, AttributeSet attrs) {
		super(context, attrs);

		initAdpostView();
	}
    
    public void initAdpostView()
    {
		ad = new MobileAdView(this.getContext());
		ad.setChannelID(naverAdPostKey);

		// 샘플 광고를 확인하기 위해서는 ad.setTest(true); 로 변경하여 적용해주세요.
		ad.setTest(false);
        
		LayoutParams l = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
		ad.setLayoutParams(l);

		ad.setListener(new MobileAdListener() {
            
			@Override
			public void onReceive(int arg0) {

				// 광고 수신 성공인 경우나, 검수중인 경우
				if(arg0 == 0 || arg0 == 104 || arg0 == 101 || arg0 == 102 || arg0 == 106)
				{
					bGotAd = true;
				}
				else
				{
					failed();
				}

			}
		});
		
		this.addView(ad);
    }

	// 스케줄러에의해 자동으로 호출됩니다.
	// 실제로 광고를 보여주기 위하여 요청합니다.	
	public void query()
	{
		if(ad == null)
			initAdpostView();
		
		queryAd();
        gotAd();
        
		ad.start();
		
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
							ad.stop();
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
			this.removeView(ad);
			ad.stop();
			ad.destroy();
			ad = null;
		}

		super.clearAdView();
	}

	public void onResume()
	{
		// 다른 Activity에서 destroy를 했을 경우 View가 보이지 않는 문제가 있으므로 다시 생성.
		if(ad != null)
		{
			this.removeView(ad);
			ad.stop();
			ad.destroy();
			ad = null;
			
			initAdpostView();
			
			ad.start();
		}
		
		super.onResume();
	}
	
	public void onPause()
	{
		if(ad != null)
		{
			ad.stop();
		}
		
		super.onPause();
	}
	
	public void onDestroy()
	{
		if(ad != null)
		{			
			ad.stop();
			ad.destroy();
			ad = null;
		}
		
		super.onDestroy();
	}
}