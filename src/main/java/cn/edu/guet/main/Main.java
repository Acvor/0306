package cn.edu.guet.main;
import cn.edu.guet.bean.Product;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
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

    Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
    int wide = (int)(dim.width*0.8);
    int height = (int)(dim.height*0.8);

    JTable table;
    JScrollPane jscrollpane = new JScrollPane();
    private String columnNames[] = {"min_salary", "name"};
    private Object[][] rowdata = null;

    public Main(){


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
            String sql = "SELECT * FROM JOBS";
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
                        product.setName(rs.getString("JOB_ID"));
                        product.setType(rs.getString("JOB_TITLE"));

                        productList.add(product);
                    }
                    rowdata = new Object[productList.size()][columnNames.length];

                    for(int i=0;i<productList.size();i++){
                        rowdata[i] = new Object[]{productList.get(i).getName(),productList.get(i).getType()};
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
            jscrollpane.setBounds(0, 100, 700, 370);
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

        jMenu.add(item01);
        jMenuBar.add(jMenu);
        jMenuBar.add(jMenu2);
        jMenuBar.setBounds(0,0,200,100);
        jPanel.add(jMenuBar);

    }

    public JFrame getjFrame(){
        return jFrame;
    }



}
