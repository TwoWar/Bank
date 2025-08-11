package com.example.demo.Dao;

import com.example.demo.models.Card;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.ScrollPosition;
import org.springframework.data.domain.Window;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {

    @NotNull List<Card> findAll();


    Window<Card> findFirst100ByCvvOrderById(String name, ScrollPosition scrollPosition);


    @Query("SELECT c FROM Card c WHERE c.cvv = :cvv AND c.number = :number AND c.name = :name")
    Optional<Card> findCardByCvvAndNumberAndName(
            @Param("cvv") @NotNull String cvv,
            @Param("number") @NotNull String number,
            @Param("name") @NotNull String name
    );

@Modifying
    @Query("UPDATE Card s SET s.money= case when s.money >= 500 then s.money - 500 else s.money end WHERE s.number=:number")
    boolean DebitingMoney(@NotNull String number);

    @Modifying
    @Query("UPDATE Card s SET s.money=s.money+500 WHERE s.number=:number")

    boolean ReceivingMoney (@NotNull String number);


}
