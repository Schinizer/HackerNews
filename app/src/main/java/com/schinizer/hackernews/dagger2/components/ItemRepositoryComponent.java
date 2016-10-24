package com.schinizer.hackernews.dagger2.components;

import com.schinizer.hackernews.dagger2.modules.APIModule;
import com.schinizer.hackernews.dagger2.modules.AndroidAPIModule;
import com.schinizer.hackernews.dagger2.modules.AppModule;
import com.schinizer.hackernews.dagger2.modules.ItemRepositoryModule;
import com.schinizer.hackernews.dagger2.modules.NetModule;
import com.schinizer.hackernews.data.ItemRepository;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by DPSUser on 10/14/2016.
 */

@Singleton
@Component(modules = {AppModule.class, NetModule.class, APIModule.class, AndroidAPIModule.class, ItemRepositoryModule.class})
public interface ItemRepositoryComponent {
    ItemRepository getItemRepository();
}
