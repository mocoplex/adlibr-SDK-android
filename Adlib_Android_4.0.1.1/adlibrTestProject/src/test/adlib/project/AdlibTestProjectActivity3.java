package test.adlib.project;
import com.mocoplex.adlib.AdlibActivity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

public class AdlibTestProjectActivity3 extends AdlibActivity {
    
    protected com.mocoplex.adlib.AdlibAdViewContainer avc = null;
    
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main3);
        
        View.OnClickListener cl;
        
        cl = new View.OnClickListener()
        {
			@Override
			public void onClick(View v) {
			
				if(avc != null)
					return;
				
				// 액티비티에 AdlibAdViewContainer 는 하나만 허용합니다.
				// 두개 이상 로드될 경우 문제가 발생할 수 있으며
				// 새로 로드하는 경우 반드시 기존 광고뷰를 삭제하고 다시 바인드 해주세요.
				
		        // 동적으로 adview 를 생성합니다.
		        avc = new com.mocoplex.adlib.AdlibAdViewContainer(AdlibTestProjectActivity3.this);        
                LayoutParams p = new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT);
                avc.setLayoutParams(p);
		        
		        ViewGroup vg = (ViewGroup)findViewById(R.id.maincontainer);
		        vg.addView(avc);
		        
		        // 동적으로 생성한 adview 에 스케줄러를 바인드합니다.
		        bindAdsContainer(avc);				
			}
        	
        };
        this.findViewById(R.id.btn1).setOnClickListener(cl);
        
        cl = new View.OnClickListener()
        {
			@Override
			public void onClick(View v) {
				
				if(avc == null)
					return;

				AdlibTestProjectActivity3.this.destroyAdsContainer();
				avc = null;
			}
        	
        };
        this.findViewById(R.id.btn2).setOnClickListener(cl);                
    }    
}