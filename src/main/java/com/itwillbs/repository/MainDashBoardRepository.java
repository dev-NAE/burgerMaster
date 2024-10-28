package com.itwillbs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.itwillbs.entity.Item;


@Repository
public interface MainDashBoardRepository extends JpaRepository<Item, String> {

}//
