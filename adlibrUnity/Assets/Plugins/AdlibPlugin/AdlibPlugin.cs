using UnityEngine;
using System;

public class AdlibPlugin : MonoBehaviour {

	public class BannerSize
	{
		private string bannerSize;
		private BannerSize(string value)
		{
			this.bannerSize = value;
		}
		
		public override string ToString()
		{
			return bannerSize;
		}

		public static BannerSize Banner = new BannerSize("BANNER");
		public static BannerSize Banner_320x50 = new BannerSize("BANNER_320X50");
	}

	public class BannerAlign
	{
		private string bannerAlign;
		private BannerAlign(string value)
		{
			this.bannerAlign = value;
		}
		
		public override string ToString()
		{
			return bannerAlign;
		}
		
		public static BannerAlign Left = new BannerAlign("LEFT");
		public static BannerAlign Right = new BannerAlign("RIGHT");
		public static BannerAlign Center = new BannerAlign("CENTER");
	}

	public class PopButton
	{
		private string buttonColor;
		private PopButton(string value)
		{
			this.buttonColor = value;
		}
		
		public override string ToString()
		{
			return buttonColor;
		}
		
		public static PopButton White = new PopButton("WHITE");
		public static PopButton Black = new PopButton("BLACK");
	}

	public class PopAlign
	{
		private string align;
		private PopAlign(string value)
		{
			this.align = value;
		}
		
		public override string ToString()
		{
			return align;
		}
		
		public static PopAlign Top = new PopAlign("TOP");
		public static PopAlign Bottom = new PopAlign("BOTTOM");
		public static PopAlign Left = new PopAlign("LEFT");
		public static PopAlign Right = new PopAlign("RIGHT");
	}

	public class RewardLinkAlign
	{
		private string align;
		private RewardLinkAlign(string value)
		{
			this.align = value;
		}
		
		public override string ToString()
		{
			return align;
		}
		
		public static RewardLinkAlign LeftTop = new RewardLinkAlign("LEFT_TOP");
		public static RewardLinkAlign RightTop = new RewardLinkAlign("RIGHT_TOP");
		public static RewardLinkAlign LeftBottom = new RewardLinkAlign("LEFT_BOTTOM");
		public static RewardLinkAlign RightBottom = new RewardLinkAlign("RIGHT_BOTTOM");
	}
	
	static AndroidJavaClass adlibPluginClass;
	static AndroidJavaClass unityPlayer;
	static AndroidJavaObject currActivity;

	public static event Action<string> ReceivedInterstitial = delegate {};
	public static event Action FailedToReceiveInterstitial = delegate {};
	public static event Action DismissedInterstitial = delegate {};
	public static event Action ReceivedPopBanner = delegate {};
	public static event Action FailedToReceivePopBanner = delegate {};
	public static event Action DismissedPopBanner = delegate {};

	void Awake() {
		gameObject.name = this.GetType().ToString();
		SetCallbackHandlerName(gameObject.name);
		DontDestroyOnLoad(this);
	}
	
	public static void InitializeAdlib(string adlibKey, string ads) {
		if(adlibPluginClass == null)
			adlibPluginClass = new AndroidJavaClass("com.mocoplex.adlib.unity.AdlibUnityPlugin");
		unityPlayer = new AndroidJavaClass("com.unity3d.player.UnityPlayer");
		currActivity = unityPlayer.GetStatic<AndroidJavaObject>("currentActivity");
		adlibPluginClass.CallStatic("Initialize", currActivity, adlibKey, ads);
	}
	
	public static void SetCallbackHandlerName(string name) {
		if(adlibPluginClass == null)
			adlibPluginClass = new AndroidJavaClass("com.mocoplex.adlib.unity.AdlibUnityPlugin");
		adlibPluginClass.CallStatic("setCallbackHandlerName", name);
	}

	public static void ShowBanner(BannerSize size, bool useHouseBanner, bool positionAtTop) {
		adlibPluginClass.CallStatic("ShowBanner", size.ToString(), useHouseBanner, positionAtTop, 0);
	}
	
	public static void ShowBanner(BannerSize size, bool useHouseBanner, bool positionAtTop, int padding) {
		adlibPluginClass.CallStatic("ShowBanner", size.ToString(), useHouseBanner, positionAtTop, padding);
	}

	public static void ShowBanner(BannerSize size, bool useHouseBanner, bool positionAtTop, BannerAlign align) {
		adlibPluginClass.CallStatic("ShowBanner", size.ToString(), useHouseBanner, positionAtTop, 0, align.ToString());
	}
	
	public static void ShowBanner(BannerSize size, bool useHouseBanner, bool positionAtTop, int padding, BannerAlign align) {
		adlibPluginClass.CallStatic("ShowBanner", size.ToString(), useHouseBanner, positionAtTop, padding, align.ToString());
	}

	public static void ShowBannerWithPixel(BannerSize size, bool useHouseBanner, bool positionAtTop) {
		adlibPluginClass.CallStatic("ShowBannerWithPixel", size.ToString(), useHouseBanner, positionAtTop, 0);
	}
	
	public static void ShowBannerWithPixel(BannerSize size, bool useHouseBanner, bool positionAtTop, int padding) {
		adlibPluginClass.CallStatic("ShowBannerWithPixel", size.ToString(), useHouseBanner, positionAtTop, padding);
	}
	
	public static void ShowBannerWithPixel(BannerSize size, bool useHouseBanner, bool positionAtTop, BannerAlign align) {
		adlibPluginClass.CallStatic("ShowBannerWithPixel", size.ToString(), useHouseBanner, positionAtTop, 0, align.ToString());
	}
	
	public static void ShowBannerWithPixel(BannerSize size, bool useHouseBanner, bool positionAtTop, int padding, BannerAlign align) {
		adlibPluginClass.CallStatic("ShowBannerWithPixel", size.ToString(), useHouseBanner, positionAtTop, padding, align.ToString());
	}
	
	public static void HideBanner() {
		adlibPluginClass.CallStatic("HideBanner");
	}

	public static void LoadInterstitialAd() {
		adlibPluginClass.CallStatic("LoadInterstitialAd");
	}

	public static void ShowPopBanner(string frameColor, PopButton btnColor, bool useInAnim, bool useOutAnim, PopAlign btnAlign, int padding) {
		adlibPluginClass.CallStatic("ShowPopBanner", frameColor, btnColor.ToString(), useInAnim, useOutAnim, btnAlign.ToString(), padding);
	}
	
	public static void HidePopBanner() {
		adlibPluginClass.CallStatic("HidePopBanner");
	}

	public static void ShowRewardLink(string linkId, RewardLinkAlign linkAlign, int x, int y) {
		adlibPluginClass.CallStatic("ShowRewardLink", linkId, linkAlign.ToString(), x, y);
	}

	public static void ShowRewardLinkWithPixel(string linkId, RewardLinkAlign linkAlign, int x, int y) {
		adlibPluginClass.CallStatic("ShowRewardLinkWithPixel", linkId, linkAlign.ToString(), x, y);
	}

	public static void HideRewardLink() {
		adlibPluginClass.CallStatic("HideRewardLink");
	}

	public void OnReceivedInterstitialAd(string platform)
	{
		ReceivedInterstitial(platform);
	}
	
	public void OnFailedToReceiveInterstitialAd(string unusedMessage)
	{
		FailedToReceiveInterstitial();
	}
	
	public void OnClosedInterstitialAd(string unusedMessage)
	{
		DismissedInterstitial();
	}

	public void OnReceivedPopBanner(string unusedMessage)
	{
		ReceivedPopBanner();
	}
	
	public void OnFailedToReceivePopBanner(string unusedMessage)
	{
		FailedToReceivePopBanner();
	}
	
	public void OnClosedPopBanner(string unusedMessage)
	{
		DismissedPopBanner();
	}
	
	public static void StopAds() {
		adlibPluginClass.CallStatic("Pause");
	}
	
	public static void RestartAds() {
		adlibPluginClass.CallStatic("Resume");
	}
	
	public static void RequestInterstitial() {
		adlibPluginClass.CallStatic("RequestInterstitial");
	}
	
	public static void ShowInterstitial() {
		adlibPluginClass.CallStatic("ShowInterstitial");
	}
}
