package com.demo.controller.blockchain;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/blockchain")
public class TransferController {
    

    //addess receiver  0xe80082d1a4a6E086D9FDB471E49173cc3B30C705


    @GetMapping("/transfer")
    public String transfer() {
        return "blockchain/transfer";
    }
}
