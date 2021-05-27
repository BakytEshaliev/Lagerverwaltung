import org.junit.*;
import java.util.*;

public class TestWarehouseManagement {
    private WarehouseManagement wm;
    private Employee e;
    private Courier c;
    private Article a1;
    private Article a2;
    private Product product1;
    private Product product2;
    private Client client;
    private Order order;

    /**
     * Set values for tests
     */
    @Before
    public void set(){
        wm = new WarehouseManagement();
        e = new Employee("Employee");
        c = new Courier("Courier");
        a1 = new Article("Phone", "Can call");
        a2 = new Article("TV","Can broadcast TV");
        product1 = new Product(100,200, a1);
        product2 = new Product(1000, 500, a2);
        client = new Client("Client", "Address", "+996555555555");
        order = new Order(new ArrayList<>(), client);
    }

    /**
     * test Give Employee Authorization
     */
    @Test
    public void testGiveEmployeeAuthorization() throws Exception{
        wm.giveEmployeeAuthorization(e);
        boolean authorized = wm.getAuthorizedEmployees().contains(e.getId());
        Assert.assertTrue(authorized);
    }

    /**
     * Test Withdraw Employee Authorization
     */
    @Test
    public void testWithdrawEmployeeAuthorization() throws Exception{
        testGiveEmployeeAuthorization();
        wm.withdrawEmployeeAuthorization(e);
        boolean authorizedAfter = wm.getAuthorizedEmployees().contains(e.getId());
        Assert.assertFalse(authorizedAfter);
    }

    /**
     * Test Give Courier Authorization
     */
    @Test
    public void testGiveCourierAuthorization() throws Exception{
        wm.giveCourierAuthorization(c);
        boolean authorized = wm.getAuthorizedCouriers().contains(c.getId());
        Assert.assertTrue(authorized);
    }

    /**
     * Test Withdraw Courier Authorization
     */
    @Test
    public void testWithdrawCourierAuthorization() throws Exception{
        testGiveEmployeeAuthorization();
        wm.withdrawCourierAuthorization(c);
        boolean authorized = wm.getAuthorizedCouriers().contains(c.getId());
        Assert.assertFalse(authorized);
    }

    /**
     * Test Check Authorization Employee
     * expected to get false from all methods when employee don't have authorization
     * and to get true in opposite case
     */
    @Test
    public void testCheckAuthorizationEmployee() throws Exception{
        testGiveEmployeeAuthorization();
        Assert.assertTrue(wm.receiptProduct(e, product1));
        Assert.assertTrue(wm.createOrder(e, order));
        Assert.assertTrue(wm.changeProductInfo(e, product1));

        testWithdrawEmployeeAuthorization();
        Assert.assertFalse(wm.receiptProduct(e, product1));
        Assert.assertFalse(wm.createOrder(e, order));
        Assert.assertFalse(wm.changeProductInfo(e, product1));
    }

    /**
     * Test reception of product
     * Expected to create new products
     */
    @Test
    public void testReceiptProduct() throws Exception{
        testGiveEmployeeAuthorization();
        wm.receiptProduct(e, product1);
        wm.receiptProduct(e, product2);
        product1 = wm.getInventory().get(a1.getId());
        product2 = wm.getInventory().get(a2.getId());
        Article getArticle = product1.getArticle();
        Article getArticle2 = product2.getArticle();

        Assert.assertEquals(100, product1.getCount());
        Assert.assertEquals(1000, product2.getCount());
        Assert.assertEquals(200, product1.getPrice(), 0);
        Assert.assertEquals(500, product2.getPrice(), 0);
        Assert.assertEquals(a1, getArticle);
        Assert.assertEquals(a2, getArticle2);
    }

    /**
     * Test reception of existing product
     * Expected to change all fields and increase amount of product by formula:
     * (new amount) = (old amount) + (amount from reception)
     */
    @Test
    public void testReceiptExistingProduct() throws Exception{
        testReceiptProduct();
        Article newArticle = new Article(a1.getId(),"New Name", "New Description");
        Product receivedExistingProduct = new Product(50, 300, newArticle);

        wm.receiptProduct(e, receivedExistingProduct);
        product1 = wm.getInventory().get(a1.getId());
        Assert.assertEquals(150, product1.getCount());
        Assert.assertEquals(300, product1.getPrice(), 0);
        Article getArticle = product1.getArticle();
        Assert.assertEquals("New Name", getArticle.getName());
        Assert.assertEquals("New Description", getArticle.getDescription());
        Assert.assertEquals(a1.getId(), getArticle.getId());
        Assert.assertEquals(newArticle, getArticle);
    }

    /**
     * Test create order
     * Expected to
     * 1) add order in field orders in class {@link Order}
     * 2) properly count total price of order
     * 3) reduce count of items from inventory
     */
    @Test
    public void testCreateOrder() throws Exception{
        testGiveEmployeeAuthorization();
        testReceiptProduct();

        Product productForOrder1 = new Product(3, product1.getPrice(), product1.getArticle());
        Product productForOrder2 = new Product(2, product2.getPrice(), product2.getArticle());

        List<Product> productsList = new ArrayList<>();
        productsList.add(productForOrder1);
        productsList.add(productForOrder2);

        double totalPrice = 3 * 200 + 2 * 500;

        order = new Order(productsList, client);

        wm.createOrder(e, order);

        Order orderFromOrders = wm.getOrders().get(order.getId());

        Assert.assertEquals(productsList, orderFromOrders.getProducts());
        Assert.assertEquals(client, orderFromOrders.getClient());
        Assert.assertEquals(totalPrice, orderFromOrders.getTotalPrice(), 0);


        String product1Id = product1.getArticle().getId();
        String product2Id = product2.getArticle().getId();
        int product1Count = wm.getInventory().get(product1Id).getCount();
        int product2Count = wm.getInventory().get(product2Id).getCount();

        Assert.assertEquals(1000 - 2, product2Count);
        Assert.assertEquals(100 - 3, product1Count);
    }

    /**
     * Test create order with more product then we have
     * Expected to:
     * 1)return false from method
     * 2)don't change information about product from inventory
     * 3)don't create order
     */
    @Test
    public void testOrderMoreProductsThenWeHave() throws Exception{
        testGiveEmployeeAuthorization();
        testReceiptProduct();

        Product productForOrder = new Product(200, product1.getPrice(), product1.getArticle());

        List<Product> productsList = new ArrayList<>();
        productsList.add(productForOrder);

        Order orderTest = new Order(productsList, client);

        Assert.assertFalse(wm.createOrder(e, orderTest));

        String id = product1.getArticle().getId();
        Product productFromInventory = wm.getInventory().get(id);

        Assert.assertEquals(product1, productFromInventory);
        Assert.assertEquals(0, wm.getOrders().size());
    }

    /**
     * Test change product's information
     * Expected to replace old information of product with new information
     */
    @Test
    public void testChangeProductInfo() throws Exception{
        testReceiptProduct();
        Article newArticle = new Article(a1.getId(), "New Name1", "New Description1");
        Product productWithNewInfo = new Product(500, 400, newArticle);
        wm.changeProductInfo(e, productWithNewInfo);

        Product productFromInventory = wm.getInventory().get(a1.getId());
        Assert.assertEquals(newArticle, productFromInventory.getArticle());
        Assert.assertEquals(400, productFromInventory.getPrice(), 0);
        Assert.assertEquals(500, productFromInventory.getCount());
    }

    /**
     * Test take order
     * Expected to move order from orders to deliveringOrders and to Courier's field
     */
    @Test
    public void testTakeOrder() throws Exception{
        testGiveCourierAuthorization();
        testCreateOrder();
        wm.takeOrder(c, order.getId());

        Assert.assertTrue(c.getOrders().containsValue(order));
        Assert.assertTrue(wm.getDeliveringOrders().containsValue(order));
        Assert.assertFalse(wm.getOrders().containsValue(order));
    }

    /**
     * test return order
     * Expected to order from deliveringOrders and Courier's field to orders
     */
    @Test
    public void testReturnOrderTest() throws Exception{
        testTakeOrder();
        wm.returnOrder(c, order.getId(), "reason");

        Assert.assertFalse(c.getOrders().containsValue(order));
        Assert.assertFalse(wm.getDeliveringOrders().containsValue(order));
        Assert.assertTrue(wm.getOrders().containsValue(order));
    }

    /**
     * Test cancel order
     * Expected to:
     * 1) Delete order
     * 2) Change products amount in inventory
     */
    @Test
    public void testCancelOrder() throws Exception{
        testCreateOrder();

        wm.cancelOrder(e, order.getId(), "reason");

        Assert.assertFalse(wm.getOrders().containsValue(order));

        String productId1 = product1.getArticle().getId();
        Product productFromInventory1 = wm.getInventory().get(productId1);
        String productId2 = product2.getArticle().getId();
        Product productFromInventory2 = wm.getInventory().get(productId2);

        Assert.assertEquals(100, productFromInventory1.getCount());
        Assert.assertEquals(1000, productFromInventory2.getCount());
    }

    /**
     * Test deliver order
     * Expected to delete order from deliveryOrders and from Courier's field
     */
    @Test
    public void testDeliverOrder() throws Exception{
        testTakeOrder();
        wm.deliverOrder(c, order.getId());

        Assert.assertFalse(wm.getDeliveringOrders().containsValue(order));
        Assert.assertFalse(c.getOrders().containsValue(order));
    }

    /**
     * Test check courier's authorization
     * Expected to:
     * 1)return false from methods when courier doesn't have authorization
     * 2)return true in opposite case
     */
    @Test
    public void testCheckAuthorizationCourier() throws Exception{
        testCreateOrder();

        Assert.assertFalse(wm.takeOrder(c, order.getId()));
        Assert.assertFalse(wm.returnOrder(c, order.getId(), "reason"));

        testGiveCourierAuthorization();
        Assert.assertTrue(wm.takeOrder(c, order.getId()));
        Assert.assertTrue(wm.returnOrder(c, order.getId(), "reason"));
    }

    /**
     * Test creating 2 same orders
     * Expected to
     * 1)return false while creating second order
     * 2)size of orders = 1
     */

    @Test
    public void testCreating2SameOrders() throws Exception{
        testGiveEmployeeAuthorization();
        wm.createOrder(e, order);
        Assert.assertFalse(wm.createOrder(e, order));
        Assert.assertEquals(1, wm.getOrders().size());
    }

    /**
     * Test change information of product which isn't existing
     * Expected to:
     * 1) return false while changing information
     * 2) don't adding product to inventory
     */
    @Test
    public void testChangeInformationAboutProductWhichIsNotExisting() throws Exception{
        testGiveEmployeeAuthorization();
        Assert.assertFalse(wm.changeProductInfo(e, product1));
        Assert.assertEquals(0, wm.getInventory().size());
    }

    /**
     * Cancel order which is not existing
     * Expected to return false while canceling order
     */
    @Test
    public void testCancelOrderWhichIsNotExisting() throws Exception{
        testGiveEmployeeAuthorization();
        Assert.assertFalse(wm.cancelOrder(e, order.getId(), "reason"));
    }

    /**
     * Test take order which is not existing
     * Expected to
     * 1) return false while taking
     * 2) don't add order to deliveringOrders and to Courier's field
     */
    @Test
    public void testTakeOrderWhichIsNotExisting() throws Exception{
        testGiveCourierAuthorization();
        Assert.assertFalse(wm.takeOrder(c, order.getId()));
        Assert.assertEquals(0, c.getOrders().size());
        Assert.assertEquals(0, wm.getDeliveringOrders().size());
    }

    /**
     * Test take order which is already exist in courier field
     * Expected to get normal case cause if order existing in orders then all is OK
     */
    @Test
    public void testTakeOrderWhichIsAlreadyExistInCourierField() throws Exception{
        testGiveCourierAuthorization();
        testCreateOrder();
        c.takeOrder(order);
        Assert.assertTrue(wm.takeOrder(c, order.getId()));
        Assert.assertEquals(0, wm.getOrders().size());
        Assert.assertEquals(1, wm.getDeliveringOrders().size());
        Assert.assertEquals(1, c.getOrders().size());
    }

    /**
     * Test take order which is already taken
     * Expected to cancel operation
     */
    @Test
    public void testTakeOrderWhichIsAlreadyTaken() throws Exception{
        testTakeOrder();
        Assert.assertFalse(wm.takeOrder(c, order.getId()));
        Assert.assertEquals(1, c.getOrders().size());
        Assert.assertEquals(0, wm.getOrders().size());
        Assert.assertEquals(1, wm.getDeliveringOrders().size());
    }

    /**
     * Test return order which is not exist in courier field
     * Expected to get normal case cause if order exists in deliveringOrders is OK
     */
    @Test
    public void testReturnOrderWhichIsNotExistInCourierField() throws Exception{
        testTakeOrder();
        c.deleteOrder(order.getId());
        Assert.assertTrue(wm.returnOrder(c, order.getId(), "reason"));
        Assert.assertEquals(1, wm.getOrders().size());
        Assert.assertEquals(0, wm.getDeliveringOrders().size());
    }

    /**
     * Test return order which isn't exist in deliveringOrders
     * Expected to cancel operation
     */
    @Test
    public void testReturnOrderWhichIsNotExistInDeliveringOrders() throws Exception{
        testTakeOrder();
        wm.getDeliveringOrders().remove(order.getId());
        Assert.assertFalse(wm.returnOrder(c, order.getId(), "reason"));
        Assert.assertEquals(0, wm.getOrders().size());
        Assert.assertEquals(0, wm.getDeliveringOrders().size());
    }
    /**
     * Test deliver order which isn't in deliveringOrders
     * Expected to cancel cancel operation
     */
    @Test
    public void testDeliverOrderWhichIsNotInDeliveringOrdersField() throws Exception{
        testTakeOrder();
        wm.getDeliveringOrders().remove(order.getId());
        Assert.assertFalse(wm.returnOrder(c, order.getId(), "reason"));
        Assert.assertEquals(0, wm.getOrders().size());
        Assert.assertEquals(0, wm.getDeliveringOrders().size());
    }

    /**
     * Expected to write inventory to "resources/information.txt"
     */
    @Test
    public void testShowInventory() throws Exception{
        testReceiptProduct();
        wm.showInventory(e);
    }

    /**
     * Expected to write orders to "resources/information.txt" in both cases with courier and employee
     */
    @Test
    public void testShowOrders() throws Exception{
        testCreateOrder();
        wm.showOrders(e);
        testGiveCourierAuthorization();
        wm.showOrders(c);
    }

    /**
     * Expected to write delivering orders to "resources/information.txt"
     */
    @Test
    public void testShowDeliveringOrders() throws Exception{
        testTakeOrder();
        wm.showDeliveringOrders(e);
    }
}
