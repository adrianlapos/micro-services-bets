package com.example.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String stadium;
    private Long externalApiId;

    public Team() {
    }

    public Team(String name, String stadium,Long externalApiId) {
        this.name = name;
        this.stadium = stadium;
        this.externalApiId = externalApiId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStadium() {
        return stadium;
    }

    public void setStadium(String stadium) {
        this.stadium = stadium;
    }

    public Long getExternalApiId() {
        return externalApiId;
    }

    public void setExternalApiId(Long externalApiId) {
        this.externalApiId = externalApiId;
    }
}
