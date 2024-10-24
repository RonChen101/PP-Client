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

            // 文件操作相关的类
            File folder = new File("resources");
            File log = new File("resources/log.json");
            FileInputStream readLog;
            FileOutputStream writeLog;

            // Json相关的类
            JSONObject jsonObject;
            JSONObject logJson;

            // 自定义类，详细请参考src/log/Manager.java
            Manager manager;

            // 检查log.json
            if (!log.exists()) {
                folder.mkdir();
                log.createNewFile();
                writeLog = new FileOutputStream("resources/log.json");
                writeLog.write("{\"data\":[]}".getBytes());
                writeLog.close();
            }

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

            // 读本地Json
            readLog = new FileInputStream("resources/log.json");
            readLog.read(buf);
            readLog.close();
            logJson = new JSONObject(new String(buf));

            // 编辑本地Json
            System.out.println(user.getName());
            System.out.println(time);
            System.out.println(content);
            manager = new Manager(user.getName(), logJson);
            manager.add(time, content);

            // 写数据
            writeLog = new FileOutputStream(log);
            writeLog.write(logJson.toString(4).getBytes());
            writeLog.close();
        } catch (Exception e) {
            System.err.println(e);
        }
    }

}

public class PP_Client {
    public static void main(String args[]) {
        User user = new User();
        String content = "你好";
        sendMessage mySendM = new sendMessage(user, content);
        mySendM.start();
    }
}
