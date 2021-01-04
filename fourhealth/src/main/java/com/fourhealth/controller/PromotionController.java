package com.fourhealth.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import org.springframework.ui.Model;
import org.springframework.util.ResourceUtils;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.fourhealth.dto.NoticePromotionTrainerDto;
import com.fourhealth.service.PromotionService;

@Controller
public class PromotionController {

	@Autowired
	private PromotionService promotionService;

	// 트레이너 프로모션 등록전 최초데이터 체크컨트롤러
	@GetMapping("/promotionCheck")
	public String promotionCheck(@RequestParam(name = "proId", required = false) String proId,
			HttpServletResponse response) throws IOException {

		System.out.println(proId);
		System.out.println(promotionService.promotionCheck(proId));
		String re = promotionService.promotionCheck(proId);
		int i = Integer.parseInt(re);
		if (i > 0) {
			return "main_layout/promotion/promotionInsert";
		} else {
			response.setContentType("text/html; charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.println("<script>alert('정보를 확인해주세요.'); location.href='/';</script>");
			out.flush();
			return "/";
		}
	}

	// 트레이너 프로모션 등록컨트롤러
	@PostMapping("/promotionInsert")
	public String trainerPromotionInsert(MultipartHttpServletRequest request, NoticePromotionTrainerDto promotionDto)
			throws ParseException, FileNotFoundException {
		System.out.println(promotionDto);

		String fileName = null;

		if (!promotionDto.getTrainerPromotionBgImage().isEmpty()) {
			int rdv = (int) (Math.random() * 1000);
			fileName = promotionDto.getTrainerPromotionBgImage().getOriginalFilename();
			String rename = rdv + "_" + fileName;
			// String path =
			// "C:\\Users\\ECS\\Documents\\GitHub\\fourhealth\\fourhealth\\src\\main\\resources\\static\\image\\";

			String realPath = ResourceUtils.getFile("src/main/resources/static/image/" + rename).getAbsolutePath();
			// 배포패스

			try {
				new File(realPath).mkdir();
				promotionDto.getTrainerPromotionBgImage().transferTo(new File(realPath));

			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			promotionDto.setProImageUrl(rename);
		}

		String start = promotionDto.getTrainerPromotionAttendStartDate();
		String end = promotionDto.getTrainerPromotionRecruitCloseDate();

		System.out.println("proInsert start-------------" + start);
		System.out.println("proInsert end-------------" + end);

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

		java.util.Date stDt = format.parse(start);
		java.util.Date edDt = format.parse(end);

		long diff = edDt.getTime() - stDt.getTime();
		long diffDays = diff / (24 * 60 * 60 * 1000);
		String totalDate = Long.toString(diffDays);

		System.out.println(diffDays);
		promotionDto.setTrainerPromotionRecruitTotalDate(totalDate);

		System.out.println(promotionDto);

		promotionService.promotionInsert(promotionDto);

		return "redirect:/promotionList";
	}

	// 트레이너 프로모션 전체리스트 컨트롤러(회원이 보는거 )공통
	@GetMapping("/promotionList")
	public String commonPromotionList(Model model,
			@RequestParam(name = "currentPage", required = false, defaultValue = "1") int currentPage) {

		model.addAttribute("title", "프로모션목록");

		Map<String, Object> resultMap = promotionService.getPromotionListPaging(currentPage);

		model.addAttribute("promotionList", resultMap.get("promotionList"));
		model.addAttribute("lastPage", resultMap.get("lastPage"));
		model.addAttribute("currentPage", currentPage);
		model.addAttribute("startPageNum", resultMap.get("startPageNum"));
		model.addAttribute("endPageNum", resultMap.get("endPageNum"));

		// 페이징없이 프로모션 전체화면 보기
		// List<NoticePromotionTrainerDto> promotionList =
		// promotionService.promotionList();
		// System.out.println(promotionList);
		// model.addAttribute("promotionList", promotionList);

		return "main_layout/promotion/promotionList";
	}

	// 트레이너 프로모션 상세정보 컨트롤러
	@GetMapping("/promotionDetail")
	public String commonPromotionDetail(@RequestParam(name = "proCode", required = false) String proCode, Model model) {

		System.out.println(proCode);

		NoticePromotionTrainerDto promotionDto = promotionService.promotionDetail(proCode);
		System.out.println(promotionDto);
		model.addAttribute("proDetail", promotionDto);

		return "main_layout/promotion/promotionDetail";
	}

}
