package dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import model.BillProviderProduct;

public class BillProviderProductDAO extends BasicDAO<BillProviderProduct>{

	private EntityManagerFactory factory;

	public BillProviderProductDAO(EntityManagerFactory factory) {
		super(BillProviderProduct.class);
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
