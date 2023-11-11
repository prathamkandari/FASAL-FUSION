package src;

import java.io.*;
import java.util.*;

public class csvSplitter {

    public static void splitCSV(String inputFilePath, String outputFilePath1, String outputFilePath2, double ratio) {
        try {
            BufferedReader inputReader = new BufferedReader(new FileReader(inputFilePath));
            BufferedWriter outputWriter1 = new BufferedWriter(new FileWriter(outputFilePath1));
            BufferedWriter outputWriter2 = new BufferedWriter(new FileWriter(outputFilePath2));

            String header = inputReader.readLine(); // Assuming the first row is the header

            outputWriter1.write(header + "\n");
            outputWriter2.write(header + "\n");

            List<String> data = new ArrayList<>();
            String row;
            while ((row = inputReader.readLine()) != null) {
                data.add(row);
            }
            inputReader.close();

            Collections.shuffle(data);

            int splitIndex = (int) (data.size() * ratio);
            List<String> data1 = data.subList(0, splitIndex);
            List<String> data2 = data.subList(splitIndex, data.size());

            for (String rowData : data1) {
                outputWriter1.write(rowData + "\n");
            }
            outputWriter1.close();

            for (String rowData : data2) {
                outputWriter2.write(rowData + "\n");
            }
            outputWriter2.close();

            System.out.println(outputFilePath1 + " created with " + (ratio * 100) + "% of the data.");
            System.out.println(outputFilePath2 + " created with " + ((1 - ratio) * 100) + "% of the data.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String inputCSV = "C:\\Users\\Manav Khandurie\\Downloads\\FASAL-FUSION\\data\\Crop_recommendation.csv";
        String outputCSV1 = "C:\\Users\\Manav Khandurie\\Downloads\\FASAL-FUSION\\data\\training_data.csv";
        String outputCSV2 = "C:\\Users\\Manav Khandurie\\Downloads\\FASAL-FUSION\\data\\testing_data.csv";
        double ratio = 0.7;

        splitCSV(inputCSV, outputCSV1, outputCSV2, ratio);
    }
}
