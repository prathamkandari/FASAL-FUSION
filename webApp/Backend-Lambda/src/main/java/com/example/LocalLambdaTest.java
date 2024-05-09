package com.example;
import java.util.HashMap;
import java.util.Map;

public class LocalLambdaTest {

    public static void main(String[] args) {
        // Create a sample input map
        Map<String, Double> input = new HashMap<>();
        input.put("N", 100.0);
        input.put("P", 50.0);
        input.put("K", 30.0);
        input.put("temp", 25.0);
        input.put("humidity", 70.0);
        input.put("ph", 6.5);
        input.put("rain", 50.0);

        // Create an instance of your Lambda function class
        MyLambdaFunction lambdaFunction = new MyLambdaFunction();

        // Simulate the Lambda execution
        Map<String, String> result = lambdaFunction.handleRequest(input, null);

        // Print the result
        System.out.println("Result: " + result);
    }
}
