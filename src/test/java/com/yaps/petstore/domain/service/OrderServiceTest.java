package com.yaps.petstore.domain.service;

import java.util.Collection;
import java.util.Random;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.yaps.petstore.authentication.domain.dto.UserDTO;
import com.yaps.petstore.authentication.domain.service.UserService;
import com.yaps.petstore.domain.constant.CreditCardStatus;
import com.yaps.petstore.domain.dao.OrderRepository;
import com.yaps.petstore.domain.dto.CategoryDTO;
import com.yaps.petstore.domain.dto.ItemDTO;
import com.yaps.petstore.domain.dto.OrderDTO;
import com.yaps.petstore.domain.dto.OrderLineDTO;
import com.yaps.petstore.domain.dto.ProductDTO;
import com.yaps.petstore.exception.CreateException;
import com.yaps.petstore.exception.DuplicateKeyException;
import com.yaps.petstore.exception.FinderException;
import com.yaps.petstore.exception.RemoveException;

import junit.framework.TestCase;

/**
 * This class tests the CatalogService class
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class OrderServiceTest extends TestCase {

//	private static final String COUNTER_NAME = "Order";

	@Autowired
	private OrderRepository orderRepository;
	@Autowired
	private OrderService orderService;
	@Autowired
	private CatalogService catalogService;
	@Autowired
	private UserService userService;
	@Autowired
	private CounterService counterService;

	private Random random = new Random();
	private int quantity = random.nextInt(30) + 1;

	// ==================================
	// = Test cases =
	// ==================================
	/**
	 * This test tries to find an object with a invalid identifier.
	 */
	@Test
	public void testServiceFindOrderWithInvalidValues() throws Exception {

		// Finds an object with a unknown identifier
//		final Long id = Long.parseLong(counterService.getUniqueId(COUNTER_NAME));
		final Long id = orderRepository.findLastId().orElse(10000L)+1;
		try {
			orderService.findOrder(id);
			fail("Object with unknonw id should not be found");
		} catch (FinderException e) {
		}

		// Finds an object with an empty identifier
		try {
			orderService.findOrder(random.nextLong() + 1L);
			fail("Object with empty id should not be found");
		} catch (FinderException e) {
		}

		// Finds an object with a null identifier
		try {
			orderService.findOrder(0);
			fail("Object with null id should not be found");
		} catch (FinderException e) {
		}
	}

	/**
	 * This method ensures that creating an object works. It first finds the object,
	 * makes sure it doesn't exist, creates it and checks it then exists.
	 */
	@Test
	public void testServiceCreateOrder() throws Exception {
//		final Long id = Long.parseLong(counterService.getUniqueId(COUNTER_NAME));
		final Long id = orderRepository.findLastId().orElse(10000L)+1;
		OrderDTO orderDTO = null;

		// Creates an object
		final long orderId = createOrder(id);

		// Ensures that the object exists
		try {
			orderDTO = findOrder(orderId);
		} catch (FinderException e) {
			fail("Object has been created it should be found");
		}

		// Checks that it's the right object
		checkOrder(orderDTO, id);

		// Cleans the test environment
		deleteOrder(orderId);

		try {
			findOrder(orderId);
			fail("Object has been deleted it shouldn't be found");
		} catch (FinderException e) {
		}
	}

	/**
	 * This test tries to create an object with a invalid values.
	 */
	@Test
	public void testServiceCreateOrderWithInvalidValues() throws Exception {
		OrderDTO orderDTO;

		// Creates an object with a null parameter
		try {
			orderService.createOrder(null);
			fail("Object with null parameter should not be created");
		} catch (CreateException e) {
		}

		// Creates an object with empty values
		try {
			orderDTO = new OrderDTO(new String(), new String(), new String(), new String(), new String(), new String());
			orderService.createOrder(orderDTO);
			fail("Object with empty values should not be created");
		} catch (Exception e) {
			assertTrue("Il devrait s'agir d'une  CreateException", e instanceof CreateException);
		}

		// Creates an object with null values
		try {
			orderDTO = new OrderDTO(null, null, null, null, null, null);
			orderService.createOrder(orderDTO);
			fail("Object with null values should not be created");
		} catch (Exception e) {
			assertTrue("Il devrait s'agir d'une  CreateException", e instanceof CreateException);
		}
	}

	/**
	 * This test tries to create an order with a invalid customer values.
	 */
	@Test
	public void testServiceCreateOrderWithInvalidCustomer() throws Exception {
		OrderDTO orderDTO;
		try {
			orderDTO = new OrderDTO("firstname", "lastname", "street", "city", "zipcode", "country");
			orderDTO.setCustomerDTO(null);
			orderService.createOrder(orderDTO);
			fail("Order with null customer should not be created");
		} catch (Exception e) {
			assertTrue("Il devrait s'agir d'une  CreateException", e instanceof CreateException);
		}

		try {
			orderDTO = new OrderDTO("firstname", "lastname", "street", "city", "zipcode", "country");
			orderDTO.setCustomerDTO(new UserDTO());
			orderService.createOrder(orderDTO);
			fail("Order with empty customer should not be created");
		} catch (Exception e) {
			assertTrue("Il devrait s'agir d'une  CreateException", e instanceof CreateException); // InvalidDataAccessApiUsageException
		}
	}

	/**
	 * This method ensures that creating an object with invalid credit card
	 * information doesn't work.
	 */
	@Test
	public void testServiceCreateOrderWithInvalidCreditCard() throws Exception {
//		final Long id = Long.parseLong(counterService.getUniqueId(COUNTER_NAME));
		final Long id = orderRepository.findLastId().orElse(10000L)+1;

		OrderDTO orderDTO = createOrderDTO(id);
		// ... with valid credit card number
		orderDTO.setCreditCardType("mastercard");
		orderDTO.setCreditCardExpiryDate("10/23");
		orderDTO.setCreditCardNumber("4564 1231 4564 1111");
		try {
			orderService.createOrder(orderDTO);
		} catch (FinderException e) {
			fail("Not a Visa. Object should be created");
		}

		// ... with invalid credit card type (no type !)
		orderDTO.setCreditCardExpiryDate("10/23");
		orderDTO.setCreditCardNumber("4564 1231 4564 2222");
		orderDTO.setCreditCardType("");
		try {
			orderService.createOrder(orderDTO);
			fail("Credit card type was invalid. Object shouldn't be created");
		} catch (FinderException e) {
			assertTrue("Il ne s'agit pas d'un problème de date invalide ?",
					e.getMessage().equals(CreditCardStatus.INVALID_CREDIT_CARD));
		}

		// ... with invalid credit card date
		orderDTO.setCreditCardExpiryDate("10/02");
		orderDTO.setCreditCardNumber("4564 1231 4564 2222");
		orderDTO.setCreditCardType("Visa");
		try {
			orderService.createOrder(orderDTO);
			fail("Credit card date was invalid. Object shouldn't be created");
		} catch (FinderException e) {
			assertTrue("Il ne s'agit pas d'un problème de date invalide ?",
					e.getMessage().equals(CreditCardStatus.INVALID_DATE));
		}

		// ... with invalid credit card number for a visa
		orderDTO.setCreditCardExpiryDate("10/23");
		orderDTO.setCreditCardNumber("4564 1231 4564 1111");
		orderDTO.setCreditCardType("Visa");
		try {
			orderService.createOrder(orderDTO);
			fail("Credit card number was invalid. Object shouldn't be created");
		} catch (FinderException e) {
			assertTrue("Il ne s'agit pas d'un problème de date invalide ?",
					e.getMessage().equals(CreditCardStatus.INVALID_NUMBER));
		}

		// The client doesn't pay with the credit card but with a cheque
		orderDTO.setCreditCardExpiryDate("");
		orderDTO.setCreditCardNumber("");
		orderDTO.setCreditCardType("");
		try {
			orderDTO = orderService.createOrder(orderDTO);
		} catch (FinderException e) {
			fail("Credit card wasn't used. Object should be created");
		}

		// Ensures that the object exists
		try {
			orderDTO = findOrder(orderDTO.getId());
		} catch (FinderException e) {
			fail("Object has been created it should be found");
		}

		// Cleans the test environment
		deleteOrder(orderDTO.getId());

		try {
			findOrder(orderDTO.getId());
			fail("Object has been deleted it shouldn't be found");
		} catch (FinderException e) {
		}
	}

	/**
	 * This test tries to create an orderLine with a invalid item values.
	 */
	@Test
	public void testServiceCreateOrderLineWithInvalidItemValues() throws Exception {
//		final Long id = Long.parseLong(counterService.getUniqueId(COUNTER_NAME));
		final Long id = orderRepository.findLastId().orElse(10000L)+1;
		// Creates an object
		final long orderId = createOrder(id);
		OrderDTO orderDTO = orderService.findOrder(orderId);

		OrderLineDTO orderLineDTO;
		try {
			orderLineDTO = new OrderLineDTO(1, 1.0);
			orderLineDTO.setItemDTO(null);
			orderLineDTO.setOrderDTO(orderDTO);
			orderService.createOrderLine(orderLineDTO);
			fail("OrderLine with null item should not be created");
		} catch (Exception e) {
			assertTrue("Il devrait s'agir d'une CreateException", e instanceof CreateException);
		}

		try {
			orderLineDTO = new OrderLineDTO(1, 1.0);
			orderLineDTO.setOrderDTO(orderDTO);
			orderLineDTO.setItemDTO(new ItemDTO());
			orderService.createOrderLine(orderLineDTO);
			fail("OrderLine empty item should not be created");
		} catch (CreateException e) {
		}
		// cleans the test environment
		deleteOrder(orderId);
	}

	/**
	 * This test tries to create an orderLine with a invalid item values.
	 */
	@Test
	public void testServiceCreateOrderLineWithInvalidOrderValues() throws Exception {
		final String id = counterService.getUniqueId("Item");
		// Creates an object
		createItem(id);
		ItemDTO itemDTO = catalogService.findItem(id);

		OrderLineDTO orderLineDTO;

		try {
			orderLineDTO = new OrderLineDTO(1, 1.0);
			orderLineDTO.setOrderDTO(null);
			orderLineDTO.setItemDTO(itemDTO);
			orderService.createOrderLine(orderLineDTO);
			fail("OrderLine with null order should not be created");
		} catch (Exception e) {
			assertTrue("Il devrait s'agir d'une  CreateException", e instanceof CreateException);
		}

		try {
			orderLineDTO = new OrderLineDTO(1, 1.0);
			orderLineDTO.setOrderDTO(new OrderDTO());
			orderLineDTO.setItemDTO(itemDTO);
			orderService.createOrderLine(orderLineDTO);
			fail("OrderLine with empty order should not be created");
		} catch (Exception e) {
			assertTrue("Il devrait s'agir d'une  CreateException", e instanceof CreateException);
		}
		// cleans the test environment
		deleteItem(id);
	}

	// ==================================
	// = Private Methods =
	// ==================================
	private OrderDTO findOrder(final long id) throws FinderException {
		final OrderDTO orderDTO = orderService.findOrder(id);
		return orderDTO;
	}

	private OrderDTO createOrderDTO(final long id) throws CreateException, FinderException {
		// Create Category
		final String categoryId = counterService.getUniqueId("Category");
		final CategoryDTO categoryDTO = new CategoryDTO("cat" + categoryId, "name" + categoryId,
				"description" + categoryId);
		catalogService.createCategory(categoryDTO);
		// Create Product
		final String productId = counterService.getUniqueId("Product");
		final ProductDTO productDTO = new ProductDTO("prod" + productId, "name" + productId, "description" + productId);
		productDTO.setCategoryDTO(categoryDTO);
		catalogService.createProduct(productDTO);
		// Create Item
		final String itemId = counterService.getUniqueId("Item");
		final ItemDTO itemDTO = new ItemDTO("item" + itemId, "name" + itemId, Double.parseDouble(itemId));
		itemDTO.setProductDTO(productDTO);
//        itemDTO.setImagePath("imagePath" + id);

		catalogService.createItem(itemDTO);

		// Create Customer
		String customerId = counterService.getUniqueId("Customer");
		final UserDTO customerDTO = new UserDTO("custo" + customerId, "firstname" + id, "lastname" + id);
		customerDTO.setPassword("abc123");
		customerDTO.setRole("ROLE_USER");
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
		userService.createUser(customerDTO);

		// Create Order
		final OrderDTO orderDTO = new OrderDTO("firstname" + id, "lastname" + id, "street1" + id, "city" + id,
				"zip" + id, "country" + id);
		orderDTO.setId(id);
		orderDTO.setCustomerDTO(customerDTO);
		orderDTO.setStreet2("street2" + id);
		orderDTO.setCreditCardExpiryDate("10/23");
		orderDTO.setCreditCardNumber("4564 1231 4564 1222");
		orderDTO.setCreditCardType("Visa");
		orderDTO.setState("state" + id);

		return orderDTO;
	}

	// Creates a category first, then a product linked to this category and an item
	// linked to the product
	// Creates a Customer and an order linked to the customer
	// Creates an orderLine linked to the order and the item
	private long createOrder(final long id) throws CreateException, FinderException {
		// Create Category
		final String categoryId = counterService.getUniqueId("Category");
		final CategoryDTO categoryDTO = new CategoryDTO("cat" + categoryId, "name" + categoryId,
				"description" + categoryId);
		catalogService.createCategory(categoryDTO);
		// Create Product
		final String productId = counterService.getUniqueId("Product");
		final ProductDTO productDTO = new ProductDTO("prod" + productId, "name" + productId, "description" + productId);
		productDTO.setCategoryDTO(categoryDTO);
		catalogService.createProduct(productDTO);
		// Create Item
		final String itemId = counterService.getUniqueId("Item");
		final ItemDTO itemDTO = new ItemDTO("item" + itemId, "name" + itemId, Double.parseDouble(itemId));
		itemDTO.setProductDTO(productDTO);
//        itemDTO.setImagePath("imagePath" + id);

		catalogService.createItem(itemDTO);

		// Create Customer
		String customerId = counterService.getUniqueId("Customer");
		final UserDTO customerDTO = new UserDTO("custo" + customerId, "firstname" + id, "lastname" + id);
		customerDTO.setPassword("abc123");
		customerDTO.setRole("ROLE_USER");
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
		userService.createUser(customerDTO);

		// Create Order
		final OrderDTO orderDTO = new OrderDTO("firstname" + id, "lastname" + id, "street1" + id, "city" + id,
				"zip" + id, "country" + id);
		orderDTO.setId(id);
		orderDTO.setCustomerDTO(customerDTO);
		orderDTO.setStreet2("street2" + id);
		orderDTO.setCreditCardExpiryDate("10/23");
		orderDTO.setCreditCardNumber("4564 1231 4564 1222");
		orderDTO.setCreditCardType("Visa");
		orderDTO.setState("state" + id);

		final OrderDTO result = orderService.createOrder(orderDTO);

		// Creates 1 orderline
		final OrderLineDTO oi1 = new OrderLineDTO(quantity, itemDTO.getUnitCost());
		oi1.setItemDTO(itemDTO);
		oi1.setOrderDTO(orderDTO);
		orderService.createOrderLine(oi1);

		return result.getId();
	}

	private void deleteOrder(final long orderId) throws RemoveException, FinderException {
		String catIBeDeleted = counterService.getLastId("Category");
		String prodIBeDeleted = counterService.getLastId("Product");
		String itemBeDeleted = counterService.getLastId("Item");
		final OrderDTO orderDTO = orderService.findOrder(orderId);
		orderService.deleteOrder(orderId);
		userService.deleteUser(orderDTO.getCustomerDTO().getUsername());
		catalogService.deleteItem("item" + itemBeDeleted);
		catalogService.deleteProduct("prod" + prodIBeDeleted);
		catalogService.deleteCategory("cat" + catIBeDeleted);
	}

	private void checkOrder(final OrderDTO orderDTO, final Long id) throws FinderException {
		Collection<OrderLineDTO> orderLinesDTO = orderService.findOrderLinesByOrderId(orderDTO.getId());
		assertEquals("firstname", "firstname" + id, orderDTO.getFirstname());
		assertEquals("lastname", "lastname" + id, orderDTO.getLastname());
		assertEquals("city", "city" + id, orderDTO.getCity());
		assertEquals("country", "country" + id, orderDTO.getCountry());
		assertEquals("state", "state" + id, orderDTO.getState());
		assertEquals("street1", "street1" + id, orderDTO.getStreet1());
		assertEquals("street2", "street2" + id, orderDTO.getStreet2());
		assertEquals("zipcode", "zip" + id, orderDTO.getZipcode());
		assertEquals("CreditCardExpiryDate", "10/23", orderDTO.getCreditCardExpiryDate());
		assertEquals("CreditCardNumber", "4564 1231 4564 1222", orderDTO.getCreditCardNumber());
		assertEquals("CreditCardType", "Visa", orderDTO.getCreditCardType());
		assertEquals("order items", 1, orderLinesDTO.size());
		OrderLineDTO firstOrderLineDTO = orderLinesDTO.iterator().next();
		assertEquals("First OrderLine quantity", quantity, firstOrderLineDTO.getQuantity());
	}

	// Creates a category first, then a product and then an item linked to this
	// product
	protected void createItem(final String id) throws CreateException, RemoveException, FinderException {
		// Create Category
		final String categoryId = counterService.getUniqueId("Category");
		final CategoryDTO categoryDTO = new CategoryDTO(categoryId, "name" + categoryId, "description" + categoryId);
		catalogService.createCategory(categoryDTO);
		// Create Product
		final String productId = counterService.getUniqueId("Product");
		final ProductDTO productDTO = new ProductDTO(productId, "name" + productId, "description" + productId);
		productDTO.setCategoryDTO(categoryDTO);
		catalogService.createProduct(productDTO);
		// Create Item
		final ItemDTO itemDTO = new ItemDTO(id, "name" + id, Double.parseDouble(id));
		itemDTO.setProductDTO(productDTO);
		itemDTO.setImagePath("imagePath" + id);
		// AJOUT oct2020 du try/catch
		try {
			catalogService.createItem(itemDTO);
		} catch (DuplicateKeyException e) {
			catalogService.deleteProduct(productDTO.getId());
			catalogService.deleteCategory(categoryDTO.getId());
			throw e;
		}
	}

	private void deleteItem(final String id) throws RemoveException, FinderException {
		final ItemDTO itemDTO = catalogService.findItem(id);
		final String productId = itemDTO.getProductDTO().getId();
		final ProductDTO productDTO = catalogService.findProduct(productId);
		final String categoryId = productDTO.getCategoryDTO().getId();
		catalogService.deleteItem(id);
		catalogService.deleteProduct(productId);
		catalogService.deleteCategory(categoryId);
	}

}
