package com.example;


import java.io.*;
import java.util.Arrays;
public class createModel implements Serializable {   
    static DecisionTreeClassifier classifier;

    // Initialize and train the model
    public static void initalize() {
        String csvFile = "D:\\PROJS\\ss\\test\\lambdatest\\src\\main\\resources\\training_data.csv";
        String line;
        String csvSplitBy = ",";
        //String[] colNames = { "N", "P", "K", "temperature", "humidity", "ph", "rainfall", "label" };
        String[][] data = new String[0][];

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            br.readLine(); // Skip the header row
            while ((line = br.readLine()) != null) {
                String[] values = line.split(csvSplitBy);
                data = Arrays.copyOf(data, data.length + 1);
                data[data.length - 1] = values;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        classifier = new DecisionTreeClassifier(3, 10);
        classifier.root = classifier.buildTree(data, 0);
    }

    // Middleware method to predict using the model
    public static String middleware(double nitrogen, double phosphorus, double potassium, double temp, double humidity, double ph, double rain){
        System.out.println(nitrogen+","+ phosphorus+","+ potassium+","+temp+","+humidity+","+ph+","+rain);
        String recommendedCrop = classifier.predictCrop(nitrogen, phosphorus, potassium,temp,humidity,ph,rain);
        System.out.println("Recommended Crop: " + recommendedCrop);
        return recommendedCrop;
    }

    public static void main(String[] args) {
        System.out.println("Trainning the model");
        initalize();
        System.out.println("Trainning Completed!!!!!!");
        // Save the trained model to a file
        classifier.saveModel("D:\\PROJS\\ss\\test\\lambdatest\\src\\main\\resources\\realmodel.ser");
    }
}