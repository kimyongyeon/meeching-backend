package com.msa.study.meeching.service;

import com.msa.study.meeching.domain.entity.MemberEntity;
import com.msa.study.meeching.domain.vo.MemberVO;
import com.msa.study.meeching.domain.vo.RemoveEnum;
import com.msa.study.meeching.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class MemberService {

   @Autowired
   MemberRepository memberRepository;

   /**
    * 회원 가입
    * @param memberVO
    */
   public void join(MemberVO memberVO) {

      // 1. 유효성 체크

      // 2. 회원조회

      // 3. 회원 존재시 이미 존재한다는 throw 던짐

      // 4. 신규 회원이면 통과

      // 5. db insert 쿼리 호출

      // 6. db insert 실패시 rollback

      // 7. db insert 성공시 commit

      // 8. 결과 성공/실패 여부

      // 9. redirect로 첫페이지 전환 관련 서버에서 할지 화면에서 할지 결정

   }

   /**
    * 동일한 아이디 체크 유무
    * @param memberVO
    * @return
    */
   public boolean sameIdCheck(MemberVO memberVO) {
      // db 아이디 조회
      String userId = findByUserId(memberVO);
      // 이미 존재하는 아이디
      if (userId.equals(memberVO.getMemberEntity().getUserId())) {
         return true;
      }
      return false;
   }

   /**
    * 회원 비밀번호 업데이트
    * @param memberVO
    */
   public void passwordUpdate(MemberVO memberVO) {
      // db 아이디별 비밀번호 업데이트 호출
      MemberEntity memberEntity = memberRepository.findById(memberVO.getMemberEntity().getMemberId()).get();
      memberEntity.setUserId(memberVO.getMemberEntity().getUserId());
      memberEntity.setPassword(memberVO.getMemberEntity().getPassword());
      memberRepository.save(memberEntity);
   }

   /**
    * 회원 수정: 회원 아이디 수정도 가능 할것인가?
    * @param memberVO
    */
   public void edit(MemberVO memberVO) {
      // update call
      MemberEntity memberEntity = memberRepository.findById(memberVO.getMemberEntity().getMemberId()).get();
      memberEntity.setUserId(memberVO.getMemberEntity().getUserId());
      memberEntity.setPassword(memberVO.getMemberEntity().getPassword());
      memberEntity.setUserName(memberVO.getMemberEntity().getUserName());
      memberRepository.save(memberVO.getMemberEntity());
   }

   /**
    * 회원 탈퇴(삭제): 회원 삭제는 논리적인 삭제를 원칙으로 하되 필요시 용량확보를 위해 영구삭제 해야 함.
    * @param memberVO
    */
   public void remove(MemberVO memberVO) {
      // delete call
      if (RemoveEnum.LOGICAL_REMOVE == memberVO.getRemoveFlag()) {
         // 논리적인 삭제
         MemberEntity memberEntity = memberRepository.findById(memberVO.getMemberEntity().getMemberId()).get();
         memberEntity.setUseYn(false);
         memberRepository.save(memberEntity);
      } else {
         // 물리적인 삭제
         memberRepository.delete(memberVO.getMemberEntity());
      }
   }

   /**
    * 회원 조회
    * @param memberVO
    * @return
    */
   public String findByUserId(MemberVO memberVO) {
      // find call
      return memberRepository.findById(memberVO.getMemberEntity().getMemberId()).get().getUserId();
   }

}
