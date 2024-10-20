
import java.net.*;
import PPClass.user;
import java.util.*;
import java.text.SimpleDateFormat;
import org.json.JSONObject;

public class PP_Client {
    public static void main(String args[]) throws Exception {
        //创建Scanner对象
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入您想发送的内容：");
    
        //获取用户名
        System.out.println("请输入用户名：");
        String userName = scanner.nextLine();

        //读取用户输入的整行文本
        String userInput = scanner.nextLine();

        //获取当前时间并格式化
        String time = new SimpleDateFormat().format(new Date());

        //创建JSON对象
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userName", userName);
        jsonObject.put("time",time);
        jsonObject.put("content", userInput);

        //输出JSON字符串
        //toString(4)方法将JSON对象转换为字符串，并以 4 个空格的缩进格式打印出来。
        System.out.println(jsonObject.toString(4));

        //关闭Scanner
        scanner.close();

    }
}
