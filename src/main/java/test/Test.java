package test;

import cn.edu.guet.main.Main;
import cn.edu.guet.util.PasswordEncoder;

import javax.swing.plaf.nimbus.State;
import java.sql.*;

public class Test {
    public static void main(String[] args) {
        Connection conn = null;
        ResultSet resultSet;
        String url="jdbc:oracle:thin:@106.52.247.48:1521:orcl";
        //String sql="SELECT * FROM HGS_USERS WHERE username=? AND password=?";//3.

        PreparedStatement pstmt;

        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");//1.
            conn= DriverManager.getConnection(url,"hgs","Grcl1234U");//2.
            System.out.println(conn);
            if(conn!=null){
                System.out.println("Connection Success.");

                String sql = "INSERT INTO HGS_USERS VALUES(11,11111)";
                pstmt = conn.prepareStatement(sql);//与sql做关联

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
    }
}
