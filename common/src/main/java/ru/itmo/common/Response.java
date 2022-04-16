package ru.itmo.common;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class Response {
    public final Status status;
    public final Object argument;


    public Response(Status status, Object argument) {
        this.status = status;
        this.argument = new Gson().toJson(argument);
    }


    public static Response fromJson(String json) {
        return new Gson().fromJson(json, Response.class);
    }


    public <T> T getArgumentAs(Class<T> clazz) {
        return new Gson().fromJson((String) argument, clazz);
    }

    public <T> Object getArgumentAs(TypeToken<T> typeToken) {
        return new Gson().fromJson((String) argument, typeToken.getType());
    }

    public String toJson() {
        return new Gson().toJson(this);
    }

    public enum Status {
        OK,
        ERROR
    }
}
