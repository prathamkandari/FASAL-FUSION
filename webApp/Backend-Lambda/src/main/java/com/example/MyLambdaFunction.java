package com.example;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

public class MyLambdaFunction implements RequestHandler<Map<String, Double>, Map<String, String>> {
    
    static DecisionTreeClassifier model;
    @Override
    public Map<String, String> handleRequest(Map<String, Double> input, Context context) {
        try {
            // Extracting values from the input map
            double N = (input.get("N"));
            double P = (input.get("P"));
            double K = (input.get("K"));
            double temp = (input.get("temp"));
            double humidity = (input.get("humidity"));
            double ph = (input.get("ph"));
            double rain = (input.get("rain"));

            // Your processing logic goes here
            String c = "None"; // Default value
            System.out.println("Classpath: " + System.getProperty("java.class.path"));
            System.out.println("Loading model... Man ");
            //DecisionTreeClassifier model;
            try {
                File file = new File(getClass().getProtectionDomain().getCodeSource().getLocation().getPath() + "/realmodel.ser");
                System.out.println(file);
                InputStream modelStream = new FileInputStream(file);
                System.out.println(modelStream);
                ObjectInputStream ois = new ObjectInputStream(modelStream);
                System.out.println(ois);

                System.out.println("Class loader: " + getClass().getClassLoader());
                System.out.println("Model stream: " + modelStream);
                model = (DecisionTreeClassifier) ois.readObject();
                System.out.println(model);
                System.out.println("seems to work");
                System.out.println("Model loaded successfully!");
                c=model.predictCrop(N, P, K, temp, humidity, ph, rain);
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                System.err.println("Error loading model: " + e.getMessage());
            }
            Map<String, String> response = new HashMap<>();
            response.put("Crop", c);

            return response;
        } catch (Exception e) {
            // Handle any exceptions or errors
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("Error", e.getMessage());
            return errorResponse;
        }
    }
}
/*
(
                

 */