import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.IntStream;

public class Deck {

    private LinkedList<Card> cards = new LinkedList<>();

    public List<Card> getCards() {
        return cards;
    }

    public Deck() {
        for (Suit suit : Suit.values()) {
            IntStream.range(1,14).forEach(value -> cards.addLast(new Card(value, suit)));
        }
    }

    public Deck shuffle() {
        Collections.shuffle(cards);
        return this;
    }

    public Card deal() {
        return cards.pop();
    }

    public int getSize() {
        return cards.size();
    }
}
