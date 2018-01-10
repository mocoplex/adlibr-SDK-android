/*
 * adlibr - Library for mobile AD mediation.
 * http://adlibr.com
 * Copyright (c) 2012-2013 Mocoplex, Inc.  All rights reserved.
 * Licensed under the BSD open source license.
 */

/*
 * confirmed compatible with AdFit SDK 2.4.1
 */

package test.adlib.project.ads;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

import com.kakao.adfit.ads.AdListener;
import com.kakao.adfit.ads.ba.BannerAdView;
import com.mocoplex.adlib.SubAdlibAdViewCore;

/*
 * ADFit SDK 업데이트로 광고 요청이 증가 할 수 있습니다.
 */
public class SubAdlibAdViewAdam extends SubAdlibAdViewCore {

    protected BannerAdView ad;
    protected boolean bGotAd = false;

    // 여기에 ADAM ID 를 입력하세요.
    protected String adamID = "Adam_ID";

    public SubAdlibAdViewAdam(Context context) {
        this(context, null);
    }

    public SubAdlibAdViewAdam(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAdamView();
    }

    public void initAdamView() {
        // AdFit 광고 뷰 생성 및 설정
        ad = new BannerAdView(getContext());

        // 킷캣 디바이스에서 렌더링에 생기는 문제로 인한 예외처리.
        // 에러표시가 생기면 disalbe check하시고 무시하셔도 무방합니다.
        if (Build.VERSION.SDK_INT == 19) {
            ad.setLayerType(LAYER_TYPE_SOFTWARE, null);
        }

        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        ad.setLayoutParams(params);

        ad.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                bGotAd = true;
                // 광고를 받아왔으면 이를 알려 화면에 표시합니다.
                gotAd();
            }

            @Override
            public void onAdFailed(int code) {
                bGotAd = true;
                failed();
            }

            @Override
            public void onAdClicked() {
            }
        });

        // 할당 받은 clientId 설정
        ad.setClientId(adamID);

        // Adlib 관련 사항 : 반복 시간 관련 검토 필수 ADfit의 기능
        // 광고 갱신 시간 : 기본 60초
        // 0 으로 설정할 경우, 갱신하지 않음.
        ad.setRequestInterval(0);

        // 광고 사이즈 설정
        ad.setAdUnitSize("320x50");
        ad.setVisibility(View.VISIBLE);

        this.addView(ad);
    }

    // 스케줄러에의해 자동으로 호출됩니다.
    // 실제로 광고를 보여주기 위하여 요청합니다.
    public void query() {
        //AdfitSDK-2.4.0 부터 특정 단말에서 광고를 요청하지 않는 현상으로 추가
        setVisibility(View.VISIBLE);

        bGotAd = false;

        if (ad == null) initAdamView();

        queryAd();

        ad.loadAd();

        // 3초 이상 리스너 응답이 없으면 다음 플랫폼으로 넘어갑니다.
        Handler adHandler = new Handler();
        adHandler.postDelayed(new Runnable() {

            @Override
            public void run() {
                if (bGotAd) {
                    return;
                } else {
                    if (ad != null) ad.setRequestInterval(0);

                    failed();
                }
            }

        }, 3000);
    }

    // 광고뷰가 사라지는 경우 호출됩니다.
    public void clearAdView() {
        if (ad != null) {
            ad.destroy();
            this.removeView(ad);
            ad = null;
        }

        super.clearAdView();
    }

    public void onResume() {
        if (ad != null) {
            ad.resume();
        }
        super.onResume();
    }

    public void onPause() {
        if (ad != null) {
            ad.pause();
        }

        super.onPause();
    }

    public void onDestroy() {
        if (ad != null) {
            ad.destroy();
            ad = null;
        }

        super.onDestroy();
    }
}