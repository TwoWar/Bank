package com.example.demo.Controller;

import com.example.demo.AuthenticitycheckCard.CheckCard;
import com.example.demo.Dao.CardDao;
import com.example.demo.Dao.CardRepository;
import com.example.demo.Dao.TestDao;
import com.example.demo.Methods;
import com.example.demo.models.Card;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.ScrollPosition;
import org.springframework.data.domain.Window;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class restController {

    @Autowired
    private CardDao cardDao;

    @Autowired
    private CheckCard checkCard;

    @Autowired
    private TestDao testDao;

    @Autowired
    private Methods methods;

    @Autowired
    private CardRepository cardRepository;

    @GetMapping("/test2")
    public Card card(){

        return cardDao.getCard("4080481191273031");
    }

    @GetMapping("/test3")
    public void generate(){
       // Faker faker = new Faker();
        //testDao.generateCards(faker);

        methods.GenerateUser();


    }


    @PostMapping("/test")
    public Card bank(@RequestBody String numbercard){ //@ResponseBody принял строку numbercard из запроса Feign и засунул эту строку уже этого метода
        System.out.println(numbercard);

        return cardDao.getCard(numbercard);
    }

    @PostMapping("/check")
    public boolean check(@RequestBody Card card){

        return checkCard.check(card);

    }

    @GetMapping("/traverse-lastname-keyset/{lastname}/1")
    public List<Card> FirstPage(@PathVariable String lastname){
        long start = System.currentTimeMillis();

        ScrollPosition offset=ScrollPosition.keyset();

        Window<Card> cardWindow;

        cardWindow=cardRepository.findFirst100ByCvvOrderById(lastname,offset);

        long time = System.currentTimeMillis() - start;
        System.out.println("keysetTime: " + time);

        return cardWindow.getContent();
    }

    @GetMapping("/traverse-lastname-keyset/{lastname}/{num}")
    public ResponseEntity<Map<String, Object>> traverseKeySet(@PathVariable String lastname , @PathVariable long num) {

        long start = System.currentTimeMillis();

        ScrollPosition offset = ScrollPosition.offset(100*(num-1)-1);


        Window<Card> cardWindow; //окно данных
        cardWindow=cardRepository.findFirst100ByCvvOrderById(lastname,offset);

        // 3. Получаем ID последней записи для следующей страницы
        Long nextLastId = null;
        if (!cardWindow.isEmpty()) {

            nextLastId = (long) (cardWindow.size())*num;
        }

        long time = System.currentTimeMillis() - start;
        System.out.println("keysetTime: " + time);

        // 4. Формируем ответ
        Map<String, Object> response = new HashMap<>();
        response.put("data", cardWindow.getContent());  // Текущие данные
        response.put("nextLastId", nextLastId);       // ID для следующей страницы
        response.put("hasNext", cardWindow.hasNext()); // Есть ли ещё данные

        return ResponseEntity.ok(response);
    }


}
