package com.yaps.petstore.authentication.domain.service;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.yaps.petstore.authentication.domain.dto.UserDTO;
import com.yaps.petstore.domain.constant.CreditCardStatus;
import com.yaps.petstore.domain.service.CounterService;
import com.yaps.petstore.exception.CreateException;
import com.yaps.petstore.exception.DuplicateKeyException;
import com.yaps.petstore.exception.FinderException;
import com.yaps.petstore.exception.ObjectNotFoundException;
import com.yaps.petstore.exception.RemoveException;
import com.yaps.petstore.exception.UpdateException;

import junit.framework.TestCase;

/**
 * This class tests the CatalogService class
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class UserServiceTest extends TestCase {
	
private static final String COUNTER_NAME = "Customer";
	
private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceTest.class);
	
	@Autowired
	private UserService userService;
	
	@Autowired
    private CounterService counterService;
	
    //==================================
    //=            Test cases          =
    //==================================
    /**
     * This test tries to find an object with a invalid identifier.
     */
	@Test
    public void testServiceFindCustomerWithInvalidValues() throws Exception {

        // Finds an object with a unknown identifier
        final String id = counterService.getUniqueId(COUNTER_NAME);
        try {
            userService.findUser(id);
            fail("Object with unknonw id should not be found");
        } catch (FinderException e) {
        }

        // Finds an object with an empty identifier
        try {
            userService.findUser(new String());
            fail("Object with empty id should not be found");
        } catch (FinderException e) {
        }

        // Finds an object with a null identifier
        try {
            userService.findUser(null);
            fail("Object with null id should not be found");
        } catch (FinderException e) {
        }
    }

    /**
     * This test ensures that the method findAll works. It does a first findAll, creates
     * a new object and does a second findAll.
     */
	@Test
    public void testServiceFindAllCustomers() throws Exception {
        final String id = counterService.getUniqueId(COUNTER_NAME);

        // First findAll
        final int firstSize = findAllCustomers();
        LOGGER.debug("firstSize is ... "+firstSize);
        // Creates an object
        createCustomer(id);

        // Ensures that the object exists
        try {
            findCustomer(id);
        } catch (FinderException e) {
            fail("Object has been created it should be found");
        }

        // Second findAll
        final int secondSize = findAllCustomers();

        // Checks that the collection size has increase of one
        if (firstSize + 1 != secondSize) fail("The collection size should have increased by 1");

        // Cleans the test environment
        deleteCustomer(id);

        try {
            findCustomer(id);
            fail("Object has been deleted it shouldn't be found");
        } catch (FinderException e) {
        }
    }

    /**
     * This method ensures that creating an object works. It first finds the object,
     * makes sure it doesn't exist, creates it and checks it then exists.
     */
	@Test
    public void testServiceCreateCustomer() throws Exception {
		final String id = counterService.getUniqueId(COUNTER_NAME);
        UserDTO customerDTO = null;

        // Ensures that the object doesn't exist
        try {
            findCustomer(id);
            fail("Object has not been created yet it shouldn't be found");
        } catch (FinderException e) {
        }

        // Creates an object
        createCustomer(id);

        // Ensures that the object exists
        try {
            customerDTO = findCustomer(id);
        } catch (FinderException e) {
            fail("Object has been created it should be found");
        }

        // Checks that it's the right object
        checkCustomer(customerDTO, id);

        // Creates an object with the same identifier. An exception has to be thrown
        try {
            createCustomer(id);
            fail("An object with the same id has already been created");
        } catch (DuplicateKeyException e) {
        }

        // Cleans the test environment
        deleteCustomer(id);

        try {
            findCustomer(id);
            fail("Object has been deleted it shouldn't be found");
        } catch (FinderException e) {
        }
    }

    /**
     * This test tries to create an object with a invalid values.
     */
    @Test
    public void testServiceCreateCustomerWithInvalidValues() throws FinderException {
      	 UserDTO customerDTO;

           // Creates an object with a null parameter
           try {
          	 userService.createUser(null);
               fail("Object with null parameter should not be created");
           } catch (CreateException e) {
           }

           // Creates an object with empty values
           try {
               customerDTO = new UserDTO(new String(), new String(), new String());
               customerDTO.setPassword("validPwd");
               userService.createUser(customerDTO);
               fail("Object with empty values should not be created");
           } catch (javax.validation.ConstraintViolationException | CreateException e) {
          	 System.err.println(e.getClass().getSimpleName());
           }

           // Creates an object with null values
           try {
               customerDTO = new UserDTO(null, null, null);
//               customerDTO.setPassword("validPwd");
               userService.createUser(customerDTO);
               fail("Object with null values should not be created");
           } catch (javax.validation.ConstraintViolationException | CreateException e) {
          	 System.err.println(e.getClass().getSimpleName());
           }
         
        // Creates an object with an invalid password
           try {
               customerDTO = new UserDTO("username", "firstname", "lastname");
               customerDTO.setPassword("abc");
               userService.createUser(customerDTO);
               fail("Object with short password should not be created");
           } catch (javax.validation.ConstraintViolationException | CreateException e) { }
      }
    
    /**
     * This method ensures that creating an object with invalid credit card information
     * doesn't work.
     */ 
    @Test
    public void testServiceCreateCustomerWithInvalidCreditCard() throws Exception {
        final String id = counterService.getUniqueId(COUNTER_NAME);
        UserDTO customerDTO = createCustomerDTO(id);
        // Invalid credit card date
        customerDTO.setCreditCardExpiryDate("10/02");
        customerDTO.setCreditCardNumber("4564 1231 4564 2222");
        customerDTO.setCreditCardType("Visa");
        try {
            userService.createUser(customerDTO);
            fail("Credit card date was invalid. Object shouldn't be created");
        } catch (FinderException e) {
        	assertTrue("Il ne s'agit pas d'un problème de date invalide ?",e.getMessage().equals(CreditCardStatus.INVALID_DATE));
        }

        // Invalid credit card number for a visa
        customerDTO.setCreditCardExpiryDate("10/23");
        customerDTO.setCreditCardNumber("4564 1231 4564 1111");
        customerDTO.setCreditCardType("Visa");
        try {
            userService.createUser(customerDTO);
            fail("Credit card number was invalid. Object shouldn't be created");
        } catch (FinderException e) {
        	assertTrue("Il ne s'agit pas d'un problème de date invalide ?",e.getMessage().equals(CreditCardStatus.INVALID_NUMBER));
        }

        // The client doesn't pay with the credit card but with a cheque
        customerDTO.setCreditCardExpiryDate("");
        customerDTO.setCreditCardNumber("");
        customerDTO.setCreditCardType("");
        try {
            userService.createUser(customerDTO);
        }catch (FinderException e) {
            fail("Credit card wasn't used. Object should be created");
        }

        // Ensures that the object exists
        try {
            findCustomer(id);
        } catch (FinderException e) {
            fail("Object has been created it should be found");
        }

        // Cleans the test environment
        deleteCustomer(id);

        try {
            findCustomer(id);
            fail("Object has been deleted it shouldn't be found");
        } catch (FinderException e) {
        }
    }

    /**
     * This test make sure that updating an object success
     */
    @Test
    public void testServiceUpdateCustomer() throws Exception {
    	final String id = counterService.getUniqueId(COUNTER_NAME);
        final String updatedId = counterService.getUniqueId(COUNTER_NAME);

        // Creates an object
        createCustomer(id);

        // Ensures that the object exists
        UserDTO customerDTO = null;
        try {
            customerDTO = findCustomer(id);
        } catch (ObjectNotFoundException e) {
            fail("Object has been created it should be found");
        }

        // Checks that it's the right object
        checkCustomer(customerDTO, id);

        // Updates the object with new values
        updateCustomer(customerDTO, updatedId);

        // Ensures that the object still exists
        UserDTO customerUpdated = null;
        try {
            customerUpdated = findCustomer(id);
        } catch (ObjectNotFoundException e) {
            fail("Object should be found");
        }

        // Checks that the object values have been updated
        checkCustomer(customerUpdated, updatedId);

        // Cleans the test environment
        deleteCustomer(id);

        try {
            findCustomer(id);
            fail("Object has been deleted it shouldn't be found");
        } catch (FinderException e) {
        }
    }

    /**
     * This test tries to update an object with a invalid values.
     */
    @Test
    public void testServiceUpdateCustomerWithInvalidValues() throws Exception {
    	UserDTO customerDTO;

        // Updates an object with a null parameter
        try {
        	userService.updateUser(null);
            fail("Object with null parameter should not be updated");
        } catch (UpdateException e) {
        }

        // Updates an object with empty values
        try {
            customerDTO = new UserDTO(new String(), new String(), new String());
            userService.updateUser(customerDTO);
            fail("Object with empty values should not be updated");
        } catch (Exception e) {
       	 System.err.println(e.getClass().getSimpleName());
        }

        // Updates an object with null values
        try {
            customerDTO = new UserDTO(null, null, null);
            userService.updateUser(customerDTO);
            fail("Object with null values should not be updated");
        } catch (Exception e) {
       	 System.err.println(e.getClass().getSimpleName());
        }
    }
    
    /**
     * This method ensures that updating an object with invalid credit card information
     * doesn't work.
     */
    @Test
    public void testServiceUpdateCustomerWithInvalidCreditCard() throws Exception {
        final String id = counterService.getUniqueId(COUNTER_NAME);

        // Creates an object
        createCustomer(id);

        // Ensures that the object exists
       UserDTO customerDTO = null;
        try {
        	customerDTO = findCustomer(id);
        } catch (FinderException e) {
            fail("Object has been created it should be found");
        }

        // Checks that it's the right object
        checkCustomer(customerDTO, id);

        customerDTO.setPassword("pass"+id);
        // Invalid credit card date
        customerDTO.setCreditCardExpiryDate("10/02");
        customerDTO.setCreditCardNumber("4564 1231 4564 2222");
        customerDTO.setCreditCardType("Visa");
        try {
            userService.updateUser(customerDTO);
            fail("Credit card date was invalid. Object shouldn't be updated");
        } catch (FinderException e) {
            assertTrue("Il ne s'agit pas d'un problÃ¨me de date invalide ?",e.getMessage().equals(CreditCardStatus.INVALID_DATE));
        }

        // Invalid credit card number for a visa
        customerDTO.setCreditCardExpiryDate("10/23");
        customerDTO.setCreditCardNumber("4564 1231 4564 1111");
        customerDTO.setCreditCardType("Visa");
        try {
            userService.updateUser(customerDTO);
            fail("Credit card number was invalid. Object shouldn't be updated");
        } catch (FinderException e) {
        	assertTrue("Il ne s'agit pas d'un problÃ¨me de date invalide ?",e.getMessage().equals(CreditCardStatus.INVALID_NUMBER));
        }

        // The client doesn't pay with the credit card but with a cheque
        customerDTO.setCreditCardExpiryDate("");
        customerDTO.setCreditCardNumber("");
        customerDTO.setCreditCardType("");
        try {
            userService.updateUser(customerDTO);
        } catch (UpdateException e) {
            fail("Credit card wasn't used. Object should be updated");
        }

        // Ensures that the object exists
        try {
            findCustomer(id);
        } catch (FinderException e) {
            fail("Object has been updated it should be found");
        }

        // Cleans the test environment
        deleteCustomer(id);

        try {
            findCustomer(id);
            fail("Object has been deleted it shouldn't be found");
        } catch (FinderException e) {
        }
    }

    /**
     * This test ensures that the system cannont remove an unknown object
     */
    @Test
    public void testServiceDeleteUnknownCustomer() throws Exception {
        final String id = counterService.getUniqueId(COUNTER_NAME);

        // Ensures that the object doesn't exist
        try {
            findCustomer(id);
            fail("Object has not been created it shouldn't be found");
        } catch (FinderException e) {
        }

        // Delete the unknown object
        try {
            deleteCustomer(id);
            fail("Deleting an unknown object should break");
        } catch (RemoveException e) {
        }
    }

    //==================================
    //=          Private Methods       =
    //==================================

    private UserDTO findCustomer(final String id) throws FinderException {
        UserDTO customerDTO = userService.findUser("custo" + id);
        return customerDTO;
    }

    private int findAllCustomers() throws FinderException {
        try {
            return (userService.findUsers()).size();
        } catch (FinderException e) {
        	LOGGER.warn("exception is ... "+e.getMessage());
            return 0;
        }
    }
    
    private UserDTO createCustomerDTO(final String id) throws CreateException, FinderException {
        UserDTO customerDTO = new UserDTO();
        customerDTO.setUsername("custo" + id);
        customerDTO.setFirstname("firstname" + id);
        customerDTO.setLastname("lastname" + id);
        customerDTO.setPassword("pwd" + id);
		customerDTO.setEmail("email" + id);
		customerDTO.setTelephone("phone" + id);
		customerDTO.setStreet1("street1" + id);
		customerDTO.setStreet2("street2" + id);
		customerDTO.setCity("city" + id);
		customerDTO.setState("" + id);
		customerDTO.setZipcode("zip" + id);
		customerDTO.setCountry("" + id);
		customerDTO.setCreditCardType("Visa");
		customerDTO.setCreditCardNumber("4564 1231 4564 1222");
		customerDTO.setCreditCardExpiryDate("10/23");
	    customerDTO.setRole("ROLE_USER");
	    return customerDTO;
    }

    private void createCustomer(final String id) throws CreateException, FinderException {
        UserDTO customerDTO = new UserDTO();
        customerDTO.setUsername("custo" + id);
        customerDTO.setFirstname("firstname" + id);
        customerDTO.setLastname("lastname" + id);
        customerDTO.setPassword("pwd" + id);
		customerDTO.setEmail("email" + id);
		customerDTO.setTelephone("phone" + id);
		customerDTO.setStreet1("street1" + id);
		customerDTO.setStreet2("street2" + id);
		customerDTO.setCity("city" + id);
		customerDTO.setState("" + id);
		customerDTO.setZipcode("zip" + id);
		customerDTO.setCountry("" + id);
		customerDTO.setCreditCardType("Visa");
		customerDTO.setCreditCardNumber("4564 1231 4564 1222");
		customerDTO.setCreditCardExpiryDate("10/23");
	    customerDTO.setRole("ROLE_USER");
        userService.createUser(customerDTO);
    }

    private void updateCustomer(final UserDTO customerDTO, final String id) throws UpdateException, FinderException {
    	customerDTO.setFirstname("firstname" + id);
    	customerDTO.setLastname("lastname" + id);
    	customerDTO.setPassword("pass"+id);
    	customerDTO.setCity("city" + id);
    	customerDTO.setCountry("" + id);
    	customerDTO.setState("" + id);
    	customerDTO.setStreet1("street1" + id);
    	customerDTO.setStreet2("street2" + id);
    	customerDTO.setTelephone("phone" + id);
    	customerDTO.setEmail("email" + id);
    	customerDTO.setZipcode("zip" + id);
    	customerDTO.setCreditCardExpiryDate("10/23");
        customerDTO.setCreditCardNumber("4564 1231 4564 1222");
        customerDTO.setCreditCardType("Visa");
        userService.updateUser(customerDTO);
    }

    private void deleteCustomer(final String id) throws RemoveException, FinderException {
        userService.deleteUser("custo" + id);
    }

    private void checkCustomer(final UserDTO customerDTO, final String id) {
        assertEquals("firstname", "firstname" + id, customerDTO.getFirstname());
        assertEquals("lastname", "lastname" + id, customerDTO.getLastname());
        assertEquals("city", "city" + id, customerDTO.getCity());
        assertEquals("country", "" + id, customerDTO.getCountry());
        assertEquals("state", "" + id, customerDTO.getState());
        assertEquals("street1", "street1" + id, customerDTO.getStreet1());
        assertEquals("street2", "street2" + id, customerDTO.getStreet2());
        assertEquals("telephone", "phone" + id, customerDTO.getTelephone());
        assertEquals("email", "email" + id, customerDTO.getEmail());
        assertEquals("zipcode", "zip" + id, customerDTO.getZipcode());
        assertEquals("CreditCardExpiryDate", "10/23", customerDTO.getCreditCardExpiryDate());
        assertEquals("CreditCardNumber", "4564 1231 4564 1222", customerDTO.getCreditCardNumber());
        assertEquals("CreditCardType", "Visa", customerDTO.getCreditCardType());
    }
    
}