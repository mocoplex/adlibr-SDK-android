package test.adlib.project;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.mocoplex.adlib.AdlibConfig.ContentType;
import com.mocoplex.adlib.AdlibManager;
import com.mocoplex.adlib.nativead.layout.AdlibNativeLayout;
import com.mocoplex.adlib.platform.nativeads.AdlibNativeAdListener;
import com.mocoplex.adlib.platform.nativeads.AdlibNativeItem;

public class AdlibTestProjectActivity9 extends Activity {

    // 일반 Activity 에서의 adlib 연동
    private AdlibManager _amanager;
    private FrameLayout layout = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main9);

        layout = (FrameLayout) findViewById(R.id.full_layout);

        // 각 애드립 액티비티에 애드립 앱 키값을 필수로 넣어주어야 합니다.
        _amanager = new AdlibManager(AdlibTestProjectConstants.ADLIB_API_KEY);
        _amanager.onCreate(this);
        // 테스트 광고 노출로, 상용일 경우 꼭 제거해야 합니다.
        _amanager.setAdlibTestMode(AdlibTestProjectConstants.ADLIB_TEST_MODE);

        // 네이티브 광고를 받고싶다면 아래의 코드를 호출 합니다.
        // 1. 타입 : 광고 유형을 지정합니다. (ALL : 비디오와 이미지, VIDEO : 비디오만 해당, IMAGE : 이미지만 해당)
        // 2. 이벤트 리스너 : 성공과 실패를 알기 위한 리스너를 지정합니다.
        _amanager.loadNativeAd(ContentType.ALL, new AdlibNativeAdListener() {
            @Override
            public void onReceiveAd(AdlibNativeItem item) {
                Log.d("ADLIB-Native", "onReceiveAd");

                AdlibNativeLayout anl = new AdlibNativeLayout(AdlibTestProjectActivity9.this, R.layout.main9_item);
                anl.setAdsData(item);

                layout.removeView(anl);
                anl.setVisibility(View.VISIBLE);
                layout.addView(anl);

                // 슬라이드 효과
                TranslateAnimation trans = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT,
                        0.0f, Animation.RELATIVE_TO_PARENT, 1.0f, Animation.RELATIVE_TO_PARENT, 0.0f);

                trans.setDuration(1000);
                trans.setFillAfter(true);
                trans.setAnimationListener(new AnimationListener() {

                    @Override
                    public void onAnimationEnd(Animation arg0) {
                    }

                    @Override
                    public void onAnimationRepeat(Animation arg0) {

                    }

                    @Override
                    public void onAnimationStart(Animation arg0) {

                    }
                });
                anl.startAnimation(trans);
            }

            @Override
            public void onError(int errorCode) {
                Log.d("ADLIB-Native", "onError ::: error code : " + errorCode);

                Toast.makeText(AdlibTestProjectActivity9.this, "광고수신 실패 :)", Toast.LENGTH_SHORT).show();
            }
        });
    }

    protected void onResume() {
        _amanager.onResume(this);
        super.onResume();
    }

    protected void onPause() {
        _amanager.onPause(this);
        super.onPause();
    }

    protected void onDestroy() {
        _amanager.onDestroy(this);
        super.onDestroy();
    }
}