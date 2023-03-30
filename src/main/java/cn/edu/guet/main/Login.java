package cn.edu.guet.main;
import cn.edu.guet.util.PasswordUtils;
import cn.edu.guet.util.PasswordEncoder;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;

public class Login extends JFrame {
    private JTextField account;
    private JTextField password;
    private JPanel jPanel;
    private JButton login;
    private JButton register;
    private JLabel Label_account,Label_password;
    Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
    int wide = (int)(dim.width*0.8);
    int height = (int)(dim.height*0.8);

    public Login(String title){
        super(title);
        setSize(400,320);
        setLocation(wide/2,height/2-50);//窗口位置
        setResizable(false);//禁止缩放
        jPanel=(JPanel)this.getContentPane();
        jPanel.setLayout(null);//布局为空
        //jPanel.setLayout(new CardLayout());

        account=new JTextField("hgs");
        account.setBounds(105,120,190,35);
        jPanel.add(account);

        password=new JPasswordField("hgs1234");
        password.setBounds(105,160,190,35);
        jPanel.add(password);

        login=new JButton("登陆");
        login.setBounds(105,200,190,35);

        register = new JButton("注册");
        register.setBounds(105,240,190,35);

        Label_account = new JLabel();
        Label_account.setText("用户名");
        Label_account.setBounds(35,120,80,35);
        Label_password = new JLabel();
        Label_password.setText("密码");
        Label_password.setBounds(35,160,80,35);
        jPanel.add(Label_account);
        jPanel.add(Label_password);

        //给 登录 添加监听事件 用匿名内部类
        login.addActionListener(e ->  {
                System.out.println("Ready to login....");

                String username=account.getText();
                String pass=password.getText();
             //与数据库比对
            //java提供了 JDBC 技术，通过java程序进行SELECT,UPDATE,DELETE,INSERT...

            //1.加载驱动
            //2.创建对数据库的链接
            //3.写Sql, "?" 是占位符
            //4.创建Statement对象与sql语句关联
            //5.获取数据
            //6.关闭链接

            Connection conn = null;
            ResultSet resultSet;
            String url="jdbc:oracle:thin:@106.52.247.48:1521:orcl";
            //String sql="SELECT * FROM HGS_USERS WHERE username=? AND password=?";//3.
            String sql = "SELECT * FROM hgs_users";
            PreparedStatement pstmt;

            try {
                Class.forName("oracle.jdbc.driver.OracleDriver");//1.
                conn= DriverManager.getConnection(url,"hgs","Grcl1234U");//2.
                System.out.println(conn);
                if(conn!=null){
                    System.out.println("Connection Success.");
                    pstmt = conn.prepareStatement(sql);//与sql做关联

                    //pstmt.setString(1,username);
                    ////PasswordEncoder passwordEncoder = new PasswordEncoder("我的盐");
                    ////pass = passwordEncoder.encode("hgs1234");
                    //pass = PasswordEncoder.GetMySaltPassword(pass);

                    //pstmt.setString(2,pass);
                    resultSet = pstmt.executeQuery();//存储查询结果，结果集：内存区
                    while(resultSet.next()){
                        if(resultSet.getString("user").equals(username)) {
                            //PasswordEncoder passwordEncoder = new PasswordEncoder();
                            String passwordInDatabase = resultSet.getString("password");
                            if (PasswordEncoder.matches(passwordInDatabase,pass)) {
                                System.out.println("Login Success.");
                                setVisible(false);//关闭登录窗口
                                Main main = new Main();
                                main.getjFrame().setVisible(true);
                            }
                            else System.out.println("False Password");
                        }
                        //else System.out.println("False Username!");
                    }
                    //游标能向下移动则 说明查询到了结果 用户名和密码正确
                    /*
                    if(resultSet.next()) System.out.println("Login Success.");
                    else System.out.println("Login Filed.");

                     */
                }else{
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
        register.addActionListener(e -> {
            System.out.println("Register....");
            //setVisible(false);//关闭登录窗口
            Register re = new Register("Register");
        });

        jPanel.add(login);
        jPanel.add(register);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);//始终显示
    }

    public static void main(String[] args) {
        new Login("Login_Windows");
    }
}
