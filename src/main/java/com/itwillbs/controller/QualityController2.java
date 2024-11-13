package com.itwillbs.controller;

import com.itwillbs.config.security.util.SecurityUtil;
import com.itwillbs.domain.transaction.*;
import com.itwillbs.entity.*;
import com.itwillbs.service.QSService;
import com.itwillbs.service.TXService;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Log
@RequestMapping("/quality")
public class QualityController2 {

    private final TXService txService;
    private final QSService qsService;

    @GetMapping("/qualityShipment")
    public String getList() {
        return "/transaction/quality/list";
    }

    @ResponseBody
    @GetMapping("/shipQualityInfo")
    public ResponseEntity<List<QualityShipmentDTO>> getQualShipmentDTOS() {
        List<QualityShipmentDTO> qualShips = qsService.getShipQualList();
        log.info("qualShips: " + qualShips);
        return ResponseEntity.ok(qualShips);
    }
//
//    @GetMapping("/insertShip")
//    public String insertShip() {
//        // '등록대상' 버튼으로 값 가져오기 전까지는 빈 화면
//        return "transaction/shipment/insert";
//    }
//
//    @ResponseBody
//    @PostMapping("/saveShip")
//    public String saveShip(ShipmentDTO shipmentDTO) {
//        txService.saveShip(shipmentDTO);
//        return "success";
//    }
//
//    @GetMapping("/findToShip")
//    public String findToShip(Model model) {
//        List<SaleDTO> sales = txService.findToShip();
//        model.addAttribute("toShip", sales);
//        return "transaction/shipment/findToShip";
//    }
//
//    // 출하정보 조회 수주물품정보
//    @ResponseBody
//    @GetMapping("/getSaleItems")
//    public ResponseEntity<List<SaleItemsDTO>> getSaleItems(String saleId) {
//        List<SaleItemsDTO> saleItems = txService.getSaleItems(saleId);
//        return ResponseEntity.ok(saleItems);
//    }
//
//
//
//
//    @GetMapping("/shipDetail")
//    public String shipDetail(@RequestParam String shipId, Model model) {
//        ShipmentDTO shipment = txService.getShipmentDTOById(shipId);
//        if (shipment.getStatus().equals("출하등록(검품요청)") && shipment.getQsStatus().equals("검품완료")) {
//            log.info("변경전: " + shipment.getStatus());
//            shipment.setStatus("출하등록(검품완료)");
//            log.info("변경후: " +shipment.getStatus());
//
//        }
//        List<SaleItems> items = txService.getSaledItems(txService.getSaleById(shipment.getSaleId()));
//        model.addAttribute("shipment", shipment);
//        model.addAttribute("items", items);
//        return "transaction/shipment/detail";
//    }
//
//    @ResponseBody
//    @GetMapping("/searchShips")
//    public ResponseEntity<List<ShipmentDTO>> searchShips(
//            @RequestParam(required = false) String status,
//            @RequestParam(required = false) String franchiseName,
//            @RequestParam(required = false) String shipDateStart,
//            @RequestParam(required = false) String shipDateEnd,
//            @RequestParam(required = false) String itemName,
//            @RequestParam(required = false) String dueDateStart,
//            @RequestParam(required = false) String dueDateEnd
//    ) {
//        log.info("TXController searchSales()");
//        List<ShipmentDTO> shipment = txService.searchShips(status, franchiseName, shipDateStart,
//                shipDateEnd, itemName, dueDateStart, dueDateEnd);
//        log.info(shipment.toString());
//        return ResponseEntity.ok(shipment);
//    }
//
//    @ResponseBody
//    @GetMapping("/syncShipStatus")
//    public ResponseEntity<ShipmentDTO> syncShipStatus(@RequestParam String shipmentId) {
//        ShipmentDTO shipmentDTO = txService.syncByShipmentId(shipmentId);
//        return ResponseEntity.ok(shipmentDTO);
//    }
//
//    @ResponseBody
//    @PostMapping("/cancelShip")
//    public String cancelShip(@RequestParam String shipmentId) {
//        txService.updateShipStatus(shipmentId, "출하취소");
//        return "success";
//    }
//
//    @ResponseBody
//    @PostMapping("/completeShip")
//    public String completeShip(@RequestParam String shipmentId) {
//        txService.updateShipStatus(shipmentId, "출하완료");
//        return "success";
//    }
//
//    @GetMapping("/invoiceForm")
//    public String invoiceForm() {
//        return "transaction/shipment/invoiceform";
//    }

}
