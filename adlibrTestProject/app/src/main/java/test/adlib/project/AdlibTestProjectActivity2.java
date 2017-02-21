package test.adlib.project;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.mocoplex.adlib.AdlibActivity;
import com.mocoplex.adlib.AdlibManager;

public class AdlibTestProjectActivity2 extends AdlibActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main2);

        // 각 애드립 액티비티에 애드립 앱 키값을 필수로 넣어주어야 합니다.
        setAdlibKey(AdlibTestProjectConstants.ADLIB_API_KEY);
        // 테스트 광고 노출로, 상용일 경우 꼭 제거해야 합니다.
        setAdlibTestMode(AdlibTestProjectConstants.ADLIB_TEST_MODE);

        // 배너 스케쥴에 등록된 광고 모두 요청 실패 시 대기 시간 설정(단위:초, 기본:10초, 최소:5초)
        // this.setBannerFailDelayTime(10);

        // 배너 스케쥴 요청 실패 시 대기 시간동안 노출되는 View 설정
        // View backFill = new View(this);
        // backFill.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        // this.setBannerBackfillView(backFill);

        this.setAdsHandler(new Handler() {
            public void handleMessage(Message message) {
                try {
                    switch (message.what) {
                        case AdlibManager.DID_SUCCEED:
                            Log.d("ADLIBr", "[Banner] onReceiveAd " + (String) message.obj);
                            break;
                        case AdlibManager.DID_ERROR:
                            Log.d("ADLIBr", "[Banner] onFailedToReceiveAd " + (String) message.obj);
                            break;
                        // 띠배너 스케줄로 설정되어있는 모든 플랫폼의 수신이 실패했을 경우 이벤트를 받습니다.
                        case AdlibManager.BANNER_FAILED:
                            Log.d("ADLIBr", "[Banner] All Failed.");
                            break;
                    }
                } catch (Exception e) {

                }
            }
        });

        setAdsContainer(R.id.ads);
    }
}