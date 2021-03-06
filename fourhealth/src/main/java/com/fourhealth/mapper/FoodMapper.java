package com.fourhealth.mapper;

import java.util.List;
import java.util.Map;

import com.fourhealth.dto.Food;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FoodMapper {
	public int insertFood(Food food);

	public int getFoodListCount();

	// Food Master Page List
	public  List<Map<String, Object>> getFoodListBoard(int startRow, int rowPerPage);

	// Food Search Page get Last Nubmer
	public int getAllSearchLastFoodList(String foodName, String foodGroup);

	// After seraching food with paging
	public List<Map<String, Object>> getAllSearchFoodList(String foodName, String foodGroup,
	int startRow, int rowPerPage);

	//Food List 
	public List<Food> getFoodList();

	//Food Group List
	public List<Food> getFoodGroupList();

	//public int insertFood2(Map<String, String> data);

	//get Map tpye Object
	public int insertFoodMapInformation(Map<String, Object> data);

	//get List type Object
	public int insertFoodListInformation(List<Map<String, Object>> data);

	

}
