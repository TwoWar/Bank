package com.example.demo.AuthenticitycheckCard;


import com.example.demo.Dao.CardDao;
import com.example.demo.models.Card;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

@Component
public class CheckCard {
    @Autowired
    private CardDao cardDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;


    public boolean check (Card card){

        try {
            System.out.println("Запрос");
            Connection conn = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection(); //подключение к бд
            PreparedStatement st = conn.prepareStatement("SELECT * FROM card WHERE number=?",ResultSet.CONCUR_UPDATABLE); //ввод sql запроса
            st.setString(1, card.getNumber());
            ResultSet rs = st.executeQuery(); //преобразование запросов в ResultSet
            System.out.println(card.getCvv());
            System.out.println(card.getName());
            while (rs.next()) {//Перебор результата как цикл for(сначала 1 результат ,потом 2 и.т.д)
                if (rs.getString("cvv").equals(card.getCvv()) && rs.getString("name").equals(card.getName())){
                    System.out.println("Вызов");
                    jdbcTemplate.update("UPDATE card SET money=? WHERE number=?",rs.getInt("money")-500,card.getNumber());

                    return true;
                }
            }
            conn.close(); //НАДО ЗАКРЫВАТЬ ,ВЕДЬ ЕСЛИ НЕ ЗАКРЫТЬ ,ТО ПУЛ ЗАПРОСОВ К БД ЗАПОЛНИТСЯ И ВООБЩЕ НЕВОЗМОЖНО БУДЕТ ОБРАТИТЬСЯ К БД

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }




return false;
    }


}
