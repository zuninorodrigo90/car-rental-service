package com.dxc.carrental.model;

import jakarta.persistence.*;

@Entity
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(unique = true, nullable = false)
    private String dni;
    private String name;
    private String email;
    private int loyaltyPoints;
}
