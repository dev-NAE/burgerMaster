package com.itwillbs.service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.itwillbs.domain.manufacture.MFBomDTO;
import com.itwillbs.domain.manufacture.MFOrderDTO;
import com.itwillbs.domain.manufacture.MFRmDTO;
import com.itwillbs.domain.manufacture.MFRmListDTO;
import com.itwillbs.entity.Item;
import com.itwillbs.entity.MFOrder;
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

	public List<MFOrderDTO> getOrderList(
			LocalDate searchDeadline,
			String searchState,
			String searchId,
			String searchName) {
		log.info("MFService getOrderList()");
		
		return mfRepository.findOrderList(searchDeadline, searchState, searchId, searchName);
	}
	
	public List<Item> getPPList(String searchId, String searchName){
		log.info("MFService getPPList()");
		
		return itemRepostiory.findByItemType(searchId, searchName);
	}

	public List<MFBomDTO> getRmList(List<MFBomDTO> list) {
		log.info("MFService getRmList()");
		
		StringBuilder sb = new StringBuilder();
		
		
		for(int i = 0; i<list.size(); i++) {
			
			sb.setLength(0);
			
			List<MFRmDTO> rmList = itemRepostiory.findRmList(list.get(i).getItemCode());
			
			for(int j = 0; j<rmList.size(); j++) {
				sb.append(rmList.get(j).toString());
				
				if(j==rmList.size()-1) {
					break;
				}
				sb.append(" / ");
			}
			
			list.get(i).setRmList(sb.toString());
		}
		
		return list;
	}
	
	public List<MFRmListDTO> getRM(String itemName){
		log.info("MFService getRM()");
		
		return itemRepostiory.findRM(itemName);
	}

	public void insertOrder(MFOrder order, String itemCode) {
		log.info("MFService insertOrder()");
		
		order.setItem(itemRepostiory.findById(itemCode).orElse(null));
		order.setOrderId(getNewOrderId());
		order.setOrderDate(new Timestamp(System.currentTimeMillis()));
		order.setOrderState("작업 전달 전");
		
		mfRepository.save(order);
	}
	
	public String getNewOrderId() {
		log.info("MFService getNewOrderId()");
		
		String maxId = mfRepository.findMaxId();
		
		if(maxId==null) {
			return "MO000001";
		} else {
			int num = Integer.parseInt(maxId.substring(2));
			
			String newId = String.format("MO%06d", num+1);
			log.info("new id generated -> "+newId);
			
			return newId;
		}
	}

	public boolean orderUpdate(String type, String key) {
		log.info("MFService orderUpdate()");
		
		if(type.equals("transmit")) {
			mfRepository.transmitOrder(key);
			log.info("MFService transmit");
			
			return true;
		}
		else if(type.equals("complete")) {
			mfRepository.completeOrder(key);
			log.info("MFService complete");
			
			return true;
		}
		
		return false;
	}
	
}
