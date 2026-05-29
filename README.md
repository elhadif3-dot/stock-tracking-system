<div align="center">

![Stock Tracking Banner](https://capsule-render.vercel.app/api?type=waving&color=0:0EA5E9,50:22C55E,100:F59E0B&height=165&section=header&text=Stock%20Tracking%20System&fontSize=42&fontColor=ffffff&animation=fadeIn&fontAlignY=38&desc=Custom%20Java%202-3%20tree%20for%20stock%20updates%20and%20price-range%20queries&descSize=15&descAlignY=60)

![Java](https://img.shields.io/badge/Java-17%2B-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Data Structures](https://img.shields.io/badge/Data%20Structures-2--3%20Tree-22C55E?style=for-the-badge)
![Algorithms](https://img.shields.io/badge/Algorithms-Range%20Queries-0EA5E9?style=for-the-badge)
![CI](https://img.shields.io/badge/GitHub%20Actions-Compile%20Check-2088FF?style=for-the-badge&logo=githubactions&logoColor=white)

</div>

# Stock Tracking System - Data Structures & Algorithms

A Java stock-management engine implemented from scratch as a data structures project. The system manages stocks, price updates, timestamped events, and price-range queries using a custom generic 2-3 tree implementation.

## Recruiter Snapshot

| Strength | What This Project Demonstrates |
| --- | --- |
| Algorithms | Balanced tree operations, ordered keys, range retrieval |
| System design | Dual indexes over the same stock objects |
| Java OOP | Encapsulated domain objects, comparable key wrappers, generic tree logic |
| Edge cases | Invalid input handling, duplicate prevention, timestamp rollback |

## Highlights

- Custom generic 2-3 tree implementation for ordered storage and retrieval.
- Dual indexing strategy: stock lookup by stock ID and ordered queries by price plus stock ID.
- Timestamped price-event history for each stock.
- Supports stock insertion, removal, price updates, event removal, current-price lookup, and range queries.
- Includes an executable test harness in `Main.java` with validation and edge-case scenarios.
- No external dependencies.

## Core API

```java
StockManager manager = new StockManager();

manager.addStock("AAPL", 1708647300000L, 185.50f);
manager.updateStock("AAPL", 1708647360000L, 2.25f);

Float currentPrice = manager.getStockPrice("AAPL");
String[] stocks = manager.getStocksInPriceRange(100f, 200f);
int count = manager.getAmountStocksInPriceRange(100f, 200f);
```

## Project Structure

```text
src/
  Main.java              Test harness and usage scenarios
  StockManager.java      Public system API and stock operations
  Tree.java              Generic 2-3 tree implementation
  Nodes.java             Tree node model
  Stock.java             Stock entity and event history
  StockEvents.java       Timestamped stock price event
  StockId.java           Comparable stock ID key wrapper
  Prices.java            Comparable price key wrapper
  PricesYstockId.java    Composite key for price-based ordering
  TimeStamps.java        Comparable timestamp key wrapper
  Outlinable.java        Interface for comparable keys with sentinels
```

## Running Locally

Requires JDK 17 or newer.

PowerShell:

```powershell
mkdir out
javac -d out src/*.java
java -cp out Main
```

Git Bash, macOS, or Linux:

```bash
mkdir -p out
javac -d out src/*.java
java -cp out Main
```

## What This Project Demonstrates

- Designing custom balanced-tree data structures.
- Maintaining multiple indexes over the same domain objects.
- Using comparable wrapper classes to support generic ordering.
- Handling edge cases such as duplicate stocks, invalid inputs, timestamp removal, and equal-price range queries.
- Building a clean Java API over low-level data-structure operations.

## Resume Summary

Built a Java stock-tracking engine backed by a custom generic 2-3 tree, supporting stock lifecycle operations, timestamped price updates, event rollback, and price-range queries through dual indexes by stock ID and price.

<div align="center">

![Footer](https://capsule-render.vercel.app/api?type=waving&color=0:F59E0B,50:22C55E,100:0EA5E9&height=95&section=footer)

</div>
