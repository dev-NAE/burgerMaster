package com.itwillbs.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.itwillbs.entity.MFOrder;
import com.itwillbs.repository.MFRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@Service
@RequiredArgsConstructor
@Log
public class MFService {
	
	private final MFRepository mfRepository;

	public List<MFOrder> getOrderList() {
		log.info("MFService getOrderList()");
		
		return mfRepository.findAll();
	}
	
}
