package com.umai.injection.module;

import android.app.Application;
import android.content.Context;

import com.umai.data.remote.TodoServices;
import com.umai.injection.ApplicationContext;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author Rajan Maurya
 *         Provide application-level dependencies.
 */
@Module
public class ApplicationModule {
    protected final Application mApplication;

    public ApplicationModule(Application application) {
        mApplication = application;
    }

    @Provides
    Application provideApplication() {
        return mApplication;
    }

    @Provides
    @ApplicationContext
    Context provideContext() {
        return mApplication;
    }

    @Provides
    @Singleton
    TodoServices provideRibotsService() {
        return TodoServices.Creator.newTodosService();
    }
}
