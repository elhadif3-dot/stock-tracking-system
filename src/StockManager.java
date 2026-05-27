public class StockManager {
    private Tree<StockId> stockIdTree;
    private Tree<PricesYstockId> pricesTree;

    public StockManager() {
        initStocks();
    }

    // 1. Initialize the system
    public void initStocks() {
        StockId tempStockId = new StockId("");
        PricesYstockId tempPricesYstockId = new PricesYstockId(new Prices(0.0f) , tempStockId);
        stockIdTree = new Tree<>(tempStockId.getMax(), tempStockId.getMin());
        pricesTree = new Tree<>(tempPricesYstockId.getMax(), tempPricesYstockId.getMin());

    }

    public void addStock(String stockId, long timestamp, Float price) {
        if (stockId == null || price == null || price <= 0|| timestamp < 0) {
            throw new IllegalArgumentException();
        }

        StockId id = new StockId(stockId);
        if (stockIdTree.search(stockIdTree.getRoot(), id) != null) {
            throw new IllegalArgumentException();
        }

        Stock newStock = new Stock(stockId, timestamp, price);
        Nodes<StockId> stockNode = new Nodes<>(newStock.getStockId(), true, null, newStock);
        Nodes<PricesYstockId> priceNode = new Nodes<>(new PricesYstockId(newStock.getPrice(),new StockId(stockId)), true, null, newStock);

        stockIdTree.insert(stockIdTree, stockNode);
        pricesTree.insert(pricesTree, priceNode);
    }

    // 3. Remove a stock
    public void removeStock(String stockId) {
        if (stockId == null) throw new IllegalArgumentException();

        StockId id = new StockId(stockId);
        Nodes<StockId> stockNode = stockIdTree.search(stockIdTree.getRoot(), id);
        if (stockNode == null) throw new IllegalArgumentException();

        Nodes<PricesYstockId> priceNode = pricesTree.search(pricesTree.getRoot(), new PricesYstockId(stockNode.getInnerObject().getPrice(), new StockId(stockId)));
        stockIdTree.delete(stockIdTree, stockNode);
        pricesTree.delete(pricesTree, priceNode);
    }

    public void updateStock(String stockId, long timestamp, Float priceDifference) {
        if (stockId == null || priceDifference == null || priceDifference == 0) {
            throw new IllegalArgumentException();
        }

        StockId id = new StockId(stockId);
        Nodes<StockId> stockNode = stockIdTree.search(stockIdTree.getRoot(), id);
        if (stockNode == null) throw new IllegalArgumentException();

        Stock stock = stockNode.getInnerObject();
        Nodes<PricesYstockId> oldPriceNode = pricesTree.search(pricesTree.getRoot(),new PricesYstockId(stock.getPrice(), new StockId(stockId) ));
        pricesTree.delete(pricesTree, oldPriceNode);
        stock.addEvent(timestamp, priceDifference);
        Nodes<PricesYstockId> newPriceNode = new Nodes<>(new PricesYstockId(stock.getPrice(),new StockId(stockId)), true, null, stock);
        pricesTree.insert(pricesTree, newPriceNode);
    }



    public Float getStockPrice(String stockId) {  //was edited from
        if (stockId == null) throw new IllegalArgumentException();

        StockId id = new StockId(stockId);
        Nodes<StockId> node = stockIdTree.search(stockIdTree.getRoot(), id);
        if (node == null) throw new IllegalArgumentException();

        return Math.round(node.getInnerObject().getPrice().price * 100) / 100.0f;
    }


    // 6. Remove a specific timestamp from a stock's history
    public void removeStockTimestamp(String stockId, long timestamp) {
        if (stockId  == null) throw new IllegalArgumentException();

        StockId id = new StockId(stockId);
        Nodes<StockId> stockNode = stockIdTree.search(stockIdTree.getRoot(), id);
        if (stockNode == null) throw new IllegalArgumentException();

        Stock stock = stockNode.getInnerObject();
        Nodes<PricesYstockId> oldPriceNode = pricesTree.search( pricesTree.getRoot(),new PricesYstockId(stock.getPrice(), new StockId(stockId)));
        pricesTree.delete(pricesTree, oldPriceNode);
        stock.removeEvent(timestamp);
        Nodes<PricesYstockId> newPriceNode = new Nodes<>(new PricesYstockId(stock.getPrice(),new StockId(stockId)), true, null, stock);
        pricesTree.insert(pricesTree, newPriceNode);
    }

    // 7. Get the amount of stocks in a given price range
    public int getAmountStocksInPriceRange(Float price1, Float price2) {
        if (price1 == null || price2 == null || price1 > price2) {
            throw new IllegalArgumentException();
        }
        return getStocksInPriceRange(price1, price2).length;
    }





    private int sumOfSmallerRec(Nodes<PricesYstockId> node, PricesYstockId price){
        if(node == null){
            return 0;
        }
        if (node != null && node.getIsLeaf()){
            if (node.getKey().compareTo(price) <= 0){
                return 1;
            }else {
                return 0;
            }
        }
        if(node.getLeftChild() != null &&  node.getLeftChild().getKey().compareTo(price) >= 0)
            return sumOfSmallerRec(node.getLeftChild(), price);
        else if (node.getMiddleChild() != null && node.getMiddleChild().getKey().compareTo(price)>= 0)
            return sumOfSmallerRec(node.getLeftChild(), price) + sumOfSmallerRec(node.getMiddleChild(), price);
        else
            return sumOfSmallerRec(node.getLeftChild(), price) + sumOfSmallerRec(node.getMiddleChild(), price) + sumOfSmallerRec(node.getRightChild(), price);
    }


//    public String[] getStocksInPriceRange(Float price1, Float price2) {
//        int size = getAmountStocksInPriceRange(price1, price2);
//        return getStringsInRange(price1, price2,size);
//    }




    public String[] getStringsInRange(Float lower, Float upper, int size) {

        if (size == 0) {
            return new String[0];  // אם אין צמתים בטווח
        }

        String[] result = new String[size];  // מקצה מערך בגודל הטווח
        int[] index = {0};  // אינדקס למעקב אחרי המיקום במערך

        if (lower.compareTo(upper) == 0) {
            // במקרה של מחיר זהה, נקרא לפונקציה למניות עם מחיר מדויק
            collectExactPriceStocks(pricesTree.getRoot(), lower, result, index);
        } else {
            rangeSearch(pricesTree.getRoot(), lower, upper, result, index);  // מחפש בטווח
        }
        return result;
    }

    private void collectExactPriceStocks(Nodes<PricesYstockId> node, float price, String[] result, int[] index) {
        if (node == null) return;

        if (node.getIsLeaf() && node.getKey().compareTo(node.getKey().getMax()) != 0 && node.getKey().compareTo(node.getKey().getMin()) != 0) {
            if (node.getKey().compareTo(new PricesYstockId(new Prices(price), new StockId(""))) == 0) {  // השוואת float
                result[index[0]++] = node.getInnerObject().getStockId().toString();  // אם הצומת בטווח, מוסיפים למערך
            }
            return;
        }

        if (node.getLeftChild() != null) collectExactPriceStocks(node.getLeftChild(), price, result, index);
        if (node.getMiddleChild() != null) collectExactPriceStocks(node.getMiddleChild(), price, result, index);
        if (node.getRightChild() != null) collectExactPriceStocks(node.getRightChild(), price, result, index);
    }


    private void rangeSearch(Nodes<PricesYstockId> node, float lower, float upper, String[] result, int[] index) {
        if (node == null) return;

        // מקרה 1: צומת עלה
        if (node.getIsLeaf() && node.getKey().compareTo(node.getKey().getMax()) != 0 && node.getKey().compareTo(node.getKey().getMin()) != 0) {
            float val = node.getInnerObject().getPrice().price;
            if (val >= lower && val <= upper) {
                result[index[0]++] = node.getInnerObject().getStockId().toString();  // אם הצומת בטווח, מוסיפים למערך
            }
            return;
        }

        // מקרה 2: צומת פנימי - בודק עד 3 תתי עצים
        if (node.getLeftChild() != null && node.getLeftChild().getKey().price.price >= lower) {
            rangeSearch(node.getLeftChild(), lower, upper, result, index);
        }
        if (node.getMiddleChild() != null && node.getMiddleChild().getKey().price.price >= lower) {
            rangeSearch(node.getMiddleChild(), lower, upper, result, index);
        }
        if (node.getRightChild() != null && node.getRightChild().getKey().price.price >= lower) {
            rangeSearch(node.getRightChild(), lower, upper, result, index);
        }
    }

    public String[] getStocksInPriceRange(Float price1, Float price2) {
        if (price1 == null || price2 == null || price1 > price2) {
            throw new IllegalArgumentException("Invalid price range");
        }

        StockId minStockId = new StockId("");
        PricesYstockId lowKey = new PricesYstockId(new Prices(price1), minStockId.getMin());
        PricesYstockId highKey = new PricesYstockId(new Prices(price2), minStockId.getMax());

        SimpleArray<String> stockIds = new SimpleArray<>();
        collectStocksInRange(pricesTree.getRoot(), lowKey, highKey, stockIds);

        return stockIds.safeToArray();
    }

    private void collectStocksInRange(Nodes<PricesYstockId> node, PricesYstockId low, PricesYstockId high, SimpleArray<String> result) {
        if (node == null) {
            return;
        }

        // Check if this is a leaf node
        if (node.getIsLeaf()) {
            if (node.getKey().compareTo(low) >= 0 && node.getKey().compareTo(high) <= 0) {
                if (node.getInnerObject() != null && node.getInnerObject().getStockId() != null) {
                    result.add(node.getInnerObject().getStockId().toString());
                }
            }
            return;
        }

        // Recursively check child nodes
        if (node.getLeftChild() != null && node.getLeftChild().getKey() != null) {
            collectStocksInRange(node.getLeftChild(), low, high, result);
        }

        if (node.getMiddleChild() != null && node.getMiddleChild().getKey() != null) {
            collectStocksInRange(node.getMiddleChild(), low, high, result);
        }

        if (node.getRightChild() != null && node.getRightChild().getKey() != null) {
            collectStocksInRange(node.getRightChild(), low, high, result);
        }
    }



    private class SimpleArray<T> {
        private Object[] arr;
        private int size;

        SimpleArray() {
            arr = new Object[10];
            size = 0;
        }

        void add(T item) {
            if (size == arr.length) {
                Object[] newArr = new Object[arr.length * 2];
                System.arraycopy(arr, 0, newArr, 0, arr.length);
                arr = newArr;
            }
            arr[size++] = item;
        }

        String[] safeToArray() {
            if (size == 0) {
                return new String[0];
            }

            String[] res = new String[size];
            for (int i = 0; i < size; i++) {
                if (arr[i] == null) {
                    res[i] = "UNKNOWN";
                } else {
                    res[i] = (String) arr[i];
                }
            }

            if (res.length != size) {
                throw new RuntimeException("Mismatch between expected and actual array size. Expected: " + size + ", Got: " + res.length);
            }

//            for (String stock : res) {
//                System.out.print(stock + " ");
//            }
//            System.out.println();

            return res;
        }
    }


}



