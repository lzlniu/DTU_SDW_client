package dtu.pay;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import dtu.ws.fastmoney.AccountInfo;
import dtu.ws.fastmoney.BankServiceException_Exception;
import dtu.ws.fastmoney.User;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class SimpleDTUPaySteps {
	String cid, mid;
	String firstName, lastName, CPR, bankIDCustomer, bankIDMerchant;
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
	
	@Given("a customer with a bank account with balance {bigdecimal}")
	public void aCustomerWithABankAccountWithBalance(BigDecimal bigDecimal) {
	    firstName = "Frank";
	    lastName = "Hansen";
	    CPR = "090701-7617";
	    try {
			bankIDCustomer = dtuPay.bank.createAccountWithBalance(createUser(CPR,firstName,lastName), bigDecimal);
			bankAccounts.add(bankIDCustomer);
		} catch (BankServiceException_Exception e) {
			e.printStackTrace();
		}
	}

	@When("the customer registers with DTU Pay")
	public void theCustomerRegistersWithDTUPay() {
		try {
			cid = dtuPay.registerCustomer(bankIDCustomer);
		} catch (Exception e){
			this.e = e;
		}
	}

	@Then("that customer is registered with DTU Pay")
	public void thatCustomerIsRegisteredWithDTUPay() {
	    assertTrue(dtuPay.customerIsRegistered(cid));
	}

	@Given("a customer with no bank account")
	public void aCustomerWithNoBankAccount() {
		bankIDCustomer = "invalidId";
	}

	@Given("a merchant with a bank account with balance {bigdecimal}")
	public void aMerchantWithABankAccountWithBalance(BigDecimal bigDecimal) {
		firstName = "Benny";
		lastName = "Bonghoved";
		CPR = "666666-9999";
		try {
			bankIDMerchant = dtuPay.bank.createAccountWithBalance(createUser(CPR,firstName,lastName), bigDecimal);
			bankAccounts.add(bankIDMerchant);
		} catch (BankServiceException_Exception e) {
			e.printStackTrace();
		}
	}

	@When("the merchant registers with DTU Pay")
	public void theMerchantRegistersWithDTUPay() {
		try {
			mid = dtuPay.registerMerchants(bankIDMerchant);
		} catch (Exception e){
			this.e = e;
		}
	}

	@Then("that merchant is registered with DTU Pay")
	public void thatMerchantIsRegisteredWithDTUPay() {
		assertTrue(dtuPay.merchantIsRegistered(mid));
	}

	@Given("a merchant with no bank account")
	public void aMerchantWithNoBankAccount() {
		bankIDMerchant = "invalidBankIdMerchant";
	}
	
	
	@Given("that the customer is registered with DTU Pay")
	public void thatTheCustomerIsRegisteredWithDTUPay() throws Exception {
	    cid = dtuPay.registerCustomer(bankIDCustomer);
	}

	@Given("that the merchant is registered with DTU Pay")
	public void thatTheMerchantIsRegisteredWithDTUPay() throws Exception {
	    mid = dtuPay.registerMerchants(bankIDMerchant);
	}

	@Then("the balance of the customer at the bank is {bigdecimal} kr")
	public void theBalanceOfTheCustomerAtTheBankIsKr(BigDecimal bigDecimal) throws BankServiceException_Exception {
	    BigDecimal balanceC = dtuPay.bank.getAccount(bankIDCustomer).getBalance();
		assertEquals(bigDecimal,balanceC);
	}

	@Then("the balance of the merchant at the bank is {bigdecimal} kr")
	public void theBalanceOfTheMerchantAtTheBankIsKr(BigDecimal bigDecimal) throws BankServiceException_Exception {
		BigDecimal balanceM = dtuPay.bank.getAccount(bankIDMerchant).getBalance();
		assertEquals(bigDecimal,balanceM);
	}

	
	@Given("a customer with id {string}")
	public void aCustomerWithId(String cid) {
	this.cid = cid;
	}
	@Given("a merchant with id {string}")
	public void aMerchantWithId(String mid) {
	this.mid = mid;
	}
	@When("the merchant initiates a payment for {bigdecimal} kr by the customer")
	public void theMerchantInitiatesAPaymentForKrByTheCustomer(BigDecimal amount) {
		try{
			successful = dtuPay.pay(amount,cid,mid);
		}catch (Exception e) {this.e = e;}
	}
	
	@Then("the payment is successful")
	public void thePaymentIsSuccessful() {
		assertTrue(successful);
	}
	
	@Given("a successful payment of {bigdecimal} kr from customer {string} to merchant {string}")
	public void aSuccessfulPaymentOfKrFromCustomerToMerchant(BigDecimal bigDecimal, String string, String string2) {
	    this.cid = string;
	    this.mid = string2;
	    try {
			successful = dtuPay.pay(bigDecimal, cid, mid);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@When("the manager asks for a list of payments")
	public void theManagerAsksForAListOfPayments() {
	    payments = dtuPay.getPayments();
	}

	@Then("the list contains a payments where customer {string} paid {bigdecimal} kr to merchant {string}")
	public void theListContainsAPaymentsWhereCustomerPaidKrToMerchant(String string, BigDecimal bigDecimal, String string2) {
	    Payment p = new Payment(string, string2, bigDecimal);
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

