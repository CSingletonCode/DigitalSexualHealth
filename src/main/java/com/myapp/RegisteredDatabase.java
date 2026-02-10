package com.myapp;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class RegisteredDatabase {
    private static final String FILE_PATH = "localdata/registeredSymptoms.json";
    private ObjectMapper mapper = new ObjectMapper();
    private File registeredFile;

    public RegisteredDatabase() throws IOException {
        registeredFile = new File(FILE_PATH);
        if (!registeredFile.exists()) {
            registeredFile.createNewFile();
            mapper.writeValue(registeredFile, new ArrayList<>());
        }
    }

    public ArrayList<String> getRegistered() throws IOException {
        registeredFile = new File(FILE_PATH);
        if (!registeredFile.exists()) {
            return new ArrayList<>();
        } else {
            return mapper.readValue(registeredFile, new TypeReference<ArrayList<String>>(){});
        }
    }
}
