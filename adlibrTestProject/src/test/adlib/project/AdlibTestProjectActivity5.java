package test.adlib.project;

import com.mocoplex.adlib.AdlibActivity;
import com.mocoplex.adlib.AdlibInterstitialView;
import com.mocoplex.adlib.AdlibManager;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.LinearLayout;
import android.widget.Toast;

public class AdlibTestProjectActivity5 extends AdlibActivity {

	LinearLayout layout;
	
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
        setContentView(R.layout.main4);
        
        layout = (LinearLayout) findViewById(R.id.full_layout);
        
        // Full Banner 호출
        loadFullBanner(new Handler() {
			public void handleMessage(Message message) {
	    		try
	    		{
	    			switch (message.what) {
		   			case AdlibManager.DID_SUCCEED:
		   				// 꽉찬 전면배너 View return
		   				AdlibInterstitialView iView = (AdlibInterstitialView)message.obj;
		   				layout.addView(iView);
		   		    	break;
		   			case AdlibManager.DID_ERROR:
		   				
		   				Toast.makeText(AdlibTestProjectActivity5.this, "광고수신 실패 :)", Toast.LENGTH_SHORT).show();		   				
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