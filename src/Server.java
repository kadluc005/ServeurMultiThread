import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Server extends Thread {
    private final Socket client;
    private final int clientNumber;

    public Server(Socket client, int clientNumber) {
        this.client = client;
        this.clientNumber = clientNumber;
    }

    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
             PrintWriter out = new PrintWriter(client.getOutputStream(), true)) {

            out.println("Bienvenue, vous êtes le client numéro " + clientNumber + ". Devinez le nombre secret entre 0 et 99 !");

            while (true) {
                if (ServeurMultiThread.isGameOver()) {
                    out.println("Le jeu est terminé. Un autre client a déjà trouvé le nombre secret.");
                    break;
                }

                // Lecture de la tentative du client
                String clientGuess = in.readLine();
                if (clientGuess == null || clientGuess.equalsIgnoreCase("exit")) {
                    out.println("Au revoir !");
                    System.out.println("Le client numéro " + clientNumber + " s'est déconnecté.");
                    break;
                }

                // Tentative de conversion en entier
                int guess;
                try {
                    guess = Integer.parseInt(clientGuess);
                } catch (NumberFormatException e) {
                    out.println("Entrée invalide. Veuillez entrer un nombre.");
                    continue;
                }

                int secretNumber = ServeurMultiThread.getSecretNumber();

                if (guess == secretNumber) {
                    ServeurMultiThread.endGame(clientNumber);  // Marquer la fin du jeu et annoncer le gagnant
                    out.println("Félicitations !!! Vous avez trouvé le nombre secret : " + secretNumber);
                    break;
                } else if (guess < secretNumber) {
                    out.println("Le nombre secret est supérieur à " + guess);
                } else {
                    out.println("Le nombre secret est inférieur à " + guess);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                client.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
