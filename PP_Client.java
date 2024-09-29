
import java.net.*;

public class PP_Client {
    public static void main(String args[]) {
        try {
            // 设置服务器IP地址，参数为“目标主机IP地址”
            InetAddress serverAddr = InetAddress.getByName("47.121.136.54");

            // 创建UDP_Socket类
            DatagramSocket socket = new DatagramSocket();

            // 创建需要发送的数据
            String buf = "你好";

            // 创建UDP数据包类
            DatagramPacket packet = new DatagramPacket(buf.getBytes(), buf.getBytes().length, serverAddr, 8888);

            // 发送数据包
            socket.send(packet);

            // 关闭Socket
            socket.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
