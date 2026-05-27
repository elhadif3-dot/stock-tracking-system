public class Prices implements Outlinable<Prices> {
    public Float price;

    public Prices(float price) {
        this.price = price;
    }

    @Override
    public Prices getMax() {
        return new Prices(Float.MAX_VALUE);
    }

    @Override
    public Prices getMin() {
        return new Prices(Float.MIN_VALUE);
    }

    public int compareTo(Prices o) {
        return Float.compare(this.price, o.price);

    }

}
