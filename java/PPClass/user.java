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

    @Override
    public String getName() {
        return "";
    }

    @Override
    public boolean hasName() {
        return true;
    }

    @Override
    public boolean setName(String name) {
        this.userName = name;
        return true;
    }
}
