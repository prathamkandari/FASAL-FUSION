import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Readingcsv {
    public static void main(String[] args) {
        String csvFile = "C:\\Project\\FASAL-FUSION\\data\\Crop_recommendation.csv"; // Replace with your file path
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

                // Process the fields as needed
                for (String field : fields) {
                    System.out.print(field + "\t");
                }
                System.out.println();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
