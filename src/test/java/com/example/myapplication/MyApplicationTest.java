package com.example.myapplication;

import com.example.myapplication.model.Card;
import com.example.myapplication.reader.ODSReader;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MyApplicationTest {

    private static String SOURCE = "./src/test/resources/test1.ods";

    @Test
    public void runTest() throws IOException {
        List<Card> cards = ODSReader.read(SOURCE);
        System.out.println("cards = " + cards);
        var ecpected = List.of(new Card("Odmowa przyłączenia maj. rząd. Krasne z par. przebrodzkiej do pohorskiej", Collections.EMPTY_LIST));
        assertEquals(ecpected, cards);
    }

}