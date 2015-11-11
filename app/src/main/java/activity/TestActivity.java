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
            bar.setProgress(msg.arg1);
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
            //    while (true) {
                    try {
                        Log.d("wangliansen", "---------------------" + progress);

                        progress = 50;
                        Message message = mHandler.obtainMessage();
                        message.arg1 = progress;
                        mHandler.sendMessage(message);
                         Thread.sleep(5000);
                        progress = 10;
                        Message message1 = mHandler.obtainMessage();
                        message1.arg1 = progress;
                        mHandler.sendMessage(message1);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
  //          }

        }).start();

    }
}
