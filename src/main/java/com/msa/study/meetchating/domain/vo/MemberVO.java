package com.msa.study.meetchating.domain.vo;

import com.msa.study.meetchating.domain.MemberEntity;
import lombok.Data;

@Data
public class MemberVO {

   private MemberEntity memberEntity;

   // 0: 논리적인 삭제, 1: 물리적인 삭제
   private RemoveEnum removeFlag = RemoveEnum.LOGICAL_REMOVE;
}
