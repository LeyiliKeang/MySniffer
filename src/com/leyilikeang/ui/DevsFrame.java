package com.leyilikeang.ui;

import com.leyilikeang.common.util.PcapUtils;
import org.jnetpcap.PcapIf;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * @author likang
 * @date 2018/9/8 10:07
 */
public class DevsFrame {

    private JPanel contentPane;
    private JComboBox devsComboBox;
    private JButton useDevButton;

    public DevsFrame(final JFrame jFrame) {
        final List<PcapIf> devs = PcapUtils.getAllDevs();
        for (PcapIf dev : devs) {
            devsComboBox.addItem(dev.getDescription() + "[" + dev.getName() + "]");
        }
        useDevButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PcapUtils.index = devsComboBox.getSelectedIndex();
                PcapUtils.useDev();
                EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        JFrame frame = new JFrame("MySniffer");
                        frame.setContentPane(new MainFrame().getContentPane());
                        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                        frame.pack();
                        frame.setLocationRelativeTo(null);
                        jFrame.setVisible(false);
                        frame.setVisible(true);
                    }
                });
            }
        });
    }

    public JPanel getContentPane() {
        return contentPane;
    }
}
