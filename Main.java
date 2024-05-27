import java.util.Random;
public class Main {
	static Random rnd = new Random();
	static boolean isCinkoPlayer1 = false;
	static boolean isCinkoPlayer2 = false;
	static Queue CreateLottoCards() {
		//cards of players are created with the function here
		Queue from1to17 = new Queue(200);
		FillFrom1To17(from1to17);
		Queue lottoCards = new Queue(300);
		int randomNumber;
		while(lottoCards.size() != 7) {
			randomNumber = rnd.nextInt(from1to17.size());
			int index = 0;
			while(index != randomNumber) {
				from1to17.enqueue(from1to17.dequeue());
				index++;
			}
			lottoCards.enqueue(from1to17.dequeue());
			//I bring the elements in the same order so that the randomness is not broken
			for(int i = 0;i < from1to17.size() - index; i++) {
				from1to17.enqueue(from1to17.dequeue());
			}
		}
		return lottoCards;
	}
	static int PrintQueue(Queue queue) {
		int size = queue.size();
		int characterNumber = 0;
		for(int i = 0;i < size; i++) {
			characterNumber += queue.peek().toString().length() + 1;
			System.out.print(queue.peek() + " ");
			queue.enqueue(queue.dequeue());
		}
		//I returned the character number of the player cards to align bag1 and bag2
		return characterNumber;
	}
	static void FillFrom1To17(Queue queue) {
		for(int i = 1;i < 18; i++) {
			queue.enqueue(i);
		}
	}
	static void EndOfTheGame(int moneyOfPlayer1, int moneyOfPlayer2) {
		if(moneyOfPlayer1 > moneyOfPlayer2) {
			System.out.println("\nPlayer1 is the winner !!!\n");
		}
		else if(moneyOfPlayer1 < moneyOfPlayer2) {
			System.out.println("\nPlayer2 is the winner !!!\n");
		}
		else {
			System.out.println("\nThe game is a draw...\n");
		}
		System.out.println("Player1 gets " + moneyOfPlayer1 + "$");
		System.out.println("Player2 gets " + moneyOfPlayer2 + "$");
	}
	static Object FindTheSelectedNumber(Queue bag1, int selectedNumber, Queue bag2) {
		int whichOrder = 0;
		int sizeBag1 = bag1.size();
		Object controlElement = 0;
		while(whichOrder != selectedNumber) {
			bag1.enqueue(bag1.dequeue());
			whichOrder++;
		}
		if(!bag1.isEmpty()) {
			controlElement = bag1.peek();
			bag2.enqueue(bag1.dequeue());
		}
		//Numbers after the selected number to sort bag1 are thrown to the end of bag1
		for(int i = 0;i < sizeBag1 - whichOrder - 1; i++) {
			bag1.enqueue(bag1.dequeue());
		}
		return controlElement;
	}
	static void ViewLatestStatusOfCards(Queue cardsOfPlayer1, Queue cardsOfPlayer2, Queue bag1, Queue bag2) {
		System.out.print("Player1 : ");
		int length = PrintQueue(cardsOfPlayer1);
		//I tried to align it with the required number of spaces
		for(int i = 0;i < 21 - length; i++) {
			System.out.print(" ");
		}
		System.out.print("\t\tBag1 : ");
		PrintQueue(bag1);
		System.out.println();
		System.out.print("Player2 : ");
		length = PrintQueue(cardsOfPlayer2);
		for(int i = 0;i < 21 - length; i++) {
			System.out.print(" ");
		}
		System.out.print("\t\tBag2 : ");
		PrintQueue(bag2);
		System.out.println();
	}
	static void CinkoMessage(int whichPlayer, int money) {
		System.out.println("\nPlayer"+ whichPlayer + " gets $" + money + "(birinci çinko)");
	}
	static void CheckOfPlayerCards(Queue cardsOfPlayer, Object controlElement, int whichPlayer) {
		//Checking the selected number on the player cards
		int size1 = cardsOfPlayer.size();
		for(int i = 0;i < size1; i++) {
			if(cardsOfPlayer.peek() == controlElement) {
				cardsOfPlayer.dequeue();
				if(cardsOfPlayer.size() == 3) {
					//if there is Çinko I save it in boolean values and print the money for the relevant player from below.
					if(whichPlayer == 1) {
						isCinkoPlayer1 = true;
					}
					else {
						isCinkoPlayer2 = true;
					}
				}
				break;
			}
			cardsOfPlayer.enqueue(cardsOfPlayer.dequeue());
		}
	}
	public static void main(String[] args) {
		Queue bag1 = new Queue(500);
		Queue bag2 = new Queue(250);
		FillFrom1To17(bag1);
		//Playing card strings are created
		Queue cardsOfPlayer1 = CreateLottoCards();
		Queue cardsOfPlayer2 = CreateLottoCards();
		int selectedNumber;
		int moneyOfPlayer1 = 0;
		int moneyOfPlayer2 = 0;
		/*When any of the players' cards ran out, I ran the main 
		loop one more time to view the last state again, so I created the variables tour and isLastRound as well.*/
		boolean isLastRound = false;
		int tour = 0;
		while(!isLastRound){
			isCinkoPlayer1 = false;
			isCinkoPlayer2 = false;
			if(bag1.size() != 0) {
				selectedNumber = rnd.nextInt(bag1.size());
			}
			else {
				selectedNumber = 0;
			}
			//things printed after each step
			ViewLatestStatusOfCards(cardsOfPlayer1, cardsOfPlayer2, bag1, bag2);
			//the selected number is found in bag1
			Object controlElement = FindTheSelectedNumber(bag1, selectedNumber, bag2);
			//the selected number is checked on player cards
			CheckOfPlayerCards(cardsOfPlayer1, controlElement, 1);
			CheckOfPlayerCards(cardsOfPlayer2, controlElement, 2);
			//Çinko checks have been made in this section
			//Afterwards, appropriate messages were received from the CinkoMessage procedure.
			if(isCinkoPlayer1 && (!isCinkoPlayer2 && cardsOfPlayer2.size() >= 4)) {
				moneyOfPlayer1 += 10;
				CinkoMessage(1, 10);
			}
			if((!isCinkoPlayer1 && cardsOfPlayer1.size() >= 4) && isCinkoPlayer2) {
				moneyOfPlayer2 += 10;
				CinkoMessage(2, 10);
			} 
			if(isCinkoPlayer1 && isCinkoPlayer2) {
				moneyOfPlayer1 += 5;
				moneyOfPlayer2 += 5;
				CinkoMessage(1, 5);CinkoMessage(2, 5);
			}
			if(tour != 1) {
				System.out.println("\nRandomly Selected Number : " + controlElement + "\n\n\n\n\n");
			}
			//the chosen number is put into bag2
			if(cardsOfPlayer1.isEmpty() || cardsOfPlayer2.isEmpty()) {
				tour++;
				if(tour == 2) {
					//if there is no card left in any queue, the loop runs for the last time and the EndGame method is executed.
					isLastRound = true;
					continue;
				}
				//The last money is calculated in this section
				if(cardsOfPlayer1.isEmpty() && !cardsOfPlayer2.isEmpty()) {
					moneyOfPlayer1 += 30;
				}
				else if(cardsOfPlayer2.isEmpty() && !cardsOfPlayer1.isEmpty()) {
					moneyOfPlayer2 += 30;
				}
				else {
					moneyOfPlayer1 += 15;
					moneyOfPlayer2 += 15;
				}
			}
		}
		EndOfTheGame(moneyOfPlayer1, moneyOfPlayer2);
	}
}
