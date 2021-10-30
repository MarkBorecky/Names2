package com.example.myapplication.reader;

import com.example.myapplication.model.Card;
import com.example.myapplication.model.Person;
import com.example.myapplication.model.PersonCollector;
import com.github.miachm.sods.SpreadSheet;
import one.util.streamex.StreamEx;
import org.apache.logging.log4j.util.Strings;
import org.javatuples.Pair;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
        return (String) objects[5];
    }

    public static Card getCards(String text) {
        return new Card(text, getFullNames(text));
    }

    private static Pair<String, ArrayList<String>> getFamillyNames(String text) {
        var regex = "[A-ZĄĆĘŁŚÓŹŻ][a-ząęćżźłó]{0,19}[ ][A-ZĄĆĘŁŚÓŹŻ][a-ząęćżźłó]{0,19}(, [A-ZĄĆĘŁŚÓŹŻ][a-ząęćżźłó]{0,19}){0,5}";
        var splitedText = text.split(regex);
        for (var s : splitedText) {
            if (Strings.isNotBlank(s)) {
                text = text.replaceAll(s, " ");
            }
        }
        text = text.trim();

        var fullNames = new ArrayList<String>();
        fullNames.addAll(List.of(text.split(", ")));
        var list = new ArrayList<String>();
        var toRemove = new ArrayList<String>();
        var firstFullName = fullNames.remove(0);
        var streamStr = firstFullName.split(" ");
        List<Person> people = pairStrings(streamStr);

        people.forEach(p -> list.add(p.toString()));
        if (people.size() > 0) {
            var surname = people.get(people.size() - 1).lastName();
            toRemove.add(firstFullName);
            fullNames.forEach(x -> {
                        list.add(String.format("%s %s", surname, x));
                        toRemove.add(x);
                    }
            );
        }
        for (String str : toRemove) {
            if (!str.equals(" ")) {
                text = text.replaceAll(str, "");
            }
        }
        return new Pair<>(text, list);
    }

    public static List<Person> pairStrings(String[] streamStr) {
        List<Person> people = StreamEx.of(streamStr)
                .sequential()
                .collect(PersonCollector.collector());
        return people;
    }

    public static List<String> getFullNames(String text) {
        text = getPlainTextWithouRegexCharacters(text);
        List<String> result = new ArrayList<>();
        var regex = "[A-ZĄĆĘŁŚÓŹŻ][a-ząęćżźłó]{0,19}[ ][A-ZĄĆĘŁŚÓŹŻ][a-ząęćżźłó]{0,19}";
        var tuple = getFamillyNames(text);
        text = tuple.getValue0();
        result.addAll(tuple.getValue1());
        String[] split = text.split(regex);
        for (var s : split) {
            if (Strings.isNotBlank(s)) {
                text = text.replace(s, "\n");
            }
        }
        List<String> list = Arrays.asList(text.split("\n"));
        for (var s : list) {
            if (Strings.isNotBlank(s)) {
                result.add(s);
            }
        }
        return result;
    }

    public static String getPlainTextWithouRegexCharacters(String text) {
        return text.replaceAll("[”„]", "")
                .replaceAll("[\\(\\)\\.\\{\\}\\[\\]]", "");
    }
}
