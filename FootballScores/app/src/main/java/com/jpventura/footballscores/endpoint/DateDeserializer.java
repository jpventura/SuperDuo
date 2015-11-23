package com.jpventura.footballscores.endpoint;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateDeserializer implements JsonDeserializer<Date> {
    private static SimpleDateFormat sDateTime = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
    private static SimpleDateFormat sDate = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public Date deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
        try {
            return parse(json.getAsJsonPrimitive().getAsString());
        } catch (ParseException e) {
            throw new JsonParseException(e.getMessage(), e);
        }
    }

    private Date parse(String token) throws ParseException {
        if ((null == token) || (token.length() == 0)) return null;

        try {
            return sDateTime.parse(token);
        } catch (ParseException e) {
            return sDate.parse(token);
        }
    }
}
