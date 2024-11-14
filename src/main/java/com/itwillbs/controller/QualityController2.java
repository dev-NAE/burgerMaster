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

    @GetMapping("/qsDetail")
    public String qsDetail(@RequestParam String qsId, Model model) {
        QualityShipmentDTO qsDTO = qsService.getQSShipmentDTOById(qsId);
        log.info("qsDTO: " + qsDTO);
        List<SaleItems> items = txService.getSaledItems(txService.getSaleById(qsDTO.getSaleId()));
        model.addAttribute("qsDTO", qsDTO);
        model.addAttribute("items", items);
        return "transaction/quality/detail";
    }

    @ResponseBody
    @GetMapping("/searchQS")
    public ResponseEntity<List<QualityShipmentDTO>> searchQS(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String franchiseName,
            @RequestParam(required = false) String shipDateStart,
            @RequestParam(required = false) String shipDateEnd,
            @RequestParam(required = false) String itemName,
            @RequestParam(required = false) String dueDateStart,
            @RequestParam(required = false) String dueDateEnd
    ) {
        log.info("QS2 Controller searchQS()");
        List<QualityShipmentDTO> qs = qsService.searchQS(status, franchiseName, shipDateStart,
                shipDateEnd, itemName, dueDateStart, dueDateEnd);
        log.info(qs.toString());
        return ResponseEntity.ok(qs);
    }

    @GetMapping("/findManager")
    public String findManager(@RequestParam(required = false) String query, Model model) {
        if (query == null || query.isEmpty()) {
            query = null;
        }
        List<Manager> managers = qsService.findManagers(query);
        model.addAttribute("managers", managers);
        return "transaction/quality/findManager";
    }

    @ResponseBody
    @PostMapping("/completeQS")
    public String completeShip(@RequestParam String qsId,
                               @RequestParam String manager,
                               @RequestParam(required = false) String note) {
        if (note == null || note.isEmpty()) {
            note = null;
        }
        qsService.updateQsStatus(qsId, manager, note);
        return "success";
    }

}
