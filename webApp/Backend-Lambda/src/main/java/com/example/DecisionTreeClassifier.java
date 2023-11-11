package com.example;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;

class DecisionTreeClassifier implements Serializable {
    Node root;
    int minSamplesSplit;
    int maxDepth;

    DecisionTreeClassifier(int minSamplesSplit, int maxDepth) {
        this.minSamplesSplit = minSamplesSplit;
        this.maxDepth = maxDepth;
    }

    

    // Serialization method to save the model
    public void saveModel(String filePath) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(this); // Write the current object to the file
            System.out.println("Model saved successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Deserialization method to load the model
    public static DecisionTreeClassifier loadModel(String filePath) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            DecisionTreeClassifier model = (DecisionTreeClassifier) ois.readObject();
            System.out.println("Model loaded successfully.");
            return model;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    Node buildTree(String[][] dataset, int currDepth) {
        int numSamples = dataset.length;
        int numFeatures = dataset[0].length - 1;

        if (numSamples >= minSamplesSplit && currDepth <= maxDepth) {
            SplitResult bestSplit = getBestSplit(dataset, numSamples, numFeatures);
            if (bestSplit.infoGain > 0) {
                Node leftSubtree = buildTree(bestSplit.datasetLeft, currDepth + 1);
                Node rightSubtree = buildTree(bestSplit.datasetRight, currDepth + 1);
                return new Node(bestSplit.featureIndex, bestSplit.threshold, leftSubtree, rightSubtree,
                        bestSplit.infoGain, null);
            }
        }

        String leafValue = calculateLeafValue(dataset);
        return new Node(-1, -1, null, null, -1, leafValue);
    }

    SplitResult getBestSplit(String[][] dataset, int numSamples, int numFeatures) {
        SplitResult bestSplit = new SplitResult();
        double maxInfoGain = Double.NEGATIVE_INFINITY;

        for (int featureIndex = 0; featureIndex < numFeatures; featureIndex++) {
            String[] featureValues = new String[numSamples];
            for (int i = 0; i < numSamples; i++) {
                featureValues[i] = dataset[i][featureIndex];
            }
            String[] possibleThresholds = Arrays.stream(featureValues).distinct().toArray(String[]::new);

            for (String threshold : possibleThresholds) {
                SplitResult splitResult = split(dataset, featureIndex, threshold);
                if (splitResult.datasetLeft.length > 0 && splitResult.datasetRight.length > 0) {
                    double infoGain = informationGain(dataset, splitResult.datasetLeft, splitResult.datasetRight,
                            "gini");
                    if (infoGain > maxInfoGain) {
                        bestSplit.featureIndex = featureIndex;
                        bestSplit.threshold = Double.parseDouble(threshold);
                        bestSplit.datasetLeft = splitResult.datasetLeft;
                        bestSplit.datasetRight = splitResult.datasetRight;
                        bestSplit.infoGain = infoGain;
                        maxInfoGain = infoGain;
                    }
                }
                
            }
        }

        return bestSplit;
    }

    SplitResult split(String[][] dataset, int featureIndex, String threshold) {
        SplitResult splitResult = new SplitResult();
        int numSamples = dataset.length;

        splitResult.datasetLeft = new String[numSamples][dataset[0].length];
        splitResult.datasetRight = new String[numSamples][dataset[0].length];

        int leftCount = 0;
        int rightCount = 0;

        for (int i = 0; i < numSamples; i++) {
            double featureValue = Double.parseDouble(dataset[i][featureIndex]);
            if (featureValue <= Double.parseDouble(threshold)) {
                splitResult.datasetLeft[leftCount] = Arrays.copyOf(dataset[i], dataset[i].length);
                leftCount++;
            } else {
                splitResult.datasetRight[rightCount] = Arrays.copyOf(dataset[i], dataset[i].length);
                rightCount++;
            }
        }

        splitResult.datasetLeft = Arrays.copyOfRange(splitResult.datasetLeft, 0, leftCount);
        splitResult.datasetRight = Arrays.copyOfRange(splitResult.datasetRight, 0, rightCount);

        return splitResult;
    }

    double informationGain(String[][] parent, String[][] leftChild, String[][] rightChild, String mode) {
        double weightLeft = (double) leftChild.length / parent.length;
        double weightRight = (double) rightChild.length / parent.length;
        double gain;

        if (mode.equals("gini")) {
            gain = giniIndex(parent) - (weightLeft * giniIndex(leftChild) + weightRight * giniIndex(rightChild));
        } else {
            gain = entropy(parent) - (weightLeft * entropy(leftChild) + weightRight * entropy(rightChild));
        }

        return gain;
    }

    double entropy(String[][] data) {
        int numSamples = data.length;
        String[] classLabels = Arrays.stream(data).map(row -> row[data[0].length - 1]).distinct()
                .toArray(String[]::new);
        double entropy = 0;

        for (String cls : classLabels) {
            int count = (int) Arrays.stream(data).filter(row -> row[data[0].length - 1].equals(cls)).count();
            double pCls = (double) count / numSamples;
            entropy -= pCls * Math.log(pCls) / Math.log(2);
        }

        return entropy;
    }

    double giniIndex(String[][] data) {
        int numSamples = data.length;
        String[] classLabels = Arrays.stream(data).map(row -> row[data[0].length - 1]).distinct()
                .toArray(String[]::new);
        double gini = 0;

        for (String cls : classLabels) {
            int count = (int) Arrays.stream(data).filter(row -> row[data[0].length - 1].equals(cls)).count();
            double pCls = (double) count / numSamples;
            gini += Math.pow(pCls, 2);
        }

        return 1 - gini;
    }

    String calculateLeafValue(String[][] data) {
        String[] labels = Arrays.stream(data).map(row -> row[data[0].length - 1]).toArray(String[]::new);
        String maxValue = "";
        int maxCount = 0;

        for (String label : labels) {
            int count = (int) Arrays.stream(labels).filter(l -> l.equals(label)).count();
            if (count > maxCount) {
                maxCount = count;
                maxValue = label;
            }
        }

        return maxValue;
    }

    String predictCrop(double nitrogen, double phosphorus, double potassium, double temp, double humidity, double ph, double rain) {
        String[] inputRecord = { Double.toString(nitrogen), Double.toString(phosphorus), Double.toString(potassium) ,
            Double.toString(temp),
            Double.toString(humidity),
            Double.toString(ph),
            Double.toString(rain)

        };
        return makePrediction(inputRecord, root);
    }

    String makePrediction(String[] x, Node tree) {
        if (tree.value != null) {
            return tree.value; // Return the predicted crop label if it's a leaf node
        }

        if (tree.featureIndex == -1) {
            // Handle leaf node or cases when the feature index is -1
            return tree.value;
        }

        // Check if the feature index is within the bounds of the input array
        if (tree.featureIndex < x.length) {
            double featureValue = Double.parseDouble(x[tree.featureIndex]);
            if (featureValue <= tree.threshold) {
                return makePrediction(x, tree.left); // Traverse left
            } else {
                return makePrediction(x, tree.right); // Traverse right
            }
        } else {
            return "Unknown"; // Handle the case when the feature index is out of bounds
        }
    }
}
