import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class BestGymEver {
    //Konstantvariabel kommer aldrig ändra sitt värde
    //Används för att spara medlems och träningsdata
    private static final String MEMBERS_FILE = "medlemsdata.txt";
    private static final String TRAINING_FILE = "traningsdata.txt";



    public static void main(String[] args) {

        List<Customer> customers = loadMembers();

        Scanner scanner = new Scanner(System.in);
        System.out.println("Skriv in kundens personnummer eller namn;");


        String input = scanner.nextLine();
        //hittar kunden i medlemslistan och kontrollerar medlems status
        Customer customer = findCustomer(customers, input);

        if (customer != null) {
            checkMembershipStatus(customer);
        } else {
            System.out.println("Kunden finns inte i systemet.");
        }
    }
    //överbelast metod så inte listorna krockar när jag kör
    //tester
    public static List<Customer> loadMembers() {
        return loadMembers(MEMBERS_FILE);
    }



    // Metod för att läsa in medlemmar från filen
    // gör den til static så att metoden tillhör klassen
    // i sig och inte ett objekt av klassen.
    //Lista som returnera en lista med objekt av typen Customer
    //instansering av en arraylist skapar helt enkelt en tom lista
    //som kommer att fyllas med Customer-objekt när metoden körs
    public static List<Customer> loadMembers(String filePath) {
        List<Customer> customers = new ArrayList<>();
       //använder try-catch för hantering av möjliga fel under filinläsning
        //string line > tilldelar rad från fil till en line
        //while loop > läser varje rad från fil tills de inte längre finns fler rader
        //substring personnumer första 10 tecken
        //substring namn extraherar namnet efter 12 tecken
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String personnummer = line.substring(0, 10);
                String name = line.substring(12);
                //omvandlar betalningsdatum i filen till LocalDate
                LocalDate lastPayment = LocalDate.parse(reader.readLine());
                System.out.println("Läser in kund: " + name + " med personnummer: " + personnummer);
                System.out.println("Betalningsdatum: " + lastPayment);

                customers.add(new Customer(name, personnummer, lastPayment));
            }
        //ifall filen inte finns och programmet inte körs i en test miljö
        } catch (NoSuchFileException e) {
            if (!isTestEnvironment()) {
                System.err.println("Filen hittades inte: " + Paths.get(filePath).toAbsolutePath());
            }


        //fångar alla andra typer, tex problem vid läsning av filrelaterade fel
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Fel vid läsning av filen; " + Paths.get(filePath).toAbsolutePath());



        }
        return customers;
    }
    //används för identifiera om programmet körs i en test miljö och undvika utskrifter vid tester.
    private static boolean isTestEnvironment() {
        return System.getProperty("java.class.path").contains("junit");
    }
    //söker efter en kund i listan baserat på namn eller personnummer via inmatning av användare
    private static Customer findCustomer(List<Customer> customers, String input) {
        for (Customer customer : customers) {
            if (customer.getName().equalsIgnoreCase(input) || customer.getPersonnummer().equals(input)) {
                return customer;
            }
        }
        return null;
    }
    //kollar om kund är en nuvurande eller före detta medlem
    //ifall kund är nuvurande sparas träningsdata
    private static void checkMembershipStatus(Customer customer) {
        LocalDate today = LocalDate.now();
        LocalDate lastPayment = customer.getLastPayment();



        if (lastPayment.isAfter(today.minusYears(1))) {
            System.out.println(customer.getName() + " är en nuvurande medlem.");
            saveTrainingData(customer);
        } else {
            System.out.println(customer.getName() + " är en före detta medlem.");
        }
    }
    //sparar träningsinformation i trainingsdata.txt
    //string entry sparar data för sedan genom bufferedwriter skriva data till filen
    //append för att göra så att ny data läggs till i slutet av filen utan ersätta befintlig data
    //create skapar filen om den inte redan finns.
    //exception ifall inte filen kan öppnas eller sparas, skrivas till
    private static void saveTrainingData (Customer customer) {
        String entry = customer.getPersonnummer() + ", " + customer.getName() + ", " + LocalDate.now() + "\n";
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(TRAINING_FILE), StandardOpenOption.APPEND, StandardOpenOption.CREATE)) {
            writer.write(entry);
            System.out.println("Träningsdata sparad.");
        } catch (IOException e) {
            System.err.println("Fel vid sparning av träningsdata; " + e.getMessage());
        }
    }
    //används i tester för att kontrollera om kunden är en nuvurande medlem baserat på ett referensdatum
    protected static boolean isCurrentMember(Customer customer, LocalDate referenceDate) {
        LocalDate lastPayment = customer.getLastPayment();
        return lastPayment.isAfter(referenceDate.minusYears(1));
    }







}