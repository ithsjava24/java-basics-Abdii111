package org.example;


import java.text.NumberFormat;
import java.util.*;

public class App {
    private static final int[] priser = new int[24];
    private Scanner scanner;

    public App(Scanner scanner) {
        this.scanner = scanner;
    }

    public static void main(String[] args) {
        Locale.setDefault(Locale.forLanguageTag("sv-SE"));
        App app = new App(new Scanner(System.in));
        app.running();
    }

    public void running() {
        boolean running = true;
        while (running) {
            displayMenu();
            String val = scanner.nextLine().trim().toLowerCase();

            switch (val) {
                case "1":
                    inmatning();
                    break;
                case "2":
                    minMaxMedel();
                    break;
                case "3":
                    sortera();
                    break;
                case "4":
                    laddningsTid();
                    break;
                case "e":
                    System.out.print("Avslutar programmet...\n");
                    running = false;
                    break;
                default:
                    System.out.print("Ogiltigt val, försök igen.\n");
            }
        }
    }

    private void displayMenu() {
        System.out.print("""
                Elpriser
                ========
                1. Inmatning
                2. Min, Max och Medel
                3. Sortera
                4. Bästa Laddningstid (4h)
                e. Avsluta
                """);
    }

    private void inmatning() {
        System.out.print("Ange elpriser för varje timme (i öre/kWh):\n");
        for (int i = 0; i < 24; i++) {
            int nästaTimme = (i + 1) % 24;
            System.out.printf("%02d - %02d: ", i, nästaTimme == 0 ? 24 : nästaTimme);
            priser[i] = Integer.parseInt(scanner.nextLine());
        }
    }

    private void minMaxMedel() {
        int minPris = Integer.MAX_VALUE;
        int maxPris = Integer.MIN_VALUE;
        int sum = 0;
        int minTimme = 0, maxTimme = 0;

        for (int i = 0; i < priser.length; i++) {
            if (priser[i] < minPris) {
                minPris = priser[i];
                minTimme = i;
            }
            if (priser[i] > maxPris) {
                maxPris = priser[i];
                maxTimme = i;
            }
            sum += priser[i];
        }

        double medelPris = sum / 24.0;

        System.out.printf("Lägsta pris: %02d-%02d, %d öre/kWh\n", minTimme, (minTimme + 1) % 24 == 0 ? 24 : (minTimme + 1) % 24, minPris);
        System.out.printf("Högsta pris: %02d-%02d, %d öre/kWh\n", maxTimme, (maxTimme + 1) % 24 == 0 ? 24 : (maxTimme + 1) % 24, maxPris);
        System.out.printf("Medelpris: %.2f öre/kWh\n", medelPris);
    }

    private void sortera() {
        List<int[]> timmePrisLista = new ArrayList<>();
        for (int i = 0; i < priser.length; i++) {
            timmePrisLista.add(new int[]{i, priser[i]});
        }

        timmePrisLista.sort((a, b) -> Integer.compare(b[1], a[1]));

        for (int[] par : timmePrisLista) {
            int timme = par[0];
            int pris = par[1];
            System.out.printf("%02d-%02d %d öre\n", timme, (timme + 1) % 24 == 0 ? 24 : (timme + 1) % 24, pris);
        }
    }

    private void laddningsTid() {
        int billigasteStart = 0;
        int billigastePrisSumma = Integer.MAX_VALUE;

        for (int i = 0; i <= priser.length - 4; i++) {
            int prisSumma = priser[i] + priser[i + 1] + priser[i + 2] + priser[i + 3];
            if (prisSumma < billigastePrisSumma) {
                billigastePrisSumma = prisSumma;
                billigasteStart = i;
            }
        }

        double medelPris = billigastePrisSumma / 4.0;
        NumberFormat commaForce = NumberFormat.getNumberInstance(Locale.forLanguageTag("sv-SE"));
        System.out.printf("Påbörja laddning klockan %02d\n", billigasteStart);
        System.out.printf("Medelpris 4h: %s öre/kWh\n", commaForce.format(medelPris));
    }
}
