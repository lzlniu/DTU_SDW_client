Feature: Payment

  Scenario: Successfully create customer
    Given a customer with a bank account with balance 500.0
    When the customer registers with DTU Pay
    Then that customer is registered with DTU Pay

  Scenario: Create customer with no Bank account
    Given a customer with no bank account
    When the customer registers with DTU Pay
    Then an error message is returned saying "must have a bank account to register"

  Scenario: Successfully create merchant
    Given a merchant with a bank account with balance 500.0
    When the merchant registers with DTU Pay
    Then that merchant is registered with DTU Pay

  Scenario: Create merchant with no Bank account
    Given a merchant with no bank account
    When the merchant registers with DTU Pay
    Then an error message is returned saying "must have a bank account to register"


  Scenario: Successful Payment
    Given a customer with a bank account with balance 1000.0
    And that the customer is registered with DTU Pay
    Given a merchant with a bank account with balance 2000.0
    And that the merchant is registered with DTU Pay
    When the merchant initiates a payment for 100.0 kr by the customer
    Then the payment is successful
    And the balance of the customer at the bank is 900.0 kr
    And the balance of the merchant at the bank is 2100.0 kr

 #outdated below!
  Scenario: List of payments
    Given a customer with a bank account with balance 1000.0
    And that the customer is registered with DTU Pay
    Given a merchant with a bank account with balance 2000.0
    And that the merchant is registered with DTU Pay
    Given a successful payment of 10.0 kr
    When the manager asks for a list of payments
    Then the list contains a payments where customer paid 10.0 kr

  Scenario: Customer is not known
    Given a customer : "customer" that is not registered with DTU pay
    Given a merchant with a bank account with balance 2000.0
    And that the merchant is registered with DTU Pay
    When the merchant initiates a payment for 10.0 kr by the customer
    Then the payment is not successful
    And an error message is returned saying "customer id is unknown"

  Scenario: Merchant is not known
    Given a merchant : "merchant" that is not registered with DTU pay
    Given a customer with a bank account with balance 2000.0
    And that the customer is registered with DTU Pay
    When the merchant initiates a payment for 10.0 kr by the customer
    Then the payment is not successful
    And an error message is returned saying "merchant id is unknown"

  Scenario: Deleted Customer interacts with bank
    Given a customer with a bank account with balance 4.20
    And that the customer is registered with DTU Pay
    And that the customer is deleted from dtu.pay
    Given a merchant with a bank account with balance 2000.0
    And that the merchant is registered with DTU Pay
    When the merchant initiates a payment for 100.0 kr by the customer
    Then the payment is not successful
    And an error message is returned saying "Debtor account does not exist"

  Scenario: Deleted merchant interacts with bank
    Given a customer with a bank account with balance 4.20
    And that the customer is registered with DTU Pay
    Given a merchant with a bank account with balance 2000.0
    And that the merchant is registered with DTU Pay
    And that the merchant is deleted from dtu.pay
    When the merchant initiates a payment for 100.0 kr by the customer
    Then the payment is not successful
    And an error message is returned saying "Creditor account does not exist"


  Scenario: Customer has insufficient funds
    Given a customer with a bank account with balance 100.0
    And that the customer is registered with DTU Pay
    Given a merchant with a bank account with balance 2000.0
    And that the merchant is registered with DTU Pay
    When the merchant initiates a payment for 100.1 kr by the customer
    Then the payment is not successful
    And an error message is returned saying "Debtor balance will be negative"

