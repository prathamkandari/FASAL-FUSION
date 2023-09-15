import java.util.*;

class Crop {
    private String name;
    private String soilType;
    private String climate;
    private List<String> recommendedCrops;

    public Crop(String name, String soilType, String climate, List<String> recommendedCrops) {
        this.name = name;
        this.soilType = soilType;
        this.climate = climate;
        this.recommendedCrops = recommendedCrops;
    }

    public String getName() {
        return name;
    }

    public String getSoilType() {
        return soilType;
    }

    public String getClimate() {
        return climate;
    }

    public List<String> getRecommendedCrops() {
        return recommendedCrops;
    }
}

public class CropRecommendationSystem {
    private List<Crop> crops;

    public CropRecommendationSystem() {
        this.crops = new ArrayList<>();
    }

    public void addCrop(Crop crop) {
        crops.add(crop);
    }

    public List<String> recommendCrops(String soilType, String climate) {
        List<String> recommendedCrops = new ArrayList<>();

        for (Crop crop : crops) {
            if (crop.getSoilType().equalsIgnoreCase(soilType) && crop.getClimate().equalsIgnoreCase(climate)) {
                recommendedCrops.addAll(crop.getRecommendedCrops());
            }
        }

        return recommendedCrops;
    }

    public static void main(String[] args) {
        CropRecommendationSystem recommendationSystem = new CropRecommendationSystem();

        // Add crop data
        recommendationSystem.addCrop(new Crop("Wheat", "Loamy", "Temperate", Arrays.asList("Wheat", "Barley")));
        recommendationSystem.addCrop(new Crop("Rice", "Clay", "Tropical", Arrays.asList("Rice", "Maize")));
        recommendationSystem.addCrop(new Crop("Soybean", "Sandy", "Subtropical", Arrays.asList("Soybean", "Sunflower")));

        // Example recommendation
        String soilType = "Loamy";
        String climate = "Temperate";
        List<String> recommendedCrops = recommendationSystem.recommendCrops(soilType, climate);

        if (recommendedCrops.isEmpty()) {
            System.out.println("No crops recommended for the given soil type and climate.");
        } else {
            System.out.println("Recommended crops for " + soilType + " soil and " + climate + " climate:");
            for (String crop : recommendedCrops) {
                System.out.println("- " + crop);
            }
        }
    }
}
