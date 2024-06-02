package com.yaps.petstore.configuration;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.yaps.petstore.authentication.domain.dto.UserDTO;
import com.yaps.petstore.authentication.domain.model.User;
import com.yaps.petstore.domain.dto.ItemDTO;
import com.yaps.petstore.domain.dto.OrderDTO;
import com.yaps.petstore.domain.dto.OrderLineDTO;
import com.yaps.petstore.domain.dto.ProductDTO;
import com.yaps.petstore.domain.model.Item;
import com.yaps.petstore.domain.model.Order;
import com.yaps.petstore.domain.model.OrderLine;
import com.yaps.petstore.domain.model.Product;

@Configuration
public class MapperConfig {

	@Bean
	public ModelMapper commonModelMapper() {		// Category, Address, CrediCard, User
		ModelMapper modelMapper = new ModelMapper();
		return modelMapper;
	}

	@Bean
	public ModelMapper productModelMapper() {
		ModelMapper modelMapper = new ModelMapper();
		TypeMap<Product, ProductDTO> typeMap = modelMapper.createTypeMap(Product.class, ProductDTO.class);
		typeMap.addMappings(mapper -> mapper.map(Product::getCategory, ProductDTO::setCategoryDTO));
		return modelMapper;
	}
	
	@Bean
	public ModelMapper itemModelMapper() {
		ModelMapper modelMapper = new ModelMapper();
		TypeMap<Item, ItemDTO> typeMap = modelMapper.createTypeMap(Item.class, ItemDTO.class);
		typeMap.addMappings(mapper -> mapper.map(Item::getProduct, ItemDTO::setProductDTO));
		return modelMapper;
	}
	
	@Bean
	public ModelMapper orderModelMapper() {
		ModelMapper modelMapper = new ModelMapper();
		TypeMap<Order, OrderDTO> typeMap = modelMapper.createTypeMap(Order.class, OrderDTO.class);
		typeMap.addMapping(src -> src.getCreditCard(), OrderDTO::setCreditCardDTO);
		typeMap.addMapping(src -> src.getCustomer(), OrderDTO::setCustomerDTO);
		return modelMapper;
	}
	
	@Bean
	public ModelMapper orderLineModelMapper() {
		ModelMapper modelMapper = new ModelMapper();
		TypeMap<OrderLine, OrderLineDTO> typeMap = modelMapper.createTypeMap(OrderLine.class, OrderLineDTO.class);
		typeMap.addMapping(src -> src.getItem(), OrderLineDTO::setItemDTO);
		typeMap.addMapping(src -> src.getOrder(), OrderLineDTO::setOrderDTO);
		return modelMapper;
	}
	
	@Bean
	public ModelMapper userDTOModelMapper() {
		ModelMapper modelMapper = new ModelMapper();
		TypeMap<User, UserDTO> typeMap = modelMapper.createTypeMap(User.class, UserDTO.class);
		modelMapper.getConfiguration().setSkipNullEnabled(true);
		typeMap.addMappings(mapper -> mapper.skip(UserDTO::setPassword));
		return modelMapper;
	}
}
