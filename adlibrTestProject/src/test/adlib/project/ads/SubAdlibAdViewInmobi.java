/*
 * adlibr - Library for mobile AD mediation.
 * http://adlibr.com
 * Copyright (c) 2012-2013 Mocoplex, Inc.  All rights reserved.
 * Licensed under the BSD open source license.
 */

/*
 * confirmed compatible with Inmobi SDK 3.7.1
 */

package test.adlib.project.ads;

import java.util.HashMap;
import java.util.Map;

import com.inmobi.androidsdk.IMAdListener;
import com.inmobi.androidsdk.IMAdRequest;
import com.inmobi.androidsdk.IMAdRequest.ErrorCode;
import com.inmobi.androidsdk.IMAdView;
import com.mocoplex.adlib.SubAdlibAdViewCore;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;

/*
 AndroidManifest.xml 에 아래 내용을 추가해주세요.

 <activity android:name="com.inmobi.androidsdk.IMBrowserActivity"
 android:configChanges="keyboardHidden|orientation|keyboard|smallestScreenSize|screenSize"
 android:hardwareAccelerated="true" /> 
 */

public class SubAdlibAdViewInmobi extends SubAdlibAdViewCore  {
	
	protected IMAdView ad;
	private IMAdRequest mAdRequest;
	protected boolean bGotAd = false;

	// 여기에 인모비에서 발급받은 key 를 입력하세요.
	String inmobiKey = "INMOBI_APP_ID";

	private int getPixels(int dipValue) {
        Resources r = getResources();
        int px = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, 
        r.getDisplayMetrics());
        return px;
	}
	
	public SubAdlibAdViewInmobi(Context context) {
		this(context,null);
	}	
	
	public SubAdlibAdViewInmobi(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		initInmobiView();
	}
	
	public void initInmobiView()
	{
		// 원하는 크기의 배너 크기를 설정하세요.
		ad = new IMAdView((Activity) this.getContext(), IMAdView.INMOBI_AD_UNIT_320X50, inmobiKey);
		ad.disableHardwareAcceleration();
		LayoutParams params = new LayoutParams(getPixels(320),getPixels(50));
		ad.setLayoutParams(params);

		// 광고 뷰의 위치 속성을 제어할 수 있습니다. 
		this.setGravity(Gravity.CENTER);
		
		mAdRequest = new IMAdRequest();
		// set the test mode to true (Make sure you set the test mode to false when distributing to the users)
		//mAdRequest.setTestMode(true);
		
		Map<String,String> reqParams = new HashMap<String,String>();
		reqParams.put("tp","c_adlib");
		mAdRequest.setRequestParams(reqParams);
		
		ad.setIMAdRequest(mAdRequest);

		// set the listener if the app has to know ad status notifications
		ad.setIMAdListener(new IMAdListener() {

			@Override
			public void onShowAdScreen(IMAdView adView) {
				
			}

			@Override
			public void onDismissAdScreen(IMAdView adView) {
			}

			@Override
			public void onAdRequestFailed(IMAdView adView, ErrorCode errorCode) {
				
				bGotAd = true;
				failed();
			}

			@Override
			public void onAdRequestCompleted(IMAdView adView) {
				
				bGotAd = true;
				ad.setVisibility(View.VISIBLE);
				// 광고를 받아왔으면 이를 알려 화면에 표시합니다.
				gotAd();
			}
			
			@Override
			public void onLeaveApplication(IMAdView adView) {
			}
		});
	}
	
	// 스케줄러에의해 자동으로 호출됩니다.
	// 실제로 광고를 보여주기 위하여 요청합니다.	
	public void query()
	{
		if(ad == null)
			initInmobiView();
		
		this.removeAllViews();
		ad.setVisibility(View.GONE);
		this.addView(ad);
		
		queryAd();
		
		ad.loadNewAd();
		
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
					SubAdlibAdViewInmobi.this.removeView(ad);
					ad.destroy();
					ad = null;
                    bGotAd = false;
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
			ad.destroy();
			ad = null;
		}
		
		super.onDestroy();
	}
}
