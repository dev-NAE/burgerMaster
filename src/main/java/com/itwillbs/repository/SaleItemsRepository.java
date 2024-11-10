package com.itwillbs.repository;

import com.itwillbs.domain.transaction.SaleItemsDTO;
import com.itwillbs.entity.Order;
import com.itwillbs.entity.OrderItems;
import com.itwillbs.entity.Sale;
import com.itwillbs.entity.SaleItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SaleItemsRepository extends JpaRepository<SaleItems, String> {

    List<SaleItems> findBySale(Sale sale);

    @Query("SELECT new com.itwillbs.domain.transaction.SaleItemsDTO" +
            "(si.item.itemCode, si.item.itemName, si.price, si.quantity, si.subtotalPrice) " +
            "FROM SaleItems si WHERE si.sale = :sale")
    List<SaleItemsDTO> findBySale2(@Param("sale") Sale sale);

    void deleteBySale(Sale sale);


}