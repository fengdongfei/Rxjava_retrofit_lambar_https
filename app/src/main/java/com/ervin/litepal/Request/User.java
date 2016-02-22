package com.ervin.litepal.Request;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Ervin on 2015/11/20.
 */
public class User {

    @SerializedName("id")
    int mId;

    @SerializedName("name")
    String mName;

    public User(int id, String name ) {
        this.mId = id;
        this.mName = name;
    }
}
