package com.dd.web.carcrawler.entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Car_models")
public class Model {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String value;
    private String description;

    @OneToMany(
            mappedBy = "model",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER
    )
    private List<FuelType> fuelTypes;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "manufacturer_id")
    private Manufacturer manufacturer;

    public Model() {
    }

    public Model(String description, String value) {
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<FuelType> getFuelTypes() {
        return fuelTypes;
    }

    public void setFuelTypes(List<FuelType> fuelTypes) {
        this.fuelTypes = fuelTypes;
    }

    public void addEngineSize(FuelType fuelType) {
        if (this.fuelTypes == null) {
            this.fuelTypes = new ArrayList<>();
        }
        this.fuelTypes.add(fuelType);
    }

    public Manufacturer getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(Manufacturer manufacturer) {
        this.manufacturer = manufacturer;
    }

    @Override
    public String toString() {
        return "\n\tModel{" +
                "name='" + description + '\'' +
                ", fuelTypes=" + fuelTypes +
                '}';
    }
}
