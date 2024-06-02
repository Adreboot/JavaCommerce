package com.yaps.petstore.domain.service;

import java.util.List;

import javax.validation.Valid;

import com.yaps.petstore.domain.dto.CategoryDTO;
import com.yaps.petstore.domain.dto.ItemDTO;
import com.yaps.petstore.domain.dto.ProductDTO;
import com.yaps.petstore.exception.CreateException;
import com.yaps.petstore.exception.FinderException;
import com.yaps.petstore.exception.RemoveException;
import com.yaps.petstore.exception.UpdateException;


public interface CatalogService  {
	// ======================================
    // =      Category Business methods     =
    // ======================================
	public CategoryDTO createCategory(@Valid final CategoryDTO categoryDTO) throws CreateException ;
    public CategoryDTO findCategory(final String categoryId) throws FinderException ;
    public void deleteCategory(final String categoryId) throws FinderException, RemoveException ;
    public CategoryDTO updateCategory(@Valid final CategoryDTO categoryDTO) throws UpdateException ;
    public List<CategoryDTO> findCategories() throws FinderException ;

    // ======================================
    // =      Product Business methods     =
    // ======================================
    public ProductDTO createProduct(@Valid final ProductDTO productDTO) throws CreateException ;
    public ProductDTO findProduct(final String productId) throws FinderException ;
    public void deleteProduct(final String productId) throws FinderException, RemoveException ;
    public ProductDTO updateProduct(@Valid final ProductDTO productDTO) throws UpdateException ;
    public List<ProductDTO> findProducts() throws FinderException ;
    public List<ProductDTO> findProducts(String categoryId) throws FinderException;

    // ======================================
    // =        Item Business methods       =
    // ======================================
    public ItemDTO createItem(@Valid final ItemDTO itemDTO) throws CreateException ;
    public ItemDTO findItem(final String itemId) throws FinderException ;
    public void deleteItem(final String itemId) throws FinderException, RemoveException;
    public ItemDTO updateItem(@Valid final ItemDTO itemDTO) throws UpdateException ;
    public List<ItemDTO> findItems() throws FinderException ;
    public List<ItemDTO> findItems(String productId) throws FinderException ;
    
    public List<ItemDTO> searchItems(String keyword) throws FinderException ;
    public List<ItemDTO> searchItemsByPrice(String gtLt, Double price) throws FinderException ;
	public List<ItemDTO> searchItemsByPriceAndKeyword(String gtLt, double price, String keyword) throws FinderException ;

}
