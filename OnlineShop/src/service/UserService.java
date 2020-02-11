package service;

import java.util.List;

import javax.persistence.Persistence;

import dao.UserDAO;
import model.User;

public class UserService {

	private UserDAO userDAO;

	public UserService() {
		try {
			userDAO = new UserDAO(Persistence.createEntityManagerFactory("OnlineShop"));
		} catch (Exception ex) {
			System.out.println(ex);
		}
	}

	public void addUser(User c) {
		userDAO.create(c);
	}

	public void updateUser(User u) {
		userDAO.update(u);
	}

	public List<User> getAllUsers() {
		return userDAO.findAll();
	}
	
	public List<User> loginUser(String user, String password) {
		return userDAO.find(user, password);
	}
	
	public List<User> loginUser(String user) {
		return userDAO.find(user);
	}
}
