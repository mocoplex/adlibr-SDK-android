package test.adlib.project;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.MemoryCategory;
import com.mocoplex.adlib.AdlibConfig;
import com.mocoplex.adlib.AdlibManager;

import java.util.ArrayList;

import test.adlib.project.banner.AdlibBannerDynamicActivity;
import test.adlib.project.banner.AdlibBannerMediationActivity;
import test.adlib.project.etc.AdlibIconAdActivity;
import test.adlib.project.etc.AdlibSampleActivity;
import test.adlib.project.interstitial.AdlibIntersDynamicActivity;
import test.adlib.project.interstitial.AdlibIntersMediationActivity;
import test.adlib.project.nativead.AdlibNativeSampleFeedActivity;
import test.adlib.project.nativead.AdlibNativeSampleSongActivity;
import test.adlib.project.nativead.AdlibnativeRecyclerViewActivity;

public class AdlibTestProjectActivity extends ListActivity {

    // 일반 Activity 에서의 adlib 연동
    private AdlibManager adlibManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Glide Library
        Glide.get(this).setMemoryCategory(MemoryCategory.HIGH);

        // 샘플 프로젝트 화면 구성
        initLayout();

        // adlibr.com 에서 발급받은 api 키를 입력합니다.
        // ADLIB - API - KEY 설정
        // 각 애드립 액티비티에 애드립 앱 키값을 필수로 넣어주어야 합니다.
        adlibManager = new AdlibManager(AdlibTestProjectConstants.ADLIB_API_KEY);
        adlibManager.onCreate(this);
        // 테스트 광고 노출로, 상용일 경우 꼭 제거해야 합니다.
        adlibManager.setAdlibTestMode(AdlibTestProjectConstants.ADLIB_TEST_MODE);

        // 미디에이션 스케쥴 관련 설정
        bindPlatform();

        // 다이얼로그 광고 요청
        adlibManager.requestAdDialog(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case AdlibManager.DID_SUCCEED:
                        break;

                    case AdlibManager.DID_ERROR:
                        break;
                }
            }
        });
    }

    @Override
    protected void onListItemClick(ListView list, View view, int position, long id) {
        super.onListItemClick(list, view, position, id);

        Intent intent = null;

        AdlibTestListItem item = (AdlibTestListItem) view.getTag();
        switch (item) {
            case BANNER_MEDIATION:
                intent = new Intent(this, AdlibBannerMediationActivity.class);
                break;
            case BANNER_DYNAMIC:
                intent = new Intent(this, AdlibBannerDynamicActivity.class);
                break;

            case INTERSTITIAL_MEDIATION:
                intent = new Intent(this, AdlibIntersMediationActivity.class);
                break;
            case INTERSTITIAL_DYNAMIC:
                intent = new Intent(this, AdlibIntersDynamicActivity.class);
                break;

            case NATIVE_LIST_1:
                intent = new Intent(this, AdlibNativeSampleFeedActivity.class);
                break;
            case NATIVE_LIST_2:
                intent = new Intent(this, AdlibNativeSampleSongActivity.class);
                break;
            case NATIVE_RECYCLER:
                intent = new Intent(this, AdlibnativeRecyclerViewActivity.class);
                break;

            case ICON_AD:
                intent = new Intent(this, AdlibIconAdActivity.class);
                break;
            case ADLIB_ACTIVITY:
                intent = new Intent(this, AdlibSampleActivity.class);
                break;
        }

        if (intent != null) {
            startActivity(intent);
        }
    }

    // 필수 사항
    protected void onResume() {
        adlibManager.onResume(this);
        super.onResume();
    }

    // 필수 사항
    protected void onPause() {
        adlibManager.onPause(this);
        super.onPause();
    }

    // 필수 사항
    protected void onDestroy() {
        adlibManager.onDestroy(this);
        super.onDestroy();
    }

    private void bindPlatform() {
        // 광고 스케줄링 설정 - 전면, 띠 배너 동일
        // AdlibManager 생성 및 onCreate() 이후
        // 광고 요청 이전에 해당 스케쥴 관련 타 플랫폼 정보 등록
        // 첫번 째 AdlibManager 생성 시에 호출
        // 광고 subview 의 패키지 경로를 설정 (실제로 작성된 패키지 경로로 변경)

        // 쓰지 않을 광고 플랫폼은 삭제해주세요.
        AdlibConfig.getInstance().bindPlatform("ADMIXER", "test.adlib.project.ads.SubAdlibAdViewAdmixer");
        AdlibConfig.getInstance().bindPlatform("ADAM", "test.adlib.project.ads.SubAdlibAdViewAdam");
        AdlibConfig.getInstance().bindPlatform("ADMOB", "test.adlib.project.ads.SubAdlibAdViewAdmob");
        AdlibConfig.getInstance().bindPlatform("AMAZON", "test.adlib.project.ads.SubAdlibAdViewAmazon");
        AdlibConfig.getInstance().bindPlatform("MOBCLIX", "test.adlib.project.ads.SubAdlibAdViewMobclix");
        AdlibConfig.getInstance().bindPlatform("CAULY", "test.adlib.project.ads.SubAdlibAdViewCauly");
        AdlibConfig.getInstance().bindPlatform("FACEBOOK", "test.adlib.project.ads.SubAdlibAdViewFacebook");
        AdlibConfig.getInstance().bindPlatform("INMOBI", "test.adlib.project.ads.SubAdlibAdViewInmobi");
        AdlibConfig.getInstance().bindPlatform("MEDIBAAD", "test.adlib.project.ads.SubAdlibAdViewMedibaAd");
        AdlibConfig.getInstance().bindPlatform("MEZZO", "test.adlib.project.ads.SubAdlibAdViewMezzo");
        AdlibConfig.getInstance().bindPlatform("MMEDIA", "test.adlib.project.ads.SubAdlibAdViewMMedia");
        AdlibConfig.getInstance().bindPlatform("MOBFOX", "test.adlib.project.ads.SubAdlibAdViewMobfox");
        AdlibConfig.getInstance().bindPlatform("MOPUB", "test.adlib.project.ads.SubAdlibAdViewMopub");
        AdlibConfig.getInstance().bindPlatform("SHALLWEAD", "test.adlib.project.ads.SubAdlibAdViewShallWeAd");
        AdlibConfig.getInstance().bindPlatform("TAD", "test.adlib.project.ads.SubAdlibAdViewTAD");
        AdlibConfig.getInstance().bindPlatform("TNK", "test.adlib.project.ads.SubAdlibAdViewTNK");
        AdlibConfig.getInstance().bindPlatform("UPLUSAD", "test.adlib.project.ads.SubAdlibAdViewUPlusAD");

    }

    @Override
    public void onBackPressed() {
        // 다이얼로그 광고 사용 가능 여부 확인
        // 광고가 없는 경우 타 플랫폼이나 매체 내부 동작 처리가 필요한 경우 사용가능한 함수
        // adlibManager.isAvailableAdDialog();

        // 종료 대화상자 광고를 노출하기 위해서 호출합니다.
        adlibManager.showAdDialog("취소", "확인", "종료하시겠습니까?");

        // 필요시 아래와 같이 색상과 클릭 액션을 변경할 수 있습니다.
        // backgroundColor, backgroundColor(click), textColor, textColor, textColor(click), lineColor
        // int[] colors = new int[]{0xffffffff, 0xffa8a8a8, 0xff404040, 0xff404040, 0xffdfdfdf};
        // adlibManager.showAdDialog("취소", "확인", "종료하시겠습니까?", colors, new AdlibDialogAdListener() {
        //
        //     @Override
        //     public void onLeftClicked() {
        //     }
        //
        //     @Override
        //     public void onRightClicked() {
        //     }
        //
        // });
    }

    // Sample layout
    private void initLayout() {

        ArrayList<AdlibTestListItem> itemList = new ArrayList<AdlibTestListItem>();

        // 띠배너 샘플
        itemList.add(AdlibTestListItem.BANNER_HEADER);
        itemList.add(AdlibTestListItem.BANNER_MEDIATION);
        itemList.add(AdlibTestListItem.BANNER_DYNAMIC);

        // 전면배너 샘플
        itemList.add(AdlibTestListItem.INTERSTITIAL_HEADER);
        itemList.add(AdlibTestListItem.INTERSTITIAL_MEDIATION);
        itemList.add(AdlibTestListItem.INTERSTITIAL_DYNAMIC);

        // 네이티브 샘플
        itemList.add(AdlibTestListItem.NATIVE_HEADER);
        itemList.add(AdlibTestListItem.NATIVE_LIST_1);
        itemList.add(AdlibTestListItem.NATIVE_LIST_2);
        itemList.add(AdlibTestListItem.NATIVE_RECYCLER);

        // 기타 샘플
        itemList.add(AdlibTestListItem.ETC_HEADER);
        itemList.add(AdlibTestListItem.ICON_AD);
        itemList.add(AdlibTestListItem.ADLIB_ACTIVITY);

        AdlibTestListAdapter adapter = new AdlibTestListAdapter(this, itemList);
        this.setListAdapter(adapter);
    }

    public class AdlibTestListAdapter extends BaseAdapter {
        private final int HEADER = 0;
        private final int ITEM = 1;

        private Context context;
        private ArrayList<AdlibTestListItem> list;

        public AdlibTestListAdapter(Context context, ArrayList<AdlibTestListItem> list) {
            this.context = context;
            this.list = list;
        }

        @Override
        public int getViewTypeCount() {
            return 2;
        }

        @Override
        public int getItemViewType(int position) {

            if (list.get(position).isHeader()) {
                return HEADER;
            } else {
                return ITEM;
            }
        }

        @Override
        public int getCount() {
            if (list != null) {
                return list.size();
            }
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int arg0) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            AdlibTestListItem data = list.get(position);
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(getItemViewType(position) == HEADER ? R.layout.main_list_header : R.layout.main_list_item, null);
            }

            TextView txtItem = (TextView) convertView.findViewById(R.id.text);
            txtItem.setText(data.getValue());
            convertView.setTag(data);
            return convertView;
        }
    }
}

