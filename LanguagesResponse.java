package com.example.tranlator;

import java.util.Map;


public class LanguagesResponse {
    Map<String, Language> translation;

    @Override
    public String toString() {
        String  languages = "";
        for (String l: translation.keySet()) {
            Language value = translation.get(l);
            languages += l + "- " + value.name + " " + value.nativeName+ ":";
        }
        return languages;
    }
}