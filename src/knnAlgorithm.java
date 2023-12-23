import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.*;

public class knnAlgorithm implements Serializable {

    private static final int NUM_FEATURES = 7; // Number of features in the dataset
    public static knnAlgorithm obj = initalize();
    private String[][] trainingData; // Training dataset
    private int k; // Number of neighbors to consider

    public knnAlgorithm(String[][] trainingData, int k) {
        this.trainingData = trainingData;
        this.k = k;
    }

    public String predict(String[] testData) {
        List<DistanceLabelPair> distances = calculateDistances(testData);

        List<DistanceLabelPair> kNearestNeighbors = findKNearestNeighbors(distances);

        String predictedLabel = majorityVote(kNearestNeighbors);

        return predictedLabel;
    }

    private List<DistanceLabelPair> calculateDistances(String[] testData) {
        List<DistanceLabelPair> distances = new ArrayList<>();

        for (String[] trainingInstance : trainingData) {
            double distance = calculateEuclideanDistance(testData, trainingInstance);
            distances.add(new DistanceLabelPair(distance, trainingInstance[NUM_FEATURES]));
        }

        return distances;
    }

    private double calculateEuclideanDistance(String[] point1, String[] point2) {
        double sum = 0.0;
        for (int i = 0; i < NUM_FEATURES; i++) {
            double diff = Double.parseDouble(point1[i]) - Double.parseDouble(point2[i]);
            sum += Math.pow(diff, 2);
        }
        return Math.sqrt(sum);
    }

    private List<DistanceLabelPair> findKNearestNeighbors(List<DistanceLabelPair> distances) {
        distances.sort(Comparator.comparingDouble(DistanceLabelPair::getDistance));
        return distances.subList(0, k);
    }

    private String majorityVote(List<DistanceLabelPair> neighbors) {
        Map<String, Integer> labelCount = new HashMap<>();

        for (DistanceLabelPair neighbor : neighbors) {
            String label = neighbor.getLabel();
            labelCount.put(label, labelCount.getOrDefault(label, 0) + 1);
        }

        int maxCount = 0;
        String predictedLabel = "";

        for (Map.Entry<String, Integer> entry : labelCount.entrySet()) {
            if (entry.getValue() > maxCount) {
                maxCount = entry.getValue();
                predictedLabel = entry.getKey();
            }
        }

        return predictedLabel;
    }

    public static void main(String[] args) {

        System.out.println("Training the model");
        predicting();
        System.out.println("Training complete");

        // // Test data
        // double n = 71;
        // double p = 54;
        // double kValue = 35;
        // double temperature = 26.63952463;
        // double humidity = 70.95705996;
        // double rainfall = 7.311077075;
        // double pH = 199.3355744;
        // String result = obj.middleware(n, p, kValue, temperature, humidity, rainfall,
        // pH);//Jute
        // System.out.println(result);

    }

    public static void predicting() {
        knnAlgorithm knnAlgorithm = initalize();
        obj = knnAlgorithm;
        obj.saveModel("C:\\Users\\Manav Khandurie\\Downloads\\fasalfusion\\models\\knnmodel.ser");

    }

    public static String middleware(double nitrogen, double phosphorus, double potassium, double temp, double humidity,
            double ph, double rain) {
        System.out.println(
                nitrogen + "," + phosphorus + "," + potassium + "," + temp + "," + humidity + "," + ph + "," + rain);
        String recommendedCrop = obj.predictCrop(nitrogen, phosphorus, potassium, temp, humidity, ph, rain);
        System.out.println("Recommended Crop: " + recommendedCrop);
        return recommendedCrop;
    }

    public String predictCrop(double nitrogen, double phosphorus, double potassium, double temp, double humidity,
            double ph,
            double rain) {
        String[] inputRecord = {
                Double.toString(nitrogen),
                Double.toString(phosphorus),
                Double.toString(potassium),
                Double.toString(temp),
                Double.toString(humidity),
                Double.toString(ph),
                Double.toString(rain)
        };
        // System.out.println(obj);
        return obj.predict(inputRecord);
    }

    public static knnAlgorithm initalize() {
        String csvFile = "C:\\Users\\Manav Khandurie\\Downloads\\fasalfusion\\data\\Train.csv";
        String line;
        String csvSplitBy = ",";
        String[][] trainingData = new String[0][];

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            br.readLine(); // Skip the header row
            while ((line = br.readLine()) != null) {
                String[] values = line.split(csvSplitBy);
                trainingData = Arrays.copyOf(trainingData, trainingData.length + 1);
                trainingData[trainingData.length - 1] = values;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        int k = 10;
        knnAlgorithm knnAlgorithm = new knnAlgorithm(trainingData, k);
        return knnAlgorithm;
    }

    public void saveModel(String filePath) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(this);
            System.out.println("Model saved successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Deserialization method to load the model
    public static knnAlgorithm loadModel(String filePath) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            knnAlgorithm model = (knnAlgorithm) ois.readObject();
            System.out.println("Model loaded successfully.");
            return model;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}

class DistanceLabelPair {
    private double distance;
    private String label;

    public DistanceLabelPair(double distance, String label) {
        this.distance = distance;
        this.label = label;
    }

    public double getDistance() {
        return distance;
    }

    public String getLabel() {
        return label;
    }
}
