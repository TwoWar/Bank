package com.example.demo.Dao;


import com.example.demo.GenerateCard.Generate;
import com.example.demo.models.Card;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class CardDao {

    @Autowired
    public JdbcTemplate jdbcTemplate;

    public CardDao(){

    }

    public CardDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Autowired
    Generate generate;




public void setCard(String name,String surname){
    jdbcTemplate.update("INSERT INTO card VALUES(default,?,?,?,?,?)",generate.GenerateNumber(),name+" "+surname,generate.GenerateCVV(),"YmpaLympa",0);
}

public void sendmoney(String numberCard,Integer money){
    jdbcTemplate.update("UPDATE card SET money=? WHERE number=?",money,numberCard);
}

    public Card getCard(String numberCard) {
        System.out.println("AllOrders");
        return jdbcTemplate.queryForObject("SELECT * FROM card WHERE number=?", new BeanPropertyRowMapper<>(Card.class), numberCard);
}






}
