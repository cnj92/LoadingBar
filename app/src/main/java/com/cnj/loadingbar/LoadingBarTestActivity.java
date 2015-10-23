package com.cnj.loadingbar;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.demo.library.LoadingBar;

/**
 * @author wangzy
 * @desciption
 * @date 2015/10/23. 10:13
 */
public class LoadingBarTestActivity extends AppCompatActivity{
    private LoadingBar lb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_loading);

        lb = (LoadingBar) findViewById(R.id.lb_loading);
        lb.loading();
        findViewById(R.id.failed).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lb.loadingComplete(false);
            }
        });

        findViewById(R.id.success).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lb.loadingComplete(true);
            }
        });
    }
}
