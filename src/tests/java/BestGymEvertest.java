import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;



class BestGymEvertest {

    @Test
    void testLoadMembers() {
        String filePath = "C:\\Users\\Admin\\Desktop\\BestGymEver\\resources\\medlemsdata.txt";
        List<Customer> customers = BestGymEver.loadMembers(filePath);
        assertEquals(14, customers.size());
    }


    @Test
    public void testCheckMembershipStatusWithFixedDate() {
        // Fixa datumet så att testerna alltid ger samma resultat
        LocalDate fixedDate = LocalDate.of(2024, 1, 1);

        // Skapa kunder med olika betalningsdatum
        Customer currentCustomer = new Customer("Fritjoff Flacon", "7911061234", fixedDate.minusMonths(6));
        Customer formerCustomer = new Customer("Chamade Coriola", "8512021234", fixedDate.minusYears(2));

        // Kontrollera att medlemsstatus baseras på det fasta datumet
        assertTrue(BestGymEver.isCurrentMember(currentCustomer, fixedDate));  // Ska vara nuvarande medlem
        assertFalse(BestGymEver.isCurrentMember(formerCustomer, fixedDate));  // Ska vara före detta medlem
    }

}