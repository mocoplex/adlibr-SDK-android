using UnityEngine;
using System.Net.Json;

// Example script showing how you can easily call into the AdlibPlugin.
public class AdlibTestScript : MonoBehaviour {

	Rect rect = new Rect();
	void OnGUI()
	{
		rect.x = 20;
		rect.y = 300;
		
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
		ads.Add(new JsonStringValue("ADLIB","52f30fc5e4b0c16dd4c0f898"));  // ADLIB_API_KEY for Test - insert your real KEY
		ads.Add(new JsonStringValue("ADAM","insert_your_adam_id"));
		ads.Add(new JsonStringValue("ADAM_INTERSTITIAL","insert_your_adam_interstitial_id"));
		ads.Add(new JsonStringValue("ADMOB","insert_your_admob_id"));
		ads.Add(new JsonStringValue("ADMOB_INTERSTITIAL","insert_your_admob_interstitial_id"));
		ads.Add(new JsonStringValue("CAULY","insert_your_cauly_id"));
		ads.Add(new JsonStringValue("CAULY_INTERSTITIAL","insert_your_cauly_interstitial_id"));
		ads.Add(new JsonStringValue("ADPOST","insert_your_naverAdPost_id"));
		ads.Add(new JsonStringValue("INMOBI","insert_your_inmobi_id"));
		ads.Add(new JsonStringValue("INMOBI_INTERSTITIAL","insert_your_inmobi_interstitial_id"));
		ads.Add(new JsonStringValue("ADMOB_MEDIATION","insert_your_admob_mediation_id"));
		ads.Add(new JsonStringValue("UPLUS","insert_your_uplus_id"));
		ads.Add(new JsonStringValue("UPLUS_INTERSTITIAL","insert_your_uplus_interstitial_id"));
		ads.Add(new JsonStringValue("TAD","insert_your_tad_id"));
		ads.Add(new JsonStringValue("TAD_INTERSTITIAL","insert_your_tad_interstitial_id"));
		ads.Add(new JsonStringValue("ADHUB","insert_your_adhub_id"));
		ads.Add(new JsonStringValue("ADHUB_INTERSTITIAL","insert_your_adhub_interstitial_id"));
		ads.Add(new JsonStringValue("AMAZON","insert_your_amazon_id"));
		ads.Add(new JsonStringValue("MEZZO","insert_your_mezzoMan_id"));
		ads.Add(new JsonStringValue("MILLENNIAL_MEDIA","insert_your_millennialMedia_id"));

		AdlibPlugin.InitializeAdlib(ads.ToString());
		
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
