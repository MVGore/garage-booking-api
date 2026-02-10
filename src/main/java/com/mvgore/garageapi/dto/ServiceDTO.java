package com.mvgore.garageapi.dto;

public class ServiceDTO {
    private Long id;
    private String name;
    private Double price;
    private Integer duration; // in minutes
    private Long garageId;

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public Integer getDuration() { return duration; }
    public void setDuration(Integer duration) { this.duration = duration; }

    public Long getGarageId() { return garageId; }
    public void setGarageId(Long garageId) { this.garageId = garageId; }
}
