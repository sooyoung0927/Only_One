package com.wanted.only_one.global.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.HikariPoolMXBean;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class JDBCTemplate {

    // 커넥션 풀을 관리하는 라이브러리
    // Hikari CP
    // 현재 가장 강력한 Conneciton Pool 라이브러리
    // Spring 환경에서도 default 로 사용된다.

    /*
     * DB 와 커넥션을 맺는 것은 비용이 매우 비싸다.
     * 네트워크 연결, 사용자 인증, 초기화 등등
     * */

    private static final HikariDataSource datasource;

    // static 블록은 정적 코드 블록으로서
    // 클래스가 로드 될 때 한 번만 실행된다.
    static {

        Properties prop = new Properties();
        // JDBCTemplete.class.getClassLoader() : 클래스를 메모리에 로드하는 역할을 한다.
        // getResourceAsStream("db-info.properties") : db-info.properties 파일을 스트림으로 가져오는 역할
        try {
            prop.load(JDBCTemplate.class.getClassLoader().getResourceAsStream("db-info.properties"));

            // HikariDataSource 를 구성 시 필요한 환경세팅 객체
            HikariConfig config = new HikariConfig();

            // db 정보 설정
            config.setJdbcUrl(prop.getProperty("db.url"));
            config.setUsername(prop.getProperty("db.username"));
            config.setPassword(prop.getProperty("db.password"));

            // connection 관련 설정
            config.setMaximumPoolSize(10); // 최대 10개의 커넥션 관리
            config.setMinimumIdle(5);      // 최소 5개 커넥션 유지
            config.setMaxLifetime(180000); // 커넥션을 사용할 수 있는 최대 시간. 30분 후 새롭게 생성한다.
            // 30분 후 새롭게 생성한다.
            config.setConnectionTimeout(2000); // 연결 요청이 2초 이상 지연되면 연결 실패로 인식

            // 구성한 환경 설정을 바탕으로 datasource 객체 생성
            datasource = new HikariDataSource(config);


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // 커넥션 풀에서 연결 된 객체를 꺼내오는 메서드
    public static Connection getConnection() throws SQLException {
        return datasource.getConnection();
    }

    // 전체 커넥션 풀을 종료하는 메서드
    public static void close() {
        if(datasource != null) {
            datasource.close();
        }
    }

    // 옵션 : 커넥션 풀 상태 확인 메서드
    public static void printConnectionStatus() {
        HikariPoolMXBean poolMXBean = datasource.getHikariPoolMXBean();
        System.out.println("🔎[HikariCP 커넥션 풀 상태 확인!!!]");
        System.out.println("총 커넥션 수 : " + poolMXBean.getTotalConnections());
        System.out.println("활성 커넥션 수 : " + poolMXBean.getActiveConnections());
        System.out.println("유후(idle) 커넥션 수 : " + poolMXBean.getIdleConnections());
        System.out.println("===========================================================");
    }

}