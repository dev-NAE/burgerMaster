package com.itwillbs.service.quality;

import com.itwillbs.domain.quality.QualitySaleDTO;
import com.itwillbs.entity.QualityOrder;
import com.itwillbs.repository.QualityOrderRepository;
import com.itwillbs.repository.quality.QualitySaleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
@Log
public class QualityService {

	private final QualityOrderRepository qualityOrderRepository;
	private final QualitySaleRepository qualitySaleRepository;
	public List<QualityOrder> getDashBaord() {
		
		return qualityOrderRepository.findAll();
		
	}
	// 출고 검품 리스트 반환
	public List<QualitySaleDTO> getQualitySaleList() {
		return qualitySaleRepository.findQualitySaleList();
	}

	
	
	
	
	
	
}
