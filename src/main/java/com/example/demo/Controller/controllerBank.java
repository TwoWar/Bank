package com.example.demo.Controller;


import com.example.demo.Dao.CardDao;
import com.example.demo.models.Card;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/createcard")
public class controllerBank {

    @Autowired
    private CardDao cardDao;



    @GetMapping("/create")
    public String create(){

        return "bank/create";
    }

    @PostMapping("/save")
    public String Save(String name,String surname){

        cardDao.setCard(name,surname);


return "redirect:/createcard/create";
    }

    @GetMapping("/money")
    public String money(){

        return "bank/money";
    }

    @PostMapping("/postmoney")
    public String postmoney(String numberCard,String money){
        cardDao.sendmoney(numberCard, Integer.valueOf(money));



        return "redirect:/createcard/money";
    }







}
