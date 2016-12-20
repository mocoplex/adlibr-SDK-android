/*
 * adlibr - Library for mobile AD mediation.
 * http://adlibr.com
 * Copyright (c) 2012-2013 Mocoplex, Inc.  All rights reserved.
 * Licensed under the BSD open source license.
 */

package test.adlib.project.ads;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.mapps.android.listner.AdListner;
import com.mapps.android.view.AdView;
import com.mocoplex.adlib.SubAdlibAdViewCore;

public class SubAdlibAdViewMezzo extends SubAdlibAdViewCore {
	
	protected AdView ad = null;
	protected boolean bPassAd = false;
	
	// 여기에 MEZZO ID 를 입력하세요.
	// 발급받은 지면이 mezzo/Solution/ 형식이면 mezzoID에 입력. 없으면 ""
	protected String mezzoID = "";
	// 발급받은 코드가 34,227,479 형식이면 순서대로 publisherCode, mediaCode, sectionCode에 입력. 없으면 ""
	protected String publisherCode = "";
	protected String mediaCode = "";
	protected String sectionCode = "";

	public SubAdlibAdViewMezzo(Context context) {
		this(context, null);
	}	
	
	public SubAdlibAdViewMezzo(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		ad = new AdView(context, 1, 0, AdView.TYPE_IMAGE);
		if(!publisherCode.equals("") && !mediaCode.equals("") && !sectionCode.equals("")) {
			ad.setAdViewCode(publisherCode, mediaCode, sectionCode);
		}
		ad.setAdListner(new AdListner() {

			@Override
			public void onChargeableBannerType(View v, boolean bcharge) {
				
				if(ad == v){
					// 무료광고 일 경우 다음광고로 넘깁니다.
					if(!bcharge){
						bPassAd = true;
						failed();
					}
				}
			}

			@Override
			public void onFailedToReceive(View v, int errCode) {
				
				if(ad == v){
					if(errCode == 0){
						// 광고 수신 성공
						if(!bPassAd){
							queryAd();
							gotAd();
						}
					}else{
						// 광고 수신 실패
						failed();
					}
				}
			}
		});
	}
	
	// 스케줄러에의해 자동으로 호출됩니다.
	// 실제로 광고를 보여주기 위하여 요청합니다.	
	public void query() {
		bPassAd = false;
		
		this.removeAllViews();
		this.addView(ad);
		
		ad.StartService();
	}

	// 광고뷰가 사라지는 경우 호출됩니다. 
	public void clearAdView() {
		if(ad != null){
			this.removeView(ad);
			ad.StopService();
		}
		
		super.clearAdView();
	}
	
	public void onResume() {
		if(ad != null){
			ad.StartService();
		}
		
        super.onResume();
	}
	
	public void onPause() {
		if(ad != null){
			ad.StopService();
		}
		
        super.onPause();
	}
	
	public void onDestroy() {
		if(ad != null){
			this.removeView(ad);
			ad.StopService();
			ad = null;
		}
        
        super.onDestroy();
	}
}