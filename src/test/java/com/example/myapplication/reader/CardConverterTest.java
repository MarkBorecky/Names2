package com.example.myapplication.reader;

import org.junit.jupiter.api.Test;

import java.util.List;

import static com.example.myapplication.converter.CardConverter.getNamesList;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CardConverterTest {

    @Test
    public void getFullNamesTest1() {
        var text = "Joe Doe Początek zdania, długo Aaa Aaa długo nic, aż tu nagle Bbb Bbb, Ccc, Ddd w samym środku tekstu.";
        var result = getNamesList(text);
        assertEquals(List.of("Joe Doe", "Aaa Aaa", "Bbb Bbb", "Bbb Ccc", "Bbb Ddd"), result);
    }

    @Test
    public void getFullNamesTest2() {
        var text = "Dfg dfg dgf dgddg Poźniakowowie Filip, Stefania, Melecjusz df fdfdf ddffd Ddf dfgdf fdgd";
        var list = getNamesList(text);
        System.out.println("list = " + list);
        assertEquals(List.of("Poźniakowowie Filip", "Poźniakowowie Stefania", "Poźniakowowie Melecjusz"), list);
    }

    @Test
    public void getFullNamesTest3() {
        var text = "„Pignilewski” Jan uczeń żyrowickich szkół duchownych udzielenie miejsca ponomara – zwolnienie za niespełnianie obowiązków – udzielenie miejsca djaczka";
        var list = getNamesList(text);
        System.out.println("list = " + list);
        assertEquals(List.of("Pignilewski Jan"), list);
    }
}
