package dtu.pay;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;

import java.math.BigDecimal;
import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import dtu.ws.fastmoney.*;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class DTUPaySteps {
	final boolean runningLocally = false;
	List<String> bankAccounts;
	customerAPI dtuPayCustomer = new customerAPI(runningLocally);
	merchantAPI dtuPayMerchant = new merchantAPI(runningLocally);
	boolean successful;
	boolean successful2;
	List<Payment> payments, report;
	Exception e, e2;
	BankService bank = new BankServiceService().getBankServicePort();
	DtuPayUser customer = new DtuPayUser();
	DtuPayUser customer2 = new DtuPayUser();
	DtuPayUser merchant = new DtuPayUser();
	DtuPayUser merchant2 = new DtuPayUser();

	List<String> customerTokens = new ArrayList<>();
	List<String> customer2Tokens = new ArrayList<>();
	int used_token_num = 0;
	HashMap<String, List<Payment>> customersPayments;

	//@author s164422 - Thomas Bergen
	private User createUser(String CPR, String first, String last) {
		User user = new User();
		user.setCprNumber(CPR);
		user.setFirstName(first);
		user.setLastName(last);
		return user;
	}

	//@author s174293 - Kasper Jørgensen
	@Before
	public void createBlankListOfCreatedAccounts() {
		bankAccounts = new ArrayList<String>();
		this.payments = new ArrayList<>();
		this.report = new ArrayList<>();
		this.customersPayments = new HashMap<>();
	}

	//@author s202772 - Gustav Kinch
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
	//@author s215949 - Zelin Li
	@Given("a customer with a bank account with balance {bigdecimal}")
	public void aCustomerWithABankAccountWithBalance(BigDecimal bigDecimal) {
	    customer.setFirstName("Frankie");
		customer.setLastName("Hansen");
		customer.setCPR("090701-7674");
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
	//@author s213578 - Johannes Pedersen
	@Given("a customer with name {string} {string} and CPR {string} and a bank account with balance {bigdecimal}")
	public void aCustomerWithNameAndCPRAndABankAccountWithBalance(String arg0, String arg1, String arg2, BigDecimal balance) {
		customer = new DtuPayUser();
		customer.setFirstName(arg0);
		customer.setLastName(arg1);
		customer.setCPR(arg2);
		try {
			customer.setBankID(bank.createAccountWithBalance(
					createUser(customer.getCPR(),
							customer.getFirstName(),
							customer.getLastName()),
							balance));

			bankAccounts.add(customer.getBankID());

		} catch (BankServiceException_Exception e) {
			e.printStackTrace();
		}
	}

	//@author s215949 - Zelin Li
	@Given("a second customer with name {string} {string} and CPR {string} and a bank account with balance {bigdecimal}")
	public void aSecondCustomerWithNameAndCPRAndABankAccountWithBalance(String arg0, String arg1, String arg2, BigDecimal balance) {
		customer2 = new DtuPayUser();
		customer2.setFirstName(arg0);
		customer2.setLastName(arg1);
		customer2.setCPR(arg2);
		try {
			customer2.setBankID(bank.createAccountWithBalance(
					createUser(customer2.getCPR(),
							customer2.getFirstName(),
							customer2.getLastName()),
					balance));

			bankAccounts.add(customer2.getBankID());

		} catch (BankServiceException_Exception e) {
			e.printStackTrace();
		}
	}


	//@author s212643 - Xingguang Geng
	@Given("a merchant with name {string} {string} and CPR {string} and a bank account with balance {bigdecimal}")
	public void aMerchantWithNameAndCPRAndABankAccountWithBalance(String arg0, String arg1, String arg2, BigDecimal balance) {
		merchant = new DtuPayUser();
		merchant.setFirstName(arg0);
		merchant.setLastName(arg1);
		merchant.setCPR(arg2);
		try {
			merchant.setBankID(bank.createAccountWithBalance(
					createUser(merchant.getCPR(),
							merchant.getFirstName(),
							merchant.getLastName()),
							balance));

			bankAccounts.add(merchant.getBankID());

		} catch (BankServiceException_Exception e) {
			e.printStackTrace();
		}
	}

	@Given("a second merchant with name {string} {string} and CPR {string} and a bank account with balance {bigdecimal}")
	public void aSecondMerchantWithNameAndCPRAndABankAccountWithBalance(String arg0, String arg1, String arg2, BigDecimal balance) {
		merchant2 = new DtuPayUser();
		merchant2.setFirstName(arg0);
		merchant2.setLastName(arg1);
		merchant2.setCPR(arg2);
		try {
			merchant2.setBankID(bank.createAccountWithBalance(
					createUser(merchant2.getCPR(),
							merchant2.getFirstName(),
							merchant2.getLastName()),
							balance));

			bankAccounts.add(merchant2.getBankID());

		} catch (BankServiceException_Exception e) {
			e.printStackTrace();
		}
	}

	//@author s164422 - Thomas Bergen
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
	//@author s174293 - Kasper Jørgensen
	@Then("that customer is registered with DTU Pay")
	public void thatCustomerIsRegisteredWithDTUPay() {
	    assertTrue(dtuPayCustomer.customerIsRegistered(customer.getDtuPayID()));
	}

	//@author s212643 - Xingguang Geng
	@Then("that customer is not registered with DTU Pay")
	public void thatCustomerIsNotRegisteredWithDTUPay() {
		assertFalse(dtuPayCustomer.customerIsRegistered(customer.getDtuPayID()));
	}

	//@author s174293 - Kasper Jørgensen
	@Then("that merchant is not registered with DTU Pay")
	public void thatMerchantIsNotRegisteredWithDTUPay() {
		assertFalse(dtuPayMerchant.merchantIsRegistered(merchant.getDtuPayID()));
	}

	//@author s202772 - Gustav Kinch
	@Given("a customer with no bank account")
	public void aCustomerWithNoBankAccount() {
		customer.setDtuPayID("invalid ID");
	}

	//@author s215949 - Zelin Li
	@Given("a merchant with a bank account with balance {bigdecimal}")
	public void aMerchantWithABankAccountWithBalance(BigDecimal bigDecimal) {
		merchant.setFirstName("Benny");
		merchant.setLastName("Bonghead");
		merchant.setCPR("696969-4202");
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
	//@author s213578 - Johannes Pedersen
	@When("the merchant registers with DTU Pay")
	public void theMerchantRegistersWithDTUPay() {
		try {
			merchant.setDtuPayID(dtuPayMerchant.registerMerchant(
					merchant.getFirstName(),
					merchant.getLastName(),
					merchant.getBankID(),
					merchant.getCPR()));

		} catch (Exception e){
			this.e = e;
		}
	}

	//@author s202772 - Gustav Kinch
	@When("the customer is deleted")
	public void theCustomerIsDeleted() {
		try{
			dtuPayCustomer.deleteCustomer(customer.getDtuPayID());
		} catch (Exception e){
			this.e = e;
		}
	}

	//@author s164422 - Thomas Bergen
	@When("the merchant is deleted")
	public void theMerchantIsDeleted() {
		try{
			dtuPayMerchant.deleteMerchant(merchant.getDtuPayID());
		} catch (Exception e){
			this.e = e;
		}
	}

	//@author s212643 - Xingguang Geng
	@Then("that merchant is registered with DTU Pay")
	public void thatMerchantIsRegisteredWithDTUPay() {
		assertTrue(dtuPayMerchant.merchantIsRegistered(merchant.getDtuPayID()));
	}

	//@author s164422 - Thomas Bergen
	@Given("a merchant with no bank account")
	public void aMerchantWithNoBankAccount() {
		merchant.setBankID("invalidBankIdMerchant");
	}
	
	//@author s174293 - Kasper Jørgensen
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
	//@author s215949 - Zelin Li
	@Given("that the second customer is registered with DTU Pay")
	public void thatTheSecondCustomerIsRegisteredWithDTUPay() {
		try {
			customer2.setDtuPayID(dtuPayCustomer.registerCustomer(
					customer2.getFirstName(),
					customer2.getLastName(),
					customer2.getBankID(),
					customer2.getCPR()));

		} catch (Exception e){
			this.e = e;
		}
	}

	//@author s202772 - Gustav Kinch
	@Given("that the merchant is registered with DTU Pay")
	public void thatTheMerchantIsRegisteredWithDTUPay() throws Exception {
		try {
			merchant.setDtuPayID(dtuPayMerchant.registerMerchant(
					merchant.getFirstName(),
					merchant.getLastName(),
					merchant.getBankID(),
					merchant.getCPR()));

		} catch (Exception e){
			this.e = e;
		}
	}

	//@author s164422 - Thomas Bergen
	@Given("that the second merchant is registered with DTU Pay")
	public void thatTheSecondMerchantIsRegisteredWithDTUPay() {
		try {
			merchant2.setDtuPayID(dtuPayMerchant.registerMerchant(
					merchant2.getFirstName(),
					merchant2.getLastName(),
					merchant2.getBankID(),
					merchant2.getCPR()));

		} catch (Exception e){
			this.e = e;
		}
	}

	//@author s215949 - Zelin Li
	@Then("the balance of the customer at the bank is {bigdecimal} kr")
	public void theBalanceOfTheCustomerAtTheBankIsKr(BigDecimal bigDecimal) throws BankServiceException_Exception {
	    BigDecimal balanceC = bank.getAccount(customer.getBankID()).getBalance();
		assertEquals(bigDecimal,balanceC);
	}

	//@author s215949 - Zelin Li
	@Given("the balance of the second customer at the bank is {bigdecimal} kr")
	public void theBalanceOfTheSecondCustomerAtTheBankIsKr(BigDecimal bigDecimal) throws BankServiceException_Exception {
		BigDecimal balanceC = bank.getAccount(customer2.getBankID()).getBalance();
		assertEquals(bigDecimal,balanceC);
	}

	//@author s212643 - Xingguang Geng
	@Then("the balance of the merchant at the bank is {bigdecimal} kr")
	public void theBalanceOfTheMerchantAtTheBankIsKr(BigDecimal bigDecimal) throws BankServiceException_Exception {
		BigDecimal balanceM = bank.getAccount(merchant.getBankID()).getBalance();
		assertEquals(bigDecimal,balanceM);
	}

	//@author s213578 - Johannes Pedersen
	@Given("the balance of the second merchant at the bank is {bigdecimal} kr")
	public void theBalanceOfTheSecondMerchantAtTheBankIsKr(BigDecimal bigDecimal) throws BankServiceException_Exception {
		BigDecimal balanceM = bank.getAccount(merchant2.getBankID()).getBalance();
		assertEquals(bigDecimal,balanceM);
	}

	//@author s164422 - Thomas Bergen
	@Given("a customer with id {string}")
	public void aCustomerWithId(String cid) {
	this.customer.setDtuPayID(cid);
	}
	//@author s174293 - Kasper Jørgensen
	@Given("a merchant with id {string}")
	public void aMerchantWithId(String mid) {
	this.merchant.setDtuPayID(mid);
	}

	@When("the merchant initiates a payment for {bigdecimal} kr by the customer with a fixed certain token")
	public void theMerchantInitiatesAPaymentForKrByTheCustomerWithAFixedCertainToken(BigDecimal amount) {
		try {
			successful = dtuPayMerchant.pay(amount,customerTokens.get(0),merchant.getDtuPayID());
			Payment p = new Payment(customerTokens.get(0), merchant.getDtuPayID(), amount);
			if (!customersPayments.containsKey(customer.getDtuPayID())) customersPayments.put(customer.getDtuPayID(),new ArrayList<>());
			customersPayments.get(customer.getDtuPayID()).add(p);
			payments.add(p);
		} catch (Exception e) {
			successful = false;
			this.e = e;
		}
	}

	//@author s202772 - Gustav Kinch
	@When("the merchant initiates a payment for {bigdecimal} kr by the customer")
	public void theMerchantInitiatesAPaymentForKrByTheCustomer(BigDecimal amount) {
		try {
			successful = dtuPayMerchant.pay(amount,customerTokens.get(used_token_num),merchant.getDtuPayID());
			Payment p = new Payment(customerTokens.get(used_token_num), merchant.getDtuPayID(), amount);
			used_token_num++;
			if (!customersPayments.containsKey(customer.getDtuPayID())) customersPayments.put(customer.getDtuPayID(),new ArrayList<>());
			customersPayments.get(customer.getDtuPayID()).add(p);
			payments.add(p);
		} catch (Exception e) {
			successful = false;
			this.e = e;
		}
	}

	//@author s174293 - Kasper Jørgensen
	@When("the merchant initiates a payment for {bigdecimal} kr by the customer and the second merchant initiates a payment for {bigdecimal} by the second customer")
	public void theMerchantInitiatesAPaymentForKrByTheCustomerAndTheSecondMerchantInitiatesAPaymentForByTheSecondCustomer(BigDecimal amount1, BigDecimal amount2) throws InterruptedException {
		var thread1 = new Thread(() -> {
			try {
				successful = dtuPayMerchant.pay(amount1,customerTokens.get(used_token_num),merchant.getDtuPayID());
				Payment p = new Payment(customerTokens.get(used_token_num), merchant.getDtuPayID(), amount1);
				used_token_num++;
				if (!customersPayments.containsKey(customer.getDtuPayID())) customersPayments.put(customer.getDtuPayID(),new ArrayList<>());
				customersPayments.get(customer.getDtuPayID()).add(p);
				payments.add(p);
			} catch (Exception e) {
				successful = false;
				this.e = e;
				System.out.println(e);
			}
		});
		var thread2 = new Thread(() -> {
			try{
				successful2 = dtuPayMerchant.pay(amount2,customer2Tokens.get(0),merchant2.getDtuPayID());

				Payment p = new Payment(customer2Tokens.get(0), merchant2.getDtuPayID(), amount2);
				if (!customersPayments.containsKey(customer2.getDtuPayID())) customersPayments.put(customer2.getDtuPayID(),new ArrayList<>());
				customersPayments.get(customer2.getDtuPayID()).add(p);
				payments.add(p);
			}catch (Exception e) {
				successful2 = false;
				this.e = e;
				System.out.println(e);
			}
		});
		thread1.start();
		thread2.start();
		thread1.join(100000);
		thread2.join(100000);
	}

	//@author s215949 - Zelin Li
	@When("the merchant initiates a second payment for {bigdecimal} kr by the customer")
	public void theMerchantInitiatesASecondPaymentForKrByTheCustomer(BigDecimal amount) {
		try {
			successful = dtuPayMerchant.pay(amount,customerTokens.get(used_token_num),merchant.getDtuPayID());
			Payment p = new Payment(customerTokens.get(used_token_num), merchant.getDtuPayID(), amount);
			used_token_num++;
			if (!customersPayments.containsKey(customer.getDtuPayID())) customersPayments.put(customer.getDtuPayID(),new ArrayList<>());
			customersPayments.get(customer.getDtuPayID()).add(p);
			payments.add(p);
		} catch (Exception e) {
			successful = false;
			this.e = e;
		}
	}

	//@author s215949 - Zelin Li
	@When("a refund is requested for {bigdecimal} kr")
	public void aRefundIsRequestedForKr(BigDecimal amount) {
		try {
			successful = dtuPayMerchant.refund(amount,customerTokens.get(used_token_num),merchant.getDtuPayID());
			Payment p = new Payment(customerTokens.get(used_token_num), merchant.getDtuPayID(), amount.negate());
			used_token_num++;
			if (!customersPayments.containsKey(customer.getDtuPayID())) customersPayments.put(customer.getDtuPayID(),new ArrayList<>());
			customersPayments.get(customer.getDtuPayID()).add(p);
			payments.add(p);
		} catch (Exception e) {
			successful = false;
			this.e = e;
		}
	}

	//@author s213578 - Johannes Pedersen
	@Then("the payment is successful")
	public void thePaymentIsSuccessful() {
		assertTrue(successful);
	}

	//@author s202772 - Gustav Kinch
	@Then("the second payment is successful")
	public void theSecondPaymentIsSuccessful() {
		assertTrue(successful2);
	}

	//@author s212643 - Xingguang Geng
	@Given("a successful payment of {bigdecimal} kr from customer {string} to merchant {string}")
	public void aSuccessfulPaymentOfKrFromCustomerToMerchant(BigDecimal bigDecimal, String string, String string2) {
		this.customer.setDtuPayID(string);
	    this.merchant.setDtuPayID(string2);
	    try {
			successful = dtuPayMerchant.pay(bigDecimal, customer.getDtuPayID(), merchant.getDtuPayID());
			payments.add(new Payment(customer.getDtuPayID(), merchant.getDtuPayID(), bigDecimal));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	//@author s164422 - Thomas Bergen
	@When("the manager asks for a list of payments")
	public void theManagerAsksForAListOfPayments() {
	    report = dtuPayMerchant.getSuperReport("managerPSW");
	}

	//@author s174293 - Kasper Jørgensen
	@Then("the payment is not successful")
	public void thePaymentIsNotSuccessful() {
	    assertFalse(successful);
	}
	//@author s202772 - Gustav Kinch
	@Then("an error message is returned saying {string}")
	public void anErrorMessageIsReturnedSaying(String string) {
		assertEquals(string, e.getMessage());
	}
    //@author s215949 - Zelin Li
	@Given("that the customer is deleted from dtu.pay")
	public void thatTheCustomerIsDeletedFromDtuPay() throws BankServiceException_Exception {
		bank.retireAccount(customer.getBankID());
		bankAccounts.remove(customer.getBankID());
	}
	//@author s213578 - Johannes Pedersen
	@Given("a successful payment of {bigdecimal} kr")
	public void aSuccessfulPaymentOfKr(BigDecimal amount) throws Exception {
		successful = dtuPayMerchant.pay(amount, customer.getDtuPayID(), merchant.getDtuPayID());
		payments.add(new Payment(customer.getDtuPayID(), merchant.getDtuPayID(), amount));
	}
	//@author s212643 - Xingguang Geng
	@Then("the list contains a payments where customer paid {bigdecimal} kr")
	public void theListContainsAPaymentsWhereCustomerPaidKr(BigDecimal amount) {
		Payment p = new Payment(customer.getDtuPayID(), merchant.getDtuPayID(), amount);
		assertTrue(payments.get(payments.size()-1).equals(p));
	}
	//@author s164422 - Thomas Bergen
	@Given("a customer : {string} that is not registered with DTU pay")
	public void aCustomerThatIsNotRegisteredWithDTUPay(String arg0) {
		this.customer.setDtuPayID(arg0);
	}
	//@author s174293 - Kasper Jørgensen
	@Given("a merchant : {string} that is not registered with DTU pay")
	public void aMerchantThatIsNotRegisteredWithDTUPay(String arg0) {
		this.merchant.setDtuPayID(arg0);
	}
	//@author s202772 - Gustav Kinch
	@Given("that the merchant is deleted from dtu.pay")
	public void thatTheMerchantIsDeletedFromDtuPay() throws BankServiceException_Exception {
		bank.retireAccount(merchant.getBankID());
		bankAccounts.remove(merchant.getBankID());
	}
	//@author s215949 - Zelin Li
	@When("the customer requests to generate {int} tokens")
	public void theCustomerRequestsToGenerateTokens(int arg0) {
		try {
			customerTokens = dtuPayCustomer.getNewTokens(customer.getDtuPayID(), arg0);
		} catch (Exception ex) {
			this.e = ex;
		}
	}

	//@author s213578 - Johannes Pedersen
	@When("both customers requests {int} and {int} tokens respectively")
	public void bothCustomersRequestsAndTokensRespectively(int arg0, int arg1) {
		try {
			customerTokens = dtuPayCustomer.getNewTokens(customer.getDtuPayID(), arg0);
		} catch (Exception ex) {
			this.e = ex;
		}
		try {
			customer2Tokens = dtuPayCustomer.getNewTokens(customer2.getDtuPayID(), arg1);
		} catch (Exception ex2) {
			ex2.printStackTrace();
			this.e2 = ex2;
		}
	}

	//@author s213578 - Johannes Pedersen
	@Then("{int} unique tokens is returned")
	public void uniqueTokensIsReturned(int arg0) {
		assertEquals(arg0, customerTokens.size());
	}

	//@author s212643 - Xingguang Geng
	@Then("{int} unique tokens is returned to the first customer")
	public void uniqueTokensIsReturnedToTheFirstCustomer(int arg0) {
		assertEquals(arg0, customerTokens.size());
	}

	//@author s164422 - Thomas Bergen
	@Then("{int} unique tokens is returned to the second customer")
	public void uniqueTokensIsReturnedToTheSecondCustomer(int arg0) throws InterruptedException {
		TimeUnit.MILLISECONDS.sleep(100);
		assertEquals(arg0, customer2Tokens.size());
	}

	//@author s212643 - Xingguang Geng
	@Given("the customer generates {int} tokens")
	public void theCustomerGeneratesTokens(int amount) {
		try {
			customerTokens = dtuPayCustomer.getNewTokens(customer.getDtuPayID(), amount);
			assertEquals(amount, customerTokens.size());
		} catch (Exception ex) {
			this.e = ex;
		}
	}
	//@author s164422 - Thomas Bergen
	@Given("customer has tokens")
	public void customerHasTokens() throws Exception {
		customerTokens = dtuPayCustomer.getNewTokens(customer.getDtuPayID(), 5);
		assertFalse(customerTokens.isEmpty());
	}

	//@author s212643 - Xingguang Geng
	@Given("the second customer has tokens")
	public void theSecondCustomerHasTokens() throws Exception {
		customer2Tokens = dtuPayCustomer.getNewTokens(customer2.getDtuPayID(), 5);
		assertFalse(customer2Tokens.isEmpty());
	}

	//@author s174293 - Kasper Jørgensen
	@Given("customer has no tokens")
	public void customerHasNoTokens() {
		customerTokens = new ArrayList<>();
		customerTokens.add("invalid token");
	}
	//@author s202772 - Gustav Kinch
	@Then("The report contains every payment made")
	public void theReportContainsEveryPaymentMade() {
		System.out.println(report);
		for (Payment p : payments){
			assertTrue(report.contains(p));
		}
	}

	//@author s215949 - Zelin Li
	//For finding bank accounts and potentially deleting them
	@When("searching")
	public void searching() throws BankServiceException_Exception {
		List<AccountInfo> as = bank.getAccounts();
		for (AccountInfo a : as){
			System.out.print(a.getUser().getFirstName());
			System.out.print(" - " + a.getUser().getLastName());
			System.out.println(" -- " + a.getAccountId());
		}
	}

	//@author s213578 - Johannes Pedersen
	@When("the manager asks for a report")
	public void theManagerAsksForAReport() {
		report = dtuPayMerchant.getSuperReport("managerPSW");
	}
	//@author s212643 - Xingguang Geng
	@When("the customer asks for a report")
	public void theCustomerAsksForAReport() {
		report = dtuPayCustomer.getReport(customer.getDtuPayID());
	}
	//@author s164422 - Thomas Bergen
	@When("the merchant asks for a report")
	public void theMerchantAsksForAReport() {
		report = dtuPayMerchant.getReport(merchant.getDtuPayID());
	}

	//@author s174293 - Kasper Jørgensen
	@Then("the report contains only payments with that merchant")
	public void theReportContainsOnlyPaymentsWithThatMerchant() {
		System.out.println(report);
		for (Payment p : report){  //check that all payments in report belong to current merchant
			assertTrue(payments.contains(p));
			assertEquals(merchant.getDtuPayID(), p.getMerchantID());
		}
		for (Payment p : payments){
			if (p.getMerchantID().equals(merchant.getDtuPayID())){
				assertTrue(report.contains(p));
			}
		}
	}
	//@author s202772 - Gustav Kinch
	@Then("the report contains only payments with that customer")
	public void theReportContainsOnlyPaymentsWithThatCustomer() {
		System.out.println(report);
		for (Payment p : report){  //check that all payments in report belong to current customer
			assertTrue(payments.contains(p));
			assertTrue(customerTokens.contains(p.getCustomerToken()));
		}
		for (Payment p : customersPayments.get(customer.getDtuPayID())){ //check that all our payments are included in report
			assertTrue(report.contains(p));
		}
	}

	@Then("the refund is successful")
	public void theRefundIsSuccessful() {
		assertTrue(successful);
	}

	@And("the report contains records with value of {bigdecimal} kr and {bigdecimal} kr")
	public void theReportContainsRecordsWithValueOfKrAndKr(BigDecimal pay_value, BigDecimal refund_value) {
		boolean have_payment = false, have_refund = false;
		for (Payment p : report){
			if (Objects.equals(p.getAmount(), pay_value)) {
				have_payment = true;
			} else if (Objects.equals(p.getAmount(), refund_value)) {
				have_refund = true;
			}
		}
		assertTrue(have_payment);
		assertTrue(have_refund);
	}
}

