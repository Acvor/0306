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
        setLocation(wide/2,height/2-50);//����λ��
        setResizable(false);//��ֹ����
        jPanel=(JPanel)this.getContentPane();
        jPanel.setLayout(null);//����Ϊ��
        //jPanel.setLayout(new CardLayout());

        account=new JTextField("hgs");
        account.setBounds(105,120,190,35);
        jPanel.add(account);

        password=new JPasswordField("hgs1234");
        password.setBounds(105,160,190,35);
        jPanel.add(password);

        login=new JButton("��½");
        login.setBounds(105,200,190,35);

        register = new JButton("ע��");
        register.setBounds(105,240,190,35);

        Label_account = new JLabel();
        Label_account.setText("�û���");
        Label_account.setBounds(35,120,80,35);
        Label_password = new JLabel();
        Label_password.setText("����");
        Label_password.setBounds(35,160,80,35);
        jPanel.add(Label_account);
        jPanel.add(Label_password);

        //�� ��¼ ��Ӽ����¼� �������ڲ���
        login.addActionListener(e ->  {
                System.out.println("Ready to login....");

                String username=account.getText();
                String pass=password.getText();
             //�����ݿ�ȶ�
            //java�ṩ�� JDBC ������ͨ��java�������SELECT,UPDATE,DELETE,INSERT...

            //1.��������
            //2.���������ݿ������
            //3.дSql, "?" ��ռλ��
            //4.����Statement������sql������
            //5.��ȡ����
            //6.�ر�����

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
                    pstmt = conn.prepareStatement(sql);//��sql������

                    //pstmt.setString(1,username);
                    ////PasswordEncoder passwordEncoder = new PasswordEncoder("�ҵ���");
                    ////pass = passwordEncoder.encode("hgs1234");
                    //pass = PasswordEncoder.GetMySaltPassword(pass);

                    //pstmt.setString(2,pass);
                    resultSet = pstmt.executeQuery();//�洢��ѯ�������������ڴ���
                    while(resultSet.next()){
                        if(resultSet.getString("user").equals(username)) {
                            //PasswordEncoder passwordEncoder = new PasswordEncoder();
                            String passwordInDatabase = resultSet.getString("password");
                            if (PasswordEncoder.matches(passwordInDatabase,pass)) {
                                System.out.println("Login Success.");
                                setVisible(false);//�رյ�¼����
                                Main main = new Main();
                                main.getjFrame().setVisible(true);
                            }
                            else System.out.println("False Password");
                        }
                        //else System.out.println("False Username!");
                    }
                    //�α��������ƶ��� ˵����ѯ���˽�� �û�����������ȷ
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
                    conn.close();//�ر����ݿ�����
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });
        register.addActionListener(e -> {
            System.out.println("Register....");
            //setVisible(false);//�رյ�¼����
            Register re = new Register("Register");
        });

        jPanel.add(login);
        jPanel.add(register);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);//ʼ����ʾ
    }

    public static void main(String[] args) {
        new Login("Login_Windows");
    }
}
