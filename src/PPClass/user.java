package PPClass;

interface userInterface {
    public abstract String getName();

    public abstract boolean hasName();

    public abstract boolean setName(String name);
}

public class user implements userInterface {
    private String userName;

    public user(String name) {
        this.userName = name;
    }

    // 获取用户名
    @Override
    public String getName() {
        if (hasName()) {

        }
        return userName;
    }

    // 判断用户名是否存在
    @Override
    public boolean hasName() {
        if (userName == null || userName.isEmpty()) {
            System.out.println("用户名不存在");
        }
        return true;
    }

    @Override
    public boolean setName(String name) {
        this.userName = name;
        return true;
    }
}
