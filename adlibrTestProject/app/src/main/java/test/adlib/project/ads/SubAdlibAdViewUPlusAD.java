/*
 * adlibr - Library for mobile AD mediation.
 * http://adlibr.com
 * Copyright (c) 2012-2013 Mocoplex, Inc.  All rights reserved.
 * Licensed under the BSD open source license.
 */

/*
 * confirmed compatible with U+AD SDK 3.0.5
 */

package test.adlib.project.ads;

import kr.co.uplusad.dmpcontrol.LGUDMPAdView;
import kr.co.uplusad.dmpcontrol.OnAdListener;

import com.mocoplex.adlib.SubAdlibAdViewCore;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;

/*
 AndroidManifest.xml 에 아래 내용을 추가해주세요.

 <activity android:name="kr.co.uplusad.dmpcontrol.LGUDMPActivity" 
 	android:theme="@android:style/Theme.Translucent.NoTitleBar.Fullscreen" 
 	android:configChanges="keyboard|keyboardHidden|orientation"/>
 */

public class SubAdlibAdViewUPlusAD extends SubAdlibAdViewCore {
	
	protected LGUDMPAdView ad;
	protected boolean bGotAd = false;
	
	// 여기에 UPLUS ID 를 입력하세요.
	protected String uplusID = "UPLUS_ID";
	protected static String uplusInterstitialID = "UPLUS_INTERSTITIAL_ID";
	protected static int REQUEST_CODE = 101;
    
	public SubAdlibAdViewUPlusAD(Context context) {
		this(context,null);
	}
	
	public SubAdlibAdViewUPlusAD(Context context, AttributeSet attrs) {
		
		super(context, attrs);
		
		initUPlusAdView();
	}
	
	public void initUPlusAdView() {
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
	public void query() {
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
				if(bGotAd){
					return;
				}else{
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
	public void clearAdView() {
		if(ad != null){
        	this.removeView(ad);
        	ad.destroy();
        	ad = null;
		}
		
        super.clearAdView();
	}
	
	public void onResume() {
        super.onResume();
	}
	
	public void onPause() {
        super.onPause();
	}
	
	public void onDestroy() {
		if(ad != null){
			ad.destroy();
			ad = null;
		}
		
		super.onDestroy();
	}
	
	/*  주의 : U+AD 전면광고는 광고 수신 성공, 실패에 대한 Listener가 존재하지 않으므로 스케줄링이 정상적으로 동작하지 않을 수 있습니다.
	 *        광고가 닫혔을 때 이벤트 처리가 필요하신 경우, 전면광고를 호출하는 Activity에서 아래와 같이 별도 구현이 필요합니다.
	 *        protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	 *		  {
	 *		      if(requestCode == REQUEST_CODE) 
	 *            {
	 *			      Toast.makeText(this, "Uplus Fullscreen Ad Result: adClose", Toast.LENGTH_SHORT).show();
	 *			  }
	 *		      super.onActivityResult(requestCode, resultCode, data);
	 *        }
	 */
	public static void loadInterstitial(Context ctx, final Handler h, final String adlibKey) {
		LGUDMPAdView.fullscreenImage((Activity)ctx, REQUEST_CODE, uplusInterstitialID, false, null);
	}
}