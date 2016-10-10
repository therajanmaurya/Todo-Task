package com.umai.ui.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.umai.UmaiApplication;
import com.umai.injection.component.ActivityComponent;
import com.umai.injection.component.DaggerActivityComponent;
import com.umai.injection.module.ActivityModule;

public class BaseActivity extends AppCompatActivity {

    private ActivityComponent mActivityComponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public ActivityComponent activityComponent() {
        if (mActivityComponent == null) {
            mActivityComponent = DaggerActivityComponent.builder()
                    .activityModule(new ActivityModule(this))
                    .applicationComponent(UmaiApplication.get(this).getComponent())
                    .build();
        }
        return mActivityComponent;
    }
}