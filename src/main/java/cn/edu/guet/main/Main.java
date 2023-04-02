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
    JMenu jMenu = new JMenu("基础信息管理");
    JMenu jMenu2 = new JMenu("高级信息管理");
    JMenuBar jMenuBar = new JMenuBar();
    JMenuItem item01 = new JMenuItem("查看商品");
    JMenuItem item02 = new JMenuItem("销售商品");
    JMenuItem item03 = new JMenuItem("增加商品");
    JMenuItem item04 = new JMenuItem("修改商品");
    JMenuItem item05 = new JMenuItem("删除商品");

    Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
    int wide = (int)(dim.width*0.8);
    int height = (int)(dim.height*0.8);
    int Goods_Quantity=0;
    int pro_rowdata_x;//可以无初始值，后续由当前商品编号GoodsNumber赋予
    int rowdata_x = -1;//初始值-1，代表无商品，0代表第一个商品

    JTable table;
    JScrollPane jscrollpane = new JScrollPane();
    private String columnNames[] = {"id", "name","price","type"};
    private Object[][] pro_rowdata =null;
    private Object[][] rowdata = new Object[10][columnNames.length];

    private JButton deleteproduct;
    private  JButton pay;

    JLabel hint = new JLabel();


    public Main(){
        pay = new JButton("结账");
        pay.setBounds(600,500,100,35);
        deleteproduct = new JButton("删除上一个商品");
        deleteproduct.setBounds(250,500,200,35);

        jFrame = new JFrame("主界面");
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
            while (rowdata_x >= 0) {//判断rowdata_x即可知道菜单里有无商品
                name += rowdata[i][1] + ";";
                price += (int) (Float.parseFloat((String) rowdata[i][2]) * 100);
                if (i == rowdata_x) break;//i和rowdata_x一样，也就是当前菜单里商品数量一样时，跳出循环，不再i++
                i++;
            }
            if (price > 0) {//有价格才能调用显示支付二维码
                String out_trade_no = WXPay.unifiedOrder(name, price );
                //年月日时分秒,生成订单时间
                LocalDateTime now = LocalDateTime.now();
                DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String time =  now.format(formatter2);


            /*
            显示二维码
             */
                JFrame jFrame = new JFrame("结账页面");
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
                                JFrame jFrame1 = new JFrame("支付成功！");
                                jFrame1.setSize(180,180);
                                jFrame1.setLocation(wide / 2 + 10, height / 2 - 120);
                                JLabel hint = new JLabel();
                                String strMsg = "<html><body  color='black'> 支付成功！ <br><body></html>";
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
                        if(tradestate){//如果支付成功

                            Connection conn = null;
                            ResultSet resultSet;
                            String url = "jdbc:oracle:thin:@106.52.247.48:1521:orcl";
                            PreparedStatement pstmt;

                            try {
                                Class.forName("oracle.jdbc.driver.OracleDriver");//1.
                                conn = DriverManager.getConnection(url, "hgs", "Grcl1234U");//2.
                                if (conn != null) {
                                    System.out.println("Connection Success.");
                                    pstmt = conn.prepareStatement("insert into goods_trade values(to_date(?,'yyyy-mm-dd hh24:mi:ss'),?,?)");//与sql做关联
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
                                    conn.close();//关闭数据库链接
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
                System.out.println("当前未选购商品！");
                JFrame jFrame1 = new JFrame("提示");
                jFrame1.setSize(180,140);
                jFrame1.setLocation(wide / 2 + 150, height / 2 + 50);
                JLabel hint = new JLabel();
                String strMsg = "<html><body  color='black'> 当前未选购商品！ <br><body></html>";
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
                    //取消选购商品代码
                    if (rowdata_x >= 0) {
                        System.out.println("取消" + rowdata[rowdata_x][1] + " ，，，");
                        rowdata[rowdata_x] = new Object[]{" ", " ", " ", " "};
                        jPanel.repaint();
                        rowdata_x--;
                    } else System.out.println("当前没有选购商品！");

                });


        item01.addActionListener(e -> {
            /*
            清屏
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
            //清屏时把rowdata也初始化
            rowdata = new Object[10][columnNames.length];
            rowdata_x = -1;
            /*
            清屏
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
            //点击菜单内的代码
            //table = JTable(rowdata,columnNames);
            //点击菜单内的代码
            table = new JTable(rowdata, columnNames);
            jscrollpane.setBounds(0, 100, 700, 200);
            jscrollpane.setViewportView(table);
            table.setRowHeight(35);
            /**
             * 字居中显示设置
             */
            DefaultTableCellRenderer r = new DefaultTableCellRenderer();
            r.setHorizontalAlignment(JLabel.CENTER);
            table.setDefaultRenderer(Object.class, r);
            jPanel.add(jscrollpane);

        });
        item02.addActionListener(e -> {
            /*
            清屏
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
            //清屏时把rowdata也初始化
            rowdata = new Object[10][columnNames.length];
            rowdata_x = -1;
            /*
            清屏
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
                int GoodsNumber = 0;//商品编号
                int GoodsX=800,GoodsY=100;//初始商品显示位置
                while (rs.next()){
                    if(rs.getInt("id")>Goods_Quantity)
                        Goods_Quantity=rs.getInt("id");//获取此时仓库中商品数量
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
                    String strMsg2 = "￥" + rs.getDouble("price");
                    String image_url = rs.getString("pic");
                    String strMsg = "<html><body bgcolor='yellow' color='black'>" +
                            "<img width='100' height='100' src='" + image_url + "'/>"
                            + "<br>&nbsp;&nbsp;&nbsp;&nbsp;" + strMsg1
                            + "<br>&nbsp;&nbsp;&nbsp;&nbsp;" + strMsg2
                            + "<body></html>";

                    product01.setText(strMsg);
                    //product01.setIcon(icon);


                    //将商品的信息放到pro_rowdata中，方便后续加入到rowdata

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
                                    hint.setText("<html><body  color='black'><br>选购商品已达到上限!<br><body></html>");
                                    jPanel.add(hint);
                                }
                                System.out.println("点击" + pro_rowdata[pro_rowdata_x][1] + " ，，，");
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
             * 字居中显示设置
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

            System.out.println("调用增加商品类");
            AddGoods addGoods = new AddGoods("增加商品");
        });

        item04.addActionListener(e -> {
            System.out.println("调用修改商品类");
            UpdateGoods updateGoods = new UpdateGoods("修改商品");
        });

        item05.addActionListener(e -> {
            System.out.println("调用删除商品类");
            DeleteGoods deleteGoods = new DeleteGoods("删除商品");
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
