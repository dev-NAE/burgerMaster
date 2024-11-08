package com.itwillbs.service;

import com.itwillbs.domain.masterdata.BOMDetailDTO;
import com.itwillbs.domain.masterdata.BOMListDTO;
import com.itwillbs.domain.masterdata.BOMSaveDTO;
import com.itwillbs.domain.masterdata.BOMSearchDTO;
import com.itwillbs.entity.BOM;
import com.itwillbs.entity.Item;
import com.itwillbs.repository.BOMRepository;
import com.itwillbs.repository.ItemRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BOMService {
    private final BOMRepository bomRepository;
    private final ItemRepository itemRepository;

    public Page<BOMListDTO> getAllBOMs(BOMSearchDTO searchDTO, Pageable pageable) {
        return bomRepository.findBySearchConditions(
            searchDTO.getPpName(), 
            searchDTO.getRmName(),
            searchDTO.getIncludeUnused(), 
            pageable
        );
    }

    public Optional<BOMDetailDTO> getBOMDetail(Long bomId) {
        return bomRepository.findWithItemsById(bomId)
            .map(BOMDetailDTO::new);
    }

    @Transactional
    public BOMDetailDTO saveBOM(BOMSaveDTO saveDTO) {
        Item processedProduct = findItemByCode(saveDTO.getPpCode(), "PP");
        Item rawMaterial = findItemByCode(saveDTO.getRmCode(), "RM");

        validateUniqueBOM(processedProduct, rawMaterial);

        BOM bom = BOM.builder()
            .processedProduct(processedProduct)
            .rawMaterial(rawMaterial)
            .quantity(saveDTO.getQuantity())
            .useYN(saveDTO.getUseYN())
            .build();

        return new BOMDetailDTO(bomRepository.save(bom));
    }

    @Transactional
    public BOMDetailDTO updateBOM(Long bomId, BOMSaveDTO saveDTO) {
        BOM bom = bomRepository.findById(bomId)
            .orElseThrow(() -> new EntityNotFoundException("BOM을 찾을 수 없습니다."));

        Item processedProduct = findItemByCode(saveDTO.getPpCode(), "PP");
        Item rawMaterial = findItemByCode(saveDTO.getRmCode(), "RM");

        validateUniqueBOM(processedProduct, rawMaterial);

        bom.setProcessedProduct(processedProduct);
        bom.setRawMaterial(rawMaterial);
        bom.setQuantity(saveDTO.getQuantity());
        bom.setUseYN(saveDTO.getUseYN());

        return new BOMDetailDTO(bomRepository.save(bom));
    }

    @Transactional
    public void deleteBOM(Long bomId) {
        bomRepository.deleteById(bomId);
    }

    private Item findItemByCode(String itemCode, String expectedType) {
        Item item = itemRepository.findById(itemCode)
            .orElseThrow(() -> new EntityNotFoundException("품목을 찾을 수 없습니다."));
        
        if (!item.getItemType().equals(expectedType)) {
            throw new IllegalArgumentException("잘못된 품목 유형입니다.");
        }
        
        return item;
    }

    private void validateUniqueBOM(Item processedProduct, Item rawMaterial) {
        if (bomRepository.existsByProcessedProductAndRawMaterial(processedProduct, rawMaterial)) {
            throw new IllegalStateException("이미 등록된 BOM입니다.");
        }
    }
}