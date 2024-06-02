package com.yaps.petstore.domain.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.Collection;
import java.util.NoSuchElementException;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.yaps.petstore.domain.model.Category;
import com.yaps.petstore.domain.model.Product;
import com.yaps.petstore.domain.service.CounterService;
import com.yaps.petstore.exception.*;

/**
 * This class tests the ProductDAO class
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class ProductDAOTest {
	
	private static final String COUNTER_NAME = "Product";
	@Autowired
	private ProductRepository productRepository;
	@Autowired
    private CategoryRepository categoryRepository;
	@Autowired
    private CounterService counterService;

    //==================================
    //=            Test cases          =
    //==================================
    /**
     * This test tries to find an object with a invalid identifier.
     */
	@Test
    public void testDomainFindProductWithInvalidValues() throws Exception {

        // Finds an object with a unknown identifier
        final String id = counterService.getUniqueId(COUNTER_NAME);
        try {
            findProduct(id);
            fail("Object with unknonw id should not be found");
        } catch (NoSuchElementException e) {
        }

        // Finds an object with an empty identifier
        try {
            productRepository.findById(new String()).get();
            fail("Object with empty id should not be found");
        } catch (Exception e) {
        }

        // Finds an object with a null identifier
        try {
            productRepository.findById(null).get();
            fail("Object with null id should not be found");
        } catch (Exception e) {
        }
    }

    /**
     * This test ensures that the method findAll works. It does a first findAll, creates
     * a new object and does a second findAll.
     */
	@Test
    public void testDomainFindAllProducts() throws Exception {
        final String id = counterService.getUniqueId(COUNTER_NAME);

        // First findAll
        final int firstSize = findAllProducts();

        // Create an object
        createProduct(id);

        // Ensures that the object exists
        try {
            findProduct(id);
        } catch (NoSuchElementException e) {
            fail("Object has been created it should be found");
        }

        // second findAll
        final int secondSize = findAllProducts();

        // Checks that the collection size has increase of one
        if (firstSize + 1 != secondSize) fail("The collection size should have increased by 1");

        // Cleans the test environment
        removeProduct(id);

        try {
            findProduct(id);
            fail("Object has been deleted it shouldn't be found");
        } catch (NoSuchElementException e) {
        }
    }

    /**
     * This test ensures that the method findAll works. It does a first findAll, creates
     * a new object and does a second findAll.
     */
	@Test
    public void testDomainFindAllProductsForACategory() throws Exception {
    	Category newCategory = createNewCategory();
    	final String categoryId = newCategory.getId();

        // First findAll
        final int firstSize = findAllProducts(categoryId);

        // Checks that the collection is empty
        if (firstSize != 0) fail("The collection should be empty");

        // Create an object
        Product product = createProductForCategory(newCategory);

        // Ensures that the object exists
        try {
            findProduct(product.getId());
        } catch (NoSuchElementException e) {
            fail("Object has been created it should be found");
        }

        // second findAll
        final int secondSize = findAllProducts(categoryId);

        // Checks that the collection size has increase of one
        if (firstSize + 1 != secondSize) fail("The collection size should have increased by 1");

        // Cleans the test environment
        removeProduct(product.getId());

    }

    /**
     * This method ensures that creating an object works. It first finds the object,
     * makes sure it doesn't exist, creates it and checks it then exists.
     */
	@Test
    public void testDomainCreateProduct() throws Exception {
        final String id = counterService.getUniqueId(COUNTER_NAME);
        Product product = null;

        // Ensures that the object doesn't exist
        try {
            product = findProduct(id);
            fail("Object has not been created yet it shouldn't be found");
        } catch (NoSuchElementException e) {
        }

        // Creates an object
        createProduct(id);

        // Ensures that the object exists
        try {
            product = findProduct(id);
        } catch (NoSuchElementException e) {
            fail("Object has been created it should be found");
        }

        // Checks that it's the right object
        checkProduct(product, id);

        // Cleans the test environment
        removeProduct(id);

        try {
            findProduct(id);
            fail("Object has been deleted it shouldn't be found");
        } catch (NoSuchElementException e) {
        }
    }

    /**
     * This test make sure that updating an object success
     */
	@Test
    public void testDomainUpdateProduct() throws Exception {
        final String id = counterService.getUniqueId(COUNTER_NAME);

        // Creates an object
        createProduct(id);

        // Ensures that the object exists
        Product product = null;
        try {
            product = findProduct(id);
        } catch (NoSuchElementException e) {
            fail("Object has been created it should be found");
        }

        // Checks that it's the right object
        checkProduct(product, id);

        // Updates the object with new values
        updateProduct(product, id + 1);

        // Ensures that the object still exists
        Product productUpdated = null;
        try {
            productUpdated = findProduct(id);
        } catch (NoSuchElementException e) {
            fail("Object should be found");
        }

        // Checks that the object values have been updated
        checkProduct(productUpdated, id + 1);

        // Cleans the test environment
        removeProduct(id);

        try {
            findProduct(id);
            fail("Object has been deleted it shouldn't be found");
        } catch (NoSuchElementException e) {
        }
    }

    /**
     * This test ensures that the system cannont remove an unknown object
     */
	@Test
    public void testDomainDeleteUnknownProduct() throws Exception {
        // Removes an unknown object
        try {
        	productRepository.delete(null);
            fail("Deleting an unknown object should break");
        } catch (Exception e) {
        }
    }

    //==================================
    //=         Private Methods        =
    //==================================
    private Product findProduct(final String id) throws NoSuchElementException {
        final Product product = (Product)productRepository.findById(id).get();
        return product;
    }

    private int findAllProducts() throws FinderException {
        try {
            return ((Collection<Product>) productRepository.findAll()).size();
        } catch (Exception e) {
            return 0;
        }
    }

    private int findAllProducts(String categoryId) throws FinderException {
        try {
        	Category category = categoryRepository.findById(categoryId).get();
            return ((Collection<Product>) productRepository.findAllByCategory(category)).size();
        } catch (Exception e) {
            return 0;
        }
    }

     // Creates a category first and then a product linked to this category
    private void createProduct(final String id) throws CreateException, ObjectNotFoundException {
        // Create Category
        final String newCategoryId = counterService.getUniqueId("Category");
        final Category category = new Category(newCategoryId, "name" + newCategoryId, "description" + newCategoryId);
        categoryRepository.save(category);
        // Create Product
        final Product product = new Product(id, "name" + id, "description" + id, category);
        try {
        	productRepository.save(product);
        } catch ( Exception e ) {
        	// remove the added category object
        	categoryRepository.deleteById(newCategoryId);
        	// rethrow the exception
        	throw e;
        }
    }

    // Creates a category and updates the product with this new category
    private void updateProduct(final Product product, final String id) throws UpdateException, CreateException, ObjectNotFoundException {
        // Create Category
        final String newCategoryId = counterService.getUniqueId("Category");
        final Category category = new Category(newCategoryId, "name" + newCategoryId, "description" + newCategoryId);
        categoryRepository.save(category);
        
     // get old  category
        Category cat = product.getCategory();
        
        // Update Product with new category
        product.setName("name" + id);
        product.setDescription("description" + id);
        product.setCategory(category);
        productRepository.save(product);
        
     // delete old  category
        categoryRepository.delete(cat);
    }

    private void removeProduct(final String id) throws RemoveException, ObjectNotFoundException {
        final String productId = id;
        Product product = productRepository.findById(productId).get();
        final String categoryId = product.getCategory().getId();
        productRepository.deleteById(id);
        categoryRepository.deleteById(categoryId);
    }

    private void checkProduct(final Product product, final String id) {
        assertEquals("name", "name" + id, product.getName());
        assertEquals("description", "description" + id, product.getDescription());
        assertNotNull("category", product.getCategory());
    }

    // Creates a new category and return it
    private Category createNewCategory() throws CreateException {
        final String newCategoryId = counterService.getUniqueId("Category");
        final Category category = new Category("cat" + newCategoryId, "name" + newCategoryId, "description" + newCategoryId);
        categoryRepository.save(category);
        return category;
    }

    // Creates a product linked to an existing category
    private Product createProductForCategory(final Category category) throws CreateException {
        final String id = counterService.getUniqueId(COUNTER_NAME);
        final Product product = new Product(id, "name" + id, "description" + id, category);
        productRepository.save(product);
        return product;
    }

}
