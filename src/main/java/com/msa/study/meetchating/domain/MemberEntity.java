package com.msa.study.meetchating.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
public class MemberEntity {
   @Id @GeneratedValue
   private long memberId;
   private String userId;
   private String password;
   private String userName;
   private boolean useYn;
}
