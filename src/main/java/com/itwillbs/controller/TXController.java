package com.itwillbs.controller;

import com.itwillbs.config.security.util.SecurityUtil;
import com.itwillbs.domain.transaction.*;
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

    @GetMapping("/insertSale")
    public String insertSale(Model model) {
        model.addAttribute("today", LocalDate.now());
        String managerId = SecurityUtil.getUserId();
        model.addAttribute("id", managerId);
        String managerName = txService.getManagerName(managerId);
        model.addAttribute("name", managerName);
        return "transaction/sale/insert";
    }

    @ResponseBody
    @PostMapping("/saveSale")
    public String saveSale(@RequestBody SaleRequestDTO saleRequestDTO) {
        SaleDTO saleDTO = saleRequestDTO.getSale();
        log.info("saleDTO: " + saleDTO);
        List<SaleItemsDTO> saleItems = saleRequestDTO.getItems();
        log.info("saleItems: " + saleItems);
        // 담당자, 거래처 정보 DB 매치 확인
        if (txService.checkSaleValidation(saleDTO)) {
            txService.saveSale(saleDTO, saleItems);
            return "success";
        } else {
            return "mismatch";
        }
    }

    @GetMapping("/findFranchise")
    public String findFranchise(@RequestParam(required = false) String query, Model model) {
        if (query == null || query.isEmpty()) {
            query = null;
        }
        List<Franchise> franchises = txService.findFranchises(query);
        model.addAttribute("franchises", franchises);
        return "transaction/sale/findFranchise";
    }

    @GetMapping("/addSaleItems")
    public String addSaleItems(@RequestParam(required = false) String query, Model model) {
        if (query == null || query.isEmpty()) {
            query = null;
        }
        List<String> salableCode = Arrays.asList("FP", "PP");
        List<TxItemsDTO> items = txService.getTXItems(query, salableCode);
        log.info("items: " + items);
        model.addAttribute("items", items);
        return "transaction/sale/addItems";
    }

    @GetMapping("/saleList")
    public String getSaleList() {
        return "transaction/sale/list";
    }

    @ResponseBody
    @GetMapping("/saleInfo")
    public ResponseEntity<List<SaleDTO>> getSaleInfo() {
        List<SaleDTO> sales = txService.getSaleList();
        log.info("sales: " + sales);
        return ResponseEntity.ok(sales);
    }

    @GetMapping("/saleDetail")
    public String saleDetail(@RequestParam String saleId, Model model) {
        Sale sale = txService.getSaleById(saleId);
        List<SaleItems> items = txService.getSaledItems(sale);
        model.addAttribute("sale", sale);
        model.addAttribute("items", items);
        return "transaction/sale/detail";
    }

    @ResponseBody
    @GetMapping("/searchSales")
    public ResponseEntity<List<SaleDTO>> searchSales(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String franchiseName,
            @RequestParam(required = false) String orderDateStart,
            @RequestParam(required = false) String orderDateEnd,
            @RequestParam(required = false) String itemName,
            @RequestParam(required = false) String dueDateStart,
            @RequestParam(required = false) String dueDateEnd
    ) {
        log.info("TXController searchSales()");
        List<SaleDTO> sales = txService.searchSales(status, franchiseName, orderDateStart, orderDateEnd, itemName, dueDateStart, dueDateEnd);
        log.info(sales.toString());
        return ResponseEntity.ok(sales);
    }

    @ResponseBody
    @PostMapping("/cancelSale")
    public String cancelSale(@RequestParam String saleId) {
        log.info("TXController cancelSale()");
        txService.updateSaleStatus(saleId, "수주취소");
        return "success";
    }

    @ResponseBody
    @PostMapping("/completeSale")
    public String completeSale(@RequestParam String saleId) {
        log.info("TXController completeSale()");
        txService.updateSaleStatus(saleId, "수주완료");
        return "success";
    }

    @GetMapping("/saleForm")
    public String saleForm() {
        return "transaction/sale/saleform";
    }

    @ResponseBody
    @PostMapping("/updateSale")
    public String updateSale(@RequestBody SaleRequestDTO saleRequestDTO) {
        log.info("Controller updateSale()");
        SaleDTO saleDTO = saleRequestDTO.getSale();
        log.info("saleDTO: " + saleDTO);
        List<SaleItemsDTO> saleItems = saleRequestDTO.getItems();
        log.info("saleItems: " + saleItems);
        // 담당자, 거래처 정보 DB 매치 확인
        if (txService.checkSaleValidation(saleDTO)) {
            txService.updateSale(saleDTO, saleItems);
            return "success";
        } else {
            return "mismatch";
        }
    }

    @GetMapping("/insertShip")
    public String insertShip() {
        // '등록대상' 버튼으로 값 가져오기 전까지는 빈 화면
        return "transaction/shipment/insert";
    }

    @ResponseBody
    @PostMapping("/saveShip")
    public String saveShip(ShipmentDTO shipmentDTO) {
        txService.saveShip(shipmentDTO);
        return "success";
    }

    @GetMapping("/findToShip")
    public String findToShip(Model model) {
        List<SaleDTO> sales = txService.findToShip();
        model.addAttribute("toShip", sales);
        return "transaction/shipment/findToShip";
    }

    // 출하정보 조회 수주물품정보
    @ResponseBody
    @GetMapping("/getSaleItems")
    public ResponseEntity<List<SaleItemsDTO>> getSaleItems(String saleId) {
        List<SaleItemsDTO> saleItems = txService.getSaleItems(saleId);
        return ResponseEntity.ok(saleItems);
    }

    @GetMapping("/shipList")
    public String getShipList() {
        return "transaction/shipment/list";
    }

    @ResponseBody
    @GetMapping("/shipInfo")
    public ResponseEntity<List<ShipmentDTO>> getShipInfo() {
        List<ShipmentDTO> ships = txService.getShipList();
        log.info("ships: " + ships);
        return ResponseEntity.ok(ships);
    }

    @GetMapping("/shipDetail")
    public String shipDetail(@RequestParam String shipId, Model model) {
        ShipmentDTO shipment = txService.getShipmentDTOById(shipId);
        if (shipment.getStatus().equals("출하등록(검품요청)") && shipment.getQsStatus().equals("검품완료")) {
            log.info("변경전: " + shipment.getStatus());
            shipment.setStatus("출하등록(검품완료)");
            log.info("변경후: " +shipment.getStatus());

        }
        List<SaleItems> items = txService.getSaledItems(txService.getSaleById(shipment.getSaleId()));
        model.addAttribute("shipment", shipment);
        model.addAttribute("items", items);
        return "transaction/shipment/detail";
    }

    @ResponseBody
    @GetMapping("/searchShips")
    public ResponseEntity<List<ShipmentDTO>> searchShips(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String franchiseName,
            @RequestParam(required = false) String shipDateStart,
            @RequestParam(required = false) String shipDateEnd,
            @RequestParam(required = false) String itemName,
            @RequestParam(required = false) String dueDateStart,
            @RequestParam(required = false) String dueDateEnd
    ) {
        log.info("TXController searchSales()");
        List<ShipmentDTO> shipment = txService.searchShips(status, franchiseName, shipDateStart,
                shipDateEnd, itemName, dueDateStart, dueDateEnd);
        log.info(shipment.toString());
        return ResponseEntity.ok(shipment);
    }

    @ResponseBody
    @GetMapping("/syncShipStatus")
    public ResponseEntity<ShipmentDTO> syncShipStatus(@RequestParam String shipmentId) {
        ShipmentDTO shipmentDTO = txService.syncByShipmentId(shipmentId);
        return ResponseEntity.ok(shipmentDTO);
    }

    @ResponseBody
    @PostMapping("/cancelShip")
    public String cancelShip(@RequestParam String shipmentId) {
        txService.updateShipStatus(shipmentId, "출하취소");
        return "success";
    }

    @ResponseBody
    @PostMapping("/completeShip")
    public String completeShip(@RequestParam String shipmentId) {
        txService.updateShipStatus(shipmentId, "출하완료");
        return "success";
    }


}
