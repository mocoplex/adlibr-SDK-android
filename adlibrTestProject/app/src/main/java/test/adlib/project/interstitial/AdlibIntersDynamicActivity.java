package test.adlib.project.interstitial;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mocoplex.adlib.AdlibManager;

import test.adlib.project.AdlibTestProjectConstants;
import test.adlib.project.R;

public class AdlibIntersDynamicActivity extends Activity {

    private AdlibManager adlibManager;

    private View adView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adlib_inters_dynamic);

        // 각 애드립 액티비티에 애드립 앱 키값을 필수로 넣어주어야 합니다.
        adlibManager = new AdlibManager(AdlibTestProjectConstants.ADLIB_API_KEY);
        adlibManager.onCreate(this);
        // 테스트 광고 노출로, 상용일 경우 꼭 제거해야 합니다.
        adlibManager.setAdlibTestMode(AdlibTestProjectConstants.ADLIB_TEST_MODE);
    }

    @Override
    protected void onResume() {
        adlibManager.onResume(this);
        super.onResume();
    }

    @Override
    protected void onPause() {
        adlibManager.onPause(this);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        adlibManager.onDestroy(this);
        super.onDestroy();
    }

    public void onClick(View v) {
        if (v.getId() == R.id.request) {
            requestAd();
        } else if (v.getId() == R.id.addview) {
            addView();
        }

    }

    // 전면배너 Dynamic 광고 요청
    private void requestAd() {
        Handler handler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                try {
                    switch (msg.what) {
                        case AdlibManager.DID_SUCCEED:
                            Log.d("ADLIBr", "[Dynamic] onReceiveAd ");
                            adView = (View) msg.obj;
                            break;
                        case AdlibManager.DID_ERROR:
                            Log.d("ADLIBr", "[Dynamic] onFailedToReceiveAd " + (String) msg.obj);
                            Toast.makeText(AdlibIntersDynamicActivity.this, "광고수신 실패 :)", Toast.LENGTH_SHORT).show();
                            break;

                        case AdlibManager.DID_CLICK:
                            Log.d("ADLIBr", "[Dynamic] onClickAd");
                            break;
                    }
                } catch (Exception e) {

                }
            }

        };
        // 다이나믹 광고의 경우 미디에이션을 지원하지 않습니다
        // 애드립 전면배너 기본 사이즈 320 * 480
        // 테스트 광고는 포함된 테스트 키에서만 기본 사이즈 광고 노출
        if (adlibManager != null) adlibManager.requestDynamicIntersView(320, 480, handler);
    }

    // 뷰가 노출 되어야하는 시점에 호출
    private void addView() {
        if (adView != null) {
            ViewGroup vg = (ViewGroup) findViewById(R.id.container);
            vg.addView(adView);
        }
    }
}
