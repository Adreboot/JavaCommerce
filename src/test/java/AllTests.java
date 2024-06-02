

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.yaps.petstore.authentication.domain.dao.UserDAOTest;
import com.yaps.petstore.authentication.domain.service.UserServiceTest;
import com.yaps.petstore.domain.dao.CategoryDAOTest;
import com.yaps.petstore.domain.dao.ItemDAOTest;
import com.yaps.petstore.domain.dao.OrderDAOTest;
import com.yaps.petstore.domain.dao.OrderLineDAOTest;
import com.yaps.petstore.domain.dao.ProductDAOTest;
import com.yaps.petstore.domain.service.CatalogServiceTest;
import com.yaps.petstore.domain.service.CreditCardServiceTest;
import com.yaps.petstore.domain.service.OrderServiceTest;
import com.yaps.petstore.domain.service.ShoppingCartTest;
import com.yaps.petstore.util.UniqueIdGeneratorTest;
import com.yaps.petstore.web.CatagoryRestTestClient;
import com.yaps.petstore.web.CreateCustomerTestWebClient;
import com.yaps.petstore.web.FindUsersControllerTest;
import com.yaps.petstore.web.ItemRestTestClient;
import com.yaps.petstore.web.ManageCatalogTestWebClient;
import com.yaps.petstore.web.OrderTestWebClient;
import com.yaps.petstore.web.ProductRestTestClient;
import com.yaps.petstore.web.SearchItemsTestWebClient;
import com.yaps.petstore.web.ShoppingCartWebTest;
import com.yaps.petstore.web.UpdateAccountTestWebClient;
import com.yaps.petstore.web.VisualiseCatalogTestWebClient;
import com.yaps.petstore.web.WebTestMockMvc;

import junit.framework.JUnit4TestAdapter;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	UserDAOTest.class,
	UserServiceTest.class,
	CategoryDAOTest.class,
	ItemDAOTest.class,
	OrderDAOTest.class,
	OrderLineDAOTest.class,
	ProductDAOTest.class,
	CatalogServiceTest.class,
	CreditCardServiceTest.class,
	OrderServiceTest.class,
	ShoppingCartTest.class,
	UniqueIdGeneratorTest.class,
	CatagoryRestTestClient.class,
	CreateCustomerTestWebClient.class,
	FindUsersControllerTest.class,
	ItemRestTestClient.class,
	ManageCatalogTestWebClient.class,
	OrderTestWebClient.class,
	ProductRestTestClient.class,
	SearchItemsTestWebClient.class,
	ShoppingCartWebTest.class,
	UpdateAccountTestWebClient.class,
	VisualiseCatalogTestWebClient.class,
	WebTestMockMvc.class
})
public class AllTests {
	
	@Test
	public void contextLoads() {
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(suite());
		// org.junit.runner.JUnitCore.main("AllTests");
	}

	public static junit.framework.Test suite() {
		JUnit4TestAdapter adapter = new JUnit4TestAdapter(AllTests.class);
		return adapter;
	}

}
