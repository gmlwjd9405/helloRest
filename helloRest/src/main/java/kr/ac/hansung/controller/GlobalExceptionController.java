package kr.ac.hansung.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import kr.ac.hansung.exception.ErrorResponse;
import kr.ac.hansung.exception.UserDuplicatedException;
import kr.ac.hansung.exception.UserNotFoundException;

@ControllerAdvice
public class GlobalExceptionController {

	// ErrorResponse: 에러 발생 원인 정보를 넣어서 response msg body에 넘겨준다.
	// HttpServletRequest: request URL 정보를 얻기 위해서 사용한다.
	// UserNotFoundException: 넘어온 정보(Exception 증거)를 꺼내기 위해서 사용한다.
	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleUserNotFoundException(HttpServletRequest req, UserNotFoundException ex) {

		String requestURL = req.getRequestURL().toString();

		ErrorResponse errorResponse = new ErrorResponse();
		errorResponse.setRequestURL(requestURL);
		errorResponse.setErrorCode("user.notfound.exception");
		errorResponse.setErrorMsg("User with id " + ex.getUserId() + " not found");

		// Error에 대한 정보를 body에 넣어준다.
		// ErrorResponse Object가 json 형식으로 response msg body에 담긴다.
		return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(UserDuplicatedException.class)
	public ResponseEntity<ErrorResponse> handleUserDuplicatedException(HttpServletRequest req,
			UserDuplicatedException ex) {

		String requestURL = req.getRequestURL().toString();

		ErrorResponse errorResponse = new ErrorResponse();
		errorResponse.setRequestURL(requestURL);
		errorResponse.setErrorCode("user.duplicated.exception");
		errorResponse.setErrorMsg("Unable to create. A user with name " + ex.getUsername() + " already exist");

		return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.CONFLICT);
	}

}
