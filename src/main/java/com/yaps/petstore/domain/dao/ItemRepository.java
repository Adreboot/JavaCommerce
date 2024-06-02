package com.yaps.petstore.domain.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.yaps.petstore.domain.model.Item;
import com.yaps.petstore.domain.model.Product;

public interface ItemRepository extends CrudRepository<Item, String> {

	Iterable<Item> findAllByProduct(Product product);
	
	@Query("select distinct i from Item i where upper(i.id) like upper(concat('%', :keyword,'%')) or upper(i.name) like upper(concat('%', :keyword,'%'))")
	Iterable<Item> findByIdOrNameContaining(@Param("keyword") String keyword);
	
	// Alternative ... mais avec doublonnage d'un paramètre identique
	Iterable<Item> findDistinctByIdContainingIgnoreCaseOrNameContainingIgnoreCase(String keyword1,String keyword2);
	
	@Query("select distinct i from Item i where i.unitCost>=:price")
	Iterable<Item> findByPriceGt(@Param("price")double price);
	// Alternative
	Iterable<Item> findByUnitCostGreaterThanEqual(double price);
	
	@Query("select distinct i from Item i where i.unitCost<=:price")
	Iterable<Item> findByPriceLt(@Param("price")double price);
	// Alternative
	Iterable<Item> findByUnitCostLessThanEqual(double price);
	
	Iterable<Item> findByUnitCostEquals(double price);
	
	@Query("select distinct i from Item i where i.unitCost>=:price and ( upper(i.id) like upper(concat('%', :keyword,'%')) or upper(i.name) like upper(concat('%', :keyword,'%')) )")
	Iterable<Item> findByPriceGreaterThanAndIdOrNameContaining(@Param("price")double price,@Param("keyword") String keyword);
	
	@Query("select distinct i from Item i where i.unitCost<=:price and ( upper(i.id) like upper(concat('%', :keyword,'%')) or upper(i.name) like upper(concat('%', :keyword,'%')) )")
	Iterable<Item> findByPriceLessThanAndIdOrNameContaining(@Param("price")double price,@Param("keyword") String keyword);
	
	@Query("select distinct i from Item i where i.unitCost=:price and ( upper(i.id) like upper(concat('%', :keyword,'%')) or upper(i.name) like upper(concat('%', :keyword,'%')) )")
	Iterable<Item> findByPriceEqualsAndIdOrNameContaining(@Param("price")double price,@Param("keyword") String keyword);

}
