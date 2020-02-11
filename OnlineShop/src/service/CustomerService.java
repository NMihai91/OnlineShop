package service;

import java.util.List;

import javax.persistence.Persistence;

import model.Customer;
import dao.CustomerDAO;

public class CustomerService {
	
	private CustomerDAO customerDAO;

	public CustomerService() {
		try {
			customerDAO = new CustomerDAO(Persistence.createEntityManagerFactory("OnlineShop"));
		} catch (Exception ex) {
			System.out.println(ex);
		}
	}

	public void addCustomer(Customer c) {
		customerDAO.create(c);
	}

	public void updateCustomer(Customer u) {
		customerDAO.update(u);
	}

	public List<Customer> getAllCustomers() {
		return customerDAO.findAll();
	}
	
	public List<Customer> findById(String id){
		return customerDAO.findById(id);
	}
}
