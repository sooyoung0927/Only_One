package com.wanted.only_one.global.utils;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class QueryUtil {

    private static final Map<String, String> queries = new HashMap<>();

    static {
        loadQueries();
    }

    private static void loadQueries() {
        try {
            loadQueryFile("member_queries.xml");
            loadQueryFile("payments_queries.xml");
            loadQueryFile("study_queries.xml");
            loadQueryFile("queries.xml");
        } catch (Exception e) {
            throw new RuntimeException("쿼리 로딩 중 오류 발생", e);
        }
    }

    private static void loadQueryFile(String fileName) throws Exception {
        InputStream inputStream = QueryUtil.class.getClassLoader().getResourceAsStream(fileName);

        if (inputStream == null) {
            throw new RuntimeException(fileName + " 파일을 찾을 수 없습니다.");
        }

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        Document document = builder.parse(inputStream);
        document.getDocumentElement().normalize();

        NodeList nodeList = document.getElementsByTagName("query");

        for (int i = 0; i < nodeList.getLength(); i++) {
            Element queryElement = (Element) nodeList.item(i);
            String key = queryElement.getAttribute("key");
            String sql = queryElement.getTextContent().trim();
            queries.put(key, sql);
        }
    }

    public static String getQuery(String key) {
        return queries.get(key);
    }
}