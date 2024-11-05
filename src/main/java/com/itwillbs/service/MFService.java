package com.itwillbs.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.itwillbs.domain.manufacture.MFOrderDTO;
import com.itwillbs.entity.Item;
import com.itwillbs.entity.MFOrder;
import com.itwillbs.repository.BOMRepository;
import com.itwillbs.repository.ItemRepository;
import com.itwillbs.repository.MFRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@Service
@RequiredArgsConstructor
@Log
public class MFService {
	
	private final MFRepository mfRepository;
	private final ItemRepository itemRepostiory;

	public List<MFOrderDTO> getOrderList() {
		log.info("MFService getOrderList()");
		
		return mfRepository.findOrderList();
	}
	
	public List<Item> getPPList(){
		log.info("MFService getPPList()");
		
		return itemRepostiory.findByItemType();
	}
	
}
