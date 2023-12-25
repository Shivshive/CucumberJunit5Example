@fruit
Feature: Test Cucumber Feature Sample
  Scenario: put fruits in the basket
    Given there is basket
    And put fruits in the basket
      | Apple  |
      | Banana |
      | Guava  |
    And verify total number of fruits in the basket is 3

