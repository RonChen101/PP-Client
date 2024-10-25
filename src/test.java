import java.util.Date;
import java.text.SimpleDateFormat;

public class test {
    public static void main(String[] args) throws Exception {
        Date date = new SimpleDateFormat().parse("2024/10/25 22:20");
        Date req = new SimpleDateFormat().parse("2024/11/24 22:11");
        System.out.println(date.after(req));
    }
}