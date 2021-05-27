/**
 * Class of courier
 * extends {@link Employee}
 */

import java.util.HashMap;
import java.util.Map;

public class Courier extends Employee{
    /** Map of all orders taken to delivery by courier (id : String, order : {@link Order}) */
    private Map<String, Order> orders = new HashMap<>();

    /**
     * Constructor of courier
     * @param name Name of courier
     */
    public Courier(String name) {
        super(name);
    }

    /**
     * Save order to {@link #orders}
     * @param order {@link Order} to save
     */
    public void takeOrder(Order order){
        orders.put(order.getId(), order);
    }

    /**
     * Delete order from {@link #orders}
     * @param id
     */
    public void deleteOrder(String id){
        orders.remove(id);
    }

    /**
     * @return Map of all orders taken to delivery by courier (id : String, order : {@link Order})
     */
    public Map<String, Order> getOrders(){
        return orders;
    }
}
