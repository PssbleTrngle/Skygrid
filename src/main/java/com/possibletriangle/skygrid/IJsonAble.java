package com.possibletriangle.skygrid;

import com.google.gson.JsonElement;

public interface IJsonAble {

    JsonElement toJSON();
    void fromJSON(JsonElement json);
    String key();
    boolean isValid();
    void validate();

}
