import java.util.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("Starting tests");
        System.out.println("Original test");
        int fails_o = orig();
        System.out.println("***************************************************");
        System.out.println("Test 1");
        int fails_t1 = test1();
        System.out.println("***************************************************");
        System.out.println("Test 2");
        int fails_t2 = test2();
        System.out.println("***************************************************");
        System.out.println("Test 3");
        int fails_t3 = test3();
        System.out.println("***************************************************");
        System.out.println("Test 4");
        int fails_t4 = test4();
        System.out.println("***************************************************");
        System.out.println("Test 5");
        test5();
        System.out.println("***************************************************");
        System.out.println("Test original Failed " + fails_o + " tests");
        System.out.println("Test 1 Failed " + fails_t1 + " tests");
        System.out.println("Test 2 Failed " + fails_t2 + " tests");
        System.out.println("Test 3 Failed " + fails_t3 + " tests");
        System.out.println("Test 4 Failed " + fails_t4 + " tests");
        System.out.println("for Test 5 Failed look up on the last test");

    }

    public static int orig() {
        int fails = 0;
        ArrayList<String> stockIds = new ArrayList<>();
        stockIds.add("ABCDEF");
        stockIds.add("GHIJKL");
        stockIds.add("MNOPQR");
        stockIds.add("STUVWX");
        stockIds.add("YZABCD");
        stockIds.add("EFGHIJ");
        stockIds.add("KLMNOP");
        stockIds.add("QRSTUV");
        stockIds.add("WXYZAB");
        stockIds.add("CDEFGH");

        ArrayList<Float> prices = new ArrayList<>();
        prices.add(123.45f);
        prices.add(67.89f);
        prices.add(45.67f);
        prices.add(234.56f);
        prices.add(89.01f);
        prices.add(150.34f);
        prices.add(98.76f);
        prices.add(210.45f);
        prices.add(115.99f);
        prices.add(300.12f);

        ArrayList<Long> initialTimestamps = new ArrayList<>();
        initialTimestamps.add(1708647300000L);
        initialTimestamps.add(1708650900000L);
        initialTimestamps.add(1708654500000L);
        initialTimestamps.add(1708658100000L);
        initialTimestamps.add(1708661700000L);
        initialTimestamps.add(1708665300000L);
        initialTimestamps.add(1708668900000L);
        initialTimestamps.add(1708672500000L);
        initialTimestamps.add(1708676100000L);
        initialTimestamps.add(1708679700000L);


        StockManager stockmanger = new StockManager();
        stockmanger.initStocks();
        boolean expression = false;
        for (int i = 0; i < stockIds.size(); i++) {
            stockmanger.addStock(stockIds.get(i), initialTimestamps.get(i), prices.get(i));
            expression = stockmanger.getStockPrice(stockIds.get(i)).equals(prices.get(i));
            Assert(expression);
        }

        for (int i = 0; i < 3; i++) {
            String stockIdToRemove = stockIds.remove(0);
            prices.remove(0);
            initialTimestamps.remove(0);
            stockmanger.removeStock(stockIdToRemove);
            try {
                stockmanger.getStockPrice(stockIdToRemove);
                System.out.println("Test failed: Exception was not thrown");
                fails++;
            } catch (IllegalArgumentException e) {
                System.out.println("Test passed: Exception was thrown as expected");
            } catch (Exception e) {
                System.out.println("Test failed: An unexpected exception was thrown");
                fails++;
            }
            Assert(expression);
            System.out.println("Removed Stock: " + stockIdToRemove);

        }
        Map<String, ArrayList<Map.Entry<Long, Float>>> additionalData = new HashMap<>();

        // Adding additional timestamps and price changes based on original timestamps
        ArrayList<Map.Entry<Long, Float>> data1 = new ArrayList<>();
        data1.add(Map.entry(initialTimestamps.get(0) + 60000L, 5.67f)); // Add 1 minute
        data1.add(Map.entry(initialTimestamps.get(0) + 120000L, -3.21f)); // Add 2 minutes
        additionalData.put("STUVWX", data1);

        ArrayList<Map.Entry<Long, Float>> data2 = new ArrayList<>();
        data2.add(Map.entry(initialTimestamps.get(1) + 300000L, 7.89f)); // Add 5 minutes
        additionalData.put("YZABCD", data2);

        ArrayList<Map.Entry<Long, Float>> data3 = new ArrayList<>();
        data3.add(Map.entry(initialTimestamps.get(2) + 60000L, -2.34f)); // Add 1 minute
        data3.add(Map.entry(initialTimestamps.get(2) + 180000L, 4.56f)); // Add 3 minutes
        data3.add(Map.entry(initialTimestamps.get(2) + 300000L, -1.78f)); // Add 5 minutes
        additionalData.put("EFGHIJ", data3);

        for (int i = 0; i < 3; i++) {
            String StockId = stockIds.get(i);
            ArrayList<Map.Entry<Long, Float>> data = additionalData.get(StockId);
            for (Map.Entry<Long, Float> entry : data) {
                stockmanger.updateStock(StockId, entry.getKey(), entry.getValue());
            }

        }
        expression = Math.round(stockmanger.getStockPrice("STUVWX") * 100) / 100.0f == 237.02f;
        if (!expression) {
            fails++;
        }
        Assert(expression);
        expression = Math.round(stockmanger.getStockPrice("YZABCD") * 100) / 100.0f == 96.9f;
        if (!expression) {
            fails++;
        }
        Assert(expression);
        expression = Math.round(stockmanger.getStockPrice("EFGHIJ") * 100) / 100.0f == 150.78f;
        if (!expression) {
            fails++;
        }
        Assert(expression);


        for (String stockId : stockIds) {
            double price = stockmanger.getStockPrice(stockId);
            System.out.println("Stock ID: " + stockId + ", Current Price: " + price);
        }
        if (additionalData.containsKey("STUVWX")) {
            ArrayList<Map.Entry<Long, Float>> stockData = additionalData.get("STUVWX");
            stockmanger.removeStockTimestamp("STUVWX", stockData.get(1).getKey());
            stockData.remove(1);
        }
        if (additionalData.containsKey("EFGHIJ")) {
            ArrayList<Map.Entry<Long, Float>> stockData = additionalData.get("EFGHIJ");
            stockmanger.removeStockTimestamp("EFGHIJ", stockData.get(2).getKey());
            stockmanger.removeStockTimestamp("EFGHIJ", stockData.get(0).getKey());
        }
        for (String stockId : stockIds) {
            double price = stockmanger.getStockPrice(stockId);
            System.out.println("Stock ID: " + stockId + ", Current Price: " + price);
        }
        expression = stockmanger.getStockPrice("STUVWX") == 240.23f;
        if (!expression) {
            fails++;
        }
        Assert(expression);
        expression = stockmanger.getStockPrice("EFGHIJ") == 154.9f;
        if (!expression) {
            fails++;
        }
        Assert(expression);
        Float price1 = 10f;
        Float price2 = 30f;
        int stockamount = stockmanger.getAmountStocksInPriceRange(price1, price2);
        expression = stockamount == 0;
        if (!expression) {
            fails++;
        }
        Assert(expression);
        System.out.println(stockmanger.getStocksInPriceRange(price1, price2).length);
        price1 = 50f;
        price2 = 170f;
        stockamount = stockmanger.getAmountStocksInPriceRange(price1, price2);
        expression = stockamount == 4;
        if (!expression) {
            fails++;
        }
        Assert(expression);
        System.out.println(stockamount);
        String[] stocksInRange = stockmanger.getStocksInPriceRange(price1, price2);
        expression = stocksInRange[0] == "YZABCD";
        if (!expression) {
            fails++;
        }
        Assert(expression);
        expression = stocksInRange[1] == "KLMNOP";
        if (!expression) {
            fails++;
        }
        Assert(expression);
        expression = stocksInRange[2] == "WXYZAB";
        if (!expression) {
            fails++;
        }
        Assert(expression);
        expression = stocksInRange[3] == "EFGHIJ";
        if (!expression) {
            fails++;
        }
        Assert(expression);
        for (String stock : stocksInRange) {
            System.out.println(stock);
        }
        return fails;
    }


    public static int test1(){
        int fails = 0;
        // Test 1 - Add 10 stocks with initial prices and timestamps - all prices are equal
        ArrayList<String> stockIds = new ArrayList<>();
        stockIds.add("ABCDEF");
        stockIds.add("GHIJKL");
        stockIds.add("MNOPQR");
        stockIds.add("STUVWX");
        stockIds.add("YZABCD");
        stockIds.add("EFGHIJ");
        stockIds.add("KLMNOP");
        stockIds.add("QRSTUV");
        stockIds.add("WXYZAB");
        stockIds.add("CDEFGH");

        //all prices are equal
        ArrayList<Float> prices = new ArrayList<>();
        prices.add(123.45f);
        prices.add(123.45f);
        prices.add(123.45f);
        prices.add(123.45f);
        prices.add(123.45f);
        prices.add(123.45f);
        prices.add(123.45f);
        prices.add(123.45f);
        prices.add(123.45f);
        prices.add(123.45f);

        ArrayList<Long> initialTimestamps = new ArrayList<>();
        initialTimestamps.add(1708647300000L);
        initialTimestamps.add(1708650900000L);
        initialTimestamps.add(1708654500000L);
        initialTimestamps.add(1708658100000L);
        initialTimestamps.add(1708661700000L);
        initialTimestamps.add(1708665300000L);
        initialTimestamps.add(1708668900000L);
        initialTimestamps.add(1708672500000L);
        initialTimestamps.add(1708676100000L);
        initialTimestamps.add(1708679700000L);

        StockManager stockmanger = new StockManager();
        stockmanger.initStocks();
        boolean expression = false;
        for (int i = 0; i < stockIds.size(); i++) {
            System.out.println("Adding Stock: " + stockIds.get(i) + " with price: " + prices.get(i));
            if (stockIds.get(i).equals("MNOPQR")){
                System.out.println();
            }
            stockmanger.addStock(stockIds.get(i), initialTimestamps.get(i), prices.get(i));
            expression = stockmanger.getStockPrice(stockIds.get(i)).equals(prices.get(i));
            if (!expression) {
                fails++;
            }
            Assert(expression);
        }

        // Test 2 - count the amount of stocks in a price range
        float price1 = 10f;
        float price2 = 30f;
        int stockamount = stockmanger.getAmountStocksInPriceRange(price1, price2);
        expression =  stockamount == 0;
        if (!expression) {
            fails++;
        }
        Assert(expression);

        price1 = 50f;
        price2 = 170f;
        stockamount = stockmanger.getAmountStocksInPriceRange(price1, price2);
        expression = stockamount == 10;
        if (!expression) {
            fails++;
        }
        Assert(expression);

        // Test 3 - get the stocks in a price range
        String[] stocksInRange = stockmanger.getStocksInPriceRange(price1, price2);
        expression = stocksInRange.length == 10;
        if (!expression) {
            fails++;
        }
        Assert(expression);
        for (String stock : stocksInRange) {
            System.out.println(stock);
        }

        // Test 4 - remove 3 stocks
        for (int i = 0; i < 3; i++) {
            String stockIdToRemove = stockIds.remove(0);
            prices.remove(0);
            initialTimestamps.remove(0);
            System.out.println("Removed Stock: " + stockIdToRemove);
            stockmanger.removeStock(stockIdToRemove);
            //System.out.println("Stock Tree by price");
            //stockmanger.priceStockTree.printTree();
            //System.out.println("Stock Tree by ID");
            //stockmanger.stockIDTree.printTree();
            try{
                stockmanger.getStockPrice(stockIdToRemove) ;
                System.out.println("Test failed: Exception was not thrown");
                fails++;
            }
            catch (IllegalArgumentException e) {
                System.out.println("Test passed: Exception was thrown as expected");
            }
            catch (Exception e) {
                System.out.println("Test failed: An unexpected exception was thrown");
                fails++;
            }
            Assert(expression);
        }

        // Test 5 - count the amount of stocks in a price range
        price1 = 10f;
        price2 = 30f;
        stockamount = stockmanger.getAmountStocksInPriceRange(price1, price2);
        expression =  stockamount == 0;
        if (!expression) {
            fails++;
        }
        Assert(expression);

        price1 = 50f;
        price2 = 170f;
        //stockmanger.priceStockTree.printTree();
        stockamount = stockmanger.getAmountStocksInPriceRange(price1, price2);
        expression = stockamount == 7;
        if (!expression) {
            fails++;
        }
        Assert(expression);

        // Test 6 - get the stocks in a price range
        stocksInRange = stockmanger.getStocksInPriceRange(price1, price2);
        expression = stocksInRange.length == 7;
        if (!expression) {
            fails++;
        }
        Assert(expression);
        System.out.println("Stocks between 50 and 170");
        for (String stock : stocksInRange) {
            System.out.println(stock);
        }

        // find the stocks between 123.45 and 123.45
        price1 = 123.45f;
        price2 = 123.45f;
        stockamount = stockmanger.getAmountStocksInPriceRange(price1, price2);
        expression = stockamount == 7;
        if (!expression) {
            fails++;
        }
        Assert(expression);
        stocksInRange = stockmanger.getStocksInPriceRange(price1, price2);
        expression = stocksInRange.length == 7;
        if (!expression) {
            fails++;
        }
        Assert(expression);
        System.out.println("Stocks between 123.45 and 123.45");
        for (String stock : stocksInRange) {
            System.out.println(stock);
        }

        // change the price of one of the stocks
        stockmanger.updateStock("CDEFGH", 1708650900000L + 60000L, 50.0f);
        //System.out.println("Stock Tree by price");
        //stockmanger.priceStockTree.printTree();
        //System.out.println("Stock Tree by ID");
        //stockmanger.stockIDTree.printTree();
        price1 = 50f;
        price2 = 170f;
        stockamount = stockmanger.getAmountStocksInPriceRange(price1, price2);
        expression = stockamount == 6;
        if (!expression) {
            fails++;
        }
        Assert(expression);
        stocksInRange = stockmanger.getStocksInPriceRange(price1, price2);
        expression = stocksInRange.length == 6;
        if (!expression) {
            fails++;
        }
        Assert(expression);
        System.out.println("Stocks between 50 and 170");
        for (String stock : stocksInRange) {
            System.out.println(stock);
        }
        price1 = 123.45f;
        price2 = 123.45f;
        stockamount = stockmanger.getAmountStocksInPriceRange(price1, price2);
        expression = stockamount == 6;
        if (!expression) {
            fails++;
        }
        Assert(expression);
        stocksInRange = stockmanger.getStocksInPriceRange(price1, price2);
        expression = stocksInRange.length == 6;
        if (!expression) {
            fails++;
        }
        Assert(expression);
        System.out.println("Stocks between 123.45 and 123.45");
        for (String stock : stocksInRange) {
            System.out.println(stock);
        }
        price1 = 50f;
        price2 = 174f;
        stockamount = stockmanger.getAmountStocksInPriceRange(price1, price2);
        expression = stockamount == 7;
        if (!expression) {
            fails++;
        }
        Assert(expression);
        stocksInRange = stockmanger.getStocksInPriceRange(price1, price2);
        expression = stocksInRange.length == 7;
        if (!expression) {
            fails++;
        }
        Assert(expression);
        System.out.println("Stocks between 50 and 170");
        for (String stock : stocksInRange) {
            System.out.println(stock);
        }

    return fails;

    }


    public static int test2(){
        int fails = 0;
        ArrayList<String> stockIds = new ArrayList<>();
        stockIds.add("ABCDEF");
        stockIds.add("GHIJKL");
        stockIds.add("MNOPQR");
        stockIds.add("STUVWX");
        stockIds.add("");
        stockIds.add("oFF");
        stockIds.add("dfhjdhf");
        stockIds.add(null);

        ArrayList<Float> prices = new ArrayList<>();
        prices.add(123.45f);
        prices.add(67.89f);
        prices.add(45.67f);
        prices.add(234.56f);
        prices.add(123.45f);
        prices.add(Float.MAX_VALUE);
        prices.add(Float.MIN_VALUE);
        prices.add(123.45f);


        ArrayList<Long> initialTimestamps = new ArrayList<>();
        initialTimestamps.add(1708647300000L);
        initialTimestamps.add(1708650900000L);
        initialTimestamps.add(1708654500000L);
        initialTimestamps.add(1708658100000L);
        initialTimestamps.add(1708647300000L);
        initialTimestamps.add(Long.MAX_VALUE);
        initialTimestamps.add(Long.MIN_VALUE);
        initialTimestamps.add(1708647300000L);


        StockManager stockmanger = new StockManager();
        stockmanger.initStocks();
        boolean expression = false;
        for (int i = 0; i < stockIds.size(); i++) {
            if (stockIds.get(i) == null || initialTimestamps.get(i) < 0) {
                if(stockIds.get(i) == null){
                    System.out.println("Adding Stock with null ID");
                }
                else{
                    System.out.println("Adding Stock with negative timestamp");
                }
                try {
                    stockmanger.addStock(stockIds.get(i), initialTimestamps.get(i), prices.get(i));
                    System.out.println("Test failed: Exception was not thrown");
                    fails++;
                } catch (IllegalArgumentException e) {
                    System.out.println("Test passed: Exception was thrown as expected");
                } catch (Exception e) {
                    System.out.println("Test failed: An unexpected exception was thrown");
                    fails++;
                }
            }
            else{
            stockmanger.addStock(stockIds.get(i), initialTimestamps.get(i), prices.get(i));}
        }
        for (String stockId : stockIds) {
            double price;
            if (stockId == null || stockId.equals("dfhjdhf")) {
                try {
                    price = stockmanger.getStockPrice(stockId);
                    System.out.println("Test failed: Exception was not thrown");
                    fails++;
                } catch (IllegalArgumentException e) {
                    System.out.println("Test passed: Exception was thrown as expected");
                } catch (Exception e) {
                    System.out.println("Test failed: An unexpected exception was thrown");
                    fails++;
                }
            } else {
                price = stockmanger.getStockPrice(stockId);
                System.out.println("Stock ID: " + stockId + ", Current Price: " + price);
            }


        }
        //System.out.println("Stock Tree by price");
        //stockmanger.priceStockTree.printTree();
        //System.out.println("Stock Tree by ID");
        //stockmanger.stockIDTree.printTree();
        return fails;
    }


    public static int test3(){
        int fails = 0;
        // add 10 stocks with initial prices and timestamps
        ArrayList<String> stockIds = new ArrayList<>();
        stockIds.add("ABCDEF");
        stockIds.add("AHIJKL");
        stockIds.add("MNOPQR");
        stockIds.add("STUVWX");
        stockIds.add("YZABCD");
        stockIds.add("EFGHIJ");
        stockIds.add("KLMNOP");
        stockIds.add("QRSTUV");
        stockIds.add("WXYZAB");
        stockIds.add("CDEFGH");

        ArrayList<Float> prices = new ArrayList<>();
        prices.add(123.45f);
        prices.add(67.89f);
        prices.add(45.67f);
        prices.add(234.56f);
        prices.add(89.01f);
        prices.add(150.34f);
        prices.add(98.76f);
        prices.add(210.45f);
        prices.add(115.99f);
        prices.add(300.12f);

        ArrayList<Long> initialTimestamps = new ArrayList<>();
        initialTimestamps.add(1708647300000L);
        initialTimestamps.add(1708650900000L);
        initialTimestamps.add(1708654500000L);
        initialTimestamps.add(1708658100000L);
        initialTimestamps.add(1708661700000L);
        initialTimestamps.add(1708665300000L);
        initialTimestamps.add(1708668900000L);
        initialTimestamps.add(1708672500000L);
        initialTimestamps.add(1708676100000L);
        initialTimestamps.add(1708679700000L);

        StockManager stockmanger = new StockManager();
        stockmanger.initStocks();
        boolean expression = false;
        for (int i = 0; i < stockIds.size(); i++) {
            stockmanger.addStock(stockIds.get(i), initialTimestamps.get(i), prices.get(i));
            expression = stockmanger.getStockPrice(stockIds.get(i)).equals(prices.get(i));
            if (!expression) {
                fails++;
            }
            Assert(expression);
        }

        //System.out.println("Stock Tree by price");
        //stockmanger.priceStockTree.printTree();
        //System.out.println("Stock Tree by ID");
        //stockmanger.stockIDTree.printTree();
        //System.out.println("************************");

        // update the price of a stock 5 times
        stockmanger.updateStock("ABCDEF", 1708647300000L + 60000L, 50.0f);
        stockmanger.updateStock("ABCDEF", 1708647300000L + 120000L, 60.0f);
        stockmanger.updateStock("ABCDEF", 1708647300000L + 180000L, -70.0f);
        stockmanger.updateStock("ABCDEF", 1708647300000L + 240000L, 80.0f);
        stockmanger.updateStock("ABCDEF", 1708647300000L + 300000L, 90.0f);
        expression = Math.round(stockmanger.getStockPrice("ABCDEF")*100)/100.0f == 333.45f;
        if (!expression) {
            fails++;
        }
        Assert(expression);
        System.out.println("Stock ID: ABCDEF, Current Price: " + stockmanger.getStockPrice("ABCDEF"));
        //System.out.println("Stock Tree by price");
        //stockmanger.priceStockTree.printTree();
        //System.out.println("Stock Tree by ID");
        //stockmanger.stockIDTree.printTree();

        System.out.println("************************");


        //remove update from stock
        System.out.println("************************");
        stockmanger.removeStockTimestamp("ABCDEF", 1708647300000L + 120000L);
        expression = Math.round(stockmanger.getStockPrice("ABCDEF")*100)/100.0f == 273.45f;
        if (!expression) {
            fails++;
        }
        Assert(expression);
        System.out.println("Stock ID: ABCDEF, Current Price: " + stockmanger.getStockPrice("ABCDEF"));
        //System.out.println("Stock Tree by price");
        //stockmanger.priceStockTree.printTree();
        //System.out.println("Stock Tree by ID");
        //stockmanger.stockIDTree.printTree();

        System.out.println("************************");
        stockmanger.removeStockTimestamp("ABCDEF", 1708647300000L + 240000L);
        expression = Math.round(stockmanger.getStockPrice("ABCDEF")*100)/100.0f == 193.45f;
        if (!expression) {
            fails++;
        }
        Assert(expression);
        System.out.println("Stock ID: ABCDEF, Current Price: " + stockmanger.getStockPrice("ABCDEF"));
        //System.out.println("Stock Tree by price");
        //stockmanger.priceStockTree.printTree();
        //System.out.println("Stock Tree by ID");
        //stockmanger.stockIDTree.printTree();

        System.out.println("************************");
        stockmanger.removeStockTimestamp("ABCDEF", 1708647300000L + 180000L);
        expression = Math.round(stockmanger.getStockPrice("ABCDEF")*100)/100.0f == 263.45f;
        if (!expression) {
            fails++;
        }
        Assert(expression);
        System.out.println("Stock ID: ABCDEF, Current Price: " + stockmanger.getStockPrice("ABCDEF"));
        //System.out.println("Stock Tree by price");
        //stockmanger.priceStockTree.printTree();
        //System.out.println("Stock Tree by ID");
        //stockmanger.stockIDTree.printTree();
        return fails;

    }

    public static int test4() {
        int fails = 0;
        ArrayList<String> stockIds = new ArrayList<>();
        stockIds.add("A");
        stockIds.add("B");
        stockIds.add("C");
        stockIds.add("D");
        stockIds.add("E");
        stockIds.add("F");
        stockIds.add("G");
        stockIds.add("H");
        stockIds.add("I");
        stockIds.add("J");

        ArrayList<Float> prices = new ArrayList<>();
        prices.add(123.45f);
        prices.add(67.89f);
        prices.add(45.67f);
        prices.add(234.56f);
        prices.add(89.01f);
        prices.add(150.34f);
        prices.add(98.76f);
        prices.add(210.45f);
        prices.add(115.99f);
        prices.add(300.12f);

        ArrayList<Long> initialTimestamps = new ArrayList<>();
        initialTimestamps.add(1708647300000L);
        initialTimestamps.add(1708650900000L);
        initialTimestamps.add(1708654500000L);
        initialTimestamps.add(1708658100000L);
        initialTimestamps.add(1708661700000L);
        initialTimestamps.add(1708665300000L);
        initialTimestamps.add(1708668900000L);
        initialTimestamps.add(1708672500000L);
        initialTimestamps.add(1708676100000L);
        initialTimestamps.add(1708679700000L);


        StockManager stockManager = new StockManager();
        // Test 1: Initialize stocks
        try {
            stockManager.initStocks();
            System.out.println("Test passed: initStocks");
        } catch (Exception e) {
            System.out.println("Test failed: initStocks");
            fails++;
        }


        boolean expression = false;
        for (int i = 0; i < stockIds.size(); i++) {
            stockManager.addStock(stockIds.get(i), initialTimestamps.get(i), prices.get(i));
            expression = stockManager.getStockPrice(stockIds.get(i)).equals(prices.get(i));
            if (!expression) {
                fails++;
            }
            Assert(expression);
        }
        System.out.println("Test passed: Add valid stock");

        // Test 3: Add stock with invalid Input
        try {
            stockManager.addStock("I", 1001L, 60.0f);
            System.out.println("Test failed: Add stock with invalid ID (should throw exception)");
            fails++;
        } catch (IllegalArgumentException e) {
            System.out.println("Test passed: Add stock with invalid ID");
        }
        try {
            stockManager.addStock("z", 1001L, 0.0f);
            System.out.println("Test failed: Add stock with invalid price (should throw exception)");
            fails++;
        } catch (IllegalArgumentException e) {
            System.out.println("Test passed: Add stock with invalid price");
        }

        for (int i = 0; i < 3; i++) {
            String stockIdToRemove = stockIds.remove(0);
            prices.remove(0);
            initialTimestamps.remove(0);
            stockManager.removeStock(stockIdToRemove);
            try {
                stockManager.getStockPrice(stockIdToRemove);
                System.out.println("Test failed: Exception was not thrown");
                fails++;
            } catch (IllegalArgumentException e) {
                System.out.println("Test passed: Exception was thrown as expected");
            } catch (Exception e) {
                System.out.println("Test failed: An unexpected exception was thrown");
                fails++;
            }
            Assert(expression);
            System.out.println("Removed Stock: " + stockIdToRemove);

        }
        System.out.println("Test passed: remove stock with valid ID");

        // Test 5: Remove non-existent stock
        try {
            stockManager.removeStock("NONEXISTENT");
            System.out.println("Test failed: Remove non-existent stock (should throw exception)");
            fails++;
        } catch (IllegalArgumentException e) {
            System.out.println("Test passed: Remove non-existent stock");
        }


        Map<String, ArrayList<Map.Entry<Long, Float>>> additionalData = new HashMap<>();

        // Adding additional timestamps and price changes based on original timestamps
        ArrayList<Map.Entry<Long, Float>> data1 = new ArrayList<>();
        data1.add(Map.entry(initialTimestamps.get(0) + 60000L, 5.67f)); // Add 1 minute
        data1.add(Map.entry(initialTimestamps.get(0) + 120000L, -3.21f)); // Add 2 minutes
        additionalData.put("D", data1);

        ArrayList<Map.Entry<Long, Float>> data2 = new ArrayList<>();
        data2.add(Map.entry(initialTimestamps.get(1) + 300000L, 7.89f)); // Add 5 minutes
        additionalData.put("E", data2);

        ArrayList<Map.Entry<Long, Float>> data3 = new ArrayList<>();
        data3.add(Map.entry(initialTimestamps.get(2) + 60000L, -2.34f)); // Add 1 minute
        data3.add(Map.entry(initialTimestamps.get(2) + 180000L, 4.56f)); // Add 3 minutes
        data3.add(Map.entry(initialTimestamps.get(2) + 300000L, -1.78f)); // Add 5 minutes
        additionalData.put("F", data3);

        // Test : Update stock price
        for (int i = 0; i < 3; i++) {
            String StockId = stockIds.get(i);
            ArrayList<Map.Entry<Long, Float>> data = additionalData.get(StockId);
            for (Map.Entry<Long, Float> entry : data) {
                stockManager.updateStock(StockId, entry.getKey(), entry.getValue());
            }

        }
        try{
            compareFloats(stockManager.getStockPrice("D"), 237.02f);
        }
        catch (Exception e){
            System.out.println("Test failed: Update stock price");
            fails++;
        }
        try {compareFloats(stockManager.getStockPrice("E"), 96.9f);
        }
        catch (Exception e){
            System.out.println("Test failed: Update stock price");
            fails++;
        }
        try{compareFloats(stockManager.getStockPrice("F"), 150.78f);
        }
        catch (Exception e){
            System.out.println("Test failed: Update stock price");
            fails++;
        }
        System.out.println("Test passed: Update stock price");

        // Test 5: update stock with 0 diff
        try {
            stockManager.updateStock("D", 1001L, 0.0f);
            System.out.println("Test failed: update stock with 0 diff (should throw exception)");
            fails++;
        } catch (IllegalArgumentException e) {
            System.out.println("Test passed: update stock with 0 diff");
        }


        for (String stockId : stockIds) {
            double price = stockManager.getStockPrice(stockId);
            System.out.println("Stock ID: " + stockId + ", Current Price: " + price);
        }


        System.out.println("next test");
        if (additionalData.containsKey("D")) {
            ArrayList<Map.Entry<Long, Float>> stockData = additionalData.get("D");
            stockManager.removeStockTimestamp("D", stockData.get(1).getKey());
            stockData.remove(1);
        }
        if (additionalData.containsKey("F")) {
            ArrayList<Map.Entry<Long, Float>> stockData = additionalData.get("F");
            stockManager.removeStockTimestamp("F", stockData.get(2).getKey());
            stockManager.removeStockTimestamp("F", stockData.get(0).getKey());
        }
        for (String stockId : stockIds) {
            double price = stockManager.getStockPrice(stockId);
            System.out.println("Stock ID: " + stockId + ", Current Price: " + price);
        }
        try{
            compareFloats(stockManager.getStockPrice("D"), 240.23f);
        }
        catch (Exception e){
            System.out.println("Test failed: Remove valid stock timestamp");
            fails++;
        }
        try{compareFloats(stockManager.getStockPrice("F"), 154.9f);
        }
        catch (Exception e){
            System.out.println("Test failed: Remove valid stock timestamp");
            fails++;
        }
        System.out.println("Test passed: Remove valid stock timestamp");

        // Test 7: Remove invalid timestamp
        try {
            stockManager.removeStockTimestamp("J", 3000L);
            float price = stockManager.getStockPrice("J");
            if (price == 300.119995f) {
                System.out.println("Test failed: Remove invalid stock timestamp");
                fails++;
            }
        } catch (Exception e) {
            System.out.println("Test passed: Remove invalid stock timestamp");
        }

        // Test :  get AmountStocks In valid PriceRange
        Float price1 = 10f;
        Float price2 = 30f;
        int stockamount = stockManager.getAmountStocksInPriceRange(price1, price2);
        expression = stockamount == 0;
        if(!expression){
            fails++;
        }
        Assert(expression);
        System.out.println("Test passed: get AmountStocks In valid PriceRange");

        // Test :  get AmountStocks In invalid PriceRange
        try {
            Float price4 = 50f;
            Float price5 = 30f;
            int stockamount1 = stockManager.getAmountStocksInPriceRange(price4, price5);
            System.out.println("Test failed: get AmountStocks In invalid PriceRange");
            fails++;
        } catch (Exception e) {
            System.out.println("Test passed: get AmountStocks In invalid PriceRange");
        }

        System.out.println(stockManager.getStocksInPriceRange(price1, price2));
        price1 = 50f;
        price2 = 170f;
        stockamount = stockManager.getAmountStocksInPriceRange(price1, price2);
        expression = stockamount == 4;
        if(!expression){
            fails++;
        }
        Assert(expression);
        System.out.println(stockamount);
        String[] stocksInRange = stockManager.getStocksInPriceRange(price1, price2);
        expression = stocksInRange[0] == "E";
        if(!expression){
            fails++;
        }
        Assert(expression);
        expression = stocksInRange[1] == "G";
        if(!expression){
            fails++;
        }
        Assert(expression);
        expression = stocksInRange[2] == "I";
        if(!expression){
            fails++;
        }
        Assert(expression);
        expression = stocksInRange[3] == "F";
        if(!expression){
            fails++;
        }
        Assert(expression);
        for (String stock : stocksInRange) {
            System.out.println(stock);
        }
        System.out.println("Test passed: Get stocks in price range ");
        System.out.println("---------------- tests on stock with same price --------------------------- ");
        stockManager.initStocks();
        ArrayList<String> stockIdsNew = new ArrayList<>();
        stockIdsNew.add("A");
        stockIdsNew.add("B");
        stockIdsNew.add("C");
        stockIdsNew.add("D");
        stockIdsNew.add("E");
        stockIdsNew.add("F");
        stockIdsNew.add("G");
        stockIdsNew.add("H");
        stockIdsNew.add("I");

        ArrayList<Float> pricesNew = new ArrayList<>();
        pricesNew.add(123.f);
        pricesNew.add(67.89f);
        pricesNew.add(67.89f);
        pricesNew.add(67.89f);
        pricesNew.add(150.34f);
        pricesNew.add(67.89f);
        pricesNew.add(98.76f);
        pricesNew.add(210.45f);
        pricesNew.add(115.99f);

        ArrayList<Long> initialTimestampsNew = new ArrayList<>();
        initialTimestampsNew.add(1708647300000L);
        initialTimestampsNew.add(1708650900000L);
        initialTimestampsNew.add(1708654500000L);
        initialTimestampsNew.add(1708658100000L);
        initialTimestampsNew.add(1708661700000L);
        initialTimestampsNew.add(1708665300000L);
        initialTimestampsNew.add(1708668900000L);
        initialTimestampsNew.add(1708672500000L);
        initialTimestampsNew.add(1708676100000L);

        expression = false;
        for (int i = 0; i < stockIdsNew.size(); i++) {
            stockManager.addStock(stockIdsNew.get(i), initialTimestampsNew.get(i), pricesNew.get(i));
            expression = stockManager.getStockPrice(stockIdsNew.get(i)).equals(pricesNew.get(i));
            if (!expression) {
                fails++;
            }
            Assert(expression);
        }

        // Test :  get AmountStocks In valid PriceRange
        price1 = 67.89f;
        price2 = 67.89f;
        stockamount = stockManager.getAmountStocksInPriceRange(price1, price2);
        expression = stockamount == 4;
        if(!expression){
            fails++;
        }
        Assert(expression);
        System.out.println("Test passed: get AmountStocks In valid PriceRange");

        // Test :  get Stocks id In valid PriceRange
        System.out.println("stockamount =" + stockamount );
        stocksInRange = stockManager.getStocksInPriceRange(price1, price2);
        expression = stocksInRange[0] == "B";
        if(!expression){
            fails++;
        }
        Assert(expression);
        expression = stocksInRange[1] == "C";
        if(!expression){
            fails++;
        }
        Assert(expression);
        expression = stocksInRange[2] == "D";
        if(!expression){
            fails++;
        }
        Assert(expression);
        expression = stocksInRange[3] == "F";
        if(!expression){
            fails++;
        }
        Assert(expression);
        for (String stock : stocksInRange) {
            System.out.println(stock);
        }
        System.out.println("Test passed: Get stocks in price range ");

        // Test :  delete init event
        try {
            stockManager.removeStockTimestamp("A",1708647300000L);
            System.out.println("Test failed: delete initialization event");
            fails++;
        } catch (Exception e) {
            System.out.println("Test passed: delete initialization event");
        }
        return fails;

    }

    public static void Assert(boolean expression){
        if (!expression){
            throw new AssertionError();
        }

    }
    public static void compareFloats(Float num1, Float num2) {
        final float epsilon = 1e-2f;

        if (Math.abs(num1 - num2) > epsilon) {
            throw new AssertionError();
        }
    }

    public static void test5() {

        StockManager manager = new StockManager();

        // 1. initStocks() on an already-new StockManager (should be empty; no error).
        manager.initStocks();
        println("Test 1: initStocks on new manager => OK (no crash).");

        // 2. Attempt to remove a non-existent stock => expect exception
        testRemoveNonExistent(manager, "FAKE_ID");

        // 3. Try to get price of a non-existent stock => expect exception
        testGetPriceNonExistent(manager, "FAKE_ID");

        // 4. Add a stock with invalid (non-positive) price => expect exception
        testAddStockInvalidPrice(manager, "AAA", 1000000L, 0f);
        testAddStockInvalidPrice(manager, "BBB", 1000001L, -5f);

        // 5. Add a valid stock
        manager.addStock("STK1", 1000000L, 50f);
        println("Test 5: added STK1 with price=50 => OK");

        // 6. Add same stock again => expect duplicate exception
        testAddDuplicateStock(manager, "STK1", 1000002L, 60f);

        // 7. Check getStockPrice => should be 50
        float priceStk1 = manager.getStockPrice("STK1");
        myAssert(priceStk1 == 50f, "STK1 price must be 50, got: " + priceStk1);
        println("Test 7: getStockPrice(STK1) => 50 => OK");

        // 8. Update stock with priceDifference=0 => expect exception
        testUpdateWithZeroDiff(manager, "STK1", 1000003L);

        // 9. Valid update => e.g. +20
        manager.updateStock("STK1", 1000004L, 20f);
        myAssert(manager.getStockPrice("STK1") == 70f, "Expected STK1 price=70");
        println("Test 9: updated STK1 with +20 => price=70 => OK");

        // 10. Another update => e.g. -10
        manager.updateStock("STK1", 1000005L, -10f);
        myAssert(manager.getStockPrice("STK1") == 60f, "Expected STK1 price=60");
        println("Test 10: updated STK1 with -10 => price=60 => OK");

        // 11. Remove invalid timestamp => expect exception
        testRemoveTimestamp(manager, "STK1", 9999999L, false);

        // 12. Remove a real timestamp => e.g. remove the +20 (1000004L)
        testRemoveTimestamp(manager, "STK1", 1000004L, true);
        myAssert(manager.getStockPrice("STK1") == 40f, "After removing +20, price should be 40 (50 initial -10 update).");
        println("Test 12: removed update with timestamp=1000004 => price=40 => OK");

        // 13. Let's add more stocks
        manager.addStock("STK2", 1000000L, 10f);
        manager.addStock("STK3", 1000000L, 100f);
        manager.addStock("STK4", 1000000L, 40f);

        // Now STK2=10, STK3=100, STK4=40, STK1=40
        // 14. Range query => check price1>price2 => expect exception
        testRangeException(manager, 100f, 50f);

        // 15. Normal range query => e.g. [0..50]
        // STK2=10, STK1=40, STK4=40 => in range => 3 stocks; STK3=100 => out
        int count = manager.getAmountStocksInPriceRange(0f, 50f);
        myAssert(count == 3, "Expected 3 in [0..50], got: " + count);
        println("Test 15: getAmountStocksInPriceRange(0..50) => 3 => OK");

        // 16. getStocksInPriceRange => see if we get STK1, STK2, STK4
        // They should be sorted by price ascending. If there's a tie, sort by stockId ascending.
        // STK2=10, STK1=40, STK4=40 => STK1 < STK4 in alphabetical order?
        // Actually, "STK1".compareTo("STK4") => negative => STK1 < STK4
        // So expected order => STK2, STK1, STK4
        String[] arr = manager.getStocksInPriceRange(0f, 50f);
        myAssert(arr.length == 3, "Expected array length=3, got:" + arr.length);
        myAssert(arr[0].equals("STK2"), "arr[0]=STK2?");
        myAssert(arr[1].equals("STK1"), "arr[1]=STK1?");
        myAssert(arr[2].equals("STK4"), "arr[2]=STK4?");
        println("Test 16: getStocksInPriceRange(0..50) => [STK2, STK1, STK4] => OK");

        // 17. Remove stock => check it disappears from range queries
        manager.removeStock("STK2");
        myAssertThrown(() -> manager.getStockPrice("STK2"), "Expected exception for getStockPrice(STK2) after removal");
        // Now range [0..50] => STK2 gone => STK1(40), STK4(40)
        count = manager.getAmountStocksInPriceRange(0f, 50f);
        myAssert(count == 2, "Expected 2 in range after removing STK2");
        println("Test 17: removeStock(STK2) => OK, not in range => OK");

        println("=== All end-case tests PASSED successfully! ===");
    }

    // --------------------- Helpers ---------------------

    private static void testRemoveNonExistent(StockManager manager, String stockId) {
        try {
            manager.removeStock(stockId);
            fail("Expected exception removing non-existent stock: " + stockId);
        } catch (IllegalArgumentException e) {
            println("Test 2: removeStock(non-existent) => threw exception => OK");
        }
    }

    private static void testGetPriceNonExistent(StockManager manager, String stockId) {
        try {
            manager.getStockPrice(stockId);
            fail("Expected exception getStockPrice(non-existent): " + stockId);
        } catch (IllegalArgumentException e) {
            println("Test 3: getStockPrice(non-existent) => threw exception => OK");
        }
    }

    private static void testAddStockInvalidPrice(StockManager manager, String stockId, long ts, float price) {
        try {
            manager.addStock(stockId, ts, price);
            fail("Expected exception adding stock with invalid price: " + price);
        } catch (IllegalArgumentException e) {
            println("Test 4: addStock('" + stockId + "', " + price + ") => threw exception => OK");
        }
    }

    private static void testAddDuplicateStock(StockManager manager, String stockId, long ts, float price) {
        try {
            manager.addStock(stockId, ts, price);
            fail("Expected exception adding duplicate stock: " + stockId);
        } catch (IllegalArgumentException e) {
            println("Test 6: addStock(duplicate '" + stockId + "') => threw exception => OK");
        }
    }

    private static void testUpdateWithZeroDiff(StockManager manager, String stockId, long ts) {
        try {
            manager.updateStock(stockId, ts, 0f);
            fail("Expected exception updating with difference=0");
        } catch (IllegalArgumentException e) {
            println("Test 8: updateStock(...) with 0 => threw exception => OK");
        }
    }

    private static void testRemoveTimestamp(StockManager manager, String stockId, long ts, boolean expectSuccess) {
        try {
            manager.removeStockTimestamp(stockId, ts);
            if (!expectSuccess) {
                fail("Expected exception removing non-existent timestamp: " + ts);
            } else {
                println("Test 12 detail: removeStockTimestamp(" + stockId + ", " + ts + ") => success => OK");
            }
        } catch (IllegalArgumentException e) {
            if (expectSuccess) {
                fail("Unexpected exception removing valid timestamp: " + ts);
            } else {
                println("Test 11: removeStockTimestamp(non-existent " + ts + ") => threw exception => OK");
            }
        }
    }

    private static void testRangeException(StockManager manager, float p1, float p2) {
        try {
            manager.getAmountStocksInPriceRange(p1, p2);
            fail("Expected exception for range with p1>p2: " + p1 + " > " + p2);
        } catch (IllegalArgumentException e) {
            println("Test 14: getAmountStocksInPriceRange(" + p1 + ", " + p2 + ") => threw => OK");
        }
    }

    // A helper for asserting a condition
    private static void myAssert(boolean condition, String messageIfFail) {
        if (!condition) {
            throw new AssertionError("Assertion failed: " + messageIfFail);
        }
    }

    // Helper for expecting an exception
    private static void myAssertThrown(Runnable action, String messageIfNoException) {
        try {
            action.run();
            fail(messageIfNoException);
        } catch (IllegalArgumentException e) {
            // OK
        }
    }

    private static void fail(String msg) {
        throw new AssertionError("FAIL: " + msg);
    }

    private static void println(String msg) {
        System.out.println(msg);
    }

}
