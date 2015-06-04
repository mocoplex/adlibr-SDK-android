package test.adlib.project;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import com.mocoplex.adlib.AdlibActivity;
import com.mocoplex.adlib.AdlibConfig;
import com.mocoplex.adlib.AdlibManager;

/*
* 중요 *
안드로이드 4.2 이상에서 WebView의 JavascriptInterface를 지원하기 위해 
프로젝트 설정의 빌드 타겟을 17이상으로 설정해야 합니다.
(최신버전 android SDK 이용 권장)
*/

// 광고 스케줄링을 위해 AdlibActivity 를 상속받은 activity 를 생성합니다.
public class AdlibTestProjectActivity extends AdlibActivity {
	
	// 애드립 광고를 테스트 하기 위한 키 입니다.
	private String ADLIB_API_KEY = "53858972e4b0ef94c0636d85";

    /** Called when the activity is first created. */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        initAds();
        
        // 실제 광고 호출이 될 adview 를 연결합니다. 
        this.setAdsContainer(R.id.ads);
        
        View.OnClickListener cl;
        
        cl = new View.OnClickListener()
        {
			@Override
			public void onClick(View v) {
				
				Intent in = new Intent(AdlibTestProjectActivity.this, AdlibTestProjectActivity2.class);
				startActivity(in);
			}
        	
        };
        this.findViewById(R.id.btn1).setOnClickListener(cl);
        
        cl = new View.OnClickListener()
        {
			@Override
			public void onClick(View v) {
				
				Intent in = new Intent(AdlibTestProjectActivity.this, AdlibTestProjectActivity3.class);
				startActivity(in);
			}
        	
        };
        this.findViewById(R.id.btn2).setOnClickListener(cl);        
        
        cl = new View.OnClickListener()
        {
			@Override
			public void onClick(View v) {
				
				Intent in = new Intent(AdlibTestProjectActivity.this, AdlibTestProjectActivity4.class);
				startActivity(in);
			}
        	
        };
        this.findViewById(R.id.btn3).setOnClickListener(cl);        
        
        cl = new View.OnClickListener()
        {
			@Override
			public void onClick(View v) {
				
				load();
			}
        	
        };
        this.findViewById(R.id.btn4).setOnClickListener(cl);
        
        cl = new View.OnClickListener()
        {
			@Override
			public void onClick(View v) {
				// 프리로드 전면광고 요청
				requestInterstitial();
			}
        	
        };
        this.findViewById(R.id.btn5).setOnClickListener(cl);
        
        cl = new View.OnClickListener()
        {
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
					// 필요시 재요청을 합니다.
					requestInterstitial();
				}
			}
        	
        };
        this.findViewById(R.id.btn6).setOnClickListener(cl);
        
        /*
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
		*/
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
        AdlibConfig.getInstance().bindPlatform("ADHUB","test.adlib.project.ads.SubAdlibAdViewAdHub");
        AdlibConfig.getInstance().bindPlatform("MEDIBAAD","test.adlib.project.ads.SubAdlibAdViewMedibaAd");
        AdlibConfig.getInstance().bindPlatform("MOBFOX","test.adlib.project.ads.SubAdlibAdViewMobfox");
        AdlibConfig.getInstance().bindPlatform("MOPUB","test.adlib.project.ads.SubAdlibAdViewMopub");
        AdlibConfig.getInstance().bindPlatform("ADMIXER","test.adlib.project.ads.SubAdlibAdViewAdmixer");
        // 쓰지 않을 플랫폼은 JAR 파일 및 test.adlib.project.ads 경로에서 삭제하면 최종 바이너리 크기를 줄일 수 있습니다.        
        
        // SMART* dialog 노출 시점 선택시 / setAdlibKey 키가 호출되는 activity 가 시작 activity 이며 해당 activity가 종료되면 app 종료로 인식합니다.
        // adlibr.com 에서 발급받은 api 키를 입력합니다.
        // ADLIB - API - KEY 설정
        
        // 각 애드립 액티비티에 애드립 앱 키값을 필수로 넣어주어야 합니다.
        setAdlibKey(ADLIB_API_KEY);
        // 테스트 광고 노출로, 상용일 경우 꼭 제거해야 합니다.
        setAdlibTestMode(true);
    }

	protected void load()
    {
    	
    	// 전면광고를 호출합니다.
//		loadFullInterstitialAd();
	
		// optional : 전면광고의 수신 성공, 실패 이벤트 처리가 필요한 경우엔 handler를 이용하실 수 있습니다.
		loadFullInterstitialAd(new Handler() {
			public void handleMessage(Message message) {
	    		try
	    		{
	    			switch (message.what) {
                    case AdlibManager.DID_SUCCEED:
                        Log.d("ADLIBr", "[Interstitial] onReceiveAd " + (String)message.obj);
                        break;
                    // 전면배너 스케줄링 사용시, 각각의 플랫폼의 수신 실패 이벤트를 받습니다.
                    case AdlibManager.DID_ERROR:
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
	    		}
	    		catch(Exception e)
	    		{
	    			
	    		}
	    	}
		});
    }
    
    @Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}
	
	public int dpToPx(int dp) {
		return (int) (dp
				* getResources().getDisplayMetrics().density + 0.5f);
	}

	public boolean onKeyDown(int keyCode, KeyEvent event){
		//Back키 눌렀을 때 dialog 닫히는것 막기
		if(keyCode == KeyEvent.KEYCODE_BACK){
			// 종료 대화상자 광고를 노출하기 위해서 호출합니다.
			showAdDialog("취소", "확인", "App 을 정말로 종료하시겠습니까?");
			
			/*
			 * 필요시 아래와 같이 색상과 클릭 액션을 변경할 수 있습니다.
			 * backgroundColor, backgroundColor(click), textColor, textColor, textColor(click), lineColor
			 */
//			int[] colors = new int[]{0xffffffff, 0xffa8a8a8, 0xff404040, 0xff404040, 0xffdfdfdf};
//			showAdDialog("취소", "확인", "App 을 정말로 종료하시겠습니까?", colors, new AdlibDialogAdListener(){
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
