package com.example.myapplication;

import com.example.myapplication.model.Card;
import com.example.myapplication.reader.ODSReader;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.ArrayList;
import java.util.List;

@RestController
@EnableAutoConfiguration
public class MyApplication implements CommandLineRunner {

    private static String SOURCE = "./src/main/resources/wilno fiszki całość.ods";
//    private static String SOURCE = "./src/test/resources/test1.ods";
    private final static List<Card> cards = new ArrayList<>();

    @RequestMapping("/cards")
    List<Card> getAllCards() {
        return cards;
    }

    public static void main(String... args) {
        SpringApplication.run(MyApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        cards.addAll(ODSReader.read(SOURCE));
    }
}

