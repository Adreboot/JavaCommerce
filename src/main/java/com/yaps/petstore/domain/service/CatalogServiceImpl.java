package com.yaps.petstore.domain.service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.yaps.petstore.domain.dao.CategoryRepository;
import com.yaps.petstore.domain.dao.ItemRepository;
import com.yaps.petstore.domain.dao.ProductRepository;
import com.yaps.petstore.domain.dto.CategoryDTO;
import com.yaps.petstore.domain.dto.ItemDTO;
import com.yaps.petstore.domain.dto.ProductDTO;
import com.yaps.petstore.domain.model.Category;
import com.yaps.petstore.domain.model.Item;
import com.yaps.petstore.domain.model.Product;
import com.yaps.petstore.exception.CreateException;
import com.yaps.petstore.exception.DuplicateKeyException;
import com.yaps.petstore.exception.FinderException;
import com.yaps.petstore.exception.RemoveException;
import com.yaps.petstore.exception.UpdateException;

/**
 * This class is a facade for all catalog services.
 */
@Service
@Validated
public class CatalogServiceImpl implements CatalogService {

	// ======================================
	// = Attributes =
	// ======================================
	private static final Logger LOGGER = LoggerFactory.getLogger(CatalogServiceImpl.class);

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private ItemRepository itemRepository;

	@Autowired
	private ModelMapper commonModelMapper, productModelMapper, itemModelMapper;

	// ======================================
	// = Constructors =
	// ======================================
	public CatalogServiceImpl() {}

	// ======================================
	// = Category Business methods =
	// ======================================
	@Override
	@Transactional
	public CategoryDTO createCategory(@Valid final CategoryDTO categoryDTO) throws CreateException {
		final String mname = "createCategory";
		LOGGER.debug("entering " + mname);

		if (categoryDTO == null)
			throw new CreateException("Category object is null");
		
		try {
			checkId(categoryDTO.getId());
		} catch (FinderException e) {
			throw new CreateException("id is invalid");
		}

		 if(categoryRepository.findById(categoryDTO.getId()).isPresent())
				throw new DuplicateKeyException();
		 
		// :::::::::::::::: We change DTO to model ::::::::::::::: //
		Category category = commonModelMapper.map(categoryDTO, Category.class);

		// Creates the object
		categoryRepository.save(category);

		LOGGER.debug("exiting " + mname);
		return categoryDTO;
	}

	@Override
	@Transactional(readOnly = true)
	public CategoryDTO findCategory(final String categoryId) throws FinderException {
		final String mname = "findCategory";
		LOGGER.debug("entering " + mname + " for id " + categoryId);

		checkId(categoryId);

		Category category = categoryRepository.findById(categoryId).orElseThrow(()->new FinderException("Category must exist to be found"));

		LOGGER.debug("exiting " + mname);
		return commonModelMapper.map(category, CategoryDTO.class);
	}

	@Override
	@Transactional
	public void deleteCategory(final String categoryId) throws FinderException, RemoveException {
		final String mname = "deleteCategory";
		LOGGER.debug("entering " + mname + " for id " + categoryId);

		checkId(categoryId);

		Category category = categoryRepository.findById(categoryId).orElseThrow(()->new RemoveException("Category must exist to be deleted"));
		// Deletes the object
		categoryRepository.delete(category);
		LOGGER.debug("exiting " + mname);
	}

	@Override
	@Transactional
	public CategoryDTO updateCategory(@Valid final CategoryDTO updatedCategoryDTO) throws UpdateException {
		final String mname = "updateCategory";
		LOGGER.debug("entering " + mname);

		if (updatedCategoryDTO == null)
			throw new UpdateException("Category object is null");

		// Checks if the object exists
		if (categoryRepository.findById(updatedCategoryDTO.getId()).isEmpty())
			throw new UpdateException("Category must exist to be updated");

		// :::::::::::::::: We change DTO to model ::::::::::::::: //
		Category updatedCategory = commonModelMapper.map(updatedCategoryDTO, Category.class);
		// Updates the object
		categoryRepository.save(updatedCategory);
		LOGGER.debug("exiting " + mname);
		return commonModelMapper.map(updatedCategory, CategoryDTO.class);
	}

	@Override
	@Transactional(readOnly = true)
	public List<CategoryDTO> findCategories() throws FinderException {
		final String mname = "findCategories";
		LOGGER.debug("entering " + mname);

		// Finds all the objects
		final Iterable<Category> categories = categoryRepository.findAll();
		int size;
		if ((size = ((Collection<Category>) categories).size()) == 0) {
			throw new FinderException("No category in the database");
		}
		List<CategoryDTO> categoryDTOs = ((List<Category>) categories)
												.stream()
												.map(category -> commonModelMapper.map(category, CategoryDTO.class))
												.collect(Collectors.toList());

		LOGGER.debug("exiting " + mname + " size of collection : " + size);
		return categoryDTOs;
	}

	// ======================================
	// = Product Business methods =
	// ======================================
	@Override
	@Transactional
	public ProductDTO createProduct(@Valid final ProductDTO productDTO) throws CreateException {
		final String mname = "createProduct";
		LOGGER.debug("entering " + mname);

		if (productDTO == null || productDTO.getCategoryDTO()==null)
			throw new CreateException("Product object is invalid");
		
		try {
			checkId(productDTO.getId());
		} catch (FinderException e) {
			throw new CreateException("id is invalid");
		}

		if(productRepository.findById(productDTO.getId()).isPresent())
			throw new DuplicateKeyException();

		if (productDTO.getCategoryDTO().getId()==null || categoryRepository.findById(productDTO.getCategoryDTO().getId()).isEmpty())
			throw new CreateException("Category must exist to update a product");

		// :::::::::::::::: We change DTO to model ::::::::::::::: //
		Product product = productModelMapper.map(productDTO, Product.class);
		LOGGER.debug("product " + product);
		// Creates the object
		productRepository.save(product);

		LOGGER.debug("exiting " + mname);
		return productDTO;
	}

	@Override
	@Transactional(readOnly = true)
	public ProductDTO findProduct(final String productId) throws FinderException {
		final String mname = "findProduct";
		LOGGER.debug("entering : " + mname + " for id " + productId);

		checkId(productId);

		Product product = productRepository.findById(productId).orElseThrow(()->new FinderException("product must exist to be found"));

		LOGGER.debug("exiting " + mname);
		return productModelMapper.map(product, ProductDTO.class);
	}

	@Override
	@Transactional
	public void deleteProduct(final String productId) throws FinderException, RemoveException {
		final String mname = "deleteProduct";
		LOGGER.debug("entering : " + mname + " with id" + productId);

		checkId(productId);

		Product product = productRepository.findById(productId).orElseThrow(()->new  RemoveException("Product must exist to be deleted"));
		
		productRepository.delete(product);
		LOGGER.debug("exiting " + mname);
	}

	@Override
	@Transactional
	public ProductDTO updateProduct(@Valid final ProductDTO updatedProductDTO) throws UpdateException {
		final String mname = "updateProduct";
		LOGGER.debug("entering " + mname);

		if (updatedProductDTO == null)
			throw new UpdateException("Product object is null");

		// Checks if the object exists
		if (productRepository.findById(updatedProductDTO.getId()).isEmpty())
			throw new UpdateException("Product must exist to be updated");

		// :::::::::::::::: We change DTO to model ::::::::::::::: //
		Product updatedProduct = productModelMapper.map(updatedProductDTO, Product.class);

		// Updates the object
		productRepository.save(updatedProduct);
		LOGGER.debug("exiting " + mname);
		return productModelMapper.map(updatedProduct, ProductDTO.class);
	}

	@Override
	@Transactional(readOnly = true)
	public List<ProductDTO> findProducts() throws FinderException {
		final String mname = "findProducts";
		LOGGER.debug("entering " + mname);

		// Finds all the objects
		final Iterable<Product> products = productRepository.findAll();
		int size;
		if ((size = ((Collection<Product>) products).size()) == 0) {
			throw new FinderException("No Product in the database");
		}
		List<ProductDTO> productDTOs = ((List<Product>) products)
										.stream()
										.map(product -> productModelMapper.map(product, ProductDTO.class))
										.collect(Collectors.toList());

		LOGGER.debug("exiting " + mname + " size of collection : " + size);
		return productDTOs;
	}

	@Override
	@Transactional(readOnly = true)
	public List<ProductDTO> findProducts(String categoryId) throws FinderException {
		final String mname = "findProductsByCategory";
		LOGGER.debug("entering " + mname);

		checkId(categoryId);

		Category category = categoryRepository.findById(categoryId).orElseThrow(()->new FinderException("Category must exist to be found"));

		// Finds all the objects
		final Iterable<Product> productsByCategory = productRepository.findAllByCategory(category);
		
		int size;
		if ((size = ((Collection<Product>) productsByCategory).size()) == 0) {
			throw new FinderException("No Product in the database");
		}

		List<ProductDTO> productDTOs = ((List<Product>) productsByCategory)
										.stream()
										.map(product -> productModelMapper.map(product, ProductDTO.class))
										.collect(Collectors.toList());

		LOGGER.debug("exiting " + mname + " size of collection : " + size);
		return productDTOs;
	}

	// ======================================
	// = Item Business methods =
	// ======================================
	@Override
	@Transactional
	public ItemDTO createItem(@Valid final ItemDTO itemDTO) throws CreateException {
		final String mname = "createItem";
		LOGGER.debug("entering " + mname);

		if (itemDTO == null || itemDTO.getProductDTO()==null)
			throw new CreateException("Item object is invalid");
		
		try {
			checkId(itemDTO.getId());
		} catch (FinderException e) {
			throw new CreateException("id is invalid");
		}

		if (itemDTO.getProductDTO() == null)
            throw new CreateException("Product object is null");
        
        if( itemDTO.getProductDTO().getId()==null || productRepository.findById(itemDTO.getProductDTO().getId()).isEmpty())
      		throw new CreateException("Product must exist to create an Item");
        
		if(itemRepository.findById(itemDTO.getId()).isPresent())
			 throw new DuplicateKeyException();

		// :::::::::::::::: We change DTO to model ::::::::::::::: //
		Item item = itemModelMapper.map(itemDTO, Item.class);

		// Creates the object
		itemRepository.save(item);

		LOGGER.debug("exiting " + mname);
		return itemDTO;
	}

	@Override
	@Transactional(readOnly = true)
	public ItemDTO findItem(final String itemId) throws FinderException {
		final String mname = "findItem";
		LOGGER.debug("entering : " + mname + " for id" + itemId);

		checkId(itemId);

		Item item = itemRepository.findById(itemId).orElseThrow(()-> new FinderException("Item must exist to be found"));

		LOGGER.debug("exiting " + mname);
		return itemModelMapper.map(item, ItemDTO.class);
	}

	@Override
	@Transactional
	public void deleteItem(final String itemId) throws FinderException, RemoveException {
		final String mname = "deleteItem";
		LOGGER.debug("entering : " + mname + " with id" + itemId);

		checkId(itemId);

		Item item = itemRepository.findById(itemId).orElseThrow(()-> new RemoveException("Item must exist to be deleted"));
		
		itemRepository.delete(item);
		LOGGER.debug("exiting " + mname);
	}

	@Override
	@Transactional
	public ItemDTO updateItem(@Valid final ItemDTO updatedItemDTO) throws UpdateException {
		final String mname = "updateItem";
		LOGGER.debug("entering " + mname);

		if (updatedItemDTO == null)
			throw new UpdateException("Item object is null");

		// Checks if the object exists
		if (itemRepository.findById(updatedItemDTO.getId()).isEmpty())
			throw new UpdateException("Item must exist to be updated");

		// :::::::::::::::: We change DTO to model ::::::::::::::: //
		Item updatedItem = itemModelMapper.map(updatedItemDTO, Item.class);

		// Updates the object
		itemRepository.save(updatedItem);
		LOGGER.debug("exiting " + mname);
		return itemModelMapper.map(updatedItem, ItemDTO.class);
	}

	@Override
	@Transactional(readOnly = true)
	public List<ItemDTO> findItems() throws FinderException {
		final String mname = "findItems";
		LOGGER.debug("entering " + mname);

		// Finds all the objects
		final Iterable<Item> items = itemRepository.findAll();

		int size;
		if ((size = ((Collection<Item>) items).size()) == 0) {
			throw new FinderException("No Item in the database");
		}
		List<ItemDTO> itemDTOs = ((List<Item>) items)
								.stream()
								.map(item -> itemModelMapper.map(item, ItemDTO.class))
								.collect(Collectors.toList());

		LOGGER.debug("exiting " + mname + " size of collection : " + size);
		return itemDTOs;
	}

	@Override
	@Transactional(readOnly = true)
	public List<ItemDTO> findItems(String productId) throws FinderException {
		final String mname = "findItemsByProduct";
		LOGGER.debug("entering " + mname);

		checkId(productId);

		Product product = productRepository.findById(productId).orElseThrow(()->new FinderException("Product must exist to be found"));

		// Finds all the objects
		final Iterable<Item> itemsByProduct = itemRepository.findAllByProduct(product);
		
		int size;
		if ((size = ((Collection<Item>) itemsByProduct).size()) == 0) {
			throw new FinderException("No Item in the database");
		}
		
		List<ItemDTO> itemDTOs = ((List<Item>) itemsByProduct)
									.stream()
									.map(item -> itemModelMapper.map(item, ItemDTO.class))
									.collect(Collectors.toList());

		LOGGER.debug("exiting " + mname + " size of collection : " + size);
		return itemDTOs;
	}
	
	@Override
    @Transactional(readOnly=true)
    public List<ItemDTO> searchItems(String keyword) throws FinderException {
    	final String mname = "searchItems";
    	LOGGER.debug("entering "+mname);
        
     // retrieves the objects from the database
        Iterable<Item> itemsSearchedFor = itemRepository.findByIdOrNameContaining(keyword);    

        int size;
		if ((size = ((Collection<Item>) itemsSearchedFor).size()) == 0) {
			throw new FinderException("No Item for this search");
		}
		// model to DTO
		List<ItemDTO> itemDTOsSearchedFor = ((List<Item>) itemsSearchedFor)
									.stream()
									.map(item -> itemModelMapper.map(item, ItemDTO.class))
									.collect(Collectors.toList());

		LOGGER.debug("exiting " + mname + " size of the search : " + size);
        return itemDTOsSearchedFor;
    }
    
    @Override
    @Transactional(readOnly=true)
    public List<ItemDTO> searchItemsByPrice(String gtLt, Double price) throws FinderException {
    	final String mname = "searchItemsByPrice";
    	LOGGER.debug("entering "+mname);
        Iterable<Item> itemsSearchedFor = null;
        
     // retrieves the objects from the database
        switch(gtLt) {
    	case "GT":
    		itemsSearchedFor = itemRepository.findByUnitCostGreaterThanEqual(price);
    		break;
    	case "LT":
    		itemsSearchedFor = itemRepository.findByUnitCostLessThanEqual(price);
    		break;
    	case "EQ":
    		itemsSearchedFor = itemRepository.findByUnitCostEquals(price);
    		break;
        }
        int size;
		if ((size = ((Collection<Item>) itemsSearchedFor).size()) == 0) {
			throw new FinderException("No Item for this search");
		}
		// model to DTO
		List<ItemDTO> itemDTOsSearchedFor = ((List<Item>) itemsSearchedFor)
									.stream()
									.map(item -> itemModelMapper.map(item, ItemDTO.class))
									.collect(Collectors.toList());

		LOGGER.debug("exiting " + mname + " size of the search : " + size);
        return itemDTOsSearchedFor;
    }
    
	@Override
	@Transactional(readOnly=true)
	public List<ItemDTO> searchItemsByPriceAndKeyword(String gtLt, double price, String keyword) throws FinderException {
		final String mname = "searchItemsByPriceAndKeyword";
		LOGGER.debug("entering "+mname);
        Iterable<Item> itemsSearchedFor = null;
        
     /// retrieves the objects from the database
        switch(gtLt) {
    	case "GT":
    		itemsSearchedFor = itemRepository. findByPriceGreaterThanAndIdOrNameContaining(price,keyword);
    		break;
    	case "LT":
    		itemsSearchedFor = itemRepository.findByPriceLessThanAndIdOrNameContaining(price,keyword);
    		break;
    	case "EQ":
    		itemsSearchedFor = itemRepository.findByPriceEqualsAndIdOrNameContaining(price,keyword);
    		break;
        }              
        int size;
		if ((size = ((Collection<Item>) itemsSearchedFor).size()) == 0) {
			throw new FinderException("No Item for this search");
		}
		// model to DTO
		List<ItemDTO> itemDTOsSearchedFor = ((List<Item>) itemsSearchedFor)
									.stream()
									.map(item -> itemModelMapper.map(item, ItemDTO.class))
									.collect(Collectors.toList());

		LOGGER.debug("exiting " + mname + " size of the search : " + size);
        return itemDTOsSearchedFor;
	}
    
	// ======================================
    // =          Private Methods           =
    // ======================================
	
	private void checkId(final String id) throws FinderException {
		if (id == null || id.equals(""))
			throw new FinderException(id + " should not be null or empty");
	}

}
