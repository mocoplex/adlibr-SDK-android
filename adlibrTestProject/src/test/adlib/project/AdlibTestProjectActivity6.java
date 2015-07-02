package test.adlib.project;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.mocoplex.adlib.AdlibActivity;

public class AdlibTestProjectActivity6 extends AdlibActivity {
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main6);
        
        // 각 애드립 액티비티에 애드립 앱 키값을 필수로 넣어주어야 합니다.
        setAdlibKey(AdlibTestProjectConstans.ADLIB_API_KEY);
        // 테스트 광고 노출로, 상용일 경우 꼭 제거해야 합니다.
        setAdlibTestMode(AdlibTestProjectConstans.ADLIB_TEST_MODE);
        
        Button btn1 = (Button) findViewById(R.id.btn1);
        btn1.setText("requestInterstitial");
        
        btn1.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// 프리로드 전면광고 요청
				requestInterstitial();
			}
		});
        
        Button btn2 = (Button) findViewById(R.id.btn2);
        btn2.setText("showInterstitial");
        
        btn2.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// 프리로드한 전면배너를 표출합니다.
				// 실패한 경우는 노출 되지 않습니다.
				// 성공을 한 광고의 경우에도 인터넷이 연결되지 않았거나 
				// 10분을 초과한 경우 노출되지 않습니다.
				if(isLoadedInterstitial()){
					showInterstitial();
					
					/*
					 * optional : 전면광고의 이벤트 처리가 필요한 경우엔 handler를 이용하실 수 있습니다.
					showInterstitial(new Handler() {
						public void handleMessage(Message message) {
				    		try
				    		{
				    			switch (message.what) {
				    				case AdlibManager.DID_SUCCEED:
				                        Log.d("ADLIBr", "[Preload Interstitial] onReceiveAd ");
				                        break;
				                    case AdlibManager.DID_ERROR:
				                        Log.d("ADLIBr", "[Preload Interstitial] onFailedToReceiveAd ");
				                        break;
				    				case AdlibManager.INTERSTITIAL_SHOWED:
				                        Log.d("ADLIBr", "[Preload Interstitial] onShowedAd ");
				                        break;
				                    case AdlibManager.INTERSTITIAL_CLOSED:
				                        Log.d("ADLIBr", "[Preload Interstitial] onClosedAd ");
				                        break;
					    		}
				    		}
				    		catch(Exception e)
				    		{
				    			
				    		}
				    	}
					});
					*/
				}else{
					Toast.makeText(AdlibTestProjectActivity6.this, "광고수신 실패 :)", Toast.LENGTH_SHORT).show();
					
					// 필요시 재요청을 합니다.
					requestInterstitial();
				}
			}
		});
    }
}
