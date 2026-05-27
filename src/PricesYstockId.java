public class PricesYstockId implements Outlinable<PricesYstockId>  {
    public Prices price;
    public StockId stockId;

    public PricesYstockId(Prices price, StockId stockId) {
        this.price = price;
        this.stockId = stockId;
    }

    public PricesYstockId getMax() {
        return new PricesYstockId(price.getMax(), stockId.getMax());
    }
    public PricesYstockId getMin() {
        return new PricesYstockId(price.getMin(), stockId.getMin());
    }

    @Override
    public int compareTo(PricesYstockId other) {
        int priceComparison = this.price.compareTo(other.price);
        if (priceComparison != 0) {
            return priceComparison;
        }
        return this.stockId.compareTo(other.stockId);
    }
    @Override
    public String toString() {
        return "PriceStockKey{" + "price=" + price + ", stockId=" + stockId + '}';
    }
}
