package com.example;

import java.io.*;
public class Node implements Serializable {
    int featureIndex;
    double threshold;
    Node left;
    Node right;
    double infoGain;
    String value;

    Node(int featureIndex, double threshold, Node left, Node right, double infoGain, String value) {
        this.featureIndex = featureIndex;
        this.threshold = threshold;
        this.left = left;
        this.right = right;
        this.infoGain = infoGain;
        this.value = value;
    }
}

