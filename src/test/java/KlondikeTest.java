import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class KlondikeTest {

    private Klondike klondike;

    @Before
    public void setUp() throws Exception {
        klondike = new Klondike();
    }

    @Test
    public void gameShouldHaveSevenTableauPiles(){
        assertThat(klondike.getTableauPiles()).hasSize(7);
    }

    @Test
    public void gameShouldHaveFourFoundationPiles(){
        assertThat(klondike.getFoundationPiles()).hasSize(4);
    }

    @Test
    public void gameShouldHaveAStockPile(){
        assertThat(klondike.getStockPile()).isNotNull();
    }

    @Test
    public void newGameHasAShuffledStockPile(){
        assertThat(klondike.getStockPile().deal()).isNotEqualTo(new Card(1,Suit.CLUBS));
    }

    @Test
    public void gameShouldHaveAWastePile(){
        assertThat(klondike.getWastePile()).hasSize(0);
    }

    @Test
    public void dealShouldSetUpTheTableauPiles(){
        klondike.deal();

        for (int i = 0; i < 7; i++) {
            assertThat(klondike.getTableauPile(i)).hasSize(i+1);
        }
    }

    @Test
    public void generateStringRepresentation(){
        klondike.deal();

        assertThat(klondike.toString()).matches(
                "Stock and Waste:\n" +
                "XXX ___\n" +
                "\n" +
                "Foundation:\n" +
                "___\n" +
                "___\n" +
                "___\n" +
                "___\n" +
                "\n" +
                "Tableaux:\n" +
                "...\n" +
                "... ...\n" +
                "... ... ...\n" +
                "... ... ... ...\n" +
                "... ... ... ... ...\n" +
                "... ... ... ... ... ...\n" +
                "... ... ... ... ... ... ...\n");
    }

    //TODO NOT REALLY A TEST!!!
    @Test
    public void playGame(){
        klondike.deal();
        System.out.println(klondike);
    }
}