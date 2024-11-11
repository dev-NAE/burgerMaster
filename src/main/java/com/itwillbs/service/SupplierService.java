package com.itwillbs.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.itwillbs.domain.masterdata.SupplierSearchDTO;
import com.itwillbs.entity.Supplier;
import com.itwillbs.repository.SupplierRepository;

@Service
@Transactional(readOnly = true)
public class SupplierService {
	private final SupplierRepository supplierRepository;

	public SupplierService(SupplierRepository supplierRepository) {
		this.supplierRepository = supplierRepository;
	}

	public Page<Supplier> searchSuppliers(SupplierSearchDTO searchDTO, Pageable pageable) {
		return supplierRepository.findBySearchConditions(searchDTO.getSupplierName(), searchDTO.getBusinessNumber(),
				searchDTO.getContactPerson(), searchDTO.getIncludeUnused(), pageable);
	}

	public Optional<Supplier> findSupplierByCode(String supplierCode) {
		return supplierRepository.findById(supplierCode);
	}

	@Transactional
	public Supplier saveSupplier(Supplier supplier) {
		validateDuplicate(supplier);
		return supplierRepository.save(supplier);
	}

	@Transactional
	public Supplier updateSupplier(Supplier supplier) {
		return supplierRepository.save(supplier);
	}

	private void validateDuplicate(Supplier supplier) {
		if (supplierRepository.existsById(supplier.getSupplierCode())) {
			throw new ResponseStatusException(HttpStatus.CONFLICT,
					"이미 존재하는 거래처코드입니다.");
		}
		
		if (supplierRepository.existsByBusinessNumber(supplier.getBusinessNumber())) {
			throw new ResponseStatusException(HttpStatus.CONFLICT,
					"이미 존재하는 사업자번호입니다.");
		}
	}

	@Transactional
	public void deleteSupplier(String supplierCode) {
		supplierRepository.deleteById(supplierCode);
	}

	public String generateNextCode() {
		String maxCode = supplierRepository.findMaxSupplierCode();
		if (maxCode == null) {
			return "SUP001";
		}
		int nextNumber = Integer.parseInt(maxCode.substring(3)) + 1;
		return String.format("SUP%03d", nextNumber);
	}
}