/**
 * Class of order
 * @author Bakyt Eshaliev
 * @version 1.0
 */

import java.util.List;
import java.util.UUID;

public class Order {
    /**
     * List of {@link Product}s which are in order
     */
    private List<Product> products;
    /**
     * ID of order
     */
    private String id;
    /**
     * Total price of order
     */
    private double totalPrice = 0;
    /**
     * {@link Client} who order
     */
    private Client client;

    /**
     * Constructor of order
     * @param products List of {@link Product}s which are in order
     * @param client {@link Client} who order
     */
    public Order(List<Product> products, Client client){

        id = UUID.randomUUID().toString();

        this.products = products;
        this.client = client;
        for (Product p : products){
            totalPrice += p.getPrice() * p.getCount();
        }
    }

    /**
     * @return ID of order
     */
    public String getId(){
        return id;
    }

    /**
     * @return List of {@link Product}s which are in order
     */
    public List<Product> getProducts(){
        return products;
    }

    /**
     * @return {@link Client} who order
     */
    public Client getClient() {
        return client;
    }

    /**
     * @return Total price of order
     */
    public double getTotalPrice() {
        return totalPrice;
    }

    /**
     * @return String value of order
     */
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("\nId : ").append(id).append("\nTotal price : ").append(totalPrice)
                .append("\n\nCLIENT : ").append(client.toString()).append("\n\nPRODUCTS : ");
        for (Product product : products){
            sb.append(product.toString()).append("\n++++++++++");
        }
        return sb.toString();
    }
}
