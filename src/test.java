
import log.Manager;

public class test {
    public static void main(String[] args) throws Exception {
        Manager manager = new Manager();
        manager.read("log.json");
        manager.extract();
        System.out.println(manager.json.toString(4));