import java.lang.Math.*;
/**
 * Coordinate class; each coordinate has a row and column component.
 * Extends the object class. 
*/
public class Coord extends java.lang.Object implements java.lang.Comparable<Coord> {
	
	public final int row;
	public final int col;

	/**
 	* Coordinate constructor
 	*/
	public Coord() {

		this.row = 0;
		this.col = 0;
	}

	/**
	* @param row1 row part of coordinate
	* @param col1 column part of coordinate
	* Constructor - row,column
	*/ 
	public Coord(int row1, int col1) {
		
		this.row = row1;
		this.col = col1;
	}

	/**
	* @param Coord coordinate to make deep copypsz
	* "Copy" constructor create a new Coord with identical row,col as the argument
	*/	 	
	public Coord(Coord other) {
		
		if (other == null) {
			this.row = 0;
			this.col = 0;
		}
		else {
			this.row = other.row;
			this.col = other.col;
		}
	}
	
	/**
 	* @param b coordinate against which to compute distance
 	* @return null, if b is null; else new Coord(|row-b.row|, |col-a.col|)
 	* Compute the "Manhattan" distance between two different coordinates
 	*/ 
	public Coord dist(Coord b) {
		
		if (b == null) {
			return null;
		}
		return new Coord(Math.abs(this.row - b.row), Math.abs(this.col - b.col));
	}	
	
	/**
 	* @param b coordinate against which to compute distance
 	* @return null, if b is null; else new Coord(row-b.row, col-a.col)
 	* Compute the signed distance (difference) between two different coordinates
 	*/ 	
	public Coord diff(Coord b) {
		
		if (b == null) {
			return null;
		}
		return new Coord(this.row - b.row, this.col - b.col);
	}

	/**
 	* @param b coordinate against which to compute distance
 	* @return Integer.MAX_VALUE if b is null, else (dist(b).row)^2 + (dist(b).col)^2
 	* Compute the sum of distances squared between two Coords
 	*/ 
	public int dist2(Coord b) {

		if (b == null) {
			return Integer.MAX_VALUE;
		}
		return (dist(b).row)*(dist(b).row) + (dist(b).col)*(dist(b).col);
	}
	
	/**
 	* @param x integer to judge
 	* @return -1 if x is less than 0, 1 if x is nonnegative
 	* Computes the sign of an integer
 	*/ 
	private int sign(int x) {

		if (x < 0) {
			return -1;
		}
		if (x == 0) {
			return 0;
		}
		return 1;
	}
	/**
 	*  @return Coord(sign(row), sign(col))
 	*  Compute the sign of the row, column component
 	*/
	public Coord unit() {

		return new Coord(sign(row),sign(col));
	}

	

	/**
 	* @param b coordinate to add
 	* @return null, if b is null; else, new Coord(row+b.row,col+a.col)
 	* Add coordinates together
 	*/ 
	public Coord add(Coord b) {

		if (b == null) {
			return null;
		}
		return 	new Coord(this.row + b.row, this.col + b.col);
	}
	
	@Override
	/**
 	* @param other the coordinate to be compared
 	* @return return -1 if other is greater, 0 if other is equal to, 1 if other is less; if other is null, return Integer.MAX_VALUE
	* Compares two coordinates
 	*/
	public int compareTo (Coord other) {

		if (other == null) {
			return -1;
		}
		Coord origin = new Coord(0,0);
		if (other.dist2(origin) > dist2(origin)) {
			return -1;
		}
		else if (other.dist2(origin) == dist2(origin)) {
			return 0;
		}
		else {
			return 1;
		}
	}

	/**
 	* @param other the object to be compared
 	* @return true if they are equal, false otherwise
 	* Determines whether two coordinates are equal
 	*/ 	
	public boolean equals (java.lang.Object other) {
		if (other == null) {
			return false;
		}
		if (other instanceof Coord) {
			Coord newCoord = (Coord)(other);
			if (this.row == newCoord.row && this.col == newCoord.col) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	/**
 	* @return String version of the coordinates 
 	* creates string version of the coordinates
 	*/
	public java.lang.String toString() {
		 return "".format("Coord:(row=%d,col=%d)", row, col);
	}
	
}
 
	
	
		


