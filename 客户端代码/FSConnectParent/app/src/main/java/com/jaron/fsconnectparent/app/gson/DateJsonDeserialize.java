package com.jaron.fsconnectparent.app.gson;

import android.util.Log;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.Date;

/**
 * Created by Jaron on 2018/6/7.
 */

public class DateJsonDeserialize implements JsonDeserializer {
    @Override
    public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        try {
            return new Date(json.getAsJsonPrimitive().getAsLong());
        }catch(Exception e){
            Log.i("FSConnectLog","DateJsonDeserialize-deserialize-error:" + (json != null ? json.toString() : ""));
            e.printStackTrace();
            return null;
        }

    }
}
