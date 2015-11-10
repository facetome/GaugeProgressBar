package activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.basic.gaugeprogress.R;
import com.basic.gaugeprogress.view.GaugeProgressBar;

/**
 * Created by acer on 2015/11/10.
 */
public class TestActivity extends Activity {
    GaugeProgressBar bar;
    int progress;

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            bar.setProgress(100);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_layout);
        bar = (GaugeProgressBar) findViewById(R.id.test);
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Log.d("wangliansen", "---------------------" + progress);
                        Thread.sleep(500);
                        if (progress <= 0) {
                            progress += 20;
                        } else if(progress < 100){
                            progress += 2;
                        }  else if (progress >= 100) {
                            progress -= 100;
                        }
                        Message message = mHandler.obtainMessage();
                        message.arg1 = progress;
                        mHandler.sendMessage(message);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

        }).start();

    }
}
