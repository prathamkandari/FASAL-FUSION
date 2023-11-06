package src;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class reportGenration {
    static DecisionTreeClassifier loadedmodel=loadModel.getModel();
    public static double Accuracy(){
        double ans=0.0;
        int numerator=0,denominator=0;

        if(loadedmodel==null){
            System.out.println("No model trainned");
            return Double.MIN_VALUE;
        }

        String csvFile = "C:\\Users\\Manav Khandurie\\Downloads\\FASAL-FUSION\\data\\testing_data.csv";
        String line;
        String csvSplitBy = ",";
        String[][] data = new String[0][];

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            br.readLine(); 
            while ((line = br.readLine()) != null) {
                String[] values = line.split(csvSplitBy);
                String expected=values[values.length-1];
                String answer=loadedmodel.predictCrop(
                    Double.parseDouble(values[0]),
                    Double.parseDouble(values[1]),
                    Double.parseDouble(values[2]),
                    Double.parseDouble(values[3]),
                    Double.parseDouble(values[4]),
                    Double.parseDouble(values[5]),
                    Double.parseDouble(values[6])
                );
                if(answer.equalsIgnoreCase(expected))
                    numerator++;
                denominator++;
                data = Arrays.copyOf(data, data.length + 1);
                data[data.length - 1] = values;

            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        ans=((double)numerator/denominator);

        System.out.println("Accuracy is :\t"+(ans)*100);
        return ans;
    }

    public static void main(String args[]) {
        Accuracy();
    }
}
