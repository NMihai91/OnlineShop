package service;

import java.util.List;

import javax.persistence.Persistence;

import dao.BillDAO;
import model.Bill;

public class BillService {

	private BillDAO billDAO;

	public BillService() {
		try {
			billDAO = new BillDAO(Persistence.createEntityManagerFactory("OnlineShop"));
		} catch (Exception ex) {
			System.out.println(ex);
		}
	}

	public void addBill(Bill c) {
		billDAO.create(c);
	}

	public void updateBill(Bill u) {
		billDAO.update(u);
	}

	public List<Bill> getAllBills() {
		return billDAO.findAll();
	}
}
