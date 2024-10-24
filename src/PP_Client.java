import java.net.*;
import user.user;
import java.util.*;
import java.text.SimpleDateFormat;
import org.json.JSONObject;
import log.Manager;
import java.io.*;

// 向服务器发送消息，保存到log.json文件中
class sendMessage extends Thread {
    @Override
    public void run() {
        try {
            // 临时模拟输入
            Scanner scanner;

            // 用户名、时间、内容
            String userName, time, content;

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

            // 获取用户名和消息内容
            scanner = new Scanner(System.in);
            System.out.println("请输入用户名：");
            userName = scanner.nextLine();
            System.out.println("请输入您想发送的内容：");
            content = scanner.nextLine();
            scanner.close();

            // 创建JSON对象
            time = new SimpleDateFormat().format(new Date());
            jsonObject = new JSONObject();
            jsonObject.put("userName", userName);
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
            manager = new Manager(userName, logJson);
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
        sendMessage mySendM = new sendMessage();
        mySendM.start();
    }
}
