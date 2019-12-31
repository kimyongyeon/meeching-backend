package com.msa.study.meeching.common.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponseMessageVO {
   // HttpStatus
   private String status;
   // Http Default Message
   private String message;
   // Error Message to USER
   private String errorMessage;
   // Error Code
   private String errorCode;

   public ApiResponseMessageVO(String status, String message) {
      this.status = status;
      this.message = message;
   }
}
