package test.adlib.project.custom;

import android.content.Context;
import android.os.Bundle;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.mediation.MediationAdRequest;
import com.google.android.gms.ads.mediation.customevent.CustomEventBanner;
import com.google.android.gms.ads.mediation.customevent.CustomEventBannerListener;
import com.mocoplex.adlib.AdlibAdListener;
import com.mocoplex.adlib.AdlibAdView;

/**
 * 애드립의 띠배너
 * 구글 애드몹에서 애드립을 미디에이션 중에 하나를 사용하기 위한 코드입니다.
 */
public class CustomEventAdlib implements CustomEventBanner, AdlibAdListener {
	
	// 애드립 광고를 테스트 하기 위한 키 입니다.
	private static String ADLIB_KEY = "53858972e4b0ef94c0636d85";
	
	private AdlibAdView adView = null;
	
	private CustomEventBannerListener bannerListener;

	@Override
	public void requestBannerAd(Context context,
			CustomEventBannerListener listener, String serverParameter,
			AdSize size, MediationAdRequest mediationAdRequest,
			Bundle customEventExtras) {
		
		this.bannerListener = listener;
		int height = size.getHeight();
		if(height != 50) {
			bannerListener.onAdFailedToLoad(AdRequest.ERROR_CODE_INVALID_REQUEST);
			return;
		}
		this.adView = new AdlibAdView(context, ADLIB_KEY);
		this.adView.setAdlibAdListener(this);
		
		// 테스트 광고 노출로, 상용일 경우 꼭 제거해야 합니다.
		this.adView.setAdlibTestMode(true);
		
		this.adView.startAd();
	}

	@Override
	public void onFailedToReceiveAd() {
		if(this.bannerListener != null)
			bannerListener.onAdFailedToLoad(AdRequest.ERROR_CODE_NO_FILL);
	}

	@Override
	public void onReceiveAd() {
		if(this.bannerListener != null)
			bannerListener.onAdLoaded(adView);
	}
	
	@Override
	public void onDestroy() {
		if(this.adView != null) {
			this.adView.destroyAd();
			this.adView = null;
		}
	}

	@Override
	public void onPause() {
		if(this.adView != null) {
			this.adView.stopAd();
		}
	}

	@Override
	public void onResume() {
		if(this.adView != null) {
			this.adView.startAd();
		}
	}
}
