package com.myapp;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Symptom {
    private String desc;
    private LocalDate date;

    public Symptom(String desc, LocalDate date) {
        this.desc = desc;
        this.date = date;
    }

    public String getDesc() { return desc; }
    public LocalDate getDate() { return date; }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");
        return date.format(formatter) + " - " + desc;
    }
}