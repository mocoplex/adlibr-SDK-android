package test.adlib.project;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.mocoplex.adlib.AdlibActivity;
import com.mocoplex.adlib.AdlibManager;

public class AdlibTestProjectActivity5 extends AdlibActivity {
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main5);
        
        // 각 애드립 액티비티에 애드립 앱 키값을 필수로 넣어주어야 합니다.
        setAdlibKey(AdlibTestProjectConstans.ADLIB_API_KEY);
        // 테스트 광고 노출로, 상용일 경우 꼭 제거해야 합니다.
        setAdlibTestMode(AdlibTestProjectConstans.ADLIB_TEST_MODE);
        
        Button btn = (Button) findViewById(R.id.btn);
        btn.setText("loadFullInterstitialAd");
        
        btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// 전면광고를 호출합니다. (미디에이션 전면배너 요청)
//				loadFullInterstitialAd();
			
				// optional : 전면광고의 수신 성공, 실패 이벤트 처리가 필요한 경우엔 handler를 이용하실 수 있습니다. (미디에이션 전면배너 요청)
				loadFullInterstitialAd(new Handler() {
					public void handleMessage(Message message) {
			    		try {
			    			switch (message.what) {
			                    case AdlibManager.DID_SUCCEED:
			                        Log.d("ADLIBr", "[Interstitial] onReceiveAd " + (String)message.obj);
			                        break;
			                    // 전면배너 스케줄링 사용시, 각각의 플랫폼의 수신 실패 이벤트를 받습니다.
			                    case AdlibManager.DID_ERROR:
			                    	Toast.makeText(AdlibTestProjectActivity5.this, "광고수신 실패 :)", Toast.LENGTH_SHORT).show();
			                        Log.d("ADLIBr", "[Interstitial] onFailedToReceiveAd " + (String)message.obj);
			                        break;
			                    // 전면배너 스케줄로 설정되어있는 모든 플랫폼의 수신이 실패했을 경우 이벤트를 받습니다.
			                    case AdlibManager.INTERSTITIAL_FAILED:
			                    	Log.d("ADLIBr", "[Interstitial] All Failed.");
			                    	break;
			                    case AdlibManager.INTERSTITIAL_CLOSED:
			                        Log.d("ADLIBr", "[Interstitial] onClosedAd " + (String)message.obj);
			                        break;
				    		}
			    		}catch(Exception e){
			    		}
			    	}
				});
			}
		});
    }
}