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
        setLocation(wide / 2 + 20, height / 2 + 20);//����λ��
        setResizable(false);//��ֹ����
        jPanel = (JPanel) this.getContentPane();
        jPanel.setLayout(null);//����Ϊ��
        //jPanel.setLayout(new CardLayout());

        account = new JTextField();
        account.setBounds(105, 120, 190, 35);
        jPanel.add(account);

        password = new JPasswordField();
        password.setBounds(105, 160, 190, 35);
        jPanel.add(password);

        register = new JButton("ע��");
        register.setBounds(105, 200, 190, 35);

        //�� ע�� ��Ӽ����¼� �������ڲ���
        register.addActionListener(e -> {
            System.out.println("Ready to login....");

            String username = account.getText();
            String pass = password.getText();
            //�����ݿ�ȶ�
            //java�ṩ�� JDBC ������ͨ��java�������SELECT,UPDATE,DELETE,INSERT...

            //1.��������
            //2.���������ݿ������
            //3.дSql, "?" ��ռλ��
            //4.����Statement������sql������

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
                    pstmt = conn.prepareStatement("SELECT * FROM HGS_USERS");//��sql������
                    resultSet = pstmt.executeQuery();//�洢��ѯ�������������ڴ���
                    boolean whether_username = false;//�Ƿ��Ѿ����ڵ�ǰ�û���
                    while(resultSet.next()) {
                        //������false
                        if (resultSet.getString("USER").equals(username)) whether_username = false;
                        else whether_username = true;
                    }
                    if(whether_username){//���������½��û�
                        String sql = "INSERT INTO HGS_USERS VALUES ('111','11')";
                        pstmt = conn.prepareStatement(sql);
                        /*
                        pstmt.setString(1, username);
                        pass = PasswordEncoder.GetMySaltPassword(pass);
                        pstmt.setString(2, pass);

                         */
                        System.out.println("ע��ɹ���");
                    }else System.out.println("�û��Ѵ���");
                } else {
                    System.out.println("COnnection Defeat!");
                }
            } catch (ClassNotFoundException | SQLException ex) {
                ex.printStackTrace();
            } finally {
                try {
                    conn.close();//�ر����ݿ�����
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        jPanel.add(register);
        //setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);//ʼ����ʾ
    }
}

