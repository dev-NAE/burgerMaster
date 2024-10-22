package com.itwillbs.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;

@Controller
@RequiredArgsConstructor
@Log
public class TXController {

    @GetMapping("/transaction")
    public String insertOrder(Model model, HttpSession session) {
        model.addAttribute("today", LocalDate.now());
        model.addAttribute("id", "ABCD");
        model.addAttribute("name", "매니저");
        return "transaction/order/insert";
    }

    @GetMapping("/findManager")
    public String findManager() {
        return "transaction/order/findManager";
    }

    @GetMapping("/findSupplier")
    public String findSupplier() {
        return "transaction/order/findSupplier";
    }

    @GetMapping("/addItems")
    public String addItems() {
        return "transaction/order/addItems";
    }
}
