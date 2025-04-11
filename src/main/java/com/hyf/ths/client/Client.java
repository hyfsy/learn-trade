package com.hyf.ths.client;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author baB_hyf
 * @date 2025/04/05
 */
public class Client {

    // TODO 重要
    static String Hexin_v = "xxx";
    static String hash_func_32 = "xxx";

    static boolean test_mode = true;
    static String m_n = "Ths_iwencai_Xuangu";
    static String cookie_userid = null;
    static String cookie_other_uid = m_n + "_" + hash_func_32;
    static int pageNum = 1;
    static int pageSize = 50;

    public static void main(String[] args) {
        String question = "";
        List<List<String>> resultTable = getResultTable(question);
        printResultTable(resultTable);
        System.out.println();
    }

    public static List<List<String>> getResultTable(String question) {
        if (question == null || question.isEmpty() || question.trim().isEmpty()) {
            throw new IllegalStateException("question is empty");
        }
        String url = "https://www.iwencai.com/customized/chart/get-robot-data";
        String rsh = cookie_userid == null ? cookie_other_uid : cookie_userid;
        String body = "{\"source\":\"" + m_n + "\",\"version\":\"2.0\",\"query_area\":\"\",\"block_list\":\"\",\"add_info\":\"{\\\"urp\\\":{\\\"scene\\\":1,\\\"company\\\":1,\\\"business\\\":1},\\\"contentType\\\":\\\"json\\\",\\\"searchInfo\\\":true}\",\"question\":\"" + question + "\",\"perpage\":"+ pageSize + ",\"page\":" + pageNum + ",\"secondary_intent\":\"stock\",\"log_info\":\"{\\\"input_type\\\":\\\"typewrite\\\"}\",\"rsh\":\"" + rsh + "\"}";
        Map<String, String> headers = new HashMap<>();
        headers.put("Origin", "https://www.iwencai.com");
        headers.put("Referer", "https://www.iwencai.com");
        headers.put("Hexin-v", Hexin_v);
        try {
            String json = null;
            if (test_mode) {
                json = HttpClient.readAsString(Client.class.getResourceAsStream("/a.json"));
            }
            else {
                json = HttpClient.postString(url, body, headers);
            }
            JSONObject jsonObject = JSON.parseObject(json);
            JSONObject data = jsonObject.getJSONObject("data");
            JSONArray answer = data.getJSONArray("answer");
            JSONObject o1 = answer.getJSONObject(0);
            JSONArray txt = o1.getJSONArray("txt");
            JSONObject o = txt.getJSONObject(0);
            JSONObject content = o.getJSONObject("content");
            JSONArray components = content.getJSONArray("components");
            JSONObject jsonObject1 = components.getJSONObject(0);
            JSONObject data1 = jsonObject1.getJSONObject("data");
            JSONArray columns = data1.getJSONArray("columns");
            List<Column> columnList = new ArrayList<>();
            for (int i = 0; i < columns.size(); i++) {
                JSONObject jsonObject2 = columns.getJSONObject(i);
                Column column = new Column();
                column.index_name = jsonObject2.getString("index_name");
                column.key = jsonObject2.getString("key");
                columnList.add(column);
            }
            List<List<String>> table = new ArrayList<>();
            List<String> columnNameList = columnList.stream().map(Column::getKey).collect(Collectors.toList());
            table.add(columnNameList);
            JSONArray datas = data1.getJSONArray("datas");
            for (int i = 0; i < datas.size(); i++) {
                JSONObject jsonObject2 = datas.getJSONObject(i);
                List<String> row = new ArrayList<>();
                for (String columnName : columnNameList) {
                    String value = jsonObject2.getString(columnName);
                    row.add(value);
                }
                table.add(row);
            }
            return table;
        } catch (IOException e) {
            throw new RuntimeException("getResultTable failed", e);
        }
    }

    private static void printResultTable(List<List<String>> resultTable) {
        System.out.println();
        for (List<String> stringList : resultTable) {
            System.out.println(stringList);
        }
    }

    public static void login() {
        cookie_userid = "xxx";
    }

    private static class Column {
        String index_name; // 换手率
        String key; // 换手率[20250206]

        public String getKey() {
            return key;
        }
    }

}
