package com.dd.web.carcrawler.entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Car_engine_sizes")
public class EngineSize {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String value;
    private String description;

    @OneToMany(
            mappedBy = "engineSize",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER
    )
    private List<Power> powers;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fuel_type_id")
    private FuelType fuelType;

    public EngineSize(String description, String value) {
        this.description = description;
        this.value = value;
    }

    public EngineSize() {
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

    public List<Power> getPowers() {
        return powers;
    }

    public void setPowers(List<Power> powers) {
        this.powers = powers;
    }

    public void addYear(String year) {
        if (this.powers == null) {
            this.powers = new ArrayList<>();
        }
        this.powers.add(new Power(year));
    }

    public FuelType getFuelType() {
        return fuelType;
    }

    public void setFuelType(FuelType fuelType) {
        this.fuelType = fuelType;
    }

    @Override
    public String toString() {
        return "\n\t\tEngineSize{" +
                "name='" + description + '\'' +
                ", powers=" + powers +
                '}';
    }
}
