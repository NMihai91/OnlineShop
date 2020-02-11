package controller;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Vector;
import java.util.regex.Pattern;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.Main;
import model.Bill;
import model.BillProviderProduct;
import model.Customer;
import model.Product;
import model.Provider;
import model.ProviderProduct;
import service.AccountService;
import service.BillProviderProductService;
import service.BillService;
import service.ProviderProductService;
import service.ProviderService;

public class LoginCustomerController implements Initializable {

	@FXML
	private TextField txtCurrentBalance;

	@FXML
	private TextField txtAddCredit;

	@FXML
	private Label lblSuccessMessage;

	@FXML
	private Label lblErrorMessage;

	@FXML
	private Label txtRating;

	@FXML
	private Label succesAddProductMessage;

	@FXML
	private Label errorAddProductMessage;

	@FXML
	private ListView<Provider> providersList;

	@FXML
	private ListView<Product> productsList;

	@FXML
	private TextField infoName;

	@FXML
	private TextField infoPrice;

	@FXML
	private TextField infoQuantity;

	@FXML
	private TextArea infoDetails;

	@FXML
	private TextField quantityPurchased;

	@FXML
	private Label txtRequestProductsAndPayLater;

	@FXML
	private Label txtEmptyTheCart;

	@FXML
	private Label txtConfirmAndPay;

	private static DecimalFormat df2 = new DecimalFormat("#.##");

	private Date date = new Date(System.currentTimeMillis());

	ObservableList<Provider> obsProviderList = FXCollections.observableArrayList();
	ObservableList<Product> obsProductList = FXCollections.observableArrayList();

	// private User logUser = new User();
	private Customer logCustomer = new Customer();

	private void resetInfoCells() {
		infoName.clear();
		infoName.setEditable(false);
		infoPrice.clear();
		infoPrice.setEditable(false);
		infoQuantity.clear();
		infoQuantity.setEditable(false);
		infoDetails.clear();
		infoDetails.setEditable(false);
	}

	public void hideCurrentBalance() {
		txtCurrentBalance.clear();
		lblSuccessMessage.setVisible(false);
		lblErrorMessage.setVisible(false);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		txtCurrentBalance.setEditable(false);

		ProviderService providerService = new ProviderService();
		List<Provider> providers = providerService.getAllProvider();

		for (int i = 0; i < providers.size(); i++) {
			obsProviderList.add(new Provider(providers.get(i).getProviderId(), providers.get(i).getProviderName()));
		}

		providersList.setItems(obsProviderList);

		providersList.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
			if (newSelection != null) {
				resetInfoCells();
				productsList.getItems().clear();
				showProductsList(newSelection.getProviderId());
			} else {
				providersList.getSelectionModel().select(0);
			}
			succesAddProductMessage.setVisible(false);
			errorAddProductMessage.setVisible(false);
		});
	}

	// temp help me to save the current product/ I need this thing because when I
	// push ADD TO CART I want to know what product I want to add
	private Product temp = new Product();
	// save the current number of products/ size initial is 1 when you add a product
	// in your cart
	private int numberOfProducts = 1;

	private ProviderProductService providerProductService = new ProviderProductService();
	private List<ProviderProduct> providerProducts = providerProductService.getAllProviderProducts();

	public void showProductsList(int tempProviderId) {

		for (int i = 0; i < providerProducts.size(); i++) {
			if (tempProviderId == providerProducts.get(i).getProvider().getProviderId()) {
				obsProductList.add(new Product(providerProducts.get(i).getProduct().getProductId(),
						providerProducts.get(i).getProduct().getProductName(),
						providerProducts.get(i).getProduct().getPrice(),
						providerProducts.get(i).getProduct().getDescription()));
			}
		}

		productsList.setItems(obsProductList);

		productsList.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
			quantityPurchased.clear();
			if (newSelection != null) {
				infoName.setText(newSelection.getProductName());
				infoName.setEditable(false);
				infoPrice.setText(String.valueOf(newSelection.getPrice()));
				infoPrice.setEditable(false);
				infoDetails.setText(newSelection.getDescription());
				infoDetails.setEditable(false);

				for (int i = 0; i < providerProducts.size(); i++) {
					if (newSelection.getProductId() == providerProducts.get(i).getProduct().getProductId()) {
						infoQuantity.setText(Integer.toString(providerProducts.get(i).getQuantityAvailable()));
						infoQuantity.setEditable(false);
					}
				}

				temp = newSelection;
				succesAddProductMessage.setVisible(false);
				errorAddProductMessage.setVisible(false);
			} else {
				productsList.getSelectionModel().select(0);
			}
		});
	}

	// save all the number of products to send to CartController
	Vector<Integer> vectorNumberOfProducts = new Vector<>();

	public void setMyNumberOfProducts() {
		if (!(quantityPurchased.getText().trim().isEmpty()) && (Integer.valueOf(quantityPurchased.getText()) > 0)) {
			// myNumberOfProducts = Integer.valueOf(quantityPurchased.getText());
			vectorNumberOfProducts.add(Integer.valueOf(quantityPurchased.getText()));
			// quantityPurchased.clear();
		} else
			vectorNumberOfProducts.add(numberOfProducts);
		// quantityPurchased.clear();
	}

	// in this list I save all wanted products
	private List<Product> myProducts = new ArrayList<Product>();

	public void btnAddProductToCart() {
		if (quantityProductIsAvailable()) {
			if (!(infoName.getText().isEmpty()) && verifySetQuantity(quantityPurchased.getText())) {
				if (temp.getProductName() == null) {
					succesAddProductMessage.setVisible(false);

					errorAddProductMessage.setText("Please select a product!");
					errorAddProductMessage.setVisible(true);
				} else {
					errorAddProductMessage.setVisible(false);

					succesAddProductMessage.setText("Product added");
					succesAddProductMessage.setVisible(true);
					myProducts.add(temp);

					setMyNumberOfProducts();
					quantityPurchased.clear();
				}
			} else {
				errorAddProductMessage.setText("Something went wrong!");
				errorAddProductMessage.setVisible(true);
				succesAddProductMessage.setVisible(false);
			}
		} else {
			errorAddProductMessage.setText("Insufficient stock!");
			errorAddProductMessage.setVisible(true);
			succesAddProductMessage.setVisible(false);
		}
	}

	public void currentBalance() {
		txtAddCredit.clear();
		lblSuccessMessage.setVisible(false);
		lblErrorMessage.setVisible(false);

		txtCurrentBalance.setText(String.valueOf(df2.format(logCustomer.getAccounts().get(0).getSum())));
	}

	private boolean verifyAddCredit(String credit) {
		String decimalPattern = "[0-9]+(\\.[0-9]+)?([Ee][+-]?[0-9]+)?";
		boolean match = Pattern.matches(decimalPattern, credit);

		return match;
	}

	public void clearCreditSum() {
		txtAddCredit.clear();
		lblSuccessMessage.setVisible(false);
		lblErrorMessage.setVisible(false);
	}

	private void payProductsFromAccount() {
		AccountService accountService = new AccountService();

		for (int i = 0; i < myProducts.size(); i++) {
			double currentBalance = logCustomer.getAccounts().get(0).getSum();
			double tempPrice = myProducts.get(i).getPrice() * vectorNumberOfProducts.get(i);

			logCustomer.getAccounts().get(0).setSum(currentBalance - tempPrice);
			accountService.updateAccount(logCustomer.getAccounts().get(0));
		}
	}

	public void removeProductsFromDatabase() {
		ProviderProduct providerProduct = new ProviderProduct();

		for (int i = 0; i < providerProducts.size(); i++) {

			for (int j = 0; j < myProducts.size(); j++) {
				if (providerProducts.get(i).getProduct().getProductId() == myProducts.get(j).getProductId()) {
					providerProduct = providerProducts.get(i);
					providerProduct.setQuantityAvailable(
							providerProducts.get(i).getQuantityAvailable() - vectorNumberOfProducts.get(j));

					providerProductService.updateProviderProduct(providerProduct);
					;
				}
			}
		}
	}

	private void generateBill(byte isPayd) {

		BillProviderProductService billProviderProductService = new BillProviderProductService();
		BillProviderProduct billProviderProduct = new BillProviderProduct();

		BillService billService = new BillService();
		Bill bill = new Bill();

		// add bill
		bill.setCustomer(logCustomer);
		bill.setEmiteDate(date);
		bill.setIsPayd(isPayd);
		billService.addBill(bill);

		// get all bills to set to bill_rovider_product
		List<Bill> bills = billService.getAllBills();

		for (int i = 0; i < myProducts.size(); i++) {
			for (int j = 0; j < providerProducts.size(); j++) {
				if (myProducts.get(i).getProductId() == providerProducts.get(j).getProduct().getProductId()) {

					billProviderProduct.setProviderProduct(providerProducts.get(j));
					billProviderProduct.setQuntityRequested(vectorNumberOfProducts.get(i));
					billProviderProduct.setBill(billService.getAllBills().get(bills.size() - 1));
					billProviderProductService.addBillProviderProduct(billProviderProduct);
				}
			}
		}
	}

	public void btnConfirmAndPay() {
		byte isPayd = 1;
		// retrive total price to verify if it's available in account
		double totalPrice = 0;

		for (int i = 0; i < vectorNumberOfProducts.size(); i++) {
			totalPrice += myProducts.get(i).getPrice() * vectorNumberOfProducts.get(i);
		}

		if (totalPrice <= logCustomer.getAccounts().get(0).getSum()) {
			if (restances <= 2) {
				if (myProducts.size() > 0) {
					payProductsFromAccount();
					generateBill(isPayd);
					removeProductsFromDatabase();
					emptyTheCart();

					txtConfirmAndPay.setText("Successful payment");
					txtConfirmAndPay.setVisible(true);
					txtEmptyTheCart.setVisible(false);
				} else {
					txtConfirmAndPay.setText("Empty cart!");
					txtConfirmAndPay.setVisible(true);
				}
			} else {
				txtConfirmAndPay.setVisible(false);
				succesAddProductMessage.setVisible(false);
				errorAddProductMessage.setText("Negative rating!");
				errorAddProductMessage.setVisible(true);
			}
		} else {
			txtConfirmAndPay.setVisible(false);
			succesAddProductMessage.setVisible(false);
			errorAddProductMessage.setText("Insufficient credit!");
			errorAddProductMessage.setVisible(true);
		}

	}

	public void btnPayLater() {
		byte isPayd = 0;

		if (myProducts.size() > 0) {
			txtRequestProductsAndPayLater.setText("Successful payment later...");
			txtRequestProductsAndPayLater.setVisible(true);

			generateBill(isPayd);
			emptyTheCart();
			txtEmptyTheCart.setVisible(false);
		} else {
			txtRequestProductsAndPayLater.setText("Empty cart!");
		}

	}

	private int restances = 0;

	public void btnRating() {
		restances = 0;

		BillService billService = new BillService();
		List<Bill> bills = billService.getAllBills();

		for (int i = 0; i < bills.size(); i++) {
			if (bills.get(i).getCustomer().getCustomerId() == logCustomer.getCustomerId()) {
				if (bills.get(i).getIsPayd() == 0) {
					restances += 1;
				}
			}
		}

		txtRating.setText("Unpaid bills: " + restances);
		txtRating.setVisible(true);
	}

	public void btnAddCredit() {
		if (!verifyAddCredit(txtAddCredit.getText())) {
			lblSuccessMessage.setVisible(false);
			lblErrorMessage.setText("Invalid sum!");
			lblErrorMessage.setVisible(true);
		} else {
			lblErrorMessage.setVisible(false);

			AccountService accountService = new AccountService();
			double sum = Double.parseDouble(txtAddCredit.getText());
			double currentBalance = logCustomer.getAccounts().get(0).getSum();

			logCustomer.getAccounts().get(0).setSum(sum + currentBalance);
			accountService.updateAccount(logCustomer.getAccounts().get(0));

			lblSuccessMessage.setText("Credit updated..." + '\n' + "refresh the Current Balance!");
			lblSuccessMessage.setVisible(true);

			txtAddCredit.clear();
		}

	}

//	public void currentUser(User user) {
//		this.logUser = user;
//	}

	public void currentCustomer(Customer customer) {
		this.logCustomer = customer;
	}

	public void showCart() {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/view/CartView.fxml"));

		try {
			loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}

		CartController cartController = loader.getController();
		cartController.getList(myProducts);
		cartController.getNumberOfProducts(vectorNumberOfProducts);

		Parent p = loader.getRoot();

		Stage stage = new Stage();
		stage.setScene(new Scene(p));
		stage.show();
	}

	public void emptyTheCart() {
		myProducts.clear();
		vectorNumberOfProducts.clear();

		txtEmptyTheCart.setText("Cart empty!");
		txtEmptyTheCart.setVisible(true);
	}

	private boolean quantityProductIsAvailable() {

		if (!(quantityPurchased.getText().trim().isEmpty()) && !(infoQuantity.getText().trim().isEmpty())) {
			if (Double.parseDouble(quantityPurchased.getText()) <= Double.parseDouble(infoQuantity.getText())) {
				return true;
			}
		} else if (quantityPurchased.getText().trim().isEmpty() && !(infoQuantity.getText().trim().isEmpty())) {
			if ((Double.parseDouble(infoQuantity.getText())) >= 1) {
				return true;
			}
		} else if (quantityPurchased.getText().trim().isEmpty() && infoQuantity.getText().trim().isEmpty()) {
			return true;
		} else {
			return false;
		}
		return false;
	}

	private boolean verifySetQuantity(String quantity) {
		String decimalPattern = "([0-9]*)";
		boolean match = Pattern.matches(decimalPattern, quantity);

		return match;
	}

	public void showBillPage(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/view/BillView.fxml"));

		try {
			loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// send provider to AddProductController
		BillController billController = loader.getController();
		billController.getCustomer(logCustomer);

		Parent p = loader.getRoot();

		Stage stage = new Stage();
		stage.setScene(new Scene(p));
		stage.show();
	}

	@FXML
	public void goMain(ActionEvent event) throws IOException {
		Main main = new Main();
		main.logOff(event);
	}
}
