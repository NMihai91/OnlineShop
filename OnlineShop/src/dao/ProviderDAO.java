package dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;

import model.Provider;;

public class ProviderDAO extends BasicDAO<Provider>{

	private EntityManagerFactory factory;

	public ProviderDAO(EntityManagerFactory factory) {
		super(Provider.class);
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
	public List<Provider> findById(String id) {
		EntityManager em = getEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Provider> q = cb.createQuery(Provider.class);

		Root<Provider> c = q.from(Provider.class);
		ParameterExpression<String> paramName = cb.parameter(String.class);
		q.select(c).where(cb.equal(c.get("providerId"), paramName));
		TypedQuery<Provider> query = em.createQuery(q);
		query.setParameter(paramName, id);

		List<Provider> results = query.getResultList();
		return results;
	}
}
