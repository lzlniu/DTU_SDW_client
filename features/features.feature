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

  Scenario: Generate 4 tokens for new customer
    Given a customer with a bank account with balance 500.0
    And that the customer is registered with DTU Pay
    When the customer requests to generate 4 tokens
    Then 4 unique tokens is returned

  Scenario: Generate 200 tokens for new customer
    Given a customer with a bank account with balance 500.0
    And that the customer is registered with DTU Pay
    When the customer requests to generate 200 tokens
    Then an error message is returned saying "Must create between 1 and 5 tokens"

  Scenario: Customer generates tokens, when they already hase more than 1
    Given a customer with a bank account with balance 500.0
    And that the customer is registered with DTU Pay
    Given the customer generates 4 tokens
    When the customer requests to generate 3 tokens
    Then an error message is returned saying "User already has more than 1 token"

  Scenario: Unregistered customer requests tokens
    Given a customer : "customer" that is not registered with DTU pay
    When the customer requests to generate 4 tokens
    Then an error message is returned saying "customer is not registered"

  # For finding bank accounts
  Scenario: Show bank accounts
    When searching