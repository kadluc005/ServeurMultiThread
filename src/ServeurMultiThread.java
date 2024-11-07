import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

public class ServeurMultiThread extends Thread {
    private static volatile boolean gameOver = false;  // Indicateur de fin de jeu
    private static int secretNumber;                   // Nombre secret pour tous les clients
    private int clientCount = 0;

    public void run() {
        try (ServerSocket ss = new ServerSocket(3001)) {
            // Génération d'un nombre secret unique pour tous les clients
            secretNumber = new Random().nextInt(100);
            System.out.println("Le nombre secret généré est : " + secretNumber);

            System.out.println("En attente de connexions...");
            while (true) {
                Socket clientSocket = ss.accept();
                clientCount++;
                System.out.println("Connexion acceptée pour le client numéro : " + clientCount);
                new Server(clientSocket, clientCount).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int getSecretNumber() {
        return secretNumber;
    }

    public static boolean isGameOver() {
        return gameOver;
    }

    public static synchronized void endGame(int winnerClientNumber) {
        if (!gameOver) {  // Assure que le message n'est envoyé qu'une fois
            gameOver = true;
            System.out.println("Le client numéro " + winnerClientNumber + " a gagné ! Le jeu est terminé.");
        }
    }
}
