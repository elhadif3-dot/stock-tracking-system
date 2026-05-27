public class StockEvents extends Stock {

    private Float priceChange;
    private StockId stockId;
    private TimeStamps timeStamp;

    public StockEvents(String stockId, long timeStamp , Float priceChange) {
        this.priceChange = priceChange;
        this.stockId = new StockId(stockId);
        this.timeStamp = new TimeStamps(timeStamp);
    }

    public Float getPriceChange() {
        return priceChange;
    }

    public StockId getStockId() {  // Change return type from String to StockId
        return stockId;
    }

    public TimeStamps getTimeStamp() {
        return timeStamp;
    }


}
