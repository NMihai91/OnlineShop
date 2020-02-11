package service;

import java.util.List;

import javax.persistence.Persistence;

import dao.BillProviderProductDAO;
import model.BillProviderProduct;

public class BillProviderProductService {

	private BillProviderProductDAO billProviderProductDAO;

	public BillProviderProductService() {
		try {
			billProviderProductDAO = new BillProviderProductDAO(Persistence.createEntityManagerFactory("OnlineShop"));
		} catch (Exception ex) {
			System.out.println(ex);
		}
	}

	public void addBillProviderProduct(BillProviderProduct c) {
		billProviderProductDAO.create(c);
	}

	public void updateBillProviderProduct(BillProviderProduct u) {
		billProviderProductDAO.update(u);
	}

	public List<BillProviderProduct> getAllBillProviderProducts() {
		return billProviderProductDAO.findAll();
	}
}
