package user;

import org.json.JSONArray;
import org.json.JSONObject;

import json.Manager;

public class User {
    private String userName;
    private Manager manager;
    private JSONObject json;
    private JSONArray data;

    public User() {
        this.userName = null;
    }

    // 获取用户名
    public String getName() {
        if (hasName()) {
            return userName;
        } else {
            return null;
        }
    }

    // 判断用户名是否存在
    public boolean hasName() {
        if (userName != null) {
            return true;
        } else {
            manager = new Manager();
            manager.check("user.json");
            json = manager.read("user.json");
            if (json.getJSONArray("data").length() == 0) {
                return false;
            } else {
                data = json.getJSONArray("data");
                this.userName = data.getJSONObject(0).getString("userName");
                return true;
            }
        }
    }

    // 设置用户名
    public void setName(String name) {
        this.userName = name;
        manager = new Manager();
        manager.check("user.json");
        json = manager.read("user.json");
        data = json.getJSONArray("data");
        if (data.length() == 0) {
            JSONObject temp = new JSONObject();
            temp.put("userName", name);
            data.put(temp);
        } else {
            data.getJSONObject(0).put("userName", name);
        }
        manager.write("user.json");
    }
}
