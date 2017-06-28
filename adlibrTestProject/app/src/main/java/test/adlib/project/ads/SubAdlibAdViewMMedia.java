/*
 * adlibr - Library for mobile AD mediation.
 * http://adlibr.com
 * Copyright (c) 2012-2013 Mocoplex, Inc.  All rights reserved.
 * Licensed under the BSD open source license.
 */

/*
 * confirmed compatible with MillennialMedia SDK 6.3.1
 */

package test.adlib.project.ads;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;

import com.millennialmedia.InlineAd;
import com.millennialmedia.InlineAd.InlineErrorStatus;
import com.millennialmedia.InterstitialAd;
import com.millennialmedia.InterstitialAd.InterstitialErrorStatus;
import com.millennialmedia.MMException;
import com.millennialmedia.MMSDK;
import com.mocoplex.adlib.AdlibManager;
import com.mocoplex.adlib.SubAdlibAdViewCore;

/*
 AndroidManifest.xml 에 아래 내용을 추가해주세요.
 
 <uses-permission android:name="android.permission.RECORD_AUDIO" />

 <activity android:name="com.millennialmedia.android.MMActivity"
	android:theme="@android:style/Theme.Translucent.NoTitleBar"
	android:configChanges="keyboardHidden|orientation|keyboard" >
 </activity>
 */

public class SubAdlibAdViewMMedia extends SubAdlibAdViewCore {
	
	protected InlineAd ad;
	protected boolean bGotAd = false;
	
	// 여기에 MMEDIA ID 를 입력하세요.
	protected String mMediaID = "MILLENNIALMEDIA_ID";
	protected static String mMediaInterstitialID = "MILLENNIALMEDIA_INTERSTITIAL_ID";
	
	protected static Handler intersHandler = null;

	public SubAdlibAdViewMMedia(Context context) {
		this(context,null);
	}	
	
	public SubAdlibAdViewMMedia(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		MMSDK.initialize((Activity)context);
		initMmediaView();
	}
	
	public void initMmediaView()
	{
		
		try {
			ad = InlineAd.createInstance(mMediaID, this);
			
			ad.setListener(new InlineAd.InlineListener() {
				
				@Override
				public void onResized(InlineAd arg0, int arg1, int arg2, boolean arg3) {
					
				}
				
				@Override
				public void onResize(InlineAd arg0, int arg1, int arg2) {
					
				}
				
				@Override
				public void onRequestSucceeded(InlineAd arg0) {
					bGotAd = true;
					// 광고를 받아왔으면 이를 알려 화면에 표시합니다.
					gotAd();
				}
				
				@Override
				public void onRequestFailed(InlineAd arg0, InlineErrorStatus arg1) {
					bGotAd = true;
					failed();
				}
				
				@Override
				public void onExpanded(InlineAd arg0) {
					
				}
				
				@Override
				public void onCollapsed(InlineAd arg0) {
					
				}
				
				@Override
				public void onClicked(InlineAd arg0) {
				}
				
				@Override
				public void onAdLeftApplication(InlineAd arg0) {
					
				}
			});
			
		} catch (MMException e) {
			e.printStackTrace();
		}
		
	}
	
	// 스케줄러에의해 자동으로 호출됩니다.
	// 실제로 광고를 보여주기 위하여 요청합니다.	
	public void query() {
		bGotAd = false;
		
		if(ad == null)
			initMmediaView();
		
		queryAd();
		
//		ad.getAd();
		
		ad.setRefreshInterval(30000);
		// The InlineAdMetadata instance is used to pass additional metadata to the server to
		// improve ad selection
		final InlineAd.InlineAdMetadata inlineAdMetadata = new InlineAd.InlineAdMetadata().setAdSize(InlineAd.AdSize.BANNER);
		ad.request(inlineAdMetadata);
		
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
//			this.removeView(ad);
			ad = null;
		}
		
		super.clearAdView();
	}
	
	public void onResume(){       
        super.onResume();
	}
	
	public void onPause(){
        super.onPause();
	}
	
	public void onDestroy(){
		if(ad != null){
//			this.removeView(ad);
			ad = null;
		}
        
        super.onDestroy();
	}
	
	public static void loadInterstitial(final Context ctx, final Handler h, final String adlibKey) {
		try {
			final InterstitialAd interstitialAd = InterstitialAd.createInstance(mMediaInterstitialID);
			
			if(interstitialAd != null){
				interstitialAd.load(ctx, null);
			}
			
			interstitialAd.setListener(new InterstitialAd.InterstitialListener() {
				
				@Override
				public void onShown(InterstitialAd arg0) {
					try{
						if(intersHandler != null){
			 				intersHandler.sendMessage(Message.obtain(intersHandler, AdlibManager.DID_SUCCEED, "MMEDIA"));
			 			}
					}catch(Exception e){
					}
				}
				
				@Override
				public void onShowFailed(InterstitialAd arg0, InterstitialErrorStatus arg1) {
					
				}
				
				@Override
				public void onLoaded(InterstitialAd arg0) {
					if(interstitialAd.isReady()){
						try{
							interstitialAd.show(ctx);
						}catch(MMException e){
							e.printStackTrace();
						}
					}else{
						
					}
				}
				
				@Override
				public void onLoadFailed(InterstitialAd arg0, InterstitialErrorStatus arg1) {
					try{
						if(intersHandler != null){
		 					intersHandler.sendMessage(Message.obtain(intersHandler, AdlibManager.DID_ERROR, "MMEDIA"));
		 				}
					}catch(Exception e){
					}
				}
				
				@Override
				public void onExpired(InterstitialAd arg0) {
					
				}
				
				@Override
				public void onClosed(InterstitialAd arg0) {
					try{
						if(intersHandler != null){
		 					intersHandler.sendMessage(Message.obtain(intersHandler, AdlibManager.INTERSTITIAL_CLOSED, "MMEDIA"));
		 				}
					}catch(Exception e){
					}
				}
				
				@Override
				public void onClicked(InterstitialAd arg0) {
				}
				
				@Override
				public void onAdLeftApplication(InterstitialAd arg0) {
					
				}
			});
			
		} catch (MMException e) {
			e.printStackTrace();
		}
		
	}
}