/*
 * adlibr - Library for mobile AD mediation.
 * http://adlibr.com
 * Copyright (c) 2012-2013 Mocoplex, Inc.  All rights reserved.
 * Licensed under the BSD open source license.
 */

/*
 * confirmed compatible with TNK SDK 6.10
 */

package test.adlib.project.ads;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;

import com.mocoplex.adlib.AdlibManager;
import com.mocoplex.adlib.SubAdlibAdViewCore;
import com.tnkfactory.ad.TnkAdListener;
import com.tnkfactory.ad.TnkSession;

/*
AndroidManifest.xml 에 아래 내용을 추가해주세요.	
<meta-data android:name="tnkad_app_id" android:value="발급받은 TNK App-ID를 입력해주세요." />
*/


public class SubAdlibAdViewTNK extends SubAdlibAdViewCore {

	public SubAdlibAdViewTNK(Context context) {
		this(context, null);
	}
	
	public SubAdlibAdViewTNK(final Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public void query() {
		
		failed();
	}

	public void clearAdView() {
		super.clearAdView();
	}

	public void onResume() {
		super.onResume();
	}

	public void onPause() {
		super.onPause();
	}

	public void onDestroy() {
		super.onDestroy();
	}
	
	public static void loadInterstitial(final Context ctx, final Handler h, final String adlibKey){
		
		TnkSession.prepareInterstitialAd((Activity) ctx, TnkSession.CPC, new TnkAdListener() {
	         public void onClose(int type) {
	        	 try{
	 				if(h != null){
	 					h.sendMessage(Message.obtain(h, AdlibManager.INTERSTITIAL_CLOSED, "TNK"));
	 				}
	 				
	 			}catch(Exception e){
	 				
	 			}
	         }

	         public void onFailure(int errCode) {
	        	 try{
	  				if(h != null){
	  					h.sendMessage(Message.obtain(h, AdlibManager.DID_ERROR, "TNK"));
	  				}
	  				
	 			}catch(Exception e){
	 				
	 			}
	         }

	         public void onLoad() {
	        	 try{
	        		 if(h != null){
	  					h.sendMessage(Message.obtain(h, AdlibManager.DID_SUCCEED, "TNK"));
	  				}
	        		 
	        		 TnkSession.showInterstitialAd((Activity)ctx);
	        		 
	        	 }catch(Exception e){
	        		 
	        	 }
	         }

	         @Override
	         public void onShow() {
	        	 
	         }
	     });
	}

}
