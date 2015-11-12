package activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.basic.gaugeprogress.R;
import com.basic.gaugeprogress.view.GaugeProgressBar;

import java.util.ArrayList;

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

        ArrayList arrayList = new ArrayList();
        arrayList.add("1");
        arrayList.add("2");
        arrayList.add("3");

        arrayList.remove(0);
        Log.d("wangliangsen", arrayList.get(0).toString() + ">>>>>>>>>>>>>>>>>1");
        for(int i=0;i<arrayList.size();i++){
            Log.d("wangliangsen", arrayList.get(i).toString() + ">>>>>>>>>>>>>>>>>");
        }
        bar = (GaugeProgressBar) findViewById(R.id.test);
        new Thread(new Runnable() {
            @Override
            public void run() {
              while (true) {
                    try {
                        Log.d("wangliansen", "---------------------" + progress);
                        if(progress >= 100){
                           Thread.sleep(100000);
                        }
                          progress +=2;
                          Thread.sleep(50);
                          Message message = mHandler.obtainMessage();
                          message.arg1 = progress;
                          mHandler.sendMessage(message);
//                         Thread.sleep(10);
//                        progress = 10;
//                        Message message1 = mHandler.obtainMessage();
//                        message1.arg1 = progress;
//                       mHandler.sendMessage(message1);
//                        Thread.sleep(10);
//                        progress = 50;
//                        Message message2 = mHandler.obtainMessage();
//                        message2.arg1 = progress;
//                        mHandler.sendMessage(message2);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
           }

        }).start();

    }
}
