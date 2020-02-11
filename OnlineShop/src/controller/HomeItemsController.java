package controller;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.Main;
import model.Customer;
import model.Provider;
import model.User;
import service.CustomerService;
import service.ProviderService;
import service.UserService;

public class HomeItemsController {

	@FXML
	private TextField txtUser;

	@FXML
	private PasswordField txtPassword;

	@FXML
	private Label lblErrorMessage;

	@FXML
	private void goRegister() throws IOException {
		Main.showRegisterScene();
	}

	public void initialize(URL url, ResourceBundle rb) {

	}

	@FXML
	private void handleCloseButtonAction(ActionEvent event) {
		((Stage) (((Button) event.getSource()).getScene().getWindow())).close();
	}

	private User logUser = new User();

	public User isUser() {
		UserService logUserService = new UserService();
		List<User> logUsersList = logUserService.loginUser(txtUser.getText(), txtPassword.getText());

		if (logUsersList.size() == 1)
			logUser = logUsersList.get(0);
		else {
			return null;
		}

		return logUser;
	}

	@FXML
	public void btnLoginClick(ActionEvent event) throws IOException {

		if (txtUser.getText().equals("") || txtPassword.getText().equals("")) {
			// System.out.println("Error: please provide the login details!");
			lblErrorMessage.setText("Error: please provide the login details!");
			lblErrorMessage.setVisible(true);
		} else {
			lblErrorMessage.setVisible(false);

			if (isUser() != null) {

				if (logUser.getIsCustomer() == 1) {
					// retreive Customer to send to loginCustomerController
					Customer customer = new Customer();
					CustomerService customerService = new CustomerService();
					List<Customer> customers = customerService
							.findById(Integer.toString(logUser.getCustomer().getCustomerId()));
					customer = customers.get(0);

					FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/LoginCustomerView.fxml"));
					Parent root = loader.load();

					// transfer user/customer to LoginCustomerController
					LoginCustomerController loginCustomerController = loader.getController();
					// loginCustomerController.currentUser(logUser);
					loginCustomerController.currentCustomer(customer);

					Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
					app_stage.setScene(new Scene(root));
					app_stage.show();

				}

				if (logUser.getIsProvider() == 1) {
					// retreive Provider to send to loginProviderController
					Provider provider = new Provider();
					ProviderService providerService = new ProviderService();

					List<Provider> providers = providerService
							.findProviderById(Integer.toString(logUser.getProvider().getProviderId()));
					provider = providers.get(0);

					FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/LoginProviderView.fxml"));
					Parent root = loader.load();

					// transfer user to loginProviderController
					LoginProviderController loginProviderController = loader.getController();
					loginProviderController.currentUser(logUser);
					// transfer provider
					loginProviderController.getProvider(provider);

					Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
					app_stage.setScene(new Scene(root));
					app_stage.show();
				}

			} else {
				lblErrorMessage.setText("Login fail...Incorect details!");
				lblErrorMessage.setVisible(true);
			}
		}

	}
}
