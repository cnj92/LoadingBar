package com.cnj.loadingbar;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.demo.library.ClickListener;
import com.demo.library.DownLoadBar;

import java.util.Random;

/**
 * @author wangzy
 * @desciption
 * @date 2015/10/23. 10:18
 */
public class DownLoadActivity extends AppCompatActivity {
    DownLoadBar dlb;

    int current = 0;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_download);
        dlb = (DownLoadBar) findViewById(R.id.dlb);
        findViewById(R.id.start_success).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                current = 0;
                handler.post(runnable);
            }
        });
        dlb.setListener(new ClickListener() {
            @Override
            public void pause() {
                handler.removeCallbacks(runnable);
            }

            @Override
            public void restart() {
                handler.post(runnable);
            }
        });


    }

    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (current < 100) {
                current += new Random().nextInt(5);
//                current+=1;
            }else {
                handler.removeCallbacks(runnable);
            }
            dlb.setmCurrentProgress(current);

            handler.postDelayed(runnable, 100);
        }
    };


}
