<<Google Play Services library를 이용하여 앱을 만드는 경우>>

admob, admob 네트워크를 이용하실 때,
admob SDK는 삭제하시고, admob SubAdView 는 현재 이 폴더의 파일로 교체 하십시오.

AndroidManifest.xml에 

<meta-data android:name="com.google.android.gms.version"
	android:value="@integer/google_play_services_version"/>
<activity android:name="com.google.android.gms.ads.AdActivity"
	android:configChanges="keyboard|keyboardHidden|orientation|screenLayout|uiMode|screenSize|smallestScreenSize" />
	
위의 내용을 추가하십시오.

애드립 설정이나, admob 외의 타 플랫폼에 대한 부분은 동일합니다.