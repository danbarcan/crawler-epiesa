package com.dd.web.carcrawler.entities;

import javax.persistence.*;

@Entity
@Table(name = "Car_powers")
public class Power {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String description;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "engine_size_id")
    private EngineSize engineSize;

    public Power() {
    }

    public Power(String description) {
        this.description = description;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return description;
    }

    public void setName(String power) {
        this.description = power;
    }

    public EngineSize getEngineSize() {
        return engineSize;
    }

    public void setEngineSize(EngineSize engineSize) {
        this.engineSize = engineSize;
    }

    @Override
    public String toString() {
        return "power=" + description;
    }
}
