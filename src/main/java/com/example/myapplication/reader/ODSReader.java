package com.example.myapplication.reader;

import com.example.myapplication.converter.CardConverter;
import com.example.myapplication.model.Card;
import com.github.miachm.sods.SpreadSheet;
import org.apache.logging.log4j.util.Strings;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ODSReader {

    public static List<Card> read(String s) throws IOException {
        File initialFile = new File(s);
        InputStream is = new FileInputStream(initialFile);
        return read(is);
    }

    public static List<Card> read(InputStream is) throws IOException {
        var spread = new SpreadSheet(is);
        var result = new ArrayList<Card>();
        for (var sheet : spread.getSheets()) {
            var range = sheet.getDataRange();
            var values = range.getValues();
            result.addAll(getDataDaoList(values));
        }
        return result;
    }

    public static List<Card> getDataDaoList(Object[][] values) {
        return Arrays.stream(values)
                .skip(1)
                .map(ODSReader::getColumn5)
                .filter(Strings::isNotBlank)
                .map(CardConverter::getCards)
                .collect(Collectors.toList());
    }

    private static String getColumn5(Object[] objects) {
        return (String) objects[5];
    }
}
