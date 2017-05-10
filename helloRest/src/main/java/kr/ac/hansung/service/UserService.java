package kr.ac.hansung.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Service;

import kr.ac.hansung.model.User;

//bean으로 등록된다.
@Service
public class UserService {

	// web app의 경우 사용자가 들어올 때마다 자동으로 thread를 생성.
	private static final AtomicLong counter = new AtomicLong(); // thread가 공유하는 id 필드를 증가시키는 메서드(상호배제)
	private static List<User> users; // DB를 사용하지 않으므로 memory상에 자료를 저장한다.

	public UserService() {
		users = new ArrayList<User>();

		users.add(new User(counter.incrementAndGet(), "Sam", 30, 70000));
		users.add(new User(counter.incrementAndGet(), "Tom", 40, 50000));
		users.add(new User(counter.incrementAndGet(), "Jerome", 45, 30000));
		users.add(new User(counter.incrementAndGet(), "Silvia", 50, 40000));
	}

	/** 모든 user들을 가져오는 메서드 */
	public List<User> findAllUsers() {
		return users;
	}

	/** 지정한 id에 맞는 user를 가져오는 메서드 */
	public User findById(long id) {
		for (User user : users) {
			if (user.getId() == id) {
				return user;
			}
		}
		return null;
	}

	/** 지정한 name에 맞는 user를 가져오는 메서드 */
	public User findByName(String name) {
		for (User user : users) {
			if (user.getName().equalsIgnoreCase(name)) {
				return user;
			}
		}
		return null;
	}

	/** id를 할당하여 List에 user를 추가하는 메서드 */
	public void saveUser(User user) {
		user.setId(counter.incrementAndGet());
		users.add(user);
	}

	/** 지정한 user를 업데이트하는 메서드 */
	public void updateUser(User user) {
		int index = users.indexOf(user);
		users.set(index, user);
	}

	/** 지정한 id에 맞는 user를 삭제하는 메서드 */
	public void deleteUserById(long id) {
		for (Iterator<User> iterator = users.iterator(); iterator.hasNext();) {
			User user = iterator.next();
			if (user.getId() == id) {
				iterator.remove();
			}
		}
	}

	/** 지정한 user가 존재하는지에 대한 여부를 리턴하는 메서드 */
	public boolean isUserExist(User user) {
		return findByName(user.getName()) != null;
	}

	/** 모든 user들을 삭제하는 메서드 */
	public void deleteAllUsers() {
		users.clear();
	}

}
