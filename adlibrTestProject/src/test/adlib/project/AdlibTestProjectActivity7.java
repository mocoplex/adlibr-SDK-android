package test.adlib.project;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.mocoplex.adlib.AdlibActivity;
import com.mocoplex.adlib.AdlibInterstitialView;
import com.mocoplex.adlib.AdlibManager;

public class AdlibTestProjectActivity7 extends AdlibActivity {

	LinearLayout layout;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.main7);
        
        // 각 애드립 액티비티에 애드립 앱 키값을 필수로 넣어주어야 합니다.
        setAdlibKey(AdlibTestProjectConstans.ADLIB_API_KEY);
        // 테스트 광고 노출로, 상용일 경우 꼭 제거해야 합니다.
        setAdlibTestMode(AdlibTestProjectConstans.ADLIB_TEST_MODE);
        
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        
        layout = (LinearLayout) findViewById(R.id.full_layout);
        
        // 애드립 전면배너 뷰 요청 (비율지정 등의 문제로 지정된 크기로는 지원하지 않습니다.)
        // 뷰의 제공은 3D 광고를 지원하지 않습니다.
        // 이미지 광고는 세로형을 지원하지 않기에 액티비티를 세로(portrait)로 고정하여 이용하시기 바랍니다.
        requestInterstitialView(new Handler(){
			public void handleMessage(Message message) {
	    		try
	    		{
	    			switch (message.what) {
			   			case AdlibManager.DID_SUCCEED:
			   				AdlibInterstitialView adlibInterView = (AdlibInterstitialView) message.obj;
			   				
			   				layout.addView(adlibInterView);
			   		    	break;
			   			case AdlibManager.DID_ERROR:
			   				Toast.makeText(AdlibTestProjectActivity7.this, "광고수신 실패 :)", Toast.LENGTH_SHORT).show();		   				
			   				break;
		    		}
	    		}
	    		catch(Exception e)
	    		{
	    			
	    		}
	    	}
        });
	}
}

