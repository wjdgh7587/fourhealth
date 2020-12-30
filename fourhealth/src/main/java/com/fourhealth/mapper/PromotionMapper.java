package com.fourhealth.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.fourhealth.dto.MatchingUserTrainerDto;
import com.fourhealth.dto.NoticePromotionTrainerDto;
import com.fourhealth.dto.UserDto;


@Mapper
public interface PromotionMapper {
	// 트레이너 프로모션 입력
	public int promotionInsert(NoticePromotionTrainerDto promotionDto);

	// 트레이너 프로모션 입력 전 체크
	public String promotionCheck(String proId);

	// 트레이너 프로모션 전체리스트
	public List<NoticePromotionTrainerDto> promotionList();

	// 트레이너 프로모션 상세정보
	public NoticePromotionTrainerDto proDetail(String proCode);
	
	
}
