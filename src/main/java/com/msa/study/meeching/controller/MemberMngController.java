package com.msa.study.meeching.controller;

import com.msa.study.meeching.common.domain.ApiResponseMessage;
import com.msa.study.meeching.domain.vo.MemberVO;
import com.msa.study.meeching.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 회원관리 REST 클래스
 */
@RestController
@RequestMapping("/member")
public class MemberMngController {

   @Autowired
   MemberService memberService;

   /**
    * 회원가입
    * @param memberVO
    * @호출방법 /member/join, requestBody {userId:''}
    * @RequestDataType json
    * @return
    */
   @PostMapping("/join")
   public ResponseEntity<ApiResponseMessage> join(@RequestBody MemberVO memberVO) {

      // 회원가입 트랜잭션 호출
      memberService.join(memberVO); // 에러나면 exceptionHandler 호출...
      ApiResponseMessage message
         = new ApiResponseMessage("Success", "회원가입 완료");
      return new ResponseEntity<ApiResponseMessage>(message, HttpStatus.CREATED);

   }

   /**
    * 회원 아이디 조회
    * @param memberVO
    * @호출방법 /member/findUserId?userId=test
    * @RequestDataType json
    * @return
    */
   @GetMapping("/findUserId")
   public ResponseEntity<ApiResponseMessage> findUserId(@ModelAttribute MemberVO memberVO) {

      // 회원아이디 조회 트랜잭션 호출
      String userId = memberService.findByUserId(memberVO);
      ApiResponseMessage message = new ApiResponseMessage("Success", userId);
      return new ResponseEntity<ApiResponseMessage>(message, HttpStatus.OK);

   }

   /**
    * 회원 탈퇴
    * @param memberVO
    *  @호출방법 /member/remove, requestBody {userId:''}
    * @RequestDataType json
    * @return
    */
   @DeleteMapping("/remove")
   public ResponseEntity<ApiResponseMessage> removeMember(@RequestBody MemberVO memberVO) {

      // 회원탈퇴 트랜잭션 호출
      memberService.remove(memberVO);
      ApiResponseMessage message = new ApiResponseMessage("Success", "회원삭제 성공");
      return new ResponseEntity<ApiResponseMessage>(message, HttpStatus.OK);
   }




}
