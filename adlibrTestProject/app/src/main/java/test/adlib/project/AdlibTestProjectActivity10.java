package test.adlib.project;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.mocoplex.adlib.AdError;
import com.mocoplex.adlib.AdlibConfig;
import com.mocoplex.adlib.AdlibIconAd;
import com.mocoplex.adlib.AdlibIconAdListener;

public class AdlibTestProjectActivity10 extends Activity {

    private AdlibIconAd ad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main5);

        ad = new AdlibIconAd(this, AdlibTestProjectConstants.ADLIB_API_KEY);
        ad.setTestMode(AdlibTestProjectConstants.ADLIB_TEST_MODE);
        ad.setAlign(AdlibConfig.IconAlign.BOTTOM_RIGHT);
        ad.setAdListener(new AdlibIconAdListener() {

            @Override
            public void onReceiveAd() {
                Toast.makeText(AdlibTestProjectActivity10.this, "Receive", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailedToReceiveAd(AdError error) {
                Toast.makeText(AdlibTestProjectActivity10.this, "Fail : " + error.getErrorMessage(), Toast.LENGTH_SHORT).show();
            }

        });

        Button btn = (Button) findViewById(R.id.btn);
        btn.setText("loadIconAd");

        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ad.loadAd();
            }
        });
    }

    @Override
    protected void onResume() {
        if(ad != null) {
            ad.resumeAd();
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        if(ad != null) {
            ad.pauseAd();
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if(ad != null) {
            ad.destroyAd();
        }
        super.onDestroy();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if(ad != null) {
            ad.reloadAd();
        }
        super.onConfigurationChanged(newConfig);
    }
}
