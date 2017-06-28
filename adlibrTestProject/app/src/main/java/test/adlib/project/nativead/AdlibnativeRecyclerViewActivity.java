package test.adlib.project.nativead;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

public class AdlibnativeRecyclerViewActivity extends Activity {

    // 일반 Activity 에서의 adlib 연동
    private AdlibManager _amanager;

    private ArrayList<Object> mList = new ArrayList<Object>();

    private RecyclerView recyclerView;
    private RecyclerView.Adapter recyclerAdapter;
    private LinearLayoutManager layoutManager;

    // 광고 뷰 표출에 도움을 주는 클래스
    // AdlibNativeHelper 사용하지 않는 경우 광고뷰 관리 필수
    private AdlibNativeHelper nativeHelper = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_native_recycler);

        initSampleFeedData();
        recyclerAdapter = new Adapter(this, mList);

        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(recyclerAdapter);

        nativeHelper = new AdlibNativeHelper(recyclerView);
        // 세로 스크롤 형태의 레이아웃에서만 사용 가능 - 추후 가로 지원 예정
        nativeHelper.registerScrollChangedListener();

        // 각 애드립 액티비티에 애드립 앱 키값을 필수로 넣어주어야 합니다.
        _amanager = new AdlibManager(AdlibTestProjectConstants.ADLIB_API_KEY);
        _amanager.onCreate(this);
        // 테스트 광고 노출로, 상용일 경우 꼭 제거해야 합니다.
        _amanager.setAdlibTestMode(AdlibTestProjectConstants.ADLIB_TEST_MODE);

        // 네이티브 광고를 받고싶다면 아래의 코드를 호출 합니다.
        // 1. 타입 : 광고 유형을 지정합니다. (ALL : 비디오와 이미지, VIDEO : 비디오만 해당, IMAGE : 이미지만 해당)
        // 2. 이벤트 리스너 : 성공과 실패를 알기 위한 리스너를 지정합니다.

        _amanager.loadNativeAd(AdlibConfig.ContentType.ALL, new AdlibNativeAdListener() {
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
                    recyclerAdapter.notifyDataSetChanged();
                    nativeHelper.update();
                }
            }

            @Override
            public void onError(int errorCode) {
                Log.d("ADLIB-Native", "onError ::: error code : " + errorCode);

                Toast.makeText(AdlibnativeRecyclerViewActivity.this, "광고수신 실패 :)", Toast.LENGTH_SHORT).show();
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

    public class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private ArrayList<Object> mList = new ArrayList<Object>();
        private Context context;

        // ---------- 리스트 아이템의 레이아웃 재정의 ---------- //

        private final int VIEW_TYPE = 0;
        private final int VIEW_TYPE_AD = 1;

        public Adapter(Context context, ArrayList<Object> objects) {
            this.context = context;
            mList = objects;

        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            RecyclerView.ViewHolder retHolder = null;
            if (viewType == VIEW_TYPE) {
                View itemView = getLayoutInflater().inflate(R.layout.activity_native_feed_item_sample, null);
                retHolder = new SampleViewHolder(itemView);
            } else {
                View itemView = new AdlibNativeLayout(context, R.layout.activity_native_feed_item);
                retHolder = new AdViewHolder(itemView);
            }
            return retHolder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            // 광고 ViewHolder
            if (holder instanceof AdViewHolder) {
                AdlibNativeItem item = (AdlibNativeItem) mList.get(position);

                AdViewHolder adHolder = (AdViewHolder) holder;
                AdlibNativeLayout layout = (AdlibNativeLayout) adHolder.itemView;

                // 광고 데이터 설정
                layout.setAdsData(item);

                // 인앱 랜딩 지원 - 옵션
                // 콜백으로 전달되는 URL 변경 시 클릭 집계 문제가 발생할 수 있습니다.
                // layout.setAdsData(item, new AdlibInAppLandingListener() {
                //     @Override
                //     public void onLanding(String url) {
                //
                //     }
                // });

            } else if (holder instanceof SampleViewHolder) {
                SampleFeedData data = (SampleFeedData) mList.get(position);
                SampleViewHolder sampleHolder = (SampleViewHolder) holder;
                ImageView profileImg = sampleHolder.getProfileImg();
                Glide.with(profileImg).load(data.getProfilePic()).into(profileImg);

                ImageView mainImg = sampleHolder.getMainImg();
                Glide.with(mainImg).load(data.getImg()).into(mainImg);

                TextView nameTxt = sampleHolder.getNameTxt();
                nameTxt.setText(data.getName());

                TextView statusTxt = sampleHolder.getStatusTxt();
                statusTxt.setText(data.getStatus());
            }
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }

        @Override
        public int getItemViewType(int position) {
            if (mList.get(position) instanceof AdlibNativeItem) {
                // 광고 타입을 추가합니다.
                return VIEW_TYPE_AD;
            } else {
                return VIEW_TYPE;
            }
        }
    }

    public class SampleViewHolder extends RecyclerView.ViewHolder {

        private ImageView profileImg;
        private ImageView mainImg;
        private TextView nameTxt;
        private TextView statusTxt;

        public SampleViewHolder(View itemView) {
            super(itemView);
            profileImg = (ImageView) itemView.findViewById(R.id.profilePic);
            mainImg = (ImageView) itemView.findViewById(R.id.img);
            nameTxt = (TextView) itemView.findViewById(R.id.name);
            statusTxt = (TextView) itemView.findViewById(R.id.status);
        }

        public ImageView getMainImg() {
            return mainImg;
        }

        public TextView getNameTxt() {
            return nameTxt;
        }

        public ImageView getProfileImg() {
            return profileImg;
        }

        public TextView getStatusTxt() {
            return statusTxt;
        }
    }

    public class AdViewHolder extends RecyclerView.ViewHolder {

        public AdViewHolder(View itemView) {
            super(itemView);
        }
    }
}
