/**
 * PP
 */

import java.net.*; 
import java.util.Scanner;
public class PP {
    public static void main(String args[]){
        try {
            InetAddress serverAddr = InetAddress.getByName("47.121.136.54");
            InetAddress add = InetAddress.getByName("127.0.0.1");
            DatagramSocket soc = new DatagramSocket(8089,add);
            String str ="";
            Scanner sc= new Scanner(System.in);
            DatagramPacket packet = new DatagramPacket(str.getBytes(), str.getBytes().length,serverAddr,8888);
            while (true) {
                str = sc.next();
                soc.send(packet);
            }
            
           
           
        }
        catch (Exception e) {
            System.out.println(e);
            // TODO: handle exception
        }
    }
}
