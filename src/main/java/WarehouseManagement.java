/**
 * Class for Warehouse Management
 * You can:
 * 1)Give/withdraw authorization from employee/courier
 * 2)Receipt product
 * 3)Create/cancel Order
 * 4)Change product's information
 * 5)Take order
 * 6)Return order
 * 7)Mark order as delivered
 * All actions will be recorded to "resources/information.txt"
 * @author Bakyt Eshaliev
 * @version 1.0
 */

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class WarehouseManagement {
    /** Set of all (id: String) of authorized employees
     * @see Employee
     */
    private Set<String> authorizedEmployees = new HashSet<>();
    /** Set of all (id: String) of authorized couriers
     * @see Courier
     */
    private Set<String> authorizedCouriers = new HashSet<>();
    /** Map of all products in inventory (id : String, product : {@link Product}) */
    private Map<String, Product> inventory = new HashMap<>();
    /** Map of all orders waiting to delivery (id : String, order : {@link Order}) */
    private Map<String, Order> orders = new HashMap<>();
    /** Map of all orders taken to delivery (id : String, order : {@link Order}) */
    private Map<String, Order> deliveringOrders = new HashMap<>();

    /**
     * @return Set of authorized employees id
     * @see #authorizedEmployees
     */
    public Set<String> getAuthorizedEmployees() {
        return authorizedEmployees;
    }

    /**
     * @return Set of authorized couriers id
     * @see #authorizedCouriers
     */
    public Set<String> getAuthorizedCouriers() {
        return authorizedCouriers;
    }

    /**
     * @return Map of {@link #inventory} (id : String, product : {@link Product})
     */
    public Map<String, Product> getInventory(){
        return inventory;
    }

    /**
     * @return Map of {@link #orders} (id : String, order : {@link Order})
     */

    public Map<String, Order> getOrders(){
        return orders;
    }

    /**
     * @return Map of orders taken to delivery (id : String, order : {@link Order}) {@link #deliveringOrders}
     */

    public Map<String, Order> getDeliveringOrders(){
        return deliveringOrders;
    }

    /**
     * This method give authorization to employee and record this action to "resources/information.txt"
     *
     * @param e {@link Employee} to who we want to give authorization
     * @throws IOException  if the "resources/information.txt" exists but is a directory rather than a regular file, does not exist but cannot be created, or cannot be opened for any other reason
     */
    public void giveEmployeeAuthorization(Employee e) throws IOException {
        authorizedEmployees.add(e.getId());
        writeToFile( "EMPLOYEE AUTHORIZATION\n\nGive employee's authorization to " + e);
    }

    /**
     * This method withdraw authorization from employee and record this action to "resources/information.txt"
     *
     * @param e {@link Employee} from who we want to withdraw authorization
     * @throws IOException  if the "resources/information.txt" exists but is a directory rather than a regular file, does not exist but cannot be created, or cannot be opened for any other reason
     */
    public void withdrawEmployeeAuthorization(Employee e) throws IOException {
        authorizedEmployees.remove(e.getId());
        writeToFile("EMPLOYEE AUTHORIZATION\n\nWithdraw employee's authorization from " + e);
    }

    /**
     * This method give authorization to courier and record this action to "resources/information.txt"
     *
     * @param c {@link Courier} to who we want to give authorization
     * @throws IOException  if the "resources/information.txt" exists but is a directory rather than a regular file, does not exist but cannot be created, or cannot be opened for any other reason
     */
    public void giveCourierAuthorization(Courier c) throws IOException {
        authorizedCouriers.add(c.getId());
        writeToFile( "COURIER AUTHORIZATION\n\nGive courier's authorization to " + c);
    }

    /**
     * This method withdraw authorization from courier and record this action to "resources/information.txt"
     * Method is safety for case if courier already doesn't have authorization
     *
     * @param c {@link Courier} from who we want to withdraw authorization
     * @throws IOException  if the "resources/information.txt" exists but is a directory rather than a regular file, does not exist but cannot be created, or cannot be opened for any other reason
     */
    public void withdrawCourierAuthorization(Courier c) throws IOException {
        authorizedCouriers.remove(c.getId());
        writeToFile("COURIER AUTHORIZATION\n\nWithdraw courier's authorization from " + c);
    }

    /**
     * This method receipt product
     *
     * The method checks authorization of employee who receipting product and return result of this checking
     * if employee isn't authorized then method won't receipt product and won't record it
     *
     * If receipting product already exists in inventory then method will update product's information
     * Count of product will be updated by formula:
     * (new count) = (old count) + (count from reception) {@link Product}
     * All other params of product will just replace old params
     *
     * Else if receipting product doesn't exist in inventory then method will add to inventory receipting product
     *
     * Action of reception will be recorded to "resources/information.txt" with information about employee and receipting product
     *
     * @param e {@link Employee} who receipts product
     * @param product {@link Product} which is receipted
     * @return Status of action. If employee have authorization return true, else return false
     * @throws IOException if the "resources/information.txt" exists but is a directory rather than a regular file, does not exist but cannot be created, or cannot be opened for any other reason
     */
    public boolean receiptProduct(Employee e, Product product) throws IOException {
        if (authorizedEmployees.contains(e.getId())) {
            writeToFile("PRODUCT RECEPTION\n\nEMPLOYEE : " + e + "\n\nPRODUCT : " + product);

            String id = product.getArticle().getId();
            if (inventory.containsKey(id)) {
                product.setCount(inventory.get(id).getCount() + product.getCount());
                inventory.replace(id, product);
            } else {
                inventory.put(id, product);
            }
            return true;
        }
        else return false;

    }

    /**
     * This method add new order
     *
     * The method checks authorization of employee who creating order, amount of products in order to existing in inventory and order to already existing in {@link #orders} or in {@link #deliveringOrders}
     * and return result of this checking (false - if employee doesn't have authorization or we don't have in inventory enough amount of products or order is already exists, else - true)
     * If result of checking is false then method won't create order and won't record action
     *
     * If result of checking true:
     * 1)this method will change information in inventory about amounts of all products which are contained in order by formula:
     * (new amount) = (old amount) - (amount from order)
     * 2)this method will record action to "resources/information.txt" with information about employee who created order and information about order
     *
     *
     * @param e {@link Employee} who creating order
     * @param order {@link Order}
     * @return Result of action. If employee doesn't have authorization or we don't have in inventory enough amount of product return false, else return true
     * @throws IOException if the "resources/information.txt" exists but is a directory rather than a regular file, does not exist but cannot be created, or cannot be opened for any other reason
     */
    public boolean createOrder(Employee e, Order order) throws IOException {
        String id = order.getId();
        boolean orderIsAlreadyExits = orders.containsKey(id) || deliveringOrders.containsKey(id);
        if (authorizedEmployees.contains(e.getId()) && !orderIsAlreadyExits){
            List<Product> productsFromOrder = order.getProducts();
            for (Product productFromOrder : productsFromOrder){
                String productId = productFromOrder.getArticle().getId();
                Product productFromInventory = inventory.get(productId);
                if (productFromInventory.getCount() < productFromOrder.getCount()){
                    return false;
                }
                else {
                    productFromInventory.setCount(productFromInventory.getCount() - productFromOrder.getCount());
                }
            }
            writeToFile("CREATE NEW ORDER\n\nEMPLOYEE : " + e.toString() + "\n\nORDER : " + order.toString());
            orders.put(id, order);
            return true;
        }
        else return false;
    }

    /**
     * This method change information of existing product
     *
     * The method checks authorization of employee who changing information of product, product to existing in inventory and return result of this checking
     * if employee isn't authorized or product doesn't existing in inventory then method won't change information of product and won't record action
     *
     * If checking is passed then method will replace all old values with new values
     * and action will be recorded to "resources/information.txt" with information about employee who changing information, old information of product and new information of product
     *
     * @param e {@link Employee} who changing information
     * @param productWithNewInfo {@link Product} with new information, but old id
     * @return Result of action. If employee doesn't have authorization return false, else return true
     * @throws IOException if the "resources/information.txt" exists but is a directory rather than a regular file, does not exist but cannot be created, or cannot be opened for any other reason
     */
    public boolean changeProductInfo(Employee e, Product productWithNewInfo) throws IOException {
        String id = productWithNewInfo.getArticle().getId();
        if (authorizedEmployees.contains(e.getId()) && inventory.containsKey(id)){
            writeToFile("CHANGE PRODUCT INFORMATION\n\nEMPLOYEE :" + e + "\n\nOLD INFORMATION :" + inventory.get(id) +
                    "\n\nNEW INFORMATION :" + productWithNewInfo);

            inventory.replace(id, productWithNewInfo);
            return true;
        }
        else return false;
    }

    /**
     * This method cancel order
     *
     * The method checks authorization of employee who canceling order and return result of this checking
     * if employee isn't authorized then method won't cancel and won't record action
     *
     * If checking is passed then this method will cancel order and change information in inventory about amounts of all products which are contained in order by formula:
     * (new amount) = (old amount) + (amount from order) {@link Product}
     *
     * Action will be recorded to "resources/information.txt" with information about employee who canceling order and information about order
     *
     * WARNING!!!
     * You can cancel only the order which is in warehouse {@link #orders}
     * But can't cancel order which is taken to delivery {@link #deliveringOrders}
     *
     * @param e {@link Employee} who canceling order
     * @param id ID of order which is canceling
     * @param reason Reason of canceling order
     * @return Result of action. If employee doesn't have authorization return false, else return true
     * @throws IOException if the "resources/information.txt" exists but is a directory rather than a regular file, does not exist but cannot be created, or cannot be opened for any other reason
     * @see Order
     */
    public boolean cancelOrder(Employee e, String id, String reason) throws IOException{
        if (authorizedEmployees.contains(e.getId()) && orders.containsKey(id)){
            Order order = orders.get(id);
            orders.remove(id);

            List<Product> productsFromOrder = order.getProducts();
            for (Product product : productsFromOrder){
                String productId = product.getArticle().getId();
                Product productFromInventory = inventory.get(productId);
                productFromInventory.setCount(productFromInventory.getCount() + product.getCount());
            }

            writeToFile("CANCEL ORDER\n\nEMPLOYEE :" + e + "\n\nORDER :" + order + "\nREASON :\n" + reason);
            return true;
        }
        else return false;
    }

    /**
     * This method take order from {@link #orders} to {@link Courier}'s field and to {@link #deliveringOrders}
     *
     * The method checks authorization of courier who taking order, order's ID to existing in {@link #orders} and in {@link Courier}'s field
     * and return result of this checking
     * if courier isn't authorized or order doesn't exist in {@link #orders} then method won't take order and won't record action
     * @param c {@link Courier} who taking order
     * @param id ID of {@link Order}
     * @return Result
     * @throws IOException if the "resources/information.txt" exists but is a directory rather than a regular file, does not exist but cannot be created, or cannot be opened for any other reason
     */
    public boolean takeOrder(Courier c, String id) throws IOException {
        if (authorizedCouriers.contains(c.getId()) && orders.containsKey(id)){
            Order order = orders.get(id);
            writeToFile("TAKING ORDER TO DELIVERY\n\nCOURIER : " + c + "\n\nORDER :" + order);

            c.takeOrder(order);
            deliveringOrders.put(id, order);
            orders.remove(id);
            return true;
        }
        else return false;
    }

    /**
     * This method return order from {@link #deliveringOrders} and from {@link Courier}'s field to {@link #orders}
     * and record action to "resources/information.txt" with information about courier, order and reason
     *
     * The method checks authorization of courier who returning order, order's ID to existing in {@link #deliveringOrders} and return result of this checking
     * if courier isn't authorized or order doesn't exist in {@link #deliveringOrders} then method won't return order and won't record action
     *
     *
     * @param c {@link Courier}
     * @param id ID of {@link Order}
     * @param reason Reason of returning order
     * @return Status of action. If action is done - return true, but if action is canceled by checks - return false
     * @throws IOException if the "resources/information.txt" exists but is a directory rather than a regular file, does not exist but cannot be created, or cannot be opened for any other reason
     */
    public boolean returnOrder(Courier c, String id, String reason) throws IOException {
        if (authorizedCouriers.contains(c.getId()) && deliveringOrders.containsKey(id)){
            Order order = deliveringOrders.get(id);
            orders.put(id, order);
            deliveringOrders.remove(id);
            c.deleteOrder(id);

            writeToFile("RETURNING ORDER\n\n" + "COURIER :" + c + "\n\nORDER:" + order + "\n\nREASON:\n" + reason);
            return true;
        }
        return false;
    }

    /**
     * This method delete order from {@link Courier}'s field and from {@link #deliveringOrders}
     * and record action to "resources/information.txt" with information about courier and order
     *
     * The method checks authorization of courier who returning order, order's ID to existing in {@link #deliveringOrders} and return result of this checking
     * if courier isn't authorized or order doesn't exist in {@link #deliveringOrders} then method won't delete order and won't record action
     * @param c {@link Courier} who deliver order
     * @param id {@link Order}'s ID
     * @return Status of action. If action is done - return true, but if action is canceled by checks - return false
     * @throws IOException if the "resources/information.txt" exists but is a directory rather than a regular file, does not exist but cannot be created, or cannot be opened for any other reason
     */
    public boolean deliverOrder(Courier c, String id) throws IOException{
        if (authorizedCouriers.contains(c.getId()) && deliveringOrders.containsKey(id)){
            Order order = deliveringOrders.get(id);
            writeToFile("DELIVER ORDER\n\nCOURIER :" + c + "\n\nORDER :" + order);

            c.getOrders().remove(id);
            deliveringOrders.remove(id);
            return true;
        }
        else return false;
    }

    /**
     * Write all products from {@link #inventory} to "resources/information.txt"
     *
     * The method check authorization of employee
     * @param e {@link Employee}
     * @return Result of action
     * @throws IOException if the "resources/information.txt" exists but is a directory rather than a regular file, does not exist but cannot be created, or cannot be opened for any other reason
     */
    public boolean showInventory(Employee e) throws IOException {
        if (authorizedEmployees.contains(e.getId())) {
            StringBuilder sb = new StringBuilder();
            sb.append("SHOW INVENTORY:\n\nEMPLOYEE:").append(e);
            Set<Map.Entry<String, Product>> entries = inventory.entrySet();
            for (Map.Entry entry : entries) {
                Product product = (Product) entry.getValue();
                sb.append("\n\nPRODUCT").append(product.toString()).append("\n+++++++++++++++++");
            }
            writeToFile(sb.toString());
            return true;
        }
        return false;
    }

    /**
     * Write all order from {@link #orders} to "resources/information.txt"
     *
     * The method check authorization of employee
     * @param e {@link Employee} or {@link Courier}
     * @return Result of action
     * @throws IOException if the "resources/information.txt" exists but is a directory rather than a regular file, does not exist but cannot be created, or cannot be opened for any other reason
     */
    public boolean showOrders(Employee e) throws IOException{
        if (authorizedEmployees.contains(e.getId()) || authorizedCouriers.contains(e.getId())){
            StringBuilder sb = new StringBuilder();
            sb.append("SHOW ORDERS:\n\nEMPLOYEE/COURIER:").append(e);
            Set<Map.Entry<String, Order>> entries = orders.entrySet();
            for (Map.Entry entry : entries) {
                Order order = (Order) entry.getValue();
                sb.append("\n\nORDER:").append(order.toString()).append("\n+++++++++++++++++");
            }
            writeToFile(sb.toString());
            return true;
        }
        else return false;
    }

    /**
     * Write all order from {@link #orders} to "resources/information.txt"
     *
     * The method check authorization of employee
     * @param e {@link Employee}
     * @return Result of action
     * @throws IOException if the "resources/information.txt" exists but is a directory rather than a regular file, does not exist but cannot be created, or cannot be opened for any other reason
     */
    public boolean showDeliveringOrders(Employee e) throws IOException{
        if (authorizedEmployees.contains(e.getId())){
            StringBuilder sb = new StringBuilder();
            sb.append("SHOW DELIVERING ORDERS:\n\nEMPLOYEE:").append(e);
            Set<Map.Entry<String, Order>> entries = deliveringOrders.entrySet();
            for (Map.Entry entry : entries) {
                Order order = (Order) entry.getValue();
                sb.append("\n\nORDER:").append(order.toString()).append("\n+++++++++++++++++");
            }
            writeToFile(sb.toString());
            return true;
        }
        else return false;
    }

    /**
     * This method helps to write record to "resources/information.txt" input string
     * and add current date to record
     * @param str String which is needed to write
     * @throws IOException if the "resources/information.txt" exists but is a directory rather than a regular file, does not exist but cannot be created, or cannot be opened for any other reason
     */
    private void writeToFile(String str) throws IOException {
        FileWriter fw = new FileWriter("src/main/resources/information.txt", true);
        Date currentDate = new Date();

        fw.write(str + "\n\nDATE : " + currentDate.toString() + "\n------------------------------------\n");
        fw.flush();
        fw.close();
    }
}
