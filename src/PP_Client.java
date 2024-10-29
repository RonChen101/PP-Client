import communication.Update;
import gui.Window;

public class PP_Client {
    public static void main(String args[]) {
        Window window = Window.Instance;
        window.start();
        Update update = new Update();
        update.start();
    }
}