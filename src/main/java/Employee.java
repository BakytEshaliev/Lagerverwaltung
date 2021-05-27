/**
 * Class of employee
 * @author Bakyt Eshaliev
 * @version 1.0
 */

import java.util.UUID;

public class Employee {
    /**
     * ID of employee
     */
    private String id;

    /**
     * Name of employee
     */
    private String name;

    /**
     * Constructor of Employee
     * @param name Name of employee
     */
    public Employee(String name){
        this.name = name;
        id = UUID.randomUUID().toString();
    }

    /**
     * @return ID of employee
     */
    public String getId(){
        return id;
    }

    /**
     * @return String value of employee
     */
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("\nId : ").append(id).append("\nName : ").append(name);
        return sb.toString();
    }
}
