package com.yaps.petstore.authentication.domain.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Collection;
import java.util.NoSuchElementException;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.junit4.SpringRunner;

import com.yaps.petstore.authentication.domain.model.User;
import com.yaps.petstore.domain.service.CounterService;
import com.yaps.petstore.exception.CreateException;
import com.yaps.petstore.exception.DuplicateKeyException;
import com.yaps.petstore.exception.FinderException;

/**
 * This class tests the CustomerDAO class
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class UserDAOTest {

	 private static final String COUNTER_NAME = "Customer";
	 
	@Autowired
	  private UserRepository customerRepository;

	@Autowired
    private CounterService counterService;	

    //==================================
    //=            Test cases          =
    //==================================
    /**
     * This test tries to find an object with a invalid identifier.
     */
	 @Test
    public void testDomainFindCustomerWithInvalidValues() throws Exception {

        // Finds an object with a unknown identifier
        final String id = counterService.getUniqueId(COUNTER_NAME);
        try {
            findCustomer(id);
            fail("Object with unknonw id should not be found");
        } catch (NoSuchElementException e) {
        }

        // Finds an object with a null identifier
        try {
            customerRepository.findById(null).get();
            fail("Object with null id should not be found");
        } catch (Exception e) {
        }
    }

    /**
     * This test ensures that the method findAll works. It does a first findAll, creates
     * a new object and does a second findAll.
     */
	 @Test
    public void testDomainFindAllCustomers() throws Exception {
        final String id = counterService.getUniqueId(COUNTER_NAME);

        // First findAll
        final int firstSize = findAllCustomers();

        // Create an object
        createCustomer(id);

        // Ensures that the object exists
        try {
            findCustomer(id);
        } catch (NoSuchElementException e) {
            fail("Object has been created it should be found");
        }

        // Second findAll
        final int secondSize = findAllCustomers();

        // Checks that the collection size has increase of one
        if (firstSize + 1 != secondSize) fail("The collection size should have increased by 1");

        // Cleans the test environment
        removeCustomer(id);

        try {
            findCustomer(id);
            fail("Object has been deleted it shouldn't be found");
        } catch (NoSuchElementException e) {
        }
    }

    /**
     * This method ensures that creating an object works. It first finds the object,
     * makes sure it doesn't exist, creates it and checks it then exists.
     */
	 @Test
    public void testDomainCreateCustomer() throws Exception {
        final String id = counterService.getUniqueId(COUNTER_NAME);
        User customer = null;

        // Ensures that the object doesn't exist
        try {
            customer = findCustomer(id);
            fail("Object has not been created yet it shouldn't be found");
        } catch (NoSuchElementException e) {
        }

        // Creates an object
        createCustomer(id);

        // Ensures that the object exists
        try {
            customer = findCustomer(id);
        } catch (NoSuchElementException e) {
            fail("Object has been created it should be found");
        }

        // Checks that it's the right object
        checkCustomer(customer, id);

        // Cleans the test environment
        removeCustomer(id);

        try {
            findCustomer(id);
            fail("Object has been deleted it shouldn't be found");
        } catch (NoSuchElementException e) {
        }

    }

    /**
     * This test make sure that updating an object success
     */
	 @Test
    public void testDomainUpdateCustomer() throws Exception {
        final String id = counterService.getUniqueId(COUNTER_NAME);

        // Creates an object
        createCustomer(id);

        // Ensures that the object exists
        User customer = null;
        try {
            customer = findCustomer(id);
        } catch (NoSuchElementException e) {
            fail("Object has been created it should be found");
        }

        // Checks that it's the right object
        checkCustomer(customer, id);

        // Updates the object with new values
        updateCustomer(customer, id + 1);

        // Ensures that the object still exists
        User customerUpdated = null;
        try {
            customerUpdated = findCustomer(id);
        } catch (NoSuchElementException e) {
            fail("Object should be found");
        }

        // Checks that the object values have been updated
        checkCustomer(customerUpdated, id + 1);

        // Cleans the test environment
        removeCustomer(id);

        try {
            findCustomer(id);
            fail("Object has been deleted it shouldn't be found");
        } catch (NoSuchElementException e) {
        }
    }

    /**
     * This test ensures that the system cannot remove an unknown object
     */
	 @Test
    public void testDomainDeleteUnknownCustomer() throws Exception {
        // Removes an unknown object
        try {
            removeCustomer(counterService.getUniqueId(COUNTER_NAME));
            fail("Deleting an unknown object should break");
        } catch (EmptyResultDataAccessException e) {
        }
    }

    //==================================
    //=         Private Methods        =
    //==================================
    private User findCustomer(final String id) throws NoSuchElementException {
        final User customer = customerRepository.findById("custo" + id).get();
        return customer;
    }

    private int findAllCustomers() throws FinderException {
        try {
            return ((Collection<User>) customerRepository.findAll()).size();
        } catch (Exception e) {
            return 0;
        }
    }

    private void createCustomer(final String id) throws CreateException {
        final User customer = new User("custo" + id, "firstname" + id, "lastname" + id);
        customer.setCity("city" + id);
        customer.setCountry("cnty" + id);
        customer.setState("state" + id);
        customer.setStreet1("street1" + id);
        customer.setStreet2("street2" + id);
        customer.setTelephone("phone" + id);
        customer.setEmail("email" + id);
        customer.setZipcode("zip" + id);
        customer.setCreditCardExpiryDate("ccexp" + id);
        customer.setCreditCardNumber("ccnum" + id);
        customer.setCreditCardType("cctyp" + id);
        customerRepository.save(customer);
    }

    private void updateCustomer(final User customer, final String id) throws DuplicateKeyException {
        customer.setFirstname("firstname" + id);
        customer.setLastname("lastname" + id);
        customer.setCity("city" + id);
        customer.setCountry("cnty" + id);
        customer.setState("state" + id);
        customer.setStreet1("street1" + id);
        customer.setStreet2("street2" + id);
        customer.setTelephone("phone" + id);
        customer.setEmail("email" + id);
        customer.setZipcode("zip" + id);
        customer.setCreditCardExpiryDate("ccexp" + id);
        customer.setCreditCardNumber("ccnum" + id);
        customer.setCreditCardType("cctyp" + id);
  		customerRepository.save(customer);
    }

    private void removeCustomer(final String id) throws EmptyResultDataAccessException {
        final String sid = "custo" + id;
        customerRepository.deleteById(sid);
    }

    private void checkCustomer(final User customer, final String id) {
        assertEquals("firstname", "firstname" + id, customer.getFirstname());
        assertEquals("lastname", "lastname" + id, customer.getLastname());
        assertEquals("city", "city" + id, customer.getCity());
        assertEquals("country", "cnty" + id, customer.getCountry());
        assertEquals("state", "state" + id, customer.getState());
        assertEquals("street1", "street1" + id, customer.getStreet1());
        assertEquals("street2", "street2" + id, customer.getStreet2());
        assertEquals("telephone", "phone" + id, customer.getTelephone());
        assertEquals("email", "email" + id, customer.getEmail());
        assertEquals("zipcode", "zip" + id, customer.getZipcode());
        assertEquals("CreditCardExpiryDate", "ccexp" + id, customer.getCreditCardExpiryDate());
        assertEquals("CreditCardNumber", "ccnum" + id, customer.getCreditCardNumber());
        assertEquals("CreditCardType", "cctyp" + id, customer.getCreditCardType());
     }
 
}
