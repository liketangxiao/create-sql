package com.idea.plugin;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author yanzhao
 * @date 2022/1/26 14:09
 * @since 1.0.0
 */
public class Main {

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        String URL = "jdbc:mysql://192.168.60.116:3306/cc_dt_qa_fssc?useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull&useSSL=false";
        String USER = "ecs_cs";
        String PASSWORD = "ecs_cs";
        Class.forName("com.mysql.jdbc.Driver");

        Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
        String sql="SELECT * FROM USER WHERE id = ? AND name = ?";
        PreparedStatement statement = conn.prepareStatement(sql);
        statement.setLong(1,1L);
    }
}
