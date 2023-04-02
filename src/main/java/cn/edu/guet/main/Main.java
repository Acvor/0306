package cn.edu.guet.main;
import cn.edu.guet.bean.Product;
import cn.edu.guet.pay.WXPay;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

public class Main {

    private JFrame jFrame;
    private JPanel jPanel;
    JMenu jMenu = new JMenu("������Ϣ����");
    JMenu jMenu2 = new JMenu("�߼���Ϣ����");
    JMenuBar jMenuBar = new JMenuBar();
    JMenuItem item01 = new JMenuItem("�鿴��Ʒ");
    JMenuItem item02 = new JMenuItem("������Ʒ");
    JMenuItem item03 = new JMenuItem("������Ʒ");
    JMenuItem item04 = new JMenuItem("�޸���Ʒ");
    JMenuItem item05 = new JMenuItem("ɾ����Ʒ");

    Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
    int wide = (int)(dim.width*0.8);
    int height = (int)(dim.height*0.8);
    int Goods_Quantity=0;
    int pro_rowdata_x;//�����޳�ʼֵ�������ɵ�ǰ��Ʒ���GoodsNumber����
    int rowdata_x = -1;//��ʼֵ-1����������Ʒ��0�����һ����Ʒ

    JTable table;
    JScrollPane jscrollpane = new JScrollPane();
    private String columnNames[] = {"id", "name","price","type"};
    private Object[][] pro_rowdata =null;
    private Object[][] rowdata = new Object[10][columnNames.length];

    private JButton deleteproduct;
    private  JButton pay;

    JLabel hint = new JLabel();


    public Main(){
        pay = new JButton("����");
        pay.setBounds(600,500,100,35);
        deleteproduct = new JButton("ɾ����һ����Ʒ");
        deleteproduct.setBounds(250,500,200,35);

        jFrame = new JFrame("������");
        jFrame.setSize(1200,800);
        jFrame.setLocation(wide / 2 - 400, height / 2 - 340);
        jPanel = (JPanel) jFrame.getContentPane();
        jPanel.setLayout(null);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //jFrame.setLocation(20 , 20);
        //jFrame.setVisible(true);

        pay.addActionListener(e -> {
            int price = 0;
            String name = "";
            int i = 0;
            while (rowdata_x >= 0) {//�ж�rowdata_x����֪���˵���������Ʒ
                name += rowdata[i][1] + ";";
                price += (int) (Float.parseFloat((String) rowdata[i][2]) * 100);
                if (i == rowdata_x) break;//i��rowdata_xһ����Ҳ���ǵ�ǰ�˵�����Ʒ����һ��ʱ������ѭ��������i++
                i++;
            }
            if (price > 0) {//�м۸���ܵ�����ʾ֧����ά��
                String out_trade_no = WXPay.unifiedOrder(name, price );
                //������ʱ����,���ɶ���ʱ��
                LocalDateTime now = LocalDateTime.now();
                DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String time =  now.format(formatter2);


            /*
            ��ʾ��ά��
             */
                JFrame jFrame = new JFrame("����ҳ��");
                jFrame.setSize(330, 350);
                jFrame.setLocation(wide / 2 + 50, height / 2 - 100);

                JLabel pay = new JLabel();
                ImageIcon icon = new ImageIcon("pay.jpg");
                icon.setImage(icon.getImage().getScaledInstance(300, 300, Image.SCALE_DEFAULT));

                pay.setVerticalTextPosition(JLabel.TOP);
                pay.setBounds(5, 5, 300, 300);

                String strMsg1 = name;
                String strMsg = "<html><body bgcolor='green' color='red'>" + strMsg1 + "<br>" + price + "<body></html>";
                //pay.setText(strMsg);
                pay.setIcon(icon);

                JPanel jPanel = (JPanel) jFrame.getContentPane();
                jPanel.setLayout(null);
                jPanel.add(pay);
                jFrame.setVisible(true);

                Map<String, String> map = WXPay.doOrderQuery(out_trade_no);
                int finalPrice = price;
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        boolean tradestate = false;
                        while (true) {
                            Map<String, String> map = WXPay.doOrderQuery(out_trade_no);
                            if (map.get("trade_state").equals("SUCCESS")) {
                                jFrame.setVisible(false);
                                JFrame jFrame1 = new JFrame("֧���ɹ���");
                                jFrame1.setSize(180,180);
                                jFrame1.setLocation(wide / 2 + 10, height / 2 - 120);
                                JLabel hint = new JLabel();
                                String strMsg = "<html><body  color='black'> ֧���ɹ��� <br><body></html>";
                                hint.setText(strMsg);
                                JPanel jPanel = (JPanel) jFrame1.getContentPane();
                                jPanel.setLayout(null);
                                jPanel.add(hint);
                                hint.setHorizontalTextPosition(JLabel.RIGHT);
                                hint.setBounds(35, 65, 100, 50);
                                jPanel.repaint();
                                jFrame1.setVisible(true);
                                jFrame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                                tradestate = true;
                                break;
                            } else System.out.println("1");
                            try {
                                Thread.sleep(5000);
                            } catch (InterruptedException ex) {
                                ex.printStackTrace();
                            }
                        }
                        if(tradestate){//���֧���ɹ�

                            Connection conn = null;
                            ResultSet resultSet;
                            String url = "jdbc:oracle:thin:@106.52.247.48:1521:orcl";
                            PreparedStatement pstmt;

                            try {
                                Class.forName("oracle.jdbc.driver.OracleDriver");//1.
                                conn = DriverManager.getConnection(url, "hgs", "Grcl1234U");//2.
                                if (conn != null) {
                                    System.out.println("Connection Success.");
                                    pstmt = conn.prepareStatement("insert into goods_trade values(to_date(?,'yyyy-mm-dd hh24:mi:ss'),?,?)");//��sql������
                                    pstmt.setString(1, time);
                                    pstmt.setString(2, out_trade_no);
                                    pstmt.setDouble(3, finalPrice*0.01);
                                    pstmt.execute();
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

                        }

                    }

                });
                thread.start();

            }

            else {
                System.out.println("��ǰδѡ����Ʒ��");
                JFrame jFrame1 = new JFrame("��ʾ");
                jFrame1.setSize(180,140);
                jFrame1.setLocation(wide / 2 + 150, height / 2 + 50);
                JLabel hint = new JLabel();
                String strMsg = "<html><body  color='black'> ��ǰδѡ����Ʒ�� <br><body></html>";
                hint.setText(strMsg);
                JPanel jPanel = (JPanel) jFrame1.getContentPane();
                jPanel.setLayout(null);
                jPanel.add(hint);
                hint.setHorizontalTextPosition(JLabel.RIGHT);
                hint.setBounds(32, 25, 120, 50);
                jPanel.repaint();
                jFrame1.setVisible(true);
            }
        });

        deleteproduct.addActionListener(e -> {
                    jPanel.repaint();
                    jPanel.remove(hint);
                    //ȡ��ѡ����Ʒ����
                    if (rowdata_x >= 0) {
                        System.out.println("ȡ��" + rowdata[rowdata_x][1] + " ������");
                        rowdata[rowdata_x] = new Object[]{" ", " ", " ", " "};
                        jPanel.repaint();
                        rowdata_x--;
                    } else System.out.println("��ǰû��ѡ����Ʒ��");

                });


        item01.addActionListener(e -> {
            /*
            ����
             */
            jPanel.removeAll();
            jPanel.repaint();
            jMenu.add(item01);
            jMenu.add(item02);
            jMenu2.add(item03);
            jMenuBar.add(jMenu);
            jMenuBar.add(jMenu2);
            jMenuBar.setBounds(0,0,200,100);
            jPanel.add(jMenuBar);
            //����ʱ��rowdataҲ��ʼ��
            rowdata = new Object[10][columnNames.length];
            rowdata_x = -1;
            /*
            ����
             */
            Connection conn = null;
            String url="jdbc:oracle:thin:@106.52.247.48:1521:orcl";
            String sql = "SELECT * FROM GOODS ORDER BY \"id\" ASC";
            PreparedStatement pstmt;
            ResultSet  rs;
            try {
                Class.forName("oracle.jdbc.driver.OracleDriver");//1.
                conn= DriverManager.getConnection(url,"hgs","Grcl1234U");//2.
                pstmt = conn.prepareStatement(sql);
                rs = pstmt.executeQuery();
                List<Product> productList = new ArrayList();
                while (rs.next()){
                    Product product = new Product();
                    product.setId(rs.getInt("id"));
                    product.setName(rs.getString("name"));
                    product.setPrice(rs.getFloat("price"));
                    product.setType(rs.getString("type"));

                    productList.add(product);
                }
                rowdata = new Object[productList.size()][columnNames.length];

                for(int i=0;i<productList.size();i++){
                    rowdata[i] = new Object[]{productList.get(i).getId(),productList.get(i).getName(),productList.get(i).getPrice(),productList.get(i).getType()};
                }

            } catch (ClassNotFoundException | SQLException ex) {
                ex.printStackTrace();
            }finally {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            //����˵��ڵĴ���
            //table = JTable(rowdata,columnNames);
            //����˵��ڵĴ���
            table = new JTable(rowdata, columnNames);
            jscrollpane.setBounds(0, 100, 700, 200);
            jscrollpane.setViewportView(table);
            table.setRowHeight(35);
            /**
             * �־�����ʾ����
             */
            DefaultTableCellRenderer r = new DefaultTableCellRenderer();
            r.setHorizontalAlignment(JLabel.CENTER);
            table.setDefaultRenderer(Object.class, r);
            jPanel.add(jscrollpane);

        });
        item02.addActionListener(e -> {
            /*
            ����
             */
            jPanel.removeAll();
            jPanel.repaint();
            jMenu.add(item01);
            jMenu.add(item02);
            jMenu2.add(item03);
            jMenuBar.add(jMenu);
            jMenuBar.add(jMenu2);
            jMenuBar.setBounds(0,0,200,100);
            jPanel.add(jMenuBar);
            //����ʱ��rowdataҲ��ʼ��
            rowdata = new Object[10][columnNames.length];
            rowdata_x = -1;
            /*
            ����
             */

            Connection conn = null;
            String url="jdbc:oracle:thin:@106.52.247.48:1521:orcl";
            String sql = "SELECT * FROM GOODS";
            PreparedStatement pstmt;
            ResultSet  rs;
            /*
            ImageIcon icon = new ImageIcon("1.jpg");
            icon.setImage(icon.getImage().getScaledInstance(100,100,Image.SCALE_DEFAULT));
             */
            try{
                Class.forName("oracle.jdbc.driver.OracleDriver");//1.
                conn= DriverManager.getConnection(url,"hgs","Grcl1234U");//2.
                pstmt = conn.prepareStatement(sql);
                rs = pstmt.executeQuery();
                int GoodsNumber = 0;//��Ʒ���
                int GoodsX=800,GoodsY=100;//��ʼ��Ʒ��ʾλ��
                while (rs.next()){
                    if(rs.getInt("id")>Goods_Quantity)
                        Goods_Quantity=rs.getInt("id");//��ȡ��ʱ�ֿ�����Ʒ����
                }
                pro_rowdata = new Object[Goods_Quantity][columnNames.length];

                pstmt = conn.prepareStatement(sql);
                rs = pstmt.executeQuery();
                while(rs.next()) {
                    JLabel product01 = new JLabel();
                    product01.setHorizontalTextPosition(JLabel.RIGHT);
                    product01.setBounds(GoodsX, GoodsY, 100, 150);
                    if (GoodsNumber != 0 && GoodsNumber % 2 != 0) {
                        GoodsY += 200;
                        GoodsX -= 200;
                    }
                    else GoodsX += 200;

                    String strMsg1 = rs.getString("name");
                    String strMsg2 = "��" + rs.getDouble("price");
                    String image_url = rs.getString("pic");
                    String strMsg = "<html><body bgcolor='yellow' color='black'>" +
                            "<img width='100' height='100' src='" + image_url + "'/>"
                            + "<br>&nbsp;&nbsp;&nbsp;&nbsp;" + strMsg1
                            + "<br>&nbsp;&nbsp;&nbsp;&nbsp;" + strMsg2
                            + "<body></html>";

                    product01.setText(strMsg);
                    //product01.setIcon(icon);


                    //����Ʒ����Ϣ�ŵ�pro_rowdata�У�����������뵽rowdata

                    pro_rowdata[GoodsNumber] = new Object[]{rs.getString("id"), rs.getString("name"), rs.getString("price"), rs.getString("type")};

                    int finalGoodsNumber = GoodsNumber;
                    product01.addMouseListener(new MouseListener() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                                jPanel.repaint();
                                pro_rowdata_x = finalGoodsNumber;
                                if(rowdata_x < rowdata.length - 1) rowdata_x++;
                                if(rowdata_x == rowdata.length - 1) {
                                    hint.setHorizontalTextPosition(JLabel.RIGHT);
                                    hint.setBounds(200, 650, 150, 50);
                                    hint.setText("<html><body  color='black'><br>ѡ����Ʒ�Ѵﵽ����!<br><body></html>");
                                    jPanel.add(hint);
                                }
                                System.out.println("���" + pro_rowdata[pro_rowdata_x][1] + " ������");
                                rowdata[rowdata_x] = pro_rowdata[pro_rowdata_x];
                                jPanel.repaint();
                        }

                        @Override
                        public void mousePressed(MouseEvent e) {

                        }

                        @Override
                        public void mouseReleased(MouseEvent e) {

                        }

                        @Override
                        public void mouseEntered(MouseEvent e) {

                        }

                        @Override
                        public void mouseExited(MouseEvent e) {

                        }

                    });
                    GoodsNumber++;
                    jPanel.add(product01);
                    jPanel.repaint();

                }

            } catch (ClassNotFoundException | SQLException ex) {
                ex.printStackTrace();
            }


            table = new JTable(rowdata, columnNames);
            jscrollpane.setBounds(0, 100, 720, 400);
            jscrollpane.setViewportView(table);
            table.setRowHeight(35);
            /**
             * �־�����ʾ����
             */

            DefaultTableCellRenderer r = new DefaultTableCellRenderer();
            r.setHorizontalAlignment(JLabel.CENTER);
            table.setDefaultRenderer(Object.class, r);

            jPanel.add(jscrollpane);
            jPanel.add(deleteproduct);
            jPanel.add(pay);
            jPanel.repaint();

            /*
            try {
                conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
             */

        });

        item03.addActionListener(e -> {

            System.out.println("����������Ʒ��");
            AddGoods addGoods = new AddGoods("������Ʒ");
        });

        item04.addActionListener(e -> {
            System.out.println("�����޸���Ʒ��");
            UpdateGoods updateGoods = new UpdateGoods("�޸���Ʒ");
        });

        item05.addActionListener(e -> {
            System.out.println("����ɾ����Ʒ��");
            DeleteGoods deleteGoods = new DeleteGoods("ɾ����Ʒ");
        });

        jMenu.add(item01);
        jMenu.add(item02);
        jMenu2.add(item03);
        jMenu2.add(item04);
        jMenu2.add(item05);
        jMenuBar.add(jMenu);
        jMenuBar.add(jMenu2);
        jMenuBar.setBounds(0,0,200,100);
        jPanel.add(jMenuBar);

    }

    public JFrame getjFrame(){
        return jFrame;
    }


}
