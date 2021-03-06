package com.fourhealth.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.xml.crypto.Data;

import com.fourhealth.dto.MemberDto;
import com.fourhealth.dto.NoticePromotionTrainerDto;
import com.fourhealth.dto.SettlementAmountDto;
import com.fourhealth.dto.UserCouponDTO;
import com.fourhealth.mapper.PaymentMapper;
import com.fourhealth.mapper.PromotionMapper;
import com.fourhealth.service.MemberService;
import com.fourhealth.service.PaymentService;
import com.fourhealth.service.PromotionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class PaymentController {

    @Autowired
    PaymentService paymentService;

    @Autowired
    PromotionService promotionService;

    @Autowired
    MemberService memberService;

    @Autowired
    PaymentMapper paymentMapper;

    @Autowired
    PromotionMapper promotionMapper;

    @PostMapping("promotionPaymentCheck")
    public String promotionPaymentCheck(@RequestParam(name = "userId", required = false) String userId,
            @RequestParam(name = "promotionNoticeCode", required = false) String promotionNoticeCode,
            @RequestParam(name = "trainerPromotionRecruitEndDate", required = false) String trainerPromotionRecruitEndDate,
            HttpServletResponse response, Model model) throws IOException, ParseException {

        // html에서 받아온 파라미터 값 확인
        System.out.println(userId + "----------->로그인된 id값 가져오기");
        System.out.println(promotionNoticeCode + "----------->사용자가 클릭한 프로모션 코드값 가져오기");
        System.out.println(trainerPromotionRecruitEndDate + "---------------프로모션 모집 마감일자");

        // 유저가 최초 데이터를 작성했는지에 대한 체크
        String re = paymentService.checkPromotionPayment(userId);
        int a = Integer.parseInt(re);

        // 유저가 선택한 프로모션이 현재 인원이 가득 찾는지에 대한 데이터 추출
        NoticePromotionTrainerDto promotionDTO = promotionService.promotionDetail(promotionNoticeCode);
        int trainerPromotionLiveAddPeople = promotionDTO.getTrainerPromotionLiveAddPeople();
        int trainerPromotionRecruitPeople = promotionDTO.getTrainerPromotionRecruitPeople();

        // 유저가 동일한 프로모션에 또 결제하는지에 대한 체크
        int checkCount = paymentService.checkCountPromotionPayment(userId, promotionNoticeCode);

        // 유저가 프로모션에 참여중인가에 대한 체크
        String promotionCheck = paymentService.checkMatching(userId);
        if (promotionCheck == null) {
            promotionCheck = "0000-00-00";
        }
        System.out.println(promotionCheck);

        Date today = new Date();
        System.out.println(today + "-===============today");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        Date a1 = dateFormat.parse(promotionCheck);
        Date a2 = dateFormat.parse(trainerPromotionRecruitEndDate);

        System.out.println(a1 + "===================a1");
        System.out.println(a2 + "===================a2");

        int compare = today.compareTo(a1);
        // 유저가 선택한 프로모션이 기간이 지나지 않았는가에 대한 체크
        int compare2 = today.compareTo(a2);
        System.out.println(compare + "-------------------compare");
        System.out.println(compare2 + "================compare2");

        if (userId.equals("")) {
            response.setContentType("text/html; charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.println("<script>alert('로그인을 해주세요.'); location.href='/login';</script>");
            out.flush();
            return null;
        } else {
            if (a == 0) {
                // 만약 유저가 최초데이터를 작성 하지 않았다면
                response.setContentType("text/html; charset=UTF-8");
                PrintWriter out = response.getWriter();
                out.println("<script>alert('정보를 확인해 주세요.'); location.href='/promtion/promotionList';</script>");
                out.flush();
                return null;
            } else {
                // 만약 현재 모집기간이 끝나버렸다면
                if (compare2 > 0) {
                    response.setContentType("text/html; charset=UTF-8");
                    PrintWriter out = response.getWriter();
                    out.println("<script>alert('현재모집 기간이 끝났습니다.'); location.href='/promtion/promotionList';</script>");
                    out.flush();
                    return null;
                } else {
                    // 만약 유저가 최초 데이터를 작성했다면
                    if (trainerPromotionLiveAddPeople == trainerPromotionRecruitPeople) {
                        // 만약 현재 프로모션이 현재인원이 가득 차있다면
                        response.setContentType("text/html; charset=UTF-8");
                        PrintWriter out = response.getWriter();
                        out.println(
                                "<script>alert('현재 매칭인원이 꽉 차있습니다.'); location.href='/promtion/promotionList';</script>");
                        out.flush();
                        return null;
                    } else {
                        // 만약 현재 프로모션이 현재인원이 가득 차있지 않다면
                        if (checkCount > 0) {
                            // 같은프로모션에 결제를 시도한것이 아니라면
                            response.setContentType("text/html; charset=UTF-8");
                            PrintWriter out = response.getWriter();
                            out.println(
                                    "<script>alert('이미 참여하신 프로모션있습니다.'); location.href='/promtion/promotionList';</script>");
                            out.flush();
                            return null;
                        } else {
                            // 이미 프로모션에 참여중이라면
                            if (compare < 0) {
                                response.setContentType("text/html; charset=UTF-8");
                                PrintWriter out = response.getWriter();
                                out.println(
                                        "<script>alert('이미 프로모션에 참여중입니다.'); location.href='/promtion/promotionList';</script>");
                                out.flush();
                                return null;
                            } else {
                                List<UserCouponDTO> userCouponList = paymentService.userCouponList(userId);
                                MemberDto member = memberService.getMemberSelect(userId);
                                model.addAttribute("promotion", promotionDTO);
                                model.addAttribute("userCouponList", userCouponList);
                                model.addAttribute("member", member);
                                return "main_layout/promotion/promotionPayment";
                            }
                        }

                    }
                }

            }

        }

    }

    @RequestMapping(value = "trainer/promtion/promotionPaymentInsert", produces = "application/json", method = RequestMethod.POST)
    public @ResponseBody String promotionTest(@RequestBody Map<String, Object> map) {

        System.out.println("From FoodController >> Controller data 표시");
        System.out.println(map);

        String trainerPromotionNoticeCode = (String) map.get("trainerPromotionNoticeCode");
        int re = paymentMapper.promotionPaymentInsert(map);

        if (re > 0) {
            promotionMapper.updatePromotionLivePeopl(trainerPromotionNoticeCode);
        } else {
            return "매칭결제중 오류가 발생하였습니다.";
        }

        return "성공";

    }

    // 트레이너가 회원에게 운동 정보를 넣어주기 위해 사용되는 운동 데이터 검색 및 넣음.
    @RequestMapping(value = "/settlementAmount", method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody String settlementAmount(@RequestParam Map<String, Object> map) {
        System.out.println(map);
        List<SettlementAmountDto> settlementAmount = paymentMapper.settlementAmount(map);
        System.out.println(settlementAmount);

        String settlementAmountPay = settlementAmount.get(0).getSettlementAmountPay();
        System.out.println(settlementAmountPay);

        return settlementAmountPay;
    }

    @GetMapping("trainer/adjustment/adjustmentAccountInsert")
    public String adjustmentAccountInsert() {
        return "manage_layout/trainer/adjustment/adjustment_account_insert";
    }

    @GetMapping("trainer/adjustment/adjustmentAccountModify")
    public String adjustmentAccountModify() {
        return "manage_layout/trainer/adjustment/adjustment_account_modify";
    }

    @GetMapping("trainer/adjustment/adjustmentAccount")
    public String adjustmentAccount() {
        return "manage_layout/trainer/adjustment/adjustment_account";
    }

}
