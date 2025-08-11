package com.example.demo.Controller;

import com.example.demo.DTO.SuccessDebitingFundsTopicDTO;
import com.example.demo.Service.CheckUserBank;
import com.example.demo.Service.EventConverter;
import com.example.demo.Service.GenerateIdOperationService;
import com.example.demo.Service.Operations;
import com.example.demo.models.Card;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/myBank")
public class replenish {

    @Autowired
    private EventConverter eventConverter;

    @Autowired
    private CheckUserBank checkUserBank;

    @Autowired
    private Operations operations;

    @Autowired
    private GenerateIdOperationService generateIdOperationService;

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @GetMapping("/replenishBalance")
    public String replenishBalance(@ModelAttribute("card") Card card, @RequestParam("userId") Long userId, Model model){

        redisTemplate.opsForValue().set(String.valueOf(userId),card);

        model.addAttribute("userId", userId);

        System.out.println(userId);

        return "/bank/replenishBalance";
    }

    @PostMapping("/postReplenish")

    public String postReplenish(Card card,@RequestParam("userId") Long userId){

        System.out.println(card.getName());
        System.out.println(userId);
        System.out.println();


        Long idOperation = generateIdOperationService.generateIdOperation();

        SuccessDebitingFundsTopicDTO successDebitingFundsTopicDTO = new SuccessDebitingFundsTopicDTO();
        successDebitingFundsTopicDTO.setId(userId);
        successDebitingFundsTopicDTO.setIdOperation(idOperation);
        successDebitingFundsTopicDTO.setMoney(500);

        operations.operationDebitingMoneyAndSendMessage(successDebitingFundsTopicDTO,card.getCvv(),card.getNumber(),card.getName());

        return "/bank/postReplenish";
    }


}
