package com.itwillbs.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.itwillbs.entity.QualityOrder;
import com.itwillbs.repository.QualityOrderRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;


@Service
@RequiredArgsConstructor
@Log
public class QualityOrderService {

	private final QualityOrderRepository qualityOrderRepository;
	public List<QualityOrder> getDashBaord() {
		
		return qualityOrderRepository.findAll();
		
	}

	
	
	
	
	
	
}
