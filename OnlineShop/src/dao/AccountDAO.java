package dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import model.Account;

public class AccountDAO extends BasicDAO<Account> {

	private EntityManagerFactory factory;

	public AccountDAO(EntityManagerFactory factory) {
		super(Account.class);
		this.factory = factory;
	}

	@Override
	public EntityManager getEntityManager() {
		try {
			return factory.createEntityManager();
		} catch (Exception ex) {
			System.out.println("The entity manager cannot be created!");
			return null;

		}
	}
}