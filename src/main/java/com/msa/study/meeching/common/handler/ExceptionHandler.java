package com.msa.study.meeching.common.handler;

import com.msa.study.meeching.common.domain.vo.ApiResponseMessageVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;

/**
 * 공통 에러 관리 핸들러
 */
@ControllerAdvice
@Slf4j
public class ExceptionHandler {

   /**
    * 체크 에러 수집
    * @param exception
    * @return
    */
   @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
   public ResponseEntity<ApiResponseMessageVO> knownError(Exception exception) {
      log.error("알수없는에러: ", exception);
      ApiResponseMessageVO message
         = new ApiResponseMessageVO("Fail", "알수없는에러", exception.getMessage(), "ERR_001");
      return new ResponseEntity<ApiResponseMessageVO>(message, HttpStatus.EXPECTATION_FAILED);
   }

   /**
    * 언 체크 에러 수집
    * @param re
    * @return
    */
   @org.springframework.web.bind.annotation.ExceptionHandler(RuntimeException.class)
   public ResponseEntity<ApiResponseMessageVO> runTimeError(RuntimeException re) {
      log.error("런타임에러: ", re);
      ApiResponseMessageVO message
         = new ApiResponseMessageVO("Fail", "런타임에러", re.getMessage(), "ERR_002");
      return new ResponseEntity<ApiResponseMessageVO>(message, HttpStatus.EXPECTATION_FAILED);
   }

   /**
    * DB 관련 에러 수집
    * @param de
    * @return
    */
   @org.springframework.web.bind.annotation.ExceptionHandler(DataAccessException.class)
   public ResponseEntity<ApiResponseMessageVO> runTimeError(DataAccessException de) {
      log.error("데이터엑세스에러: ", de);
      ApiResponseMessageVO message
         = new ApiResponseMessageVO("Fail", "데이터베이스에러", de.getMessage(), "ERR_003");
      return new ResponseEntity<ApiResponseMessageVO>(message, HttpStatus.EXPECTATION_FAILED);
   }


}
