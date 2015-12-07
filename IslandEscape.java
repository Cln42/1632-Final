import java.util.*;


public class IslandEscape {
	public static Location location; 
	public static boolean keepGoing;
	 
	
	static int[][] items= {{5, 2, 6, 1}, 
					{4, 1, 5, 2}, 
					{5, 3, 6, 3},
					{3, 4, 7, 1},
					{3, 3, 6, 2},
					{4, 1, 6, 2}};
	
	static int[] inventory = {0, 0, 0, 0, 0, 0, 0, 0};
	
	static String[] itemNames = {"Boat", "Fire", "Torch", "Log(s)", "Rope(s)", "Rock(s)", "Stick(s)", "Flint"};
	
	

	
	private static void printInventory(){
		System.out.print("Inventory:\n===================\n");
		for(int i=0; i<8; i++){ 
			if(i==6){
				System.out.print(itemNames[i]+"\t"+inventory[i]+"\n");
			}
			else{
				System.out.print(itemNames[i]+"\t\t"+inventory[i]+"\n");
			}
		}
		System.out.println();
	}
	
	private static void win(){
		System.out.print("Congratulations!\nYou have built a boat and escaped the island!\nYou win!\n");
		keepGoing = false;
	}
	
	private static void lose(){
		System.out.print("You have not escaped the island!\nYou lose.\n");
		keepGoing = false;
	}
	
	 
	public static int parseInput(String input, Location loc){
		input=input.toLowerCase();
		int ret = -1;
		if(input.equals("f") || input.equals("forward")){
			ret = loc.moveForward();
		}else if(input.equals("b") || input.equals("back")){
			ret = loc.moveBack();
		}else if(input.equals("l") || input.equals("look")){
			if(loc.getSearched()==false){
				int locID = loc.getNum();
				int item1ID = items[locID][0];
				int item1Q = items[locID][1];
				int item2ID = items[locID][2];
				int item2Q = items[locID][3];
				inventory[item1ID] += item1Q;
				inventory[item2ID] += item2Q;
				System.out.println("You found "+item1Q+" "+itemNames[item1ID]+" and "+item2Q+" "+itemNames[item2ID]+"!\n");
				loc.setSearched();
			}else{
				System.out.println("There's nothing here!\n");
			}
			ret = 0;
		}else if(input.equals("h") || input.equals("help")){
			helpMessage();
			ret = 0;
		}else if(input.equals("i") || input.equals("inventory")){
			printInventory();
			ret = 0;
		}else if(input.equals("e") || input.equals("exit")){
			lose();
			ret = 0;
		}else if(input.equals("c") || input.equals("craft")){
			System.out.println("Please enter an item to craft. Type H for help.");
			ret = 0;
		}
		
		else if(input.equals("c torch") || input.equals("craft torch")){
			if(inventory[6]>0 && inventory[7]>0){
				inventory[6] -= 1;
				inventory[7] -= 1;
				inventory[2] += 1;
				System.out.println("You made a torch!");}
			else{System.out.println("You don't have enough items.\nYou need 1 stick and 1 flint to make a torch.");}
			ret = 0;
		}else if(input.equals("c fire") || input.equals("craft fire")){
			if(inventory[5]>4 && inventory[6]>4 && inventory[2]>0){
				inventory[5] -= 5;
				inventory[6] -= 5;
				inventory[2] -= 1;
				inventory[1] += 1;
				System.out.println("You made a fire!");}
			else{System.out.println("You don't have enough items.\nYou need 5 rocks and 5 sticks and 1 torch to make a fire.");}
			ret = 0;
		}else if(input.equals("c boat") || input.equals("craft boat")){
			if(inventory[3]>4 && inventory[4]>0 && inventory[1]>0){
				inventory[3] -= 5;
				inventory[4] -= 1;
				inventory[1] -= 1;
				inventory[0] += 1;
				
				win();
			}
			else{System.out.println("You don't have enough items.\nYou need 5 logs and 1 rope and 1 fire to make a boat.");}
			ret = 0;
		}
		else{
			System.out.println("Unknown Command: Type H for help");
			ret = 1;
		}
		return ret;
	}
	
	public static void helpMessage(){
		String message = "You wake up on a desert island. Can you build a boat to escape?\n\n"
				+ "Commands:\n=========\n"
				+ "F or Forward: Move forward to the next location\n"
				+ "B or Back: Move back to previous location\n"
				+ "L or Look: Look around and pick up any items\n"
				+ "I or Inventory: Display your inventory\n"
				+ "C [item] or Craft [item]: Create an item using current items\n"
				+ "H or Help: Display game information\n\n"
				+ "Craft Commands;\n==============\n"
				+ "Craft Torch: requires 1 stick and 1 flint\n"
				+ "Craft Fire: requires 5 rocks, 5 sticks, and 1 torch\n"
				+ "Craft Boat: requires 5 logs, 1 rope, and 1 fire\n\n";
		System.out.print(message);
	}
	
	
	public static void prompt(Location loc){
		String prompt = "You are at the " + loc.getName();
		prompt += "\n>";
		System.out.print(prompt);
	}
	

	public static void main(String[] args){
		Scanner scan = new Scanner(System.in);
		String input = "";
		location = new Location(0);
		helpMessage();
		keepGoing=true;
		while(keepGoing){
			prompt(location);
			input = scan.nextLine();
			parseInput(input, location);
		}
		scan.close();
		System.exit(0);
	}
	
	

}
