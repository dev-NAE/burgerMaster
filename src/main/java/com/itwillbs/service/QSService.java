package com.itwillbs.service;

import com.itwillbs.domain.transaction.QualityShipmentDTO;
import com.itwillbs.domain.transaction.ShipmentDTO;
import com.itwillbs.entity.Manager;
import com.itwillbs.entity.QualityShipment;
import com.itwillbs.entity.Shipment;
import com.itwillbs.repository.ManagerRepository;
import com.itwillbs.repository.QualityShipmentRepository;
import com.itwillbs.repository.SaleRepository;
import com.itwillbs.repository.ShipmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Log
@RequiredArgsConstructor
@Service
public class QSService {

    private final ManagerRepository managerRepository;
    private final QualityShipmentRepository qualityShipmentRepository;
    private final SaleRepository saleRepository;

    public List<QualityShipmentDTO> getShipQualList() {
        List<QualityShipment> allShipQuals = qualityShipmentRepository.findAll(Sort.by(Sort.Direction.DESC, "qualityShipmentId"));
        log.info(allShipQuals.toString());
        return getQualShipmentDTOS(allShipQuals);
    }

    public List<QualityShipmentDTO> searchQS(String status, String franchiseName, String shipDateStart, String shipDateEnd,
                                         String itemName, String dueDateStart, String dueDateEnd) {
        log.info("QSService: searchQS");

        // 날짜 자료형 String -> Timestamp 변경
        Timestamp shipStart = convertToTimestamp(shipDateStart);
        Timestamp shipEnd = convertToTimestamp(shipDateEnd);
        Timestamp dueStart = convertToTimestamp(dueDateStart);
        Timestamp dueEnd = convertToTimestamp(dueDateEnd);

        String formattedStatus = (status != null && !status.trim().isEmpty()) ? status : null;

        // LIKE 검색할 것들 % 붙여주기
        String formattedFranchiseName = franchiseName != null && !franchiseName.trim().isEmpty() ? "%" + franchiseName + "%" : null;
        String formattedItemName = itemName != null && !itemName.trim().isEmpty() ? "%" + itemName + "%" : null;

        log.info("status: " + formattedStatus + " supplierName: " + formattedFranchiseName + " shipStart: " + shipStart + " shipEnd: " + shipEnd + " itemName: " + formattedItemName + " dueStart: " + dueStart + " dueEnd: " + dueEnd);

        List<QualityShipment> qsByConditions = qualityShipmentRepository.findQualityShipmentByConditions
                (formattedStatus, formattedFranchiseName, shipStart, shipEnd, formattedItemName, dueStart, dueEnd);

        log.info("QSService: qsByConditions" + qsByConditions);

        return getQualShipmentDTOS(qsByConditions);
    }

    private List<QualityShipmentDTO> getQualShipmentDTOS(List<QualityShipment> qualityShipmentsByConditions) {
        return qualityShipmentsByConditions.stream()
                .map(qs -> {
                    QualityShipmentDTO qsDTO = new QualityShipmentDTO();
                    qsDTO.setQualityShipmentId(qs.getQualityShipmentId());
                    qsDTO.setStatus(qs.getStatus());
                    if (qs.getManager() != null) {
                        qsDTO.setManagerId(qs.getManager().getManagerId());
                        qsDTO.setManagerName(qs.getManager().getName());
                    }
                    qsDTO.setShipmentId(qs.getShipment().getShipmentId());
                    qsDTO.setShipDate(qs.getShipDate());
                    qsDTO.setDueDate(qs.getSale().getDueDate());

                    qsDTO.setFranchiseName(saleRepository.findFranchiseNameBySaleId(qs.getSale().getSaleId()));
                    List<String> firstItem = saleRepository.findFirstItemNameBySale(qs.getSale());
                    qsDTO.setItemName(firstItem.isEmpty() ? null : firstItem.get(0));
                    qsDTO.setItemCount(saleRepository.findSaleItemCountBySale(qs.getSale()));

                    log.info("qsDTO: " + qsDTO);
                    return qsDTO;
                })
                .collect(Collectors.toList());
    }

    private Timestamp convertToTimestamp(String dateStr) {
        if (dateStr == null || dateStr.isEmpty()) {
            return null;
        }
        LocalDate dateTime = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        return Timestamp.valueOf(dateTime.atStartOfDay());
    }

    public QualityShipmentDTO getQSShipmentDTOById(String qsId) {
        return qualityShipmentRepository.getShipmentDTOById(qsId);
    }

    public List<Manager> findManagers(String managerName) {
        if (managerName != null) {
            managerName = "%" + managerName + "%";
        }
        return managerRepository.findManagerOnQuality(managerName);
    }

    public void updateQsStatus(String qsId, String manager, String note) {
        qualityShipmentRepository.updateQsStatus(qsId, manager, note);
    }
}
