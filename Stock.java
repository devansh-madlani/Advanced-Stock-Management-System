public class Stock {
    private String name;
    private int quantity;
    private double buyPrice;
    private double currentPrice;
    private String position;

    public Stock(String name, int quantity, double buyPrice, double currentPrice, String position) {
        this.name = name;
        this.quantity = quantity;
        this.buyPrice = buyPrice;
        this.currentPrice = currentPrice;
        this.position = position;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getBuyPrice() {
        return buyPrice;
    }

    public double getCurrentPrice() {
        return currentPrice;
    }

    public String getPosition() {
        return position;
    }

    public void setCurrentPrice(double currentPrice) {
        this.currentPrice = currentPrice;
    }

    public double getProfitLoss() {
        return (currentPrice - buyPrice) * quantity;
    }

    public void displayStock() {
        System.out.println("Stock: " + name);
        System.out.println("Quantity: " + quantity);
        System.out.println("Buy Price: " + buyPrice);
        System.out.println("Current Price: " + currentPrice);
        System.out.println("Profit/Loss: " + getProfitLoss());
    }

    public String getPositionType() {
        if (quantity > 0)
            return "LONG";
        else if (quantity < 0)
            return "SHORT";
        else
            return "CLOSED";
    }
}