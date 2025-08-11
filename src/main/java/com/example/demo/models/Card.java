package com.example.demo.models;


import jakarta.persistence.*;

import lombok.Data;

@Data
@Entity
@Table(name = "card")
public class Card {



    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "card_sequence", sequenceName = "card_seq", allocationSize = 1)
    private Long id;

    private String number ;

    private String name;

    private String cvv;

    private  String name_bank;

    private Integer money;



}
