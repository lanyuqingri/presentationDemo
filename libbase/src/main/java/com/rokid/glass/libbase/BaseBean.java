package com.rokid.glass.libbase;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rokid.glass.libbase.logger.Logger;

public class BaseBean {

    @Override
    public String toString() {
        String str = new Gson().toJson(this);
        Logger.d(this.getClass().getSimpleName() + "@" + Integer.toHexString(this.hashCode()) + str);
        return str;
    }

    public String toJson() {
        return new Gson().toJson(this);
    }

    public String toJson(double version) {
        return new GsonBuilder()
                .setVersion(version)
                .create()
                .toJson(this);
    }

}
