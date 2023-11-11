package com.example;

import java.io.Serializable;

public class SplitResult implements Serializable {
    int featureIndex;
    double threshold;
    String[][] datasetLeft;
    String[][] datasetRight;
    double infoGain;

    SplitResult() {
        featureIndex = -1;
        threshold = -1;
        datasetLeft = null;
        datasetRight = null;
        infoGain = -1;
    }
}
