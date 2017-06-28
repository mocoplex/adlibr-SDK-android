# ADLib 띠 배너 적용 가이드
> 애드립 띠배너 적용 방법에 대해 설명 드립니다. <br>


## 미디에이션 배너 연동
> 애드립 기본 띠배너 <br>
> 플랫폼 미디에이션을 지원

### 단계 1.기본 설정
- AdlibManager 초기화 및 이벤트 핸들러 등록
- 참고 : [미디에이션 배너 참고](./AdlibBannerMediationActivity.java)

```java
// 각 애드립 액티비티에 애드립 앱 키값을 필수로 넣어주어야 합니다.
adlibManager = new AdlibManager(AdlibTestProjectConstants.ADLIB_API_KEY);
adlibManager.onCreate(this);
// 테스트 광고 노출로, 상용일 경우 꼭 제거해야 합니다.
adlibManager.setAdlibTestMode(AdlibTestProjectConstants.ADLIB_TEST_MODE);
// 배너 스케쥴에 등록된 광고 모두 광고 요청 실패 시 대기 시간 설정(단위:초, 기본:10초, 최소:1초)
// adlibManager.setBannerFailDelayTime(10);

// 배너 스케쥴 요청 실패 시 대기 시간동안 노출되는 View 설정
// View backFill = new View(this);
// backFill.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
// adlibManager.setBannerBackfillView(backFill);

// 이벤트 핸들러 등록
adlibManager.setAdsHandler(new Handler() {
    public void handleMessage(Message message) {
        try {
            switch (message.what) {
                case AdlibManager.DID_SUCCEED:
                    Log.d("ADLIBr", "[Banner] onReceiveAd " + (String) message.obj);
                    break;
                case AdlibManager.DID_ERROR:
                    Log.d("ADLIBr", "[Banner] onFailedToReceiveAd " + (String) message.obj);
                    break;
                case AdlibManager.BANNER_FAILED:
                    Log.d("ADLIBr", "[Banner] All Failed.");
                    break;
            }
        } catch (Exception e) {

        }
    }
});

adlibManager.setAdsContainer(R.id.ads);
```

### 단계2. 광고 호출 방법
> 광고 호출 방법은  XML과 View 동적 생성 형태 제공

#### 1. XML 사용
- 광고 노출이 필요한 Activity XML 에 AdlibADViewContainer 를 추가
- AdlibManager 뷰 설정 및 미디에이션 광고 시작

```xml
<com.mocoplex.adlib.AdlibAdViewContainer
    android:id="@+id/ads"
    android:layout_width="match_parent"
    android:layout_height="50dp" />

// 띠배너 미디에이션 뷰 설정 및 시작
adlibManager.setAdsContainer(R.id.ads);
```

#### 2. 동적 생성
- 광고 뷰를 동적으로 생성하여 광고를 노출
- AdlibManager 뷰 설정 및 미디에이션 광고 시작

```java
AdlibAdViewContainer avc = new AdlibAdViewContainer(this);

...

adlibManager.bindAdsContainer(avc);
```

- 동적 생성의 경우 광고 영역 삭제가 필요한 경우

```java
adlibManager.destroyAdsContainer();
```

## 다이나믹 배너 광고
> 광고 요청 시 광고 노출 가능한 뷰를 전달해주는 형태의 광고<br>
> 다양한 사이즈의 광고 요청이 가능<br>
> 플랫폼 미디에이션을 지원하지 않음

### 단계 1.기본 설정
- AdlibManager 초기화
- 참고 : [미디에이션 배너 참고](./AdlibBannerDynamicActivity.java)

```java
// 각 애드립 액티비티에 애드립 앱 키값을 필수로 넣어주어야 합니다.
adlibManager = new AdlibManager(AdlibTestProjectConstants.ADLIB_API_KEY);
adlibManager.onCreate(this);
// 테스트 광고 노출로, 상용일 경우 꼭 제거해야 합니다.
adlibManager.setAdlibTestMode(AdlibTestProjectConstants.ADLIB_TEST_MODE);
```

### 단계 2. 광고 요청 및 이벤트 처리

```java
Handler handler = new Handler() {
	@Override
	public void handleMessage(Message msg) {
	    try {
	        switch (msg.what) {
	            case AdlibManager.DID_SUCCEED:
	                Log.d("ADLIBr", "[Dynamic] onReceiveAd ");
	                adView = (View) msg.obj;
	                break;
	            case AdlibManager.DID_ERROR:
	                Log.d("ADLIBr", "[Dynamic] onFailedToReceiveAd " + (String) msg.obj);
	                Toast.makeText(AdlibBannerDynamicActivity.this, "광고수신 실패 :)", Toast.LENGTH_SHORT).show();
	                break;

	            case AdlibManager.DID_CLICK:
	                Log.d("ADLIBr", "[Dynamic] onClickAd");
	                break;
	        }
	    } catch (Exception e) {}
	}
};
// 다이나믹 광고의 경우 미디에이션을 지원하지 않습니다
// 애드립 띠배너 기본 사이즈 320 * 50
// 테스트 광고는 포함된 테스트 키에서만 기본 사이즈 광고 노출
if (adlibManager != null) adlibManager.requestDynamicBannerView(320, 50, handler);
```

### 단계 3. 광고 노출
- 광고 노출이 필요한 시점에 광고 뷰를 추가

```java
if (adView != null) {
    ViewGroup vg = (ViewGroup) findViewById(R.id.container);
    vg.addView(adView);
}
```

### 참고 사항
- 광고 뷰를 동적으로 추가 및 삭제하는 경우 광고 뷰 관리가 필요함
- Exception 및 메모리 누수 발생 가능