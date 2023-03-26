package cn.edu.guet.main;

import cn.edu.guet.util.PasswordEncoder;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class Register extends JFrame {
    private JTextField account;
    private JTextField password;
    private JPanel jPanel;
    private JButton register;

    Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
    int wide = (int) (dim.width * 0.8);
    int height = (int) (dim.height * 0.8);

    public Register(String title) {
        super(title);
        setSize(400, 280);
        setLocation(wide / 2 + 20, height / 2 + 20);//窗口位置
        setResizable(false);//禁止缩放
        jPanel = (JPanel) this.getContentPane();
        jPanel.setLayout(null);//布局为空
        //jPanel.setLayout(new CardLayout());

        account = new JTextField();
        account.setBounds(105, 120, 190, 35);
        jPanel.add(account);

        password = new JPasswordField();
        password.setBounds(105, 160, 190, 35);
        jPanel.add(password);

        register = new JButton("注册");
        register.setBounds(105, 200, 190, 35);

        //给 注册 添加监听事件 用匿名内部类
        register.addActionListener(e -> {
            System.out.println("Ready to login....");

            String username = account.getText();
            String pass = password.getText();
            //与数据库比对
            //java提供了 JDBC 技术，通过java程序进行SELECT,UPDATE,DELETE,INSERT...

            //1.加载驱动
            //2.创建对数据库的链接
            //3.写Sql, "?" 是占位符
            //4.创建Statement对象与sql语句关联

            Connection conn = null;
            ResultSet resultSet;
            String url = "jdbc:oracle:thin:@106.52.247.48:1521:orcl";
            //String sql="SELECT * FROM HGS_USERS WHERE username=? AND password=?";//3.

            PreparedStatement pstmt;

            try {
                Class.forName("oracle.jdbc.driver.OracleDriver");//1.
                conn = DriverManager.getConnection(url, "hgs", "Grcl1234U");//2.
                System.out.println(conn);
                if (conn != null) {
                    System.out.println("Connection Success.");
                    pstmt = conn.prepareStatement("SELECT * FROM HGS_USERS");//与sql做关联
                    resultSet = pstmt.executeQuery();//存储查询结果，结果集：内存区
                    boolean whether_username = false;//是否已经存在当前用户名
                    while(resultSet.next()) {
                        //存在则false
                        if (resultSet.getString("USER").equals(username)) whether_username = false;
                        else whether_username = true;
                    }
                    if(whether_username){//不存在则新建用户
                        String sql = "INSERT INTO HGS_USERS VALUES ('111','11')";
                        pstmt = conn.prepareStatement(sql);
                        /*
                        pstmt.setString(1, username);
                        pass = PasswordEncoder.GetMySaltPassword(pass);
                        pstmt.setString(2, pass);

                         */
                        System.out.println("注册成功！");
                    }else System.out.println("用户已存在");
                } else {
                    System.out.println("COnnection Defeat!");
                }
            } catch (ClassNotFoundException | SQLException ex) {
                ex.printStackTrace();
            } finally {
                try {
                    conn.close();//关闭数据库链接
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        jPanel.add(register);
        //setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);//始终显示
    }
}

