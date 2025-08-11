package com.example.demo.Service;


import com.example.demo.Dao.CardRepository;
import com.example.demo.models.Card;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class CheckUserBank {

    @Autowired
    private CardRepository cardRepository;

    private static final Logger logger = LoggerFactory.getLogger(CheckUserBank.class);



    public void debitingMoney(String cvv, String number, String name, int amount) {
        System.out.println("debiting money");
                try {
                    Optional<Card> checkCard = cardRepository.findCardByCvvAndNumberAndName(cvv, number, name);

                    if (checkCard.isPresent()) {
                        Card card = checkCard.get(); //Важно создать именно новый объект на основе ответа от бд

                        if (checkCard.get().getMoney() >= amount){
                            card.setMoney(checkCard.get().getMoney() - amount);
                            cardRepository.save(card);
                        }
                    }else {
                        logger.error("Карта с номером: {} , name: {} , cvv: {} не найдена " , number, name, cvv);
                        throw new RuntimeException("Карта с введенными вами данными не найдена");
                    }

                }catch (Exception e){
                    System.out.println("Не удалось найти пользователя " + e.getMessage());
                    throw new RuntimeException("Не удалось найти пользователя " + e.getMessage());
                }
    }



}
