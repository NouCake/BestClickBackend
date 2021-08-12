package org.unitedinternet.kevfischer.BestClick.controller.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RandomGeneratorService {

    private final RestTemplate template;


    public RandomGeneratorService(RestTemplateBuilder builder) {
        this.template = builder.build();
    }

    public ResponseEntity<String[]> getRandomUserName(int amount){
        if(amount > 5000) throw new IllegalArgumentException("Max 5000 names per request");
        String url = "https://randomuser.me/api?nat=AU,BR,CA,CH,DE,DK,ES,FI,FR,GB,IE,NO,NL,NZ,TR,US&inc=name&results="+amount;
        try {
            JsonNode results = template.getForObject(url, ObjectNode.class).get("results");

            String[] randomNames = new String[results.size()];
            for(int i = 0; i < results.size(); i++) {
                JsonNode name = results.get(i).get("name");
                randomNames[i] = name.get("first").asText() + " " + name.get("last").asText();
            }
            return new ResponseEntity<>(randomNames, HttpStatus.OK);
        } catch (NullPointerException e) {
            return new ResponseEntity<>(new String[0], HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
