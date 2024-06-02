package com.yaps.petstore.domain.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Random;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.yaps.petstore.authentication.domain.dao.UserRepository;
import com.yaps.petstore.authentication.domain.model.User;
import com.yaps.petstore.domain.model.Category;
import com.yaps.petstore.domain.model.Item;
import com.yaps.petstore.domain.model.Order;
import com.yaps.petstore.domain.model.OrderLine;
import com.yaps.petstore.domain.model.Product;
import com.yaps.petstore.domain.service.CounterService;
import com.yaps.petstore.exception.CreateException;
import com.yaps.petstore.exception.FinderException;
import com.yaps.petstore.exception.ObjectNotFoundException;
import com.yaps.petstore.exception.RemoveException;

/**
 * This class tests the OrderLineDAO class
 */
@RunWith(SpringRunner.class)
@SpringBootTest
//@Transactional
public class OrderLineDAOTest {
	
//	private static final String COUNTER_NAME = "OrderLine";
	@Autowired
    private OrderLineRepository orderLineRepository;
	@Autowired
    private OrderRepository orderRepository;
	@Autowired
    private CategoryRepository categoryRepository;
	@Autowired
    private ProductRepository productRepository;
	@Autowired
    private ItemRepository itemRepository;
	@Autowired
    private UserRepository customerRepository;
	@Autowired
    private CounterService counterService;
	
    private final int _defaultQuantity = 12345;
    private final double _defaultUnitCost = 1.2345;
    
    private Random random = new Random();
    
    private static final Logger logger = LoggerFactory.getLogger(OrderLineDAOTest.class);

    //==================================
    //=            Test cases          =
    //==================================
    /**
     * This test tries to find an object with a invalid identifier.
     */
    @Test
    public void testDomainFindOrderLineWithInvalidValues() throws Exception {

        // Finds an object with a unknown identifier
//    	final Long id = Long.parseLong(counterService.getUniqueId(COUNTER_NAME));
    	final Long id = orderLineRepository.findLastId().orElse(10000L)+1;
        try {
            findOrderLine(id);
            fail("Object with unknonw id should not be found");
        } catch (NoSuchElementException e) {
        }

        // Finds an object with an empty identifier
        try {
            orderLineRepository.findById(random.nextLong()+1L).get();
            fail("Object with empty id should not be found");
        } catch (Exception e) {
        }

        // Finds an object with a null identifier
        try {
        	orderLineRepository.findById(null).get();
            fail("Object with null id should not be found");
        } catch (Exception e) {
        }
    }

    /**
     * This test ensures that the method findAll works. It does a first findAll, creates
     * a new object and does a second findAll.
     */
    @Test
    public void testDomainFindAllOrderLines() throws Exception {
//    	final Long id = Long.parseLong(counterService.getUniqueId(COUNTER_NAME));
    	final Long id = orderLineRepository.findLastId().orElse(10000L)+1;
        // First findAll
        final int firstSize = findAllOrderLines();
        logger.info("firstSize is ... "+firstSize);
        // Create an object
        final long orderLineId = createOrderLine(id);
        
        // Ensures that the object exists
        try {
            findOrderLine(orderLineId);
        } catch (NoSuchElementException e) {
            fail("Object has been created it should be found");
        }

        // second findAll
        final int secondSize = findAllOrderLines();
        logger.info("secondSize is ... "+secondSize);
        // Checks that the collection size has increase of one
        if (firstSize + 1 != secondSize) fail("The collection size should have increased by 1");

        // Cleans the test environment
        removeOrderLine(orderLineId);

        try {
            findOrderLine(orderLineId);
            fail("Object has been deleted it shouldn't be found");
        } catch (NoSuchElementException e) {
        }
    }

    /**
     * This method ensures that creating an object works. It first finds the object,
     * makes sure it doesn't exist, creates it and checks it then exists.
     */
    @Test
    public void testDomainCreateOrderLine() throws Exception {
//    	final Long id = Long.parseLong(counterService.getUniqueId(COUNTER_NAME));
    	final Long id = orderLineRepository.findLastId().orElse(10000L)+1;
        OrderLine orderLine = null;

        // Creates an object
        final long orderLineId = createOrderLine(id);

        // Ensures that the object exists
        try {
            orderLine = findOrderLine(orderLineId);
        } catch (NoSuchElementException e) {
            fail("Object has been created it should be found");
        }

        // Checks that it's the right object
        checkOrderLine(orderLine, id);

        // Cleans the test environment
        removeOrderLine(orderLineId);

        try {
            findOrderLine(orderLineId);
            fail("Object has been deleted it shouldn't be found");
        } catch (NoSuchElementException e) {
        }
    }
    
    //==================================
    //=         Private Methods        =
    //==================================
    private OrderLine findOrderLine(final long id) throws NoSuchElementException {
        final OrderLine orderLine = orderLineRepository.findById(id).get();
        return orderLine;
    }

    private int findAllOrderLines() throws FinderException {
        try {
            return ((Collection<OrderLine>) orderLineRepository.findAll()).size();
        } catch (Exception e) {
        	logger.warn(e.getMessage());
            return 0;
        }
    }

    // Creates a category first, then a product linked to this category and an item linked to the product
    // Creates a Customer and an order linked to the customer
    // Creates an orderLine linked to the order and the item
    private long createOrderLine(final long id) throws CreateException {
        // Create Category
    	String categoryId = counterService.getUniqueId("Category");
        final Category category = new Category("cat" + categoryId, "name" + categoryId, "description" + categoryId);
        categoryRepository.save(category);
        // Create Product
    	String productId = counterService.getUniqueId("Product");
        final Product product = new Product("prod" + productId, "name" + productId, "description" + productId, category);
        productRepository.save(product);
        // Create Item
    	String itemId = counterService.getUniqueId("Item");
        final Item item = new Item("item" + itemId, "name" + itemId, 1, product);
        itemRepository.save(item);
        // Create Customer
        String customerId = counterService.getUniqueId("Customer");
        final User customer = new User(customerId, "firstname" + customerId, "lastname" + customerId);
//        customer.setPassword("pwd"+customerId);
        customerRepository.save(customer);
        // Create Order
        //long orderId = Long.parseLong(counterService.getUniqueId("Order"));
        final Order order = new Order("firstname" + id, "lastname" + id, "street1" + id, "city" + id, "zip" + id, "country" + id, customer);
        order.setStreet2("street2" + id);
        order.setCreditCardExpiryDate("ccexp" + id);
        order.setCreditCardNumber("ccnum" + id);
        order.setCreditCardType("cctyp" + id);
        order.setState("state" + id);
        orderRepository.save(order);

        // Create OrderLine
        final OrderLine orderLine = new OrderLine(_defaultQuantity, _defaultUnitCost, order, item); //final long id, 
        orderLineRepository.save(orderLine);
        return orderLine.getId();
    }

    private void checkOrderLine(final OrderLine orderLine, final long id) {
        assertEquals("quantity", _defaultQuantity, orderLine.getQuantity());
    }

    private void removeOrderLine(final long orderLineId) throws RemoveException, ObjectNotFoundException {
    	final OrderLine orderLine = orderLineRepository.findById(orderLineId).get();
    	Order order = orderLine.getOrder();
        final long orderId = order.getId();
        order = orderRepository.findById(orderId).get();
    	final String customerId = order.getCustomer().getUsername();
    	final String itemId = orderLine.getItem().getId();
        Item item = itemRepository.findById(itemId).get();
        final String productId = item.getProduct().getId();
        Product product = productRepository.findById(productId).get();
        final String categoryId = product.getCategory().getId();
    	orderLineRepository.deleteById(orderLineId);
    	orderRepository.delete(order);
    	customerRepository.deleteById(customerId);
    	itemRepository.delete(item);
    	productRepository.delete(product);
    	categoryRepository.deleteById(categoryId);
    }
}
