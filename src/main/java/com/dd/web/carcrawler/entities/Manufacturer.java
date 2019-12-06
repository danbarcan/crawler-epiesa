package com.dd.web.carcrawler.entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Car_manufacturers")
public class Manufacturer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String value;
    private String description;

    @OneToMany(
            mappedBy = "manufacturer",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER
    )
    private List<Model> models;

    public Manufacturer() {
    }

    public Manufacturer(String description, String value) {
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

    public List<Model> getModels() {
        return models;
    }

    public void setModels(List<Model> models) {
        this.models = models;
    }

    public void addModel(Model model) {
        if (this.models == null) {
            this.models = new ArrayList<>();
        }
        this.models.add(model);
    }

    @Override
    public String toString() {
        return "Manufacturer{" +
                "name='" + description + '\'' +
                ", models=" + models +
                '}';
    }
}
