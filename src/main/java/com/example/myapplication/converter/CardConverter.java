package com.example.myapplication.converter;

import com.example.myapplication.model.PersonCollector2;
import com.example.myapplication.model.Person;
import com.example.myapplication.model.PersonCollector;
import one.util.streamex.StreamEx;
import org.apache.logging.log4j.util.Strings;
import org.javatuples.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CardConverter {

    public static List<String> getFullNames2(String text) {
        text = cleanTextFromUnnecessaryParts(text);
        List<String> names = filterNames(text);
        return names;
    }

    public static List<String> filterNames(String text) {
        List<String> list = List.of(text.split("\n"));
        return list.stream().sequential().collect(PersonCollector2.collector())
                .stream().map(p->p.toString()).collect(Collectors.toList());
    }

    public static String cleanTextFromUnnecessaryParts(String text) {
        return text.replaceAll(" [0-9a-zżźóąęćśł,\\.\\(\\)\\{\\}\\[\\]]{1,30}", " _")
                .replaceAll("( _){1,99} ", "\n");
    }


    public static List<String> getFullNames(String text) {
        text = getPlainTextWithouRegexCharacters(text);
        List<String> result = new ArrayList<>();
        var regex = "[A-ZĄĆĘŁŚÓŹŻ][a-ząęćżźłó]{0,19}[ ][A-ZĄĆĘŁŚÓŹŻ][a-ząęćżźłó]{0,19}";
        var tuple = getFamillyNames(text);
        text = tuple.getValue0();
        if (tupleIsNotEmpty(tuple)) {
            result.addAll(tuple.getValue1());
        }
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

    private static boolean tupleIsNotEmpty(Pair<String, ArrayList<String>> tuple) {
        return tuple != null && tuple.getValue1() != null && tuple.getValue1().size() > 0 && Strings.isNotBlank(tuple.getValue1().get(0));
    }

    public static String getPlainTextWithouRegexCharacters(String text) {
        return text.replaceAll("[”„]", "")
                .replaceAll("[\\(\\)\\.\\{\\}\\[\\]]", "");
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

}
