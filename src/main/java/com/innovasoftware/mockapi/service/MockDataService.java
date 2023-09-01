package com.innovasoftware.mockapi.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.innovasoftware.mockapi.service.dto.MockDataDTO;
import com.innovasoftware.mockapi.service.dto.ResourceDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.github.javafaker.Faker;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Service
public class MockDataService {


    Faker faker = new Faker();


    private final Logger log = LoggerFactory.getLogger(MockService.class);


    public void generateMockData(MockDataDTO mockData){
        log.debug("Request to generate mock data");

        mockData.getResourceSchema().forEach(resourceSchema -> {
            log.info("Resource schema: {}", resourceSchema);

            if(resourceSchema.getType().equals("Faker.js")){
                String[] splitMethod = resourceSchema.getFakerMethod().split("\\.");
                if(splitMethod.length > 1){
//                    Object subFaker = getSubFaker(faker, splitMethod[0]);
//                    if(subFaker != null){
//                        Object randomData= getFakeData(subFaker, splitMethod[1]);
//                        log.info("Random data: name:{}  data:{}", resourceSchema.getName() ,randomData);
//                    }
                    String randomData = generateData(faker, splitMethod[0], splitMethod[1]);
                    log.info("Random data name:{}  data:{}", resourceSchema.getName() ,randomData);
                }
            }
        });


//        // Iterate over your resource schema
//        for (Map.Entry<String, String> entry : resourceSchema.entrySet()) {
//            String type = entry.getKey();
//            String methodName = entry.getValue();
//
//            String[] splitMethod = methodName.split("\\.");
//            if(splitMethod.length > 1){
//                Object subFaker = getSubFaker(faker, splitMethod[0]);
//                if(subFaker != null){
//                    Object randomData= getFakeData(subFaker, splitMethod[1]);
//                    System.out.println(type + ": " + randomData);
//                }
//            }
//        }


    }


    public String generateMockData(ResourceDTO resource){
        log.debug("Request to generate mock data");

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonArray jsonArray = new JsonArray();



        for(int i = 0; i < 10; i++) {
            JsonObject jsonObject = new JsonObject();
            resource.getResourceSchemas().forEach(resourceSchema -> {
                log.info("Resource schema: {}", resourceSchema);
                if (resourceSchema.getType().equals("Faker.js")) {
                    String[] splitMethod = resourceSchema.getFakerMethod().split("\\.");
                    if (splitMethod.length > 1) {
//                        Faker fkr = new Faker();
                        String randomData = generateData(faker, splitMethod[0], splitMethod[1]);
                        log.info("Random data name:{}  data:{}", resourceSchema.getName(), randomData);
                        jsonObject.addProperty(resourceSchema.getName(), randomData);
                    }
                }
            });
            jsonArray.add(jsonObject);
        }

//        JsonObject result = new JsonObject();
//        result.add("items", jsonArray);


        return gson.toJson(jsonArray);

    }



    private static Object getSubFaker(Faker faker, String fieldName) {
        try {
            return faker.getClass().getDeclaredField(fieldName).get(faker);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new IllegalStateException("No such field: " + fieldName,e);
        }
    }

    private static Object getFakeData(Object subFaker, String methodName) {
        try {
            Method method = subFaker.getClass().getMethod(methodName);
            return method.invoke(subFaker);
        } catch (Exception e) {
            throw new IllegalStateException("Could not invoke method: " + methodName,e);
        }
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
