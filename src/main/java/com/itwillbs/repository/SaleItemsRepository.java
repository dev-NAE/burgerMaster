package com.itwillbs.repository;

import com.itwillbs.entity.Order;
import com.itwillbs.entity.OrderItems;
import com.itwillbs.entity.Sale;
import com.itwillbs.entity.SaleItems;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SaleItemsRepository extends JpaRepository<SaleItems, String> {

    List<SaleItems> findBySale(Sale sale);

    void deleteBySale(Sale sale);

}