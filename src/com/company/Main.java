package com.company;

import java.util.*;

public class Main {

    public static void main(String[] args) {


        final int winningScore = 100;
        int score = 0;
        //creates arraylists for players and decks
        ArrayList stockDeck = new ArrayList();
        ArrayList discardDeck = new ArrayList();
        ArrayList myMeld = new ArrayList();
        ArrayList pcMeld = new ArrayList();
        ArrayList<ArrayList> meld = new ArrayList<ArrayList>();

        String card = "";
        Scanner sc = new Scanner(System.in);

        boolean gameOver = false;
        boolean firstTime = true;

        //figures out if you want to start the game (just 2 player for now)
        System.out.println("Would you like to begin?");
        String yesNo = sc.nextLine();
        yesNo.toLowerCase();
        Players computer = new Players("Bob");
        Players player = new Players("Me");

        //as long as no isn't registered it will draw you a random card and display it.
        try {
            if (yesNo != "n" && yesNo != "no") {
                for (int x = 0; x < 13; x++) {
                    for (int y = 0; y < 4; y++) {
                        card = fillDeck(x, y);
                        stockDeck.add(card);
                    }
                }

                // shuffle the deck before we begin.
                Collections.shuffle(stockDeck);

                //draws the initial 7 cards into the each hand
                for (int x = 0; x < 7; x++) {
                    Random rand = new Random();
                    //picks random number of the cards that are left in the "stock" pile.
                    int random = rand.nextInt(stockDeck.size());
                    //saves it temporarily
                    String temp = String.valueOf(stockDeck.get(random));
                    //adds the object to the players hand arraylist
                    player.addCard(temp);
                    //deletes the object from the "stock" pile of cards so it can't be drawn again.
                    stockDeck.remove(random);
                    //does the same for the computer
                    random = rand.nextInt(stockDeck.size());
                    temp = String.valueOf(stockDeck.get(random));
                    computer.addCard(temp);
                    stockDeck.remove(random);
                }
            } else {
                System.exit(0);
            }
            computer.displayHand(computer);
            player.displayHand(player);
        } catch (Exception e) {
            System.out.println("Exception: " + e);
        }


        //game begins.
        while (gameOver == false) {
            //first draw is different than the rest so this plays first
            while (firstTime == true) {
                //switches firstTime boolean
                firstTime = false;
                System.out.println("There are no cards in the discard pile. \n "
                        + "Hit 1 to draw a stock card!");
                int temp = sc.nextInt();
                if (temp == 1) {
                    drawFromStock(stockDeck, player);
                    discardCard(discardDeck, player);
                    computerTurn(computer, stockDeck, discardDeck);
                } else {
                    System.out.println("You can only draw from the stock deck on your first turn!");
                }
            }
            System.out.println("Pick a number:\n" + "1. Draw card from stock pile with " + stockDeck.size() + " cards \n" + "2. Draw card from discard pile.\n"
                    + "3. Display table/hands.\n" + "4. Lay down some cards.\n" + "5. Meld cards.\n");
            int temp = 0;
            temp = sc.nextInt();
            switch (temp) {
                case 1:
                    drawFromStock(stockDeck, player);
                    discardCard(discardDeck, player);
                    computerTurn(computer, stockDeck, discardDeck);
                    break;
                case 2:
                    drawFromDiscard(discardDeck, player);
                    discardCard(discardDeck, player);
                    computerTurn(computer, stockDeck, discardDeck);
                    break;
                case 3:
                    displayTable(stockDeck, discardDeck, player, computer);
                    break;
                case 4:
                    player.displayHand(player);
                    meldCard(meld,myMeld, player);
                    meldCard(meld,pcMeld, computer);
                    displayMeld(meld);
                    break;
                case 5:
                    meld(meld, myMeld, player);
            }
        }
        sc.close();
    }

    private static void meld(ArrayList<ArrayList> meld, ArrayList hand, Players player) {
        Scanner sc = new Scanner(System.in);
        String name = String.valueOf(player.getName());
        if ( name != "Bob") {
            System.out.println("How many cards do you want to meld?");
            int cardNum = sc.nextInt();
            System.out.println("Which meld pile do you want to use?");
            int meldPile= sc.nextInt() -1;
            player.displayHand(player);
            System.out.println("Meld Pile: " + meld.get(meldPile) + "\n");
            int pileSize = meld.get(meldPile).size();
            while (meld.get(meldPile).size() < pileSize + cardNum) {
                System.out.println("Select a card from your hand");
                int value = sc.nextInt() - 1;
                String card = String.valueOf(player.getHand().get(value));
                System.out.println("Do you want to add " + card + " to the front or the back of meld.\n"
                        + "Hit 1 for front or 2 for back.");
                int frontBack = sc.nextInt();
                if (frontBack == 1) {
                    meld.get(meldPile).add(0, card);
                    player.getHand().remove(card);
                    continue;
                } else {
                    meld.get(meldPile).add(card);
                    player.getHand().remove(card);
                    continue;
                }
            }
            System.out.println("Meld Pile: " + meld.get(meldPile) + "\n");
        }
    }

    private static void displayTable(ArrayList stockDeck, ArrayList discardDeck, Players player, Players computer) {
        computer.displayHand(computer);
        //TODO layCards method for meld
        int temp = discardDeck.size() - 1;
        System.out.println("\n\n");
        System.out.println("Stock Deck:");
        System.out.println(stockDeck.size() + " cards\n\n");
        System.out.println("Discard Deck:");
        System.out.println(discardDeck.get(temp) + "\n" + discardDeck.size() + " cards");
        //TODO more laycards method stuff
        player.displayHand(player);
    }


    private static void discardCard(ArrayList discardDeck, Players player) {
        String name = String.valueOf(player.getName());
        if (name != "Bob") {
            Scanner discardScanner = new Scanner(System.in);
            System.out.println("Enter the number that is to the left of the card you would like to discard.");
            int cardNum = discardScanner.nextInt() - 1;
            String card = String.valueOf(player.getHand().get(cardNum));
            player.removeCard(card);
            discardDeck.add(card);
            System.out.println(player.getName() + " discarded " + card);
            //computer ai is random so this plays for the computer to discard a card.
        } else {
            Random rand = new Random();
            int cardNum = rand.nextInt(player.getHand().size()) - 1;
            String card = String.valueOf(player.getHand().get(cardNum));
            player.removeCard(card);
            discardDeck.add(card);
        }
    }

    private static void drawFromStock(ArrayList stockDeck, Players player) {
        //saves it temporarily
        String temp = String.valueOf(stockDeck.get(stockDeck.size() - 1));
        //adds the object to the players hand arraylist
        player.addCard(temp);
        player.displayHand(player);
        stockDeck.remove(stockDeck.size() - 1);
    }

    private static void drawFromDiscard(ArrayList discardDeck, Players player) {
        //saves it temporarily
        String temp = String.valueOf(discardDeck.get(discardDeck.size() - 1));
        //adds the object to the players hand arraylist
        player.addCard(temp);
        player.displayHand(player);
        discardDeck.remove(discardDeck.size()-1);
    }

    //TODO add computer ai rather than just random

    private static void computerTurn(Players computer, ArrayList stockDeck, ArrayList discardDeck) {
        Random rand = new Random();
        boolean choice = rand.nextBoolean();

        if (choice == true) {
            System.out.println(computer.getName() + " draws from the stock pile.");
            drawFromStock(stockDeck, computer);
            discardCard(discardDeck, computer);
        }
        else if ( discardDeck.size() == 0) {
            System.out.println(computer.getName() + " draws from the stock pile.");
            drawFromStock(stockDeck, computer);
            discardCard(discardDeck, computer);


        } else if (choice == false){
            System.out.println(computer.getName() + " draws from the discard pile.");
            drawFromDiscard(discardDeck, computer);
            discardCard(discardDeck,computer);
        }



    }
    private static void meldCard(ArrayList<ArrayList> meld ,ArrayList hand, Players player) {
        Scanner sca = new Scanner(System.in);
        String name = String.valueOf(player.getName());
        if ( name != "Bob") {
            System.out.println("How many cards do you want to lay down?");
            int temp = sca.nextInt();
            while (hand.size() <= temp - 1) {

                System.out.println("Select a card from your hand");
                int value = sca.nextInt() - 1;
        String card = String.valueOf(player.getHand().get(value));
        hand.add(card);
        player.getHand().remove(card);
    }
    System.out.println(player.getName() + " 's meld" );
    System.out.print("[");
    for (Object i : hand) {
        System.out.print(String.valueOf(i) + "; ");
    }
    System.out.print("] \n");
    meld.add(hand);
}
        else {
            System.out.println(player.getName() + " 's meld" );
            Random compMeld = new Random();
            boolean choice = compMeld.nextBoolean();
            if (choice = true) {
                while (hand.size() <= 3) {
                    Random ran = new Random();
                    int temp = ran.nextInt(player.getHand().size() - 1);
                    String card = String.valueOf(player.getHand().get(temp));
                    while (hand.contains(card)) {
                        temp = ran.nextInt(player.getHand().size() - 1);
                        card = String.valueOf(player.getHand().get(temp));
                    }
                    hand.add(card);
                    player.getHand().remove(card);
                }

                System.out.print("[");
                for (Object i : hand) {
                    System.out.print(String.valueOf(i) + "; ");
                }
                System.out.print("]");
                meld.add(hand);
            }else {
                System.out.println(player.getName() + " didn't want to lay and cards down.");
            }
            }
        }


    private static void displayMeld(ArrayList<ArrayList> meld) {

        System.out.println("here is your present meld: \n");
        System.out.print("[ ");
        for (int y = 0; y < meld.size(); y++) {
            System.out.print(+(y + 1) + ". " + meld.get(y) + " | ");
        }
        System.out.println(" ]");
        System.out.println();
    }

    public static String fillDeck(int num, int suit) {
        String face = "";
        if (num == 0) {
            face = "Ace";
        } else if (num == 10) {
            face = "Jack";
        } else if (num == 11) {
            face = "Queen";
        } else if (num == 12) {
            face = "King";
        } else {
            face = Integer.toString(num);
        }

        String temp = "";
        if (suit == 0) {
            temp = "Hearts";
        } else if (suit == 1) {
            temp = "Clubs";
        } else if (suit == 2) {
            temp = "Diamonds";
        } else if (suit == 3) {
            temp = "Spades";
        }

        String card = face + " " + temp;
        return card;
    }
}

