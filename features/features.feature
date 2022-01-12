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
