package com.itwillbs.controller;

import com.itwillbs.config.security.util.SecurityUtil;
import com.itwillbs.domain.transaction.OrderDTO;
import com.itwillbs.domain.transaction.OrderItemsDTO;
import com.itwillbs.domain.transaction.OrderRequestDTO;
import com.itwillbs.domain.transaction.TxItemsDTO;
import com.itwillbs.entity.*;
import com.itwillbs.service.TXService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Arrays;
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
    public String insertOrder(Model model) {
        model.addAttribute("today", LocalDate.now());

        // 접속자 id, name 가져와서 model에 담기
        String managerId = SecurityUtil.getUserId();

        if (managerId != null) {
            managerId = "admin";
        } // 완성 전 테스트용 임시 조치. 이후 삭제.

        model.addAttribute("id", managerId);
        String managerName = txService.getManagerName(managerId);
        model.addAttribute("name", managerName);
        return "transaction/order/insert";
    }

    @ResponseBody
    @PostMapping("/saveOrder")
    public String saveOrder(@RequestBody OrderRequestDTO orderRequestDTO) {
        log.info("Controller saveOrder()");
        OrderDTO orderDTO = orderRequestDTO.getOrder();
        log.info("orderDTO: " + orderDTO);
        List<OrderItemsDTO> orderItems = orderRequestDTO.getItems();
        log.info("orderItems: " + orderItems);
        // 담당자, 거래처 정보 DB 매치 확인
        if (txService.checkValidation(orderDTO)) {
            txService.saveOrder(orderDTO, orderItems);
            return "success";
        } else {
            return "mismatch";
        }
    }

    @GetMapping("/findManager")
    public String findManager(@RequestParam(required = false) String query, Model model) {
        if (query == null || query.isEmpty()) {
            query = null;
        }
        List<Manager> managers = txService.findManagers(query);
        model.addAttribute("managers", managers);
        return "transaction/order/findManager";
    }

    @GetMapping("/findSupplier")
    public String findSupplier(@RequestParam(required = false) String query, Model model) {
        if (query == null || query.isEmpty()) {
            query = null;
        }
        List<Supplier> suppliers = txService.findSuppliers(query);
        model.addAttribute("suppliers", suppliers);
        return "transaction/order/findSupplier";
    }

    @GetMapping("/addOrderItems")
    public String addOrderItems(@RequestParam(required = false) String query, Model model) {
        if (query == null || query.isEmpty()) {
            query = null;
        }
        List<String> orderableCode = Arrays.asList("FP", "RM");
        List<TxItemsDTO> items = txService.getTXItems(query, orderableCode);
        log.info("items: " + items);
        model.addAttribute("items", items);
        return "transaction/order/addItems";
    }

    @GetMapping("/orderList")
    public String getOrderList() {
        return "transaction/order/list";
    }

    @ResponseBody
    @GetMapping("/orderInfo")
    public ResponseEntity<List<OrderDTO>> getOrderInfo() {
        List<OrderDTO> orders = txService.getOrderList();
        log.info("orders: " + orders);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/orderDetail")
    public String orderDetail(@RequestParam String orderId, Model model) {
        Order order = txService.getOrderById(orderId);
        List<OrderItems> items = txService.getOrderedItems(order);
        model.addAttribute("order", order);
        model.addAttribute("items", items);
        return "transaction/order/detail";
    }

    @ResponseBody
    @GetMapping("/searchOrders")
    public ResponseEntity<List<OrderDTO>> searchOrders(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String supplierName,
            @RequestParam(required = false) String orderDateStart,
            @RequestParam(required = false) String orderDateEnd,
            @RequestParam(required = false) String itemName,
            @RequestParam(required = false) String dueDateStart,
            @RequestParam(required = false) String dueDateEnd
    ) {
        log.info("TXController searchOrders()");
        List<OrderDTO> orders = txService.searchOrders(status, supplierName, orderDateStart, orderDateEnd, itemName, dueDateStart, dueDateEnd);
        log.info(orders.toString());
        return ResponseEntity.ok(orders);
    }

    @ResponseBody
    @PostMapping("/cancelOrder")
    public String cancelOrder(@RequestParam String orderId) {
        log.info("TXController cancelOrder()");
        txService.updateOrderStatus(orderId, "발주취소");
        return "success";
    }

    @ResponseBody
    @PostMapping("/completeOrder")
    public String completeOrder(@RequestParam String orderId) {
        log.info("TXController completeOrder()");
        txService.updateOrderStatus(orderId, "발주완료");
        return "success";
    }

    @GetMapping("/orderForm")
    public String orderForm() {
        return "transaction/order/orderform";
    }

    @ResponseBody
    @PostMapping("/updateOrder")
    public String updateOrder(@RequestBody OrderRequestDTO orderRequestDTO) {
        log.info("Controller updateOrder()");
        OrderDTO orderDTO = orderRequestDTO.getOrder();
        log.info("orderDTO: " + orderDTO);
        List<OrderItemsDTO> orderItems = orderRequestDTO.getItems();
        log.info("orderItems: " + orderItems);
        // 담당자, 거래처 정보 DB 매치 확인
        if (txService.checkValidation(orderDTO)) {
            txService.updateOrder(orderDTO, orderItems);
            return "success";
        } else {
            return "mismatch";
        }
    }



}