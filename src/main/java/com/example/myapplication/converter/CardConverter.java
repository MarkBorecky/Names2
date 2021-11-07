package com.example.myapplication.converter;

import com.example.myapplication.model.Card;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class CardConverter {

    public static final String TITLE_CASE = "[A-ZĄĆĘŁÓŻŹ][a-ząćęłóżź]{1,50}";
    public static final Pattern PATTERN = Pattern.compile(String.format("(%s+\s+%s(,+\s+%s){0,9})", TITLE_CASE, TITLE_CASE, TITLE_CASE));

    public static String clearText(String text) {
        return text.replaceAll("[”„]", "");
    }

    public static Card getCards(String text) {
        return new Card(text, CardConverter.getNamesList(text));
    }

    public static List<String> getNamesList(String text) {
        var matcher = PATTERN.matcher(clearText(text));
        var result = new ArrayList<String>();
        while (matcher.find()) result.addAll(unwrapIfIsFamilyList(matcher.group()));
        return result;
    }

    public static List<String> unwrapIfIsFamilyList(String input) {
        return input.contains(", ") ? unwrapFamilyList(input) : List.of(input);
    }

    private static List<String> unwrapFamilyList(String input) {
        var surname = input.split(" ")[0];
        return Arrays.stream(input.split(", "))
                .map(name -> name.contains(" ") ? name : String.format("%s %s", surname, name))
                .toList();
    }
}
