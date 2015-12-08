/* Location.java
 * Each location has a number and a name. Names and whether 
 * location has been searched are stored in arrays
 * 
 * Written by Casey Nispel, Cln42@pitt.edu
 * For CS1632 Final Deliverable
 */
public class Location {

	private int num;
	private String name;

	String[] names = { "Main Beach", "North Beach", "North Forest", "Main Forest", "South Forest", "South Beach" };

	Boolean[] searched = { false, false, false, false, false, false };

	public Location(int i) {
		i = i % 6;
		num = i;
		name = names[i];
	}

	public String[] getAllLocations() {
		return names;
	}

	public String getName() {
		return name;
	}

	public int getNum() {
		return num;
	}

	public boolean getSearched() {
		return searched[num];
	}

	public void setSearched() {
		searched[num] = true;
	}

	public int moveForward() {
		num = (num + 1) % 6;
		name = names[num];
		return num;
	}

	public int moveBack() {
		num -= 1;
		if (num < 0) {
			num = 5;
		}
		name = names[num];
		return num;
	}

}
