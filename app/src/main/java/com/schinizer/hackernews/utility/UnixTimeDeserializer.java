package com.schinizer.hackernews.utility;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.Date;

/**
 * Created by DPSUser on 10/13/2016.
 */

public class UnixTimeDeserializer implements JsonDeserializer<Date> {
    public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        return new Date(json.getAsLong() * 1000); // Data is in seconds, Date() needs milliseconds
    }
}