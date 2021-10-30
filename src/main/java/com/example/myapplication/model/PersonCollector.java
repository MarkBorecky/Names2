package com.example.myapplication.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;

public class PersonCollector {
    private final List<Person> list = new ArrayList<>();
    private static String surname;

    public void accept(String str) {
        if (surname == null) {
            surname = str;
        } else {
            list.add(new Person(surname, str));
            surname = null;
        }
    }

    public List<Person> finish() {
        return list;
    }

    public static Collector<String, ?, List<Person>> collector() {
        return Collector.of(PersonCollector::new, PersonCollector::accept, (a, b) -> a, PersonCollector::finish);
    }
}

