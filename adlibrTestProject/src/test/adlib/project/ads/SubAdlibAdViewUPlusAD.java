/*
 * adlibr - Library for mobile AD mediation.
 * http://adlibr.com
 * Copyright (c) 2012-2013 Mocoplex, Inc.  All rights reserved.
 * Licensed under the BSD open source license.
 */

/*
 * confirmed compatible with U+AD SDK 3.0.2
 */

package test.adlib.project.ads;

import kr.co.uplusad.dmpcontrol.LGUDMPAdView;
import kr.co.uplusad.dmpcontrol.OnAdListener;

import com.mocoplex.adlib.SubAdlibAdViewCore;

import android.content.Context;
import android.util.AttributeSet;

/*
 AndroidManifest.xml 에 아래 내용을 추가해주세요.

 <activity android:name="kr.co.uplusad.dmpcontrol.LGUDMPActivity" 
 	android:theme="@android:style/Theme.Translucent.NoTitleBar.Fullscreen" 
 	android:configChanges="keyboard|keyboardHidden|orientation"/>
 */

public class SubAdlibAdViewUPlusAD extends SubAdlibAdViewCore  {
	
	protected LGUDMPAdView ad;
	protected boolean bGotAd = false;
	
	// 여기에 UPLUS ID 를 입력하세요.
	String uplusID = "UPLUS_ID";
    
	public SubAdlibAdViewUPlusAD(Context context) {
		this(context,null);
	}
	
	public SubAdlibAdViewUPlusAD(Context context, AttributeSet attrs) {
		
		super(context, attrs);
		
		initUPlusAdView();
	}
	
	public void initUPlusAdView()
	{
		ad = new LGUDMPAdView(getContext());
		ad.setSlotID(uplusID);
		ad.setHouseAD(false);
		ad.setBackgroundColor(0x00000000);
		
		ad.setOnAdListener(new OnAdListener() {

			@Override
			public void onAdStatus(int arg0) {
				
			}

			@Override
			public void onChargeInfo(LGUDMPAdView arg0, boolean arg1) {
				
			}

			@Override
			public void onCloseAd(LGUDMPAdView arg0) {
				
			}

			@Override
			public void onFailedToReceiveAd(LGUDMPAdView arg0) {
				
				bGotAd = true;
				failed();
			}

			@Override
			public void onNewAd(LGUDMPAdView arg0, boolean arg1) {
				
			}

			@Override
			public void onReceiveAd(LGUDMPAdView arg0) {
				
				bGotAd = true;
				// 광고를 받아왔으면 이를 알려 화면에 표시합니다.
				gotAd();
			}
		});
		
		this.addView(ad);
	}
    
	// 스케줄러에의해 자동으로 호출됩니다.
	// 실제로 광고를 보여주기 위하여 요청합니다.
	public void query()
	{
		bGotAd = false;
		
		if(ad == null)
			initUPlusAdView();
		
		queryAd();
		
		ad.execute();
        
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
					{
						SubAdlibAdViewUPlusAD.this.removeView(ad);
						ad.destroy();
						ad = null;
					}
					failed();
				}
			}
            
		}, 3000);
	}
	
	// 광고뷰가 사라지는 경우 호출됩니다. 
	public void clearAdView()
	{
		if(ad != null)
		{
        	this.removeView(ad);
        	ad.destroy();
        	ad = null;
		}
		
        super.clearAdView();
	}
	
	public void onResume()
	{
		if(ad != null)
		{
			ad.execute();
		}
		
        super.onResume();
	}
	
	public void onPause()
	{
		if(ad != null)
		{
			ad.destroy();
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