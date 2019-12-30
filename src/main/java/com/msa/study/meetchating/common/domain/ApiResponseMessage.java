package com.msa.study.meetchating.common.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponseMessage {
   // HttpStatus
   private String status;
   // Http Default Message
   private String message;
   // Error Message to USER
   private String errorMessage;
   // Error Code
   private String errorCode;

   public ApiResponseMessage(String status, String message) {
      this.status = status;
      this.message = message;
   }
}
