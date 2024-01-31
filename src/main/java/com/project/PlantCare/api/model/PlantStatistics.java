package com.project.PlantCare.api.model;

public class PlantStatistics {
    private Integer plantId;
    private Integer uniqueId;
    private float airHumidity;
    private float airTemperature;
    private float waterLvl;
    private float sunLvl;

    public PlantStatistics(float airHumidity, float airTemperature, float waterLvl, float sunLvl) {
        this.airHumidity = airHumidity;
        this.airTemperature = airTemperature;
        this.waterLvl = waterLvl;
        this.sunLvl = sunLvl;
    }

    public Integer getPlantId() {
        return plantId;
    }
    public Integer getUniqueId() {
        return uniqueId;
    }
    public float getAirHumidity() {
        return airHumidity;
    }
    public float getAirTemperature() {
        return airTemperature;
    }
    public float getWaterLvl() {
        return waterLvl;
    }
    public float getSunLvl() {
        return sunLvl;
    }
}
