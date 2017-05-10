package kr.ac.hansung.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import kr.ac.hansung.model.User;
import kr.ac.hansung.service.UserService;

@RestController // @Controller + @ResponseBody
@RequestMapping("/api") // class 수준에서 "/api" Mapping한다.
public class RestAPIController {

	// model에서 bean으로 등록한 User를 Injection한다.
	@Autowired
	UserService userService;

	/* api에 해당되는 method를 만든다. */
	/** --- Retrieve All Users */
	@RequestMapping(value = "/users", method = RequestMethod.GET)
	public ResponseEntity<List<User>> listAllUsers() { // HTTP.status, header, body(json)

		List<User> users = userService.findAllUsers(); // DB에서 넘겨받은 resource

		// 넘겨줄 user가 없으면
		if (users.isEmpty()) {
			// HTTP.status만 넘겨준다.
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<List<User>>(users, HttpStatus.OK);
	}

	/** --- Retrieve Single User */
	@RequestMapping(value = "/users/{id}", method = RequestMethod.GET)
	public ResponseEntity<User> getUser(@PathVariable("id") long id) {

		User user = userService.findById(id); // DB에서 넘겨받은 resource

		// 일치하는 user가 없으면
		if (user == null) {
			// to do list: Exception(다른 쪽으로 분기되어 밑으로 넘어가지 않는다.)
		}
		return new ResponseEntity<User>(user, HttpStatus.OK);
	}

	/** --- Create a User */
	// RequestMethod.POST: client가 입력한 정보가 request msg body에 json형식으로 넘어온다.
	// @RequestBody: json형식으로 넘어온 것을 Object로 자동으로 binding(converting)된다.
	// UriComponentsBuilder: 새로 생성된 user의 URI를 header에 넘겨준다.
	@RequestMapping(value = "/users", method = RequestMethod.POST)
	public ResponseEntity<Void> createUser(@RequestBody User user, UriComponentsBuilder ucBuilder) {

		// 이미 지정한 user가 있으면
		if (userService.isUserExist(user) == true) {
			// to do list: Exception(다른 쪽으로 분기되어 밑으로 넘어가지 않는다.)
		}
		userService.saveUser(user);

		// savaUser에 의해 증가된 id값을 가져와 {id}에 설정한 후 URI형태로 바꿔준다.
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(ucBuilder.path("/api/users/{id}").buildAndExpand(user.getId()).toUri());

		// body에는 아무것도 넣지 않고 새로 생성한 user의 URI를 header에 넣어준다.
		return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
	}
}
