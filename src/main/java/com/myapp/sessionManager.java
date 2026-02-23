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
        boolean has_PIN = validatePIN(null,true);

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode root = mapper.createObjectNode();
        root.put("email", email);
        root.put("has_PIN",has_PIN);
        mapper.writeValue(new File("loggedIn.json"),root);
    }

    public static String[] getSession() throws IOException{
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(new File("loggedIn.json"));
        try {
            String email = root.get("email").asText();
            boolean has_PIN = root.get("has_PIN").asBoolean();

            return new String[]{email, String.valueOf(has_PIN)};
        } catch (Exception e) {
            return null;
        }
    }
}
