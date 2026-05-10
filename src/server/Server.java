package server;
import model.*;

//  loop de accept + thread por cliente + parser de comandos


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

class Server{
    Map<String, Doctor> medicos = new HashMap<>();
    Map<String, List<MedicRecord>> prontuarios = new ConcurrentHashMap<>();

    public static void main(String[] args){
        int port = 5000;
        // criar server numa porta
        try{
            ServerSocket server = new ServerSocket(port);
            System.out.println("Rodando na porta " + port);
            while(true){
                Socket socket = server.accept();

            }

        } catch (IOException e) {
            throw new RuntimeException(e);

        }


        // loop infinito que espera o cliente conectar

        // retornar um socket para o cliente que conectar


        // uma thread volta para a principal para continuar aceitando clientes
    }
}