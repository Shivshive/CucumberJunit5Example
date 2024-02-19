@amazon_sample @ui
Feature: search products on amazon

  Scenario: search macbook on amazon
    Given open amazon india website
    And search with product "macbook"

  Scenario: search iphone on amazon
    Given open amazon india website
    And search with product "iphone"