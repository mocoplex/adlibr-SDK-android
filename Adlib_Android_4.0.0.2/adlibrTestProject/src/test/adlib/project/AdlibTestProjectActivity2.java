package test.adlib.project;
import com.mocoplex.adlib.AdlibActivity;
import android.os.Bundle;

public class AdlibTestProjectActivity2 extends AdlibActivity {
    /** Called when the activity is first created. */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main2);
        
        this.setAdsContainer(R.id.ads);   
    }    
}