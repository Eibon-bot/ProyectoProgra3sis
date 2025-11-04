package pos.logic;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Server {
    ServerSocket ss;
    List<Worker> workers;

    public Server() {
        try {
            ss = new ServerSocket(Protocol.PORT);
            workers = Collections.synchronizedList(new ArrayList<Worker>());

            System.out.println("Servidor iniciado...");
        } catch (IOException ex) { System.out.println(ex);}
    }
    public void run() {
        Service service = new Service();
        boolean continuar = true;
        Socket s;
        Worker worker;
        String sid;
        while (continuar) {
            try {
                s = ss.accept();
                System.out.println("Conexion Establecida...");
                ObjectOutputStream os = new ObjectOutputStream(s.getOutputStream());
                ObjectInputStream is = new ObjectInputStream(s.getInputStream());
                int type= is.readInt();
                switch (type) {
                    case Protocol.SYNC:
                        sid=s.getRemoteSocketAddress().toString();
                        System.out.println("SYNCH: "+sid);
                        worker = new Worker(this, s, os, is, sid, Service.instance());
                        workers.add(worker);
                        System.out.println("Quedan: " + workers.size());
                        worker.start();
                        os.writeObject(sid);
                        break;
                    case Protocol.ASYNC:
                        sid=(String)is.readObject();
                        System.out.println("ASYNCH: "+sid);
                        join(s,os,is,sid);
                        break;
                }
                os.flush();
            }
            catch (IOException | ClassNotFoundException ex) {
                System.out.println(ex);
            }
        }
    }





    public void remove(Worker w) {
        workers.remove(w);
        System.out.println("Quedan: " +workers.size());
    }
    public void connectedUsers(Worker from){
        System.out.println("connectedUsers from=" + from.getUsuario().getId());
        List<String> ids = new ArrayList<>();
        for (Worker w : workers) {
            if (!Objects.equals(w.getUsuario().getId(), from.getUsuario().getId()) && !ids.contains(w.getUsuario().getId())) {
                from.sendNotification(Protocol.USER_JOINED, w.getUsuario());
                ids.add(w.getUsuario().getId());

            }
        }
    }



    public void join(Socket as, ObjectOutputStream aos, ObjectInputStream ais, String sid) {
        for (Worker w: workers) {
            if (w.sid.equals(sid)) {
                if (w.aos != null) {
                    System.out.println("Ignorando ASYNC duplicado para sid " + sid);
                    try { as.close(); } catch (IOException ignored) {}
                    return;
                }
                w.setAs(as, aos, ais);
                System.out.println("JOIN sid=" + sid + " user? " + (w.getUsuario() != null));
                if (w.getUsuario() != null) connectedUsers(w);
                break;
            }
        }
    }




    public void notifyUserConnected(Worker from, Usuario user) {
        System.out.println("Usuario conectado: " + user.getId());

        boolean alreadyConnected = workers.stream()
                .filter(w -> w != from)
                .anyMatch(w -> w.getUsuario().getId().equals(user.getId()));

        if (!alreadyConnected) {
            for (Worker w : workers) {
                if (!Objects.equals(w.getUsuario().getId(), user.getId())) {
                    w.sendNotification(Protocol.USER_JOINED, user);
                }
            }
        }
    }

    public void notifyUserDisconnected(Worker from) {
        workers.remove(from);
        System.out.println("Usuario desconectado: " +from.getUsuario().getId() );

        boolean stillConnected = workers.stream()
                .anyMatch(w -> w.getUsuario().equals(from.getUsuario()));

        if (!stillConnected) {
            for (Worker w : workers) {
                w.sendNotification(Protocol.USER_LEFT, from.getUsuario());
            }
        }

    }
    public void sendMessage(Usuario from, Usuario destino, String texto) {

        for (Worker w : workers) {
            if (w.getUsuario().getId().equals(destino.getId())) {
                w.sendMessage(Protocol.DELIVER_MESSAGE, from , texto);
            }
        }
    }

}