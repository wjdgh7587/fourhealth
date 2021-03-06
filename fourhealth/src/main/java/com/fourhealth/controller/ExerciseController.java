package com.fourhealth.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fourhealth.dto.MetExerciseDto;
import com.fourhealth.mapper.ExerciseMapper;
import com.fourhealth.service.ExerciseService;

@Controller
public class ExerciseController {

	@Autowired
	private ExerciseService exerciseService;
	@Autowired
	private ExerciseMapper exerciseMapper;

	/* 관리자 운동DB 관리 페이지 맵핑 시작 */
	@GetMapping("master/exerciseManage/exerciseDataInsert")
	public String exerciseDataInsert(Model model) {
		return "manage_layout/master/exercise_manage/exercise_data_insert";
	}

	@GetMapping("master/exerciseManage/exerciseDataList")
	public String exerciseDataList(Model model) {

		model.addAttribute("exerciseList", exerciseMapper.getAllExerciseList2());

		return "manage_layout/master/exercise_manage/exercise_data_list";
	}

	@GetMapping("master/exerciseManage/exerciseDataModify")
	public String exerciseDataModify(Model model) {
		return "manage_layout/master/exercise_manage/exercise_data_modify";
	}
	/* 관리자 운동DB 관리 페이지 맵핑 끝 */

	@GetMapping("exercise/exercise")
	public String exercise(Model model) {
		List<MetExerciseDto> easyList = exerciseService.getAllEasyExerciseList();
		List<MetExerciseDto> NormalList = exerciseService.getAllNormalExerciseList();
		List<MetExerciseDto> HardList = exerciseService.getAllHardExerciseList();
		model.addAttribute("title", "Fourhealth exercise");
		model.addAttribute("easy", easyList);
		model.addAttribute("normal", NormalList);
		model.addAttribute("hard", HardList);

		return "main_layout/exercise/exercise";
	}

	// 전체 운동보기 (전체 접근가능)
	@GetMapping("/exercise/allExerciseList")
	public String allExerciseList(Model model) {
		List<MetExerciseDto> metExercise = exerciseService.getAllExerciseList();
		model.addAttribute("MetExercise", metExercise);
		return "main_layout/exercise/exercise_all_list";
	}

	// 운동검색 (전체 접근가능)
	@PostMapping("/exercise/serachExercise")
	public String serachExercise(Model model,
			@RequestParam(name = "exercise_name", required = false) String exerciseName,
			@RequestParam(name = "met_coefficient", required = false) String metCoefficient,
			@RequestParam(name = "currentPage", required = false, defaultValue = "1") int currentPage) {
		System.out.println("아이디" + exerciseName);
		System.out.println("검색조건" + metCoefficient);

		Map<String, Object> serachList = exerciseService.getAllSearchExerciseList(exerciseName, metCoefficient,
				currentPage);
		System.out.println(serachList);

		model.addAttribute("title", "Fourhealth serachExercise");

		model.addAttribute("exerciseName", exerciseName);
		model.addAttribute("metCoefficient", metCoefficient);

		model.addAttribute("exerciseList", serachList.get("exerciseList"));
		model.addAttribute("lastPage", serachList.get("lastPage"));
		model.addAttribute("currentPage", currentPage);
		model.addAttribute("startPageNum", serachList.get("startPageNum"));
		model.addAttribute("endPageNum", serachList.get("endPageNum"));

		return "main_layout/exercise/exercise_serach_list";
	}

	// 검색된 운동 페이지 동작(전체 접근가능)
	@GetMapping("/exercise/serachExercise")
	public String serachExercisePage(Model model,
			@RequestParam(name = "exercise_name", required = false) String exerciseName,
			@RequestParam(name = "met_coefficient", required = false) String metCoefficient,
			@RequestParam(name = "currentPage", required = false, defaultValue = "1") int currentPage) {
		System.out.println("아이디" + exerciseName);
		System.out.println("검색조건" + metCoefficient);

		Map<String, Object> serachList = exerciseService.getAllSearchExerciseList(exerciseName, metCoefficient,
				currentPage);
		System.out.println(serachList);

		model.addAttribute("title", "Fourhealth serachExercise");

		model.addAttribute("exerciseName", exerciseName);
		model.addAttribute("metCoefficient", metCoefficient);

		model.addAttribute("serachList", serachList);
		model.addAttribute("exerciseList", serachList.get("exerciseList"));
		model.addAttribute("lastPage", serachList.get("lastPage"));
		model.addAttribute("currentPage", currentPage);
		model.addAttribute("startPageNum", serachList.get("startPageNum"));
		model.addAttribute("endPageNum", serachList.get("endPageNum"));

		return "main_layout/exercise/exercise_serach_list";
	}

	// 운동별 상세정보(준비중)
	@GetMapping("/exercise/exerciseInfo")
	public String exerciseInfo(HttpServletResponse response) throws IOException {

		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.println("<script>alert('준비중입니다.');location.href='/exercise/exercise';</script>");
		out.flush();

		return null;
	}

}
