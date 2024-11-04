package com.itwillbs.service;

import org.springframework.stereotype.Service;

import com.itwillbs.repository.MFRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@Service
@RequiredArgsConstructor
@Log
public class MFService {
	
	private final MFRepository mfRepository;
	
}
