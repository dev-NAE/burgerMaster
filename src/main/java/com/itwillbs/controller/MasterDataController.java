package com.itwillbs.controller;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

	@GetMapping("/test")
	public String listItems() {
		return "masterdata/items/form";
	}
	
	// Item Management
	//getlists - list -검색 service 추가 필요
	@GetMapping("/items")
	public String listItems(Model model, ItemSearchDTO searchDto, @RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {
		Page<Item> items = itemService.searchItems(searchDto, PageRequest.of(page, size));
		model.addAttribute("items", items);
		model.addAttribute("searchDto", searchDto);
		return "masterdata/items/list";
	}

	//@getItem - detail
	@GetMapping("/items/{itemCode}")
	public String detailItem(@PathVariable String itemCode, Model model) {
		Optional<Item> item = itemService.findItemByCode(itemCode);
		model.addAttribute("item", item.get());
		return "masterdata/items/detail";
	}

	//@등록 빈 form
	@GetMapping("/items/new")
	public String newItemForm(Model model) {
		model.addAttribute("item", new Item());
		return "masterdata/items/form";
	}

	//@수정 시 조회 된 form
	@GetMapping("/items/{itemCode}/edit")
	public String editItemForm(@PathVariable String itemCode, Model model) {
		Optional<Item> item = itemService.findItemByCode(itemCode);
		model.addAttribute("item", item.get());
		return "masterdata/items/form";
	}

	//등록폼 select 선택 시 최대코드의 다음값 조회
	@GetMapping("/items/nextCode")
	public String getNextCode(@RequestParam String itemType) {
		return itemService.generateNextCode(itemType);
	}

	//@등록 Validated 및 bindingresult 고려하기 
	@PostMapping("/items")
    public String createItem(@ModelAttribute Item item) {
        Item savedItem = itemService.saveItem(item);
        return "redirect:/items/" + savedItem.getItemCode();
    }

	//@수정
    @PutMapping("/items/{itemCode}")
    public String updateItem(@PathVariable String itemCode, @ModelAttribute Item item) {
        item.setItemCode(itemCode);
        Item updatedItem = itemService.updateItem(item);
        return "redirect:/items/" + updatedItem.getItemCode();
    }

	//@삭제버튼 delete
	@DeleteMapping("/items/{itemCode}")
	public String deleteItem(@PathVariable String itemCode) {
		itemService.deleteItem(itemCode);
		return "redirect:/masterdata/items";
	}
	
	

	// BOM Management
	@GetMapping("/boms")
	public String listBOMs() {
		return "masterdata/boms";
	}

	@GetMapping("/boms/{id}")
	public String bomDetail(@PathVariable Long id) {
		return "masterdata/bom-detail";
	}

	@GetMapping("/boms/select")
	public String selectBOM() {
		return "masterdata/bom-select";
	}

	// Supplier Management
	@GetMapping("/suppliers")
	public String listSuppliers() {
		return "masterdata/suppliers";
	}

	@GetMapping("/suppliers/{id}")
	public String supplierDetail(@PathVariable Long id) {
		return "masterdata/supplier-detail";
	}

	// Franchise Management
	@GetMapping("/franchises")
	public String listFranchises() {
		return "masterdata/franchises";
	}

	@GetMapping("/franchises/{id}")
	public String franchiseDetail(@PathVariable Long id) {
		return "masterdata/franchise-detail";
	}
}
