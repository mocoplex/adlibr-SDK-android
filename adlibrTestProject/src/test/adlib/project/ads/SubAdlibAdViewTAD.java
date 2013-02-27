/*
 * adlibr - Library for mobile AD mediation.
 * http://adlibr.com
 * Copyright (c) 2012 Mocoplex, Inc.  All rights reserved.
 * Licensed under the BSD open source license.
 */

/*
 * confirmed compatible with T ad SDK 3.0.2.6
 */

package test.adlib.project.ads;

import com.mocoplex.adlib.SubAdlibAdViewCore;
import com.skplanet.tad.AdListener;
import com.skplanet.tad.AdView.AnimationType;
import com.skplanet.tad.AdView.Slot;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;

public class SubAdlibAdViewTAD extends SubAdlibAdViewCore  {
	
	protected com.skplanet.tad.AdView ad;
	protected boolean bGotAd = false;
	
	public SubAdlibAdViewTAD(Context context) {
		this(context,null);
		
		// 여기에 T-AD 에서 발급받은 id 를 입력하세요.
		String tAdId = "T_AD_ID";
		
		// 광고 뷰의 위치 속성을 제어할 수 있습니다.
		this.setGravity(Gravity.CENTER);
		
		ad = new com.skplanet.tad.AdView(this.getContext());
		ad.setClientId(tAdId);
		ad.setSlotNo(Slot.RICHMEDIA_320X50_INLINE);
		
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
		ad.setAnimationType(AnimationType.ROTATE3D_180_VERTICAL);
		// 새로운 광고를 요청하는 주기를 입력합니다. 최소값은 15, 최대값은 60 입니다
		ad.setRefreshInterval(20);
		/* 광고 View 의 Background 의 사용 유무를 설정합니다.
		 * useBackFill 속성을 true 로 설정하면 각 광고마다 광고주가 설정한 배경색이 그려지고 false 인 경우 투명(0x00000000)으로 나타납니다. */
		ad.setUseBackFill(true);
		// TestMode 를 정합니다. true 인경우 test 광고가 수신됩니다.
		ad.setTestMode(false);
		ad.setListener(new AdListener(){
			
			@Override
			public void onAdReceived() {
				gotAd();
				bGotAd = true;
			}
			
			@Override
			public void onAdFailed(ErrorCode arg0) {
				// 광고 수신에 실패했다. 바로 다음 플랫폼을 보인다.
				if(!bGotAd)
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
			
		});
		
		this.addView(ad);
	}
	
	public SubAdlibAdViewTAD(Context context, AttributeSet attrs) {
		super(context, attrs);
		
	}
    
	public void query()
	{
		ad.startAd();
		
		if(bGotAd)
			gotAd();
	}
	
	public void clearAdView()
	{
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
		super.onDestroy();
		
		if(ad != null)
		{
			ad.destroyAd();
		}
	}
}