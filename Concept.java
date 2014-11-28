import java.util.HashSet;
import java.util.Set;

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
	Concept( Set<String> obj, Set<String> attr )
	{
		objects = obj;
		attributes = attr;
		children = new Concept[1000];
		childnum = 0;
	}
	
	Concept( Set<String> obj )
	{
		objects = obj;
		attributes = null;
		children = new Concept[1000];
		childnum = 0;
	}
	
	public void addChild( Concept c )
	{
		children[childnum] = c;
		childnum++;
	}
}
