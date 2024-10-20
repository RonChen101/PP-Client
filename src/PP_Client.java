import java.net.*;
import PPClass.user;
import java.util.*;
import java.text.SimpleDateFormat;
import org.json.JSONObject;
import log.Manager;
import java.io.*;

class sendMessage extends Thread {
    @Override
    public void run() {
        try {
            Scanner scanner;
            String userName;
            String content;
            String time;
            Manager manager;
            // 缓冲
            byte[] buf = new byte[10000];
            // 文件操作相关的类
            File log = new File("./resources/log.json");

            // UDP相关的类
            DatagramSocket UDPsocket = new DatagramSocket();
            DatagramPacket UDPpacket;

            // 检查log.json
            if (!log.exists()) {
                log.createNewFile();
            }

            FileInputStream readLog = new FileInputStream(log);

            // 创建需要发送的json和log.json对象
            JSONObject jsonObject;
            JSONObject logJson;

            // 创建Scanner对象
            scanner = new Scanner(System.in);

            // 获取用户名
            System.out.println("请输入用户名：");
            userName = scanner.nextLine();

            // 读取用户输入的整行文本
            System.out.println("请输入您想发送的内容：");
            content = scanner.nextLine();
            scanner.close();

            // 获取当前时间并格式化
            time = new SimpleDateFormat().format(new Date());

            // 创建JSON对象
            jsonObject = new JSONObject();
            jsonObject.put("userName", userName);
            jsonObject.put("time", time);
            jsonObject.put("content", content);

            // 输出JSON字符串
            // toString(4)方法将JSON对象转换为字符串，并以 4 个空格的缩进格式打印出来。
            System.out.println(jsonObject.toString(4));
            UDPpacket = new DatagramPacket(jsonObject.toString().getBytes(), jsonObject.toString().getBytes().length,
                    InetAddress.getByName("47.121.136.54"), 8888);
            UDPsocket.send(UDPpacket);

            // 读本地Json
            readLog.read(buf);
            if (buf[0] == 0) {
                FileOutputStream writeEmpty = new FileOutputStream(log);
                writeEmpty.write("{\"data\":[]}".getBytes());
                writeEmpty.close();
                readLog.read(buf);
            }
            logJson = new JSONObject(new String(buf));

            // 添加JSON数据到本地
            manager = new Manager(userName);
            manager.add(logJson, time, content);

            System.out.println(logJson.toString(4));

            FileOutputStream writeLog = new FileOutputStream(log);

            // 写数据
            writeLog.write(logJson.toString(4).getBytes());

            writeLog.close();
        } catch (Exception e) {
            System.err.println(e);
        }
    }

}

public class PP_Client {
    public static void main(String args[]) throws Exception {
        sendMessage mySendM = new sendMessage();
        mySendM.start();
    }
}
