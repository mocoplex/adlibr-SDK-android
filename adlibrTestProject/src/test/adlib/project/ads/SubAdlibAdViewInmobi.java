/*
 * adlibr - Library for mobile AD mediation.
 * http://adlibr.com
 * Copyright (c) 2012 Mocoplex, Inc.  All rights reserved.
 * Licensed under the BSD open source license.
 */

/*
 * confirmed compatible with Inmobi SDK 350
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
import android.util.AttributeSet;
import android.util.TypedValue;

/*
 AndroidManifest.xml 에 아래 내용을 추가해주세요.

 <activity android:name="com.inmobi.androidsdk.IMBrowserActivity"
 android:configChanges="keyboardHidden|orientation|keyboard" /> 
 */

public class SubAdlibAdViewInmobi extends SubAdlibAdViewCore  {
	
	protected IMAdView ad;
	private IMAdRequest mAdRequest;
	protected boolean bGotAd = false;
    
	private IMAdListener mIMAdListener = new IMAdListener() {

		@Override
		public void onShowAdScreen(IMAdView adView) {
		}

		@Override
		public void onDismissAdScreen(IMAdView adView) {
		}

		@Override
		public void onAdRequestFailed(IMAdView adView, ErrorCode errorCode) {
            if(bGotAd)
                return;
    
			failed();
		}

		@Override
		public void onAdRequestCompleted(IMAdView adView) {
			gotAd();
			bGotAd = true;
		}
		
		@Override
		public void onLeaveApplication(IMAdView adView) {
		}
	};

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
		
		// 여기에 인모비에서 발급받은 key 를 입력하세요.
        // 마찬가지로 애드립의 스케줄 설정창에도 발급받은 키를 입력해주세요.
        // 같은 키를 소스파일, 애드립 페이지 두 곳에 입력해야 리워드 포인트를 적립받을 수 있습니다.
		String inmobiKey = "INMOBI ID";

		// Get the IMAdView instance
		
		// 원하는 크기의 배너 크기를 설정하세요.
		// http://developer.inmobi.com/wiki/index.php?title=Integration_Guidelines#Android_2
		ad = new IMAdView((Activity) context, IMAdView.INMOBI_AD_UNIT_320X50, inmobiKey);
		LayoutParams params = new LayoutParams(getPixels(320),getPixels(50));
		ad.setLayoutParams(params);

		this.addView(ad);
	}
	
	// 스케줄러에의해 자동으로 호출됩니다.
	// 실제로 광고를 보여주기 위하여 요청합니다.	
	public void query()
	{
		// set the test mode to true (Make sure you set the test mode to false
		// when distributing to the users)
		mAdRequest = new IMAdRequest();
		//mAdRequest.setTestMode(true);

		// 애드립 리워드 포인트 적립을 위해 필요한 코드입니다. -- 삭제하지 마세요.
		Map<String,String> reqParams = new HashMap<String,String>();
		reqParams.put("tp","c_adlib");
		mAdRequest.setRequestParams(reqParams);		
		// 애드립 리워드 포인트 적립을 위해 필요한 코드입니다. -- 삭제하지 마세요.
		
		ad.setIMAdRequest(mAdRequest);

		// set the listener if the app has to know ad status notifications
		ad.setIMAdListener(mIMAdListener);
		
		ad.loadNewAd();
		
		if(bGotAd)
			gotAd();		
	}
	
	// 광고뷰를 삭제하는 경우 호출됩니다. 
	public void clearAdView()
	{
		if(ad != null)
		{
		}

		super.clearAdView();
	}
	
	public void onResume()
	{
		super.onResume();
		
		if(ad != null)
		{
		}
	}
	public void onPause()
	{
		super.onPause();
		
		if(ad != null)
		{
		}		
	}
}
