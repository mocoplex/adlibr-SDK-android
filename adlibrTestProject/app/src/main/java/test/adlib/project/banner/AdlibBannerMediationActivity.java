package test.adlib.project.banner;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mocoplex.adlib.AdlibAdViewContainer;
import com.mocoplex.adlib.AdlibManager;

import test.adlib.project.AdlibTestProjectConstants;
import test.adlib.project.R;

public class AdlibBannerMediationActivity extends Activity {

    private AdlibManager adlibManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adlib_banner_mediation);

        // 각 애드립 액티비티에 애드립 앱 키값을 필수로 넣어주어야 합니다.
        adlibManager = new AdlibManager(AdlibTestProjectConstants.ADLIB_API_KEY);
        adlibManager.onCreate(this);
        // 테스트 광고 노출로, 상용일 경우 꼭 제거해야 합니다.
        adlibManager.setAdlibTestMode(AdlibTestProjectConstants.ADLIB_TEST_MODE);
        // 배너 스케쥴에 등록된 광고 모두 광고 요청 실패 시 대기 시간 설정(단위:초, 기본:10초, 최소:1초)
        // adlibManager.setBannerFailDelayTime(10);

        // 배너 스케쥴 요청 실패 시 대기 시간동안 노출되는 View 설정
        // View backFill = new View(this);
        // backFill.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        // adlibManager.setBannerBackfillView(backFill);

        adlibManager.setAdsHandler(new Handler() {
            public void handleMessage(Message message) {
                try {
                    switch (message.what) {
                        case AdlibManager.DID_SUCCEED:
                            Log.d("ADLIBr", "[Banner] onReceiveAd " + (String) message.obj);
                            break;
                        case AdlibManager.DID_ERROR:
                            Log.d("ADLIBr", "[Banner] onFailedToReceiveAd " + (String) message.obj);
                            break;
                        case AdlibManager.BANNER_FAILED:
                            Log.d("ADLIBr", "[Banner] All Failed.");
                            Toast.makeText(AdlibBannerMediationActivity.this, "광고수신 실패 :)", Toast.LENGTH_SHORT).show();
                            break;
                    }
                } catch (Exception e) {

                }
            }
        });

        // 띠배너 미디에이션 뷰 설정 및 시작
        adlibManager.setAdsContainer(R.id.ads);
    }

    private AdlibAdViewContainer getAdlibAdViewContainer() {
        // 액티비티에 AdlibAdViewContainer 는 하나만 허용합니다.
        // 두개 이상 로드될 경우 문제가 발생할 수 있으며
        // 새로 로드하는 경우 반드시 기존 광고뷰를 삭제하고 다시 바인드 해주세요.

        // 동적으로 adview 를 생성합니다.
        AdlibAdViewContainer avc = new com.mocoplex.adlib.AdlibAdViewContainer(this);
        ViewGroup.LayoutParams p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        avc.setLayoutParams(p);

        return avc;
    }

    // 동적으로 Container 를 생성하여, 그 객체를 통하여 BIND 하는 경우
    private void bindAdsContainer(AdlibAdViewContainer a) {
        adlibManager.bindAdsContainer(a);
    }

    // AD 영역을 동적으로 삭제할때 호출하는 메소드
    private void destroyAdsContainer() {
        adlibManager.destroyAdsContainer();
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
}
