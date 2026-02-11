package com.myapp;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import java.io.*;
import java.time.LocalDate;
import java.nio.file.Files;
import java.nio.file.Paths;

public class SymptomHandling {
   @FXML private TextField inputSymptomField;
   @FXML private ListView<Symptom> symptomListView;

   private ObservableList<Symptom> symptomInfo = FXCollections.observableArrayList();

   private static final String TEMP = "symptom_temp.txt";
   private static final String STORE = "secure_symptom_store.dat";

   @FXML
    public void initialize() {
       symptomListView.setItems(symptomInfo);
   }

   @FXML
    protected void handleNewSymptom() {
       String text = inputSymptomField.getText();
       if (text!= null && !text.trim().isEmpty()) {
           try {
               writeToTempFile(text);
               String dataFromFile = readFromTempFile();
               wipeTempFile();
               Symptom createSymptom = new Symptom(dataFromFile, LocalDate.now());
               symptomInfo.add(0, createSymptom);
               safelyStore(createSymptom);
               inputSymptomField.setText("");
           } catch(IOException e) {
               e.printStackTrace();
           }
       }
   }

   private void writeToTempFile(String text) throws IOException {
       FileWriter writer = new FileWriter(TEMP);
       writer.write(text);
       writer.close();
   }

   private String readFromTempFile() throws IOException {
       String content = new String(Files.readAllBytes(Paths.get(TEMP)));
       return content.trim();
   }

   private void wipeTempFile() throws IOException {
       FileWriter writer = new FileWriter(TEMP);
       writer.write("");
       writer.close();
       Files.deleteIfExists(Paths.get(TEMP));
   }

   private void safelyStore(Symptom symptom) {
       try (FileWriter fw = new FileWriter(STORE, true);
            BufferedWriter bw =  new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw)) {
           out.println(symptom.getDate() + " | " + symptom.getDesc());
       } catch (IOException e) {
           System.err.println("Failed to store data safely");

       }

   }
}
