package cn.edu.guet.main;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class DeleteGoods extends JFrame {


    private JTextField Gname,GModifyName,Gprice,Gtype,Gpic;
    private JLabel Label_name,Label_ModifyName,Label_price,Label_type,Label_pic;
    private JPanel jPanel;
    private JPanel jPanel2;
    private JButton addGoods;
    private JButton confirmation_y;
    private JButton confirmation_n;
    JFrame jFrame;
    Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
    int wide = (int) (dim.width * 0.8);
    int height = (int) (dim.height * 0.8);
    JLabel hint = new JLabel();

    public DeleteGoods(String title) {
        super(title);
        setSize(300, 200);
        setLocation(wide / 2 + 20, height / 2 + 20);//����λ��
        //setResizable(false);//��ֹ����
        jPanel = (JPanel) this.getContentPane();
        jPanel.setLayout(null);//����Ϊ��
        //jPanel.setLayout(new CardLayout());

        addGoods = new JButton("ɾ��");
        addGoods.setBounds(110,110,100,35);

        Gname = new JTextField();
        Gname.setBounds(100, 50, 150, 35);
        /*
        GModifyName = new JTextField();
        GModifyName.setBounds(105, 160, 190, 35);
        Gprice = new JTextField();
        Gprice.setBounds(105, 200, 190, 35);
        Gpic = new JTextField();
        Gpic.setBounds(105, 240, 190, 35);
        Gtype = new JTextField();
        Gtype.setBounds(105, 280, 190, 35);

         */

        Label_name = new JLabel();
        Label_name.setText("ɾ����Ʒ��");
        Label_name.setBounds(10,50,80,35);
        /*
        Label_ModifyName = new JLabel();
        Label_ModifyName.setText("�޸ĺ�����");
        Label_ModifyName.setBounds(5,160,85,35);
        Label_price = new JLabel();
        Label_price.setText("��Ʒ�۸�");
        Label_price.setBounds(20,200,70,35);
        Label_pic = new JLabel();
        Label_pic.setText("��ƷͼƬ��ַ");
        Label_pic.setBounds(10,240,90,35);
        Label_type = new JLabel();
        Label_type.setText("��Ʒ����");
        Label_type.setBounds(20,280,70,35);

         */

        jPanel.add(Gname);
        /*
        jPanel.add(GModifyName);
        jPanel.add(Gprice);
        jPanel.add(Gpic);
        jPanel.add(Gtype);

         */
        jPanel.add(Label_name);
        /*
        jPanel.add(Label_ModifyName);
        jPanel.add(Label_price);
        jPanel.add(Label_pic);
        jPanel.add(Label_type);

         */

        //�� ɾ�� ��Ӽ����¼� �������ڲ���
        addGoods.addActionListener(e -> {
            System.out.println("ɾ����Ʒ " + Gname.getText());
            String goodsname = Gname.getText();

            jFrame = new JFrame("��ȷ��");
            jFrame.setSize(400,200);
            jFrame.setLocation(wide / 2 + 150, height / 2 );

            jPanel2 = (JPanel) jFrame.getContentPane();
            jPanel2.setLayout(null);//����Ϊ��
            jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            confirmation_y = new JButton("��");
            confirmation_y.setBounds(70,100,80,40);
            confirmation_n = new JButton("��");
            confirmation_n.setBounds(250,100,80,40);
            confirmation_n.addActionListener(e1 -> {
                System.out.println("��� ���� ");
                jFrame.setVisible(false);
            });
            hint = new JLabel();
            hint.setHorizontalTextPosition(JLabel.RIGHT);
            hint.setBounds(150, 20, 150, 50);
            hint.setText("<html><body  color='black'><br>ȷ��Ҫɾ����Ʒ" + goodsname + " ?<br><body></html>");
            confirmation_y.addActionListener(e1 -> {
                System.out.println("��� ���ǡ� ");
                JPanel jPanel = jPanel2;
                JLabel hint1 = hint;
                Connection conn = null;
                ResultSet resultSet;
                String url = "jdbc:oracle:thin:@106.52.247.48:1521:orcl";
                //String sql="SELECT * FROM HGS_USERS WHERE username=? AND password=?";//3.
                PreparedStatement pstmt;

                try {
                    Class.forName("oracle.jdbc.driver.OracleDriver");//1.
                    conn = DriverManager.getConnection(url, "hgs", "Grcl1234U");//2.
                    if (conn != null) {
                        System.out.println("Connection Success.");
                        pstmt = conn.prepareStatement("SELECT * FROM GOODS");//��sql������
                        resultSet = pstmt.executeQuery();//�洢��ѯ�������������ڴ���
                        boolean whether_goods = false;//�Ƿ��Ѿ����ڵ�ǰ��Ʒ
                        while(resultSet.next()) {
                            //������true,���� ɾ��
                            if (resultSet.getString("name").equals(goodsname)) {
                                whether_goods = true;
                                //goodsid = resultSet.getInt("id");
                            }
                            else whether_goods = false;
                        }
                        if(whether_goods){//������ɾ����Ʒ��
                            String sql = " DELETE FROM GOODS WHERE \"name\" = ? ";
                            pstmt = conn.prepareStatement(sql);
                            pstmt.setString(1, goodsname);
                        /*
                        pstmt.setString(2, modifygoodsname);
                        pstmt.setDouble(3, goodsprice);
                        pstmt.setString(4, goodspic);
                        pstmt.setString(5, goodstype);
                        pstmt.setString(6,goodsname);

                         */
                            pstmt.execute();
                            jFrame.setVisible(false);
                            JFrame jFrame1 = new JFrame("��ʾ");
                            jFrame1.setSize(180,140);
                            jFrame1.setLocation(wide / 2 + 150, height / 2 + 50);
                            JLabel hint = new JLabel();
                            String strMsg = "<html><body  color='black'> ɾ���ɹ��� <br><body></html>";
                            hint.setText(strMsg);
                            JPanel jPanel1 = (JPanel) jFrame1.getContentPane();
                            jPanel1.setLayout(null);
                            jPanel1.add(hint);
                            hint.setHorizontalTextPosition(JLabel.RIGHT);
                            hint.setBounds(50, 25, 100, 50);
                            jPanel1.repaint();
                            jFrame1.setVisible(true);
                            setVisible(false);
                            //jFrame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                        }
                        else {
                            hint1.setHorizontalTextPosition(JLabel.RIGHT);
                            hint1.setBounds(20, 20, 280, 50);
                            hint1.setText("<html><body  color='black'><br>��Ʒ�����ڣ���ȷ����Ҫ�޸ĵ������Ʒ������<br><body></html>");
                            jPanel.add(hint1);
                            jPanel.repaint();
                            System.out.println("��Ʒ�����ڣ���ȷ����Ҫ�޸ĵ������Ʒ������");
                        }
                    } else {
                        System.out.println("Connection Defeat!");
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
            jPanel2.add(hint);
            jPanel2.add(confirmation_y);
            jPanel2.add(confirmation_n);
            jFrame.setDefaultCloseOperation(HIDE_ON_CLOSE);
            jFrame.setVisible(true);


        });

        jPanel.add(addGoods);
        setVisible(true);//ʼ����ʾ
    }
}
