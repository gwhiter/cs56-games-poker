package edu.ucsb.cs56.projects.games.poker;

import java.io.Serializable;
import java.util.*;
import java.util.ArrayList;

/**
 * Class that Compares Hands between Players
 */
public class CompareHands implements Serializable{
    
    private ArrayList<Card> cardHand1;
    private ArrayList<Card> cardHand2;
    
    private int player1Value = 0;
    private int player2Value = 0;    

    /**
     * Compare Hands Constructor
     * @param player1 first player
     * @param player2 second player
     * @param table the cards that are on the table
     * 
     */    
    public CompareHands(Player player1, Player player2, TableCards table) {
	cardHand1 = new ArrayList<Card>();
	cardHand1.addAll(player1.getHand());
	cardHand1.addAll(table.getFlopCards());
	cardHand1.add(table.getTurnCard());
	cardHand1.add(table.getRiverCard());

	cardHand2 = new ArrayList<Card>();
	cardHand2.addAll(player2.getHand());
	cardHand2.addAll(table.getFlopCards());
	cardHand2.add(table.getTurnCard());
	cardHand2.add(table.getRiverCard());
    }

    /**
     * Returns 1 if "Player 1" hand is better than "Player 2" hand
     * Returns 0 if the opposite.
     * Returns 2 if exact tie
     * @return int 
     */
    public int compareHands(){
	cardHand1 = findBestOf5WithRank(cardHand1);
	cardHand2 = findBestOf5WithRank(cardHand2);

	player1Value = cardHand1.get(6).getValue();	
	cardHand1.remove(player1Value, "RANK");		
	player2Value = cardHand2.get(6).getValue();
	cardHand2.remove(player2Value, "RANK");
	
	//player1Value = calculateValue(cardHand1);
	//player2Value = calculateValue(cardHand2);
	
	if(player1Value>player2Value) //Player 1 has the better hand
	    return 1;
	else if(player1Value<player2Value) //Player 2 has the better hand
	    return 0;
	else { //Both players have the same type of hand
	    return resolveTie(cardHand1, cardHand2);

	    /*int yourHandValue = sameHandUpdated(cardHand1, 1);
	    int otherHandValue = sameHandUpdated(cardHand2, 1);
	    if(yourHandValue>otherHandValue)
		return 1;
	    else if (yourHandValue<otherHandValue)
		return 0;
	    else {
		Card yourmaxCard = cardHand1.get(0);
		Card othermaxCard = cardHand2.get(0);
		if (yourmaxCard.getValue()<cardHand1.get(1).getValue())
		    yourmaxCard = cardHand1.get(1);
		if (othermaxCard.getValue()<cardHand2.get(1).getValue())
		    othermaxCard = cardHand2.get(1);

		if (yourmaxCard.getValue()>othermaxCard.getValue())
		    return 1;
		else if (yourmaxCard.getValue()<othermaxCard.getValue())
		    return 0;
		
		else {
		    yourHandValue = sameHandUpdated(cardHand1, 2);
		    otherHandValue = sameHandUpdated(cardHand2, 2);
		    if(yourHandValue>otherHandValue)
			return 1;
		    else if (yourHandValue<otherHandValue)
			return 0;
		    else {
			Card yourminCard = cardHand1.get(0);
			Card otherminCard = cardHand2.get(0);
			if (yourminCard.getValue()>cardHand1.get(1).getValue())
			    yourminCard = cardHand1.get(1);
			if (otherminCard.getValue()>cardHand2.get(1).getValue())
			    otherminCard = cardHand2.get(1);
			
			if (yourminCard.getValue()>otherminCard.getValue())
			    return 1;
			else if (yourminCard.getValue()<otherminCard.getValue())
			    return 0;
			else
			return 2;// Only arrives here if cards are exactly the same		
			}
			}
			}		*/
	}
    }

    /**
     *This method takes in 7 cards and figures out which 5 returns the best hand.
     * @param c1 this is the hand containing 7 cards
     * @return hand this hand has the 5 best cards. The 6th and final card represents the rank.
     * the value of the rank card is the rank. the suit is the string "RANK".
     */
    public ArrayList<Card> findBestOf5WithRank(ArrayList<Card> hand) {
	Card firstCard = new Card(0, "FIRST");
	Card secondCard = new Card(0, "SEC");
	int locOne = 0;
	int locTwo = 0;
	int max = 0;
	int contender = 0;
	int maxPlace1 = 0;
	int maxPlace2 = 0;
	hand = sortCardHand(hand);
	for (Card c : hand) {
	    firstCard = c;
	    hand.remove(c); 
	    for (Card d : hand) {
	        secondCard = d;
		hand.remove(d);
		contender = calculateValue(hand);
		if (contender > max) {
		    max = contender;
		    maxPlace1 = locOne;
		    maxPlace2 = locTwo;
     		}
		if (max == 8)
		    return hand;
		hand.add(locTwo, secondCard);
		locTwo = locTwo + 1;
	    }
	    hand.add(locOne, firstCard);
	    locOne = locOne + 1;
	}
	hand.remove(maxPlace1);
	hand.remove(maxPlace2);
	Card rank = new Card(max, "RANK");
	hand.add(rank);
	return hand;
    }

    /**
     *This method is used if both hands are the same type
     *
     *
     */
    private int resolveTie(ArrayList<Card> c1, ArrayList<Card> c2) {
	int max1 = c1.get(5).getValue();
	int max2 = c2.get(5).getValue();
	if (max1 > max2)
	    return 1;
	else
	    return 0;
    }

    /**
     * This method is used if both hands are the same type
     * Hand is either this or the otherhand
     * recursion: Either 1 if called 1st, 2 if called 2nd
     * func won't work as intended if given a different number
     * @param c this is the array of cards
     * @param recursion MUST BE A 1 OR A 2 IF YOU WANT THIS TO WORK 
     * @return int returns an integer
     */
    private int sameHandUpdated(ArrayList<Card> c, int recursion) {
	ArrayList<Card> cards_tmp = new ArrayList<Card>();
	Card yourCard = c.get(0);
	if (recursion==1) {
	    if (c.get(1).getValue()>yourCard.getValue())
	     	yourCard = c.get(1);
	    cards_tmp.add(yourCard);
	    for (int i=2; i<c.size(); i++)
		cards_tmp.add(c.get(i));
	    return calculateValue(cards_tmp);
	}
	if (recursion==2) {
	    if (c.get(1).getValue()<yourCard.getValue())
		yourCard = c.get(1);
	    cards_tmp.add(yourCard);
	    for(int i=2; i<c.size(); i++)
		cards_tmp.add(c.get(i));
	    return calculateValue(cards_tmp);
	}
	return -1; // Should only pass in 1 or 2 in parameter
    }

    /**
     * Method that finds the type of hand the player has
     * @param player the cards that belong to the players
     * @return int
     */
    
    public int calculateValue(ArrayList<Card> player) {
	if(isStraightFlush(player))
	    return 8;
	else if(isFourOfAKind(player))
	    return 7;
	else if(isFullHouse(player))
	    return 6;
	else if(isFlush(player))
	    return 5;
	else if(isStraight(player))
	    return 4;
	else if(isThreeOfAKind(player))
	    return 3;
	else if(isTwoPair(player))
	    return 2;
	else if(isOnePair(player))
	    return 1;
	else
	    return 0;
    }

    /**
     * @param player the cards belonging to the player
     * @return sortedHand
    */
    public ArrayList<Integer> sortHand(ArrayList<Card> player) {
	ArrayList<Integer> sortedHand=new ArrayList<Integer>();
	for(int i=0;i<player.size();i++) {
	    sortedHand.add(player.get(i).getValue());
	}
	Collections.sort(sortedHand);
	return sortedHand;
    }

    /**
     *Method sorts the hand
     * @param player the hand to be sorted
     * @return sortedCards NOT an arrayList of integers
     */
    public ArrayList<Card> sortCardHand(ArrayList<Card> player) {
	ArrayList<Integer> ranks = new ArrayList<Integer>();
	ranks = sortHand(player);
	ArrayList<Card> sortedCards = new ArrayList<Card>();
	int x = 0;
	for (Integer i: ranks) {
	    while ( i != player.get(x).getValue() )
		x = x + 1;
	    sortedCards.add(player.get(x));
	    player.remove(x);
	    x = 0;			   
	}
	return sortedCards;
    }
    
    /**
       Returns boolean for if the hand is a straight flush
       * @param player Cards belonging to the player
       * @return boolean
    */
    private boolean isStraightFlush(ArrayList<Card> player){
	int straightFlushCounter=0;
	int spadeCounter=0;
	int cloverCounter=0;
	int heartCounter=0;
	int diamondCounter=0;
	ArrayList<Integer> spades=new ArrayList<Integer>();
	ArrayList<Integer> clovers=new ArrayList<Integer>();
	ArrayList<Integer> diamonds=new ArrayList<Integer>();
	ArrayList<Integer> hearts=new ArrayList<Integer>();
	for(Card c: player){
	    if(c.getSuit()=="S"){
		spadeCounter++;
		spades.add(c.getValue());
	    }
	    else if(c.getSuit()=="C"){
		cloverCounter++;
		clovers.add(c.getValue());
	    }
	    else if(c.getSuit()=="D"){
		diamondCounter++;
		diamonds.add(c.getValue());
	    }
	    else if(c.getSuit()=="H"){
		heartCounter++;
		hearts.add(c.getValue());
	    }
	}
	int i=0;
	if(spadeCounter>=5){
	    Collections.sort(spades);
	    if(spades.get(i)==(spades.get(i+1)-1) && 
	       spades.get(i)==(spades.get(i+2)-2) &&
	       spades.get(i)==(spades.get(i+3)-3) &&
	       spades.get(i)==(spades.get(i+4)-4))
		straightFlushCounter=4;
	}
	else if(cloverCounter>=5){
	    Collections.sort(clovers);
	    if(clovers.get(i)==(clovers.get(i+1)-1) && 
	       clovers.get(i)==(clovers.get(i+2)-2) &&
	       clovers.get(i)==(clovers.get(i+3)-3) &&
	       clovers.get(i)==(clovers.get(i+4)-4))
		straightFlushCounter=4;
	}
	else if(heartCounter>=5){
	    Collections.sort(hearts);
	    if(spades.get(i)==(hearts.get(i+1)-1) && 
	       hearts.get(i)==(hearts.get(i+2)-2) &&
	       hearts.get(i)==(hearts.get(i+3)-3) &&
	       hearts.get(i)==(hearts.get(i+4)-4))
		straightFlushCounter=4;
	}
	else if(diamondCounter>=5){
	    Collections.sort(diamonds);
	    if(diamonds.get(i)==(diamonds.get(i+1)-1) && 
	       diamonds.get(i)==(diamonds.get(i+2)-2) &&
	       diamonds.get(i)==(diamonds.get(i+3)-3) &&
	       diamonds.get(i)==(diamonds.get(i+4)-4))
		straightFlushCounter=4;
	}
	else
	    return false;
	    
	if(straightFlushCounter==4)
	    return true;
	else
	    return false;
    }

    /**
       Returns boolean for if the hand has a four of a kind.
       * @param player the cards belonging to the player
       * @return boolean
    */
    private boolean isFourOfAKind(ArrayList<Card> player) {
	ArrayList<Integer> sortedHand=sortHand(player);
	int quadCounter=0;
	for(int i=0;i<player.size()-3;i++) {
	    if(sortedHand.get(i)==sortedHand.get(i+1) && sortedHand.get(i+1)
	       ==sortedHand.get(i+2) && sortedHand.get(i+2)==sortedHand.get(i+3)) {
		quadCounter=3;
	    }
	}
	if(quadCounter==3)
	    return true;
	else
	    return false;
    }

    /**
       Returns boolean for if the hand is a full house.
       * @param player the cards belonging to the player
       * @return boolean
    */
    private boolean isFullHouse(ArrayList<Card> player) {
	ArrayList<Integer> sortedHand=sortHand(player);
	int doubleCounter=0;
	int tripleCounter=0;
	for(int i=0;i<player.size()-1;i++) {
	    if(sortedHand.get(i)==sortedHand.get(i+1)) {
		if(tripleCounter==1) {
		    sortedHand.remove(i+1);
		    sortedHand.remove(i);
		    tripleCounter++;
		    break;
		}
		else {
		    if(i==1)
			tripleCounter=0;
		    else
			tripleCounter++;
		}
	    }
	    else
		tripleCounter=0;
	}
	
	if(tripleCounter==2) {
	    sortedHand.trimToSize();
	    int size=sortedHand.size();
	    for(int i=0;i<(size-1);i++) {
		if(sortedHand.get(i)==sortedHand.get(i+1))
		    doubleCounter++;
	    }
	}
	else
	    return false;
	if(doubleCounter>=1)
	    return true;
	else
	    return false;
    }
    
    /**
       Returns boolean for if the hand is a flush.
       * @param player the cards belonging to the player
       * @return boolean
    */
    private boolean isFlush(ArrayList<Card> player) {
	int spadeCounter=0;
	int cloverCounter=0;
	int heartCounter=0;
	int diamondCounter=0;
	for(Card c: player){
	    if(c.getSuit()=="S")
		spadeCounter++;
	    else if(c.getSuit()=="C")
		cloverCounter++;
	    else if(c.getSuit()=="D")
		diamondCounter++;
	    else
		heartCounter++;
	}
	if(spadeCounter>=5 || cloverCounter>=5 || diamondCounter>=5 
	   || heartCounter>=5)
	    return true;
	else
	    return false;
    }
    
    /**
       Returns boolean for if the hand is a straight.
       * @param player the card belonging to the player
       * @return boolean
    */
    private boolean isStraight(ArrayList<Card> player) {
	ArrayList<Integer> sortedHand=sortHand(player);
	int straightCounter=0;
	for(int i=0;i<player.size()-4;i++) {
		if(sortedHand.get(i)==(sortedHand.get(i+1)-1) && 
		   sortedHand.get(i)==(sortedHand.get(i+2)-2) &&
		   sortedHand.get(i)==(sortedHand.get(i+3)-3) &&
		   sortedHand.get(i)==(sortedHand.get(i+4)-4))
		    {
			straightCounter=4;
		    }
	}
	return(straightCounter==4);
    }
    
    /**
       Returns boolean for if the hand has three of a kind.
       * @param player the cards belonging to the player
       * @return boolean
    */
    private boolean isThreeOfAKind(ArrayList<Card> player) {
	if(isFullHouse(player)){
	    return false;
	}
	
	ArrayList<Integer> sortedHand= sortHand(player);
	int tripleCounter=0;
	for(int i=0;i<player.size()-1;i++) {
		if(sortedHand.get(i)==sortedHand.get(i+1))
		    tripleCounter++;
		else {
		    if(tripleCounter==1)
			tripleCounter=0;
		}
	}
	if(tripleCounter==2)
	    return true;
	else
	    return false;		
    }
    
    /**
       Returns boolean for if the hand has two pairs.
       * @param player the cards that belong to the player
       * @return boolean
    */
    private boolean isTwoPair(ArrayList<Card> player) {
	ArrayList<Integer> sortedHand=new ArrayList<Integer>();
	sortedHand=sortHand(player);
	int pair1Counter=0;
	int pair2Counter=0;
	int pair1Int=100;
	int pair2Int=100;
	for(int i=0;i<player.size()-1;i++) {
	    if(sortedHand.get(i)==sortedHand.get(i+1)){
		if(sortedHand.get(i)==pair1Int || sortedHand.get(i)==pair2Int){
		    return false;
		}
		else if(pair1Counter==1){
		    pair2Counter++;
		    pair2Int=sortedHand.get(i);
		}
		else{
		    pair1Counter++;
		    pair1Int=sortedHand.get(i);
		}
	    }
	}
	return(pair1Counter==1 && pair2Counter>=1);
    }
    
    /**
       Returns boolean for if the hand has only one pair.
       * @param player cards that belong to the player
       * @return boolean
    */
    private boolean isOnePair(ArrayList<Card> player) {
	ArrayList<Integer> sortedHand=new ArrayList<Integer>();
	sortedHand=sortHand(player);
	int pairCounter=0;
	for(int i=0;i<player.size()-1;i++) {
	    if(sortedHand.get(i)==sortedHand.get(i+1))
		pairCounter++;
	}
	if(pairCounter==1)
	    return true;
	else
	    return false;
    }

}
