package com.yaps.petstore.domain.service;

import java.util.Collection;
import java.util.Map;

import javax.validation.Valid;

import com.yaps.petstore.domain.dto.OrderDTO;
import com.yaps.petstore.domain.dto.OrderLineDTO;
import com.yaps.petstore.exception.CreateException;
import com.yaps.petstore.exception.FinderException;
import com.yaps.petstore.exception.RemoveException;


public interface OrderService {
    
	/// ======================================
    // =     Order Business methods     =
    // ======================================
	public Long createOrder(String customerId, Map<String,Integer> shoppingCart) throws CreateException, FinderException ;
    public OrderDTO createOrder(@Valid final OrderDTO orderDTO) throws CreateException, FinderException ;
    public OrderDTO findOrder(final long orderId) throws FinderException ;
    public void deleteOrder(final long orderId) throws FinderException, RemoveException ;
    
    /// ======================================
    // =     OrderLine Business methods     =
    // ======================================
    public OrderLineDTO createOrderLine(@Valid final OrderLineDTO orderLineDTO) throws CreateException ;
    public OrderLineDTO findOrderLine(final long orderLineId) throws FinderException ;
    public void deleteOrderLine(final long orderLineId) throws FinderException, RemoveException ;
    public Collection<OrderLineDTO> findOrderLinesByOrderId(final long  orderId) throws FinderException;

}
