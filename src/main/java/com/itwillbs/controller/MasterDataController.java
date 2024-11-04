package com.itwillbs.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

import com.itwillbs.domain.masterdata.ItemSearchDTO;
import com.itwillbs.entity.Item;
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

	// Item Management
	// 목록 페이지
	@GetMapping("/items")
	public String listItems() {
		return "masterdata/items";
	}

	// 아이템 목록 조회 loadItems()
	@GetMapping("/api/items")
	@ResponseBody
	public Page<Item> getItems(ItemSearchDTO searchDto, @RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "10") int size) {
		return itemService.searchItems(searchDto, PageRequest.of(page, size));
	}

	// 아이템 상세 조회 loadItemDetail(itemCode)
	@GetMapping("/api/items/{itemCode}")
	@ResponseBody
	public ResponseEntity<Item> getItem(@PathVariable(name = "itemCode") String itemCode) {
		return itemService.findItemByCode(itemCode).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	}

	// 아이템 저장 saveItem() 
	@PostMapping("/api/items")
	@ResponseBody
	public ResponseEntity<Item> saveItem(@RequestBody @Validated Item item) {
		Item savedItem = itemService.saveItem(item);
		return ResponseEntity.ok(savedItem);
	}

	// 아이템 수정 saveItem() 
	@PutMapping("/api/items/{itemCode}")
	@ResponseBody
	public ResponseEntity<Item> updateItem(@PathVariable(name = "itemCode") String itemCode, @RequestBody @Validated Item item) {
		item.setItemCode(itemCode);
		Item updatedItem = itemService.updateItem(item);
		return ResponseEntity.ok(updatedItem);
	}

	// 아이템 삭제 deleteItem()
	@DeleteMapping("/api/items/{itemCode}")
	@ResponseBody
	public ResponseEntity<Void> deleteItem(@PathVariable(name = "itemCode") String itemCode) {
		itemService.deleteItem(itemCode);
		return ResponseEntity.ok().build();
	}

	// 코드 최대값+1 조회 updateItemCode()
	@GetMapping("/api/items/nextCode")
	@ResponseBody
	public String getNextCode(@RequestParam(name = "itemType") String itemType) {
	    return itemService.generateNextCode(itemType);
	}

//	// BOM Management
//	@GetMapping("/boms")
//	public String listBOMs() {
//		return "masterdata/boms";
//	}
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
//	// Supplier Management
//	@GetMapping("/suppliers")
//	public String listSuppliers() {
//		return "masterdata/suppliers";
//	}
//
//	@GetMapping("/suppliers/{id}")
//	public String supplierDetail(@PathVariable Long id) {
//		return "masterdata/supplier-detail";
//	}
//
//	// Franchise Management
//	@GetMapping("/franchises")
//	public String listFranchises() {
//		return "masterdata/franchises";
//	}
//
//	@GetMapping("/franchises/{id}")
//	public String franchiseDetail(@PathVariable Long id) {
//		return "masterdata/franchise-detail";
//	}
}
