
public class loadModel {

    static DecisionTreeClassifier loadedmodel = DecisionTreeClassifier
            .loadModel("C:\\Users\\Manav Khandurie\\Downloads\\fasalfusion\\models\\consolemodel.ser");
    static knnAlgorithm knnAlgo = knnAlgorithm
            .loadModel("C:\\Users\\Manav Khandurie\\Downloads\\fasalfusion\\models\\knnmodel.ser");

    public static DecisionTreeClassifier getModel() {
        return loadedmodel;
    }

    public static knnAlgorithm getKnnModel() {
        return knnAlgo;
    }

    public static String middleware(double nitrogen, double phosphorus, double potassium, double temp, double humidity,
            double ph, double rain) {
        String result = "";
        result = loadedmodel.predictCrop(nitrogen, phosphorus, potassium, temp, humidity, ph, rain);
        return result;
    }

    public static void main(String[] args) {
        String result = "", input;
        double nitrogen, phosphorus, potassium, temp, ph, humidity, rain;
        int choice;

        input = System.console().readLine(
                "Enter the model to be loaded: \n 1:Decision Tree Classifier Model \n 2: KNN Classification Model \n");
        choice = Integer.parseInt(input);

        input = System.console().readLine("Enter the value of N: ");
        nitrogen = Double.parseDouble(input);

        input = System.console().readLine("Enter the value of P: ");
        phosphorus = Double.parseDouble(input);

        input = System.console().readLine("Enter the value of K: ");
        potassium = Double.parseDouble(input);

        input = System.console().readLine("Enter the value of temp: ");
        temp = Double.parseDouble(input);

        input = System.console().readLine("Enter the value of humidity: ");
        humidity = Double.parseDouble(input);

        input = System.console().readLine("Enter the value of ph: ");
        ph = Double.parseDouble(input);

        input = System.console().readLine("Enter the value of rain: ");
        rain = Double.parseDouble(input);
        boolean flag = true;
        switch (choice) {
            case 1:

                break;
            case 2:
                flag = false;
                break;
            default:
                System.out.println("Incorect Choice!!");
        }
        if (loadedmodel != null && flag) {
            result = loadedmodel.predictCrop(nitrogen, phosphorus, potassium, temp, humidity, ph, rain);
            System.out.println(result);
        }

        else if (knnAlgo != null && !flag) {
            result = knnAlgo.predictCrop(221.0, 1.0, 1.0, 2.52, 52.0, 25.0, 25.0);
            System.out.println(result);
        } else if (!flag)
            System.out.println("No KNN Model Created");
        else
            System.out.println("No DTC Model Created");
    }
}
