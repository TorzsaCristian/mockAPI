package com.innovasoftware.mockapi.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.innovasoftware.mockapi.service.dto.ResourceDTO;
import net.datafaker.Faker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Service
public class MockDataGeneratorService {


    Faker faker = new Faker();

    Gson gson = new GsonBuilder().setPrettyPrinting().create();


    private final Logger log = LoggerFactory.getLogger(MockService.class);

    public String generateMockData(ResourceDTO resource){
        log.debug("Request to generate mock data");

        JsonArray jsonArray = new JsonArray();

        for(int i = 0; i < resource.getCount(); i++) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("id", i+1);
            resource.getResourceSchemas().forEach(resourceSchema -> {
                if (resourceSchema.getType().equals("Faker.js")) {
                    String[] splitMethod = resourceSchema.getFakerMethod().split("\\.");
                    if (splitMethod.length > 1) {
                        String randomData = generateData(faker, splitMethod[0], splitMethod[1]);
                        jsonObject.addProperty(resourceSchema.getName(), randomData);
                    }
                }
            });
            jsonArray.add(jsonObject);
        }
        return gson.toJson(jsonArray);
    }

    public static String generateData(Faker faker, String category, String methodName) {
        try {
            // get category field from Faker class
            Object fieldObject = faker.getClass().getMethod(category).invoke(faker);

            // get method from fieldObjects class
            Method method = fieldObject.getClass().getMethod(methodName);

            // call method and return string
            return (String) method.invoke(fieldObject);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            throw new IllegalStateException("Could not invoke method: " + methodName,e);
        }
    }
}
