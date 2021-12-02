package com.example.myapplication;

import com.example.myapplication.model.Card;
import com.example.myapplication.model.Name;
import com.example.myapplication.model.Occurrence;
import com.example.myapplication.reader.ODSReader;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@EnableAutoConfiguration
public class MyApplication implements CommandLineRunner {

//    private static String SOURCE = "./src/main/resources/wilno fiszki całość.ods";
    private static String SOURCE = "./src/main/resources/Materiał Seminarium Duchowne DR.ods";
    private static String SOURCE2 = "./src/main/resources/patronimika.ods";
    private final static List<Card> cards = new ArrayList<>();
    private final static List<Name> names = new ArrayList<>();

    @RequestMapping("/cards")
    List<Card> getAllCards() {
        return cards;
    }

    @RequestMapping("/names")
    List<Name> getAllNames() {
        return names;
    }

    @RequestMapping("/map-names")
    List<Occurrence> getMapNames() {
        return names.stream()
                .collect(Collectors.groupingBy(Name::name, Collectors.counting()))
                .entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(entry -> new Occurrence(entry.getKey(), entry.getValue()))
                .toList();
    }

    @RequestMapping("/unique")
    List<String> getUniqueNames() {
        return names.stream().map(Name::name).distinct().sorted().toList();
    }

    @RequestMapping("/not-empty-cards")
    List<Card> getNotEmptyCards() {
        return cards.stream().filter(card -> card.fullName().size() > 0).toList();
    }

    public static void main(String... args) {
        SpringApplication.run(MyApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
//        cards.addAll(ODSReader.readCards(SOURCE));
        names.addAll(ODSReader.readNames(SOURCE, 1, 12));
        names.addAll(ODSReader.readNames(SOURCE2, 0, 1));
    }
}

