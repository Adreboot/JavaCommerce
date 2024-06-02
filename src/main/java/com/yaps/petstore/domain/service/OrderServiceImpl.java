package com.yaps.petstore.domain.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.yaps.petstore.authentication.domain.dao.UserRepository;
import com.yaps.petstore.authentication.domain.dto.UserDTO;
import com.yaps.petstore.authentication.domain.model.User;
import com.yaps.petstore.domain.dao.ItemRepository;
import com.yaps.petstore.domain.dao.OrderLineRepository;
import com.yaps.petstore.domain.dao.OrderRepository;
import com.yaps.petstore.domain.dto.ItemDTO;
import com.yaps.petstore.domain.dto.OrderDTO;
import com.yaps.petstore.domain.dto.OrderLineDTO;
import com.yaps.petstore.domain.model.Item;
import com.yaps.petstore.domain.model.Order;
import com.yaps.petstore.domain.model.OrderLine;
import com.yaps.petstore.exception.CreateException;
import com.yaps.petstore.exception.FinderException;
import com.yaps.petstore.exception.RemoveException;

/**
 * This class is a facade for all order services.
 */
@Service
@Validated
public class OrderServiceImpl implements OrderService {

	private static final Logger LOGGER = LoggerFactory.getLogger(OrderServiceImpl.class);

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private OrderLineRepository orderLineRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ItemRepository itemRepository;

	@Autowired
	private ModelMapper orderModelMapper;
	
	@Autowired
	private ModelMapper itemModelMapper;

	@Autowired
	private ModelMapper orderLineModelMapper;
	
	@Autowired
	private ModelMapper userDTOModelMapper;

	@Autowired
	private CreditCardService creditCardService;

	// ======================================
	// = Constructors =
	// ======================================
	public OrderServiceImpl() {}

	// ======================================
	// = Order Business methods =
	// ======================================
	
	@Override
	@Transactional
	public Long createOrder(String customerId, Map<String, Integer> shoppingCart) throws CreateException, FinderException {
		final String mname = "createOrder (shopping cart)";
		LOGGER.debug("entering " + mname);

		if (shoppingCart == null || shoppingCart.isEmpty())
			throw new CreateException("Order object is empty");
		
		// Finds the customer
		if (customerId==null || userRepository.findById(customerId).isEmpty())
			throw new CreateException("Customer must exist to create an order");
		
		User customer = userRepository.findById(customerId).get();
		
		UserDTO customerDTO = userDTOModelMapper.map(customer, UserDTO.class);
		
		final OrderDTO orderDTO = new OrderDTO(customerDTO.getFirstname(),customerDTO.getLastname(),customerDTO.getStreet1(),customerDTO.getCity(),customerDTO.getZipcode(),customerDTO.getCountry());
		orderDTO.setStreet2(customerDTO.getStreet2());
		orderDTO.setState(customer.getState());
		orderDTO.setCustomerDTO(customerDTO);
		orderDTO.setCreditCardType(customer.getCreditCardType());
		orderDTO.setCreditCardNumber(customer.getCreditCardNumber());
		orderDTO.setCreditCardExpiryDate(customer.getCreditCardExpiryDate());
		final OrderDTO finalOrderDTO = createOrder(orderDTO);
		
		shoppingCart.keySet().forEach(itemId -> {
			if (itemRepository.findById(itemId).isPresent()) {
				Item item = itemRepository.findById(itemId).get();
				ItemDTO itemDTO = itemModelMapper.map(item, ItemDTO.class);
				OrderLineDTO orderLineDTO = new OrderLineDTO();
				orderLineDTO.setItemDTO(itemDTO);
				orderLineDTO.setUnitCost(item.getUnitCost());
				orderLineDTO.setQuantity((int) shoppingCart.get(itemId));
				orderLineDTO.setOrderDTO(finalOrderDTO);
				try {
					createOrderLine(orderLineDTO);
				} catch (CreateException e) {
					LOGGER.error(mname+" - error creating orderline for itemId : "+ itemId+" => "+e.getMessage());
				}
			}
		});
		return orderDTO.getId();
	}
	
	@Override
	@Transactional
	public OrderDTO createOrder(@Valid final OrderDTO orderDTO) throws CreateException, FinderException {
		final String mname = "createOrder";
		LOGGER.debug("entering " + mname);

		if (orderDTO == null)
			throw new CreateException("Order object is null");

		if (orderDTO.getCustomerDTO() == null || orderDTO.getCustomerDTO().getUsername() == null || userRepository.findById(orderDTO.getCustomerDTO().getUsername()).isEmpty())
			throw new CreateException("Customer must exist to create an order");

		// Credit Card Check
		if (orderDTO.getCreditCardNumber() != null && orderDTO.getCreditCardExpiryDate() != null
				&& !orderDTO.getCreditCardNumber().equals("") && !orderDTO.getCreditCardExpiryDate().equals(""))
			creditCardService.verifyCreditCard(orderDTO.getCreditCardDTO());

		// :::::::::::::::: We change DTO to model ::::::::::::::: //
		Order order = orderModelMapper.map(orderDTO, Order.class);

		// Creates the order
		orderRepository.save(order);
		orderDTO.setId(orderRepository.findLastId().get());
		LOGGER.debug("exiting " + mname);
		return orderDTO;
	}

	@Override
	@Transactional(readOnly = true)
	public OrderDTO findOrder(final long orderId) throws FinderException {
		final String mname = "findOrder";
		LOGGER.debug("entering " + mname + " for id " + orderId);
		
		checkId(orderId);
		// Finds the object
		Order order = orderRepository.findById(orderId).orElseThrow(()->new FinderException("Order must exist to be found"));

		LOGGER.debug("exiting " + mname);
		return orderModelMapper.map(order, OrderDTO.class);
	}

	@Override
	@Transactional
	public void deleteOrder(final long orderId) throws FinderException, RemoveException {
		final String mname = "deleteOrder";
		LOGGER.debug("entering " + mname + " for id " + orderId);
		
		checkId(orderId);
		
		Order order = orderRepository.findById(orderId).orElseThrow(()->new RemoveException("Order must exist to be deleted"));
		
		// Deletes the object
		orderLineRepository.findAllByOrder(order).forEach(o->orderLineRepository.delete(o));
		orderRepository.delete(order);
		LOGGER.debug("exiting " + mname);
	}

	// ======================================
	// = OrderLine Business methods =
	// ======================================

	@Override
	@Transactional
	public OrderLineDTO createOrderLine(@Valid OrderLineDTO orderLineDTO) throws CreateException {
		final String mname = "createOrderLine";
		LOGGER.debug("entering " + mname);

		if (orderLineDTO == null)
			throw new CreateException("OrderLine object is null");

		if (orderLineDTO.getItemDTO() == null || orderLineDTO.getItemDTO().getId() == null || itemRepository.findById(orderLineDTO.getItemDTO().getId()).isEmpty())
			throw new CreateException("Item must exist to create an OrderLine");

		try {
			findOrder(orderLineDTO.getOrderDTO().getId());
		} catch (NullPointerException | FinderException e) {
			throw new CreateException("Order must exist to create an OrderLine");
		}
		
		// :::::::::::::::: We change DTO to model ::::::::::::::: //
		OrderLine orderLine = orderLineModelMapper.map(orderLineDTO, OrderLine.class);

		// Creates the order
		orderLineRepository.save(orderLine);
		LOGGER.debug("exiting " + mname);
		return orderLineDTO;
	}

	@Override
	@Transactional(readOnly = true)
	public OrderLineDTO findOrderLine(long orderLineId) throws FinderException {
		final String mname = "findOrderLine";
		LOGGER.debug("entering " + mname + " for id " + orderLineId);
		
		checkId(orderLineId);
		
		OrderLine orderLine= orderLineRepository.findById(orderLineId).orElseThrow(()->new FinderException("orderLine must exist to be found"));

		LOGGER.debug("exiting " + mname);
		return orderLineModelMapper.map(orderLine, OrderLineDTO.class);
	}

	@Override
	@Transactional
	public void deleteOrderLine(long orderLineId) throws FinderException, RemoveException {
		final String mname = "deleteOrderLine";
		LOGGER.debug("entering " + mname + " for id " + orderLineId);
		
		checkId(orderLineId);
		
		OrderLine orderLine = orderLineRepository.findById(orderLineId).orElseThrow(()->new RemoveException("OrderLine must exist to be deleted"));

		orderLineRepository.delete(orderLine);
		LOGGER.debug("exiting " + mname);
	}

	@Override
	@Transactional(readOnly = true)
	public List<OrderLineDTO> findOrderLinesByOrderId(long orderId) throws FinderException {
		final String mname = "findOrderLinesByOrderId";
		LOGGER.debug("entering " + mname + " for orderId " + orderId);
		checkId(orderId);
		OrderDTO orderDTO = findOrder(orderId);
		Collection<OrderLine> orderLines = orderLineRepository.findAllByOrder(orderModelMapper.map(orderDTO, Order.class));
		int size;
		if ((size = orderLines.size()) == 0) {
			throw new FinderException("No OrderLine for this order");
		}
		List<OrderLineDTO> orderLinesDTO = ((List<OrderLine>) orderLines)
											.stream()
											.map(orderLine -> orderLineModelMapper.map(orderLine, OrderLineDTO.class))
											.collect(Collectors.toList());

		LOGGER.debug("exiting " + mname + " size of collection : " + size);
		return orderLinesDTO;
	}

	private void checkId(final long l) throws FinderException {
		if (l == 0)
			throw new FinderException("Id should not be 0");
	}

}
