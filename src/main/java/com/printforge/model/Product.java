package com.printforge.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private String material;   // e.g. PLA, PETG, ABS, TPU
    private Double price;
    private Integer stock;
}
