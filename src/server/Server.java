package server;

import model.*;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

class Server {

    Map<String, Doctor> medicos = new ConcurrentHashMap<>();

    Map<String, List<MedicRecord>> prontuarios =
            new ConcurrentHashMap<>();


    public Server(){

        medicos.put(
                "crm123",
                new Doctor("Joao", "crm123", "123" )
        );

        medicos.put(
                "crm999",
                new Doctor("Maria", "crm999", "Joao")
        );
    }



    public Doctor parseCommand(
            String line,
            PrintWriter writer,
            Doctor loggedDoctor
    ) {

        String[] parts = line.split(";");

        String command = parts[0];

        switch (command) {

            case "LOGIN":

                if(parts.length < 3){
                    writer.println("ERRO;Parametros invalidos");
                    return loggedDoctor;
                }

                String crm = parts[1];
                String senha = parts[2];

                Doctor medicoEncontrado = medicos.get(crm);

                if(medicoEncontrado == null){
                    writer.println("ERRO;CRM nao encontrado");
                    return loggedDoctor;
                }

                if(medicoEncontrado.getSenha().equals(senha)){

                    writer.println("OK;Login realizado");

                    return medicoEncontrado;
                }

                writer.println("ERRO;Senha incorreta");

                return loggedDoctor;



            case "ADD_RECORD":

                if(loggedDoctor == null){
                    writer.println("ERRO;Nao autenticado");
                    return null;
                }

                if(parts.length < 4){
                    writer.println("ERRO;Parametros invalidos");
                    return loggedDoctor;
                }

                String paciente = parts[1];
                String diagnostico = parts[2];
                String medicacao = parts[3];

                MedicRecord record = new MedicRecord(
                        paciente,
                        diagnostico,
                        medicacao
                );

                prontuarios
                        .computeIfAbsent(
                                paciente,
                                k -> new CopyOnWriteArrayList<>()
                        )
                        .add(record);

                writer.println("OK;Prontuario adicionado");

                return loggedDoctor;



            case "GET_RECORDS":

                if(loggedDoctor == null){
                    writer.println("ERRO;Nao autenticado");
                    return null;
                }

                if(parts.length < 2){
                    writer.println("ERRO;Paciente nao informado");
                    return loggedDoctor;
                }

                String nomePaciente = parts[1];

                List<MedicRecord> records =
                        prontuarios.get(nomePaciente);

                if(records == null || records.isEmpty()){

                    writer.println("ERRO;Nenhum prontuario encontrado");

                    return loggedDoctor;
                }

                for(MedicRecord r : records){
                    writer.println(r.toString());
                }

                writer.println("END");

                return loggedDoctor;



            case "LOGOUT":

                writer.println("OK;Logout realizado");

                return null;



            default:

                writer.println("ERRO;Comando desconhecido");

                return loggedDoctor;
        }
    }



    public void handleClient(Socket socket) {

        Doctor loggedDoctor = null;

        System.out.println("Cliente conectado");

        try {

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(socket.getInputStream())
            );

            PrintWriter writer = new PrintWriter(
                    socket.getOutputStream(),
                    true
            );

            String line;

            while ((line = reader.readLine()) != null) {

                System.out.println("Recebido: " + line);

                loggedDoctor = parseCommand(
                        line,
                        writer,
                        loggedDoctor
                );
            }

        } catch (IOException e) {

            System.out.println("Cliente desconectado");
        }
    }



    public static void main(String[] args) {

        int port = 5000;

        Server serverClient = new Server();

        ExecutorService executor =
                Executors.newFixedThreadPool(12);

        try {

            ServerSocket server =
                    new ServerSocket(port);

            System.out.println("Rodando na porta " + port);

            while (true) {

                Socket socket = server.accept();

                executor.execute(() -> {
                    serverClient.handleClient(socket);
                });
            }

        } catch (IOException e) {

            throw new RuntimeException(e);
        }
    }
}