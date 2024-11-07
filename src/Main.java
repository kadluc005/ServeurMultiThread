public class Main extends Thread{
    public static void main(String[] args) {

        System.out.println("Hello world!");
        new ServeurMultiThread().start();
    }
}