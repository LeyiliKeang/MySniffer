package com.leyilikeang.ui;

import com.leyilikeang.common.packethandler.MyPacketHandler;
import com.leyilikeang.common.util.FileUtils;
import com.leyilikeang.common.util.MenuUtils;
import com.leyilikeang.common.util.PacketUtils;
import com.leyilikeang.common.util.PcapUtils;
import com.leyilikeang.service.CaptureService;
import com.leyilikeang.service.FileService;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author likang
 * @date 2018/9/7 20:30
 */
public class MainFrame {

    private JPanel contentPane;
    private JTextArea toHexDumpTextArea;
    private JTextArea toStringTextArea;
    private JTable packetTable;
    private JButton captureButton;
    private JButton stopButton;
    private JScrollPane packetTableScrollPane;
    private JButton fileButton;
    private JButton lookOverButton;
    private JButton toolButton;
    private JButton capButton;
    private JLabel countLabel;
    private JLabel timeLabel;
    private JLabel rateLabel;
    private JPanel bottomPane;
    private JPanel rightPane;
    private JPanel leftPane;
    private DevsFrame devsFrame;
    private JComboBox expressionComboBox;
    private JButton applyButton;

    private JFrame mainFrame;

    private CaptureService captureService = new CaptureService();

    private DefaultTableModel defaultTableModel;

    private Timer timer;

    public static boolean isReady = false;

    public static boolean isDevs = true;

    public static boolean isFilter = false;

    public MainFrame(JFrame mainFrame) {
        this();
        this.mainFrame = mainFrame;
    }

    public MainFrame() {
        expressionComboBox.setSelectedIndex(-1);
        rightPane.setVisible(false);
        captureButton.setEnabled(false);
        stopButton.setEnabled(false);
        defaultTableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        defaultTableModel.setColumnIdentifiers(
                new Object[]{"序号", "源地址", "源端口", "目的地址", "目的端口", "协议", "长度"});
        packetTable.setModel(defaultTableModel);
        packetTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        packetTableScrollPane.setViewportView(packetTable);
        packetTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (packetTable.getSelectedRow() != -1) {
                    final String hexDump = PacketUtils.allPackets.get(packetTable.getSelectedRow()).toHexdump();
                    final String toString = PacketUtils.allPackets.get(packetTable.getSelectedRow()).toString();
                    EventQueue.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            toHexDumpTextArea.setText(hexDump);
                            toStringTextArea.setText(toString);
                        }
                    });
                } else {
                    EventQueue.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            toHexDumpTextArea.setText("");
                            toStringTextArea.setText("");
                        }
                    });
                }
            }
        });

        captureButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        JDialog dialog = new JDialog(mainFrame, "捕获过滤器", true);
                        dialog.setContentPane(new FilterFrame(MainFrame.this, dialog).getContentPane());
                        dialog.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
                        dialog.pack();
                        dialog.setLocationRelativeTo(mainFrame);
                        dialog.setVisible(true);
                        int state = dialog.getDefaultCloseOperation();
                        if (state == JDialog.DISPOSE_ON_CLOSE) {
                            captureButton.setEnabled(false);
                            stopButton.setEnabled(true);
                            countLabel.setText("数量：0");
                            FileUtils.openFile = null;
                        }
                    }
                });
            }
        });

        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        captureButton.setEnabled(true);
                        stopButton.setEnabled(false);
                        rateLabel.setText("流量：0.00Kb/s");
                    }
                });
                captureService.stop();
                timer.cancel();
            }
        });

        applyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String expression = expressionComboBox.getEditor().getItem().toString().trim();
                if (isFilter) {
                    expression = "";
                    isFilter = false;
                    applyButton.setText("应用");
                    expressionComboBox.setEnabled(true);
                } else {
                    if (expression.equals("")) {
                        return;
                    }
                }
                System.out.println(expression);
                if (FileUtils.openFile == null) {
                    FileUtils.openFile = FileUtils.tempFile;
                }
                FileService fileService = new FileService();
                fileService.open(MainFrame.this, expression);
            }
        });

        // 设置快捷键为 Alt + F
        fileButton.setMnemonic('F');
        // 按下Alt键时第四个字符带有下划线
        fileButton.setDisplayedMnemonicIndex(3);
        fileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JPopupMenu fileMenu = MenuUtils.getFileMenu(MainFrame.this);
                fileMenu.show(fileButton, 0, fileButton.getHeight());
            }
        });

        lookOverButton.setMnemonic('L');
        lookOverButton.setDisplayedMnemonicIndex(3);
        lookOverButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JPopupMenu lookOverMenu = MenuUtils.getLookOverMenu();
                lookOverMenu.show(lookOverButton, 0, lookOverButton.getHeight());
            }
        });

        capButton.setMnemonic('C');
        capButton.setDisplayedMnemonicIndex(3);
        capButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JPopupMenu capMenu = MenuUtils.getCapMenu(MainFrame.this);
                capMenu.show(capButton, 0, capButton.getHeight());
            }
        });

        toolButton.setMnemonic('T');
        toolButton.setDisplayedMnemonicIndex(3);
        toolButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JPopupMenu toolsMenu = MenuUtils.getToolsMenu();
                toolsMenu.show(toolButton, 0, toolButton.getHeight());
            }
        });

        fileButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                JPopupMenu fileMenu = MenuUtils.getFileMenu(MainFrame.this);
                fileMenu.show(fileButton, 0, fileButton.getHeight());
            }
        });
        lookOverButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                JPopupMenu lookOverMenu = MenuUtils.getLookOverMenu();
                lookOverMenu.show(lookOverButton, 0, lookOverButton.getHeight());
            }
        });
        capButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                JPopupMenu capMenu = MenuUtils.getCapMenu(MainFrame.this);
                capMenu.show(capButton, 0, capButton.getHeight());
            }
        });
        toolButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                JPopupMenu toolsMenu = MenuUtils.getToolsMenu();
                toolsMenu.show(toolButton, 0, toolButton.getHeight());
            }
        });
    }

    public JPanel getContentPane() {
        return contentPane;
    }

    public DefaultTableModel getDefaultTableModel() {
        return defaultTableModel;
    }

    public JScrollPane getPacketTableScrollPane() {
        return packetTableScrollPane;
    }

    public JLabel getCountLabel() {
        return countLabel;
    }

    public DevsFrame getDevsFrame() {
        return devsFrame;
    }

    public JFrame getMainFrame() {
        return mainFrame;
    }

    public void ready() {
        isDevs = false;
        isReady = true;
        this.leftPane.setVisible(false);
        this.rightPane.setVisible(true);
    }

    public void devs() {
        isDevs = true;
        isReady = false;
        this.rightPane.setVisible(false);
        this.leftPane.setVisible(true);
        devsFrame.recent();
        devsFrame.refreshDevs();
    }

    public CaptureService getCaptureService() {
        return captureService;
    }

    public JLabel getTimeLabel() {
        return timeLabel;
    }

    public JLabel getRateLabel() {
        return rateLabel;
    }

    public void setTimer(Timer timer) {
        this.timer = timer;
    }

    public JButton getCaptureButton() {
        return captureButton;
    }

    public JButton getStopButton() {
        return stopButton;
    }

    public JButton getApplyButton() {
        return applyButton;
    }

    public JComboBox getExpressionComboBox() {
        return expressionComboBox;
    }
}
