Feature: reports
  Background:
    Given a customer with name "cus" "tomer" and CPR "823573-6514" and a bank account with balance 1000.0
    And that the customer is registered with DTU Pay
    Given a merchant with name "Ben" "Gjs" and CPR "383838-1234" and a bank account with balance 2000.0
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
    Given a customer with name "aja" "ja" and CPR "123473-6514" and a bank account with balance 500.0
    And that the customer is registered with DTU Pay
    Given a merchant with name "NO" "Weebs" and CPR "383848-1234" and a bank account with balance 1000.0
    And that the merchant is registered with DTU Pay
    Given customer has tokens
    When the merchant initiates a payment for 100.0 kr by the customer
    Then the payment is successful
    And the balance of the customer at the bank is 400.0 kr
    And the balance of the merchant at the bank is 1100.0 kr

  Scenario: getting super report
    When the manager asks for a report
    Then The report contains every payment made

  Scenario: getting Customer report
    When the customer asks for a report
    Then the report contains only payments with that customer

   Scenario: getting Merchant report
     When the merchant asks for a report
     Then the report contains only payments with that merchant