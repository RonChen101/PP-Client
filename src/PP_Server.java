import org.json.JSONObject;

// 接收用户消息
class receiveMessage extends Thread {
    @Override
    public void run() {
        try {

        } catch (Exception e) {
            System.err.println(e);
        }
    }
}

public class PP_Server {
    public static void main(String args[]) throws Exception {
        receiveMessage myReceiveM = new receiveMessage();
        myReceiveM.start();
    }
}
