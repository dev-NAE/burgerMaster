package com.itwillbs.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itwillbs.domain.masterdata.BOMDetailDTO;
import com.itwillbs.domain.masterdata.BOMListDTO;
import com.itwillbs.domain.masterdata.BOMSearchDTO;
import com.itwillbs.entity.BOM;
import com.itwillbs.entity.Item;
import com.itwillbs.repository.BOMRepository;
import com.itwillbs.repository.ItemRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
@Transactional(readOnly = true)
public class BOMService {
	private final BOMRepository bomRepository;
	private final ItemRepository itemRepository;

	public BOMService(BOMRepository bomRepository, ItemRepository itemRepository) {
		this.bomRepository = bomRepository;
		this.itemRepository = itemRepository;
	}

	public Page<BOMListDTO> getAllBOMs(BOMSearchDTO searchDTO, Pageable pageable) {
		return bomRepository.findBySearchConditions(searchDTO.getPpName(), searchDTO.getRmName(),
				searchDTO.getIncludeUnused(), pageable);
	}

	public Optional<BOMDetailDTO> getBOMDetail(Long bomId) {
		return bomRepository.findWithItemsById(bomId).map(BOMDetailDTO::new);
	}

	@Transactional
	public BOMDetailDTO saveBOM(BOM bom) {
		validateBOM(bom);
		return new BOMDetailDTO(bomRepository.save(bom));
	}

	@Transactional
	public BOMDetailDTO updateBOM(BOM bom) {
		validateBOM(bom);
		return new BOMDetailDTO(bomRepository.save(bom));
	}

	@Transactional
	public void deleteBOM(Long bomId) {
		bomRepository.deleteById(bomId);
	}

	private void validateBOM(BOM bom) {
		Item pp = itemRepository.findById(bom.getPpCode())
				.orElseThrow(() -> new EntityNotFoundException("가공품을 찾을 수 없습니다."));
		if (!"PP".equals(pp.getItemType())) {
			throw new IllegalArgumentException("가공품 타입이 아닙니다.");
		}

		Item rm = bom.getRawMaterial();
		if (!"RM".equals(rm.getItemType())) {
			throw new IllegalArgumentException("원재료 타입이 아닙니다.");
		}

		if (bomRepository.existsByPpCodeAndRawMaterial_ItemCode(bom.getPpCode(), rm.getItemCode())) {
			throw new IllegalStateException("이미 등록된 BOM입니다.");
		}
	}
}