package gui;

import java.awt.*;
import javax.swing.*;

import org.json.JSONObject;

import java.awt.event.*;
import java.util.Scanner;

import json.Manager;
import user.User;
import communication.*;

public class Window implements Runnable {

    public void run() {
        // Invoked on the event dispatching thread.
        // Construct and show GUI.
        MainWindow();
    }

    // 创建主窗口
    public void MainWindow() {
        // 创建主窗口并设置标题
        JFrame mainWindow = new JFrame("PP");

        // 设置主窗口大小
        mainWindow.setSize(300, 400);

        // 设置主窗体的位置和大小
        mainWindow.setBounds(300, 200, 500, 400);

        // 退出关闭
        mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 将主界面划分成5*5网格
        mainWindow.setLayout(new GridBagLayout());

        // 设置用户名位置
        GridBagConstraints gbc2 = new GridBagConstraints();
        gbc2.gridx = 0;
        gbc2.gridy = 0;
        // 横跨五行
        gbc2.gridheight = 5;
        // 填充全部区域
        gbc2.fill = GridBagConstraints.BOTH;

        // 设置文本框位置
        GridBagConstraints gbc1 = new GridBagConstraints();
        gbc1.gridx = 2;
        gbc1.gridy = 4;
        // 横跨四列，两行
        gbc1.weightx = 4;
        gbc1.weighty = 2;
        // 填充全部区域
        gbc1.fill = GridBagConstraints.BOTH;

        // 创建文本输入框面板、用户名面板
        JPanel inputSendJPanel = new JPanel();
        JPanel userJPanel = new JPanel();

        // 在面板中添加组件
        inputSendBox(inputSendJPanel);
        displayUser(userJPanel);

        // 将JPanel到主窗口
        mainWindow.add(inputSendJPanel, gbc1);
        mainWindow.add(userJPanel, gbc2);

        // 设置窗体可见
        mainWindow.setVisible(true);
    }

    // 创建文本输入框和按钮
    public void inputSendBox(JPanel inputSendJPanel) {
        JPanel inputJPanel = new JPanel();
        JPanel sendJPanel = new JPanel();

        // 文本框
        JTextField inputTextField = new JTextField(20);

        // 监听键盘输入事件
        inputTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {

                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    if (inputTextField.getText().length() == 0) {
                        return;
                    } else {
                        System.err.println(inputTextField.getText());
                        SendMessage sendMessage = new SendMessage(inputTextField.getText());
                        sendMessage.start();
                    }

                }

            }
        });

        inputJPanel.add(inputTextField);

        // 创建发送按钮
        JButton jButtonsend = new JButton("发送");

        // 设置按钮颜色
        jButtonsend.setBackground(Color.pink);

        // 发送按钮绑定监听
        jButtonsend.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (inputTextField.getText().length() == 0) {
                    return;
                } else {
                    SendMessage sendMessage = new SendMessage(inputTextField.getText());
                    sendMessage.start();
                }
            }
        });

        sendJPanel.add(jButtonsend);

        // 往面板中添加文本框组件
        inputSendJPanel.add(inputJPanel);
        inputSendJPanel.add(sendJPanel);
    }

    // 显示用户名
    public void displayUser(JPanel jPanelUser) {
        Manager user = new Manager();
        user.check("user.json");
        user.read("user.json");

        // 创建标签
        JLabel JLabelUser = new JLabel(
                new JSONObject(user.json.getJSONArray("data").get(0).toString()).getString("userName"));

        // 把标签放入面板
        jPanelUser.add(JLabelUser);

    }

}
