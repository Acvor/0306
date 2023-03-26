package cn.edu.guet.main;

import cn.edu.guet.util.PasswordEncoder;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class AddGoods extends JFrame {
    private JTextField Gname,Gprice,Gtype,Gpic;
    private JLabel Label_name,Label_price,Label_type,Label_pic;
    private JPanel jPanel;
    private JButton addGoods;
    Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
    int wide = (int) (dim.width * 0.8);
    int height = (int) (dim.height * 0.8);

    public AddGoods(String title) {
        super(title);
        setSize(400, 400);
        setLocation(wide / 2 + 20, height / 2 + 20);//窗口位置
        //setResizable(false);//禁止缩放
        jPanel = (JPanel) this.getContentPane();
        jPanel.setLayout(null);//布局为空
        //jPanel.setLayout(new CardLayout());

        addGoods = new JButton("增加商品");
        addGoods.setBounds(150,320,100,35);

        Gname = new JTextField();
        Gname.setBounds(105, 160, 190, 35);
        Gprice = new JTextField();
        Gprice.setBounds(105, 200, 190, 35);
        Gpic = new JTextField();
        Gpic.setBounds(105, 240, 190, 35);
        Gtype = new JTextField();
        Gtype.setBounds(105, 280, 190, 35);
        Label_name = new JLabel();
        Label_name.setText("商品名称");
        Label_name.setBounds(20,160,70,35);
        Label_price = new JLabel();
        Label_price.setText("商品价格");
        Label_price.setBounds(20,200,70,35);
        Label_pic = new JLabel();
        Label_pic.setText("商品图片地址");
        Label_pic.setBounds(10,240,90,35);
        Label_type = new JLabel();
        Label_type.setText("商品类型");
        Label_type.setBounds(20,280,70,35);

        jPanel.add(Gname);
        jPanel.add(Gprice);
        jPanel.add(Gpic);
        jPanel.add(Gtype);
        jPanel.add(Label_name);
        jPanel.add(Label_price);
        jPanel.add(Label_pic);
        jPanel.add(Label_type);

        //给 注册 添加监听事件 用匿名内部类
        addGoods.addActionListener(e -> {
            System.out.println("准备增加商品");
            String goodsname = Gname.getText();
            Double goodsprice = Double.valueOf(Gprice.getText()).doubleValue();
            String goodstype = Gtype.getText();
            String goodspic = Gpic.getText();
            int goodsid = -1;

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
                    pstmt = conn.prepareStatement("SELECT * FROM GOODS");//与sql做关联
                    resultSet = pstmt.executeQuery();//存储查询结果，结果集：内存区
                    boolean whether_goods = false;//是否已经存在当前商品
                    while(resultSet.next()) {
                        //存在则false
                        if (resultSet.getString("name").equals(goodsname)) {
                            whether_goods = false;
                            System.out.println("该商品已存在！");
                        }
                        else whether_goods = true;
                        goodsid = resultSet.getInt("id");
                    }
                    if(whether_goods){//不存在则往数据库中增加商品
                        String sql = "INSERT INTO GOODS VALUES (?,?,?,?,?)";
                        pstmt = conn.prepareStatement(sql);
                        pstmt.setInt(1, goodsid+1);
                        pstmt.setString(2, goodsname);
                        pstmt.setDouble(3, goodsprice);
                        pstmt.setString(4, goodspic);
                        pstmt.setString(5, goodstype);
                        while(resultSet.next()){
                            if (resultSet.getString("name").equals(goodsname)) {
                                System.out.println("成功增加商品！");
                            }
                        }
                    }else System.out.println("商品已存在，若是想修改商品信息，请选择修改商品");
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

        });

        jPanel.add(addGoods);
        //setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);//始终显示
    }

}
