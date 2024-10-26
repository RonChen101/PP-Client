import org.json.JSONObject;

import json.Manager;

public class test {
    public static void main(String[] args) throws Exception {
        Manager temp = new Manager("罗钟琛", "时间", "我是罗钟琛");
        System.out.println(temp.json.toString(4));
    }
}