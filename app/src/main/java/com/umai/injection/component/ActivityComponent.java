package com.umai.injection.component;

import com.umai.injection.PerActivity;
import com.umai.injection.module.ActivityModule;
import com.umai.ui.main.MainActivity;

import dagger.Component;

/**
 * @author Rajan Maurya
 *         This component inject dependencies to all Activities across the application
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(MainActivity mainActivity);
}
