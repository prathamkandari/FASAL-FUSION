package fasal_fusion;
import java.util.Map;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;



public class RecommendationHandler implements RequestHandler<Map<String, Object>, String> {

    @Override
    public String handleRequest(Map<String, Object> input, Context context) {
        // Extract necessary parameters from the input
        Map<String, Object> requestBody = (Map<String, Object>) input.get("body");
        // Extract NPK values, pH, humidity, temperature, rainfall, etc. from the request body
        double nitrogen = (double) requestBody.get("nitrogen");
        double phosphorus = (double) requestBody.get("phosphorus");
        double potassium = (double) requestBody.get("potassium");
        double pHValue = (double) requestBody.get("pH");
        double humidity = (double) requestBody.get("humidity");
        double temperature = (double) requestBody.get("temperature");
        double rainfall = (double) requestBody.get("rainfall");
        
        // Example: Process the input data using your decision tree model to get the recommended crop
        String recommendedCrop = loadModel.getModel().predictCrop(nitrogen, phosphorus, potassium,  temperature,humidity,pHValue, rainfall);

        // Prepare the response
        return recommendedCrop;
    }
}
