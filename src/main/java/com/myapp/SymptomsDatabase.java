package com.myapp;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SymptomsDatabase {
    private static final String FILE_PATH = "localdata/enteredSymptoms.json";
    private ObjectMapper mapper = new ObjectMapper();
    private File file;

    public SymptomsDatabase() {
        file = new File(FILE_PATH);
        if (!file.exists()) {
            try {
                file.getParentFile().mkdirs();
                mapper.writeValue(file, new ArrayList<SymptomEntry>());
            } catch (IOException e) {
                System.out.println("File missing.");
            }
        }
    }
}
