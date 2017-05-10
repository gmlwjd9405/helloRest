package kr.ac.hansung.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Service;

import kr.ac.hansung.model.User;

//bean���� ��ϵȴ�.
@Service
public class UserService {

	// web app�� ��� ����ڰ� ���� ������ �ڵ����� thread�� ����.
	private static final AtomicLong counter = new AtomicLong(); // thread�� �����ϴ� id �ʵ带 ������Ű�� �޼���(��ȣ����)
	private static List<User> users; // DB�� ������� �����Ƿ� memory�� �ڷḦ �����Ѵ�.

	public UserService() {
		users = new ArrayList<User>();

		users.add(new User(counter.incrementAndGet(), "Sam", 30, 70000));
		users.add(new User(counter.incrementAndGet(), "Tom", 40, 50000));
		users.add(new User(counter.incrementAndGet(), "Jerome", 45, 30000));
		users.add(new User(counter.incrementAndGet(), "Silvia", 50, 40000));
	}

	/** ��� user���� �������� �޼��� */
	public List<User> findAllUsers() {
		return users;
	}

	/** ������ id�� �´� user�� �������� �޼��� */
	public User findById(long id) {
		for (User user : users) {
			if (user.getId() == id) {
				return user;
			}
		}
		return null;
	}

	/** ������ name�� �´� user�� �������� �޼��� */
	public User findByName(String name) {
		for (User user : users) {
			if (user.getName().equalsIgnoreCase(name)) {
				return user;
			}
		}
		return null;
	}

	/** id�� �Ҵ��Ͽ� List�� user�� �߰��ϴ� �޼��� */
	public void saveUser(User user) {
		user.setId(counter.incrementAndGet());
		users.add(user);
	}

	/** ������ user�� ������Ʈ�ϴ� �޼��� */
	public void updateUser(User user) {
		int index = users.indexOf(user);
		users.set(index, user);
	}

	/** ������ id�� �´� user�� �����ϴ� �޼��� */
	public void deleteUserById(long id) {
		for (Iterator<User> iterator = users.iterator(); iterator.hasNext();) {
			User user = iterator.next();
			if (user.getId() == id) {
				iterator.remove();
			}
		}
	}

	/** ������ user�� �����ϴ����� ���� ���θ� �����ϴ� �޼��� */
	public boolean isUserExist(User user) {
		return findByName(user.getName()) != null;
	}

	/** ��� user���� �����ϴ� �޼��� */
	public void deleteAllUsers() {
		users.clear();
	}

}
