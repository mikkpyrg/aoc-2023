import java.util.*;

public class Day07 extends Solver {
    @Override
    // compare hand of cards and order them by rank, rank multiply by bet
    public Object solve() {
        var sum = 0;
        var hands = new ArrayList<CamelCardHand>();

        for (String line : getDataLines()) {
            var split = line.split(" ");
            hands.add(new CamelCardHand(split[0], Integer.parseInt(split[1]), false));
        }

        hands.sort(new Comparator<CamelCardHand>() {
            @Override
            public int compare(CamelCardHand o1, CamelCardHand o2) {
                var hand1 = o1.hand;
                var hand2 = o2.hand;
                return o1.handType == o2.handType
                    ? o1.compareHands(o2.hand)
                    : o1.handType - o2.handType;
            }
        });

        for (int i = 0; i < hands.size(); i++) {
            var hand = hands.get(i);
            sum += hand.bet * (i + 1);
            System.out.printf("Hand %s: %s\n", hand.hand, hand.displayHandType());
        }

        return sum;
    }


    @Override
    // same as 1, only now J can be any other card(to make hand better) and it's value is lowest.
    // Going by current rule set, it means that J will be counted as the next most numerous card in hand
    public Object solve2() {
        var sum = 0;
        var hands = new ArrayList<CamelCardHand>();

        for (String line : getDataLines()) {
            var split = line.split(" ");
            hands.add(new CamelCardHand(split[0], Integer.parseInt(split[1]), true));
        }

        hands.sort(new Comparator<CamelCardHand>() {
            @Override
            public int compare(CamelCardHand o1, CamelCardHand o2) {
                var hand1 = o1.hand;
                var hand2 = o2.hand;
                return o1.handType == o2.handType
                        ? o1.compareHandsJ(o2.hand)
                        : o1.handType - o2.handType;
            }
        });

        for (int i = 0; i < hands.size(); i++) {
            var hand = hands.get(i);
            sum += hand.bet * (i + 1);
            System.out.printf("Hand %s: %s\n", hand.hand, hand.displayHandType());
        }

        return sum;
    }


}

class CamelCardHand {
    public CamelCardHand(String hand, int bet, boolean includeJSpecial) {
        this.hand = hand;
        this.bet = bet;
        this.handType = getHandType(hand, includeJSpecial);
    }

    public String hand;
    public int bet;
    public int handType;

    public String displayHandType() {
        return switch (handType) {
            case 0 -> "High card";
            case 1 -> "One pair";
            case 2 -> "Two pair";
            case 3 -> "Three of a kind";
            case 4 -> "Full house";
            case 5 -> "Four of a kind";
            case 6 -> "Five of a kind";
            default -> "Error";
        };
    }

    public int cardValue(char card) {
        return switch (card) {
            case '2' -> 2;
            case '3' -> 3;
            case '4' -> 4;
            case '5' -> 5;
            case '6' -> 6;
            case '7' -> 7;
            case '8' -> 8;
            case '9' -> 9;
            case 'T' -> 10;
            case 'J' -> 11;
            case 'Q' -> 12;
            case 'K' -> 13;
            case 'A' -> 14;
            default -> 0;
        };
    }

    public int cardValueJ(char card) {
        return switch (card) {
            case 'J' -> 1;
            case '2' -> 2;
            case '3' -> 3;
            case '4' -> 4;
            case '5' -> 5;
            case '6' -> 6;
            case '7' -> 7;
            case '8' -> 8;
            case '9' -> 9;
            case 'T' -> 10;
            case 'Q' -> 12;
            case 'K' -> 13;
            case 'A' -> 14;
            default -> 0;
        };
    }

    public int compareHands(String anotherHand) {
        for (int i = 0; i < 5; i++) {
            if (hand.charAt(i) == anotherHand.charAt(i)) {
                continue;
            } else {
                return cardValue(hand.charAt(i)) - cardValue(anotherHand.charAt(i));
            }
        }
        return 0;
    }

    public int compareHandsJ(String anotherHand) {
        for (int i = 0; i < 5; i++) {
            if (hand.charAt(i) == anotherHand.charAt(i)) {
                continue;
            } else {
                return cardValueJ(hand.charAt(i)) - cardValueJ(anotherHand.charAt(i));
            }
        }
        return 0;
    }

    private int getHandType(String hand, boolean considerJ) {
        var uniqueCards =  new HashMap<Character, Integer>();
        var hasJ = false;
        for (char card : hand.toCharArray()) {
            if (card == 'J') {
                hasJ = true;
            }

            if (uniqueCards.containsKey(card)) {
                uniqueCards.put(card, uniqueCards.get(card) + 1);
            } else {
                uniqueCards.put(card, 1);
            }
        }

        if (considerJ && hasJ) {
            var JCount = uniqueCards.get('J');
            uniqueCards.remove('J');
            char highestCard = 'J';
            int cardCount = 0;

            for (char key: uniqueCards.keySet()) {
                if (uniqueCards.get(key) > cardCount) {
                    highestCard = key;
                    cardCount = uniqueCards.get(key);
                }
            }

            uniqueCards.put(highestCard, cardCount + JCount);
        }

        if (uniqueCards.size() == 5) {
            // high card
            return 0;
        } else if (uniqueCards.size() == 1) {
            // five of a kind
            return 6;
        } else if (uniqueCards.size() == 2) {
            var amountOfCards = uniqueCards.entrySet().iterator().next().getValue();
            if (amountOfCards == 2 || amountOfCards == 3) {
                // full house
                return 4;
            } else {
                // four of a kind
                return 5;
            }
        } else if (uniqueCards.size() == 4) {
            // one pair
            return 1;
        } else {
            for (char key: uniqueCards.keySet()) {
                if (uniqueCards.get(key) == 3) {
                    // three of a kind
                    return 3;
                }
            }
            // two pairs
            return 2;
        }
    }
}
