package com.schinizer.hackernews.dagger2.components;

import com.schinizer.hackernews.dagger2.modules.APIModule;
import com.schinizer.hackernews.dagger2.modules.AndroidAPIModule;
import com.schinizer.hackernews.dagger2.modules.AppModule;
import com.schinizer.hackernews.dagger2.modules.ItemDataSourceModule;
import com.schinizer.hackernews.dagger2.modules.NetModule;
import com.schinizer.hackernews.dagger2.modules.SchedulersModule;
import com.schinizer.hackernews.data.ItemRepository;

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
