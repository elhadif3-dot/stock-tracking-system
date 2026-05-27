/**
 * This is an abstract class that represents a node in the 2-3 tree.
 */
public class Nodes<T extends Outlinable<T>> {
    private Boolean isLeaf;
    private Stock innerObject;
    private final Nodes<T>[] children;
    private T key;
    private Nodes<T> parent;
    private Nodes<T> nextNodes = null;
    public int size;

    @SuppressWarnings("unchecked")
    public Nodes(T key, Boolean isLeaf, Nodes<T> parent, Stock innerObject) {
        this.key = key;
        this.isLeaf = isLeaf;
        this.parent = parent;
        this.innerObject = innerObject;
        this.children = (Nodes<T>[]) new Nodes[3];
        this.size = 1;
    }

    @SuppressWarnings("unchecked")
    public Nodes(T key, Boolean isLeaf, Nodes<T> parent) {
        this.key = key;
        this.isLeaf = isLeaf;
        this.parent = parent;
        this.innerObject = null;
        this.children = (Nodes<T>[]) new Nodes[3];
    }

    @SuppressWarnings("unchecked")
    public Nodes(Nodes<T> parent) {
        this.parent = parent;
        this.isLeaf = false;
        this.children = (Nodes<T>[]) new Nodes[3];
    }

    @SuppressWarnings("unchecked")
    public Nodes() {
        this.isLeaf = false;
        this.children = (Nodes<T>[]) new Nodes[3];
    }

    public int getSize() {
        return this.size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public Boolean getIsLeaf() {
        return isLeaf;
    }

    public T getKey() {
        return key;
    }

    public Stock getInnerObject() {
        return innerObject;
    }

    public Nodes<T> getLeftChild() {
        return children[0];
    }

    public Nodes<T> getMiddleChild() {
        return children[1];
    }

    public Nodes<T> getRightChild() {
        return children[2];
    }

    public Nodes<T> getPNodes() {
        return parent;
    }

    public void setKey(T key) {
        this.key = key;
    }

    public void setLChild(Nodes<T> l) {
        children[0] = l;
    }

    public void setMChild(Nodes<T> m) {
        children[1] = m;
    }

    public void setRChild(Nodes<T> r) {
        children[2] = r;
    }

    public void setParent(Nodes<T> parent) {
        this.parent = parent;
    }

    public void setNext(Nodes<T> next) {
        nextNodes = next;
    }

    public Nodes<T> getNext() {
        return nextNodes;
    }
}
