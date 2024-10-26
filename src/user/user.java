package user;

import java.io.*;
import log.Manager;

interface userInterface {
    public abstract String getName();

    public abstract boolean hasName();

    public abstract boolean setName(String name);
}

public class User implements userInterface {
    private String userName;

    public User() {
        this.userName = null;
    }

    // 获取用户名
    @Override
    public String getName() {
        if (hasName()) {
            return userName;
        } else {
            return null;
        }
    }

    // 判断用户名是否存在
    @Override
    public boolean hasName() {
        if (userName != null) {
            return true;
        } else {
            Manager manager;
            manager = new Manager();
            manager.check("user.json");
            byte[] buf = new byte[1000];
            try {
                File userJSON = new File("resources/user.json");
                if (userJSON.length() == 0) {
                    return false;
                } else {
                    FileInputStream readUser = new FileInputStream("resources/user.json");
                    readUser.read(buf);
                    readUser.close();
                    // 读文件还会把\0读出来
                    this.userName = (new String(buf)).replace("\0", "");
                    return true;
                }
            } catch (Exception e) {
                System.err.println(e);
            }
        }
        return false;
    }

    // 待完善
    // 设置用户名
    @Override
    public boolean setName(String name) {
        this.userName = name;
        checkJson();
        try {
            FileOutputStream writeUser = new FileOutputStream("resources/user.json");
            writeUser.write(name.getBytes());
            writeUser.close();
        } catch (Exception e) {
            System.err.println(e);
        }
        return true;
    }

    // 检查JSON文件
    private void checkJson() {
        // 文件操作相关的类
        File folder = new File("resources");
        File userJSON = new File("resources/user.json");
        try {
            if (!userJSON.exists()) {
                folder.mkdir();
                userJSON.createNewFile();
            }
        } catch (Exception e) {
            System.err.println(e);
        }
    }
}
