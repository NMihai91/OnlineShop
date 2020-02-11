package dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import model.Bill;

public class BillDAO extends BasicDAO<Bill> {

	private EntityManagerFactory factory;

	public BillDAO(EntityManagerFactory factory) {
		super(Bill.class);
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
