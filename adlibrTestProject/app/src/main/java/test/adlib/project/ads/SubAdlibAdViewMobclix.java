/*
 * adlibr - Library for mobile AD mediation.
 * http://adlibr.com
 * Copyright (c) 2012-2013 Mocoplex, Inc.  All rights reserved.
 * Licensed under the BSD open source license.
 */

/*
 * confirmed compatible with Axonix(mobclix) SDK 4.3.0
 */

package test.adlib.project.ads;

import com.axonix.android.sdk.AxonixAdView;
import com.axonix.android.sdk.AxonixAdViewListener;
import com.axonix.android.sdk.AxonixFullScreenAdViewListener;
import com.axonix.android.sdk.AxonixFullScreenAdView;
import com.axonix.android.sdk.AxonixMMABannerXLAdView;
import com.mocoplex.adlib.AdlibManager;
import com.mocoplex.adlib.SubAdlibAdViewCore;
import com.tnkfactory.ad.TnkSession;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.Gravity;

/*
 AndroidManifest.xml 에 아래 내용을 추가해주세요.

 <meta-data android:name="com.mobclix.APPLICATION_ID"
	android:value="insert-your-application-key" />
 <activity android:name="com.mobclix.android.sdk.MobclixBrowserActivity"
	android:theme="@android:style/Theme.Translucent.NoTitleBar"
	android:hardwareAccelerated="true" />
 */

public class SubAdlibAdViewMobclix extends SubAdlibAdViewCore {
	
	protected AxonixMMABannerXLAdView ad;
	protected boolean bGotAd = false;

	public SubAdlibAdViewMobclix(Context context) {
		this(context,null);
	}	
	
	public SubAdlibAdViewMobclix(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		initAxonixView();
	}
	
	public void initAxonixView() {
		ad = new AxonixMMABannerXLAdView(getContext());
		
		ad.addAxonixAdViewListener(new AxonixAdViewListener() {
			
			public String keywords() {
				return null;
			}

			public void onAdClick(AxonixAdView arg0) {
				
			}

			public void onCustomAdTouchThrough(AxonixAdView arg0, String arg1) {
				
			}

			public void onFailedLoad(AxonixAdView arg0, int arg1) {
				bGotAd = true;
				failed();
			}

			public boolean onOpenAllocationLoad(AxonixAdView arg0, int arg1) {
				return false;
			}

			public void onSuccessfulLoad(AxonixAdView arg0) {
				bGotAd = true;
				// 광고를 받아왔으면 이를 알려 화면에 표시합니다.
				gotAd();
			}

			public String query() {
				return null;
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
			initAxonixView();
		
		queryAd();
		
		ad.getAd();
		
		// 3초 이상 리스너 응답이 없으면 다음 플랫폼으로 넘어갑니다.
		Handler adHandler = new Handler();
		adHandler.postDelayed(new Runnable() {

			@Override
			public void run() {
				if(bGotAd){
					return;
				}else{
					failed();
				}
			}
		
		}, 3000);
	}

	// 광고뷰가 사라지는 경우 호출됩니다. 
	public void clearAdView() {
		if(ad != null){
			ad.pause();
			this.removeView(ad);
			ad = null;
		}
		
		super.clearAdView();
	}
	
	public void onResume() {
		if(ad != null){
			ad.getAd();
		}
		
        super.onResume();
	}
	
	public void onPause() {
		if(ad != null){
			ad.pause();
		}
		
        super.onPause();
	}
	
	public void onDestroy() {
		if(ad != null){
			ad.pause();
			this.removeView(ad);
			ad = null;
		}
        
        super.onDestroy();
	}
	
	public static void loadInterstitial(final Context ctx, final Handler h, final String adlibKey){
		final AxonixFullScreenAdView adInters = new AxonixFullScreenAdView((Activity)ctx);
		
		adInters.requestAd();
		
		adInters.addAxonixAdViewListener(new AxonixFullScreenAdViewListener() {

			@Override
			public String keywords() {
				return null;
			}

			@Override
			public void onDismissAd(AxonixFullScreenAdView inters) {
				
			}

			@Override
			public void onFailedLoad(AxonixFullScreenAdView inters, int arg1) {
				try{
	  				if(h != null){
	  					h.sendMessage(Message.obtain(h, AdlibManager.DID_ERROR, "Axonix(Mobclix)"));
	  				}
	  				
	 			}catch(Exception e){
	 				
	 			}
			}

			@Override
			public void onFinishLoad(AxonixFullScreenAdView inters) {
				try{
	 				if(h != null){
	 					h.sendMessage(Message.obtain(h, AdlibManager.INTERSTITIAL_CLOSED, "Axonix(Mobclix)"));
	 				}
	 				
	 			}catch(Exception e){
	 				
	 			}
			}

			@Override
			public void onPresentAd(AxonixFullScreenAdView inters) {
				try{
	        		 if(h != null){
	  					h.sendMessage(Message.obtain(h, AdlibManager.DID_SUCCEED, "Axonix(Mobclix)"));
	  				}
	        		 
	        		 adInters.displayRequestedAd();
	        		 
	        	 }catch(Exception e){
	        		 
	        	 }
			}

			@Override
			public String query() {
				return null;
			}

		});
		
	}

	
}