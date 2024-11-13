package com.itwillbs.service;

import com.itwillbs.domain.transaction.QualityShipmentDTO;
import com.itwillbs.domain.transaction.ShipmentDTO;
import com.itwillbs.entity.QualityShipment;
import com.itwillbs.entity.Shipment;
import com.itwillbs.repository.QualityShipmentRepository;
import com.itwillbs.repository.SaleRepository;
import com.itwillbs.repository.ShipmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Log
@RequiredArgsConstructor
@Service
public class QSService {

    private final ShipmentRepository shipmentRepository;
    private final QualityShipmentRepository qualityShipmentRepository;
    private final SaleRepository saleRepository;

    public List<QualityShipmentDTO> getShipQualList() {
        List<QualityShipment> allShipQuals = qualityShipmentRepository.findAll(Sort.by(Sort.Direction.DESC, "qualityShipmentId"));
        log.info(allShipQuals.toString());
        return getQualShipmentDTOS(allShipQuals);
    }

    private List<QualityShipmentDTO> getQualShipmentDTOS(List<QualityShipment> qualityShipmentsByConditions) {
        return qualityShipmentsByConditions.stream()
                .map(qs -> {
                    QualityShipmentDTO qsDTO = new QualityShipmentDTO();
                    qsDTO.setQualityShipmentId(qs.getQualityShipmentId());
                    qsDTO.setShipmentId(qs.getShipment().getShipmentId());
                    qsDTO.setShipDate(qs.getShipDate());
                    qsDTO.setDueDate(qs.getSale().getDueDate());

                    qsDTO.setFranchiseName(saleRepository.findFranchiseNameBySaleId(qs.getSale().getSaleId()));
                    List<String> firstItem = saleRepository.findFirstItemNameBySale(qs.getSale());
                    qsDTO.setItemName(firstItem.isEmpty() ? null : firstItem.get(0));
                    qsDTO.setItemCount(saleRepository.findSaleItemCountBySale(qs.getSale()));

                    log.info("TXService: qsDTO: " + qsDTO);
                    return qsDTO;
                })
                .collect(Collectors.toList());
    }



}
