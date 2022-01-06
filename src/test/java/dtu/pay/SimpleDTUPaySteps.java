package dtu.pay;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import dtu.ws.fastmoney.BankServiceException_Exception;
import dtu.ws.fastmoney.User;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class SimpleDTUPaySteps {
	String cid, mid;
	String firstName, lastName, CPR, bankID;
	List<String> bankAccounts;
	SimpleDTUPay dtuPay = new SimpleDTUPay();
	boolean successful;
	List<Payment> payments;
	Exception e;
	
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
				dtuPay.bank.retireAccount(account);
			} catch (BankServiceException_Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	@Given("a customer with a bank account with balance {int}")
	public void aCustomerWithABankAccountWithBalance(int int1) {
	    firstName = "Frank";
	    lastName = "Hansen";
	    CPR = "090701-7617";
	    try {
	    	bankID = dtuPay.bank.createAccountWithBalance(createUser(CPR,firstName,lastName), BigDecimal.valueOf(int1));
			bankAccounts.add(bankID);
		} catch (BankServiceException_Exception e) {
			e.printStackTrace();
		}
	}

	@When("the customer registers with DTU Pay")
	public void theCustomerRegistersWithDTUPay() {
	    cid = dtuPay.registerCustomer(bankID);
	}

	@Then("that customer is registered with DTU Pay")
	public void thatCustomerIsRegisteredWithDTUPay() {
	    assertTrue(dtuPay.customerIsRegistered(cid));
	}

	@Given("a customer with no bank account")
	public void aCustomerWithNoBankAccount() {
	    // Write code here that turns the phrase above into concrete actions
	    throw new io.cucumber.java.PendingException();
	}

	@Given("a merchant with a bank account with balance {int}")
	public void aMerchantWithABankAccountWithBalance(Integer int1) {
	    // Write code here that turns the phrase above into concrete actions
	    throw new io.cucumber.java.PendingException();
	}

	@When("the merchant registers with DTU Pay")
	public void theMerchantRegistersWithDTUPay() {
	    // Write code here that turns the phrase above into concrete actions
	    throw new io.cucumber.java.PendingException();
	}

	@Then("that merchant is registered with DTU Pay")
	public void thatMerchantIsRegisteredWithDTUPay() {
	    // Write code here that turns the phrase above into concrete actions
	    throw new io.cucumber.java.PendingException();
	}

	@Given("a merchant with no bank account")
	public void aMerchantWithNoBankAccount() {
	    // Write code here that turns the phrase above into concrete actions
	    throw new io.cucumber.java.PendingException();
	}
	
	
	@Given("that the customer is registered with DTU Pay")
	public void thatTheCustomerIsRegisteredWithDTUPay() {
	    // Write code here that turns the phrase above into concrete actions
	    throw new io.cucumber.java.PendingException();
	}

	@Given("that the merchant is registered with DTU Pay")
	public void thatTheMerchantIsRegisteredWithDTUPay() {
	    // Write code here that turns the phrase above into concrete actions
	    throw new io.cucumber.java.PendingException();
	}

	@Then("the balance of the custoemr at the bank is {int} kr")
	public void theBalanceOfTheCustoemrAtTheBankIsKr(Integer int1) {
	    // Write code here that turns the phrase above into concrete actions
	    throw new io.cucumber.java.PendingException();
	}

	@Then("the balance of the merchant at the bank is {int} kr")
	public void theBalanceOfTheMerchantAtTheBankIsKr(Integer int1) {
	    // Write code here that turns the phrase above into concrete actions
	    throw new io.cucumber.java.PendingException();
	}
	
	
	
	
	
	@Given("a customer with id {string}")
	public void aCustomerWithId(String cid) {
	this.cid = cid;
	}
	@Given("a merchant with id {string}")
	public void aMerchantWithId(String mid) {
	this.mid = mid;
	}
	@When("the merchant initiates a payment for {int} kr by the customer")
	public void theMerchantInitiatesAPaymentForKrByTheCustomer(int amount) {
		try{
			successful = dtuPay.pay(amount,cid,mid);
		}catch (Exception e) {this.e = e;}
	}
	
	@Then("the payment is successful")
	public void thePaymentIsSuccessful() {
		assertTrue(successful);
	}
	
	@Given("a successful payment of {int} kr from customer {string} to merchant {string}")
	public void aSuccessfulPaymentOfKrFromCustomerToMerchant(Integer int1, String string, String string2) {
	    this.cid = string;
	    this.mid = string2;
	    try {
			successful = dtuPay.pay(int1, cid, mid);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@When("the manager asks for a list of payments")
	public void theManagerAsksForAListOfPayments() {
	    payments = dtuPay.getPayments();
	}

	@Then("the list contains a payments where customer {string} paid {int} kr to merchant {string}")
	public void theListContainsAPaymentsWhereCustomerPaidKrToMerchant(String string, int int1, String string2) {
	    Payment p = new Payment(string, string2, int1);
	    assertTrue(payments.get(payments.size()-1).equals(p));
	}

	@Then("the payment is not successful")
	public void thePaymentIsNotSuccessful() {
	    assertFalse(successful);
	}

	@Then("an error message is returned saying {string}")
	public void anErrorMessageIsReturnedSaying(String string) {
		assertEquals(string, e.getMessage());
	}
}

