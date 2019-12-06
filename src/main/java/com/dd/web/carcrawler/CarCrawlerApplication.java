package com.dd.web.carcrawler;

import com.dd.web.carcrawler.controllers.SaveAllCarsService;
import com.dd.web.carcrawler.entities.Manufacturer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Comparator;
import java.util.List;

@SpringBootApplication
public class CarCrawlerApplication {
    private static SaveAllCarsService saveAllCarsService;

    @Autowired
    public CarCrawlerApplication(SaveAllCarsService saveAllCarsService) {
        this.saveAllCarsService = saveAllCarsService;
    }


    public static void main(String[] args) {
        SpringApplication.run(CarCrawlerApplication.class, args);

        boolean finished = false;

        while (!finished) {
            List<Manufacturer> manufacturers = saveAllCarsService.getAllManufacturers();

            try {
                saveAllCarsService.prepare();
                Manufacturer lastManufacturer = manufacturers.stream().sorted(Comparator.comparing(Manufacturer::getDescription).reversed()).findFirst().orElse(null);
                finished = saveAllCarsService.test(lastManufacturer);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                saveAllCarsService.teardown();
            }
        }

    }

}
