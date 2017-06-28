package test.adlib.project.nativead;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mocoplex.adlib.AdlibConfig;
import com.mocoplex.adlib.AdlibManager;
import com.mocoplex.adlib.nativead.layout.AdlibNativeLayout;
import com.mocoplex.adlib.platform.nativeads.AdlibNativeAdListener;
import com.mocoplex.adlib.platform.nativeads.AdlibNativeHelper;
import com.mocoplex.adlib.platform.nativeads.AdlibNativeItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import test.adlib.project.AdlibTestProjectConstants;
import test.adlib.project.R;

public class AdlibNativeSampleFeedActivity extends Activity {

    // 일반 Activity 에서의 adlib 연동
    private AdlibManager _amanager;

    private ArrayList<Object> mList = new ArrayList<Object>();

    private ListView listView;
    private Adapter listAdapter;

    // 광고 뷰 표출에 도움을 주는 클래스
    // AdlibNativeHelper 사용하지 않는 경우 광고뷰 관리 필수
    private AdlibNativeHelper nativeHelper = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_native_feed_list);

        initSampleFeedData();

        listView = (ListView) findViewById(R.id.list);

        listAdapter = new Adapter(this, mList);
        listView.setAdapter(listAdapter);

        nativeHelper = new AdlibNativeHelper(listView);

        AbsListView.OnScrollListener scrollListener = new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // 세로 스크롤 형태의 레이아웃에서만 사용 가능 - 추후 가로 지원 예정
                nativeHelper.onScrollStateChanged(view, scrollState);
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }
        };

        listView.setOnScrollListener(scrollListener);

        // 각 애드립 액티비티에 애드립 앱 키값을 필수로 넣어주어야 합니다.
        _amanager = new AdlibManager(AdlibTestProjectConstants.ADLIB_API_KEY);
        _amanager.onCreate(this);
        // 테스트 광고 노출로, 상용일 경우 꼭 제거해야 합니다.
        _amanager.setAdlibTestMode(AdlibTestProjectConstants.ADLIB_TEST_MODE);

        // 네이티브 광고를 받고싶다면 아래의 코드를 호출 합니다.
        // 1. 타입 : 광고 유형을 지정합니다. (ALL : 비디오와 이미지, VIDEO : 비디오만 해당, IMAGE : 이미지만 해당)
        // 2. 이벤트 리스너 : 성공과 실패를 알기 위한 리스너를 지정합니다.

        _amanager.loadNativeAd(AdlibConfig.ContentType.VIDEO, new AdlibNativeAdListener() {
            @Override
            public void onReceiveAd(AdlibNativeItem item) {
                Log.d("ADLIB-Native", "onReceiveAd");

                // 광고 정보
                // Log.d("ADLIB-Native", i + " -> Content Type : " + item.getContentType());
                // Log.d("ADLIB-Native", i + " -> Title : " + item.getTitle());
                // Log.d("ADLIB-Native", i + " -> SubTitle : " + item.getSubTitle());
                // Log.d("ADLIB-Native", i + " -> Icon Url : " + item.getIconUrl());
                // Log.d("ADLIB-Native", i + " -> Description : " + item.getDescription());
                // Log.d("ADLIB-Native", i + " -> GoButton Text : " + item.getBtnText());
                if (item != null) {
                    mList.add(mList.size() / 2, item);
                    listAdapter.notifyDataSetChanged();
                    nativeHelper.update();
                }
            }

            @Override
            public void onError(int errorCode) {
                Log.d("ADLIB-Native", "onError ::: error code : " + errorCode);

                Toast.makeText(AdlibNativeSampleFeedActivity.this, "광고수신 실패 :)", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        _amanager.onResume(this);
        nativeHelper.onResume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        _amanager.onPause(this);
        nativeHelper.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        _amanager.onDestroy(this);
        nativeHelper.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        nativeHelper.update();
    }

    private void initSampleFeedData() {
        try {
            JSONObject json = new JSONObject(loadJSONFromAsset());
            JSONArray arr = json.getJSONArray("feed");
            int size = arr.length();
            for (int i = 0; i < size; i++) {
                JSONObject obj = arr.getJSONObject(i);
                SampleFeedData child = new SampleFeedData(obj);
                mList.add(child);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String loadJSONFromAsset() {
        String json = null;
        try {

            InputStream is = getAssets().open("sample_feed.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");

        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;

    }

    private class SampleFeedData {
        private int id;
        private String name;
        private String img;
        private String status;
        private String profilePic;
        private String timeStamp;
        private String url;
        private int width;
        private int height;

        public SampleFeedData(JSONObject obj) {
            init(obj);
        }

        private void init(JSONObject obj) {
            try {
                id = obj.getInt("id");
                name = obj.getString("name");
                img = obj.getString("image");
                status = obj.getString("status");
                profilePic = obj.getString("profilePic");
                timeStamp = obj.getString("timeStamp");
                url = obj.getString("url");
                width = obj.getInt("width");
                height = obj.getInt("height");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getImg() {
            return img;
        }

        public String getStatus() {
            return status;
        }

        public String getProfilePic() {
            return profilePic;
        }

        public String getTimeStamp() {
            return timeStamp;
        }

        public String getUrl() {
            return url;
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }
    }

    public class Adapter extends BaseAdapter {

        private Context context;

        private ArrayList<Object> mList = new ArrayList<Object>();

        // ---------- 리스트 아이템의 레이아웃 재정의 ---------- //

        private final int VIEW_TYPE = 0;
        private final int VIEW_TYPE_AD = 1;

        @Override
        public int getItemViewType(int position) {

            if (mList.get(position) instanceof AdlibNativeItem) {
                // 광고 타입을 추가합니다.
                return VIEW_TYPE_AD;
            } else {
                return VIEW_TYPE;
            }
        }

        @Override
        public int getViewTypeCount() {
            int viewTypeCount = super.getViewTypeCount();
            return viewTypeCount + 1; // 기존 레이아웃의 타입에 1을 더합니다.
        }

        // ---------- 리스트 아이템의 레이아웃 재정의 ---------- //

        public Adapter(Context context, ArrayList<Object> objects) {
            this.context = context;
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

            if (type == VIEW_TYPE) {

                // 기본 뷰에 대해 처리를 합니다.
                if (convertView == null) {
                    convertView = getLayoutInflater().inflate(R.layout.activity_native_feed_item_sample, null);
                }
                SampleFeedData data = (SampleFeedData) mList.get(position);
                ImageView profileImg = (ImageView) convertView.findViewById(R.id.profilePic);
                Glide.with(profileImg).load(data.getProfilePic()).into(profileImg);

                ImageView mainImg = (ImageView) convertView.findViewById(R.id.img);
                Glide.with(mainImg).load(data.getImg()).into(mainImg);

                TextView nameTxt = (TextView) convertView.findViewById(R.id.name);
                nameTxt.setText(data.getName());

                TextView statusTxt = (TextView) convertView.findViewById(R.id.status);
                statusTxt.setText(data.getStatus());

            } else if (type == VIEW_TYPE_AD) {

                // 광고 뷰에 대해 처리를 합니다.
                AdlibNativeItem item = (AdlibNativeItem) mList.get(position);
                if (convertView == null) {
                    convertView = new AdlibNativeLayout(context, R.layout.activity_native_feed_item);
                }

                if (convertView instanceof AdlibNativeLayout) {
                    AdlibNativeLayout nativeLayout = (AdlibNativeLayout) convertView;
                    if (item != null) {
                        // 광고 데이터 설정
                        nativeLayout.setAdsData(item);

                        // 인앱 랜딩 지원 - 옵션
                        // 콜백으로 전달되는 URL 변경 시 클릭 집계 문제가 발생할 수 있습니다.
                        // nativeLayout.setAdsData(item, new AdlibInAppLandingListener() {
                        //     @Override
                        //     public void onLanding(String url) {
                        //
                        //     }
                        // });
                    }
                }
            }

            return convertView;
        }
    }
}