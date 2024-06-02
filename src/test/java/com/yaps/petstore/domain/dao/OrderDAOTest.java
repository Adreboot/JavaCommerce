package com.yaps.petstore.domain.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Random;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.yaps.petstore.authentication.domain.dao.UserRepository;
import com.yaps.petstore.authentication.domain.model.User;
import com.yaps.petstore.domain.model.Order;
import com.yaps.petstore.domain.service.CounterService;
import com.yaps.petstore.exception.CreateException;
import com.yaps.petstore.exception.FinderException;
import com.yaps.petstore.exception.ObjectNotFoundException;
import com.yaps.petstore.exception.RemoveException;
import com.yaps.petstore.exception.UpdateException;

/**
 * This class tests the OrderDAO class
 */
@RunWith(SpringRunner.class)
@SpringBootTest
//@Transactional
public class OrderDAOTest {
	
//	private static final String COUNTER_NAME = "Order";
	@Autowired
	private OrderRepository orderRepository;
	@Autowired
	private UserRepository customerRepository;
	@Autowired
    private CounterService counterService;
	
	private Random random = new Random();

    //==================================
    //=            Test cases          =
    //==================================
    /**
     * This test tries to find an object with a invalid identifier.
     */
	@Test
    public void testDomainFindOrderWithInvalidValues() throws Exception {

        // Finds an object with a unknown identifier
//		final Long id = Long.parseLong(counterService.getUniqueId(COUNTER_NAME));
		final Long id = orderRepository.findLastId().orElse(10000L)+1;
        try {
            findOrder(id);
            fail("Object with unknonw id should not be found");
        } catch (NoSuchElementException e) {
        }

        // Finds an object with an empty identifier
        try {
            orderRepository.findById(random.nextLong()+1L).get();
            fail("Object with empty id should not be found");
        } catch (Exception e) {
        }

        // Finds an object with a null identifier
        try {
        	orderRepository.findById(null).get();
            fail("Object with null id should not be found");
        } catch (Exception e) {
        }
    }

    /**
     * This test ensures that the method findAll works. It does a first findAll, creates
     * a new object and does a second findAll.
     */
	@Test
    public void testDomainFindAllOrders() throws Exception {
//		final Long id = Long.parseLong(counterService.getUniqueId(COUNTER_NAME));
		final Long id = orderRepository.findLastId().orElse(10000L)+1;

        // First findAll
        final int firstSize = findAllOrders();

        // Create an object
        final long orderId = createOrder(id);

        // Ensures that the object exists
        try {
            findOrder(orderId);
        } catch (NoSuchElementException e) {
            fail("Object has been created it should be found");
        }

        // second findAll
        final int secondSize = findAllOrders();

        // Checks that the collection size has increase of one
        if (firstSize + 1 != secondSize) fail("The collection size should have increased by 1");

        // Cleans the test environment
        removeOrder(orderId);

        try {
            findOrder(orderId);
            fail("Object has been deleted it shouldn't be found");
        } catch (NoSuchElementException e) {
        }
    }

    /**
     * This method ensures that creating an object works. It first finds the object,
     * makes sure it doesn't exist, creates it and checks it then exists.
     */
	@Test
    public void testDomainCreateOrder() throws Exception {
//		final Long id = Long.parseLong(counterService.getUniqueId(COUNTER_NAME));
		final Long id = orderRepository.findLastId().orElse(10000L)+1;
        Order order = null;

        // Creates an object
        final long orderId = createOrder(id);

        // Ensures that the object exists
        try {
            order = findOrder(orderId);
        } catch (NoSuchElementException e) {
            fail("Object has been created it should be found");
        }

        // Checks that it's the right object
        checkOrder(order, id);

        // Cleans the test environment
        removeOrder(orderId);

        try {
            findOrder(orderId);
            fail("Object has been deleted it shouldn't be found");
        } catch (NoSuchElementException e) {
        }
    }

	/**
     * This test make sure that updating an object success
     */
    @Test
    public void testDomainUpdateOrder() throws Exception {
//    	final Long id = Long.parseLong(counterService.getUniqueId(COUNTER_NAME));
    	final Long id = orderRepository.findLastId().orElse(10000L)+1;

        // Creates an object
        final long orderId = createOrder(id);

        // Ensures that the object exists
        Order order = null;
        try {
            order = findOrder(orderId);
        } catch (NoSuchElementException e) {
            fail("Object has been created it should be found");
        }

        // Checks that it's the right object
        checkOrder(order, id);

        // Updates the object with new values
        updateOrder(order, id + 1);

        // Ensures that the object still exists
        Order orderUpdated = null;
        try {
            orderUpdated = findOrder(orderId);
        } catch (NoSuchElementException e) {
            fail("Object should be found");
        }

        // Checks that the object values have been updated
        checkOrder(orderUpdated, id + 1);

        // Cleans the test environment
        removeOrder(orderId);

        try {
            findOrder(orderId);
            fail("Object has been deleted it shouldn't be found");
        } catch (NoSuchElementException e) {
        }
    }

    //==================================
    //=         Private Methods        =
    //==================================
    private Order findOrder(final Long id) throws NoSuchElementException {
        final Order order = orderRepository.findById(id).get();
        return order;
    }

    private int findAllOrders() throws FinderException {
        try {
            return ((Collection<Order>) orderRepository.findAll()).size();
        } catch (Exception e) {
            return 0;
        }
    }

    // Creates a customer first and then a order linked to this customer and returns the key
    private long createOrder(final long id) throws CreateException, FinderException {
        // Create Customer
    	String customerId = counterService.getUniqueId("Customer");
        final User customer = new User("custo" + customerId, "firstname" + customerId, "lastname" + customerId);
        customer.setRole("ROLE_USER");
        customerRepository.save(customer);
        // Create Order
        final Order order = new Order("firstname" + id, "lastname" + id, "street1" + id, "city" + id, "zip" + id, "country" + id, customer);
        order.setStreet2("street2" + id);
        order.setCreditCardExpiryDate("ccexp" + id);
        order.setCreditCardNumber("ccnum" + id);
        order.setCreditCardType("cctyp" + id);
        order.setState("state" + id);
        orderRepository.save(order);
        return order.getId();
    }

    // Creates a customer and updates the order with this new customer
    private void updateOrder(final Order order, final long id) throws UpdateException, CreateException, FinderException {
    	// get old customer
    	User cust = order.getCustomer();
    	// Create new Customer
    	String customerId = counterService.getUniqueId("Customer");
        final User customer = new User("custo" + customerId, "firstname" + customerId, "lastname" + customerId);
        customer.setRole("ROLE_USER");
        customerRepository.save(customer);
        // Update Order with new customer
        order.setFirstname("firstname" + id);
        order.setLastname("lastname" + id);
        order.setCountry("country" + id);
        order.setZipcode("zip" + id);
        order.setCity("city" + id);
        order.setStreet1("street1" + id);
        order.setStreet2("street2" + id);
        order.setCreditCardExpiryDate("ccexp" + id);
        order.setCreditCardNumber("ccnum" + id);
        order.setCreditCardType("cctyp" + id);
        order.setState("state" + id);
        order.setCustomer(customer);
        orderRepository.save(order);
     // delete old customer
    	customerRepository.delete(cust);
        }

    private void removeOrder(final long orderId) throws RemoveException, ObjectNotFoundException {
    	Order order = orderRepository.findById(orderId).get();
    	User customer = order.getCustomer();
    	orderRepository.deleteById(orderId);
        customerRepository.delete(customer);
    }

    private void checkOrder(final Order order, final Long id) {
        assertEquals("firstname", "firstname" + id, order.getFirstname());
        assertEquals("lastname", "lastname" + id, order.getLastname());
        assertEquals("street1", "street1" + id, order.getStreet1());
        assertEquals("street2", "street2" + id, order.getStreet2());
        assertEquals("city", "city" + id, order.getCity());
        assertEquals("zipcode", "zip" + id, order.getZipcode());
        assertEquals("country", "country" + id, order.getCountry());
        assertEquals("CreditCardExpiryDate", "ccexp" + id, order.getCreditCardExpiryDate());
        assertEquals("CreditCardNumber", "ccnum" + id, order.getCreditCardNumber());
        assertEquals("CreditCardType", "cctyp" + id, order.getCreditCardType());
        assertEquals("State", "state" + id, order.getState());
        assertNotNull("Customer", order.getCustomer());
    }

}
