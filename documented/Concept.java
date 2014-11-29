import java.util.HashSet;
import java.util.Set;

/*Concept class
 * contains set of objects, set of attributes, childlist, number of children and an id
 */

public class Concept 
{
	public Set<String> objects;
	public Set<String> attributes;
	public Concept[] children;
	int childnum;
	public int id; 
	
	Concept()
	{
		objects = null;
		attributes = null;
		children = new Concept[1000];
		childnum = 0;
	}
	/* Constructor Function
	 * Parameters : set of objects, set of attributes
	*/
	Concept( Set<String> obj, Set<String> attr )
	{
		objects = obj;
		attributes = attr;
		children = new Concept[1000];
		childnum = 0;
	}
	
	/* Constructor function
	 * Parameters : set of objects
	 */ 
	Concept( Set<String> obj )
	{
		objects = obj;
		attributes = null;
		children = new Concept[1000];
		childnum = 0;
	}
	
	/* Function that adds a child concept
	 * Parameters : a concept c
	 * returns void
	 */ 
	public void addChild( Concept c )
	{
		children[childnum] = c;
		childnum++;
	}
}
