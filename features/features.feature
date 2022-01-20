Feature: User management

  #@author s164422 - Thomas Bergen
  Scenario: Successfully create customer
    Given a customer with a bank account with balance 500.0
    When the customer registers with DTU Pay
    Then that customer is registered with DTU Pay
  #@author s174293 - Kasper Jørgensen
  Scenario: Create customer with no Bank account
    Given a customer with no bank account
    When the customer registers with DTU Pay
    Then an error message is returned saying "must have a bank account to register"
  #@author s202772 - Gustav Kinch
  Scenario: Successfully create merchant
    Given a merchant with a bank account with balance 500.0
    When the merchant registers with DTU Pay
    Then that merchant is registered with DTU Pay
  #@author s215949 - Zelin Li
  Scenario: Create merchant with no Bank account
    Given a merchant with no bank account
    When the merchant registers with DTU Pay
    Then an error message is returned saying "must have a bank account to register"
  #@author s164422 - Thomas Bergen
  Scenario: Delete customer
    Given a customer with a bank account with balance 500.0
    And that the customer is registered with DTU Pay
    Then that customer is registered with DTU Pay
    When the customer is deleted
    Then that customer is not registered with DTU Pay
  #@author s174293 - Kasper Jørgensen
  Scenario: Delete merchant
    Given a merchant with a bank account with balance 500.0
    And that the merchant is registered with DTU Pay
    Then that merchant is registered with DTU Pay
    When the merchant is deleted
    Then that merchant is not registered with DTU Pay
  #@author s213578 - Johannes Pedersen
  Scenario: Generate 4 tokens for new customer
    Given a customer with a bank account with balance 500.0
    And that the customer is registered with DTU Pay
    When the customer requests to generate 4 tokens
    Then 4 unique tokens is returned
  #@author s212643 - Xingguang Geng
  Scenario: Generate 200 tokens for new customer
    Given a customer with a bank account with balance 500.0
    And that the customer is registered with DTU Pay
    When the customer requests to generate 200 tokens
    Then an error message is returned saying "Must create between 1 and 5 tokens"
  #@author s164422 - Thomas Bergen
  Scenario: Customer generates tokens, when they already hase more than 1
    Given a customer with a bank account with balance 500.0
    And that the customer is registered with DTU Pay
    Given the customer generates 4 tokens
    When the customer requests to generate 3 tokens
    Then an error message is returned saying "User already has more than 1 token"
  #@author s174293 - Kasper Jørgensen
  Scenario: Unregistered customer requests tokens
    Given a customer : "customer" that is not registered with DTU pay
    When the customer requests to generate 4 tokens
    Then an error message is returned saying "customer is not registered"
  #@author s202772 - Gustav Kinch
  Scenario: 2 customers requesting tokens at the same time
    Given a customer with name "cus" "tomer" and CPR "823573-6514" and a bank account with balance 1000.0
    And that the customer is registered with DTU Pay
    Given a second customer with name "custo" "mer" and CPR "853573-6514" and a bank account with balance 1000.0
    And that the second customer is registered with DTU Pay
    When both customers requests 3 and 5 tokens respectively
    Then 3 unique tokens is returned to the first customer
    And 5 unique tokens is returned to the second customer
  #@author s215949 - Zelin Li
  Scenario: whole process
    Given a customer with name "Deer" "Li" and CPR "121299-9999" and a bank account with balance 123.4
    And that the customer is registered with DTU Pay
    When the customer requests to generate 3 tokens
    Then 3 unique tokens is returned
    Given a merchant with a bank account with balance 2000.0
    And that the merchant is registered with DTU Pay
    When the merchant initiates a payment for 23.1 kr by the customer
    Then the payment is successful
    And the balance of the customer at the bank is 100.3 kr
    And the balance of the merchant at the bank is 2023.1 kr
    When a refund is requested for 100.0 kr
    Then the refund is successful
    And the balance of the customer at the bank is 200.3 kr
    And the balance of the merchant at the bank is 1923.1 kr
    When the customer asks for a report
    Then the report contains only payments with that customer
    And the report contains records with value of 23.1 kr and -100.0 kr
    When the merchant asks for a report
    Then the report contains only payments with that merchant
    And the report contains records with value of 23.1 kr and -100.0 kr