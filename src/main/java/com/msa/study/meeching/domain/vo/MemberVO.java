package com.msa.study.meeching.domain.vo;

import com.msa.study.meeching.domain.entity.MemberEntity;
import lombok.Data;

@Data
public class MemberVO {

   private MemberEntity memberEntity;

   // 0: 논리적인 삭제, 1: 물리적인 삭제
   private RemoveEnum removeFlag = RemoveEnum.LOGICAL_REMOVE;
}
