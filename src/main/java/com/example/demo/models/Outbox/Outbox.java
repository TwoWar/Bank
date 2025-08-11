package com.example.demo.models.Outbox;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Date;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "outbox_operation")
public class Outbox {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "operation_sequence", sequenceName = "operation_seq", allocationSize = 100)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "id_operation")
    private Long idOperation;

    private int money;

    private Timestamp date;




}
