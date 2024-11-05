package com.itwillbs.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.itwillbs.entity.MFOrder;

public interface MFRepository extends JpaRepository<MFOrder, String>{

}
