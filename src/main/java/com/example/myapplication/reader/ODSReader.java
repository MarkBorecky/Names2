package com.example.myapplication.reader;

import com.example.myapplication.converter.CardConverter;
import com.example.myapplication.model.Card;
import com.example.myapplication.model.Name;
import com.example.myapplication.utils.Utils;
import com.github.miachm.sods.SpreadSheet;
import org.apache.logging.log4j.util.Strings;

import java.io.*;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ODSReader {
    public static final String MINUS_ONE = "-1";
    public static final String NONE = "NONE";
    public static final List<String> FORBIDDEN_STRINGS = List.of("-", "- ");
    public static final Predicate<Name> FILTER_NAMES = name -> !FORBIDDEN_STRINGS.contains(name.name());
    private static final Map<String, String> nameMap = Utils.populateNameMap();

    public static List<Card> readCards(String s) throws IOException {
        File initialFile = new File(s);
        InputStream is = new FileInputStream(initialFile);
        return readCards(is);
    }

    public static Collection<Name> readNames(String s, int nameColumn, int yearColumn) throws IOException {
        File initialFile = new File(s);
        InputStream is = new FileInputStream(initialFile);
        return readNames(is, nameColumn, yearColumn);
    }

    public static List<Name> readNames(InputStream is, int nameColumn, int yearColumn) throws IOException {
        var spread = new SpreadSheet(is);
        var result = new ArrayList<Name>();
        for (var sheet : spread.getSheets()) {
            var range = sheet.getDataRange();
            var values = range.getValues();
            result.addAll(getNameList(values, nameColumn, yearColumn));
        }
        return result;
    }

    private static Collection<Name> getNameList(Object[][] values, int nameColumn, int yearColumn) {
        return Arrays.stream(values)
                .skip(1)
                .map(v -> getName(nameColumn, yearColumn, v))
                .filter(FILTER_NAMES)
                .toList();
    }

    private static Name getName(int nameColumn, int yearColumn, Object[] v) {
        var originalName = getColumn(v, nameColumn);
        var translatedName = translate(originalName.trim().replaceAll("[.*]",""));
        var year = Utils.parseInt(yearColumn, v);
        return new Name(translatedName, year);
    }

    private static String translate(String originalName) {
        return nameMap.getOrDefault(originalName, originalName);
    }

    public static List<Card> readCards(InputStream is) throws IOException {
        var spread = new SpreadSheet(is);
        var result = new ArrayList<Card>();
        for (var sheet : spread.getSheets()) {
            var range = sheet.getDataRange();
            var values = range.getValues();
            result.addAll(getCardList(values));
        }
        return result;
    }

    public static List<Card> getCardList(Object[][] values) {
        return Arrays.stream(values)
                .skip(1)
                .map(objects -> getColumn(objects, 5))
                .filter(Strings::isNotBlank)
                .map(CardConverter::getCards)
                .collect(Collectors.toList());
    }

    public static String getColumn(Object[] objects, int columnNumber) {
        return Optional.ofNullable(objects[columnNumber]).map(Object::toString)
                .orElse(columnNumber == 12 ? MINUS_ONE : (stjepanCase(objects[0], columnNumber) ? MINUS_ONE : NONE));
    }

    private static boolean stjepanCase(Object object, int columnNumber) {
        return columnNumber == 1 && object != null && object.equals("Стефан*");
    }
}
