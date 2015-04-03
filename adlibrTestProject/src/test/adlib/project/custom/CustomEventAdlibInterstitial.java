package test.adlib.project.custom;

import android.content.Context;
import android.os.Bundle;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.mediation.MediationAdRequest;
import com.google.android.gms.ads.mediation.customevent.CustomEventInterstitial;
import com.google.android.gms.ads.mediation.customevent.CustomEventInterstitialListener;
import com.mocoplex.adlib.AdlibAdInterstitial;
import com.mocoplex.adlib.AdlibAdInterstitialListener;

/**
 * 애드립의 전면배너
 * 구글 애드몹에서 애드립을 미디에이션 중에 하나를 사용하기 위한 코드입니다.
 */
public class CustomEventAdlibInterstitial implements CustomEventInterstitial, AdlibAdInterstitialListener {

	// 애드립 광고를 테스트 하기 위한 키 입니다.
	private static String ADLIB_KEY = "53858972e4b0ef94c0636d85";
	
	private AdlibAdInterstitial adInterstitial = null;
	
	private CustomEventInterstitialListener interstitialListener;

	@Override
	public void onDestroy() {
		if(this.adInterstitial != null) {
			this.adInterstitial.onDestroy();
			this.adInterstitial = null;
		}
	}

	@Override
	public void onPause() {
		if(this.adInterstitial != null)
			this.adInterstitial.onPause();
	}

	@Override
	public void onResume() {
		if(this.adInterstitial != null)
			this.adInterstitial.onResume();
	}

	@Override
	public void requestInterstitialAd(Context context,
			CustomEventInterstitialListener listener, String serverParameter,
			MediationAdRequest mediationAdRequest, Bundle customEventExtras) {
		
		this.interstitialListener = listener;
		
		this.adInterstitial = new AdlibAdInterstitial(context, ADLIB_KEY);
		this.adInterstitial.setAdlibAdListener(this);
		
		// 테스트 광고 노출로, 상용일 경우 꼭 제거해야 합니다.
		this.adInterstitial.setAdlibTestMode(true);
		
		this.adInterstitial.requestAd();
	}

	@Override
	public void showInterstitial() {
		if(this.adInterstitial != null)
			this.adInterstitial.showAd();
	}
	
	@Override
	public void onReceiveAd() {
		if(this.interstitialListener != null)
			this.interstitialListener.onAdLoaded();
	}

	@Override
	public void onFailedToReceiveAd() {
		if(this.interstitialListener != null)
			this.interstitialListener.onAdFailedToLoad(AdRequest.ERROR_CODE_NO_FILL);
	}

	@Override
	public void onClosedAd() {
		if(this.interstitialListener != null)
			this.interstitialListener.onAdClosed();
	}
	
	@Override
	public void onShowedAd() {
		if(this.interstitialListener != null)
			this.interstitialListener.onAdOpened();
	}
}
