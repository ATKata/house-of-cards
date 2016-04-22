import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;
import java.util.stream.IntStream;

public class Deck {

    private Stack<Card> cards = new Stack<>();

    public Stack<Card> getCards() {
        return cards;
    }

    public Deck() {
        for (Suit suit : Suit.values()) {
            IntStream.range(1,14).forEach(value -> cards.add(new Card(value, suit)));
        }

    }

    public void shuffle() {
        Collections.shuffle(cards);
    }
}
