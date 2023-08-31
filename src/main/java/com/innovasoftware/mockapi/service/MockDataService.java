package com.innovasoftware.mockapi.service;

import com.innovasoftware.mockapi.service.dto.MockDataDTO;
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
