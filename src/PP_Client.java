import java.net.*;
import user.User;
import java.util.*;
import java.text.SimpleDateFormat;
import org.json.JSONObject;
import java.io.*;

import log.Manager;
import gui.window;

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

            // 时间
            String time;

            // 缓冲区
            byte[] buf = new byte[10000];

            // UDP相关的类
            DatagramSocket UDPsocket = new DatagramSocket();
            DatagramPacket UDPpacket;

            // Json相关的类
            JSONObject jsonObject;

            // 自定义类，详细请参考src/log/Manager.java
            Manager manager;

            // 创建JSON对象
            time = new SimpleDateFormat().format(new Date());
            jsonObject = new JSONObject();
            jsonObject.put("userName", user.getName());
            jsonObject.put("time", time);
            jsonObject.put("content", content);

            // 创建UDP数据包
            UDPpacket = new DatagramPacket(jsonObject.toString().getBytes(), jsonObject.toString().getBytes().length,
                    InetAddress.getByName("47.121.136.54"), 8888);

            // 发送数据
            UDPsocket.send(UDPpacket);

            // 检查json
            manager = new Manager(user.getName());
            manager.check("log.json");

            // 读Json
            manager.read("log.json");

            // 编辑Json
            manager.add(1, time, content);

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
        String content = "你好";
        sendMessage mySendM = new sendMessage(new User(), content);
        mySendM.start();
    }
}
