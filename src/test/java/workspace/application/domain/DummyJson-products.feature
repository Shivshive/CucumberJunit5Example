@api
Feature: Products Catalog
  @add-product
  Scenario: Add New Product to the catalog
    Given Add "banana" as a new product
    And verify response status code is one of following 200,201