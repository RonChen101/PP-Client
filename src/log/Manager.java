/*
 * 这个类用来管理本地Json文件
 * 属性：
 *      1、name：以name为索引
 * 方法：
 *      1、add(JSONObject json, String time, String content)
 *          参数：json对象、时间、内容
 *          描述：这个方法以name为索引，向json对象中加入新内容
 */

package log;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.*;

public class Manager {
    private String name;
    private JSONObject json;

    public Manager() {
        this.name = null;
        this.json = null;
    }

    public Manager(String name) {
        this.name = name;
        this.json = null;
    }

    // 向json中增加新数据
    public void add(int group, String time, String content) {
        JSONArray data = json.getJSONArray("data");
        JSONObject user;
        boolean flag = true;
        for (int i = 0; i < data.length(); i++) {
            user = new JSONObject(data.get(i).toString());
            if (user.getString("userName").equals(name)) {
                user.getJSONArray("log")
                        .put(new JSONObject("{\"time\":\"" + time + "\",\"content\":\"" + content + "\"}"));
                flag = false;
                data.remove(i);
                data.put(user);
                break;
            }
        }
        if (flag) {
            user = new JSONObject(
                    "{\"userName\": \"" + name + "\",\"log\": [{\"time\": \"" + time
                            + "\",\"content\": \"" + content + "\"}]}");
            data.put(user);
        }
    }

    // 检查Json
    public void check(String fileName) {
        // 文件操作相关的类
        File folder = new File("resources");
        File log = new File("resources/" + fileName);
        FileOutputStream writeJson;
        try {
            if (!log.exists()) {
                synchronized (this) {
                    if (!log.exists()) {
                        folder.mkdir();
                        log.createNewFile();
                        writeJson = new FileOutputStream("resources/" + fileName);
                        writeJson.write("{\"data\":[]}".getBytes());
                        writeJson.close();
                    }
                }
            }
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    // 读Json
    public void read(String fileName) {
        FileInputStream readJson;
        byte[] buf = new byte[10000];
        try {
            readJson = new FileInputStream("resources/" + fileName);
            readJson.read(buf);
            readJson.close();
        } catch (Exception e) {
            System.err.println(e);
        }
        this.json = new JSONObject(new String(buf));
    }

    // 设置名字
    public void setName(String name) {
        this.name = name;
    }

    // 写Json
    public void write(String fileName) {
        FileOutputStream writeJson;
        try {
            writeJson = new FileOutputStream("resources/" + fileName);
            writeJson.write(json.toString(4).getBytes());
            writeJson.close();
        } catch (Exception e) {
            System.err.println(e);
        }
    }
}