
public class Stock {
    private StockId stockId;
    private Prices price;
    private Tree<TimeStamps> stockEvents;
    private TimeStamps initialTime;




    public Stock(String stockId, long TimeStamp, Float price) {
        if(price <= 0) {
            throw new IllegalArgumentException();
        }
        this.stockId = new StockId(stockId);
        this.price = new Prices(price);
        initialTime = new TimeStamps(TimeStamp);
        this.stockEvents = new Tree<>(initialTime.getMax(), initialTime.getMin());
    }

    protected Stock(){}//for events only

    public StockId getStockId() {

        return stockId;
    }

    public Prices getPrice() {
        return price;
    }
    private void changePrice(Float priceDifference) {
        price.price += priceDifference;
    }

    public void addEvent(long TimeStamp, Float priceChange) {
        StockEvents newStockEvent = new StockEvents(this.stockId.toString(),TimeStamp,priceChange);
        TimeStamps tempTime = new TimeStamps(TimeStamp);
        Nodes <TimeStamps> newNode = new Nodes<TimeStamps>(tempTime,true,stockEvents.getRoot(),newStockEvent);
        stockEvents.insert(stockEvents,newNode);
        this.changePrice(priceChange);
    }

    public void removeEvent(long TimeStamp) {
        TimeStamps temp = new TimeStamps(TimeStamp);
        Nodes<TimeStamps> event = stockEvents.search(stockEvents.getRoot(),temp);
        if(event != null){
            price.price -= ((StockEvents) event.getInnerObject()).getPriceChange();
            stockEvents.delete(stockEvents,event);
        }
        else throw new IllegalArgumentException ();
    }




}
