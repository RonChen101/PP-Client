package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.*;
import java.util.*;

import org.json.*;
import json.Manager;
import user.User;
import communication.*;

public class Window {

    // 创建主窗口
    public void start() {
        // 创建JFrame
        JFrame jFrame = new JFrame("PP");
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setSize(800, 600);

        // 将主界面划分成5*5网格
        jFrame.setLayout(new GridBagLayout());

        // 设置用户名位置
        GridBagConstraints gbc1 = new GridBagConstraints();
        gbc1.gridx = 0;
        gbc1.gridy = 0;
        // 横跨五行
        gbc1.weightx = 1;
        gbc1.gridheight = 5;
        // 填充全部区域
        gbc1.fill = GridBagConstraints.BOTH;

        // 设置聊天记录框位置
        GridBagConstraints gbc2 = new GridBagConstraints();
        gbc2.gridx = 1;
        gbc2.gridy = 0;
        // 横跨四列，两行
        gbc2.weightx = 4;
        gbc2.weighty = 4;
        // 填充全部区域
        gbc2.fill = GridBagConstraints.BOTH;

        // 设置文本框位置
        GridBagConstraints gbc3 = new GridBagConstraints();
        gbc3.gridx = 1;
        gbc3.gridy = 3;
        // 横跨四列，两行
        gbc3.weightx = 4;
        gbc3.weighty = 1;
        // 填充全部区域
        gbc3.fill = GridBagConstraints.BOTH;

        // 创建文本输入框面板、用户名面板
        JPanel inputSendJPanel = new JPanel();
        JPanel userJPanel = new JPanel();

        // 在面板中添加组件
        inputSendBox(inputSendJPanel);
        displayUser(userJPanel);

        // 显示消息
        JPanel logJPanel = new JPanel();
        logJPanel.setLayout(new BoxLayout(logJPanel, BoxLayout.Y_AXIS));
        JScrollPane logJScrollPane = new JScrollPane(logJPanel);
        log(logJPanel);

        // 添加组件
        jFrame.add(userJPanel, gbc1);
        jFrame.add(logJScrollPane, gbc2);
        jFrame.add(inputSendJPanel, gbc3);

        // 设置可见
        jFrame.setVisible(true);
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
                    return;
                } else {
                    System.err.println(inputTextField.getText());
                    SendMessage sendMessage = new SendMessage(inputTextField.getText());
                    sendMessage.start();
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

    public void log(JPanel logJPanel) {
        // 读log.json
        Manager log = new Manager();
        log.check("log.json");
        log.read("log.json");

        // 将消息内容提取到logArrayList中
        JSONArray data = log.json.getJSONArray("data");
        JSONArray userLog;
        ArrayList<Map<String, String>> logArrayList = new ArrayList<>();
        for (int i = 0; i < data.length(); i++) {
            userLog = data.getJSONObject(i).getJSONArray("log");
            for (int j = 0; j < userLog.length(); j++) {
                Map<String, String> temp = new HashMap<>();
                temp.put("userName", data.getJSONObject(i).getString("userName"));
                temp.put("time", userLog.getJSONObject(j).getString("time"));
                temp.put("content", userLog.getJSONObject(j).getString("content"));
                logArrayList.add(temp);
            }
        }

        // 根据时间进行排序
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        try {
            for (int i = 0; i < logArrayList.size() - 1; i++) {
                for (int j = 0; j < logArrayList.size() - 1; j++) {
                    if (simpleDateFormat.parse(logArrayList.get(j).get("time")).after(
                            simpleDateFormat.parse(logArrayList.get(j + 1).get("time")))) {
                        Map<String, String> temp = logArrayList.get(j);
                        logArrayList.set(j, logArrayList.get(j + 1));
                        logArrayList.set(j + 1, temp);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println(e);
        }

        // 输出到屏幕
        User user = new User();
        String userName = user.getName();
        int flag;
        SimpleDateFormat simple = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        for (int i = 0; i < logArrayList.size(); i++) {
            if (logArrayList.get(i).get("userName").equals(userName)) {
                flag = FlowLayout.RIGHT;
            } else {
                flag = FlowLayout.LEFT;
            }

            // 时间
            JPanel temp1 = new JPanel();
            try {
                JLabel temp1JLabel = new JLabel(simple.format(simpleDateFormat.parse(logArrayList.get(i).get("time"))));
                temp1.add(temp1JLabel);
                temp1.setMaximumSize(new Dimension(Integer.MAX_VALUE, (int) temp1.getPreferredSize().getHeight()));
            } catch (Exception e) {
                System.err.println(e);
            }

            // 用户名
            JPanel temp2 = new JPanel(new FlowLayout(flag));
            JLabel temp2JLabel = new JLabel(logArrayList.get(i).get("userName"));
            temp2JLabel.setFont(new Font("楷体", Font.PLAIN, 12));
            temp2JLabel.setForeground(new Color(0xFF777777));
            temp2.add(temp2JLabel);
            temp2.setMaximumSize(new Dimension(Integer.MAX_VALUE, (int) temp2.getPreferredSize().getHeight()));

            // 内容
            JPanel temp3 = new JPanel(new FlowLayout(flag));
            JTextArea temp3JTextArea = new JTextArea(logArrayList.get(i).get("content"));
            temp3JTextArea.setFont(new Font("微软雅黑", Font.PLAIN, 16));
            temp3JTextArea.setEditable(false);
            temp3JTextArea.setLineWrap(true);
            if (logArrayList.get(i).get("content").length() < 15) {
                temp3JTextArea.setColumns(logArrayList.get(i).get("content").length() + 1);
            } else {
                temp3JTextArea.setColumns(15);
            }
            temp3.add(temp3JTextArea);

            // 间隔
            JPanel temp4 = new JPanel(new FlowLayout(flag));
            temp4.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));

            logJPanel.add(temp1);

            logJPanel.add(temp3);
            logJPanel.add(temp4);

        }
    }
}