package com.dd.web.carcrawler.controllers;

import com.dd.web.carcrawler.entities.*;
import com.dd.web.carcrawler.repositories.*;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class SaveAllCarsService {
    private final String INSERT_QUERY = "insert into table %s (%s) values (%s);\n";

    private String testUrl;
    private WebDriver driver;

    private FuelTypesRepository fuelTypesRepository;
    private PowersRepository powersRepository;
    private EngineSizeRepository engineSizeRepository;
    private ModelRepository modelRepository;
    private ManufacturerRepository manufacturerRepository;

    @Autowired
    public SaveAllCarsService(FuelTypesRepository fuelTypesRepository, PowersRepository powersRepository, EngineSizeRepository engineSizeRepository, ModelRepository modelRepository, ManufacturerRepository manufacturerRepository) {
        this.powersRepository = powersRepository;
        this.engineSizeRepository = engineSizeRepository;
        this.modelRepository = modelRepository;
        this.manufacturerRepository = manufacturerRepository;
        this.fuelTypesRepository = fuelTypesRepository;
    }

    public void prepare() {
        System.setProperty(
                "webdriver.chrome.driver",
                "webdriver/chromedriver.exe");

        testUrl = "https://www.epiesa.ro/";

        ChromeOptions options = new ChromeOptions();
        options.addArguments("headless");

        driver = new ChromeDriver(options);
        driver.manage().window().maximize();

        driver.get(testUrl);
    }

    public boolean test(Manufacturer lastManufacturer) {
        final Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
                .withTimeout(Duration.ofSeconds(2))
                .pollingEvery(Duration.ofSeconds(1))
                .ignoring(NoSuchElementException.class);

        List<Manufacturer> manufacturerList = new ArrayList<>();
        WebElement selectManufacturerElement = driver.findElement(By.name("select_marca"));
        Select selectManufacturer = new Select(selectManufacturerElement);

        boolean readManufacturers = false;
        int retryNoMan = 5;
        while (!readManufacturers && retryNoMan-- > 0) {
            try {
                manufacturerList = selectManufacturer.getOptions()
                        .stream()
                        .filter(manufacturer -> !manufacturer.getText().contains("MARCA") && (lastManufacturer == null || lastManufacturer.getDescription().compareTo(manufacturer.getText()) < 0))
                        .map(manufacturer -> new Manufacturer(manufacturer.getText(), manufacturer.getAttribute("value")))
                        .collect(Collectors.toList());
                readManufacturers = true;
            } catch (Exception e) {
                System.out.println("exception marca");
            }
        }

        StringBuilder sb = new StringBuilder();
        manufacturerList.forEach(manufacturer -> {
            manufacturerRepository.save(manufacturer);
            new Select(driver.findElement(By.name("select_marca"))).selectByValue(manufacturer.getValue());
            WebElement selectModelElement = driver.findElement(By.name("select_model"));
            wait.until((ExpectedCondition<Boolean>) driver -> {
                WebElement ele = driver.findElement(By.name("select_model"));
                if (ele == null || new Select(ele).getOptions().size() < 2)
                    return false;
                else {
                    System.out.println("WebElement model found");
                    return true;
                }
            });
//            while (new Select(selectModelElement).getOptions().size() < 2) ;
            Select selectModel = new Select(selectModelElement);
            List<Model> models = new ArrayList<>();

            boolean readModels = false;
            int retryNoModels = 5;
            while (!readModels && retryNoModels-- > 0) {
                try {
                    models = selectModel.getOptions()
                            .stream()
                            .filter(model -> !model.getText().contains("MODEL"))
                            .map(model -> new Model(model.getText(), model.getAttribute("value")))
                            .collect(Collectors.toList());
                    readModels = true;
                } catch (Exception e) {
                    System.out.println("exception model");
                }
            }
            models.forEach(model -> {
                model.setManufacturer(manufacturer);
                modelRepository.save(model);
                new Select(driver.findElement(By.name("select_model"))).selectByValue(model.getValue());
                WebElement selectVariantElement = driver.findElement(By.name("select_carburant"));
                wait.until((ExpectedCondition<Boolean>) driver -> {
                    WebElement ele = driver.findElement(By.name("select_carburant"));
                    if (ele == null || new Select(ele).getOptions().size() < 2)
                        return false;
                    else {
                        System.out.println("WebElement carburant found");
                        return true;
                    }
                });
//                while (new Select(selectVariantElement).getOptions().size() < 2) ;
                Select selectVariant = new Select(selectVariantElement);
                List<FuelType> fuelTypes = new ArrayList<>();

                boolean readFuelTypes = false;
                int retryNoFT = 5;
                while (!readFuelTypes && retryNoFT-- > 0) {
                    try {
                        fuelTypes = selectVariant.getOptions()
                                .stream()
                                .filter(fuelType -> !fuelType.getText().contains("CARBURANT"))
                                .map(fuelType -> new FuelType(fuelType.getText(), fuelType.getAttribute("value")))
                                .collect(Collectors.toList());
                        readFuelTypes = true;
                    } catch (Exception e) {
                        System.out.println("exception carburant");
                    }
                }

                fuelTypes.forEach(fuelType -> {
                    fuelType.setModel(model);
                    fuelTypesRepository.save(fuelType);
                    new Select(driver.findElement(By.name("select_carburant"))).selectByValue(fuelType.getValue());
                    WebElement selectEngineElement = driver.findElement(By.name("select_cilindree"));
                    wait.until((ExpectedCondition<Boolean>) driver -> {
                        WebElement ele = driver.findElement(By.name("select_cilindree"));
                        if (ele == null || new Select(ele).getOptions().size() < 2)
                            return false;
                        else {
                            System.out.println("WebElement cilindree found");
                            return true;
                        }
                    });
//                    while (new Select(selectEngineElement).getOptions().size() < 2) ;
                    Select selectEngine = new Select(selectEngineElement);

                    List<EngineSize> engineSizes = new ArrayList<>();
                    boolean readEngineSizes = false;
                    int retryNoES = 5;
                    while (!readEngineSizes && retryNoES-- > 0) {
                        try {
                            engineSizes = selectEngine.getOptions()
                                    .stream()
                                    .filter(engineSize -> !engineSize.getText().contains("CILINDREE"))
                                    .map(engineSize -> new EngineSize(engineSize.getText(), engineSize.getAttribute("value")))
                                    .collect(Collectors.toList());
                            readEngineSizes = true;
                        } catch (Exception e) {
                            System.out.println("exception cilindree");
                        }
                    }

                    engineSizes.forEach(engineSize -> {
                        engineSize.setFuelType(fuelType);
                        engineSizeRepository.save(engineSize);
                        new Select(driver.findElement(By.name("select_cilindree"))).selectByValue(engineSize.getValue());
                        WebElement selectYearElement;

                        selectYearElement = driver.findElement(By.name("select_motorizari"));
                        wait.until((ExpectedCondition<Boolean>) driver -> {
                            WebElement ele = driver.findElement(By.name("select_motorizari"));
                            if (ele == null || new Select(ele).getOptions().size() < 2)
                                return false;
                            else {
                                System.out.println("WebElement motorizari found");
                                return true;
                            }
                        });
//                        while (new Select(selectYearElement).getOptions().size() < 2) ;

                        Select selectYear = new Select(selectYearElement);

                        boolean readPowers = false;
                        int retryNoPowers = 5;
                        while (!readPowers && retryNoPowers-- > 5) {
                            try {
                                engineSize.setPowers(selectYear.getOptions()
                                        .stream()
                                        .filter(year -> !year.getText().contains("PUTERE"))
                                        .map(year -> {
                                            Power y = new Power(year.getText());
                                            y.setEngineSize(engineSize);
                                            powersRepository.save(y);
                                            return y;
                                        })
                                        .collect(Collectors.toList()));
                                readPowers = true;
                            } catch (Exception e) {
                                System.out.println("exeption putere");
                            }
                        }
                    });
                    fuelType.setEngineSizes(engineSizes);
                });
                model.setFuelTypes(fuelTypes);
            });
            manufacturer.setModels(models);
            sb.append(String.format(INSERT_QUERY, "CAR_MANUFACTURERS", "ID, DESCRIPTION", manufacturer.getId() + ", '" + manufacturer.getDescription() + "'"));

//            Manufacturer manufacturer1 = manufacturerRepository.save(manufacturer);
            manufacturer.getModels().forEach(model -> {
                sb.append(String.format(INSERT_QUERY, "CAR_MODELS", "ID, DESCRIPTION, MANUFACTURER_ID", model.getId() + ", '" + model.getDescription() + "', " + model.getManufacturer().getId()));
                //                model = modelRepository.save(model);
                model.getFuelTypes().forEach(fuelType -> {
                    sb.append(String.format(INSERT_QUERY, "CAR_FUEL_TYPES", "ID, DESCRIPTION, MODEL_ID", fuelType.getId() + ", '" + fuelType.getName() + "', " + fuelType.getModel().getId()));
//                    fuelType = fuelTypesRepository.save(fuelType);
                    fuelType.getEngineSizes().forEach(engineSize -> {
                        sb.append(String.format(INSERT_QUERY, "CAR_ENGINE_SIZES", "ID, DESCRIPTION, FUEL_TYPE_ID", engineSize.getId() + ", '" + engineSize.getDescription() + "', " + engineSize.getFuelType().getId()));
//                        engineSize = engineSizeRepository.save(engineSize);
                        engineSize.getPowers().forEach(power -> {
//                            power = powersRepository.save(power);
                            sb.append(String.format(INSERT_QUERY, "CAR_POWERS", "ID, DESCRIPTION, ENGINE_SIZE_ID", power.getId() + ", '" + power.getName() + "', " + power.getEngineSize().getId()));
                        });
                    });
                });
            });
        });

        System.out.println("End");

        manufacturerRepository.findAll().forEach(manufacturer -> {
            sb.append(String.format(INSERT_QUERY, "CAR_MANUFACTURERS", "ID, DESCRIPTION", manufacturer.getId() + ", '" + manufacturer.getDescription() + "'"));
            manufacturer.getModels().forEach(model -> {
                sb.append(String.format(INSERT_QUERY, "CAR_MODELS", "ID, DESCRIPTION, MANUFACTURER_ID", model.getId() + ", '" + model.getDescription() + "', " + model.getManufacturer().getId()));
                //                model = modelRepository.save(model);
                model.getFuelTypes().forEach(fuelType -> {
                    sb.append(String.format(INSERT_QUERY, "CAR_FUEL_TYPES", "ID, DESCRIPTION, MODEL_ID", fuelType.getId() + ", '" + fuelType.getName() + "', " + fuelType.getModel().getId()));
//                    fuelType = fuelTypesRepository.save(fuelType);
                    fuelType.getEngineSizes().forEach(engineSize -> {
                        sb.append(String.format(INSERT_QUERY, "CAR_ENGINE_SIZES", "ID, DESCRIPTION, FUEL_TYPE_ID", engineSize.getId() + ", '" + engineSize.getDescription() + "', " + engineSize.getFuelType().getId()));
//                        engineSize = engineSizeRepository.save(engineSize);
                        engineSize.getPowers().forEach(power -> {
//                            power = powersRepository.save(power);
                            sb.append(String.format(INSERT_QUERY, "CAR_POWERS", "ID, DESCRIPTION, ENGINE_SIZE_ID", power.getId() + ", '" + power.getName() + "', " + power.getEngineSize().getId()));
                        });
                    });
                });
            });
        });

        try {
            Files.write(Paths.get("cars_epiesa.sql"), sb.toString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }

    public void teardown() {
        driver.quit();
    }

    public List<Manufacturer> getAllManufacturers() {
        return manufacturerRepository.findAll();
    }
}
