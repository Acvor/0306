package cn.edu.guet.main;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class UpdateGoods extends JFrame {

    private JTextField Gname,GModifyName,Gprice,Gtype,Gpic;
    private JLabel Label_name,Label_ModifyName,Label_price,Label_type,Label_pic;
    private JPanel jPanel;
    private JButton addGoods;
    Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
    int wide = (int) (dim.width * 0.8);
    int height = (int) (dim.height * 0.8);
    JLabel hint = new JLabel();

    public UpdateGoods(String title) {
        super(title);
        setSize(400, 400);
        setLocation(wide / 2 + 20, height / 2 + 20);//窗口位置
        //setResizable(false);//禁止缩放
        jPanel = (JPanel) this.getContentPane();
        jPanel.setLayout(null);//布局为空
        //jPanel.setLayout(new CardLayout());

        addGoods = new JButton("修改商品");
        addGoods.setBounds(150,320,100,35);

        Gname = new JTextField();
        Gname.setBounds(105, 120, 190, 35);
        GModifyName = new JTextField();
        GModifyName.setBounds(105, 160, 190, 35);
        Gprice = new JTextField();
        Gprice.setBounds(105, 200, 190, 35);
        Gpic = new JTextField();
        Gpic.setBounds(105, 240, 190, 35);
        Gtype = new JTextField();
        Gtype.setBounds(105, 280, 190, 35);
        Label_name = new JLabel();
        Label_name.setText("修改商品");
        Label_name.setBounds(20,120,85,35);
        Label_ModifyName = new JLabel();
        Label_ModifyName.setText("修改后名称");
        Label_ModifyName.setBounds(5,160,85,35);
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
        jPanel.add(GModifyName);
        jPanel.add(Gprice);
        jPanel.add(Gpic);
        jPanel.add(Gtype);
        jPanel.add(Label_name);
        jPanel.add(Label_ModifyName);
        jPanel.add(Label_price);
        jPanel.add(Label_pic);
        jPanel.add(Label_type);

        //给 注册 添加监听事件 用匿名内部类
        addGoods.addActionListener(e -> {
            System.out.println("修改商品 " + Gname.getText());
            String goodsname = Gname.getText();
            String modifygoodsname = GModifyName.getText();
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
                        //存在则true,可以修改
                        if (resultSet.getString("name").equals(goodsname)) {
                            whether_goods = true;
                            goodsid = resultSet.getInt("id");
                        }
                        else whether_goods = false;
                    }
                    if(whether_goods){//存在则修改商品！
                        String sql = " UPDATE GOODS " +
                                " SET GOODS.\"id\"=?,GOODS.\"name\"=?,GOODS.\"price\"=?,GOODS.\"pic\"=?,GOODS.\"type\"=?" +
                                " WHERE GOODS.\"name\"=? ";
                        pstmt = conn.prepareStatement(sql);
                        pstmt.setInt(1, goodsid);
                        pstmt.setString(2, modifygoodsname);
                        pstmt.setDouble(3, goodsprice);
                        pstmt.setString(4, goodspic);
                        pstmt.setString(5, goodstype);
                        pstmt.setString(6,goodsname);
                        pstmt.execute();
                        hint.setHorizontalTextPosition(JLabel.RIGHT);
                        hint.setBounds(110, 60, 80, 50);
                        hint.setText("<html><body  color='black'><br>修改成功！<br><body></html>");
                        jPanel.add(hint);
                        jPanel.repaint();
                    }
                    else System.out.println("商品不存在，请确认你要修改的这个商品已增加");
                } else {
                    System.out.println("Connection Defeat!");
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
