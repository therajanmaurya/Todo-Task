package com.umai;

import android.app.Application;
import android.content.Context;

import com.umai.injection.component.ApplicationComponent;
import com.umai.injection.component.DaggerApplicationComponent;
import com.umai.injection.module.ApplicationModule;

/**
 * Created by Rajan Maurya on 10/10/16.
 */

public class UmaiApplication extends Application {

    ApplicationComponent mApplicationComponent;

    public static UmaiApplication get(Context context) {
        return (UmaiApplication) context.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public ApplicationComponent getComponent() {
        if (mApplicationComponent == null) {
            mApplicationComponent = DaggerApplicationComponent.builder()
                    .applicationModule(new ApplicationModule(this))
                    .build();
        }
        return mApplicationComponent;
    }

    // Needed to replace the component with a test specific one
    public void setComponent(ApplicationComponent applicationComponent) {
        mApplicationComponent = applicationComponent;
    }
}
