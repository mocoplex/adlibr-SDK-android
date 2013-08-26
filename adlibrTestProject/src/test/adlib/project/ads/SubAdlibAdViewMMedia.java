/*
 * adlibr - Library for mobile AD mediation.
 * http://adlibr.com
 * Copyright (c) 2012-2013 Mocoplex, Inc.  All rights reserved.
 * Licensed under the BSD open source license.
 */

/*
 * confirmed compatible with MillennialMedia SDK 5.1.0
 */

package test.adlib.project.ads;

import com.millennialmedia.android.MMAd;
import com.millennialmedia.android.MMAdView;
import com.millennialmedia.android.MMException;
import com.millennialmedia.android.MMSDK;
import com.millennialmedia.android.RequestListener;
import com.mocoplex.adlib.SubAdlibAdViewCore;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.LinearLayout;

/*
 AndroidManifest.xml 에 아래 내용을 추가해주세요.
 
 <uses-permission android:name="android.permission.RECORD_AUDIO" />

 <activity android:name="com.millennialmedia.android.MMActivity"
	 android:theme="@android:style/Theme.Translucent.NoTitleBar" 
	 android:configChanges="keyboardHidden|orientation|keyboard" />
 <activity android:name="com.millennialmedia.android.VideoPlayer"
	 android:configChanges="keyboardHidden|orientation|keyboard" /> 
 */

public class SubAdlibAdViewMMedia extends SubAdlibAdViewCore  {
	
	protected MMAdView ad;
	protected boolean bGotAd = false;
	
	// 여기에 MMEDIA ID 를 입력하세요.
	String mMediaID = "MILLENNIALMEDIA_ID";

	public SubAdlibAdViewMMedia(Context context) {
		this(context,null);
	}	
	
	public SubAdlibAdViewMMedia(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		initMmediaView();
	}
	
	public void initMmediaView()
	{
		ad = new MMAdView(this.getContext());
		ad.setApid(mMediaID);
		ad.setId(MMSDK.getDefaultAdId());
		ad.setWidth(320);
		ad.setHeight(50);
		int adWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (float) 320.0, getResources().getDisplayMetrics());
		int adHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (float) 50.0, getResources().getDisplayMetrics());
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(adWidth, adHeight);
		ad.setLayoutParams(params);
		
		ad.setListener(new RequestListener(){

			@Override
			public void MMAdOverlayClosed(MMAd mmAd) {
				
			}

			@Override
			public void MMAdOverlayLaunched(MMAd mmAd) {
				
			}

			@Override
			public void MMAdRequestIsCaching(MMAd mmAd) {
				
			}

			@Override
			public void onSingleTap(MMAd mmAd) {
				
			}

			@Override
			public void requestCompleted(MMAd mmAd) {
				
				bGotAd = true;
				// 광고를 받아왔으면 이를 알려 화면에 표시합니다.
				gotAd();
			}

			@Override
			public void requestFailed(MMAd mmAd, MMException exception) {
				
				bGotAd = true;
				failed();
			}
		});
		
		// 광고 뷰의 위치 속성을 제어할 수 있습니다.
		this.setGravity(Gravity.CENTER);

		this.addView(ad);
	}
	
	// 스케줄러에의해 자동으로 호출됩니다.
	// 실제로 광고를 보여주기 위하여 요청합니다.	
	public void query()
	{
		bGotAd = false;
		
		if(ad == null)
			initMmediaView();
		
		queryAd();
		
		ad.getAd();
		
		// 3초 이상 리스너 응답이 없으면 다음 플랫폼으로 넘어갑니다.
		Handler adHandler = new Handler();
		adHandler.postDelayed(new Runnable() {

			@Override
			public void run() {
				if(bGotAd)
					return;
				else
				{
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
			ad = null;
		}
		
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
	
	public void onDestroy()
	{
		if(ad != null)
		{
			this.removeView(ad);
			ad = null;
		}
        
        super.onDestroy();
	}
}