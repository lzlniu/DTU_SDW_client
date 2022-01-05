package dtu.pay;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class SimpleDTUPaySteps {
	String cid, mid;
	SimpleDTUPay dtuPay = new SimpleDTUPay();
	boolean successful;
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
	successful = dtuPay.pay(amount,cid,mid);
	}
	
	@Then("the payment is successful")
	public void thePaymentIsSuccessful() {
		assertTrue(successful);;
	}
	
	
	//TODO implement these tests
	@Given("a successful payment of {string} kr from customer {string} to merchant {string}")
	public void aSuccessfulPaymentOfKrFromCustomerToMerchant(String string, String string2, String string3) {
	    // Write code here that turns the phrase above into concrete actions
	    throw new io.cucumber.java.PendingException();
	}

	@When("the manager asks for a list of payments")
	public void theManagerAsksForAListOfPayments() {
	    // Write code here that turns the phrase above into concrete actions
	    throw new io.cucumber.java.PendingException();
	}

	@Then("the list contains a payments where customer {string} paid {string} kr to merchant {string}")
	public void theListContainsAPaymentsWhereCustomerPaidKrToMerchant(String string, String string2, String string3) {
	    // Write code here that turns the phrase above into concrete actions
	    throw new io.cucumber.java.PendingException();
	}

	@When("the merchant initiates a payment for {string} kr by the customer")
	public void theMerchantInitiatesAPaymentForKrByTheCustomer(String string) {
	    // Write code here that turns the phrase above into concrete actions
	    throw new io.cucumber.java.PendingException();
	}

	@Then("the payment is not successful")
	public void thePaymentIsNotSuccessful() {
	    // Write code here that turns the phrase above into concrete actions
	    throw new io.cucumber.java.PendingException();
	}

	@Then("an error message is returned saying {string}")
	public void anErrorMessageIsReturnedSaying(String string) {
	    // Write code here that turns the phrase above into concrete actions
	    throw new io.cucumber.java.PendingException();
	}
}

