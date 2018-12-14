import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Client1 extends AbstractServerClient {
    public static void main(String[] args) throws IOException {
        Client1 client = new Client1();
        Socket socket = new Socket("localhost",8800);
        System.out.println("请输入昵称：");
        Scanner s = new Scanner(System.in);
        String name = s.nextLine();
        client.send(1,name,socket.getOutputStream());
        new  Thread(()->{
           try (OutputStream out = socket.getOutputStream()){
               Scanner scanner = new Scanner(System.in);
               while (scanner.hasNextLine()){
                   String line = scanner.nextLine();
                   char cmd = line.charAt(0);
                   switch (cmd){
                       case '2':
                           client.send(2,"",out);
                           break;
                       case '3':
                           String content = line.substring(2);
                           client.send(3,content,out);
                           break;
                       case '4':
                           String content2=line.substring(2);
                           client.send(4,content2,out);
                           break;
                       default:
                   }
               }
           } catch (IOException e){
               e.printStackTrace();
           }
        }).start();
        new Thread(()->{
            try (InputStream in = socket.getInputStream();OutputStream out = socket.getOutputStream()){
                client.receive(socket,in,out);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

    }

    @Override
    protected void handle(int cmd, String content, Socket socket, InputStream in, OutputStream out) {
        switch (cmd){
            case 5:
                System.out.println(content);
                break;
            case 6:
                System.out.println(content);
            case 7:
                System.out.println(content);
                break;
            case 8:
                System.out.println(content);

        }

    }
}
