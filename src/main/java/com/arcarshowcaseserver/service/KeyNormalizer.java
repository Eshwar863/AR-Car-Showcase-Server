package com.arcarshowcaseserver.service;

import java.util.Map;

public class KeyNormalizer {

    private static final Map<String, String> KEY_ALIASES = Map.ofEntries(
        Map.entry("petrol fuel tank capacity",    "Fuel Tank Capacity"),
        Map.entry("diesel fuel tank capacity",    "Fuel Tank Capacity"),
        Map.entry("cng fuel tank capacity",       "Fuel Tank Capacity"),
        Map.entry("electric fuel tank capacity",  "Fuel Tank Capacity"),
        Map.entry("petrol mileage arai",          "ARAI Mileage"),
        Map.entry("diesel mileage arai",          "ARAI Mileage"),
        Map.entry("arai mileage",                 "ARAI Mileage"),
        Map.entry("petrol highway mileage",       "Highway Mileage"),
        Map.entry("diesel highway mileage",       "Highway Mileage"),
        Map.entry("petrol city mileage",          "City Mileage"),
        Map.entry("diesel city mileage",          "City Mileage"),
        Map.entry("0-100kmph",                    "0-100 kmph"),
        Map.entry("0-100kmph (tested)",           "0-100 kmph"),
        Map.entry("acceleration",                 "0-100 kmph")
    );

    public static String normalize(String rawKey) {
        if (rawKey == null || rawKey.isBlank()) return "";
        return KEY_ALIASES.getOrDefault(rawKey.toLowerCase().trim(), rawKey);
    }
}
