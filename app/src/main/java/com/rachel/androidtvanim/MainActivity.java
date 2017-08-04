package com.rachel.androidtvanim;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;

import com.rachel.androidtvanim.view.FlyBroadLayout;
import com.rachel.androidtvanim.view.MainUpLayout;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MainUpLayout mainUpLayout = (MainUpLayout) findViewById(R.id.activity_base_content);
        final FlyBroadLayout flyBroadLayout = (FlyBroadLayout) findViewById(R.id.activity_base_mainupview);
        mainUpLayout.getViewTreeObserver().addOnGlobalFocusChangeListener(new ViewTreeObserver.OnGlobalFocusChangeListener() {
            @Override
            public void onGlobalFocusChanged(View oldFocus, View newFocus) {
                flyBroadLayout.setFocusView(newFocus,oldFocus,1.2f);
            }
        });
    }
}
