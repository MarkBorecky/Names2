package com.example.myapplication.model;

public record Person(String lastName, String firstName) {
    @Override
    public String toString() {
        return String.format("%s %s", lastName, firstName);
    }
}
