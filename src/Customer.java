import java.time.LocalDate;
import java.time.LocalDateTime;

public class Customer {
    private String name;
    private String personnummer;
    private LocalDate lastPayment;


    //Konstruktor för att skapa ett nytt Customer-objekt
    public Customer(String name, String personnummer, LocalDate lastPayment){
        this.name = name;
        this.personnummer = personnummer;
        this.lastPayment = lastPayment;
    }
    //hämta data från objekt genom getter-metoden.
    public String getName() {
        return name;
    }

    public String getPersonnummer() {
        return personnummer;
    }

    public LocalDate getLastPayment() {
        return lastPayment;
    }


}

