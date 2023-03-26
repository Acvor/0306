package test;

import cn.edu.guet.bean.Product;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author liwei
 * @Date 2023/3/12 12:21
 * @Version 1.0
 */
public class Main {

    private JFrame jFrame;
    private JPanel jPanel;

    JMenu jMenu = new JMenu("基础信息管理");
    JMenuBar jMenuBar = new JMenuBar();
    JMenuItem item01 = new JMenuItem("查看商品");
    JMenuItem item02 = new JMenuItem("销售商品");

    JButton delete;
    JButton pay;

    JTable table;
    JScrollPane jscrollpane = new JScrollPane();
    private String columnNames[] = {"ID", "商品名称", "单价", "类型"};
    private Object[][] rowData = {
            {"", "", "", ""},
            {"", "", "", ""},
            {"", "", "", ""}
    };

    public Main() {

        delete = new JButton("删除商品");
        delete.setBounds(300, 300, 100, 30);

        pay = new JButton("结账");
        pay.setBounds(300, 300, 100, 30);
        pay.addActionListener(e -> {
            /*
            开始结账
             */
            int price = 0;
            String name="";
            for (int i = 0; i < rowData.length; i++) {
                String str[] = (String[]) rowData[i];
                name+=str[1]+";";
                price += (int) Float.parseFloat(str[2]);
            }

            /*
            显示二维码
             */
            JFrame jFrame=new JFrame("结账页面");
            jFrame.setSize(300,330);

            JLabel pay=new JLabel();
            ImageIcon icon=new ImageIcon("pay.jpg");    icon.setImage(icon.getImage().getScaledInstance(300,300, Image.SCALE_DEFAULT));

            pay.setVerticalTextPosition(JLabel.TOP);
            pay.setBounds(0,0,300,300);

            String strMsg1 = name;
            String strMsg = "<html><body bgcolor='green' color='red'>" +strMsg1+ "<br>" +price+ "<body></html>";
            pay.setText(strMsg);
            pay.setIcon(icon);

            JPanel jPanel= (JPanel) jFrame.getContentPane();
            jPanel.setLayout(null);
            jPanel.add(pay);
            jFrame.setVisible(true);

        });

        jFrame = new JFrame("主界面");
        jFrame.setSize(1700, 1100);
        jPanel = (JPanel) jFrame.getContentPane();
        jPanel.setLayout(null);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        item01.addActionListener(e -> {
            /*
            1、加载驱动
            2、创建连接
            3、声明PreparedsStatement对象
            4、执行SQL
            5、获取数据
            6、关闭连接
             */
            Connection conn = null;
            String url = "jdbc:oracle:thin:@43.139.94.243:1521:orcl";
            String sql = "SELECT * FROM product";
            PreparedStatement pstmt;
            ResultSet rs;
            List<Product> productList = null;
            try {
                Class.forName("oracle.jdbc.driver.OracleDriver");
                conn = DriverManager.getConnection(url, "liwei", "Grcl1234LiweiU");
                pstmt = conn.prepareStatement(sql);
                rs = pstmt.executeQuery();
                productList = new ArrayList<>();
                while (rs.next()) {
                    Product product = new Product();
                    product.setId(rs.getInt("ID"));
                    product.setName(rs.getString("NAME"));
                    product.setPrice(rs.getFloat("PRICE"));
                    product.setType(rs.getString("TYPE"));

                    productList.add(product);//每循环一次把拿到的商品的数据存入集合，好比每摘一个芒果，把芒果放入桶里
                }
                rowData = new Object[productList.size()][columnNames.length];
                /*
                把集合的数据转成二维数组
                 */
                for (int i = 0; i < rowData.length; i++) {
                    Product product = productList.get(i);
                    rowData[i] = new Object[]{product.getId(), product.getName(), product.getPrice(), product.getType()};
                }

            } catch (ClassNotFoundException | SQLException ex) {
                ex.printStackTrace();
            } finally {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            /*DefaultTableModel tableModel = new DefaultTableModel(rowData, columnNames) {
                public boolean isCellEditable(int row, int column) {
                    return true;
                }
            };
            table=new JTable();
            table.setModel(tableModel);
             */
            table = new JTable(rowData, columnNames);
            //设置表头高度
            table.getTableHeader().setPreferredSize(new Dimension(table.getColumn("ID").getPreferredWidth(), 35));
            //表头居中
            ((DefaultTableCellRenderer) table.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);
            table.setRowHeight(35);
            /**
             * 表数据居中显示
             */
            DefaultTableCellRenderer r = new DefaultTableCellRenderer();
            r.setHorizontalAlignment(r.CENTER);
            table.setDefaultRenderer(Object.class, r);
            jscrollpane.setBounds(0, 30, jPanel.getWidth(), 35 * 7 + 5);
            jscrollpane.setViewportView(table);
            jPanel.add(jscrollpane);
            jPanel.add(delete);
            jPanel.repaint();
        });
        item02.addActionListener(e -> {

            table = new JTable(rowData, columnNames);
            //设置表头高度
            table.getTableHeader().setPreferredSize(new Dimension(table.getColumn("ID").getPreferredWidth(), 35));
            //表头居中
            ((DefaultTableCellRenderer) table.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);
            table.setRowHeight(35);
            /**
             * 表数据居中显示
             */
            DefaultTableCellRenderer r = new DefaultTableCellRenderer();
            r.setHorizontalAlignment(r.CENTER);
            table.setDefaultRenderer(Object.class, r);
            jscrollpane.setBounds(0, 30, 700, 35 * 7 + 5);
            jscrollpane.setViewportView(table);
            jPanel.add(jscrollpane);
            jPanel.add(pay);
            jPanel.repaint();

            //去数据库查询商品，并以JLabel的方式显示

            JLabel product01 = new JLabel();
            //headPhoto.setVerticalTextPosition(JLabel.CENTER);
            product01.setHorizontalTextPosition(JLabel.RIGHT);
            product01.setBounds(860, 40, 100, 150);

            String strMsg1 = "宫保鸡丁";
            String strMsg2 = "58.00";
            String url = "'http://rs209p3t9.hn-bkt.clouddn.com/gbjd.jpeg?e=1679710812&token=Vh5IcHOQV-uDk9R8WEpCegKEw7CLP-CrCaJC5opk:YG_tQ9YTed1-f7YDA5j23jVeLlc='";
            String strMsg = "<html><body bgcolor='#fae070' color='black'>" +
                    "<img width='100' height='100' align='left' " +
                    "src=" + url + "/>"
                    + "<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; ¥" + strMsg1 + "<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
                    + strMsg2 + "<body></html>";
            product01.setText(strMsg);
            product01.addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    /*
                    把所选的菜品添加到左侧
                     */
                    rowData[0] = new String[]{"1", strMsg1, strMsg2, "炒菜"};
                    rowData[1] = new String[]{"1", "鱼香肉丝", strMsg2, "炒菜"};
                    rowData[2] = new String[]{"1", "金银小馒头", strMsg2, "炒菜"};
                    jscrollpane.repaint();
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

            jPanel.add(product01);
            jPanel.repaint();

        });
        jMenu.add(item01);
        jMenu.add(item02);
        jMenuBar.add(jMenu);
        jMenuBar.setBounds(0, 0, 100, 30);
        jPanel.add(jMenuBar);
    }

    public JFrame getjFrame() {
        return jFrame;
    }

    public static void main(String[] args) {
        Main main = new Main();
        main.getjFrame().setVisible(true);
    }
}