package com.msa.study.meeching.domain.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
public class MemberEntity {
   @Id @GeneratedValue(strategy = GenerationType.AUTO)
   private long memberId;
   private String userId;
   private String password;
   private String userName;
   private boolean useYn;
}
