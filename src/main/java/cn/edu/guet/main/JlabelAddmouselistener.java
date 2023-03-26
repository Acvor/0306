package cn.edu.guet.main;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class JlabelAddmouselistener extends JFrame {
    private JLabel jLabel;

    public void Addmouselistener(Object rowdata[][],Object pro_rowdata[][]){
        try {
            jLabel.addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {

                    System.out.println("µã»÷" + "£¬£¬£¬");
                    rowdata[0]=pro_rowdata[0];
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
        } catch (NullPointerException ee){
            ee.printStackTrace();
        }
    }

}
