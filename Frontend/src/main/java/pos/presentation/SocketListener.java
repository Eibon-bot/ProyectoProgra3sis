package pos.presentation;

import pos.logic.Protocol;
import pos.logic.Usuario;
import pos.presentation.ThreadListener;

import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class SocketListener {
    ThreadListener listener;
    String sid; // Session Id
    Socket as; // Asynchronous Socket
    ObjectOutputStream aos;
    ObjectInputStream ais;
    public SocketListener(ThreadListener listener, String sid) throws Exception {
        this.listener = listener;
        as =new Socket(Protocol.SERVER, Protocol.PORT);
        this.sid = sid;
        aos = new ObjectOutputStream(as.getOutputStream());
        ais = new ObjectInputStream(as.getInputStream());
        aos.writeInt(Protocol.ASYNC);
        aos.writeObject(sid);
        aos.flush();
    }

    boolean condition = true;
    private Thread t;
    public void start() {
        t = new Thread(new Runnable() {  public void run() {
            listen();
        } });
        condition = true;
        t.start();
    }
    public void stop() { condition = false; }
    public void listen() {
        int method;
        while (condition) {
            try {
                method = ais.readInt();
                switch (method) {
                    case Protocol.DELIVER_MESSAGE:
                        try {

                            Usuario usuario = (Usuario) ais.readObject();
                            String message = (String) ais.readObject();
                            deliver(usuario, message);
                        } catch (ClassNotFoundException ex) {
                            break;
                        }
                        break;
                    case Protocol.USER_JOINED:
                        try {
                            Usuario usuario = (Usuario) ais.readObject();
                            joined(usuario); // nombre, id
                        } catch (ClassNotFoundException ex) {
                            break;
                        }
                        break;
                    case Protocol.USER_LEFT:
                        try {
                            Usuario usuario = (Usuario) ais.readObject();
                            left(usuario);
                        } catch (ClassNotFoundException ex) {
                            break;
                        }
                        break;
                }
            } catch (IOException ex) { condition = false; }
        }
        try {
            as.shutdownOutput();
            as.close();
        } catch (IOException e) {}
    }

    private void deliver(final Usuario usuarioEmisor, final String message) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                listener.deliver_message(usuarioEmisor, message);
            }
        });
    }
    private void joined(final Usuario usuario) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                listener.joined(usuario);
            }
        });
    }
    private void left(final Usuario usuario) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                listener.leftt(usuario);
            }
        });
    }

}
