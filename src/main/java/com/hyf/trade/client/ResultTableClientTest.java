package com.hyf.trade.client;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author baB_hyf
 * @date 2025/05/26
 */
public class ResultTableClientTest {

    static String Hexin_v      = "";
    static String hash_func_32 = "eughhwmopqt3vc64hy7rlvuzvljhnfwk";
    static String question     = "当前交易日竞价涨幅大于等于2%小于等于5%，前二十个交易日内有涨停，当前交易日涨跌幅排序，所属概念板块";

    public static void main(String[] args) {
        if (Hexin_v == null || Hexin_v.isEmpty()) {
            throw new IllegalArgumentException("Hexin_v is empty");
        }
        ResultTableClient client = new ResultTableClient();
        client.init(Hexin_v, hash_func_32, false);

        ResultTable resultTable = client.createResultTable(question);
        resultTable.print();
        List<String> values = new ArrayList<>();
        resultTable.forEachColumn(new ResultTable.ColumnHandler() {
            @Override
            public void process(int columnIndex, String columnName, List<String> rows) {
                if ("所属概念".equals(columnName)) {
                    for (String row : rows) {
                        String[] concepts = row.split(";");
                        values.addAll(Arrays.asList(concepts));
                    }
                }
            }
        });

        Map<String, Long> results = values.stream().collect(Collectors.groupingBy(v -> v, Collectors.counting()));
        List<Concept> concepts = new ArrayList<>();
        results.forEach((k, v) -> concepts.add(new Concept(k, v)));
        concepts.sort(Comparator.comparingLong(c -> c.count));
        for (Concept concept : concepts) {
            System.out.println(concept.name + "->" + concept.count);
        }

        System.out.println();
    }

    private static class Concept {
        public String name;
        public long   count;

        public Concept(String name, long count) {
            this.name = name;
            this.count = count;
        }
    }
}
