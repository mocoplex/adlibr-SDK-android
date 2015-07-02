package test.adlib.project;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.mocoplex.adlib.AdlibAdViewContainer;
import com.mocoplex.adlib.AdlibManager;
import com.mocoplex.adlib.AdlibManager.AdlibVersionCheckingListener;

public class AdlibTestProjectActivity4 extends Activity {
    
	// 일반 Activity 에서의 adlib 연동	
	private AdlibManager _amanager;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// 각 애드립 액티비티에 애드립 앱 키값을 필수로 넣어주어야 합니다.
		_amanager = new AdlibManager(AdlibTestProjectConstans.ADLIB_API_KEY);
		_amanager.onCreate(this);
		// 테스트 광고 노출로, 상용일 경우 꼭 제거해야 합니다.
		_amanager.setAdlibTestMode(AdlibTestProjectConstans.ADLIB_TEST_MODE);
		
		_amanager.setAdsHandler(new Handler() {
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

		// 일반적인 연동은 추가적으로 구현필요
		setContentView(R.layout.main4);
		this.setAdsContainer(R.id.ads);
	}

	protected void onResume() {		
		_amanager.onResume(this);
		super.onResume();
	}
	
	protected void onPause() {    	
		_amanager.onPause(this);
		super.onPause();
	}
    
	protected void onDestroy() {    	
		_amanager.onDestroy(this);
		super.onDestroy();
	}

	// xml 에 지정된 ID 값을 이용하여 BIND 하는 경우
	public void setAdsContainer(int rid) {
		_amanager.setAdsContainer(rid);
	}
	
	// 동적으로 Container 를 생성하여, 그 객체를 통하여 BIND 하는 경우
	public void bindAdsContainer(AdlibAdViewContainer a) {
		_amanager.bindAdsContainer(a);		
	}
	
	// 전면광고 호출
	public void loadFullInterstitialAd() {
		_amanager.loadFullInterstitialAd(this);
	}
		
	// 전면광고 호출 (광고 수신 성공, 실패 여부를 받고 싶을 때 handler 이용)
	public void loadFullInterstitialAd(Handler h) {
		_amanager.loadFullInterstitialAd(this, h);
	}
	
	// 전면광고 프리로드 요청
	public void requestInterstitial() {
		_amanager.requestInterstitial();
	}
	
	// 전면광고 프리로드 표출
	public void showInterstitial() {
		_amanager.showInterstitial();
	}
		
	// 전면광고 프리로드 표출 (광고 수신 성공, 실패 여부를 받고 싶을 때 handler 이용)
	public void showInterstitial(Handler h) {
		_amanager.showInterstitial(h);
	}
	
	public void setVersionCheckingListner(AdlibVersionCheckingListener l) {
		_amanager.setVersionCheckingListner(l);		
	}
	
	// AD 영역을 동적으로 삭제할때 호출하는 메소드
	public void destroyAdsContainer() {
		_amanager.destroyAdsContainer();
	}
	// 애드립 연동에 필요한 구현부 끝    
}
