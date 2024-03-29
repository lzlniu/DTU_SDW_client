Feature: Payment
  #@author s202772 - Gustav Kinch
  Scenario: Customer successfully pays merchant
    Given a customer with a bank account with balance 1000.0
    And that the customer is registered with DTU Pay
    Given a merchant with a bank account with balance 2000.0
    And that the merchant is registered with DTU Pay
    Given customer has tokens
    When the merchant initiates a payment for 100.0 kr by the customer
    Then the payment is successful
    And the balance of the customer at the bank is 900.0 kr
    And the balance of the merchant at the bank is 2100.0 kr
  #@author s215949 - Zelin Li
  Scenario: Customer pays with nonexistent token
    Given a customer with a bank account with balance 1000.0
    And that the customer is registered with DTU Pay
    Given a merchant with a bank account with balance 2000.0
    And that the merchant is registered with DTU Pay
    Given customer has no tokens
    When the merchant initiates a payment for 100.0 kr by the customer
    Then the payment is not successful
    And an error message is returned saying "Customer token not valid"
  #@author s213578 - Johannes Pedersen
  Scenario: payment with merchant not registered
    Given a customer with a bank account with balance 1000.0
    And that the customer is registered with DTU Pay
    Given a merchant with a bank account with balance 2000.0
    Given customer has tokens
    When the merchant initiates a payment for 100.0 kr by the customer
    Then the payment is not successful
    And an error message is returned saying "merchant id is unknown"
  #@author s212643 - Xingguang Geng
  Scenario: customer pays with same token twice
    Given a customer with a bank account with balance 1000.0
    And that the customer is registered with DTU Pay
    Given a merchant with a bank account with balance 2000.0
    And that the merchant is registered with DTU Pay
    Given customer has tokens
    When the merchant initiates a payment for 100.0 kr by the customer with a fixed certain token
    Then the payment is successful
    And the balance of the customer at the bank is 900.0 kr
    And the balance of the merchant at the bank is 2100.0 kr
    When the merchant initiates a payment for 200.0 kr by the customer with a fixed certain token
    Then the payment is not successful
    And an error message is returned saying "Customer token not valid"
  #@author s164422 - Thomas Bergen
  Scenario: Customer successfully pays merchant twice
    Given a customer with a bank account with balance 1000.0
    And that the customer is registered with DTU Pay
    Given a merchant with a bank account with balance 2000.0
    And that the merchant is registered with DTU Pay
    Given customer has tokens
    When the merchant initiates a payment for 100.0 kr by the customer
    Then the payment is successful
    And the balance of the customer at the bank is 900.0 kr
    And the balance of the merchant at the bank is 2100.0 kr
    When the merchant initiates a second payment for 200.0 kr by the customer
    Then the payment is successful
    And the balance of the customer at the bank is 700.0 kr
    And the balance of the merchant at the bank is 2300.0 kr
  #@author s174293 - Kasper Jørgensen
  Scenario: Customer with deleted bank account interacts with bank
    Given a customer with a bank account with balance 4.20
    And that the customer is registered with DTU Pay
    And that the customer is deleted from dtu.pay
    Given a merchant with a bank account with balance 2000.0
    And that the merchant is registered with DTU Pay
    Given customer has tokens
    When the merchant initiates a payment for 100.0 kr by the customer
    Then the payment is not successful
    And an error message is returned saying "Debtor account does not exist"
  #@author s202772 - Gustav Kinch
  Scenario: Merchant with deleted bank account  interacts with bank
    Given a customer with a bank account with balance 4.20
    And that the customer is registered with DTU Pay
    Given a merchant with a bank account with balance 2000.0
    And that the merchant is registered with DTU Pay
    And that the merchant is deleted from dtu.pay
    Given customer has tokens
    When the merchant initiates a payment for 100.0 kr by the customer
    Then the payment is not successful
    And an error message is returned saying "Creditor account does not exist"
  #@author s215949 - Zelin Li
  Scenario: Customer has insufficient funds
    Given a customer with a bank account with balance 100.0
    And that the customer is registered with DTU Pay
    Given a merchant with a bank account with balance 2000.0
    And that the merchant is registered with DTU Pay
    Given customer has tokens
    When the merchant initiates a payment for 100.01 kr by the customer
    Then the payment is not successful
    And an error message is returned saying "Debtor balance will be negative"
  #@author s213578 - Johannes Pedersen
  Scenario: Two concurrent payments
    Given a customer with name "cus" "tomer" and CPR "823573-6514" and a bank account with balance 1000.0
    And that the customer is registered with DTU Pay
    Given a merchant with name "Ben" "Gjs" and CPR "383838-1234" and a bank account with balance 2000.0
    And that the merchant is registered with DTU Pay
    Given customer has tokens
    Given a second customer with name "cust" "omer" and CPR "825573-6114" and a bank account with balance 1000.0
    And that the second customer is registered with DTU Pay
    Given the second customer has tokens
    Given a second merchant with name "Benny" "Gjis" and CPR "352838-1524" and a bank account with balance 2000.0
    And that the second merchant is registered with DTU Pay
    When the merchant initiates a payment for 100.01 kr by the customer and the second merchant initiates a payment for 372.03 by the second customer
    Then the payment is successful
    Then the second payment is successful
    And the balance of the customer at the bank is 899.99 kr
    And the balance of the merchant at the bank is 2100.01 kr
    And the balance of the second customer at the bank is 627.97 kr
    And the balance of the second merchant at the bank is 2372.03 kr

  #@author s213578 - Johannes Pedersen
  Scenario: Successful refund
    Given a customer with name "cus" "tomer" and CPR "823573-6514" and a bank account with balance 1000.0
    And that the customer is registered with DTU Pay
    Given a merchant with name "Ben" "Gjs" and CPR "383838-1234" and a bank account with balance 2000.0
    And that the merchant is registered with DTU Pay
    Given customer has tokens
    When a refund is requested for 100.00 kr
    Then the refund is successful
    And the balance of the customer at the bank is 1100.00 kr
    And the balance of the merchant at the bank is 1900.00 kr

