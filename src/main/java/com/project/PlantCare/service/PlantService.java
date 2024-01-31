package com.project.PlantCare.service;

import com.project.PlantCare.api.model.Plant;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class PlantService {
    private List<Plant> plantList;

    public PlantService() {
        plantList = new ArrayList<>();

        Plant plant1 = new Plant(1, "Monstera", 45.0f, 65.0f, 19.0f,29.0f, 45.0f, 70.0f, 50.0f, 80.0f);
        Plant plant2 = new Plant(2, "Sansewiera", 10.0f, 30.0f, 21.0f,31.0f, 15.0f, 30.0f, 70.0f, 90.0f);

        plantList.addAll(Arrays.asList(plant1, plant2));
    }

    public Optional<Plant> getPlant(Integer id) {
        Optional optional = Optional.empty();
        for (Plant plant: plantList) {
            if(id == plant.getId()) {
                optional = Optional.of(plant);
                return optional;
            }
        }
        return optional;
    }
}
