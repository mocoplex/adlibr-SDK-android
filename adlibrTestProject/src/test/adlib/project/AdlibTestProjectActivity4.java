package test.adlib.project;
import com.mocoplex.adlib.AdlibAdViewContainer;
import com.mocoplex.adlib.AdlibManager;
import com.mocoplex.adlib.AdlibManager.AdlibVersionCheckingListener;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;

public class AdlibTestProjectActivity4 extends Activity {
    
	// 일반 Activity 에서의 adlib 연동	
	private AdlibManager _amanager;

	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		_amanager = new AdlibManager();
		_amanager.onCreate(this);

		// 일반적인 연동은 추가적으로 구현필요
		setContentView(R.layout.main2);
		this.setAdsContainer(R.id.ads);   
	}

	protected void onResume()
	{		
		_amanager.onResume(this);
		super.onResume();
	}
	
	protected void onPause()
	{    	
		_amanager.onPause();
		super.onPause();
	}
    
	protected void onDestroy()
	{    	
		_amanager.onDestroy(this);
		super.onDestroy();
	}

	// xml 에 지정된 ID 값을 이용하여 BIND 하는 경우
	public void setAdsContainer(int rid)
	{
		_amanager.setAdsContainer(rid);
	}
	
	// 동적으로 Container 를 생성하여, 그 객체를 통하여 BIND 하는 경우
	public void bindAdsContainer(AdlibAdViewContainer a)
	{
		_amanager.bindAdsContainer(a);		
	}
	
	// 전면광고 호출
	public void loadInterstitialAd()
	{
		_amanager.loadInterstitialAd(this);
	}
			
	// 전면광고 호출 (광고 수신 성공, 실패 여부를 받고 싶을 때 handler 이용)
	public void loadInterstitialAd(Handler h)
	{
		_amanager.loadInterstitialAd(this, h);
	}
	
	public void setVersionCheckingListner(AdlibVersionCheckingListener l)
	{
		_amanager.setVersionCheckingListner(l);		
	}
	
	// AD 영역을 동적으로 삭제할때 호출하는 메소드
	public void destroyAdsContainer()
	{
		_amanager.destroyAdsContainer();
	}	
	// 애드립 연동에 필요한 구현부 끝    
}
