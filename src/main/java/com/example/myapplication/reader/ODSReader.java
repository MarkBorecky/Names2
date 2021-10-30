package com.example.myapplication.reader;

import com.example.myapplication.model.Card;
import com.github.miachm.sods.SpreadSheet;
import org.apache.logging.log4j.util.Strings;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ODSReader {
    public static Predicate<? super String> startFromCapital = s -> s.matches("[A-ZĄĆĘŁŚÓŹŻ][a-ząęćśżźłó]{1,9}");

    public static List<Card> read(String s) throws IOException {
        File initialFile = new File(s);
        InputStream is = new FileInputStream(initialFile);
        return read(is);
    }

    public static List<Card> read(InputStream is) throws IOException {
        var spread = new SpreadSheet(is);
        var sheet = spread.getSheets().get(0);
        var range = sheet.getDataRange();
        var values = range.getValues();
        return getDataDaoList(values);
    }

    public static List<Card> getDataDaoList(Object[][] values) {
        return Arrays.stream(values)
                .skip(1)
                .map(ODSReader::getColumn5)
                .filter(Strings::isNotBlank)
                .map(ODSReader::getCards)
                .filter(x -> x != null)
                .collect(Collectors.toList());
    }

    private static String getColumn5(Object[] objects) {
        return  (String) objects[5];
    }

    public static Card getCards(String text) {
        return new Card(text, getFullNames(text));
    }

    public static List<String> getFullNames(String text) {
        var regex = "[A-ZĄĆĘŁŚÓŹŻ][a-ząęćżźłó]{0,19}[ ][A-ZĄĆĘŁŚÓŹŻ][a-ząęćżźłó]{0,19}";
        text = text.replaceAll("[”„]", "");
        String[] split = text.split(regex);
        for (var s : split) {
            if (Strings.isNotBlank(s)) {
                text = text.replace(s, "\n");
            }
        }
        List<String> list = Arrays.asList(text.split("\n"));
        List<String> result = new ArrayList<>();
        for (var s : list) {
            if (Strings.isNotBlank(s)) {
                result.add(s);
            }
        }
        return result;
    }
}