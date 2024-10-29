package communication;

import org.json.*;

import java.net.*;

import json.Manager;

public class Update extends Thread {
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
                manager.merge(new JSONObject(new String(buf)));

                // 写入文件
                manager.write("log.json");

                // 休眠
                sleep(1000);
            }
        } catch (Exception e) {
            System.err.println(e);
        }
    }
}
