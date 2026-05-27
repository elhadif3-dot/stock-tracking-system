public class TimeStamps implements Outlinable<TimeStamps> {
    public long timeStamp;

    public TimeStamps(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    @Override
    public TimeStamps getMax() {
        return new TimeStamps(Long.MAX_VALUE);
    }

    @Override
    public TimeStamps getMin() {
        return new TimeStamps(Long.MIN_VALUE);
    }

    public int compareTo(TimeStamps o) {
        return Long.compare(this.timeStamp, o.timeStamp);
    }
    public long getTimeStamp() {
        return timeStamp;
    }
}
