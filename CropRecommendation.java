import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

class Tree {
    int depth;
    Node[] nodeArray;
}

class Node {
    Node parent;
    int feature;
    int index;
    String threshold;
    ArrayList<Integer> leftIndices;
    ArrayList<Integer> rightIndices;
    Node leftChild;
    Node rightChild;
}

public class CropRecommendation {
    public static ArrayList<String[]> data;
    public static Map<Integer, String> thresholdMap;
    public static Tree tree;

    public static void main(String[] args) {
        data = new ArrayList<>();
        thresholdMap = new HashMap<>();

        // Read the CSV file and build the decision tree
        readCSVFile("C:\\Project\\FASAL-FUSION\\data\\Crop_recommendation.csv"); // Replace with your file path
        buildDecisionTree(3); // Specify the depth of the decision tree

        // Get input values for nitrogen, phosphorus, and potassium
        Scanner input = new Scanner(System.in);
        System.out.print("Enter Nitrogen value: ");
        double nitrogen = input.nextDouble();
        System.out.print("Enter Phosphorus value: ");
        double phosphorus = input.nextDouble();
        System.out.print("Enter Potassium value: ");
        double potassium = input.nextDouble();

        // Create a data record for prediction
        String[] inputRecord = { String.valueOf(nitrogen), String.valueOf(phosphorus), String.valueOf(potassium) };

        // Make a crop recommendation
        String recommendedCrop = makeCropRecommendation(inputRecord);
        System.out.println("Recommended Crop: " + recommendedCrop);
    }

    public static void readCSVFile(String csvFile) {
        String line = "";
        String csvSplitBy = ","; // CSV file delimiter
        boolean skipHeader = true; // Set this flag to true to skip the header

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            while ((line = br.readLine()) != null) {
                if (skipHeader) {
                    skipHeader = false;
                    continue; // Skip the header
                }

                // Split the line into fields using the delimiter
                String[] fields = line.split(csvSplitBy);

                // Process the fields as needed and store them in the 'data' ArrayList
                data.add(fields);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void buildDecisionTree(int depth) {
        int numFeatures = data.get(0).length - 1;
        tree = new Tree();
        tree.depth = depth;
        tree.nodeArray = new Node[(int) Math.pow(2, depth) - 1];

        Map<Integer, Boolean> featureStatus = new HashMap<>();
        Map<Integer, String> tMap = new HashMap<>();

        for (int i = 0; i < numFeatures; i++) {
            featureStatus.put(i, false);
            tMap.put(i, data.get(0)[i]);
        }
        thresholdMap = tMap;

        for (int i = 0; i < tree.nodeArray.length; i++) {
            if (i > 0) {
                int parentIndex = 0;
                ArrayList<String[]> dataSubset;
                if (i % 2 == 0) {
                    parentIndex = i / 2 - 1;
                    if (tree.nodeArray[parentIndex] != null) {
                        if (tree.nodeArray[parentIndex].threshold != null) {
                            dataSubset = getSubset(tree.nodeArray[parentIndex].rightIndices);
                        } else {
                            dataSubset = null;
                        }
                    } else {
                        dataSubset = null;
                    }
                } else {
                    parentIndex = (i - 1) / 2;
                    if (tree.nodeArray[parentIndex] != null) {
                        if (tree.nodeArray[parentIndex].threshold != null) {
                            dataSubset = getSubset(tree.nodeArray[parentIndex].leftIndices);
                        } else {
                            dataSubset = null;
                        }
                    } else {
                        dataSubset = null;
                    }
                }
                if (dataSubset != null && dataSubset.size() > 0) {
                    Map<Integer, Double> gainMap = new HashMap<>();
                    for (int f = 0; f < numFeatures; f++) {
                        if (!featureStatus.get(f)) {
                            gainMap.put(f, getInfoGain(f, dataSubset));
                        }
                    }
                    int thisFeature = getBestFeature(gainMap);
                    if (gainMap.get(thisFeature) != null && gainMap.get(thisFeature) != -1) {
                        tree.nodeArray[i] = new Node();
                        tree.nodeArray[i].parent = tree.nodeArray[parentIndex];
                        tree.nodeArray[i].feature = thisFeature;
                        tree.nodeArray[i].index = i;
                        featureStatus.put(thisFeature, true);
                        if (i % 2 != 0) {
                            tree.nodeArray[i].parent.leftChild = tree.nodeArray[i];
                        } else {
                            tree.nodeArray[i].parent.rightChild = tree.nodeArray[i];
                        }

                        String threshold = thresholdMap.get(thisFeature);
                        tree.nodeArray[i].leftIndices = new ArrayList<>();
                        tree.nodeArray[i].rightIndices = new ArrayList<>();
                        tree.nodeArray[i].threshold = threshold;
                        for (int index = 0; index < dataSubset.size(); index++) {
                            if (data.get(index)[thisFeature].equals(threshold)) {
                                tree.nodeArray[i].leftIndices.add(index);
                            } else {
                                tree.nodeArray[i].rightIndices.add(index);
                            }
                        }
                    }
                }
            } else {
                Map<Integer, Double> gainMap = new HashMap<>();
                for (int f = 0; f < numFeatures; f++) {
                    gainMap.put(f, getInfoGain(f, data));
                }
                int thisFeature = getBestFeature(gainMap);
                if (gainMap.get(thisFeature) != null && gainMap.get(thisFeature) != -1) {
                    tree.nodeArray[i] = new Node();
                    tree.nodeArray[i].parent = null;
                    tree.nodeArray[i].feature = thisFeature;
                    tree.nodeArray[i].index = i;
                    featureStatus.put(thisFeature, true);
                    String threshold = thresholdMap.get(thisFeature);
                    tree.nodeArray[i].leftIndices = new ArrayList<>();
                    tree.nodeArray[i].rightIndices = new ArrayList<>();
                    tree.nodeArray[i].threshold = threshold;
                    for (int index = 0; index < data.size(); index++) {
                        if (data.get(index)[thisFeature].equals(threshold)) {
                            tree.nodeArray[i].leftIndices.add(index);
                        } else {
                            tree.nodeArray[i].rightIndices.add(index);
                        }
                    }
                }
            }
        }
    }

    public static int getBestFeature(Map<Integer, Double> gainMap) {
        int bestFeature = -1;
        double maxValue = -1;
        for (int i : gainMap.keySet()) {
            if (gainMap.get(i) != -1) {
                if (gainMap.get(i) > maxValue) {
                    maxValue = gainMap.get(i);
                    bestFeature = i;
                }
            } else {
                maxValue = -1;
                bestFeature = i;
            }
        }
        return bestFeature;
    }

    public static double getInfoGain(int f, ArrayList<String[]> dataSubset) {
        double entropyBefore = getEntropy(dataSubset);
        if (entropyBefore != 0) {
            String threshold = thresholdMap.get(f);
            ArrayList<String[]> leftData = new ArrayList<>();
            ArrayList<String[]> rightData = new ArrayList<>();
            for (String[] d : dataSubset) {
                if (d[f].equals(threshold)) {
                    leftData.add(d);
                } else {
                    rightData.add(d);
                }
            }
            if (leftData.size() > 0 && rightData.size() > 0) {
                double leftProb = (double) leftData.size() / dataSubset.size();
                double rightProb = (double) rightData.size() / dataSubset.size();
                double entropyLeft = getEntropy(leftData);
                double entropyRight = getEntropy(rightData);
                double gain = entropyBefore - (leftProb * entropyLeft) - (rightProb * entropyRight);
                return gain;
            } else {
                return 0;
            }
        } else {
            return -1;
        }
    }

    public static double getEntropy(ArrayList<String[]> dataSubset) {
        double entropy = 0;
        if (dataSubset.size() > 0) {
            int y = dataSubset.get(0).length - 1;
            Map<String, Integer> freqMap = new HashMap<>();
            for (String[] d : dataSubset) {
                if (freqMap.containsKey(d[y])) {
                    freqMap.put(d[y], freqMap.get(d[y]) + 1);
                } else {
                    freqMap.put(d[y], 1);
                }
            }
            for (String s : freqMap.keySet()) {
                double prob = (double) freqMap.get(s) / dataSubset.size();
                entropy -= prob * Math.log(prob) / Math.log(2);
            }
        }
        return entropy;
    }

    public static ArrayList<String[]> getSubset(ArrayList<Integer> indices) {
        ArrayList<String[]> subSet = new ArrayList<>();
        for (Integer i : indices) {
            subSet.add(data.get(i));
        }
        return subSet;
    }

    public static String makeCropRecommendation(String[] inputRecord) {
        int i = 0;
        while (i < tree.nodeArray.length) {
            Node thisNode = tree.nodeArray[i];
            if (thisNode.threshold != null) {
                if (i >= inputRecord.length) {
                    System.out.println("Error: Insufficient input data.");
                    break;
                }
    
                if (inputRecord[thisNode.feature].equals(thisNode.threshold)) {
                    if (thisNode.leftChild != null) {
                        i = thisNode.leftChild.index;
                    } else {
                        return getPrediction(thisNode.leftIndices);
                    }
                } else {
                    if (thisNode.rightChild != null) {
                        i = thisNode.rightChild.index;
                    } else {
                        return getPrediction(thisNode.rightIndices);
                    }
                }
            } else {
                System.out.println("Nothing learnt from training data");
                break;
            }
        }
        return "Unknown";
    }

    public static String getPrediction(ArrayList<Integer> indices) {
        int y = data.get(indices.get(0)).length - 1;
        Map<String, Integer> freqMap = new HashMap<>();
        for (Integer i : indices) {
            if (freqMap.containsKey(data.get(i)[y])) {
                freqMap.put(data.get(i)[y], freqMap.get(data.get(i)[y]) + 1);
            } else {
                freqMap.put(data.get(i)[y], 1);
            }
        }
        String prediction = null;
        int maxValue = 0;
        for (String i : freqMap.keySet()) {
            if (freqMap.get(i) > maxValue) {
                maxValue = freqMap.get(i);
                prediction = i;
            }
        }
        return prediction;
    }
}
