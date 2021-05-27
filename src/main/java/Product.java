/**
 * Class of product
 * @author Bakyt Eshaliev
 * @version 1.0
 */
public class Product {
    /**
     * Count of product
     */
    private int count;
    /**
     * Price of product
     */
    private double price;
    /**
     * {@link Article} of product
     */
    private Article article;

    /**
     * Constructor of class
     * @param count Count of product
     * @param price Price of product
     * @param article {@link Article} of product
     */
    public Product(int count, double price, Article article){
        this.article = article;
        this.count = count;
        this.price = price;
    }

    /**
     * @return Count of product
     */
    public int getCount() {
        return count;
    }

    /**
     * Set count of product
     * @param count New count
     */
    public void setCount(int count) {
        this.count = count;
    }

    /**
     * @return Price of Product
     */
    public double getPrice() {
        return price;
    }

    /**
     * @return {@link Article} of product
     */
    public Article getArticle() {
        return article;
    }

    /**
     * @return String value of product
     */
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(article.toString()).append("\nCount : ").append(count).append("\nPrice : ").append(price);
        return sb.toString();
    }
}
