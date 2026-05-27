public class StockId implements Outlinable<StockId> {
    public String stockId;

    @Override
    public int compareTo(StockId other) {
        return this.stockId.compareTo(other.stockId);
    }

    public StockId(String stockId) {
        this.stockId = stockId;
    }

    @Override
    public StockId getMax() {
        return new StockId("\uffff");
    }
    @Override
    public StockId getMin() {
        return new StockId("\u0000");
    }
    public String toString() {
        return stockId;
    }

}
