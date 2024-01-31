package com.project.PlantCare.api.controller;
import com.project.PlantCare.api.fuzzy.PlantCareFuzzy;
import com.project.PlantCare.api.fuzzy.PlantCareFuzzyAll;
import com.project.PlantCare.api.model.Plant;
import com.project.PlantCare.api.model.PlantStatistics;
import com.project.PlantCare.service.PlantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class PlantController {
    private PlantService plantService;
    private PlantCareFuzzy plantCareFuzzy;
    private PlantCareFuzzyAll plantCareFuzzyAll;


    @Autowired
    public PlantController(PlantService plantService) throws Exception {
        this.plantService = plantService;
        try {
            plantCareFuzzy = new PlantCareFuzzy();
            plantCareFuzzy.loadFile();

            plantCareFuzzyAll = new PlantCareFuzzyAll();
            plantCareFuzzyAll.loadFile();
        } catch(Exception e) {
            throw e;
        }
    }

    @GetMapping("/plant")
    public Plant getPlant(@RequestParam Integer id) {
        Optional<Plant> plant = plantService.getPlant(id);
        if(plant.isPresent()) {
            return (Plant) plant.get();
        }
        return null;
    }
    @CrossOrigin
    @PostMapping("/plant-statistics")
    public String getStatistics(@RequestBody PlantStatistics plantStatistics) throws Exception {

        Optional<Plant> plant = plantService.getPlant(plantStatistics.getPlantId());
        if(plant.isPresent()) {
            try {
                return plantCareFuzzy.run(plant.get(), plantStatistics);
            } catch(Exception e) {
                throw e;
            }
        }
        return null;
    }

    @CrossOrigin
    @PostMapping("/plant-all-statistics")
    public String getAllStatistics(@RequestBody PlantStatistics plantStatistics) throws Exception {

        Optional<Plant> plant = plantService.getPlant(plantStatistics.getPlantId());
        if(plant.isPresent()) {
            try {
                return plantCareFuzzyAll.run(plant.get(), plantStatistics);
            } catch(Exception e) {
                throw e;
            }
        }
        return null;
    }
}
