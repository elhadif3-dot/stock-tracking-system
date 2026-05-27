# Architecture Notes

The project exposes a small stock-management API through `StockManager` and keeps the implementation centered around custom data structures.

## Main Components

- `StockManager` is the facade used by clients. It owns two trees: one indexed by stock ID and one indexed by price plus stock ID.
- `Tree<T extends Outlinable<T>>` is a generic 2-3 tree implementation used for all ordered indexes.
- `Nodes<T>` stores tree keys, parent/child references, and the linked domain object.
- `Stock` stores the current price and a timestamp-indexed event tree.
- `StockEvents` represents a single price change at a specific timestamp.
- `StockId`, `Prices`, `PricesYstockId`, and `TimeStamps` are comparable key wrappers that allow the generic tree to order different domain concepts.

## Indexing Strategy

`StockManager` keeps two synchronized indexes:

- `stockIdTree`: maps each stock ID to the matching `Stock` object.
- `pricesTree`: orders stocks by current price, with stock ID as a tie-breaker for deterministic range results.

When a stock price changes, the old price key is removed from `pricesTree`, the stock event is applied, and the stock is reinserted with its new price key.
