import java.net.*;
import user.User;
import java.util.*;
import org.json.JSONObject;
import java.io.*;

import gui.window;
import json.Manager;

// 向服务器发送消息，保存到log.json文件中
class sendMessage extends Thread {
    private User user;
    private String content;

    public sendMessage(User user, String content) {
        this.user = user;
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

            // 自定义类，详细请参考src/log/Manager.java
            Manager message, manager;

            // 创建UDP数据包
            message = new Manager(user.getName(), new Date().toString(), content);
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
            System.err.println(e);
        }
    }

}

// 向服务器请求更新
class update extends Thread {
    @Override
    public void run() {
        // 自定义类，详细请参考src/log/Manager.java
        Manager manager;

        // 检查log.json
        manager = new Manager();
        manager.check("log.json");

    }
}

public class PP_Client {
    public static void main(String args[]) {
        sendMessage mySend = new sendMessage(new User(), "大家好");
        mySend.start();
    }
}
