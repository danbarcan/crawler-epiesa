package com.dd.web.carcrawler.entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Car_fuel_type")
public class FuelType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String value;
    private String description;

    @OneToMany(
            mappedBy = "fuelType",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER
    )
    private List<EngineSize> engineSizes;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "model_id")
    private Model model;

    public FuelType() {
    }

    public FuelType(String description, String value) {
        this.description = description;
        this.value = value;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getName() {
        return description;
    }

    public void setName(String fuelType) {
        this.description = fuelType;
    }

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public List<EngineSize> getEngineSizes() {
        return engineSizes;
    }

    public void setEngineSizes(List<EngineSize> engineSizes) {
        this.engineSizes = engineSizes;
    }

    public void addEngineSize(EngineSize engineSize) {
        if (this.engineSizes == null) {
            this.engineSizes = new ArrayList<>();
        }
        this.engineSizes.add(engineSize);
    }

    @Override
    public String toString() {
        return "\n\t\tFuelType{" +
                "fuelType='" + description + '\'' +
                ", engineSizes=" + engineSizes +
                '}';
    }
}
