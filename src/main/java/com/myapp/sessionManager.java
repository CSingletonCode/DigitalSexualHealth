package com.myapp;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.File;
import java.io.IOException;

import static com.myapp.DatabaseManager.validatePIN;

public class sessionManager {
    public static void setSession() throws IOException{
        String email = userSession.getInstance().getEmail();
        int id = userSession.getInstance().getUserId();
        boolean has_PIN = validatePIN(null,true,null);

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode root = mapper.createObjectNode();
        root.put("email", email);
        root.put("has_PIN",has_PIN);
        root.put("id", id);
        mapper.writeValue(new File("localdata/loggedIn.json"),root);
    }

    public static String[] getSession() throws IOException{
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(new File("localdata/loggedIn.json"));
        try {
            String email = root.get("email").asText();
            boolean has_PIN = root.get("has_PIN").asBoolean();

            return new String[]{email, String.valueOf(has_PIN),String.valueOf(root.get("id"))};
        } catch (Exception e) {
            return null;
        }
    }

    public static String getEmail() throws IOException{
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(new File("localdata/loggedIn.json"));
        return root.get("email").asText();
    }

    public static String getId() throws IOException{
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(new File("localdata/loggedIn.json"));
        return String.valueOf(root.get("id").asInt());
    }

    public static void clearSession() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode root = mapper.createObjectNode();
        mapper.writeValue(new File("localdata/loggedIn.json"),root);
        System.out.println("clear session");
    }
}
