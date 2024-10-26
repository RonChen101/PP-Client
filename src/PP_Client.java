import org.json.JSONObject;

import java.net.*;
import java.text.SimpleDateFormat;
import java.util.*;

import gui.window;
import json.Manager;
import user.User;

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
            System.err.println(e);
        }
    }

}

// 向服务器请求更新
class update extends Thread {
    @Override
    public void run() {
        try {

            // 自定义类，详细请参考src/log/Manager.java
            Manager manager = new Manager(), message = new Manager();

            // 创建UDP_socket
            DatagramSocket UDPsocket = new DatagramSocket();

            // 创建UDP数据包
            DatagramPacket UDPpacket;

            // 缓冲区
            byte[] buf = new byte[10000];

            while (true) {
                // 检查log.json
                message.check("log.json");

                // 读Json
                message.read("log.json");

                // 提取时间
                message.extract();

                // 生成数据包
                UDPpacket = new DatagramPacket(
                        message.json.toString().getBytes(),
                        message.json.toString().getBytes().length,
                        InetAddress.getByName("47.121.136.54"),
                        6666);

                // 发送数据包
                UDPsocket.send(UDPpacket);

                // 接收新数据
                UDPpacket = new DatagramPacket(buf, buf.length);
                UDPsocket.receive(UDPpacket);

                // 检查文件
                manager.check("log.json");

                // 读取文件
                manager.read("log.json");

                // 合并
                manager.merge(new JSONObject(buf.toString()));

                // 写入文件
                manager.write("log.json");
            }
        } catch (Exception e) {
            System.err.println(e);
        }

    }
}

public class PP_Client {
    public static void main(String args[]) {
        Scanner scanner = new Scanner(System.in);

        sendMessage mySendMessage;
        update myUpdate = new update();
        myUpdate.start();

        while (true) {
            System.out.print("发送：");
            mySendMessage = new sendMessage(new User(), scanner.nextLine());
            mySendMessage.start();
        }
    }
}