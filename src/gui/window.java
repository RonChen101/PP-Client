package gui;

import javax.swing.*;
import java.awt.*;
import java.text.*;
import java.util.*;

import org.json.*;
import json.Manager;
import user.User;

public class Window {
    public void start() {
        // 创建JFrame
        JFrame jFrame = new JFrame("PP");
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setSize(800, 600);

        // 显示消息
        JPanel logJPanel = new JPanel();
        logJPanel.setLayout(new BoxLayout(logJPanel, BoxLayout.Y_AXIS));
        JScrollPane logJScrollPane = new JScrollPane(logJPanel);

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
            logJPanel.add(temp2);
            logJPanel.add(temp3);
            logJPanel.add(temp4);
        }

        // 添加组件
        jFrame.add(logJScrollPane);

        // 设置可见
        jFrame.setVisible(true);
    }

    public void log(JPanel logJPanel) {
    }
}