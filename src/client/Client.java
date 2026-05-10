package client;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) {

        String host = "localhost";
        int port = 5000;

        try {

            Socket socket = new Socket(host, port);

            System.out.println("Conectado ao servidor!");

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(socket.getInputStream())
            );

            PrintWriter writer = new PrintWriter(
                    socket.getOutputStream(),
                    true
            );

            Scanner scanner = new Scanner(System.in);

            while (true) {

                System.out.println("\n===== MENU =====");
                System.out.println("1 - Login");
                System.out.println("2 - Adicionar prontuario");
                System.out.println("3 - Buscar prontuarios");
                System.out.println("4 - Logout");
                System.out.println("5 - Sair");

                String option = scanner.nextLine();

                switch (option) {

                    case "1":

                        System.out.print("CRM: ");
                        String crm = scanner.nextLine();

                        System.out.print("Senha: ");
                        String senha = scanner.nextLine();

                        writer.println(
                                "LOGIN;" + crm + ";" + senha
                        );

                        System.out.println(
                                reader.readLine()
                        );

                        break;



                    case "2":

                        System.out.print("Paciente: ");
                        String paciente = scanner.nextLine();

                        System.out.print("Diagnostico: ");
                        String diagnostico = scanner.nextLine();

                        System.out.print("Medicacao: ");
                        String medicacao = scanner.nextLine();

                        writer.println(
                                "ADD_RECORD;"
                                        + paciente + ";"
                                        + diagnostico + ";"
                                        + medicacao
                        );

                        System.out.println(
                                reader.readLine()
                        );

                        break;



                    case "3":

                        System.out.print("Paciente: ");
                        String nomePaciente = scanner.nextLine();

                        writer.println(
                                "GET_RECORDS;" + nomePaciente
                        );

                        String response;

                        while (!(response = reader.readLine()).equals("END")) {

                            System.out.println(response);

                            if(response.startsWith("ERRO")){
                                break;
                            }
                        }

                        break;



                    case "4":

                        writer.println("LOGOUT");

                        System.out.println(
                                reader.readLine()
                        );

                        break;



                    case "5":

                        socket.close();

                        System.out.println("Conexao encerrada.");

                        return;



                    default:

                        System.out.println("Opcao invalida");
                }
            }

        } catch (IOException e) {

            System.out.println("Erro ao conectar no servidor");
        }
    }
}