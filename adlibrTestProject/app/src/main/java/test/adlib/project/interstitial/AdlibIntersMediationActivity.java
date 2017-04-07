package test.adlib.project.interstitial;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.mocoplex.adlib.AdlibManager;

import test.adlib.project.AdlibTestProjectConstants;
import test.adlib.project.R;

public class AdlibIntersMediationActivity extends Activity {

    private AdlibManager adlibManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adlib_inters_mediation);

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

        // 전면광고를 호출합니다. (미디에이션 전면배너 요청)
        //adlibManager.loadFullInterstitialAd(this);

        // optional : 전면광고의 수신 성공, 실패 이벤트 처리가 필요한 경우엔 handler를 이용하실 수 있습니다. (미디에이션 전면배너 요청)
        adlibManager.loadFullInterstitialAd(this, new Handler() {
            public void handleMessage(Message message) {
                try {
                    switch (message.what) {
                        case AdlibManager.DID_SUCCEED:
                            Log.d("ADLIBr", "[Interstitial] onReceiveAd " + (String) message.obj);
                            break;
                        // 전면배너 스케줄링 사용시, 각각의 플랫폼의 수신 실패 이벤트를 받습니다.
                        case AdlibManager.DID_ERROR:
                            Log.d("ADLIBr", "[Interstitial] onFailedToReceiveAd " + (String) message.obj);
                            break;
                        // 전면배너 스케줄로 설정되어있는 모든 플랫폼의 수신이 실패했을 경우 이벤트를 받습니다.
                        case AdlibManager.INTERSTITIAL_FAILED:
                            Toast.makeText(AdlibIntersMediationActivity.this, "광고수신 실패 :)", Toast.LENGTH_SHORT).show();
                            Log.d("ADLIBr", "[Interstitial] All Failed.");
                            break;
                        case AdlibManager.INTERSTITIAL_CLOSED:
                            Log.d("ADLIBr", "[Interstitial] onClosedAd " + (String) message.obj);
                            break;
                    }
                } catch (Exception e) {
                }
            }
        });
    }
}
