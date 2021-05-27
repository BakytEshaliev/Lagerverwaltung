/**
 * Class of client
 * @author Bakyt Eshaliev
 * @version 1.0
 */

public class Client {
    /**
     * Name of Client
     */
    private String name;
    /**
     * Address of client
     */
    private String address;
    /**
     * Phone of client
     */
    private String phone;

    /**
     * Constructor of class
     * @param name Name of client
     * @param address Address of client
     * @param phone Phone of client
     */
    public Client(String name, String address, String phone){
        this.name = name;
        this.address = address;
        this.phone = phone;
    }

    /**
     * @return String value of client
     */
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("\nName : ").append(name).append("\nAddress : ").append(address).append("\nPhone : ").append(phone);
        return sb.toString();
    }
}
