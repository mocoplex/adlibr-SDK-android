package test.adlib.project;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.mocoplex.adlib.AdlibConfig.ContentType;
import com.mocoplex.adlib.AdlibManager;
import com.mocoplex.adlib.platform.nativeads.AdlibNativeAdListener;
import com.mocoplex.adlib.platform.nativeads.AdlibNativeHelper;
import com.mocoplex.adlib.platform.nativeads.AdlibNativeItem;

public class AdlibTestProjectActivity8 extends Activity {
	
	// 일반 Activity 에서의 adlib 연동	
	private AdlibManager _amanager;
	
	private ArrayList<Object> mList = new ArrayList<Object>();
	
	private ListView listView;
	private Adapter listAdapter;
	
	// 광고 뷰 표출에 도움을 주는 클래스
	private AdlibNativeHelper anh = null;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.main8_list);
		
		listView = (ListView) findViewById(R.id.list);
		
		listAdapter = new Adapter(this, mList);
		listView.setAdapter(listAdapter);
		
		anh = new AdlibNativeHelper(this);

		OnScrollListener scrollListener = new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				anh.onScrollStateChanged(view, scrollState);
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				anh.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
			}
		};
		
		listView.setOnScrollListener(scrollListener);
		
		// 각 애드립 액티비티에 애드립 앱 키값을 필수로 넣어주어야 합니다.
		_amanager = new AdlibManager(AdlibTestProjectConstans.ADLIB_API_KEY);
		_amanager.onCreate(this);
		// 테스트 광고 노출로, 상용일 경우 꼭 제거해야 합니다.
		_amanager.setAdlibTestMode(AdlibTestProjectConstans.ADLIB_TEST_MODE);
		
		// 네이티브 광고를 받고싶다면 아래의 코드를 호출 합니다.
		// 1. 수량 : 최대로 수신하고자 하는 수량을 지정합니다. (10개를 초과한 경우 10개로 지정됩니다.)
		// 2. 타입 : 광고 유형을 지정합니다. (ALL : 비디오와 이미지, VIDEO : 비디오만 해당, IMAGE : 이미지만 해당)
		// 3. 타임아웃 : 해당 시간 동안 광고를 대기합니다. (초단위로 광고 최대 수량이 높다면 높게 설정해주시기 바랍니다.)
		// 4. 이벤트 리스너 : 성공과 실패를 알기 위한 리스너를 지정합니다.
		_amanager.loadNativeAd(10, ContentType.ALL, 20, new AdlibNativeAdListener(){
			@Override
			public void onReceiveAd(ArrayList<AdlibNativeItem> items) {
				Log.d("ADLIB-Native", "onReceiveAd ::: item size : " + items.size());
				
				// 광고를 수신하면 리스트에 넣습니다.
				for(int i=0;i<items.size();i++){
					
//					Log.d("ADLIB-Native", i + " -> Content Type : " + items.get(i).getContentType());
//					Log.d("ADLIB-Native", i + " -> Title : " + items.get(i).getTitle());
//					Log.d("ADLIB-Native", i + " -> SubTitle : " + items.get(i).getSubTitle());
//					Log.d("ADLIB-Native", i + " -> Icon Url : " + items.get(i).getIconUrl());
//					Log.d("ADLIB-Native", i + " -> Description : " + items.get(i).getDescription());
//					Log.d("ADLIB-Native", i + " -> GoButton Text : " + items.get(i).getBtnText());
					
					mList.add(items.get(i));
				}
				
				listAdapter.notifyDataSetChanged();
			}
			
			@Override
			public void onError(int errorCode) {
				Log.d("ADLIB-Native", "onError ::: error code : " + errorCode);
				
				Toast.makeText(AdlibTestProjectActivity8.this, "광고수신 실패 :)", Toast.LENGTH_SHORT).show();
			}
		});
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
	
	public class Adapter extends BaseAdapter {

		private ArrayList<Object> mList = new ArrayList<Object>();
		
		// ---------- 리스트 아이템의 레이아웃 재정의 ---------- //
		
		private final int VIEW_TYPE = 0;
		private final int VIEW_TYPE_AD = 1;
		
		@Override
		public int getItemViewType(int position) {
			
			if(mList.get(position) instanceof AdlibNativeItem){
				// 광고 타입을 추가합니다.
				return VIEW_TYPE_AD;
			}else{
				return VIEW_TYPE;
			}
		}
		
		@Override
		public int getViewTypeCount() {
			int viewTypeCount = super.getViewTypeCount();
		    return viewTypeCount + 1; // 기존 레이아웃의 타입에 1을 더합니다.
		}
		
		// ---------- 리스트 아이템의 레이아웃 재정의 ---------- //
		
		public Adapter(Context context, ArrayList<Object> objects){
			mList = objects;
		}
		
		@Override
		public int getCount() {
			return mList.size();
		}

		@Override
		public Object getItem(int location) {
			return mList.get(location);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			int type = getItemViewType(position);
			
			if(type == VIEW_TYPE){
				
				// 기본 뷰에 대해 처리를 합니다.
				
			}else if(type == VIEW_TYPE_AD){
				
				// 광고 뷰에 대해 처리를 합니다.
				
				return anh.getView(position, convertView, mList, R.layout.main8_item);
				
			}
			
			return convertView;
		}
	}
}