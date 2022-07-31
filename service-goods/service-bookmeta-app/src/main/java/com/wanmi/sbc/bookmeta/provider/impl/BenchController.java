package com.wanmi.sbc.bookmeta.provider.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;

import javax.annotation.PostConstruct;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
//@RestController
public class BenchController {
    private static final String LOGGER_FORMAT = "操作执行异常：异常编码{},异常信息：{},堆栈信息：{}";
    private static int max = 90;
    private static AtomicBoolean inited = new AtomicBoolean(false);
    private static List<Connection> conns = new ArrayList<>(max);

    //bench
    private static final String hosturl = "192.168.1.54:9030/metabook";
    private static final String username = "root";
    private static final String password = "123456";

    //orign
//    private static final String hosturl = "172.26.128.37:3306/sbc-goods";
//    private static final String username = "fddsh_mall";
//    private static final String password = "776b1418FD";


    @PostMapping("/book/test")
    public String test() throws Exception {
        String sql =
                "SELECT count(1) \n" +
                        "FROM\n" +
                        "\tmeta_book t1\n" +
                        "\tLEFT JOIN meta_book_content t2 ON t2.book_id = t1.id AND t2.del_flag=0\n" +
                        "\tLEFT JOIN meta_book_rcmmd t3 ON t3.book_id = t1.id AND t3.del_flag =0\n" +
                        "\tLEFT JOIN meta_book_label t4 ON t4.book_id = t1.id AND t4.del_flag = 0\n" +
                        "\tLEFT JOIN meta_label t5 ON t5.id = t4.label_id AND t5.del_flag=0\n" +
                        "\tLEFT JOIN meta_book_figure t6 ON t6.book_id = t1.id AND t6.del_flag=0\n" +
                        "\tLEFT JOIN meta_figure t7 ON t7.id=t6.figure_id and t7.del_flag=0\n" +
                        "WHERE RAND() > 0\n" +
                        "\tLIMIT 0, 1000";

        PreparedStatement prepStmt = getConnect().prepareStatement(sql);
        ResultSet result = prepStmt.executeQuery();
        log.info("col num = {}", result.getMetaData().getColumnCount());
//        while (result.next()) {
//            log.info("id={}, name={}", result.getString("id"), result.getString("name"));
//        }
        return "ok";
    }

    private AtomicInteger count = new AtomicInteger(0);
    private Connection getConnect() {
        return conns.get(count.addAndGet(1) % max);
    }

    @PostConstruct
    public static void init() {
        if (inited.compareAndSet(false, true)) {
            for (int i=0; i<max; i++) {
                conns.add(createConnect());
            }
        }
    }

    private static Connection createConnect() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            //2.设置需登录的属性文件
            Properties prop = new Properties();
            prop.setProperty("user", username);
            prop.setProperty("password", password);
            //3.用注册的manager来获取连接
            return DriverManager.getConnection("jdbc:mysql://" + hosturl, prop);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
