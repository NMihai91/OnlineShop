package dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;

import model.ProviderProduct;

public class ProviderProductDAO extends BasicDAO<ProviderProduct>{

	private EntityManagerFactory factory;

	public ProviderProductDAO(EntityManagerFactory factory) {
		super(ProviderProduct.class);
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
	
	public List<ProviderProduct> findById(String id) {
		EntityManager em = getEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<ProviderProduct> q = cb.createQuery(ProviderProduct.class);

		Root<ProviderProduct> c = q.from(ProviderProduct.class);
		ParameterExpression<String> paramName = cb.parameter(String.class);
		q.select(c).where(cb.equal(c.get("providerProductId"), paramName));
		TypedQuery<ProviderProduct> query = em.createQuery(q);
		query.setParameter(paramName, id);

		List<ProviderProduct> results = query.getResultList();
		return results;
	}
}
