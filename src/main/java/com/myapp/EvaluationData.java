package com.myapp;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class EvaluationData {
    public static final Set<String> selectedAllergies = new HashSet<>();
    public static int riskScore = 0;

    public static void saveDataToFile() {
        String allergiesList = selectedAllergies.stream()
                .map(s -> "\"" + s + "\"")
                .collect(Collectors.joining(","));

        String content = "{\n" +
                " \"riskScore\": " + EvaluationData.riskScore + ",\n" +
                " \"recordedAllergies\": [" + allergiesList + "]\n" +
                "}";

        try (FileWriter file = new FileWriter("localdata/allergies.json", false)) {
            file.write(content);
            System.out.println("Successfully written to allergies json file");
        } catch (IOException e) {
            System.out.println("Error writing to allergies json file");
            e.printStackTrace();
        }
    }
}

