package com.project.PlantCare.api.fuzzy;

import java.util.*;
import com.project.PlantCare.api.model.Plant;
import com.project.PlantCare.api.model.PlantStatistics;
import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.FunctionBlock;
import net.sourceforge.jFuzzyLogic.rule.Variable;

public class PlantCareFuzzyAll {

    class Importance {
        public double waterLvl = 0;
        public double sunLvl = 0;
        public double airTemperature = 0;
        public double airHumidity = 0;

        public ArrayList<double[]> getSorted() {
            ArrayList<double[]> importanceArray = new ArrayList<double[]>();
            importanceArray.add(new double[]{1.0, sunLvl});
            importanceArray.add(new double[]{2.0, waterLvl});
            importanceArray.add(new double[]{3.0, airTemperature});
            importanceArray.add(new double[]{4.0, airHumidity});

            Collections.sort(importanceArray, new Comparator<double[]>() {
                @Override
                public int compare(double[] o1, double[] o2) {
                    return (int) (Math.abs(o1[1]) - Math.abs(o2[1]));
                }
            });

            return importanceArray;
        }

        public ArrayList<String> getActionsList() {
            ArrayList<double[]> importanceArray = getSorted();
            ArrayList<String> actionsList = new ArrayList<String>();
            for (int i = 0; i < importanceArray.size(); i++) {
                String prefix = importanceArray.get(i)[1] > 0 ? "INCREASE" : "DECREASE";
                String suffix = "";
                if (importanceArray.get(i)[0] == 1.0) suffix = "SUNLIGHT";
                if (importanceArray.get(i)[0] == 2.0) suffix = "WATER";
                if (importanceArray.get(i)[0] == 3.0) suffix = "TEMPERATURE";
                if (importanceArray.get(i)[0] == 4.0) suffix = "HUMIDITY";
                actionsList.add(prefix + "_" + suffix);
            }
            return actionsList;
        }

    }
    private HashMap<Integer, Importance> importanceMap = new HashMap<Integer, Importance>();

    public void setImportance(int UniqueId, double sunLvl, double waterLvl, double airHumidity, double airTemperature) {
        Importance importance = new Importance();
        importance.sunLvl = sunLvl;
        importance.waterLvl = waterLvl;
        importance.airHumidity = airHumidity;
        importance.airTemperature = airTemperature;
        importanceMap.put(UniqueId, importance);
    }

    public Importance getImportance(int UniqueId) {
        if (importanceMap.containsKey(uniqueId)) {
            return importanceMap.get(UniqueId);
        } else {
            return new Importance();
        }
    }

    double sunLvl;
    double waterLvl;
    double humidityLvl;
    double temperatureLvl;
    Integer uniqueId;
    Integer plantId;
    FIS fisMonstera;
    FIS fisSansewiera;

    public void loadFile() throws Exception{

        String fileNameSansewiera = "src/main/java/com/project/PlantCare/api/fuzzy/plantcareKaktus.fcl";
        fisSansewiera = FIS.load(fileNameSansewiera, true);
        if( fisSansewiera == null ) {
            System.err.println("File: '" + fileNameSansewiera + "' can not be loaded");
            return;
        }

        String fileNameMonstera = "src/main/java/com/project/PlantCare/api/fuzzy/plantcareMonstera.fcl";
        fisMonstera = FIS.load(fileNameMonstera, true);
        if( fisMonstera == null ) {
            System.err.println("File: '" + fileNameMonstera + "' can not be loaded");
            return;
        }
    }

    public String run(Plant plant, PlantStatistics plantStatistics) {

        sunLvl = plantStatistics.getSunLvl();
        waterLvl = plantStatistics.getWaterLvl();
        humidityLvl = plantStatistics.getAirHumidity();
        temperatureLvl = plantStatistics.getAirTemperature();
        uniqueId = plantStatistics.getUniqueId();
        plantId = plantStatistics.getPlantId();

        FIS fis;

        if(plantId.equals(1)) {
            fis = fisMonstera;

        } else {
            fis = fisSansewiera;
        }

        FunctionBlock functionBlock = fis.getFunctionBlock(null);
        fis.setVariable("sunLvl", sunLvl);
        fis.setVariable("waterLvl", waterLvl);
        fis.setVariable("humidityLvl", humidityLvl);
        fis.setVariable("temperatureLvl", temperatureLvl);
        fis.evaluate();

        Variable sunlight_importance_temporary = functionBlock.getVariable("sunlight_importance");
        Variable water_importance_temporary = functionBlock.getVariable("water_importance");
        Variable humidity_importance_temporary = functionBlock.getVariable("humidity_importance");
        Variable temperature_importance_temporary = functionBlock.getVariable("temperature_importance");
        double action_sun = sunlight_importance_temporary.getValue();
        double action_water = water_importance_temporary.getValue();
        double action_humidity = humidity_importance_temporary.getValue();
        double action_temperature = temperature_importance_temporary.getValue();

        Importance importance = getImportance(uniqueId);

        if(action_sun > 10.0 || action_sun < -10.0) {
            action_sun = Math.clamp(importance.sunLvl + action_sun, -200, 200);
        } else {
            action_sun = 0;
        }
        if(action_water > 10.0 || action_water < -10.0) {
            action_water = Math.clamp(importance.waterLvl + action_water, -200, 200);
        } else {
            action_water = 0;
        }
        if(action_humidity > 10.0 || action_humidity < -10.0) {
            action_humidity = Math.clamp(importance.airHumidity + action_humidity/2f, -200, 200);
        } else {
            action_humidity = 0;
        }
        if(action_temperature > 10.0 || action_temperature < -10.0) {
            action_temperature = Math.clamp(importance.airTemperature + action_temperature/2f, -200, 200);
        } else {
            action_temperature = 0;
        }
        if (action_sun == 0 && action_water == 0 && action_humidity == 0 && action_temperature == 0) {
            return "NOTHING";
        }


//        System.out.println("sun: " + action_sun + ", water: " + action_water);


        setImportance(
            uniqueId,
            action_sun,
            action_water,
            action_humidity,
            action_temperature
        );

//        TEST
        System.out.println("sun: " + (importance.sunLvl + action_sun) + ", water: " + (importance.waterLvl + action_water) + ", humidity: " + (importance.airHumidity + action_humidity) + ", temperature: " + (importance.airTemperature + action_temperature));

        ArrayList<String> actionsList = getImportance(uniqueId).getActionsList();
//        String result = String.join("|", actionsList);
        String result = actionsList.get(3);

        System.out.println("ACTION: " + result + "\n");
        return result;







    }
}
