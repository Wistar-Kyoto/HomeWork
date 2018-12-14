import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server1 extends AbstractServerClient{
    public static void main(String[] args) {
        Server1 server = new Server1();
        System.out.println("服务器正在运行···");
        try{
            ServerSocket serverSocket = new ServerSocket(8800);
            ExecutorService service = Executors.newFixedThreadPool(10);
            while (true){
                Socket socket = serverSocket.accept();
                service.submit(()->{
                    try (InputStream in = socket.getInputStream();OutputStream out = socket.getOutputStream()){
                        server.receive(socket,in,out);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private Map<Socket,String> map = new ConcurrentHashMap<>();

    @Override
    protected void handle(int cmd, String content, Socket socket, InputStream in, OutputStream out) throws IOException{
        switch (cmd){
            case 1:
                map.put(socket,content);
                send(5,"欢迎【"+content+"】来到聊天室",out);
                break;
            case 2:
                send(6,map.values().toString(),out);
                break;
            case 3:
                System.out.println("收到的信息："+content);
                String nick = map.get(socket);
                String c=nick+"说："+content;
                map.forEach((s,value)->{
                    try {
                        send(7,c,s.getOutputStream());
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                });
                break;
            case 4:
                String[] array = content.split(" ");
                String n = array[0];
                String c2 = array[1];
                boolean found = false;
                for (Map.Entry<Socket,String> entry :map.entrySet()){
                    if (entry.getValue().equals(n)){
                        send(8,map.get(socket)+"对你私聊"+c2,entry.getKey().getOutputStream());
                        found =true;
                        break;
                    }
                }
                if (!found){
                    send(8,"无法找到该用户",out);
                }
                break;
            default:
        }

    }
}
