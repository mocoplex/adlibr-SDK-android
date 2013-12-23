/*
 * adlibr - Library for mobile AD mediation.
 * http://adlibr.com
 * Copyright (c) 2012-2013 Mocoplex, Inc.  All rights reserved.
 * Licensed under the BSD open source license.
 */

/*
 * confirmed compatible with T ad SDK 3.3.3.6
 */

package test.adlib.project.ads;

import com.mocoplex.adlib.SubAdlibAdViewCore;
import com.skplanet.tad.AdListener;
import com.skplanet.tad.AdView.AnimationType;
import com.skplanet.tad.AdView.Slot;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.Gravity;

public class SubAdlibAdViewTAD extends SubAdlibAdViewCore  {
	
	protected com.skplanet.tad.AdView ad;
	protected boolean bGotAd = false;
	
	// 여기에 T-AD 에서 발급받은 id 를 입력하세요.
	String tAdId = "T_AD_ID";
	
	public SubAdlibAdViewTAD(Context context) {
		this(context,null);
        
	}
	
	public SubAdlibAdViewTAD(Context context, AttributeSet attrs) {
		super(context, attrs);
		
        initTadView();
	}
    
    public void initTadView()
    {
		ad = new com.skplanet.tad.AdView(this.getContext());
		ad.setClientId(tAdId);
		ad.setSlotNo(Slot.BANNER);
		
		/*  새로운 받은 광고가 Display 되는 Animation 효과를 설정합니다.
		 * NONE							효과없음
		 * FADE     					Fade 효과
		 * ￼￼ZOOM ￼ ￼ ￼ ￼ 					Zoom 효과
		 * ROTATE						회전 효과
		 * ￼￼SLIDE_FROM_RIGHT_TO_LEFT ￼ ￼ ￼ ￼ 오른쪽에서 왼쪽으로 나타남
		 * SLIDE_FROM_LEFT_TO_RIGHT		왼쪽에서 오른쪽으로 나타남
		 * ￼￼SLIDE_FROM_BOTTOM_TO_TOP ￼ ￼ ￼ ￼ 아래에서 위쪽으로 나타남
		 * SLIDE_FROM_TOP_TO_BOTTOM		위쪽에서 아래쪽으로 나타남
		 * FLIP_HORIZONTAL				가로로 접기 효과
		 * FLIP_VERTICAL				세로로 접기 효과
		 * ROTATE3D_180_HORIZONTAL		가로로 3D 회전 효과
		 * ROTATE3D_180_VERTICAL		세로로 3D 회전 효과
		 */
		ad.setAnimationType(AnimationType.NONE);
		// 새로운 광고를 요청하는 주기를 입력합니다. 최소값은 15, 최대값은 60 입니다. 0으로 설정한 경우 1번의 광고만 수신합니다.(mediation 사용하는 경우엔 0으로 설정)
		ad.setRefreshInterval(0);
		/* 광고 View 의 Background 의 사용 유무를 설정합니다.
		 * useBackFill 속성을 true 로 설정하면 각 광고마다 광고주가 설정한 배경색이 그려지고 false 인 경우 투명(0x00000000)으로 나타납니다. */
		ad.setUseBackFill(true);
		// TestMode 를 정합니다. true 인경우 test 광고가 수신됩니다.
		ad.setTestMode(false);
		ad.setListener(new AdListener(){
			
			@Override
			public void onAdReceived() {
				
				bGotAd = true;
				// 광고를 받아왔으면 이를 알려 화면에 표시합니다.
				gotAd();
			}
			
			@Override
			public void onAdFailed(ErrorCode arg0) {
				
				bGotAd = true;
				// 광고 수신에 실패하여 다음 플랫폼을 호출합니다.
				failed();
			}
            
			@Override
			public void onAdClicked() {
				
			}
            
			@Override
			public void onAdExpandClosed() {
				
			}
            
			@Override
			public void onAdExpanded() {
				
			}
            
			@Override
			public void onAdLoaded() {
				
			}
            
			@Override
			public void onAdResizeClosed() {
				
			}
            
			@Override
			public void onAdResized() {
				
			}
            
			@Override
			public void onAdWillLoad() {
				
			}
            
			@Override
			public void onAdWillReceive() {
				
			}
			
			@Override
			public void onAdDismissScreen() {
				
			}
			
			@Override
			public void onAdLeaveApplication() {
				
			}
			
			@Override
			public void onAdPresentScreen() {
				
			}
			
		});
		
		// 광고 뷰의 위치 속성을 제어할 수 있습니다.
		this.setGravity(Gravity.CENTER);
		
		this.addView(ad);
    }
    
	public void query()
	{
		bGotAd = false;
		
		if(ad == null)
            initTadView();
        
        queryAd();
        
		ad.loadAd();
		
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
						SubAdlibAdViewTAD.this.removeView(ad);
						ad.destroyAd();
						ad = null;
					}
					failed();
				}
			}
			
		}, 3000);
	}
	
	public void clearAdView()
	{
        // T-ad SDK 3.0 이후로 화면에 광고뷰가 보이지 않을 때 반드시 destroy를 시켜야 합니다.
        if(ad != null)
        {
            this.removeView(ad);
            ad.destroyAd();
            ad = null;
        }
        
        super.clearAdView();
	}
	
	public void onResume()
	{
		if(ad != null)
		{
			ad.loadAd();
		}
		
		super.onResume();
	}
	
	public void onPause()
	{
		if(ad != null)
		{
			ad.stopAd();
		}
		
		super.onPause();
	}
	
	public void onDestroy()
	{
		if(ad != null)
		{
			ad.destroyAd();
            ad = null;
		}
		
		super.onDestroy();
	}
}