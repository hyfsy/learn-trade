package com.hyf.trade.client;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author baB_hyf
 * @date 2025/05/26
 */
public class ResultTable {

    private List<String>     columnNames;
    private List<List<Cell>> cellData;

    private List<Stock> stocks;

    public ResultTable(List<List<String>> table) {
        if (table == null || table.size() < 2) {
            this.columnNames = Collections.emptyList();
            this.cellData = Collections.emptyList();
            return;
        }

        this.columnNames = table.remove(0);

        List<List<Cell>> cellData = new ArrayList<>();
        for (int i = 0; i < table.size(); i++) {
            List<Cell> cellValues = new ArrayList<>();
            List<String> row = table.get(i);
            for (int j = 0; j < row.size(); j++) {
                String columnName = columnNames.get(j);
                String value = row.get(j);
                cellValues.add(new Cell(i, j, columnName, value));
            }
            cellData.add(cellValues);
        }
        this.cellData = cellData;

    }

    public void print() {
        System.out.println();
        System.out.println(columnNames);
        for (List<Cell> stringList : cellData) {
            System.out.println(stringList.stream().map(c -> c.value).collect(Collectors.toList()));
        }
    }

    public Cell getCell(int rowIndex, int columnIndex) {
        if (rowIndex + 1 > cellData.size()) {
            // IndexOutOfBounds
            return null;
        }
        List<Cell> cells = cellData.get(rowIndex);
        if (cells == null) {
            return null;
        }
        if (columnIndex + 1 > cells.size()) {
            // IndexOutOfBounds
            return null;
        }
        return cells.get(columnIndex);
    }

    public Stock getStock(int rowIndex) {
        List<Stock> stocks = getStocks();
        if (rowIndex + 1 > stocks.size()) {
            // IndexOutOfBounds
            return null;
        }
        return stocks.get(rowIndex);
    }

    public void forEachRow(RowHandler rowHandler) {
        for (int i = 0; i < cellData.size(); i++) {
            rowHandler.process(i, mapToValue(cellData.get(i)));
        }
    }

    public void forEachColumn(ColumnHandler columnHandler) {
        for (int i = 0; i < columnNames.size(); i++) {
            List<Cell> rows = new ArrayList<>();
            for (List<Cell> cells : cellData) {
                Cell column = cells.get(i);
                rows.add(column);
            }
            columnHandler.process(i, columnNames.get(i), mapToValue(rows));
        }
    }

    public void forEachCell(CellHandler cellHandler) {
        for (List<Cell> row : cellData) {
            for (Cell cell : row) {
                cellHandler.process(cell);
            }
        }
    }

    private List<String> mapToValue(List<Cell> cells) {
        return cells.stream().map(cell -> cell.value).collect(Collectors.toList());
    }

    private List<Stock> getStocks() {
        if (this.stocks == null) {
            List<Stock> stocks = new ArrayList<>();
            for (int i = 0; i < cellData.size(); i++) {
                List<Cell> cells = cellData.get(i);
                Map<String, String> stockData = new LinkedHashMap<>();
                for (int j = 0; j < columnNames.size(); j++) {
                    Cell cell = cells.get(j);
                    stockData.put(cell.columnName, cell.value);
                }
                stocks.add(new Stock(i, stockData));
            }
            this.stocks = stocks;
        }
        return this.stocks;
    }

    public interface RowHandler {
        void process(int rowIndex, List<String> columns);
    }

    public interface ColumnHandler {
        void process(int columnIndex, String columnName, List<String> rows);
    }

    public interface CellHandler {
        void process(Cell cell);
    }

    public static class Cell {
        public int    rowIndex;
        public int    columnIndex;
        public String columnName;
        public String value;

        public Cell(int rowIndex, int columnIndex, String columnName, String value) {
            this.rowIndex = rowIndex;
            this.columnIndex = columnIndex;
            this.columnName = columnName;
            this.value = value;
        }
    }

    public static class Stock {

        private Map<String, String> stockData;
        public int                 rowIndex;
        public String              stockCode;
        public String              stockName;

        public Stock(int rowIndex, Map<String, String> stockData) {
            this.rowIndex = rowIndex;
            this.stockData = stockData;
            this.stockCode = stockData.get("股票代码");
            this.stockName = stockData.get("股票简称");
        }

        public String getValue(String column) {
            return stockData.get(column);
        }
    }
}
