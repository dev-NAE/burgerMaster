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
public class SupplierService {
	private final SupplierRepository supplierRepository;

	public SupplierService(SupplierRepository supplierRepository) {
		this.supplierRepository = supplierRepository;
	}

	public Page<Supplier> searchItems(SupplierSearchDTO searchDTO, Pageable pageable) {
		return supplierRepository.findBySearchConditions(searchDTO.getSupplierName(), searchDTO.getBusinessNumber(),
				searchDTO.getContactPerson(), searchDTO.getIncludeUnused(), pageable);
	}

	public Optional<Supplier> findSupplierByCode(String supplierCode) {
		return supplierRepository.findById(supplierCode);
	}

	@Transactional
	public Supplier saveSupplier(Supplier supplier) {
		validateSupplierCode(supplier);
		validateDuplicate(supplier);
		validateSupplier(supplier);
		return supplierRepository.save(supplier);
	}

	@Transactional
	public Supplier updateSupplier(Supplier supplier) {
		validateSupplierCode(supplier);
		validateSupplier(supplier);
		return supplierRepository.save(supplier);
	}

	private void validateSupplierCode(Supplier supplier) {
		if (!supplier.getSupplierCode().matches("^SUP\\d{3}$")) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"거래처코드는 SUP로 시작하고 3자리 숫자여야 합니다.");
		}
	}

	private void validateDuplicate(Supplier supplier) {
		if (supplierRepository.existsById(supplier.getSupplierCode())) {
			throw new ResponseStatusException(HttpStatus.CONFLICT,
					"이미 존재하는 거래처코드입니다.");
		}
	}

	private void validateSupplier(Supplier supplier) {
		if (!supplier.getSupplierName().matches("^[가-힣A-Za-z0-9\\s\\-\\_]+$")) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"거래처명에 허용되지 않는 문자가 포함되어 있습니다.");
		}

		if (!supplier.getBusinessNumber().matches("^\\d{3}-\\d{2}-\\d{5}$")) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"사업자번호 형식이 올바르지 않습니다.");
		}

		if (!supplier.getContactPerson().matches("^[가-힣A-Za-z\\s]+$")) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"담당자명에 허용되지 않는 문자가 포함되어 있습니다.");
		}

		if (supplier.getAddress().length() > 200) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"주소는 200자를 초과할 수 없습니다.");
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