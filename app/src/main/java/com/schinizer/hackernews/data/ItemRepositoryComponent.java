package com.schinizer.hackernews.data;

import com.schinizer.hackernews.AppModule;
import com.schinizer.hackernews.utility.schedulers.SchedulersModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by DPSUser on 10/14/2016.
 */

@Singleton
@Component(modules = {AppModule.class, NetModule.class, SchedulersModule.class, APIModule.class, AndroidAPIModule.class, ItemDataSourceModule.class})
public interface ItemRepositoryComponent {
    ItemRepository getItemRepository();
}
