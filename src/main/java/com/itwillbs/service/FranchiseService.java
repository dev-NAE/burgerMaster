package com.itwillbs.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.itwillbs.domain.masterdata.FranchiseSearchDTO;
import com.itwillbs.entity.Franchise;
import com.itwillbs.repository.FranchiseRepository;

@Service
@Transactional(readOnly = true)
public class FranchiseService {
	private final FranchiseRepository franchiseRepository;

	public FranchiseService(FranchiseRepository franchiseRepository) {
		this.franchiseRepository = franchiseRepository;
	}

	public Page<Franchise> searchFranchises(FranchiseSearchDTO searchDTO, Pageable pageable) {
		return franchiseRepository.findBySearchConditions(searchDTO.getFranchiseName(), searchDTO.getOwnerName(),
				searchDTO.getBusinessNumber(), searchDTO.getContractStartDate(), searchDTO.getContractEndDate(),
				searchDTO.getIncludeUnused(), pageable);
	}

	public Optional<Franchise> findFranchiseByCode(String franchiseCode) {
		return franchiseRepository.findById(franchiseCode);
	}

	@Transactional
	public Franchise saveFranchise(Franchise franchise) {
		validateDuplicate(franchise);
		validateFranchise(franchise);
		return franchiseRepository.save(franchise);
	}

	@Transactional
	public Franchise updateFranchise(Franchise franchise) {
		validateFranchise(franchise);
		return franchiseRepository.save(franchise);
	}

	private void validateDuplicate(Franchise franchise) {
		if (franchiseRepository.existsById(franchise.getFranchiseCode())) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "이미 존재하는 가맹점코드입니다.");
		}

		if (franchiseRepository.existsByBusinessNumber(franchise.getBusinessNumber())) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "이미 등록된 사업자번호입니다.");
		}
	}

	private void validateFranchise(Franchise franchise) {
		if (franchise.getContractEndDate() != null
				&& franchise.getContractStartDate().isAfter(franchise.getContractEndDate())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "계약종료일은 계약시작일 이후여야 합니다.");
		}
	}

	@Transactional
	public void deleteFranchise(String franchiseCode) {
		franchiseRepository.deleteById(franchiseCode);
	}

	public String generateNextCode() {
		String maxCode = franchiseRepository.findMaxFranchiseCode();
		if (maxCode == null) {
			return "FRC001";
		}
		int nextNumber = Integer.parseInt(maxCode.substring(3)) + 1;
		return String.format("FRC%03d", nextNumber);
	}
}