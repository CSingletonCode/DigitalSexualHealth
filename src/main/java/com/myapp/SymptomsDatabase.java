package com.myapp;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class SymptomsDatabase {
    private static final String FILE_PATH = "localdata/enteredSymptoms.json";
    private ObjectMapper mapper = new ObjectMapper();
    private File trackerFile;

    public SymptomsDatabase() throws IOException {
        trackerFile = new File(FILE_PATH);
        if (!trackerFile.exists()) {
            trackerFile.createNewFile();
            mapper.writeValue(trackerFile, new ArrayList<>());
        }
    }

    private ArrayList<SymptomEntry> getAllSymptoms() throws IOException {
        trackerFile = new File(FILE_PATH);
        if (!trackerFile.exists()) {
            return new ArrayList<>();
        } else{
            return mapper.readValue(trackerFile, new TypeReference<ArrayList<SymptomEntry>>(){});
        }
    }

    private void setAllSymptoms(ArrayList<SymptomEntry> symptoms) throws IOException {
        trackerFile = new File(FILE_PATH);
        if (!trackerFile.exists()) {
            trackerFile.createNewFile();
            mapper.writeValue(trackerFile, new ArrayList<>());
        } else {
            mapper.readValue(trackerFile, new TypeReference<ArrayList<SymptomEntry>>(){});
        }
    }

    public void recordSymptom(SymptomEntry symptom) {
        try {
            ArrayList<SymptomEntry> tracked = getAllSymptoms();
            tracked.add(symptom);
            setAllSymptoms(tracked);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void removeByID(String id) {
        try {
            ArrayList<SymptomEntry> tracked = getAllSymptoms();
            for (SymptomEntry symptom : tracked) {
                if (symptom.getId().equals(id)) {
                    tracked.remove(symptom);
                    break;
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
