import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public abstract class AbstractServerClient {
    public void send(int cmd, String content, OutputStream out) throws IOException{
        out.write(cmd);
        byte[] bytes=content.getBytes("utf-8");
        int length = bytes.length;
        out.write(0xFF&(length >>8));
        out.write(0xFF&length);
        out.write(bytes);
    }
    public void receive(Socket socket, InputStream in,OutputStream out)throws IOException{
        while (true){
            int cmd = in.read();
            if (cmd == -1){
                break;
            }
            int hi = in.read();
            int lo = in.read();
            int length = (hi << 8) + lo;
            byte[] bytes = new  byte[length];
            in.read(bytes);
            String content =new String(bytes,"utf-8");
            handle(cmd,content,socket,in,out);
        }
    }

    protected abstract void handle(int cmd, String content, Socket socket, InputStream in, OutputStream out) throws IOException;
}
