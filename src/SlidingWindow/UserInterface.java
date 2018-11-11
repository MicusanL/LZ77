package SlidingWindow;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

public class UserInterface {
    private JButton btnCode;
    private JPanel panel1;
    private JButton btnDecode;
    private JTextField textOffset;
    private JCheckBox checkBox1;
    private JTextField textLength;
    private JList list1;

    public static void main(String[] args) {
        JFrame frame = new JFrame("UserInterface");
        frame.setContentPane(new UserInterface().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);



    }

    public UserInterface() {



        btnCode.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                Code lz77Code = new Code();
                short offset = Short.parseShort(textOffset.getText());
                short length = Short.parseShort(textLength.getText());
                lz77Code.CodeFileUsingLz77(offset, length);


            }
        });

        btnDecode.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                Decode lz77Decode = new Decode();
                try {
                    lz77Decode.DecodeFileUsingLz77();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }
}
