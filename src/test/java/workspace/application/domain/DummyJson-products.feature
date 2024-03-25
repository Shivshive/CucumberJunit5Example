@api
Feature: Products Catalog

  @add-product
  Scenario: Add New Product to the catalog
    Given Add "banana" as a new product
    And verify response status code is one of following 200,201

  @get-product
  Scenario: Get Product from product catalog
    Given Get product with Id 1 from product catalog
    And verify response status code is 200
    And verify product "iPhone 9" is returned from catalog

  @auth-user
  Scenario: Authentication Use Case
    Given Get current auth user
    And verify response status code is 200