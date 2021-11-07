package com.example.myapplication.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;

public class PersonCollector2 {
    private final List<Person> list = new ArrayList<>();
    private static String surname;

    public void accept(String str) {
        if (str.contains(" ")) {
            String[] s = str.split(" ");
            surname = s[0];
            list.add(new Person(surname, s[1]));
        } else if (surname != null) {
            list.add(new Person(surname, str));
        }
    }

    public List<Person> finish() {
        return list;
    }

    public static Collector<String, ?, List<Person>> collector() {
        return Collector.of(PersonCollector2::new, PersonCollector2::accept, (a, b) -> a, PersonCollector2::finish);
    }
}
