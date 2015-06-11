package test.adlib.project;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.mocoplex.adlib.AdlibActivity;
import com.mocoplex.adlib.AdlibManager;

public class AdlibTestProjectActivity2 extends AdlibActivity {
	
	// 애드립 광고를 테스트 하기 위한 키 입니다.
	private String ADLIB_API_KEY = "53858972e4b0ef94c0636d85";
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main2);
        
        // 각 애드립 액티비티에 애드립 앱 키값을 필수로 넣어주어야 합니다.
        setAdlibKey(ADLIB_API_KEY);
        // 테스트 광고 노출로, 상용일 경우 꼭 제거해야 합니다.
        setAdlibTestMode(true);
        
        this.setAdsHandler(new Handler() {
			public void handleMessage(Message message) {
	    		try
	    		{
	    			switch (message.what) {
	                    case AdlibManager.DID_SUCCEED:
	                        Log.d("ADLIBr", "[Banner] onReceiveAd " + (String)message.obj);
	                        break;
	                    case AdlibManager.DID_ERROR:
	                        Log.d("ADLIBr", "[Banner] onFailedToReceiveAd " + (String)message.obj);
	                        break;
		    		}
	    		}
	    		catch(Exception e)
	    		{
	    			
	    		}
	    	}
		});
        
        setAdsContainer(R.id.ads);
    }
}