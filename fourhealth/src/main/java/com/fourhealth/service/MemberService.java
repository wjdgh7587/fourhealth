package com.fourhealth.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fourhealth.mapper.FoodMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MemberService {

	@Autowired
	private FoodMapper foodMapper;

}
