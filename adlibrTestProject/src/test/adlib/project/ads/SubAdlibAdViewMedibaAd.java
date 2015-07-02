/*
 * adlibr - Library for mobile AD mediation.
 * http://adlibr.com
 * Copyright (c) 2012-2013 Mocoplex, Inc.  All rights reserved.
 * Licensed under the BSD open source license.
 */

/*
 * confirmed compatible with mediba ad SDK 2.0.7
 */

package test.adlib.project.ads;

import mediba.ad.sdk.android.openx.MasAdListener;
import mediba.ad.sdk.android.openx.MasAdView;

import com.mocoplex.adlib.SubAdlibAdViewCore;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.Gravity;

/*
 AndroidManifest.xml 에 아래 내용을 추가해주세요.

 <activity android:name="mediba.ad.sdk.android.openx.MasAdClickWebview" />
 */

public class SubAdlibAdViewMedibaAd extends SubAdlibAdViewCore {
	
	protected MasAdView ad;
	protected boolean bGotAd = false;
	
	// 여기에 mediba ad ID 를 입력하세요.
	protected String medibaAdID = "MEDIBA_AD_ID";

	public SubAdlibAdViewMedibaAd(Context context) {
		this(context,null);
	}	
	
	public SubAdlibAdViewMedibaAd(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		initMedibaAdView();
	}
	
	public void initMedibaAdView() {
		ad = new MasAdView(this.getContext());
		ad.setSid(medibaAdID);
		ad.setAdListener(new MasAdListener() {

			@Override
			public void onFailedToReceiveAd() {
				
				bGotAd = true;
				failed();
			}

			@Override
			public void onFailedToReceiveRefreshedAd() {
				
			}

			@Override
			public void onInternalBrowserClose() {
				
			}

			@Override
			public void onInternalBrowserOpen() {
				
			}

			@Override
			public void onReceiveAd() {
				
				bGotAd = true;
				// 광고를 받아왔으면 이를 알려 화면에 표시합니다.
				gotAd();
			}

			@Override
			public void onReceiveRefreshedAd() {
				
			}
			
		});
		
		// 광고 뷰의 위치 속성을 제어할 수 있습니다.
		this.setGravity(Gravity.CENTER);

		this.addView(ad);
	}
	
	// 스케줄러에의해 자동으로 호출됩니다.
	// 실제로 광고를 보여주기 위하여 요청합니다.	
	public void query() {
		bGotAd = false;
		
		if(ad == null)
			initMedibaAdView();
		
		queryAd();
		
		ad.start();
		
		// 3초 이상 리스너 응답이 없으면 다음 플랫폼으로 넘어갑니다.
		Handler adHandler = new Handler();
		adHandler.postDelayed(new Runnable() {

			@Override
			public void run() {
				if(bGotAd){
					return;
				}else{
					if(ad != null)
						ad.stop();
					failed();
				}
			}
		
		}, 3000);
	}

	// 광고뷰가 사라지는 경우 호출됩니다. 
	public void clearAdView() {
		if(ad != null){
			this.removeView(ad);
			ad.destroy();
			ad = null;
		}
		
		super.clearAdView();
	}
	
	public void onResume() {
		if(ad != null){
			ad.start();
		}
		
        super.onResume();
	}
	
	public void onPause() {
		if(ad != null){
			ad.stop();
		}
		
        super.onPause();
	}
	
	public void onDestroy() {
		if(ad != null){
			ad.destroy();
			ad = null;
		}
        
        super.onDestroy();
	}
}