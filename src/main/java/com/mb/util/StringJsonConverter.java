package com.mb.util;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

public class StringJsonConverter {

    private StringJsonConverter() {

    }

    public static <T> T convertJsonToObject(Class<T> objectClass, String s) {
        Gson gson = new Gson();
        return gson.fromJson(s, objectClass);
    }

    public static <T> List<T> convertJsonToObjectList(Type type, String s) {
        Gson gson = new Gson();
        return gson.fromJson(s, type);
    }

    public static String convertMapToJson(Map<String, Object> dataMap) {
        Gson gson = new Gson();
        Type gsonType = new TypeToken<HashMap<String, Object>>() {
        }.getType();
        return gson.toJson(dataMap, gsonType);
    }

    public static <T> T convertMapToObject(Map<String, String> dataMap, Class<T> objectClass) {
        Gson gson = new Gson();
        JsonElement jsonElement = gson.toJsonTree(dataMap);
        return gson.fromJson(jsonElement, objectClass);
    }

    /**
     * This method takes a list in json form and converts it to JsonArray
     *
     * @param list
     * @return
     */
    public static JsonArray convertListToJsonArray(List<?> list) {
        Gson gson = new Gson();
        String data = gson.toJson(list);
        return new JsonParser().parse(data).getAsJsonArray();
    }

    /**
     * This method takes a list in json form [{"a":"b"},{"c":"d"}] and converts it to JsonArray String
     *
     * @param list
     * @return
     */
    @SuppressWarnings("unchecked")
    public static String createJsonArray(List<String> list) {
        JSONArray jsonArray = new JSONArray();
        for (String el : list) {
            Map<String, String> map = convertJsonToObject(Map.class, el);
            JSONObject jsonObj = new JSONObject(map);
            jsonArray.add(jsonObj);
        }
        return jsonArray.toJSONString();
    }

    public static String convertToJson(List<?> list) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Type type = new TypeToken<List<?>>() {}.getType();
        return gson.toJson(list, type);
    }

}
