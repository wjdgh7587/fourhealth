package com.fourhealth.controller;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.fourhealth.dto.MemberDTO;
import com.fourhealth.dto.PromotionDTO;
import com.fourhealth.service.PromotionService;

@Controller
public class PromotionController {

	@Autowired
	private PromotionService promotionService;

	@GetMapping("/pro")
	public String promotionCheck(@RequestParam(name = "proId", required = false) String proId,
			HttpServletResponse response) throws IOException {

		System.out.println(proId);
		System.out.println(promotionService.promotionCheck(proId));
		String re = promotionService.promotionCheck(proId);
		int i = Integer.parseInt(re);
		if (i > 0) {
			return "promotion/promotionInsert";
		} else {
			response.setContentType("text/html; charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.println("<script>alert('정보를 확인해주세요.'); location.href='/';</script>");
			out.flush();
			return "redirect:/login";
		}
	}

	@PostMapping("/proInsert")
	public String promotionInsert(MultipartHttpServletRequest request, PromotionDTO promotionDto)
			throws ParseException {
		System.out.println(promotionDto);

		String fileName = null;

		if (!promotionDto.getProBgImage().isEmpty()) {
			fileName = promotionDto.getProBgImage().getOriginalFilename();
			String path = "C:\\Users\\ECS\\git\\fourhealth\\fourhealth\\src\\main\\resources\\static\\image\\"; // 패스 경로

			try {
				new File(path).mkdir();
				promotionDto.getProBgImage().transferTo(new File(path + fileName));

			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			promotionDto.setProImageUrl(fileName);
		}

		String start = promotionDto.getProAttendStartDate();
		String end = promotionDto.getProRecruitcloseDate();

		System.out.println("proInsert start-------------" + start);
		System.out.println("proInsert end-------------" + end);

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

		java.util.Date stDt = format.parse(start);
		java.util.Date edDt = format.parse(end);

		long diff = edDt.getTime() - stDt.getTime();
		long diffDays = diff / (24 * 60 * 60 * 1000);
		String totalDate = Long.toString(diffDays);

		System.out.println(diffDays);
		promotionDto.setProTotalDate(totalDate);

		System.out.println(promotionDto);

		promotionService.promotionInsert(promotionDto);

		return "redirect:/login";
	}

	@GetMapping("/proList")
	public String promotionList(Model model) {
		List<PromotionDTO> promotionList = promotionService.promotionList();
		System.out.println(promotionList);
		model.addAttribute("promotionList", promotionList);

		return "promotion/promotionList";
	}

	@GetMapping("/proDetail")
	public String proDetail(@RequestParam(name = "proCode", required = false) String proCode, Model model) {

		System.out.println(proCode);

		PromotionDTO promotionDto = promotionService.proDetail(proCode);
		System.out.println(promotionDto);
		model.addAttribute("proDetail", promotionDto);

		return "promotion/promotionDetail";
	}

	@PostMapping("/proPayment")
	public String proPayment(PromotionDTO promotionDTO, MemberDTO memberDTO, Model model) {
		System.out.println(promotionDTO);
		model.addAttribute("p", promotionDTO);

		return "promotion/promotionPayment";
	}
}
