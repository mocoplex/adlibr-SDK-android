package test.adlib.project.etc;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.mocoplex.adlib.AdError;
import com.mocoplex.adlib.AdlibIconAd;

import kr.co.gapping.GappingAdListener;
import kr.co.gapping.GappingConstans;
import kr.co.gapping.GappingConstans.AdEvent;
import kr.co.gapping.GappingConstans.AdStatus;
import test.adlib.project.AdlibTestProjectConstants;
import test.adlib.project.R;

public class AdlibIconAdActivity extends Activity {

    private AdlibIconAd ad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adlib_icon);

        // 광고 객체 초기화
        ad = new AdlibIconAd(this, AdlibTestProjectConstants.ADLIB_API_KEY);
        // 테스트 광고 노출로, 상용일 경우 꼭 제거해야 합니다.
        ad.setTestMode(AdlibTestProjectConstants.ADLIB_TEST_MODE);
        // 광고 위치 설정 (LEFT, RIGHT)
        ad.setAlign(GappingConstans.FloatingAlign.RIGHT);
        // 광고 이벤트 리스너 등록
        ad.setAdListener(new GappingAdListener() {

            @Override
            public void onReceiveAd() {
                Log.d("ADLIBr", "[Icon] onReceiveAd ");
            }

            @Override
            public void onFailedToReceiveAd(AdError error) {
                Log.d("ADLIBr", "[Icon] onFailedToReceiveAd " + error.getErrorCode() + " / " + error.getErrorMessage());
                Toast.makeText(AdlibIconAdActivity.this, "광고수신 실패 :)", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onReceivedInteraction(String data) {

            }

            @Override
            public void onChangeStatus(AdStatus status) {

            }

            @Override
            public void onReceiveEvent(AdEvent event) {

            }

        });
    }

    @Override
    protected void onResume() {
        if (ad != null) {
            ad.resumeAd();
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        if (ad != null) {
            ad.pauseAd();
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if (ad != null) {
            ad.destroyAd();
        }
        super.onDestroy();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if (ad != null) {
            ad.reloadAd();
        }
        super.onConfigurationChanged(newConfig);
    }

    public void onClick(View v) {
        ad.loadAd();
    }
}
