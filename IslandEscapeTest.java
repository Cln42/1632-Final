import static org.junit.Assert.*;
import java.io.*;
import org.junit.*;
import static org.mockito.Mockito.*;

/* IslandEscapeTest.java 
 * Tests IslandEscape.java and Location.java
 * Has one test to test each requirement
 * 
 * Written by Casey Nispel, Cln42@pitt.edu
 * For CS1632 Final Deliverable
 */

public class IslandEscapeTest {

	private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

	/*
	 * Catch the statements being printed out so I can check them 
	 */
	@Before
	public void setUpStreams() {
	    System.setOut(new PrintStream(outContent));
	}
	  
	/*
	 * The program should print out a welcome message on startup
	 */
	@Test
	public void FUN_WELCOME_MSG_TEST() {
		String message = "Welcome to Island Escape!\n"
				+ "==========================\n\n"
				+ "You wake up on a desert island. Can you build a boat to escape?\n\n"
				+ "Commands:\n=========\n"
				+ "F or Forward: Move forward to the next location\n"
				+ "B or Back: Move back to previous location\n"
				+ "L or Look: Look around and pick up any items\n"
				+ "I or Inventory: Display your inventory\n"
				+ "C [item] or Craft [item]: Create an item using current items\n"
				+ "H or Help: Display game information\n\n"
				+ "Craft Commands:\n===============\n"
				+ "Craft Torch: requires 1 stick and 1 flint\n"
				+ "Craft Fire: requires 5 rocks, 5 sticks, and 1 torch\n"
				+ "Craft Boat: requires 5 logs, 1 rope, and 1 fire\n\n";
		IslandEscape.welcomeMessage();
		assertEquals(message, outContent.toString());
	}
	
	/*
	 * On every iteration the program should prompt the user with >
	 */
	@Test
	public void FUN_PROMPT_TEST() {
		Location mockLoc = mock(Location.class);
		when(mockLoc.getName()).thenReturn("Mock Location");
		IslandEscape.prompt(mockLoc);
		assertTrue(outContent.toString().endsWith(">"));
	}
	
	/*
	 * Make sure that the program accepts valid input
	 */
	@Test
	public void FUN_INPUT_TEST() {
		Location loc = mock(Location.class);
		assertEquals(0, IslandEscape.parseInput("f", loc));
	}
	
	/*
	 * Make sure the program does not accept invalid input
	 */
	@Test
	public void FUN_UNKNOWN_INPUT_TEST() {
		Location loc = mock(Location.class);
		assertEquals(1, IslandEscape.parseInput("a", loc));
		assertEquals("Unknown Command: Type H for help\n", outContent.toString());
	}
	
	/*
	 * Make sure there are only 6 locations numbered 0-5
	 * If location is created with number greater than 5, it
	 * should still only be assigned to one of those 6 locations
	 * using %6
	 */
	@Test
	public void FUN_LOCATIONS_TEST() {
		Location loc = new Location(15);
		String[] allLocs = loc.getAllLocations();
		assertEquals(6, allLocs.length);
		assertEquals(loc.getNum(), 3);
		assertEquals(loc.getName(), "Main Forest");
	}
	
	/*
	 * When user is at location 5 and goes forward, 
	 * they should go to location 0. When the user is at location 0
	 * and goes back, they should go to location 5.
	 */
	@Test
	public void FUN_CIRCLE_TEST() {
		Location loc = new Location(5);
		int num = loc.moveForward();
		assertEquals(0, num);
		num = loc.moveBack();
		assertEquals(5, num);
	}
	
	/*
	 * Input F takes user to next location
	 */
	@Test
	public void FUN_FORWARD_TEST() {
		Location loc = new Location(3);
		assertEquals(4, IslandEscape.parseInput("f", loc));
	}
	
	/*
	 * Input B takes user back to previous location
	 */
	@Test
	public void FUN_BACK_TEST() {
		Location loc = new Location(2);
		assertEquals(1, IslandEscape.parseInput("b", loc));
	}
	
	/*
	 * Input L gets any items in that location and places them in inventory
	 */
	@Test
	public void FUN_LOOK_TEST() {
		Location loc = new Location(4);
		IslandEscape.parseInput("l", loc);
		assertEquals(3, IslandEscape.inventory[3]);
		assertEquals(2, IslandEscape.inventory[6]);
	}

	/*
	 * Input H displays help message
	 */
	@Test
	public void FUN_HELP_TEST() {
		String message = "Goal: Make a Boat to escape the island\n\n"
				+ "Commands:\n=========\n"
				+ "F or Forward: Move forward to the next location\n"
				+ "B or Back: Move back to previous location\n"
				+ "L or Look: Look around and pick up any items\n"
				+ "I or Inventory: Display your inventory\n"
				+ "C [item] or Craft [item]: Create an item using current items\n"
				+ "H or Help: Display game information\n\n"
				+ "Craft Commands:\n===============\n"
				+ "Craft Torch: requires 1 stick and 1 flint\n"
				+ "Craft Fire: requires 5 rocks, 5 sticks, and 1 torch\n"
				+ "Craft Boat: requires 5 logs, 1 rope, and 1 fire\n\n";
		Location loc = mock(Location.class);
		IslandEscape.parseInput("h", loc);
		assertEquals(message, outContent.toString());
	}
	
	/*
	 * Input C without item displays an error message
	 */
	@Test
	public void FUN_CRAFT_HELP_TEST() {
		String message = "Please enter an item to craft. Type H for help.\n";
		Location loc = mock(Location.class);
		IslandEscape.parseInput("c", loc);	
		assertEquals(message, outContent.toString());
	}

	/*
	 * Program should remove used items and add crafted item to inventory
	 */
	@Test
	public void FUN_CRAFT_VALID_TEST() {
		Location loc = mock(Location.class);
		IslandEscape.inventory[6] = 2;
		IslandEscape.inventory[7] = 1;
		IslandEscape.parseInput("c torch", loc);
		assertEquals(1, IslandEscape.inventory[6]);
		assertEquals(0, IslandEscape.inventory[7]);
		assertEquals(1, IslandEscape.inventory[2]);
	}

	/*
	 * If user does not have enough items to craft a new item, 
	 * an error message should display
	 */
	@Test
	public void FUN_CRAFT_INVALID_TEST() {
		String message = "You don't have enough items.\nYou need 5 rocks and 5 sticks and 1 torch to make a fire.\n";
		Location loc = mock(Location.class);
		IslandEscape.inventory[5] = 2;
		IslandEscape.inventory[6] = 1;
		IslandEscape.inventory[2] = 1;
		IslandEscape.parseInput("c fire", loc);
		assertEquals(message, outContent.toString());
	}
		
	/*
	 * Input I displays user's inventory with correct values
	 */
	@Test
	public void FUN_INVENTORY_TEST() {
		IslandEscape.inventory[0]=0;
		IslandEscape.inventory[1]=1;
		IslandEscape.inventory[2]=2;
		IslandEscape.inventory[3]=3;
		IslandEscape.inventory[4]=4;
		IslandEscape.inventory[5]=5;
		IslandEscape.inventory[6]=6;
		IslandEscape.inventory[7]=7;
		String message = "Inventory:\n===================\nBoat\t\t0\nFire\t\t1\nTorch\t\t2\nLog(s)\t\t3\nRope(s)\t\t4\nRock(s)\t\t5\nStick(s)\t6\nFlint\t\t7\n\n";
		Location loc = mock(Location.class);
		IslandEscape.parseInput("i", loc);
		assertEquals(message, outContent.toString());
	}
	
	/*
	 * Program should be case insensitive
	 */
	@Test
	public void FUN_CASE_TEST() {
		Location loc = mock(Location.class);
		assertEquals(0, IslandEscape.parseInput("B", loc));
		assertEquals(1, IslandEscape.parseInput("Z", loc));
	}
	
	/*
	 * When Boat = 1, Success message prints and program exits
	 */
	@Test
	public void FUN_BOAT_TEST() {
		String message = "Congratulations!\nYou have built a boat and escaped the island!\nYou win!\n";
		IslandEscape.inventory[0] = 0;
		IslandEscape.inventory[3] = 5;
		IslandEscape.inventory[4] = 1;
		IslandEscape.inventory[1] = 1;
		Location loc = mock(Location.class);
		IslandEscape.parseInput("Craft Boat", loc);
		assertEquals(message, outContent.toString());
	}

	/*
	 * Input E will print Failure message and program exits
	 */
	@Test
	public void  FUN_EXIT_TEST() {
		String message = "You have not escaped the island!\nYou lose.\n";
		Location loc = mock(Location.class);
		IslandEscape.parseInput("E", loc);
		assertEquals(message, outContent.toString());
	}

	/*
	 * Print out to console again
	 */
	@After
	public void cleanUpStreams() {
	    System.setOut(null);
	}
	
}
