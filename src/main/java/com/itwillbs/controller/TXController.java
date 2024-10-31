package com.itwillbs.controller;

import com.itwillbs.domain.transaction.OrderDTO;
import com.itwillbs.domain.transaction.OrderItemsDTO;
import com.itwillbs.domain.transaction.OrderRequestDTO;
import com.itwillbs.entity.*;
import com.itwillbs.service.TXService;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Log
@RequestMapping("/tx")
public class TXController {

    private final TXService txService;

    @GetMapping({"", "/"})
    public String transaction() {
        return "redirect:/tx/insertOrder";
    }

    @GetMapping("/insertOrder")
    public String insertOrder(Model model, HttpSession session) {
        model.addAttribute("today", LocalDate.now());
        // 접속자 id, name 가져와서 model에 담기
        String managerId = SecurityContextHolder.getContext().getAuthentication().getName();
        model.addAttribute("id", managerId);
        String managerName = txService.getManagerName(managerId);
        model.addAttribute("name", managerName);
        return "transaction/order/insert";
    }

    @PostMapping("/saveOrder")
    public ResponseEntity<String> saveOrder(@RequestBody OrderRequestDTO orderRequestDTO) {
        log.info("Controller saveOrder()");
        OrderDTO orderDTO = orderRequestDTO.getOrder();
        log.info(orderDTO.toString());
        List<OrderItemsDTO> orderItems = orderRequestDTO.getItems();
        log.info(orderItems.toString());
        txService.saveOrder(orderDTO, orderItems);
        return ResponseEntity.ok("success");
    }

    @GetMapping("/findManager")
    public String findManager(Model model) {
        List<Manager> managers = txService.getAllManagers();
        model.addAttribute("managers", managers);
        return "transaction/order/findManager";
    }

    @GetMapping("/findSupplier")
    public String findSupplier(Model model) {
        List<Supplier> suppliers = txService.getAllSuppliers();
        model.addAttribute("suppliers", suppliers);
        return "transaction/order/findSupplier";
    }

    @GetMapping("/addItems")
    public String addItems(Model model) {
        List<Item> items = txService.getOrderItems();
        model.addAttribute("items", items);
        return "transaction/order/addItems";
    }

    @GetMapping("/orderList")
    public String orderList(Model model) {
        List<Order> orders = txService.getOrderList();
        model.addAttribute("orders", orders);
        // 속한 품목명, 개수 표시해줄 방법 찾기...
        return "transaction/order/list";
    }

    @GetMapping("/orderDetail")
    public String orderDetail(@RequestParam String orderId, Model model) {
        Order order = txService.getOrderById(orderId);
        List<OrderItems> items = txService.getOrderedItems(orderId);
        model.addAttribute("order", order);
        model.addAttribute("items", items);
        return "transaction/order/detail";
    }

}