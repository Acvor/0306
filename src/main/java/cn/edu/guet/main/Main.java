package cn.edu.guet.main;
import cn.edu.guet.bean.Product;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.*;
import java.util.List;
import java.util.ArrayList;

public class Main {

    private JFrame jFrame;
    private JPanel jPanel;
    JMenu jMenu = new JMenu("基础信息管理");
    JMenu jMenu2 = new JMenu("高级信息管理");
    JMenuBar jMenuBar = new JMenuBar();
    JMenuItem item01 = new JMenuItem("查看商品");
    JMenuItem item02 = new JMenuItem("销售商品");
    JMenuItem item03 = new JMenuItem("增加商品");

    Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
    int wide = (int)(dim.width*0.8);
    int height = (int)(dim.height*0.8);

    JTable table;
    JScrollPane jscrollpane = new JScrollPane();
    private String columnNames[] = {"id", "name","price","type"};
    private Object[][] pro_rowdata =null;
    private Object[][] rowdata = {
            {" "," "," "," "}
};

    private JButton deleteproduct;
    private  JButton pay;


    public Main(){
        pay = new JButton("结账");
        pay.setBounds(300,500,100,35);
        deleteproduct = new JButton("删除");
        deleteproduct.setBounds(300,500,100,35);

        jFrame = new JFrame("主界面");
        jFrame.setSize(1200,800);
        jPanel = (JPanel) jFrame.getContentPane();
        jPanel.setLayout(null);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //jFrame.setLocation(20 , 20);
        //jFrame.setVisible(true);


        item01.addActionListener(e -> {
            Connection conn = null;
            String url="jdbc:oracle:thin:@106.52.247.48:1521:orcl";
            String sql = "SELECT * FROM GOODS";
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
            jscrollpane.setBounds(0, 100, 700, 80);
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
        });
        item02.addActionListener(e -> {
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
                int GoodsNumber = 0;//商品数量
                int GoodsX=800,GoodsY=100;//初始商品显示位置
                JLabel[] jLabels = new JLabel[8];
                JLabel product01 = new JLabel();
                while(rs.next()) {
                    product01 = new JLabel();
                    product01.setHorizontalTextPosition(JLabel.RIGHT);
                    product01.setBounds(GoodsX, GoodsY, 100, 150);
                    if (GoodsNumber != 0 && GoodsNumber % 2 != 0) GoodsY += 200;
                    else GoodsX += 200;

                    String strMsg1 = rs.getString("name");
                    String strMsg2 = "$" + rs.getDouble("price");
                    String image_url = rs.getString("pic");
                    String strMsg = "<html><body bgcolor='yellow' color='black'>" +
                            "<img width='100' height='100' src='" + image_url + "'/>"
                            + "<br>&nbsp;&nbsp;&nbsp;&nbsp;" + strMsg1
                            + "<br>&nbsp;&nbsp;&nbsp;&nbsp;" + strMsg2
                            + "<body></html>";

                    product01.setText(strMsg);
                    //product01.setIcon(icon);

                    //将商品的信息放到pro_rowdata中，方便后续加入到rowdata
                    pro_rowdata = new Object[jLabels.length][columnNames.length];
                    pro_rowdata[GoodsNumber] = new Object[]{rs.getString("id"), rs.getString("name"), rs.getString("price"), rs.getString("type")};
                    jLabels[GoodsNumber] = product01;
                    GoodsNumber++;
                    jPanel.add(product01);
                    jPanel.repaint();
                }
                /*
                for (int i=0;i<jLabels.length;i++) {
                    JlabelAddmouselistener[] jlabelAddmouselisteners = new JlabelAddmouselistener[jLabels.length];
                    jlabelAddmouselisteners[i].Addmouselistener(rowdata,pro_rowdata);
                }

                 */


            } catch (ClassNotFoundException | SQLException ex) {
                ex.printStackTrace();
            }


            table = new JTable(rowdata, columnNames);
            jscrollpane.setBounds(0, 100, 700, 400);
            jscrollpane.setViewportView(table);
            table.setRowHeight(35);
            /**
             * 字居中显示设置
             */
            DefaultTableCellRenderer r = new DefaultTableCellRenderer();
            r.setHorizontalAlignment(JLabel.CENTER);
            table.setDefaultRenderer(Object.class, r);
            jPanel.add(jscrollpane);
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

        jMenu.add(item01);
        jMenu.add(item02);
        jMenu2.add(item03);
        jMenuBar.add(jMenu);
        jMenuBar.add(jMenu2);
        jMenuBar.setBounds(0,0,200,100);
        jPanel.add(jMenuBar);

    }

    public JFrame getjFrame(){
        return jFrame;
    }



}
