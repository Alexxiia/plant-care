package com.project.PlantCare.api.fuzzy;

import com.project.PlantCare.api.model.Plant;
import com.project.PlantCare.api.model.PlantStatistics;
import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.FunctionBlock;
import net.sourceforge.jFuzzyLogic.rule.Variable;

public class PlantCareFuzzy {

    double sunLvl;
    double sunLvlNeeded;
    double waterLvl;
    double waterLvlNeeded;
    FIS fisWater;
    FIS fisSunlight;

    public void loadFile() throws Exception{

        String fileNameSunlight = "src/main/java/com/project/PlantCare/api/fuzzy/plantcareSunlight.fcl";
        fisSunlight = FIS.load(fileNameSunlight, true);
        if( fisSunlight == null ) {
            System.err.println("File: '" + fileNameSunlight + "' can not be loaded");
            return;
        }

        String fileNameWater = "src/main/java/com/project/PlantCare/api/fuzzy/plantcareWater.fcl";
        fisWater = FIS.load(fileNameWater, true);
        if( fisWater == null ) {
            System.err.println("File: '" + fileNameWater + "' can not be loaded");
            return;
        }
    }

    public String run(Plant plant, PlantStatistics plantStatistics) {

        sunLvl = plantStatistics.getSunLvl();
        waterLvl = plantStatistics.getWaterLvl();

        sunLvlNeeded = (plant.getMinSunLvl() + plant.getMaxSunLvl())/2f;
        waterLvlNeeded = (plant.getMinWaterLvl() + plant.getMaxWaterLvl())/2f;

        System.out.println("sunLvl: " + sunLvl + ", waterLvl: " + waterLvl);
        System.out.println("sunNeeded: " + sunLvlNeeded + ", waterNeeded: " + waterLvlNeeded);

        FunctionBlock functionBlockSun = fisSunlight.getFunctionBlock(null);
        fisSunlight.setVariable("sunLvl", sunLvl);
        fisSunlight.setVariable("sunLvlNeeded", sunLvlNeeded);
        fisSunlight.evaluate();

        FunctionBlock functionBlockWater = fisWater.getFunctionBlock(null);
        fisWater.setVariable("waterLvl", waterLvl);
        fisWater.setVariable("waterLvlNeeded", waterLvlNeeded);
        fisWater.evaluate();

        Variable sunlight_importance_temporary = functionBlockSun.getVariable("sunlight_importance");
        Variable water_importance_temporary = functionBlockWater.getVariable("water_importance");
        double action_sun = sunlight_importance_temporary.getValue();
        double action_water = water_importance_temporary.getValue();

        String result = "";

//        System.out.println(String.format("S1: %8.4f\t  S2: %8.4f\t D:%8.4f\t => P:%8.4f", speed1*10000, speed2*10000, (phi1-phi2)*100, speed_change)); //Wy�wietla parametry na konsoli
        System.out.println("sun: " + action_sun + ", water: " + action_water);

//        AKCJE NA PODSTAWIE SYSEMU ROZMYTEGO

        if(action_water >= 60 || action_sun >= 60 ) {
            if(action_water >= action_sun) {
                result = "INCREASE_WATER";
            } else if(action_sun > action_water) {
                result = "INCREASE_SUNLIGHT";
            }
        } else if(action_water <= 40 || action_sun <= 40 ) {
            if(action_water <= action_sun) {
                result = "DECREASE_WATER";
            } else if(action_sun < action_water) {
                result = "DECREASE_SUNLIGHT";
            }
        } else {
            result = "NOTHING";
        }

        System.out.println("ACTION: " + result + "\n");
        return result;
    }
}
