package com.yaps.petstore.domain.service;

import java.util.List;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.context.junit4.SpringRunner;

import com.yaps.petstore.domain.dto.CategoryDTO;
import com.yaps.petstore.domain.dto.ItemDTO;
import com.yaps.petstore.domain.dto.ProductDTO;
import com.yaps.petstore.exception.CreateException;
import com.yaps.petstore.exception.DuplicateKeyException;
import com.yaps.petstore.exception.FinderException;
import com.yaps.petstore.exception.RemoveException;
import com.yaps.petstore.exception.UpdateException;

import junit.framework.TestCase;

/**
 * This class tests the CatalogService class
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class CatalogServiceTest extends TestCase {
	
	
	@Autowired
	private CatalogService service;
	
	@Autowired
    private CounterService counterService;

    //==================================
    //=   Test cases for Category      =
    //==================================
    /**
     * This test tries to find an object with a invalid identifier.
     */
	@Test
    public void testServiceFindCategoryWithInvalidValues() throws Exception {
    	
        // Finds an object with a unknown identifier
        final String id = counterService.getUniqueId("Category");
        try {
            service.findCategory(id);
            fail("Object with unknonw id should not be found");
        } catch (FinderException e) {
        }

        // Finds an object with an empty identifier
        try {
            service.findCategory(new String());
            fail("Object with empty id should not be found");
        } catch (FinderException e) {
        }

        // Finds an object with a null identifier
        try {
            service.findCategory(null);
            fail("Object with null id should not be found");
        } catch (FinderException e) {
        }
    }

    /**
     * This test ensures that the method findAll works. It does a first findAll, creates
     * a new object and does a second findAll.
     */
	@Test
    public void testServiceFindAllCategories() throws Exception {
        final String id = counterService.getUniqueId("Category");

        // First findAll
        final int firstSize = findAllCategories();

        // Creates an object
        createCategory(id);

        // Ensures that the object exists
        try {
            findCategory(id);
        } catch (FinderException e) {
            fail("Object has been created it should be found");
        }

        // Second findAll
        final int secondSize = findAllCategories();

        // Checks that the collection size has increase of one
        if (firstSize + 1 != secondSize) fail("The collection size should have increased by 1");

        // Cleans the test environment
        deleteCategory(id);

        try {
            findCategory(id);
            fail("Object has been deleted it shouldn't be found");
        } catch (FinderException e) {
        }
    }

    /**
     * This method ensures that creating an object works. It first finds the object,
     * makes sure it doesn't exist, creates it and checks it then exists.
     */
	@Test
    public void testServiceCreateCategory() throws Exception {
        final String id = counterService.getUniqueId("Category");
        CategoryDTO categoryDTO = null;

        // Ensures that the object doesn't exist
        try {
            findCategory(id);
            fail("Object has not been created yet it shouldn't be found");
        } catch (FinderException e) {
        }

        // Creates an object
        createCategory(id);

        // Ensures that the object exists
        try {
        	categoryDTO = findCategory(id);
        } catch (FinderException e) {
            fail("Object has been created it should be found");
        }

        // Checks that it's the right object
        checkCategory(categoryDTO, id);

        // Creates an object with the same identifier. An exception has to be thrown
        try {
            createCategory(id);
            fail("An object with the same id has already been created");
        } catch (DuplicateKeyException e) {
        }

        // Cleans the test environment
        deleteCategory(id);

        try {
            findCategory(id);
            fail("Object has been deleted it shouldn't be found");
        } catch (FinderException e) {
        }
    }

    /**
     * This test tries to create an object with a invalid values.
     */
    @Test
    public void testServiceCreateCategoryWithInvalidValues() throws Exception {
    	CategoryDTO categoryDTO;

        // Creates an object with a null parameter
        try {
            service.createCategory(null);
            fail("Object with null parameter should not be created");
        } catch (CreateException e) {
        }

        // Creates an object with empty values
        try {
        	categoryDTO = new CategoryDTO(new String(), new String(), new String());
            service.createCategory(categoryDTO);
            fail("Object with empty values should not be created");
        } catch (Exception e) {
        	assertTrue("Il devrait s'agir d'une  CreateException",e instanceof CreateException);
        }

        // Creates an object with null values
        try {
        	categoryDTO = new CategoryDTO(null, null, null);
            service.createCategory(categoryDTO);
            fail("Object with null values should not be created");
        } catch (Exception e) {
        	assertTrue("Il devrait s'agir d'une  CreateException",e instanceof CreateException);
        }
    }

    /**
     * This test make sure that updating an object success
     */
    @Test
    public void testServiceUpdateCategory() throws Exception {
        final String id = counterService.getUniqueId("Category");
        final String updatePattern = id + "_updated";

        // Creates an object
        createCategory(id);

        // Ensures that the object exists
        CategoryDTO categoryDTO = null;
        try {
        	categoryDTO = findCategory(id);
        } catch (FinderException e) {
            fail("Object has been created it should be found");
        }

        // Checks that it's the right object
        checkCategory(categoryDTO, id);

        // Updates the object with new values
        updateCategory(categoryDTO, updatePattern);

        // Ensures that the object still exists
        CategoryDTO categoryUpdated = null;
        try {
            categoryUpdated = findCategory(id);
        } catch (FinderException e) {
            fail("Object should be found");
        }

        // Checks that the object values have been updated
        checkCategory(categoryUpdated, updatePattern);

        // Cleans the test environment
        deleteCategory(id);

        try {
            findCategory(id);
            fail("Object has been deleted it shouldn't be found");
        } catch (FinderException e) {
        }
    }

    /**
     * This test tries to update an object with a invalid values.
     */
    @Test
    public void testServiceUpdateCategoryWithInvalidValues() throws Exception {
    	CategoryDTO categoryDTO;

        // Updates an object with a null parameter
        try {
            service.updateCategory(null);
            fail("Object with null parameter should not be updated");
        } catch (UpdateException e) {
        }

        // Updates an object with empty values
        try {
        	categoryDTO = new CategoryDTO(new String(), new String(), new String());
            service.updateCategory(categoryDTO);
            fail("Object with empty values should not be updated");
        } catch (Exception e) {
        	assertTrue("Il devrait s'agir d'une  UpdateException",e instanceof UpdateException);
        }

        // Updates an object with null values
        try {
        	categoryDTO = new CategoryDTO(null, null, null);
            service.updateCategory(categoryDTO);
            fail("Object with null values should not be updated");
        } catch (Exception e) {
        	assertTrue("Il devrait s'agir d'une  InvalidDataAccessApiUsageException",e instanceof InvalidDataAccessApiUsageException);
        }
    }

    /**
     * This test ensures that the system cannont remove an unknown object
     */
    @Test
    public void testServiceDeleteUnknownCategory() throws Exception {
        final String id = counterService.getUniqueId("Category");

        // Ensures that the object doesn't exist
        try {
            findCategory(id);
            fail("Object has not been created it shouldn't be found");
        } catch (FinderException e) {
        }

        // Delete the unknown object
        try {
            deleteCategory(id);
            fail("Deleting an unknown object should break");
        } catch (RemoveException e) {
        }
    }


    //==================================
    //=    Test cases for product      =
    //==================================
    /**
     * This test tries to find an object with a invalid identifier.
     */
    @Test
    public void testServiceFindProductWithInvalidValues() throws Exception {

        // Finds an object with a unknown identifier
        final String id = counterService.getUniqueId("Product");
        try {
            service.findProduct(id);
            fail("Object with unknonw id should not be found");
        } catch (FinderException e) {
        }

        // Finds an object with an empty identifier
        try {
            service.findProduct(new String());
            fail("Object with empty id should not be found");
        } catch (FinderException e) {
        }

        // Finds an object with a null identifier
        try {
            service.findProduct(null);
            fail("Object with null id should not be found");
        } catch (FinderException e) {
        }
    }

    /**
     * This test ensures that the method findAll works. It does a first findAll, creates
     * a new object and does a second findAll.
     */
    @Test
    public void testServiceFindAllProducts() throws Exception {
        final String id = counterService.getUniqueId("Product");

        // First findAll
        final int firstSize = findAllProducts();

        // Creates an object
        createProduct(id);

        // Ensures that the object exists
        try {
            findProduct(id);
        } catch (FinderException e) {
            fail("Object has been created it should be found");
        }

        // Second findAll
        final int secondSize = findAllProducts();

        // Checks that the collection size has increase of one
        if (firstSize + 1 != secondSize) fail("The collection size should have increased by 1");

        // Cleans the test environment
        deleteProduct(id);

        try {
            findProduct(id);
            fail("Object has been deleted it shouldn't be found");
        } catch (FinderException e) {
        }
    }

    /**
     * This test ensures that the method findAll works. It does a first findAll, creates
     * a new object and does a second findAll.
     */
    @Test
    public void testServiceFindAllProductsForACategory() throws Exception {
    	CategoryDTO newCategoryDTO = createNewCategory();
    	final String categoryId = newCategoryDTO.getId();

        // First findAll
        final int firstSize = findAllProducts(categoryId);

        // Checks that the collection is empty
        if (firstSize != 0) fail("The collection should be empty");

        // Create an object
        ProductDTO productDTO = createProductForCategory(newCategoryDTO);

        // Ensures that the object exists
        try {
            findProduct(productDTO.getId());
        } catch (FinderException e) {
            fail("Object has been created it should be found");
        }

        // second findAll
        final int secondSize = findAllProducts(categoryId);

        // Checks that the collection size has increase of one
        if (firstSize + 1 != secondSize) fail("The collection size should have increased by 1");

        // Cleans the test environment
        deleteProduct(productDTO.getId());
    }

    /**
     * This method ensures that creating an object works. It first finds the object,
     * makes sure it doesn't exist, creates it and checks it then exists.
     */
    @Test
    public void testServiceCreateProduct() throws Exception {
    	final String id = counterService.getUniqueId("Product");
        ProductDTO productDTO = null;

        // Ensures that the object doesn't exist
        try {
            findProduct(id);
            fail("Object has not been created yet it shouldn't be found");
        } catch (FinderException e) {
        }

        // Creates an object
        createProduct(id);

        // Ensures that the object exists
        try {
        	productDTO = findProduct(id);
        } catch (FinderException e) {
            fail("Object has been created it should be found");
        }

        // Checks that it's the right object
        checkProduct(productDTO, id);

        // Creates an object with the same identifier. An exception has to be thrown
        try {
            createProduct(id);
            fail("An object with the same id has already been created");
        } catch (DuplicateKeyException e) {
        }

        // Cleans the test environment
        deleteProduct(id);

        try {
            findProduct(id);
            fail("Object has been deleted it shouldn't be found");
        } catch (FinderException e) {
        }
    }

    /**
     * This test tries to create an object with a invalid values.
     */
    @Test
    public void testServiceCreateProductWithInvalidValues() throws Exception {
    	ProductDTO productDTO;

        // Creates an object with a null parameter
        try {
            service.createProduct(null);
            fail("Object with null parameter should not be created");
        } catch (CreateException e) {
        }

        // Creates an object with empty values
        try {
        	productDTO = new ProductDTO(new String(), new String(), new String());
            service.createProduct(productDTO);
            fail("Object with empty values should not be created");
        } catch (Exception e) {
        	assertTrue("Il devrait s'agir d'une  CreateException",e instanceof CreateException);
        }

        // Creates an object with null values
        try {
        	productDTO = new ProductDTO(null, null, null);
            service.createProduct(productDTO);
            fail("Object with null values should not be created");
        } catch (Exception e) {
        	assertTrue("Il devrait s'agir d'une  CreateException",e instanceof CreateException);
        }
    }

    /**
     * This test tries to create an object with a invalid linked object.
     */
    @Test
    public void testServiceCreateProductWithInvalidCategory() throws Exception {
    	final String id = counterService.getUniqueId("Product");
        ProductDTO productDTO;
        
     // Creates an object with no object linked
        try {
        	productDTO = new ProductDTO(id, "name" + id, "description" + id);
            service.createProduct(productDTO);
            fail("Object with no object linked should not be created");
        } catch (CreateException e) {
        }

        // Creates an object with a null linked object
        try {
        	productDTO = new ProductDTO(id, "name" + id, "description" + id);
            service.createProduct(productDTO);
            fail("Object with null object linked should not be created");
        } catch (Exception e) {
        	assertTrue("Il devrait s'agir d'une  CreateException",e instanceof CreateException);
        }

        // Creates an object with an empty linked object
        try {
        	productDTO = new ProductDTO(id, "name" + id, "description" + id);
            service.createProduct(productDTO);
            fail("Object with an empty object linked should not be created");
        } catch (CreateException e) {
        }

        // Creates an object with an unknown linked object
        try {
        	productDTO = new ProductDTO(id, "name" + id, "description" + id);
            service.createProduct(productDTO);
            fail("Object with an unknown object linked should not be created");
        } catch (CreateException e) {
        }
    }

    /**
     * This test make sure that updating an object success
     */
    @Test
    public void testServiceUpdateProduct() throws Exception {
    	final String id = counterService.getUniqueId("Product");
        final String updatePattern = id + "_updated";

        // Creates an object
        createProduct(id);

        // Ensures that the object exists
        ProductDTO productDTO = null;
        try {
        	productDTO = findProduct(id);
        } catch (FinderException e) {
            fail("Object has been created it should be found");
        }

        // Checks that it's the right object
        checkProduct(productDTO, id);

        // Updates the object with new values
        updateProduct(productDTO, updatePattern);

        // Ensures that the object still exists
        ProductDTO productDTOUpdated = null;
        try {
        	productDTOUpdated = findProduct(id);
        } catch (FinderException e) {
            fail("Object should be found");
        }

        // Checks that the object values have been updated
        checkProduct(productDTOUpdated, updatePattern);

        // Cleans the test environment
        deleteProduct(id);

        try {
            findProduct(id);
            fail("Object has been deleted it shouldn't be found");
        } catch (FinderException e) {
        }
    }

    /**
     * This test tries to update an object with a invalid values.
     */
    @Test
    public void testServiceUpdateProductWithInvalidValues() throws Exception {
    	 ProductDTO productDTO;

         // Updates an object with a null parameter
         try {
             service.updateProduct(null);
             fail("Object with null parameter should not be updated");
         } catch (UpdateException e) {
         }

         // Updates an object with empty values
         try {
         	productDTO = new ProductDTO(new String(), new String(), new String());
             service.updateProduct(productDTO);
             fail("Object with empty values should not be updated");
         } catch (Exception e) {
         	assertTrue("Il devrait s'agir d'une  UpdateException",e instanceof UpdateException);
         }

         // Updates an object with null values
         try {
         	productDTO = new ProductDTO(null, null, null);
             service.updateProduct(productDTO);
             fail("Object with null values should not be updated");
         } catch (Exception e) {
         	assertTrue("Il devrait s'agir d'une  InvalidDataAccessApiUsageException",e instanceof InvalidDataAccessApiUsageException);
         }
    }

    /**
     * This test ensures that the system cannont remove an unknown object
     */
    @Test
    public void testServiceDeleteUnknownProduct() throws Exception {
        final String id = counterService.getUniqueId("Product");

        // Ensures that the object doesn't exist
        try {
            findProduct(id);
            fail("Object has not been created it shouldn't be found");
        } catch (FinderException e) {
        }

        // Delete the unknown object
        try {
            deleteProduct(id);
            fail("Deleting an unknown object should break");
        } catch (FinderException | RemoveException e) {
        }
    }

    //==================================
    //=      Test cases for item       =
    //==================================
    /**
     * This test tries to find an object with a invalid identifier.
     */
    @Test
    public void testServiceFindItemWithInvalidValues() throws Exception {

        // Finds an object with a unknown identifier
        final String id = counterService.getUniqueId("Item");
        try {
            service.findItem(id);
            fail("Object with unknonw id should not be found");
        } catch (FinderException e) {
        }

        // Finds an object with an empty identifier
        try {
            service.findItem(new String());
            fail("Object with empty id should not be found");
        } catch (FinderException e) {
        }

        // Finds an object with a null identifier
        try {
            service.findItem(null);
            fail("Object with null id should not be found");
        } catch (FinderException e) {
        }
    }

    /**
     * This test ensures that the method findAll works. It does a first findAll, creates
     * a new object and does a second findAll.
     */
    @Test
    public void testServiceFindAllItems() throws Exception {
        final String id = counterService.getUniqueId("Item");

        // First findAll
        final int firstSize = findAllItems();

        // Creates an object
        createItem(id);

        // Ensures that the object exists
        try {
            findItem(id);
        } catch (FinderException e) {
            fail("Object has been created it should be found");
        }

        // Second findAll
        final int secondSize = findAllItems();

        // Checks that the collection size has increase of one
        if (firstSize + 1 != secondSize) fail("The collection size should have increased by 1");

        // Cleans the test environment
        deleteItem(id);

        try {
            findItem(id);
            fail("Object has been deleted it shouldn't be found");
        } catch (FinderException e) {
        }
    }

    @Test
    public void testServiceFindAllItemsForAmazonParrot() throws Exception {
    	final String productId = "AVCB01";
    	final String name ="Amazon Parrot";
    	final String description = "Great companion for up to 75 years";
    	ProductDTO productDTO = findProduct(productId);
    	assertEquals(name, productDTO.getName());
    	assertEquals(description, productDTO.getDescription());
    	List<ItemDTO> itemDTOs = getAllItemsForProduct(productId);
        for (ItemDTO itemDTO : itemDTOs) {
        	assertNotNull(itemDTO.getProductDTO().getDescription());
        }
    }

    /**
     * This test ensures that the method findAll works. It does a first findAll, creates
     * a new object and does a second findAll.
     */
    @Test
    public void testServiceFindAllItemsForAProduct() throws Exception {
    	ProductDTO newProductDTO = createNewProduct();
    	final String productId = newProductDTO.getId();

        // First findAll
        final int firstSize = findAllItems(productId);

        // Checks that the collection is empty
        if (firstSize != 0) fail("The collection should be empty");

        // Create an object
        ItemDTO itemDTO = createItemForProduct(newProductDTO);

        // Ensures that the object exists
        try {
            findItem(itemDTO.getId());
        } catch (FinderException e) {
            fail("Object has been created it should be found");
        }

        // second findAll
        final int secondSize = findAllItems(productId);

        // Checks that the collection size has increase of one
        if (firstSize + 1 != secondSize) fail("The collection size should have increased by 1");

        // Cleans the test environment
        deleteItem(itemDTO.getId());
    }
    
    /**
     * This test ensures that the method search works. It does a first search, creates
     * a new object and does a second search.
     */
    @Test
    public void testServiceSearchItems() throws Exception {
    	final String id = counterService.getUniqueId("Item");

        // First search
        final int firstSize = searchItems(id);

        // Creates an object
        createItem(id);

        // Ensures that the object exists
        try {
            findItem(id);
        } catch (FinderException e) {
            fail("Object has been created it should be found");
        }

        // Second search
        final int secondSize = searchItems(id);

        // Checks that the collection size has increase of one
        if (firstSize + 1 != secondSize) 
        	fail("The collection size should have increased by 1");

        // Cleans the test environment
        deleteItem(id);

        try {
            findItem(id);
            fail("Object has been deleted it shouldn't be found");
        } catch (FinderException e) {
        }
    }
    
    @Test
    public void testServiceSearchItemsWithPriceSupTo() throws Exception {
    	final String id = counterService.getUniqueId("Item");
    	final double price = 100;

        // First search
        final int firstSize = searchItemsForPrice("GT",price);

        // Creates an object
        createItem(id);

        // Ensures that the object exists
        try {
        	ItemDTO itemDTO = findItem(id);
        	itemDTO.setUnitCost(price+1);
            service.updateItem(itemDTO);
        } catch (FinderException e) {
            fail("Object has been created it should be found");
        }

        // Second search
        final int secondSize = searchItemsForPrice("GT",price);

        // Checks that the collection size has increase of one
        if (firstSize + 1 != secondSize) 
        	fail("The collection size should have increased by 1");

        // Cleans the test environment
        deleteItem(id);

        try {
            findItem(id);
            fail("Object has been deleted it shouldn't be found");
        } catch (FinderException e) {
        }
    }

	@Test
    public void testServiceSearchItemsWithPriceInfTo() throws Exception {
    	final String id = counterService.getUniqueId("Item");
    	final double price = 100;

        // First search
        final int firstSize = searchItemsForPrice("LT",price);

        // Creates an object
        createItem(id);

        // Ensures that the object exists
        try {
        	ItemDTO itemDTO = findItem(id);
        	itemDTO.setUnitCost(price-1);
            service.updateItem(itemDTO);
        } catch (FinderException e) {
            fail("Object has been created it should be found");
        }

        // Second search
        final int secondSize = searchItemsForPrice("LT",price);

        // Checks that the collection size has increase of one
        if (firstSize + 1 != secondSize) 
        	fail("The collection size should have increased by 1");

        // Cleans the test environment
        deleteItem(id);

        try {
            findItem(id);
            fail("Object has been deleted it shouldn't be found");
        } catch (FinderException e) {
        }
    }

    @Test
    public void testServiceSearchItemsWithPriceEqualTo() throws Exception {
    	final String id = counterService.getUniqueId("Item");
    	final double price = 100;

        // First search
        final int firstSize = searchItemsForPrice("EQ",price);

        // Creates an object
        createItem(id);

        // Ensures that the object exists
        try {
        	ItemDTO itemDTO = findItem(id);
        	itemDTO.setUnitCost(price);
            service.updateItem(itemDTO);
        } catch (FinderException e) {
            fail("Object has been created it should be found");
        }

        // Second search
        final int secondSize = searchItemsForPrice("EQ",price);

        // Checks that the collection size has increase of one
        if (firstSize + 1 != secondSize) 
        	fail("The collection size should have increased by 1");

        // Cleans the test environment
        deleteItem(id);

        try {
            findItem(id);
            fail("Object has been deleted it shouldn't be found");
        } catch (FinderException e) {
        }
    }
    
    @Test
    public void testServiceSearchItemsWithPriceSupToAndKeyword() throws Exception {
    	final String id = counterService.getUniqueId("Item");
    	final double price = 100;

        // First search
        final int firstSize = searchItemsForPriceForKeyword("GT",price,id);

        // Creates an object
        createItem(id);

        // Ensures that the object exists
        try {
        	ItemDTO itemDTO = findItem(id);
        	itemDTO.setUnitCost(price+1);
            service.updateItem(itemDTO);
        } catch (FinderException e) {
            fail("Object has been created it should be found");
        }

        // Second search
        final int secondSize = searchItemsForPriceForKeyword("GT",price,id);

        // Checks that the collection size has increase of one
        if (firstSize + 1 != secondSize) 
        	fail("The collection size should have increased by 1");

        // Cleans the test environment
        deleteItem(id);

        try {
            findItem(id);
            fail("Object has been deleted it shouldn't be found");
        } catch (FinderException e) {
        }
    }
    
    @Test
    public void testServiceSearchItemsWithPriceInfToAndKeyword() throws Exception {
    	final String id = counterService.getUniqueId("Item");
    	final double price = 100;

        // First search
        final int firstSize = searchItemsForPriceForKeyword("LT",price,id);

        // Creates an object
        createItem(id);

        // Ensures that the object exists
        try {
        	ItemDTO itemDTO = findItem(id);
        	itemDTO.setUnitCost(price-1);
            service.updateItem(itemDTO);
        } catch (FinderException e) {
            fail("Object has been created it should be found");
        }

        // Second search
        final int secondSize = searchItemsForPriceForKeyword("LT",price,id);

        // Checks that the collection size has increase of one
        if (firstSize + 1 != secondSize) 
        	fail("The collection size should have increased by 1");

        // Cleans the test environment
        deleteItem(id);

        try {
            findItem(id);
            fail("Object has been deleted it shouldn't be found");
        } catch (FinderException e) {
        }
    }
    
    @Test
    public void testServiceSearchItemsWithPriceEqToAndKeyword() throws Exception {
    	final String id = counterService.getUniqueId("Item");
    	final double price = 100;

        // First search
        final int firstSize = searchItemsForPriceForKeyword("EQ",price,id);

        // Creates an object
        createItem(id);

        // Ensures that the object exists
        try {
        	ItemDTO itemDTO = findItem(id);
        	itemDTO.setUnitCost(price);
            service.updateItem(itemDTO);
        } catch (FinderException e) {
            fail("Object has been created it should be found");
        }

        // Second search
        final int secondSize = searchItemsForPriceForKeyword("EQ",price,id);

        // Checks that the collection size has increase of one
        if (firstSize + 1 != secondSize) 
        	fail("The collection size should have increased by 1");

        // Cleans the test environment
        deleteItem(id);

        try {
            findItem(id);
            fail("Object has been deleted it shouldn't be found");
        } catch (FinderException e) {
        }
    }

    /**
     * This method ensures that creating an object works. It first finds the object,
     * makes sure it doesn't exist, creates it and checks it then exists.
     */
    @Test
    public void testServiceCreateItem() throws Exception {
    	final String id = counterService.getUniqueId("Item");
        ItemDTO itemDTO = null;

        // Ensures that the object doesn't exist
        try {
            findItem(id);
            fail("Object has not been created yet it shouldn't be found");
        } catch (FinderException e) {
        }

        // Creates an object
        createItem(id);

        // Ensures that the object exists
        try {
        	itemDTO = findItem(id);
        } catch (FinderException e) {
            fail("Object has been created it should be found");
        }

        // Checks that it's the right object
        checkItem(itemDTO, id);

        // Cleans the test environment
        deleteItem(id);

        try {
            findItem(id);
            fail("Object has been deleted it shouldn't be found");
        } catch (FinderException e) {
        }
    }

    /**
     * This test tries to create an object with a invalid values.
     */
    @Test
    public void testServiceCreateItemWithInvalidValues() throws Exception {
    	ItemDTO itemDTO;

        // Creates an object with a null parameter
        try {
            service.createItem(null);
            fail("Object with null parameter should not be created");
        } catch (CreateException e) {
        }

        // Creates an object with empty values
        try {
        	itemDTO = new ItemDTO(new String(), new String(), 0);
            service.createItem(itemDTO);
            fail("Object with empty values should not be created");
        } catch (Exception e) {
        	assertTrue("Il devrait s'agir d'une  CreateException",e instanceof CreateException);
        }

        // Creates an object with null values
        try {
        	itemDTO = new ItemDTO(null, null, 0);
            service.createItem(itemDTO);
            fail("Object with null values should not be created");
        } catch (Exception e) {
        	assertTrue("Il devrait s'agir d'une  CreateException",e instanceof CreateException);
        }
    }

    /**
     * This test tries to create an object with a invalid linked object.
     */
    @Test
    public void testServiceCreateItemWithInvalidProduct() throws Exception {
    	final String id = counterService.getUniqueId("Item");
        ItemDTO itemDTO;
        
     // Creates an object with no object linked
        try {
        	itemDTO = new ItemDTO(id, "name" + id, 0);
            service.createItem(itemDTO);
            fail("Object with no object linked should not be created");
        } catch (Exception e) {
        	assertTrue("Il devrait s'agir d'une  CreateException",e instanceof CreateException);
        }

        // Creates an object with a null linked object
        try {
        	itemDTO = new ItemDTO(id, "name" + id, 0);
            service.createItem(itemDTO);
            fail("Object with null object linked should not be created");
        } catch (Exception e) {
        	assertTrue("Il devrait s'agir d'une  CreateException",e instanceof CreateException);
        }

        // Creates an object with an empty linked object
        try {
        	itemDTO = new ItemDTO(id, "name" + id, 0);
            service.createItem(itemDTO);
            fail("Object with an empty object linked should not be created");
        } catch (Exception e) {
        	assertTrue("Il devrait s'agir d'une  CreateException",e instanceof CreateException);
        }

        // Creates an object with an unknown linked object
        try {
        	itemDTO = new ItemDTO(id, "name" + id, 0);
            service.createItem(itemDTO);
            fail("Object with an unknown object linked should not be created");
        } catch (Exception e) {
        	assertTrue("Il devrait s'agir d'une  CreateException",e instanceof CreateException);
        }
    }

    /**
     * This test make sure that updating an object success
     */
    @Test
    public void testServiceUpdateItem() throws Exception {
    	final String id = counterService.getUniqueId("Item");
        final String updatePattern = id + "_updated";

        // Creates an object
        createItem(id);

        // Ensures that the object exists
        ItemDTO itemDTO = null;
        try {
        	itemDTO = findItem(id);
        } catch (FinderException e) {
            fail("Object has been created it should be found");
        }

        // Checks that it's the right object
        checkItem(itemDTO, id);

        // Updates the object with new values
        updateItem(itemDTO, updatePattern);

        // Ensures that the object still exists
        ItemDTO itemDTOUpdated = null;
        try {
        	itemDTOUpdated = findItem(id);
        } catch (FinderException e) {
            fail("Object should be found");
        }

        // Checks that the object values have been updated
        checkItem(itemDTOUpdated, updatePattern);

        // Cleans the test environment
        deleteItem(id);

        try {
            findItem(id);
            fail("Object has been deleted it shouldn't be found");
        } catch (FinderException e) {
        }
    }

    /**
     * This test tries to update an object with a invalid values.
     */
    @Test
    public void testServiceUpdateItemWithInvalidValues() throws Exception {
    	ItemDTO itemDTO;

        // Updates an object with a null parameter
        try {
            service.updateItem(null);
            fail("Object with null parameter should not be updated");
        } catch (UpdateException e) {
        }

        // Updates an object with empty values
        try {
        	itemDTO = new ItemDTO(new String(), new String(), 0);
            service.updateItem(itemDTO);
            fail("Object with empty values should not be updated");
        } catch (Exception e) {
        	assertTrue("Il devrait s'agir d'une UpdateException",e instanceof UpdateException);
        }

        // Updates an object with null values
        try {
        	itemDTO = new ItemDTO(null, null, 0);
            service.updateItem(itemDTO);
            fail("Object with null values should not be updated");
        } catch (Exception e) {
        	assertTrue("Il devrait s'agir d'une InvalidDataAccessApiUsageException",e instanceof InvalidDataAccessApiUsageException);
        }
    }

    /**
     * This test ensures that the system cannont remove an unknown object
     */
    @Test
    public void testServiceDeleteUnknownItem() throws Exception {
        final String id = counterService.getUniqueId("Item");

        // Ensures that the object doesn't exist
        try {
            findItem(id);
            fail("Object has not been created it shouldn't be found");
        } catch (FinderException e) {
        }

        // Delete the unknown object
        try {
            deleteItem(id);
            fail("Deleting an unknown object should break");
        } catch (FinderException e) {
        } catch (RemoveException e) {
        }
    }

    //==================================
    //=         Private Methods        =
    //==================================

    //==================================
    //=  Private Methods for Category  =
    //==================================
    private CategoryDTO findCategory(final String id) throws FinderException {
        final CategoryDTO categoryDTO = service.findCategory(id);
        return categoryDTO;
    }

    private int findAllCategories() throws FinderException {
        try {
            return service.findCategories().size();
        } catch (FinderException e) {
            return 0;
        }
    }

    private void createCategory(final String id) throws CreateException {
        final CategoryDTO categoryDTO = new CategoryDTO(id, "name" + id, "description" + id);
        service.createCategory(categoryDTO);
    }

    private void updateCategory(final CategoryDTO categoryDTO, final String updatePattern) throws UpdateException {
        categoryDTO.setName("name" + updatePattern);
        categoryDTO.setDescription("description" + updatePattern);
        service.updateCategory(categoryDTO);
    }

    private void deleteCategory(final String id) throws RemoveException, FinderException {
        service.deleteCategory(id);
    }

    private void checkCategory(final CategoryDTO categoryDTO, final String id) {
        assertEquals("name", "name" + id, categoryDTO.getName());
        assertEquals("description", "description" + id, categoryDTO.getDescription());
    }

    //==================================
    //=  Private Methods for Product   =
    //==================================
    private ProductDTO findProduct(final String id) throws FinderException {
        final ProductDTO productDTO = service.findProduct(id);
        return productDTO;
    }

    private int findAllProducts() throws FinderException {
        try {
            return service.findProducts().size();
        } catch (FinderException e) {
            return 0;
        }
    }

    private int findAllProducts(String categoryId) throws FinderException {
        try {
            return service.findProducts(categoryId).size();
        } catch (FinderException e) {
            return 0;
        }
    }

    // Creates a category first and then a product linked to this category
    private void createProduct(final String id) throws CreateException, RemoveException, FinderException {
        // Create Category
    	final String categoryId = counterService.getUniqueId("Category");
    	final CategoryDTO categoryDTO = new CategoryDTO(categoryId, "name" + categoryId, "description" + categoryId);
		service.createCategory(categoryDTO);
        // Create Product
        final ProductDTO productDTO = new ProductDTO(id, "name" + id, "description" + id);
        productDTO.setCategoryDTO(categoryDTO);
        try {
			service.createProduct(productDTO);
		} catch (DuplicateKeyException e) {
				deleteCategory(categoryId);
				 throw new DuplicateKeyException();
		}
    }

    // Creates a category and updates the product with this new category
    private void updateProduct(final ProductDTO productDTO, final String id) throws UpdateException, CreateException, RemoveException, FinderException {
    	// get old  category id
        String catId = productDTO.getCategoryDTO().getId();
    	// Create Category
    	final String categoryId = counterService.getUniqueId("Category");
        final CategoryDTO categoryDTO = new CategoryDTO(categoryId, "name" + categoryId, "description" + categoryId);
        service.createCategory(categoryDTO);
        // Update Product with new category
        productDTO.setName("name" + id);
        productDTO.setDescription("description" + id);
        productDTO.setCategoryDTO(categoryDTO);
        service.updateProduct(productDTO);
        // delete old  category
        deleteCategory(catId);
    }

    private void deleteProduct(final String id) throws RemoveException, FinderException {
    	final String productId = id;
    	final ProductDTO productDTO = service.findProduct(productId);
    	final String categoryId = productDTO.getCategoryDTO().getId();
    	service.deleteProduct(productId);
        service.deleteCategory(categoryId);
    }

    private void checkProduct(final ProductDTO productDTO, final String id) {
        assertEquals("name", "name" + id, productDTO.getName());
        assertEquals("description", "description" + id, productDTO.getDescription());
    }

    // Creates a new category and return it
    private CategoryDTO createNewCategory() throws CreateException {
    	final String categoryId = counterService.getUniqueId("Category");
        final CategoryDTO categoryDTO = new CategoryDTO(categoryId, "name" + categoryId, "description" + categoryId);
        service.createCategory(categoryDTO);
        return categoryDTO;
    }

    // Creates a product linked to an existing category
    private ProductDTO createProductForCategory(final CategoryDTO categoryDTO) throws CreateException {
        final String id = counterService.getUniqueId("Product");
        final ProductDTO productDTO = new ProductDTO(id, "name" + id, "description" + id);
        productDTO.setCategoryDTO(categoryDTO);
        service.createProduct(productDTO);
        return productDTO;
    }
    
    //==================================
    //=    Private Methods for Item    =
    //==================================
    private ItemDTO findItem(final String id) throws FinderException {
        final ItemDTO itemDTO = service.findItem(id);
        return itemDTO;
    }

    private int findAllItems() throws FinderException {
        try {
            return service.findItems().size();
        } catch (FinderException e) {
            return 0;
        }
    }

    private int findAllItems(String productId) throws FinderException {
        try {
            return service.findItems(productId).size();
        } catch (FinderException e) {
            return 0;
        }
    }

    private List<ItemDTO> getAllItemsForProduct(String productId) throws FinderException {
        try {
            return service.findItems(productId);
        } catch (FinderException e) {
            return null;
        }
    }
    
    private int searchItems(String keyword)  {
        try {
            return service.searchItems(keyword).size();
        } catch (Exception e) {
            return 0;
        }
    }
    
    private int searchItemsForPrice(String GLT, double price) {
    	try {
    		if(GLT.equals("GT"))
    			return service.searchItemsByPrice("GT", price).size();
    		else if(GLT.equals("LT"))
    			return service.searchItemsByPrice("LT", price).size();
    		else if(GLT.equals("EQ"))
    			return service.searchItemsByPrice("EQ", price).size();
    		else
    			throw new Exception();
        } catch (Exception e) {
            return 0;
        }
	}
    
    private int searchItemsForPriceForKeyword(String GLT, double price, String id) {
    	try {
    		if(GLT.equals("GT"))
    			return service.searchItemsByPriceAndKeyword("GT", price,id).size();
    		else if(GLT.equals("LT"))
    			return service.searchItemsByPriceAndKeyword("LT", price,id).size();
    		else if(GLT.equals("EQ"))
    			return service.searchItemsByPriceAndKeyword("EQ", price,id).size();
    		else
    			throw new Exception();
        } catch (Exception e) {
            return 0;
        }
	}
    
    // Creates a category first, then a product and then an item linked to this product
    private void createItem(final String id) throws CreateException {
    	// Create Category
    	final String categoryId = counterService.getUniqueId("Category");
        final CategoryDTO categoryDTO = new CategoryDTO(categoryId, "name" + categoryId, "description" + categoryId);
        service.createCategory(categoryDTO);
        // Create Product
    	final String productId =counterService.getUniqueId("Product");
        final ProductDTO productDTO = new ProductDTO(productId, "name" + productId, "description" + productId);
        productDTO.setCategoryDTO(categoryDTO);
        service.createProduct(productDTO);
        // Create Item
        final ItemDTO itemDTO = new ItemDTO(id, "name" + id, Double.parseDouble(id));
        itemDTO.setImagePath("imagePath" + id);
        itemDTO.setProductDTO(productDTO);
        service.createItem(itemDTO);
    }

    // Creates a category, a product and updates the item with this new product
    private void updateItem(final ItemDTO itemDTO, final String updatePattern) throws UpdateException, CreateException, FinderException, RemoveException {
    	// get old  product id
        String prodId = itemDTO.getProductDTO().getId();
    	// Create Category
    	final String categoryId = counterService.getUniqueId("Category");
        final CategoryDTO categoryDTO = new CategoryDTO(categoryId, "name" + categoryId, "description" + categoryId);
        service.createCategory(categoryDTO);
        // Create Product
    	final String productId = counterService.getUniqueId("Product");
        final ProductDTO productDTO = new ProductDTO(productId, "name" + productId, "description" + productId);
        productDTO.setCategoryDTO(categoryDTO);
        service.createProduct(productDTO);
        // Updates the item
        itemDTO.setName("name" + updatePattern);
        itemDTO.setImagePath("imagePath" + updatePattern);
        itemDTO.setProductDTO(productDTO);
        service.updateItem(itemDTO);
        deleteProduct(prodId);
    }

    private void deleteItem(final String id) throws RemoveException, FinderException {
    	final ItemDTO itemDTO = service.findItem(id);
    	final String productId = itemDTO.getProductDTO().getId();
    	final ProductDTO productDTO = service.findProduct(productId);
    	final String categoryId = productDTO.getCategoryDTO().getId();
        service.deleteItem(id);
        service.deleteProduct(productId);
        service.deleteCategory(categoryId);
    }

    private void checkItem(final ItemDTO itemDTO, final String id) {
        assertEquals("name", "name" + id, itemDTO.getName());
        assertEquals("imagePath", "imagePath" + id, itemDTO.getImagePath());
        assertNotNull(itemDTO.getProductDTO().getId());
        assertNotNull(itemDTO.getProductDTO().getName());
        assertNotNull(itemDTO.getProductDTO().getDescription());
        }

    // Creates a category first, then a product and return it
    private ProductDTO createNewProduct() throws CreateException {
        // Create Category
    	final String categoryId = counterService.getUniqueId("Category");
        final CategoryDTO categoryDTO = new CategoryDTO(categoryId, "name" + categoryId, "description" + categoryId);
        service.createCategory(categoryDTO);
        // Create Product
    	final String productId = counterService.getUniqueId("Product");
        final ProductDTO productDTO = new ProductDTO(productId, "name" + productId, "description" + productId);
        productDTO.setCategoryDTO(categoryDTO);
        service.createProduct(productDTO);
        return productDTO;
    }

    // Creates an item linked to an existing product
    private ItemDTO createItemForProduct(final ProductDTO productDTO) throws CreateException {
        final String id = counterService.getUniqueId("Item");
        final ItemDTO itemDTO = new ItemDTO(id, "name" + id, Double.parseDouble(id));
        itemDTO.setImagePath("imagePath" + id);
        itemDTO.setProductDTO(productDTO);
        service.createItem(itemDTO);
        return itemDTO;
    }
}

