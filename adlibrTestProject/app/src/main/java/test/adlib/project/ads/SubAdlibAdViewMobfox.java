/*
 * adlibr - Library for mobile AD mediation.
 * http://adlibr.com
 * Copyright (c) 2012-2013 Mocoplex, Inc.  All rights reserved.
 * Licensed under the BSD open source license.
 */

/*
 * confirmed compatible with mobfox SDK v3.2.5
*/
package test.adlib.project.ads;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;

import com.mobfox.sdk.bannerads.Banner;
import com.mobfox.sdk.bannerads.BannerListener;
import com.mobfox.sdk.interstitialads.InterstitialAd;
import com.mobfox.sdk.interstitialads.InterstitialAdListener;
import com.mocoplex.adlib.AdlibManager;
import com.mocoplex.adlib.SubAdlibAdViewCore;

/*
AndroidManifest.xml 에 아래 내용을 추가해주세요.

<activity android:name="com.mobfox.sdk.interstitialads.InterstitialActivity"></activity>
*/

public class SubAdlibAdViewMobfox extends SubAdlibAdViewCore {

    protected boolean bGotAd = false;

    private Banner banner;

    // 여기에 MOBFOX ID 를 입력하세요.
    protected String mofoxBannerID = "MOBFOX_ID";
    protected static String mofoxInterstitialID = "MOBFOX_INTERSTITIAL_ID";

    public SubAdlibAdViewMobfox(Context context) {
        this(context, null);
    }

    public SubAdlibAdViewMobfox(Context context, AttributeSet attrs) {
        super(context, attrs);

        initMobfoxView();
    }

    public void initMobfoxView() {
        banner = new Banner(getContext(), 320, 50);

        banner.setListener(new BannerListener() {
            @Override
            public void onBannerError(View banner, Exception e) {
                bGotAd = true;
                failed();
            }

            @Override
            public void onBannerLoaded(View banner) {
                bGotAd = true;
                // 광고를 받아왔으면 이를 알려 화면에 표시합니다.
                gotAd();
            }

            @Override
            public void onBannerClosed(View banner) {
            }

            @Override
            public void onBannerFinished() {
            }

            @Override
            public void onBannerClicked(View banner) {
            }

            @Override
            public void onNoFill(View banner) {
            }
        });
        banner.setInventoryHash(mofoxBannerID);

        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        banner.setLayoutParams(params);
        this.setGravity(Gravity.CENTER);
        this.addView(banner);
    }

    // 스케줄러에의해 자동으로 호출됩니다.
    // 실제로 광고를 보여주기 위하여 요청합니다.
    public void query() {
        bGotAd = false;
        if (banner == null)
            initMobfoxView();

        banner.load();
        queryAd();

        // 3초 이상 리스너 응답이 없으면 다음 플랫폼으로 넘어갑니다.
        Handler adHandler = new Handler();
        adHandler.postDelayed(new Runnable() {

            @Override
            public void run() {
                if (bGotAd) {
                    return;
                } else {
                    if (banner != null)
                        banner.onPause();
                    failed();
                }
            }

        }, 3000);
    }

    // 광고뷰가 사라지는 경우 호출됩니다.
    public void clearAdView() {
        if (banner != null) {
            this.removeView(banner);
            banner.onPause();
            banner = null;
        }

        super.clearAdView();
    }

    public void onResume() {
        if (banner != null) {
            banner.onResume();
        }
        super.onResume();
    }

    public void onPause() {
        if (banner != null) {
            banner.onPause();
        }
        super.onPause();
    }

    public void onDestroy() {
        if (banner != null) {
            banner = null;
        }
        this.removeAllViews();

        super.onDestroy();
    }


    // 전면광고가 호출되는 경우
    public static void loadInterstitial(Context ctx, final Handler h, final String adlibKey) {
        InterstitialAd interstitial = new InterstitialAd(ctx);

        InterstitialAdListener listener = new InterstitialAdListener() {
            @Override
            public void onInterstitialLoaded(InterstitialAd interstitial) {
                try {
                    if (h != null) {
                        h.sendMessage(Message.obtain(h, AdlibManager.DID_SUCCEED, "MOBFOX"));
                    }
                } catch (Exception e) {
                }
                // 미디에이션 통계 정보
                interstitial.show();
            }

            @Override
            public void onInterstitialFailed(InterstitialAd interstitial, Exception e) {
                try {
                    if (h != null) {
                        h.sendMessage(Message.obtain(h, AdlibManager.DID_ERROR, "MOBFOX"));
                    }
                    // release.
                } catch (Exception exception) {
                }
            }

            @Override
            public void onInterstitialClosed(InterstitialAd interstitial) {
                try {
                    if (h != null) {
                        h.sendMessage(Message.obtain(h, AdlibManager.INTERSTITIAL_CLOSED, "MOBFOX"));
                    }
                } catch (Exception e) {
                }
            }

            @Override
            public void onInterstitialFinished() {

            }

            @Override
            public void onInterstitialClicked(InterstitialAd interstitial) {
            }

            @Override
            public void onInterstitialShown(InterstitialAd interstitial) {
            }
        };
        interstitial.setListener(listener);
        interstitial.setSkip(true);
        interstitial.setInventoryHash(mofoxInterstitialID);

        interstitial.load();
    }

}
