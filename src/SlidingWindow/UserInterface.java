package SlidingWindow;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UserInterface {
    private JButton btnCode;
    private JPanel panel1;
    private JButton btnDecode;

    public static void main(String[] args) {
        JFrame frame = new JFrame("UserInterface");
        frame.setContentPane( new UserInterface().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
    public UserInterface() {

        btnCode.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                Code lz77Code = new Code();
                try {
                    lz77Code.CodeFileUsingLz77();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

       /* btnDecode.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                Decode lz77Decode = new Decode();
                try {
                    lz77Decode.CodeFileUsingLz77();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });*/
    }
}
