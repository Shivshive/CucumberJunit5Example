@Vegie
Feature: Test Cucumber Feature Sample

  Scenario: put vegetables in the basket
    Given there is basket
    And put fruits in the basket
      | Okra     |
      | Potato   |
      | Eggplant |
    And verify total number of fruits in the basket is 3

