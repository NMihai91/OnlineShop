package service;

import java.util.List;

import javax.persistence.Persistence;

import model.Account;
import dao.AccountDAO;

public class AccountService {
	private AccountDAO accountDAO;

	public AccountService() {
		try {
			accountDAO = new AccountDAO(Persistence.createEntityManagerFactory("OnlineShop"));
		} catch (Exception ex) {
			System.out.println(ex);
		}
	}

	public void addAccount(Account c) {
		accountDAO.create(c);
	}

	public void updateAccount(Account u) {
		accountDAO.update(u);
	}

	public List<Account> getAllAccounts() {
		return accountDAO.findAll();
	}
}