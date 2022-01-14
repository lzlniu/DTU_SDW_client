Feature: Payment
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
