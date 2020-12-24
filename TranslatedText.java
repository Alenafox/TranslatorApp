package com.example.tranlator;

import java.util.ArrayList;

class TranslatedText {
    ArrayList<Translation> translations;
    @Override
    public String toString() {
        String translation = "";
        for (Translation t: translations){
            translation +=  t.text;
        }
        return translation;
    }
}
