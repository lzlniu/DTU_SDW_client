package dtu.pay;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import dtu.ws.fastmoney.*;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class SimpleDTUPaySteps {

	List<String> bankAccounts;
	customerAPI dtuPayCustomer = new customerAPI();
	merchantAPI dtuPayMerchant = new merchantAPI();
	boolean successful;
	List<Payment> payments;
	Exception e;
	BankService bank = new BankServiceService().getBankServicePort();
	DtuPayUser customer = new DtuPayUser();
	DtuPayUser merchant = new DtuPayUser();

	List<String> customerTokens = new ArrayList<>();
	
	private User createUser(String CPR, String first, String last) {
		User user = new User();
		user.setCprNumber(CPR);
		user.setFirstName(first);
		user.setLastName(last);
		return user;
	}
	
	@Before
	public void createBlankListOfCreatedAccounts() {
		bankAccounts = new ArrayList<String>();
	}
	
	@After
	public void deleteCreatedBankAccounts() {
		for (String account : bankAccounts) {
			try {
				bank.retireAccount(account);
			} catch (BankServiceException_Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	@Given("a customer with a bank account with balance {bigdecimal}")
	public void aCustomerWithABankAccountWithBalance(BigDecimal bigDecimal) {
	    customer.setFirstName("Frankie");
		customer.setLastName("Hansen");
		customer.setCPR("090701-7672");
	    try {
			customer.setBankID(bank.createAccountWithBalance(
					createUser(customer.getCPR(),
							customer.getFirstName(),
							customer.getLastName()), bigDecimal));

			bankAccounts.add(customer.getBankID());

		} catch (BankServiceException_Exception e) {
			e.printStackTrace();
		}
	}

	@When("the customer registers with DTU Pay")
	public void theCustomerRegistersWithDTUPay() {
		try {
			customer.setDtuPayID(dtuPayCustomer.registerCustomer(
					customer.getFirstName(),
					customer.getLastName(),
					customer.getBankID(),
					customer.getCPR()));

		} catch (Exception e){
			this.e = e;
		}
	}

	@Then("that customer is registered with DTU Pay")
	public void thatCustomerIsRegisteredWithDTUPay() {
	    assertTrue(dtuPayCustomer.customerIsRegistered(customer.getDtuPayID()));
	}

	@Given("a customer with no bank account")
	public void aCustomerWithNoBankAccount() {
		customer.setDtuPayID("invalid ID");
	}

	@Given("a merchant with a bank account with balance {bigdecimal}")
	public void aMerchantWithABankAccountWithBalance(BigDecimal bigDecimal) {
		merchant.setFirstName("Benny");
		merchant.setLastName("Bonghead");
		merchant.setCPR("696969-4200");
		try {
			merchant.setBankID(bank.createAccountWithBalance(
					createUser( merchant.getCPR(),
								merchant.getFirstName(),
								merchant.getLastName()), bigDecimal));

			bankAccounts.add(merchant.getBankID());

		} catch (BankServiceException_Exception e) {
			e.printStackTrace();
		}
	}

	@When("the merchant registers with DTU Pay")
	public void theMerchantRegistersWithDTUPay() {
		try {
			merchant.setDtuPayID(dtuPayMerchant.registerMerchants(
					merchant.getFirstName(),
					merchant.getLastName(),
					merchant.getBankID(),
					merchant.getCPR()));

		} catch (Exception e){
			this.e = e;
		}
	}

	@Then("that merchant is registered with DTU Pay")
	public void thatMerchantIsRegisteredWithDTUPay() {
		assertTrue(dtuPayMerchant.merchantIsRegistered(merchant.getDtuPayID()));
	}

	@Given("a merchant with no bank account")
	public void aMerchantWithNoBankAccount() {
		merchant.setBankID("invalidBankIdMerchant");
	}
	
	
	@Given("that the customer is registered with DTU Pay")
	public void thatTheCustomerIsRegisteredWithDTUPay() throws Exception {
		try {
			customer.setDtuPayID(dtuPayCustomer.registerCustomer(
					customer.getFirstName(),
					customer.getLastName(),
					customer.getBankID(),
					customer.getCPR()));

		} catch (Exception e){
			this.e = e;
		}
	}

	@Given("that the merchant is registered with DTU Pay")
	public void thatTheMerchantIsRegisteredWithDTUPay() throws Exception {
		try {
			merchant.setDtuPayID(dtuPayMerchant.registerMerchants(
					merchant.getFirstName(),
					merchant.getLastName(),
					merchant.getBankID(),
					merchant.getCPR()));

		} catch (Exception e){
			this.e = e;
		}
	}

	@Then("the balance of the customer at the bank is {bigdecimal} kr")
	public void theBalanceOfTheCustomerAtTheBankIsKr(BigDecimal bigDecimal) throws BankServiceException_Exception {
	    BigDecimal balanceC = bank.getAccount(customer.getBankID()).getBalance();
		assertEquals(bigDecimal,balanceC);
	}

	@Then("the balance of the merchant at the bank is {bigdecimal} kr")
	public void theBalanceOfTheMerchantAtTheBankIsKr(BigDecimal bigDecimal) throws BankServiceException_Exception {
		BigDecimal balanceM = bank.getAccount(merchant.getBankID()).getBalance();
		assertEquals(bigDecimal,balanceM);
	}

	
	@Given("a customer with id {string}")
	public void aCustomerWithId(String cid) {
	this.customer.setDtuPayID(cid);
	}
	@Given("a merchant with id {string}")
	public void aMerchantWithId(String mid) {
	this.merchant.setDtuPayID(mid);
	}
	@When("the merchant initiates a payment for {bigdecimal} kr by the customer")
	public void theMerchantInitiatesAPaymentForKrByTheCustomer(BigDecimal amount) {
		try{
			successful = dtuPayMerchant.pay(amount,customerTokens.get(0),merchant.getDtuPayID());
		}catch (Exception e) {this.e = e;}
	}
	
	@Then("the payment is successful")
	public void thePaymentIsSuccessful() {
		assertTrue(successful);
	}
	
	@Given("a successful payment of {bigdecimal} kr from customer {string} to merchant {string}")
	public void aSuccessfulPaymentOfKrFromCustomerToMerchant(BigDecimal bigDecimal, String string, String string2) {
		this.customer.setDtuPayID(string);
	    this.merchant.setDtuPayID(string2);
	    try {
			successful = dtuPayMerchant.pay(bigDecimal, customer.getDtuPayID(), merchant.getDtuPayID());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@When("the manager asks for a list of payments")
	public void theManagerAsksForAListOfPayments() {
	    payments = dtuPayMerchant.getPayments();
	}


	@Then("the payment is not successful")
	public void thePaymentIsNotSuccessful() {
	    assertFalse(successful);
	}

	@Then("an error message is returned saying {string}")
	public void anErrorMessageIsReturnedSaying(String string) {
		assertEquals(string, e.getMessage());
	}

	@Given("that the customer is deleted from dtu.pay")
	public void thatTheCustomerIsDeletedFromDtuPay() throws BankServiceException_Exception {
		bank.retireAccount(customer.getBankID());
		bankAccounts.remove(customer.getBankID());
	}

	@Given("a successful payment of {bigdecimal} kr")
	public void aSuccessfulPaymentOfKr(BigDecimal amount) throws Exception {
		successful = dtuPayMerchant.pay(amount, customer.getDtuPayID(), merchant.getDtuPayID());
	}

	@Then("the list contains a payments where customer paid {bigdecimal} kr")
	public void theListContainsAPaymentsWhereCustomerPaidKr(BigDecimal amount) {
		Payment p = new Payment(customer.getDtuPayID(), merchant.getDtuPayID(), amount);
		assertTrue(payments.get(payments.size()-1).equals(p));
	}

	@Given("a customer : {string} that is not registered with DTU pay")
	public void aCustomerThatIsNotRegisteredWithDTUPay(String arg0) {
		this.customer.setDtuPayID(arg0);
	}

	@Given("a merchant : {string} that is not registered with DTU pay")
	public void aMerchantThatIsNotRegisteredWithDTUPay(String arg0) {
		this.merchant.setDtuPayID(arg0);
	}

	@Given("that the merchant is deleted from dtu.pay")
	public void thatTheMerchantIsDeletedFromDtuPay() throws BankServiceException_Exception {
		bank.retireAccount(merchant.getBankID());
		bankAccounts.remove(merchant.getBankID());
	}

	@When("the customer requests to generate {int} tokens")
	public void theCustomerRequestsToGenerateTokens(int arg0) {
		try {
			customerTokens = dtuPayCustomer.getNewTokens(customer, arg0);
		} catch (Exception ex) {
			this.e = ex;
		}
	}

	@Then("{int} unique tokens is returned")
	public void uniqueTokensIsReturned(int arg0) {
		assertEquals(arg0, customerTokens.size());
	}

	@Given("the customer generates {int} tokens")
	public void theCustomerGeneratesTokens(int amount) {
		try {
			customerTokens = dtuPayCustomer.getNewTokens(customer, amount);
			assertEquals(amount, customerTokens.size());
		} catch (Exception ex) {
			this.e = ex;
		}
	}

	@Given("customer has tokens")
	public void customerHasTokens() throws Exception {
		customerTokens = dtuPayCustomer.getNewTokens(customer, 5);
		System.out.println(customerTokens.toString());
		assertTrue(!customerTokens.isEmpty());
	}


	//For finding bank accounts and potentially deleting them
	@When("searching")
	public void searching() throws BankServiceException_Exception {
		//bank.retireAccount("ee111e98-9cb8-4062-bdf8-bf8f10abb7e4");
		List<AccountInfo> as = bank.getAccounts();
		for (AccountInfo a : as){
			System.out.print(a.getUser().getFirstName());
			System.out.print(" - " + a.getUser().getLastName());
			System.out.println(" -- " + a.getAccountId());
		}
	}
}

