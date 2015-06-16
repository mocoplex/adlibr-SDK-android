package test.adlib.project;

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.mocoplex.adlib.AdlibConfig;
import com.mocoplex.adlib.AdlibManager;

public class AdlibTestProjectActivity extends ListActivity {
	
	// 일반 Activity 에서의 adlib 연동	
	private AdlibManager _amanager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		initAds();
		
		ArrayList<String> itemList = new ArrayList<String>();
		itemList.add("Banner (Static)");
		itemList.add("Banner (Dynamic)");
		itemList.add("Banner (Unused AdlibActivity)");
		itemList.add("Interstitial (Mediation)");
		itemList.add("Interstitial (Preload)");
		itemList.add("Interstitial (View)");
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, itemList);
		
		this.setListAdapter(adapter);
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		
		Intent intent = null;
		switch (position) {
			case 0: // banner (Static)
				intent = new Intent(AdlibTestProjectActivity.this, AdlibTestProjectActivity2.class);
				startActivity(intent);
				break;
	
			case 1: // banner (Dynamic)
				intent = new Intent(AdlibTestProjectActivity.this, AdlibTestProjectActivity3.class);
				startActivity(intent);
				break;
				
			case 2: // banner (Unused AdlibActivity)
				intent = new Intent(AdlibTestProjectActivity.this, AdlibTestProjectActivity4.class);
				startActivity(intent);
				break;
				
			case 3: // interstitial (Mediation)
				intent = new Intent(AdlibTestProjectActivity.this, AdlibTestProjectActivity5.class);
				startActivity(intent);
				break;
				
			case 4: // interstitial (Preload)
				intent = new Intent(AdlibTestProjectActivity.this, AdlibTestProjectActivity6.class);
				startActivity(intent);
				break;
				
			case 5: // interstitial (View)
				intent = new Intent(AdlibTestProjectActivity.this, AdlibTestProjectActivity7.class);
				startActivity(intent);
				break;
		}
	}
	
	// AndroidManifest.xml에 권한과 activity를 추가하여야 합니다.     
    protected void initAds()
    {
    	// AdlibActivity 를 상속받은 액티비티이거나,
    	// 일반 Activity 에서는 AdlibManager 를 동적으로 생성한 후 아래 코드가 실행되어야 합니다. (AdlibTestProjectActivity4.java)

    	// 광고 스케줄링 설정을 위해 아래 내용을 프로그램 실행시 한번만 실행합니다. (처음 실행되는 activity에서 한번만 호출해주세요.)    	
    	// 광고 subview 의 패키지 경로를 설정합니다. (실제로 작성된 패키지 경로로 수정해주세요.)

    	// 쓰지 않을 광고플랫폼은 삭제해주세요.
        AdlibConfig.getInstance().bindPlatform("ADAM","test.adlib.project.ads.SubAdlibAdViewAdam");
        AdlibConfig.getInstance().bindPlatform("ADMOB","test.adlib.project.ads.SubAdlibAdViewAdmob");
        AdlibConfig.getInstance().bindPlatform("CAULY","test.adlib.project.ads.SubAdlibAdViewCauly");
        AdlibConfig.getInstance().bindPlatform("TAD","test.adlib.project.ads.SubAdlibAdViewTAD");
        AdlibConfig.getInstance().bindPlatform("NAVER","test.adlib.project.ads.SubAdlibAdViewNaverAdPost");
        AdlibConfig.getInstance().bindPlatform("SHALLWEAD","test.adlib.project.ads.SubAdlibAdViewShallWeAd");
        AdlibConfig.getInstance().bindPlatform("INMOBI","test.adlib.project.ads.SubAdlibAdViewInmobi");
        AdlibConfig.getInstance().bindPlatform("MMEDIA","test.adlib.project.ads.SubAdlibAdViewMMedia");
        AdlibConfig.getInstance().bindPlatform("MOBCLIX","test.adlib.project.ads.SubAdlibAdViewMobclix");
        AdlibConfig.getInstance().bindPlatform("UPLUSAD","test.adlib.project.ads.SubAdlibAdViewUPlusAD");
        AdlibConfig.getInstance().bindPlatform("MEZZO","test.adlib.project.ads.SubAdlibAdViewMezzo");
        AdlibConfig.getInstance().bindPlatform("AMAZON","test.adlib.project.ads.SubAdlibAdViewAmazon");
        AdlibConfig.getInstance().bindPlatform("MEDIBAAD","test.adlib.project.ads.SubAdlibAdViewMedibaAd");
        AdlibConfig.getInstance().bindPlatform("MOBFOX","test.adlib.project.ads.SubAdlibAdViewMobfox");
        AdlibConfig.getInstance().bindPlatform("MOPUB","test.adlib.project.ads.SubAdlibAdViewMopub");
        AdlibConfig.getInstance().bindPlatform("ADMIXER","test.adlib.project.ads.SubAdlibAdViewAdmixer");
        // 쓰지 않을 플랫폼은 JAR 파일 및 test.adlib.project.ads 경로에서 삭제하면 최종 바이너리 크기를 줄일 수 있습니다.        
        
        // SMART* dialog 노출 시점 선택시 / setAdlibKey 키가 호출되는 activity 가 시작 activity 이며 해당 activity가 종료되면 app 종료로 인식합니다.
        // adlibr.com 에서 발급받은 api 키를 입력합니다.
        // ADLIB - API - KEY 설정
        
        // 각 애드립 액티비티에 애드립 앱 키값을 필수로 넣어주어야 합니다.
 		_amanager = new AdlibManager(AdlibTestProjectConstans.ADLIB_API_KEY);
 		_amanager.onCreate(this);
 		// 테스트 광고 노출로, 상용일 경우 꼭 제거해야 합니다.
 		_amanager.setAdlibTestMode(AdlibTestProjectConstans.ADLIB_TEST_MODE);
 		
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

	public boolean onKeyDown(int keyCode, KeyEvent event){
		//Back키 눌렀을 때 dialog 닫히는것 막기
		if(keyCode == KeyEvent.KEYCODE_BACK){
			// 종료 대화상자 광고를 노출하기 위해서 호출합니다.
			_amanager.showAdDialog("취소", "확인", "App 을 정말로 종료하시겠습니까?");
			
			/*
			 * 필요시 아래와 같이 색상과 클릭 액션을 변경할 수 있습니다.
			 * backgroundColor, backgroundColor(click), textColor, textColor, textColor(click), lineColor
			 */
//			int[] colors = new int[]{0xffffffff, 0xffa8a8a8, 0xff404040, 0xff404040, 0xffdfdfdf};
//			_amanager.showAdDialog("취소", "확인", "App 을 정말로 종료하시겠습니까?", colors, new AdlibDialogAdListener(){
//
//				@Override
//				public void onLeftClicked() {
//				}
//
//				@Override
//				public void onRightClicked() {
//				}
//				
//			});
		}
		return super.onKeyDown(keyCode, event);
	}
}
