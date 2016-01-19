package activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.basic.gaugeprogress.R;
import com.basic.gaugeprogress.view.GaugeProgressBar;

import java.util.ArrayList;

/**
 * Created by acer on 2015/11/10.
 */
public class TestActivity extends Activity {
    GaugeProgressBar bar;
    int progress;

    private int length = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_layout);
        bar = (GaugeProgressBar) findViewById(R.id.test);
        TextView textView = (TextView) findViewById(R.id.text);
        textView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                length +=2;
                bar.setProgress(length);
            }
        });

    }
}
