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
	player1Value = calculateValue(cardHand1);
	player2Value = calculateValue(cardHand2);
	
	if(player1Value>player2Value)
	    return 1;
	else if(player1Value<player2Value)
	    return 0;
	else {
	    
	    int yourHandValue = sameHandUpdated(cardHand1, 1);
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
		
		else { //
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
			    }  //
		}
	    }		
	}
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
       Returns boolean for if the hand is a straight flush
       * @param player Cards belonging to the player
       * @return boolean
    */
    private boolean isStraightFlush(ArrayList<Card> player){
	int straightFlushCounter=0;
	int spadeCounter=0;
	int clubsCounter=0;
	int heartCounter=0;
	int diamondCounter=0;
	ArrayList<Integer> spades=new ArrayList<Integer>();
	ArrayList<Integer> clubs=new ArrayList<Integer>();
	ArrayList<Integer> diamonds=new ArrayList<Integer>();
	ArrayList<Integer> hearts=new ArrayList<Integer>();
	for(Card c: player){ //Sort the hand into seperate arrayLists defined by suits.
	    if(c.getSuit()=="S"){
		spadeCounter++;
		spades.add(c.getValue());
	    }
	    else if(c.getSuit()=="C"){
		clubsCounter++;
		clubs.add(c.getValue());
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
	removeDuplicates(spades);
	removeDuplicates(clubs);
	removeDuplicates(diamonds);
	removeDuplicates(hearts);
	
	if(spadeCounter>=5){
	    Collections.sort(spades);
	    if (spades.get(spades.size()-1 ) == 14) //if the top value is an ace, use the method below
		if (isLowestRankingStraight(spades))
		    return true;
	    for (i = 0; i < spadeCounter-5; i++) {
		if(spades.get(i)==(spades.get(i+1)-1) && 
		   spades.get(i)==(spades.get(i+2)-2) &&
		   spades.get(i)==(spades.get(i+3)-3) &&
		   spades.get(i)==(spades.get(i+4)-4))
		    straightFlushCounter=4;
	    }
	}
	else if(clubsCounter>=5){
	    Collections.sort(clubs);
	    if (clubs.get(clubs.size()-1 ) == 14) //if the top value is an ace, use the method below
		if (isLowestRankingStraight(clubs))
		    return true;
	    for (i = 0; i < clubsCounter-5; i++) {
		if(clubs.get(i)==(clubs.get(i+1)-1) && 
		   clubs.get(i)==(clubs.get(i+2)-2) &&
		   clubs.get(i)==(clubs.get(i+3)-3) &&
		   clubs.get(i)==(clubs.get(i+4)-4))
		    straightFlushCounter=4;
	    }
	}
	else if(heartCounter>=5){
	    if (hearts.get(hearts.size()-1 ) == 14) //if the top value is an ace, use the method below
		if (isLowestRankingStraight(hearts))
		    return true;
	   	    Collections.sort(hearts);
	    for (i = 0; i < heartCounter-5; i++) {
		if(spades.get(i)==(hearts.get(i+1)-1) && 
		   hearts.get(i)==(hearts.get(i+2)-2) &&
		   hearts.get(i)==(hearts.get(i+3)-3) &&
		   hearts.get(i)==(hearts.get(i+4)-4))
		    straightFlushCounter=4;
	    }
	}
	else if(diamondCounter>=5){
	    if (diamonds.get(diamonds.size()-1 ) == 14) //if the top value is an ace, use the method below
		if (isLowestRankingStraight(diamonds))
		    return true;
	    	    Collections.sort(diamonds);
	    for (i = 0; i < diamondCounter-5; i++) {
		if(diamonds.get(i)==(diamonds.get(i+1)-1) && 
		   diamonds.get(i)==(diamonds.get(i+2)-2) &&
		   diamonds.get(i)==(diamonds.get(i+3)-3) &&
		   diamonds.get(i)==(diamonds.get(i+4)-4))
		    straightFlushCounter=4;
	    }
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
	removeDuplicates(sortedHand);
	if (sortedHand.get(sortedHand.size()-1 ) == 14) //if the top value is an ace, use the method below
	    if (isLowestRankingStraight(sortedHand))
		return true;
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
     *Used when checking for Straights. Ace has a value of 14
     * but technically it should also have a value of one. This method covers this.
     * @param sortedHand a hand that is already sorted from lowest to highest
     * @return boolean 
     */
    private boolean isLowestRankingStraight(ArrayList<Integer> sortedHand) {
	for (int i = 0; i<sortedHand.size()-3; i++) {
	    if (sortedHand.get(0) == 2  && //has 2
		sortedHand.get(1) == 3 && //has 3
		sortedHand.get(2) == 4  && //has 4
	        sortedHand.get(3) == 5 )  //has 5
		return true;
	}
	return false;
    }

    /**
     * Method that removes duplicate integers from an arrayList.
     * If the hand is sorted, it stays sorted.
     * @param sortedHand it is sorted, but it doesn't have to be.
     * @return sortedHand a hand without duplicates.
     */
    private ArrayList<Integer> removeDuplicates(ArrayList<Integer> sortedHand) {
	ArrayList<Integer> deDupList = new ArrayList<>();
	for(Integer i : sortedHand){
	    if(! (deDupList.contains(i) ) ){
		deDupList.add(i);
	    }
	}
	sortedHand = deDupList;
	return sortedHand;
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
