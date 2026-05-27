
/**
 * this is an abstract implementation of a 2-3 tree to be used in this project
 */
public class Tree <T extends Outlinable<T>> {
    private Nodes<T> root;
    T leftSentinel;
    T rightSentinel;

    

    public Tree ( T max, T min){
        Nodes<T> x = new Nodes<>(null,false,null);
        Nodes<T> l = new Nodes<T>(null,true,null);
        Nodes<T> m = new Nodes<T>(null,true,null);
        l.setKey(min);
        m.setKey(max);
        l.setParent(x);
        m.setParent(x);
        x.setKey(max);
        x.setLChild(l);
        x.setMChild(m);
        this.root = x;
        leftSentinel = min;
        rightSentinel = max;
    }


    public Nodes<T> search(Nodes<T> x, T k) {
        if (x == null){
            return null;
        }
        if (x.getIsLeaf())
            if (x.getKey().compareTo(k) == 0){
                return x;
            }
            else{
                return null;
            }
        if (k.compareTo(x.getLeftChild().getKey()) <= 0){
            return search(x.getLeftChild(),k);
        }
        else if (k.compareTo(x.getMiddleChild().getKey()) <= 0){///check that it doesnt do any problems!!!
            return search(x.getMiddleChild(),k);
        }
        else{
            return search(x.getRightChild(),k);
        }
    }


    public Nodes <T> Minimum(){
        Nodes<T> x = root;
        T temp = x.getKey();
        while (x.getIsLeaf() == false){
            x = x.getLeftChild();
        }
        x = x.getPNodes().getMiddleChild();
        if(x.getKey().compareTo(temp.getMax()) != 0) {
            return x;
        }
        else return null;//the tree is empty

    }

    public Nodes<T> Successor(Nodes<T> x) {
        if (x == null) return null;

        // 1️⃣ אם יש לילד אמצעי או ימין, חפש את הצומת הקטן ביותר בתת-העץ
        if (!x.getIsLeaf() && x.getMiddleChild() != null) {
            x = x.getMiddleChild(); // עבור לילד האמצעי
            while (!x.getIsLeaf()) {
                x = x.getLeftChild(); // המשך שמאלה עד שמגיעים לעלה
            }
            return x;
        } else if (!x.getIsLeaf() && x.getRightChild() != null) {
            x = x.getRightChild(); // עבור לילד הימני אם אין ילד אמצעי
            while (!x.getIsLeaf()) {
                x = x.getLeftChild(); // המשך שמאלה עד שמגיעים לעלה
            }
            return x;
        }

        // 2️⃣ אם אין ילדים מתאימים, עלה במעלה העץ למציאת היורש
        Nodes<T> parent = x.getPNodes();
        while (parent != null) {
            // אם הצומת הנוכחי הוא ילד שמאלי, היורש הוא ההורה
            if (x == parent.getLeftChild()) {
                return parent.getMiddleChild() != null ? parent.getMiddleChild() : parent.getRightChild();
            }
            // אם הוא ילד אמצעי, היורש הוא הילד הימני
            else if (parent.getMiddleChild() != null && x == parent.getMiddleChild()) {
                return parent.getRightChild();
            }
            // המשך למעלה אם לא נמצא עד כה יורש
            x = parent;
            parent = parent.getPNodes();
        }

        return null; // אם לא נמצא יורש, החזר null
    }
    public Nodes<T> Predecessor(Nodes<T> x) {
        if (x == null) return null;

        // 1️⃣ אם יש ילד שמאלי - יורדים ימינה כדי למצוא את הגדול ביותר בתת-העץ השמאלי
        if (x.getLeftChild() != null && !x.getIsLeaf()) {
            x = x.getLeftChild();
            while (!x.getIsLeaf()) {
                x = x.getRightChild() != null ? x.getRightChild() : x.getMiddleChild();
            }
            return x;
        }

        // 2️⃣ אם אין ילד שמאלי, מטפסים במעלה העץ עד שנמצא צומת שבו `x` היה צאצא ימני או אמצעי
        Nodes<T> z = x.getPNodes();
        while (z != null && (x == z.getLeftChild() || (z.getMiddleChild() != null && x == z.getMiddleChild()))) {
            x = z;
            z = z.getPNodes();
        }

        if (z == null) return null; // אם הגענו לשורש ואין יורש, אין `Predecessor`

        // 3️⃣ אם `x` היה צאצא ימני או אמצעי - `Predecessor` הוא תת-העץ השמאלי של האב
        Nodes<T> y;
        if (x == z.getRightChild()) {
            y = z.getMiddleChild() != null ? z.getMiddleChild() : z.getLeftChild();
        } else {
            y = z.getLeftChild();
        }

        // 4️⃣ יורדים לתת-העץ הימני ביותר כדי למצוא את העלה הקטן ביותר המתאים
        while (y != null && !y.getIsLeaf()) {
            y = y.getRightChild() != null ? y.getRightChild() : y.getMiddleChild();
        }

        return y;
    }

    /*
     * this function updates the key to the minimum key in the subtree rooted at x
     * x.middle ad x.right may be null
     */
    public void updateKey(Nodes<T> x){
        if(x.getLeftChild().getKey().compareTo(rightSentinel) < 0) {
            x.setKey(x.getLeftChild().getKey());
        }
        if (x.getMiddleChild() != null ){
            if(x.getMiddleChild().getKey().compareTo(rightSentinel) < 0) {
                x.setKey(x.getMiddleChild().getKey());
            }
        }
        if (x.getRightChild() != null){
            if(x.getRightChild().getKey().compareTo(rightSentinel) < 0) {
                x.setKey(x.getRightChild().getKey());
            }
            else {
                x.setKey(x.getMiddleChild().getKey());
            }
        }
    }

    public void setChild(Nodes<T> x, Nodes<T> l, Nodes<T> m, Nodes<T> r){
        x.setLChild(l);
        x.setMChild(m);
        x.setRChild(r);
        l.setParent(x);
        if(m != null){
            m.setParent(x);
        }
        if(r != null){
            r.setParent(x);
        }
        updateKey(x);
    }
    
    public Nodes<T> insertAndSplit(Nodes<T> x, Nodes<T> z){
        Nodes<T> l = x.getLeftChild();
        Nodes<T> m = x.getMiddleChild();
        Nodes<T> r = x.getRightChild();
        if (r == null){
            if (z.getKey().compareTo(l.getKey()) < 0){
                setChild(x,z,l,m);
                updateKey(x);
            }
            else if (z.getKey().compareTo(m.getKey()) < 0){
                setChild(x,l,z,m);
            }
            else{
                setChild(x,l,m,z);
            }
            return null;
        }
        Nodes<T> y = new Nodes<>(null,false,x.getPNodes());
        if (z.getKey().compareTo(l.getKey()) < 0) {
            setChild(x, z, l, null);
            setChild(y, m, r, null);
        }
        else if (z.getKey().compareTo(m.getKey()) < 0) {
                setChild(x, l, z, null);
                setChild(y, m, r, null);
            }
        else if (z.getKey().compareTo(r.getKey()) < 0) {
            setChild(x, l, m, null);
            setChild(y, z, r, null);
        }
        else{
            setChild(x,l,m,null);
            setChild(y,r,z,null);
        }
        return y;
    }


    public void insert(Tree<T> T, Nodes<T> z){
        Nodes<T> y = this.root;
        while (y.getIsLeaf() == false){
            if (z.getKey().compareTo(y.getLeftChild().getKey()) < 0){
                y = y.getLeftChild();
            }
            else if (z.getKey().compareTo(y.getMiddleChild().getKey()) < 0){
                y = y.getMiddleChild();
            }
            else if (y.getRightChild() == null) {
                y = y.getMiddleChild();
            }
            else{
                y = y.getRightChild();

            }
        }
        Nodes<T> x = y.getPNodes();
        z = insertAndSplit(x,z);
        while (x != T.root){
            x = x.getPNodes();
            if (z != null){
                z = insertAndSplit(x,z);
            }
            else updateKey(x);
        }
        if (z != null){
            Nodes<T> w = new Nodes<>(null,false,null);
            setChild(w,x,z,null);
            T.root = w;
        }
    }
    public void delete(Nodes<T> x){
        x.setParent(null);
        x=null;
//        x.setLChild(null);
//        x.setMChild(null);
//        x.setRChild(null);
    }
//    public Nodes<T> getClosestEqualOrLarger(Nodes<T> x, T k) {
//        if (x == null) {
//            return null;
//        }
//
//        // אם הצומת הוא עלה, בדוק אם הוא מתאים והחזר אותו
//        if (x.getIsLeaf()) {
//            return x.getKey().compareTo(k) >= 0 ? x : null;
//        }
//
//        // קבלת ילדים
//        Nodes<T> leftChild = x.getLeftChild();
//        Nodes<T> middleChild = x.getMiddleChild();
//        Nodes<T> rightChild = x.getRightChild();
//
//        // חיפוש בצומת השמאלי
//        if (k.compareTo(x.getLeftChild().getKey()) <= 0) {
//            return getClosestEqualOrLarger(leftChild, k);
//        }
//        // חיפוש בצומת האמצעי
//        else if (middleChild != null && k.compareTo(middleChild.getKey()) <= 0) {
//            return getClosestEqualOrLarger(middleChild, k);
//        }
//        // חיפוש בצומת הימני (אם קיים)
//        else if (rightChild != null) {
//            return getClosestEqualOrLarger(rightChild, k);
//        }
//
//        return null; // במקרה חריג (לא אמור לקרות בעץ תקין)
//    }

        public void delete(Tree<T> t, Nodes<T> x){
            if(x == root){
                x.setKey(rightSentinel);
                delete(x);
                return;
            }
            if(x==null){
                return;
            }
            Nodes<T> y = x.getPNodes();
            if ( x == y.getLeftChild())
                setChild(y, y.getMiddleChild(), y.getRightChild(), null);
            else if (x == y.getMiddleChild())
                setChild(y, y.getLeftChild(), y.getRightChild(), null);
            else
                setChild(y, y.getLeftChild(), y.getMiddleChild(), null);
            delete (x);
            while (y != null){
                if (y.getMiddleChild()!= null){
                    updateKey(y);
                    y = y.getPNodes();
                }
                else{
                    if (y != t.root){
                        y = borrowOrMarge(y);
                    }
                    else{
                        t.root = y.getLeftChild();
                        y.getLeftChild().setParent(null);
                        delete(y);
                        return;
                    }

                }
            }
        }


    public Nodes<T> borrowOrMarge (Nodes<T> y){
        Nodes<T> z = y.getPNodes();
        Nodes<T> x;
        if (y == z.getLeftChild()){
            x = z.getMiddleChild();
            if(x.getRightChild() != null){
                setChild(y,y.getLeftChild(),x.getLeftChild(),null);
                setChild(x,x.getMiddleChild(),x.getRightChild(),null);
            }
            else{
                setChild(x,y.getLeftChild(),x.getLeftChild(),x.getMiddleChild());
                delete(y);
                setChild(z, x, z.getRightChild(), null);
            }
            return z;
        }
        if(y == z.getMiddleChild()){
            x = z.getLeftChild();
            if(x.getRightChild() != null){
                setChild(y,x.getRightChild(),y.getLeftChild(),null);
                setChild(x,x.getLeftChild(),x.getMiddleChild(),null);
            }
            else{
                setChild(x,x.getLeftChild(),x.getMiddleChild(),y.getLeftChild());
                delete(y);
                setChild(z, x, z.getRightChild(), null);
            }
            return z;
        }
        x = z.getMiddleChild();
        if(x.getRightChild() != null){
            setChild(y,x.getRightChild(),y.getLeftChild(),null);
            setChild(x,x.getLeftChild(),x.getMiddleChild(),null);
        }
        else{
            setChild(x,x.getLeftChild(),x.getMiddleChild(),y.getLeftChild());
            delete(y);
            setChild(z, z.getLeftChild(), x, null);
        }
        return z;

    }  
    public Nodes<T> getRoot(){
        return root;
    }








}

