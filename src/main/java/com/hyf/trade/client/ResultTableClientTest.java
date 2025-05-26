package com.hyf.trade.client;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author baB_hyf
 * @date 2025/05/26
 */
public class ResultTableClientTest {

    static String Hexin_v      = "";
    static String hash_func_32 = "";
    static String question     = "";

    public static void main(String[] args) {
        ResultTableClient client = new ResultTableClient();
        client.init(Hexin_v, hash_func_32, false);

        ResultTable resultTable = client.createResultTable(question);
        resultTable.print();
        List<String> values = new ArrayList<>();
        resultTable.forEachColumn(new ResultTable.ColumnHandler() {
            @Override
            public void process(int columnIndex, String columnName, List<String> rows) {
                if ("".equals(columnName)) {
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
