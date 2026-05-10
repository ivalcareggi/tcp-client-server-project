package server;
import model.*;

//  loop de accept + thread por cliente + parser de comandos


import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class Server{
    Map<String, Doctor> medicos = new HashMap<>();
    Map<String, List<MedicRecord>> prontuarios = new ConcurrentHashMap<>();

    public void handleClient(Socket socket){
        System.out.println("Cliente conectado");
        try{
        InputStreamReader Reader = new InputStreamReader(socket.getInputStream());
        BufferedReader reader = new BufferedReader(Reader);
        PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
        String line;
        while ((line = reader.readLine()) != null) {
                System.out.println(line);
                // adicionar parser aqui
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public static void main(String[] args){
        int port = 5000;
        Server serverClient = new Server();
        ExecutorService executor = Executors.newFixedThreadPool(12);
        // criar server numa porta
        try{
            ServerSocket server = new ServerSocket(port);
            System.out.println("Rodando na porta " + port);
            while(true){
                Socket socket = server.accept();
                executor.execute(() -> {
                    serverClient.handleClient(socket);

                });


            }

        } catch (IOException e) {
            throw new RuntimeException(e);

        }


        // loop infinito que espera o cliente conectar

        // retornar um socket para o cliente que conectar


        // uma thread volta para a principal para continuar aceitando clientes
    }
}