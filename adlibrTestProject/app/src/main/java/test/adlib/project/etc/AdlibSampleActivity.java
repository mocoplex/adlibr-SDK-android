package test.adlib.project.etc;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.mocoplex.adlib.AdlibActivity;
import com.mocoplex.adlib.AdlibManager;

import test.adlib.project.AdlibTestProjectConstants;
import test.adlib.project.R;

public class AdlibSampleActivity extends AdlibActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adlib_sample);

        // 각 애드립 액티비티에 애드립 앱 키값을 필수로 넣어주어야 합니다.
        setAdlibKey(AdlibTestProjectConstants.ADLIB_API_KEY);

        // 테스트 광고 노출로, 상용일 경우 꼭 제거해야 합니다.
        setAdlibTestMode(AdlibTestProjectConstants.ADLIB_TEST_MODE);
        // 배너 스케쥴에 등록된 광고 모두 광고 요청 실패 시 대기 시간 설정(단위:초, 기본:10초, 최소:1초)
        // setBannerFailDelayTime(10);

        // 배너 스케쥴 요청 실패 시 대기 시간동안 노출되는 View 설정
        // View backFill = new View(this);
        // backFill.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        // setBannerBackfillView(backFill);

        // 띠배너 광고 이벤트 리스터 등록
        setAdsHandler(new Handler() {
            public void handleMessage(Message message) {
                try {
                    switch (message.what) {
                        case AdlibManager.DID_SUCCEED:
                            Log.d("ADLIBr", "[Banner] onReceiveAd " + (String) message.obj);
                            break;
                        case AdlibManager.DID_ERROR:
                            Log.d("ADLIBr", "[Banner] onFailedToReceiveAd " + (String) message.obj);
                            Toast.makeText(AdlibSampleActivity.this, "광고수신 실패 :)", Toast.LENGTH_SHORT).show();
                            break;
                        case AdlibManager.BANNER_FAILED:
                            Log.d("ADLIBr", "[Banner] All Failed.");
                            break;
                    }
                } catch (Exception e) {

                }
            }
        });

        // 띠배너 미디에이션 뷰 설정 및 시작
        setAdsContainer(R.id.ads);
    }

    private void loadInterstitialAd() {
        // 전면광고를 호출합니다. (미디에이션 전면배너 요청)
        //loadFullInterstitialAd(this);

        // optional : 전면광고의 수신 성공, 실패 이벤트 처리가 필요한 경우엔 handler를 이용하실 수 있습니다. (미디에이션 전면배너 요청)
        loadFullInterstitialAd(new Handler() {
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
                            Toast.makeText(AdlibSampleActivity.this, "광고수신 실패 :)", Toast.LENGTH_SHORT).show();
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

    private void requestAdlibDialog() {
        // 다이얼로그 광고 요청
        requestAdDialog(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case AdlibManager.DID_SUCCEED:
                        break;

                    case AdlibManager.DID_ERROR:
                        break;
                }
            }
        });
    }

    private void showAdlibDialog() {
        // 다이얼로그 광고 사용 가능 여부 확인
        // 광고가 없는 경우 타 플랫폼이나 매체 내부 동작 처리가 필요한 경우 사용가능한 함수
        // adlibManager.isAvailableAdDialog();

        // 종료 대화상자 광고를 노출하기 위해서 호출합니다.
        showAdDialog("취소", "확인", "종료하시겠습니까?");

        // 필요시 아래와 같이 색상과 클릭 액션을 변경할 수 있습니다.
        //backgroundColor, backgroundColor(click), textColor, textColor, textColor(click), lineColor
        // int[] colors = new int[]{0xffffffff, 0xffa8a8a8, 0xff404040, 0xff404040, 0xffdfdfdf};
        // adlibManager.showAdDialog("취소", "확인", "종료하시겠습니까?", colors, new AdlibDialogAdListener() {
        //
        //     @Override
        //     public void onLeftClicked() {
        //     }
        //
        //     @Override
        //     public void onRightClicked() {
        //     }
        //
        // });
    }

}
