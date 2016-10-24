package com.schinizer.hackernews.utility;

import com.google.gson.TypeAdapterFactory;
import com.ryanharter.auto.value.gson.GsonTypeAdapterFactory;

/**
 * Created by DPSUser on 10/19/2016.
 */

@GsonTypeAdapterFactory
public abstract class AutoValueAdapterFactory implements TypeAdapterFactory {

    // Static factory method to access the package
    // private generated implementation
    public static TypeAdapterFactory create() {
        return new AutoValueGson_AutoValueAdapterFactory();
    }
}
