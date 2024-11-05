package com.itwillbs.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;

import com.itwillbs.domain.masterdata.ItemSearchDTO;
import com.itwillbs.domain.masterdata.SupplierSearchDTO;
import com.itwillbs.entity.Item;
import com.itwillbs.entity.Supplier;
import com.itwillbs.service.BOMService;
import com.itwillbs.service.FranchiseService;
import com.itwillbs.service.ItemService;
import com.itwillbs.service.SupplierService;

@Controller
@RequestMapping("/masterdata")
public class MasterDataController {

	private final ItemService itemService;
	private final SupplierService supplierService;
	private final FranchiseService franchiseService;
	private final BOMService bomService;

	public MasterDataController(ItemService itemService, SupplierService supplierService,
			FranchiseService franchiseService, BOMService bomService) {
		this.itemService = itemService;
		this.supplierService = supplierService;
		this.franchiseService = franchiseService;
		this.bomService = bomService;
	}

	// Layout
	@GetMapping("/layout")
	public String layout() {
		return "masterdata/layout";
	}

	// 목록 페이지
	@GetMapping("/items")
	public String listItems() {
		return "masterdata/items";
	}

	@GetMapping("/boms")
	public String listBOMs() {
		return "masterdata/boms";
	}

	@GetMapping("/franchises")
	public String listFranchises() {
		return "masterdata/franchises";
	}

	@GetMapping("/suppliers")
	public String listSuppliers() {
		return "masterdata/suppliers";
	}

	// 목록 조회 loadItems()
	@GetMapping("/api/items")
	@ResponseBody
	public Page<Item> getItems(ItemSearchDTO searchDTO, @RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "10") int size) {
		return itemService.searchItems(searchDTO, PageRequest.of(page, size));
	}

	@GetMapping("/api/suppliers")
	@ResponseBody
	public Page<Supplier> getSuppliers(SupplierSearchDTO searchDTO,
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "10") int size) {
		return supplierService.searchItems(searchDTO, PageRequest.of(page, size));
	}

	// 상세 조회 loadItemDetail(itemCode) 200 404
	@GetMapping("/api/items/{itemCode}")
	@ResponseBody
	public ResponseEntity<Item> getItem(@PathVariable(name = "itemCode") String itemCode) {
		return itemService.findItemByCode(itemCode).map(ResponseEntity::ok)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
						String.format("품목코드 %s를 찾을 수 없습니다.", itemCode)));
	}

	@GetMapping("/api/suppliers/{supplierCode}")
	@ResponseBody
	public ResponseEntity<Supplier> getSupplier(@PathVariable(name = "supplierCode") String supplierCode) {
		return supplierService.findSupplierByCode(supplierCode).map(ResponseEntity::ok)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
						String.format("거래처코드 %s를 찾을 수 없습니다.", supplierCode)));
	}

	// 저장 saveItem() 201
	@PostMapping("/api/items")
	@ResponseBody
	public ResponseEntity<Item> saveItem(@RequestBody @Validated Item item) {
		return ResponseEntity.status(HttpStatus.CREATED).body(itemService.saveItem(item));
	}

	@PostMapping("/api/suppliers")
	@ResponseBody
	public ResponseEntity<Supplier> saveSupplier(@RequestBody @Validated Supplier supplier) {
		return ResponseEntity.status(HttpStatus.CREATED).body(supplierService.saveSupplier(supplier));
	}

	// 수정 saveItem() 200
	@PutMapping("/api/items/{itemCode}")
	@ResponseBody
	public ResponseEntity<Item> updateItem(@PathVariable(name = "itemCode") String itemCode,
			@RequestBody @Validated Item item) {
		if (!itemCode.equals(item.getItemCode())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "경로의 itemCode와 요청 본문의 itemCode가 일치하지 않습니다.");
		}
		return ResponseEntity.ok(itemService.updateItem(item));
	}

	@PutMapping("/api/suppliers/{supplierCode}")
	@ResponseBody
	public ResponseEntity<Supplier> updateSupplier(@PathVariable(name = "supplierCode") String supplierCode,
			@RequestBody @Validated Supplier supplier) {
		if (!supplierCode.equals(supplier.getSupplierCode())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"경로의 supplierCode와 요청 본문의 supplierCode가 일치하지 않습니다.");
		}
		return ResponseEntity.ok(supplierService.updateSupplier(supplier));
	}

	// 삭제 deleteItem() 204
	@DeleteMapping("/api/items/{itemCode}")
	@ResponseBody
	public ResponseEntity<Void> deleteItem(@PathVariable(name = "itemCode") String itemCode) {
		itemService.deleteItem(itemCode);
		return ResponseEntity.noContent().build();
	}

	@DeleteMapping("/api/suppliers/{supplierCode}")
	@ResponseBody
	public ResponseEntity<Void> deleteSupplier(@PathVariable(name = "supplierCode") String supplierCode) {
		supplierService.deleteSupplier(supplierCode);
		return ResponseEntity.noContent().build();
	}

	// 코드 최대값+1 조회 updateItemCode()
	@GetMapping("/api/items/nextCode")
	@ResponseBody
	public String getNextItemCode(@RequestParam(name = "itemType") String itemType) {
		return itemService.generateNextCode(itemType);
	}

	@GetMapping("/api/suppliers/nextCode")
	@ResponseBody
	public String getNextSupplierCode() {
		return supplierService.generateNextCode();
	}

//
//	@GetMapping("/boms/{id}")
//	public String bomDetail(@PathVariable Long id) {
//		return "masterdata/bom-detail";
//	}
//
//	@GetMapping("/boms/select")
//	public String selectBOM() {
//		return "masterdata/bom-select";
//	}
//
//
//	@GetMapping("/suppliers/{id}")
//	public String supplierDetail(@PathVariable Long id) {
//		return "masterdata/supplier-detail";
//	}
//

//
//	@GetMapping("/franchises/{id}")
//	public String franchiseDetail(@PathVariable Long id) {
//		return "masterdata/franchise-detail";
//	}
}
