/**
 * Class of article of {@link Product}
 * @author Bakyt Eshaliev
 * @version 1.0
 */

import java.util.UUID;

public class Article {
    /**
     * ID of {@link Product}
     */
    private String id;
    /**
     * Name of {@link Product}
     */
    private String name;
    /**
     * Description of {@link Product}
     */
    private String description;

    /**
     * Constructor of article for new product
     * Auto generate ID
     * @param name Name of product
     * @param description Description of product
     */
    public Article(String name, String description) {
        id = UUID.randomUUID().toString();
        this.name = name;
        this.description = description;
    }

    /**
     * Constructor of article for existing product
     * @param id ID of product
     * @param name name of product
     * @param description description of product
     */
    public Article(String id, String name, String description){
        this.id = id;
        this.name = name;
        this.description = description;
    }

    /**
     * @return ID
     */
    public String getId() {
        return id;
    }

    /**
     * @return Name
     */
    public String getName() {
        return name;
    }

    /**
     * @return Description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return String calue of product
     */
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("\nId : ").append(id).append("\nName : ").append(name).append("\nDescription : ").append(description);
        return sb.toString();
    }
}
