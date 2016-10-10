package com.umai.injection.component;

import android.app.Application;
import android.content.Context;

import com.umai.data.DataManager;
import com.umai.injection.ApplicationContext;
import com.umai.injection.module.ApplicationModule;

import javax.inject.Singleton;

import dagger.Component;


/**
 * @author Rajan Maurya
 */
@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {


    @ApplicationContext
    Context context();

    Application application();
    DataManager dataManager();

}
