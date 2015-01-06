using UnityEngine;
using System.Net.Json;

// Example script showing how you can easily call into the AdlibPlugin.
public class AdlibTestScript : MonoBehaviour {

	Rect rect = new Rect();
	void OnGUI()
	{
		rect.x = 20;
		rect.y = 150;
		
		rect.width = Screen.width * 0.3f;
		rect.height = Screen.height * 0.1f * 2 / 3;
		// Make the Enable Button
		if (GUI.Button(rect, "Hide")) {
			AdlibPlugin.HideBanner();
		}
		
		rect.y = rect.y + rect.height + 10;
		if (GUI.Button(rect, "Show")) {
			AdlibPlugin.ShowBanner(AdlibPlugin.BannerSize.Banner, false, false);
		}

		rect.y = rect.y + rect.height + 10;
		if (GUI.Button(rect, "LoadInterstitial")) {
			AdlibPlugin.LoadInterstitialAd();
		}

		rect.y = rect.y + rect.height + 10;
		if (GUI.Button(rect, "ShowPopBanner")) {
			AdlibPlugin.ShowPopBanner("#ff444444",
			                          AdlibPlugin.PopButton.White,
			                          true, 
			                          false,
			                          AdlibPlugin.PopAlign.Bottom,
			                          60);
		}

		rect.y = rect.y + rect.height + 10;
		if (GUI.Button(rect, "ShowIcon1")) {
			// RewardLink ID for Test - insert your real ID
			AdlibPlugin.ShowRewardLink("519c29ffe4b00e029838e9ed",
			                           AdlibPlugin.RewardLinkAlign.RightTop,
			                           50,
			                           60);
		}

		rect.y = rect.y + rect.height + 10;
		if (GUI.Button(rect, "ShowIcon2")) {
			// RewardLink ID for Test - insert you real ID
			AdlibPlugin.ShowRewardLink("519c2a0ee4b00e029838e9ee",
			                           AdlibPlugin.RewardLinkAlign.LeftTop,
			                           50,
			                           60);
		}

		rect.y = rect.y + rect.height + 10;
		if (GUI.Button(rect, "HideIcon")) {           
			AdlibPlugin.HideRewardLink();
		}

		rect.y = rect.y + rect.height + 10;
		if (GUI.Button(rect, "RequestInterstitial")) {           
			AdlibPlugin.RequestInterstitial();
		}

		rect.y = rect.y + rect.height + 10;
		if (GUI.Button(rect, "ShowInterstitial")) {           
			AdlibPlugin.ShowInterstitial();
		}
	}

	// Use this for initialization
	void Start () {
		AdlibPlugin.ReceivedInterstitial += HandleReceivedInterstitial;
		AdlibPlugin.FailedToReceiveInterstitial += HandleFailedToReceiveInterstitial;
		AdlibPlugin.DismissedInterstitial += HandleDismissedInterstitial;
		AdlibPlugin.ReceivedPopBanner += HandleReceivedPopBanner;
		AdlibPlugin.FailedToReceivePopBanner += HandleFailedToReceivePopBanner;
		AdlibPlugin.DismissedPopBanner += HandleDismissedPopBanner;

		JsonObjectCollection ads = new JsonObjectCollection();
		ads.Add(new JsonStringValue("ADAM","test.adlib.project.ads.SubAdlibAdViewAdam"));
		ads.Add(new JsonStringValue("ADMOB","test.adlib.project.ads.SubAdlibAdViewAdmob"));
		ads.Add(new JsonStringValue("CAULY","test.adlib.project.ads.SubAdlibAdViewCauly"));
		ads.Add(new JsonStringValue("TAD","test.adlib.project.ads.SubAdlibAdViewTAD"));
		ads.Add(new JsonStringValue("NAVER","test.adlib.project.ads.SubAdlibAdViewNaverAdPost"));
		ads.Add(new JsonStringValue("SHALLWEAD","test.adlib.project.ads.SubAdlibAdViewShallWeAd"));
		ads.Add(new JsonStringValue("INMOBI","test.adlib.project.ads.SubAdlibAdViewInmobi"));
		ads.Add(new JsonStringValue("MMEDIA","test.adlib.project.ads.SubAdlibAdViewMMedia"));
		ads.Add(new JsonStringValue("MOBCLIX","test.adlib.project.ads.SubAdlibAdViewMobclix"));
		ads.Add(new JsonStringValue("UPLUSAD","test.adlib.project.ads.SubAdlibAdViewUPlusAD"));
		ads.Add(new JsonStringValue("MEZZO","test.adlib.project.ads.SubAdlibAdViewMezzo"));
		ads.Add(new JsonStringValue("AMAZON","test.adlib.project.ads.SubAdlibAdViewAmazon"));
		ads.Add(new JsonStringValue("ADHUB","test.adlib.project.ads.SubAdlibAdViewAdHub"));
		ads.Add(new JsonStringValue("MEDIBAAD","test.adlib.project.ads.SubAdlibAdViewMedibaAd"));
		ads.Add(new JsonStringValue("MOBFOX","test.adlib.project.ads.SubAdlibAdViewMobfox"));
		ads.Add(new JsonStringValue("MOPUB","test.adlib.project.ads.SubAdlibAdViewMopub"));

		// ADLIB_API_KEY for Test - insert your real KEY
		AdlibPlugin.InitializeAdlib("52f30fc5e4b0c16dd4c0f898", ads.ToString());
		
		AdlibPlugin.ShowBanner(AdlibPlugin.BannerSize.Banner_320x50, true, true, 0);
	}

	void OnApplicationPause(bool pause) {
		if(pause) {
			AdlibPlugin.StopAds();
		}
		else {
			AdlibPlugin.RestartAds();
		}
	}

	public void HandleReceivedInterstitial(string platform) {
		print("Received Interstitial Ad : ");
		print(platform);
	}

	public void HandleFailedToReceiveInterstitial() {
		print("Failed Interstitial Ad");
	}

	public void HandleDismissedInterstitial() {
		print("Closed Interstitial Ad");
	}

	public void HandleReceivedPopBanner() {
		print("Received PopBanner");
	}
	
	public void HandleFailedToReceivePopBanner() {
		print("Failed PopBanner");
	}
	
	public void HandleDismissedPopBanner() {
		print("Closed PopBanner");
	}
}
