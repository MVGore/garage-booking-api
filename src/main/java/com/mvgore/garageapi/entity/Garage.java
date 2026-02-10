package com.mvgore.garageapi.entity;

import jakarta.persistence.*;
import java.util.Set;

@Entity
@Table(name = "garages")
public class Garage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @Column(nullable = false)
    private String name;

    @Column(length = 500)
    private String description;

    @Column(nullable = false)
    private String location;

    @Column(name = "price_range")
    private String priceRange;

    @OneToMany(mappedBy = "garage", cascade = CascadeType.ALL)
    private Set<GarageServiceEntity> services;

    @OneToMany(mappedBy = "garage", cascade = CascadeType.ALL)
    private Set<Booking> bookings;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getOwner() { return owner; }
    public void setOwner(User owner) { this.owner = owner; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getPriceRange() { return priceRange; }
    public void setPriceRange(String priceRange) { this.priceRange = priceRange; }

    public Set<GarageServiceEntity> getServices() { return services; }
    public void setServices(Set<GarageServiceEntity> services) { this.services = services; }

    public Set<Booking> getBookings() { return bookings; }
    public void setBookings(Set<Booking> bookings) { this.bookings = bookings; }
}
