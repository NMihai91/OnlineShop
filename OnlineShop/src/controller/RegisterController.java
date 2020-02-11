package controller;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import main.Main;
import model.Account;
import model.Customer;
import model.Provider;
import model.User;
import service.AccountService;
import service.CustomerService;
import service.ProviderService;
import service.UserService;

public class RegisterController {
	@FXML
	private TextField txtCustUser;

	@FXML
	private PasswordField txtCustPassword;

	@FXML
	private TextField txtCustName;

	@FXML
	private TextField txtCustEmail;

	@FXML
	private TextArea txtCustAdress;

	@FXML
	private Label lblErrorMessage;

	@FXML
	private Label lblSuccessMessage;

	@FXML
	private TextField txtProvUser;

	@FXML
	private PasswordField txtProvPassword;

	@FXML
	private TextField txtProvName;

	@FXML
	private TextField txtProvEmail;

	@FXML
	private Label lblProvErrorMessage;

	@FXML
	private Label lblProvSuccessMessage;
	
	Date date = new Date(System.currentTimeMillis());

	@FXML
	public void goMain() throws IOException {
		Main.showHomeItems();
	}

	public boolean verifyInfo(String userName, String password, String name) {
		if (userName.equals("") || password.equals("") || name.equals("")) {
			return false;
		}
		return true;
	}

	// Lambda expression - verify if the name contains just agree characters
	public boolean isOnlyCharact(String realName) {
		if (!(realName.chars().allMatch(Character::isLetter) || txtCustName.getText().contains(" "))) {
			{
				return false;
			}
		}

		return true;
	}

	public void btnCustRegisterClick(ActionEvent event) {
		Customer logCustomer = new Customer();
		CustomerService customerService = new CustomerService();

		User logUser = new User();
		UserService userService = new UserService();

		// verify if the user exist in DB
		List<User> listCustUser = userService.loginUser(txtCustUser.getText());

		if (!verifyInfo(txtCustUser.getText(), txtCustPassword.getText(), txtCustName.getText())) {
			lblErrorMessage.setText("Error: please provide the login details!");
			lblErrorMessage.setVisible(true);
			lblSuccessMessage.setVisible(false);
		} else if (listCustUser.size() == 1) {
			lblErrorMessage.setText("The username already exist!");
			lblErrorMessage.setVisible(true);
			lblSuccessMessage.setVisible(false);
		} else if (!isOnlyCharact(txtCustName.getText())) {
			lblErrorMessage.setText("Error: Customer name must contains just letters");
			lblErrorMessage.setVisible(true);
			lblSuccessMessage.setVisible(false);
		} else if (txtCustPassword.getText().length() < 5) {
			lblErrorMessage.setText("Error: Password must contains more than 4 digits");
			lblErrorMessage.setVisible(true);
			lblSuccessMessage.setVisible(false);
		} else {
			lblErrorMessage.setVisible(false);

			// Add a new customer in DB
			logCustomer.setCustomerName(txtCustName.getText());
			logCustomer.setAddress(txtCustAdress.getText());
			customerService.addCustomer(logCustomer);

			// retain the ID of the created Customer because is necessary to save this id in
			// User table
			List<Customer> logCustomers = customerService.getAllCustomers();
			Customer lastCustomer = logCustomers.get(logCustomers.size() - 1);

			// add new account for this customer
			AccountService accountService = new AccountService();
			Account account = new Account();
			account.setCustomer(lastCustomer);
			account.setSum(0);
			accountService.addAccount(account);

			// if the last name saved in Customer table is the same with the last name
			// entered in table
			if (lastCustomer.getCustomerName().equals(txtCustName.getText())) {
				byte isCustomer = 1, isProvider = 0;
				short status = 0;

				logUser.setUserName(txtCustUser.getText());
				logUser.setUserPassword(txtCustPassword.getText());
				logUser.setEmail(txtCustEmail.getText());
				logUser.setCustomer(lastCustomer);
				logUser.setIsCustomer(isCustomer);
				logUser.setIsProvider(isProvider);
				logUser.setRegisterDate(date);
				logUser.setStatus(status);
				userService.addUser(logUser);

				lblSuccessMessage.setText(
						"Registration successfull, click on HOME to LOGIN " + "\n" + "or submit another registration");
				lblSuccessMessage.setVisible(true);
			}

			txtCustUser.clear();
			txtCustPassword.clear();
			txtCustEmail.clear();
			txtCustName.clear();
			txtCustAdress.clear();
		}
	}

	public void btnProvRegisterClick(ActionEvent event) {
		Provider logProvider = new Provider();
		ProviderService providerService = new ProviderService();

		User logProvUser = new User();
		UserService userService = new UserService();

		List<User> listProvUser = userService.loginUser(txtProvUser.getText());

		if (!(verifyInfo(txtProvUser.getText(), txtProvPassword.getText(), txtProvName.getText()))) {
			lblProvErrorMessage.setText("Error: please provide the login details!");
			lblProvErrorMessage.setVisible(true);
			lblProvSuccessMessage.setVisible(false);
		} else if (listProvUser.size() == 1) {
			lblProvErrorMessage.setText("The username already exist!");
			lblProvErrorMessage.setVisible(true);
			lblProvSuccessMessage.setVisible(false);
		} else if (txtProvPassword.getText().length() < 5) {
			lblProvErrorMessage.setText("Error: Password must contains more than 4 digits");
			lblProvErrorMessage.setVisible(true);
			lblProvSuccessMessage.setVisible(false);
		} else {
			lblProvErrorMessage.setVisible(false);

			logProvider.setProviderName(txtProvName.getText());
			providerService.addProvider(logProvider);

			// retain the ID of the created Provider because is necessary to save this id in
			// User table
			List<Provider> logProviders = providerService.getAllProvider();
			Provider lastProvider = logProviders.get(logProviders.size() - 1);

			// if the last name saved in Provider table is the same with the last name
			// entered in table
			if (lastProvider.getProviderName().equals(txtProvName.getText())) {
				byte isCustomer = 0, isProvider = 1;
				short status = 0;

				logProvUser.setUserName(txtProvUser.getText());
				logProvUser.setUserPassword(txtProvPassword.getText());
				logProvUser.setEmail(txtProvEmail.getText());
				logProvUser.setProvider(lastProvider);
				logProvUser.setIsCustomer(isCustomer);
				logProvUser.setIsProvider(isProvider);
				logProvUser.setRegisterDate(date);
				logProvUser.setStatus(status);
				userService.addUser(logProvUser);
			}

			lblProvSuccessMessage.setText(
					"Registration successfull, click on HOME to LOGIN " + "\n" + "or submit another registration");
			lblProvSuccessMessage.setVisible(true);

			txtProvUser.clear();
			txtProvPassword.clear();
			txtProvName.clear();
			txtProvEmail.clear();
		}
	}
}
