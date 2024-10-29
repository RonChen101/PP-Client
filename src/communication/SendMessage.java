package communication;

import java.net.*;
import java.text.SimpleDateFormat;
import java.util.*;

import json.Manager;
import user.User;

// 向服务器发送消息，保存到log.json文件中
public class SendMessage extends Thread {
    private User user = new User();
    private String content;

    public SendMessage(String content) {
        this.content = content;
    }

    @Override
    public void run() {
        try {
            // 判断是否有用户名
            if (!user.hasName()) {
                // 未来需要改成一个window类中的方法
                user.setName("temp");
                System.out.println("还没有名字哦");
                System.exit(1);
            }

            // UDP相关的类
            DatagramSocket UDPsocket = new DatagramSocket();
            DatagramPacket UDPpacket;

            // 时间格式化类
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

            // 自定义类，详细请参考src/log/Manager.java
            Manager message, manager;

            // 创建UDP数据包
            message = new Manager(user.getName(), simpleDateFormat.format(new Date()), content);
            UDPpacket = new DatagramPacket(
                    message.json.toString().getBytes(),
                    message.json.toString().getBytes().length,
                    InetAddress.getByName("47.121.136.54"), 8888);

            // 发送数据
            UDPsocket.send(UDPpacket);

            // 检查json
            manager = new Manager();
            manager.check("log.json");

            // 读Json
            manager.read("log.json");

            // 编辑Json
            manager.merge(message.json);

            // 写Json
            manager.write("log.json");
        } catch (Exception e) {
            System.err.println("sendMassage" + e);
        }
    }
}
