package com.project.PlantCare.api.model;

public class Plant {
    private int id;
    private String name;
    private float minAirHumidity;
    private float maxAirHumidity;
    private float minAirTemperature;
    private float maxAirTemperature;
    private float minWaterLvl;
    private float maxWaterLvl;
    private float minSunLvl;
    private float maxSunLvl;

    public Plant(int id, String name, float minAirHumidity, float maxAirHumidity, float minAirTemperature, float maxAirTemperature, float minWaterLvl, float maxWaterLvl, float minSunLvl, float maxSunLvl) {
        this.id = id;
        this.name = name;
        this.minAirHumidity = minAirHumidity;
        this.maxAirHumidity = maxAirHumidity;
        this.minAirTemperature = minAirTemperature;
        this.maxAirTemperature = maxAirTemperature;
        this.minWaterLvl = minWaterLvl;
        this.maxWaterLvl = maxWaterLvl;
        this.minSunLvl = minSunLvl;
        this.maxSunLvl = maxSunLvl;
    }

    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public float getMinAirHumidity() {
        return minAirHumidity;
    }
    public float getMaxAirHumidity() {
        return maxAirHumidity;
    }
    public float getMinAirTemperature() {
        return minAirTemperature;
    }
    public float getMaxAirTemperature() {
        return maxAirTemperature;
    }
    public float getMinSunLvl() {
        return minSunLvl;
    }
    public float getMaxSunLvl() {
        return maxSunLvl;
    }
    public float getMinWaterLvl() {
        return minWaterLvl;
    }
    public float getMaxWaterLvl() {
        return maxWaterLvl;
    }
}