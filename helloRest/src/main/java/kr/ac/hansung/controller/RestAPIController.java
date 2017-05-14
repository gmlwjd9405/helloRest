package kr.ac.hansung.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import kr.ac.hansung.exception.ErrorResponse;
import kr.ac.hansung.exception.UserDuplicatedException;
import kr.ac.hansung.exception.UserNotFoundException;
import kr.ac.hansung.model.User;
import kr.ac.hansung.service.UserService;

@RestController // @Controller + @ResponseBody
@RequestMapping("/api") // class ���ؿ��� "/api" Mapping�Ѵ�.
public class RestAPIController {

	// model���� bean���� ����� User�� Injection�Ѵ�.
	@Autowired
	UserService userService;

	/* api�� �ش�Ǵ� method�� �����. */
	/** --- Retrieve All Users */
	@RequestMapping(value = "/users", method = RequestMethod.GET)
	public ResponseEntity<List<User>> listAllUsers() { // HTTP.status, header, body(json)

		List<User> users = userService.findAllUsers(); // DB���� �Ѱܹ��� resource

		// �Ѱ��� user�� ������
		if (users.isEmpty()) {
			// HTTP.status�� �Ѱ��ش�.
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<List<User>>(users, HttpStatus.OK);
	}

	/** --- Retrieve Single User */
	@RequestMapping(value = "/users/{id}", method = RequestMethod.GET)
	public ResponseEntity<User> getUser(@PathVariable("id") long id) {

		User user = userService.findById(id); // DB���� �Ѱܹ��� resource

		// ��ġ�ϴ� user�� ������
		if (user == null) {
			// to do list: Exception(�ٸ� ������ �б�Ǿ� ������ �Ѿ�� �ʴ´�.)
			throw new UserNotFoundException(id);
		}
		return new ResponseEntity<User>(user, HttpStatus.OK);
	}

	/** --- Create a User */
	// RequestMethod.POST: client�� �Է��� ������ request msg body�� json�������� �Ѿ�´�.
	// @RequestBody: json�������� �Ѿ�� ���� Object�� �ڵ����� binding(converting)�ȴ�.
	// UriComponentsBuilder: ���� ������ user�� URI�� header�� �Ѱ��ش�.
	@RequestMapping(value = "/users", method = RequestMethod.POST)
	public ResponseEntity<Void> createUser(@RequestBody User user, UriComponentsBuilder ucBuilder) {

		// �̹� ������ user�� ������
		if (userService.isUserExist(user) == true) {
			// to do list: Exception(�ٸ� ������ �б�Ǿ� ������ �Ѿ�� �ʴ´�.)
			throw new UserDuplicatedException(user.getName());
		}
		userService.saveUser(user);

		// savaUser�� ���� ������ id���� ������ {id}�� ������ �� URI���·� �ٲ��ش�.
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(ucBuilder.path("/api/users/{id}").buildAndExpand(user.getId()).toUri());

		// body���� �ƹ��͵� ���� �ʰ� ���� ������ user�� URI�� header�� �־��ش�.
		return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
	}

	/** --- Update a User */
	// @RequestBody: json�������� �Ѿ�� user�� ������ ������ Object�� �ڵ� binding(converting)�ȴ�.
	// @PathVariable: Template Valuable({id})�� Parameter Value(long id)�� Binding �����ش�.
	@RequestMapping(value = "/users/{id}", method = RequestMethod.PUT)
	public ResponseEntity<User> updateUser(@PathVariable("id") long id, @RequestBody User user) {

		User currentUser = userService.findById(id); // DB���� �Ѱܹ��� resource

		// ������ user�� ������
		if (currentUser == null) {
			// to do list: Exception(�ٸ� ������ �б�Ǿ� ������ �Ѿ�� �ʴ´�.)
			throw new UserNotFoundException(id);
		}
		currentUser.setName(user.getName());
		currentUser.setAge(user.getAge());
		currentUser.setSalary(user.getSalary());

		userService.updateUser(currentUser);

		// ������ ������ body�� �־��ش�.
		return new ResponseEntity<User>(currentUser, HttpStatus.OK);
	}

	/** --- Delete a User */
	@RequestMapping(value = "/users/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Void> deleteUser(@PathVariable("id") long id) {

		User user = userService.findById(id); // DB���� �Ѱܹ��� resource

		// ������ user�� ������
		if (user == null) {
			// to do list: Exception(�ٸ� ������ �б�Ǿ� ������ �Ѿ�� �ʴ´�.)
			throw new UserNotFoundException(id);
		}
		userService.deleteUserById(id);

		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	/** --- Delete All Users */
	@RequestMapping(value = "/users", method = RequestMethod.DELETE)
	public ResponseEntity<Void> deleteAllUsers() {

		userService.deleteAllUsers();

		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

}
