Feature: Payment
Scenario: Successfully create customer
	Given a customer with a bank account with balance 500
	When the customer registers with DTU Pay
	Then that customer is registered with DTU Pay
	
Scenario: Create customer with no Bank account
	Given a customer with no bank account
	When the customer registers with DTU Pay
	Then an error message is returned saying "must have a bank account to register"	

Scenario: Successfully merchant customer
	Given a merchant with a bank account with balance 500
	When the merchant registers with DTU Pay
	Then that merchant is registered with DTU Pay
	
Scenario: Create merchant with no Bank account
	Given a merchant with no bank account
	When the merchant registers with DTU Pay
	Then an error message is returned saying "must have a bank account to register"		


Scenario: Successful Payment
Given a customer with a bank account with balance 1000
And that the customer is registered with DTU Pay
Given a merchant with a bank account with balance 2000
And that the merchant is registered with DTU Pay
When the merchant initiates a payment for 100 kr by the customer
Then the payment is successful
And the balance of the custoemr at the bank is 900 kr
And the balance of the merchant at the bank is 2100 kr

Scenario: Successful Payment
	Given a customer with id "cid1"
	And a merchant with id "mid1"
	When the merchant initiates a payment for 10 kr by the customer
	Then the payment is successful
	
Scenario: List of payments
	Given a successful payment of 10 kr from customer "cid1" to merchant "mid1"
	When the manager asks for a list of payments
	Then the list contains a payments where customer "cid1" paid 10 kr to merchant "mid1"
	
Scenario: Customer is not known
	Given a customer with id "cid2"
	And a merchant with id "mid1"
	When the merchant initiates a payment for 10 kr by the customer
	Then the payment is not successful
	And an error message is returned saying "customer with id cid2 is unknown"
	
Scenario: Merchant is not known
	Given a customer with id "cid1"
	And a merchant with id "mid2"
	When the merchant initiates a payment for 10 kr by the customer
	Then the payment is not successful
	And an error message is returned saying "merchant with id mid2 is unknown"	