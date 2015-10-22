package com.cnj.loadingbar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.demo.library.LoadingBar;

public class MainActivity extends AppCompatActivity {
    private LoadingBar lb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lb = (LoadingBar) findViewById(R.id.lb_loading);
        lb.loading();
    }


}
