package fasal_fusion;

public class loadModel  {
    static DecisionTreeClassifier obj;
    public static <DecisionTreeClassifier> DecisionTreeClassifier getModel() {
        return DecisionTreeClassifier.loadModel("path_to_saved_model.ser");
    }
    public static String middleware(double nitrogen, double phosphorus, double potassium, double temp, double humidity, double ph, double rain){
        String result="";
        result=DecisionTreeClassifier.predictCrop(
            nitrogen,
            phosphorus,
            potassium,
            temp,
            humidity,
            ph
        );
        return result;
    }
}
