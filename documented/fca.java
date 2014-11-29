import java.io.*;
import java.util.*;

public class fca extends Concept
{
	public static Concept topConcept;
	public static List<mapEntry> map = new ArrayList<mapEntry>();
	public static Concept[] queue = new Concept[ 1000 ];
	public static int qSize = 0;
	public static int qStart = 0;
	public static List<Set<String>> S;
	public int[][] matrix;
	public static int ID=0;

	/* Enqueue function that enqueues a concept to the queue
	 * Parameters : a concept c
	 * Returns : void
	 */ 
	public static void enqueue( Concept c )
	{
		if( qSize < 0 )
			qSize = 0;
		queue[ qSize ] = c;
		qSize ++;

		return;
	}
	
	/* Dequeue function that dequeues a concept from the queue
	 * Parameters : none
	 * Returns : dequeued concept or null
	 */ 
	public static Concept dequeue( )
	{
		if( qSize < 0 || qStart >= qSize)
		{
			System.out.println( "Queue Empty" );
			return null;
		}

		Concept dConcept = queue [ qStart ];

		qStart++;

		return dConcept;
	}

	/* Function to check if queue is empty or not
	 * Parameters : size of queue
	 * Returns : true or false 
	 */
	public static boolean queueEmpty ( int qSize )
	{
		if( qSize < 0 || qStart >= qSize )
		{
			return true;
		}
		return false;
	}


	/* @Name : pSet
	 * @Param : a set of strings - oSet
	 * @Returns : power set of oSet
	 */
	  
	public static Set<Set<String>> pSet (Set<String> oSet)
	{
		List<String> list = new ArrayList<String>(oSet);
		int n = list.size();

		Set<Set<String>> powerSet = new HashSet<Set<String>>();

		for( long i = 0; i < (1 << n); i++) {
			Set<String> element = new HashSet<String>();
			for( int j = 0; j < n; j++ )
				if( (i >> j) % 2 == 1 ) element.add(list.get(j));
			powerSet.add(element); 
		}

		return powerSet;
	}

	/* Function to check if the concept is already in the queue or not
	 * Parameters : Concept x, the concept queue and the size of queue
	 * Returns : true or false
	 */ 
	public static boolean queueContains ( Concept x, Concept[] queue, int qSize )
	{
		for( int i = 0 ; i < qSize ; i++ )
		{
			if( x.objects.equals( queue[i].objects ) && x.attributes.equals( queue[i].attributes ) )
				return true;
		}
		return false;
	}
	
	/* Function to find union of two sets
	 * Parameters : set x, set y
	 * Returns : X U Y
	 */
	  
	public static Set<String> Union ( Set<String> x, Set<String> y )
	{
		Set<String> unionSet = new HashSet<String> ();
		for( String s1 : x )
		{
			unionSet.add(s1);
		}
		for( String s2 : y)
		{
			if( !unionSet.contains(s2) )
				unionSet.add(s2);
		}

		return unionSet;
	}
	
	/* Function to return the index of a property, if its already there in the property list
	 * Parameters : name of property, list of all properties, no of properties
	 * Returns : index (integer value)
	 */
	  
	public static int getPropertyIndex(String propertyName, String[] properties, int noOfProperties)
	{
		for( int i = 1; i <= noOfProperties; i++ )
		{
			if( properties[i].equalsIgnoreCase(propertyName) )
				return i;
		}
		return 0;
	}

	/* Function to return the index of an object, if its already there in the object list
	 * Parameters : name of the object, list of all objects, no of objects
	 * Returns : index (integer value)
	 */
	 
	public static int getObjectIndex(String objectName, String[] objects, int noOfObjects)
	{
		for( int i = 1; i <= noOfObjects; i++)
		{
			if( objects[i].equalsIgnoreCase(objectName) )
				return i;
		}
		return 0;
	}

	//Printing the matrix
	public static void printMatrix( int[][] matrix, int rows, int cols)
	{
		for(int i = 1; i <= rows ; i++)
		{
			for( int j = 1; j <= cols; j++ )
				System.out.print( matrix[i][j] + " " );
			System.out.println();
		}
	}

	//Print an array
	public void printArray( int[] array, int limit )
	{
		for( int i = 1; i <= limit; i++ )
		{
			System.out.print( array[i]+ " " );
		}
		System.out.println();
	}

	/* @Name : Attributes
	 * @Param : objectSet, list of all objects, no of objects, list of all properties, no of properties, matrix
	 * @Returns : attributes that are common to all the objects in 'objectSet'
	 */
	  
	public static Set<String> Attributes( Set<String> objectSet, String[] objects, int noOfObjects, String[] properties, int noOfProperties, int[][] matrix )
	{
		boolean propFound = false;
		Set<String> attribSet = new HashSet<String> ();
		Set<String> emptySet = new HashSet<String>();
		boolean includeprop = true;

		if( objectSet == null )
		{
			return emptySet;
		}

		for( int i = 1; i <= noOfProperties; i++ )
		{
			includeprop = true;
			for( String s : objectSet )
			{
				int obj = getObjectIndex( s, objects, noOfObjects );
				if( obj != 0 )
				{
					if( matrix[ obj ][ i ] == 0 )
						includeprop = false;
				}
				else
					includeprop = false;
			}
			if( includeprop )
			{
				attribSet.add( properties[i] );
				propFound = true;
			}
		}

		if( propFound )
			return attribSet;
		else
			return emptySet;
	}

	/* @Name : Objects
	 * @Param : propSet, list of all objects, no of objects, list of all properties, no of properties, matrix
	 * @Returns : the objects that share all the properties in the set - propSet
	 */
	  
	public static Set<String> Objects( Set<String> propSet, String[] objects, int noOfObjects, String[] properties, int noOfProperties, int[][] matrix )
	{
		boolean objFound = false;
		Set<String> objSet = new HashSet<String> ();
		Set<String> emptySet = new HashSet<String>();
		boolean includeobj = true;

		if( propSet == null )
			return emptySet;

		for( int i = 1; i <= noOfObjects; i++ )
		{
			includeobj = true;
			for( String s : propSet )
			{
				int prop = getPropertyIndex( s, properties, noOfProperties );
				if( prop != 0 )
				{
					if( matrix[ i ][ prop ] == 0 )
						includeobj = false;
				}
				else
					includeobj = false;

			}
			if( includeobj )
			{
				objSet.add( objects[i] );
				objFound = true;
			}
		}

		if( objFound )
			return objSet;
		else
			return emptySet;
	}

	/* @Name : nbr
	 * @Param : propertyName, list of all objects, no of objects, list of all properties, no of properties, matrix
	 * @Returns : the set of objects that have the property
	 */
	  
	public static Set<String> nbr( String property, String[] objects, int noOfObjects, String[] properties, int noOfProperties, int[][] matrix )
	{
		int propIndex = getPropertyIndex(property, properties, noOfProperties );
		Set<String> objSet = new HashSet<String> ();

		if ( propIndex == 0 )
		{
			System.out.println("Property \"" + property + "\" not found" );
			return null;
		}
		for( int i = 1; i <= noOfObjects; i++ )
		{
			if( matrix[ i ][ propIndex ] == 1 )
			{
				objSet.add( objects[i] );
			}
		}
		return objSet;
	}

	/* @Name : findIntersection
	 * @Paremeters : set1, set2
	 * @Returns : intersection of set1 and set2 
	 */
	  
	public static Set<String> findIntersection( Set<String> s1, Set<String> s2 )
	{
		Set<String> result = new HashSet<String> ();

		if( s1 == null || s2 == null )
			return null;

		for( String s: s1)
		{
			for ( String st : s2 )
			{
				if( s.equalsIgnoreCase(st) )
					result.add(s);
			}
		}

		if( result.isEmpty() )
		{
			Set<String> emptySet = new HashSet<String>();
			return emptySet;
		}

		return result;
	}
	
	/* @Name : res
	 * @Param : attributeSet, list of all objects, no of objects, list of all properties, no of properties, matrix
	 * res function
	 */
	  
	public static List<Set<String>> res( Set<String> attributeSet, String[] objects, int noOfObjects, String[] properties, int noOfProperties, int[][] matrix )
	{
		//Set<String> attribSet = new HashSet<String> ();
		Set<String> attributeSetC = new HashSet<String> ();
		Set<String> obj = new HashSet<String> ();
		boolean matchFound = false;

		//Complementing attributeSet w.r.t complete set of properties
		for( int i = 1; i <= noOfProperties; i++ )
		{
			matchFound = false;
			for( String s : attributeSet )
			{
				if( properties[i].equalsIgnoreCase(s) )
					matchFound = true;
			}
			if( matchFound == false )
			{
				attributeSetC.add( properties[i] );
			}
		}

		obj = Objects( attributeSet, objects, noOfObjects, properties, noOfProperties, matrix );

		Set<String> S1 = new HashSet<String>();

		//Computing nbr of each of the properties in complement set 
		for( String s : attributeSetC )
		{
			Set<String> nbrResult;// = new HashSet<String>();
			nbrResult = nbr( s, objects, noOfObjects, properties, noOfProperties, matrix );
			if( nbrResult == null || nbrResult.isEmpty() )
				continue;
			if( findIntersection ( nbrResult, obj ) != null || !findIntersection(nbrResult,obj).isEmpty() )
			{
				if( S1.contains( s ) == false )
					S1.add( s );
			}
		}

		List<Set<String>> fSet = new ArrayList<Set<String>>();
		for( String str: S1 )
		{
			Set<String> intersect = findIntersection(obj, nbr(str, objects, noOfObjects, properties, noOfProperties, matrix ));
			Set<String> set = new HashSet<String>();
			set.add(str);
			for( String st : S1 )
			{
				Set<String> intersect2 = findIntersection(obj, nbr(st, objects, noOfObjects, properties, noOfProperties, matrix ));
				if( intersect.equals(intersect2) && !str.equals(st) )
				{
					set.add(st);
				}
			}
			if(!fSet.contains(set))
				fSet.add(set);
		}

		return fSet;
	}

	/* @Name : AttrChild
	 * @Param : attribSet, list of all objects, no of objects, list of all properties, no of properties, matrix
	 * @Returns : res(attribSet)
	 */
	  
	public static List<Set<String>> AttrChild( Set<String> attribSet, String[] objects, int noOfObjects, String[] properties, int noOfProperties, int[][] matrix )
	{
		List<Set<String>> X = new ArrayList<Set<String>>();
		X = res( attribSet, objects, noOfObjects, properties, noOfProperties, matrix);

		return X;
	}

	public static void printArray( String[] array, int limit )
	{
		for( int i = 1; i <= limit; i++ )
		{
			System.out.print( array[i]+ " " );
		}
		System.out.println();
	}

	/* @Name : DFStoFile
	 * @Param : concept tc and bufferedwriter object
	 * @Returns : void
	 * prints the lattice in dot format into the bufferedwriter outputstream object
	 */ 
	public static void DFStoFile( Concept tc, BufferedWriter output )
	{
		for( int i = 0 ; i < tc.childnum; i++ )
		{
			try {
				output.write(tc.id + "--" + tc.children[i].id + ";\n" );
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			DFStoFile(tc.children[i], output);
		}
	}

	/* @Name : DFSConcepts
	 * @Param : concept tc and bufferedwriter object
	 * @Returns : void
	 * prints the index and corresponding details into the bufferedwriter outputstream object
	 */ 
	public static void DFSConcepts( Concept tc, BufferedWriter output )
	{
		for( int i = 0 ; i < tc.childnum; i++ )
		{
			try {
				output.write(tc.children[i].id + "--" + tc.children[i].attributes + tc.children[i].objects + "\n" );
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			DFSConcepts(tc.children[i], output);
		}
	}

	/* @Name : difference
	 * @Param : set1, set2
	 * @Returns : set2 - set1 if set2 is larger, else set1 - set2
	 */
	  
	public static Set<String> difference( Set<String> S1, Set<String> S2 )
	{
		Set<String> inter = findIntersection(S1, S2);
		Set<String> first;
		if( S1.size() > S2.size() )
		{
			first = S1;
		}
		else
		{
			first = S2;
		}

		for( String str : inter )
		{
			first.remove(str);
		}

		return first;
	}

	public static void main(String[] args) 
	{

		BufferedReader br = null;
		String line;

		String[] properties; 
		int noOfProperties = 0;

		String[] objects;
		int noOfObjects = 0;

		int[][] matrix;
		StringTokenizer tokenizer;

		try
		{
			//READING INPUT
			if( args.length == 0 )
			{
				System.out.println("\n\t\tUsage : java fca <inputFileName>");
				System.out.println("\t\tTo specify numerical range use the command - java fca <inputFileName> <rangeSpecFileName>\n");
				System.exit(0);
			}
			if(args.length == 1 ) {
				noOfProperties = 0;
				br = new BufferedReader( new FileReader( args[0] ) );

				//Reading the no of objects
				line = br.readLine();
				noOfObjects = Integer.parseInt( line );
				objects = new String[ noOfObjects + 1 ];

				//Reading and storing first title row
				line = br.readLine();
				tokenizer = new StringTokenizer( line );
				noOfProperties = tokenizer.countTokens() - 1;
				properties = new String[ noOfProperties+1 ];
				matrix = new int[ noOfObjects + 1 ][ noOfProperties + 1 ];

				System.out.println(noOfObjects + " objects and " + noOfProperties + " properties" );
				int i = 0;

				while(tokenizer.hasMoreElements())
				{
					properties[i] = tokenizer.nextToken();
					i++;
				}
				objects[0] = properties[0];

				//Reading and storing the table
				int rowNum,columnNum;
				rowNum = 1;
				columnNum = 1;

				line = br.readLine();
				while(line != null)
				{
					tokenizer = new StringTokenizer( line );
					boolean firstElementFlag = true;
					columnNum = 1;
					while( tokenizer.hasMoreElements() )
					{
						if(firstElementFlag)
						{
							objects[rowNum] = tokenizer.nextToken();
							firstElementFlag = false;
						}
						else
						{
							matrix[rowNum][columnNum] = Integer.parseInt( tokenizer.nextToken() );
							columnNum++;
						}

					}
					rowNum++;
					line = br.readLine();
				}
			}

			else
			{
				noOfProperties = 0;


				String[][] rangeMatrix = new String[100][4];
				br= new BufferedReader(new FileReader( args[1]));
				line = br.readLine();
				tokenizer= new StringTokenizer(line);
				int k = 0;
				while(line!=null)
				{
					int j = 0;
					while( tokenizer.hasMoreElements() )
					{
						rangeMatrix[k][j]=tokenizer.nextToken();
						j++;
					}
					k++;
					line = br.readLine();
				}
				br.close();

				br = new BufferedReader( new FileReader( args[0] ) );
				//Reading the no of objects
				line = br.readLine();
				noOfObjects = Integer.parseInt( line );
				objects = new String[ noOfObjects + 1 ];

				//Reading and storing first title row
				line = br.readLine();
				tokenizer = new StringTokenizer( line );
				noOfProperties = tokenizer.countTokens() - 1;
				properties = new String[ noOfProperties+1 ];
				matrix = new int[ noOfObjects + 1 ][ noOfProperties + 1 ];

				//System.out.println(noOfObjects + " objects and " + noOfProperties + " properties" );
				int i = 0;


				while(tokenizer.hasMoreElements())
				{
					properties[i] = tokenizer.nextToken();
					i++;
				}
				objects[0] = properties[0];

				//Reading and storing the table
				int rowNum,columnNum;
				rowNum = 1;
				columnNum = 1;

				line = br.readLine();
				while(line != null)
				{
					tokenizer = new StringTokenizer( line );
					boolean firstElementFlag = true;
					columnNum = 1;
					while( tokenizer.hasMoreElements() )
					{
						if(firstElementFlag)
						{
							objects[rowNum] = tokenizer.nextToken();
							firstElementFlag = false;
						}
						else
						{
							String token = tokenizer.nextToken();
							int tokenint = Integer.parseInt(token);
							if( Integer.parseInt( token ) == 1 
									|| Integer.parseInt( token ) == 0    )
								matrix[rowNum][columnNum] = Integer.parseInt( token );
							else
							{
								for( int j = 0 ; i < noOfProperties; i++ )
								{
									if( rangeMatrix[j][0].equalsIgnoreCase(properties[columnNum]) )
									{
										if ( tokenint >= Integer.parseInt(rangeMatrix[j][1]) && tokenint <= Integer.parseInt(rangeMatrix[j][2]))
											matrix[rowNum][columnNum] = 1;
										else
											matrix[rowNum][columnNum] = 0;
									}
								}
							}
							columnNum++;
						}

					}
					rowNum++;
					line = br.readLine();
				}

			}//END OF INPUT READING
			
			
			Set<String> g = new HashSet<String> ();
			Set<String> m = new HashSet<String> ();
			for( int j = 1 ; j <= noOfObjects; j++ )
			{
				g.add( objects[ j ] );
			}
			for( int j = 1; j <= noOfProperties; j++ )
			{
				m.add( properties[ j ] );
			}
			
			//Creating topConcept
			topConcept = new Concept( g, Attributes(g, objects, noOfObjects, properties, noOfProperties, matrix) );
			topConcept.id = ID++;
			
			//enqueueing topConcept
			enqueue( topConcept );

			Concept c = new Concept();
			Set<String> x = new HashSet<String>();
			List<Set<String>> aChild = new ArrayList<Set<String>>();

			Concept successor;
			while( !queueEmpty( qSize ) )
			{
				c = dequeue( );
				x = Attributes( c.objects, objects, noOfObjects, properties, noOfProperties, matrix );
			
				aChild = AttrChild( x, objects, noOfObjects, properties, noOfProperties, matrix );
			
				Set<String> uset = new HashSet<String>();
				for( Set<String> ss : aChild )
				{
					uset = Union(ss, x);
					if( Attributes(Objects(uset,objects,noOfObjects,properties,noOfProperties,matrix), objects, noOfObjects, properties, noOfProperties, matrix ).size() 
							<= uset.size() )
					{
						boolean childAdded = false;
						successor = new Concept(Objects(uset,objects,noOfObjects,properties,noOfProperties,matrix), ss);
						for( int j = 0; j < qSize; j++ )
						{
							if( successor.attributes.contains(queue[j].attributes) || queue[j].objects.equals(successor.objects) )
							{
								successor.attributes = Union(queue[j].attributes, successor.attributes);
								queue[j].attributes = Union(queue[j].attributes, successor.attributes);
								c.addChild(successor);
								successor.id = queue[j].id;
								childAdded = true;
							}
						}
						if( !childAdded )
						{
							successor.id = ID++;
							c.addChild(successor);
						
							enqueue(successor);
						
						}	
					}

				}
			}	
			//Final check to see if the bottom most concept is already there in the lattice
			boolean alreadyPresent = false;
			for( int j = 0 ;j < qSize; j++ )
			{
					if( queue[j].attributes.equals(m) || queue[j].objects.isEmpty() )
					{
						alreadyPresent = true;
					}
			}
			//If bottom most concept is not present, add it explicitly
			if(!alreadyPresent)
			{	
				successor = new Concept(new HashSet<String>(), m );
				successor.id=ID++;
				c.addChild(successor);
			}

			// Outputting the lattice data in dot language format to file "output.gv"
			File file = new File("output.gv");
			BufferedWriter output = new BufferedWriter(new FileWriter(file));
			output.write("graph fca { \n");
			DFStoFile( topConcept, output );
			output.write("}");
			output.close();
			
			// Outputting the indices and corresponding details into file "index.txt"
			File file2 = new File("index.txt");
			BufferedWriter output2 = new BufferedWriter(new FileWriter(file2));
			output2.write(topConcept.id + "--" + topConcept.attributes + topConcept.objects + "\n" );
			DFSConcepts( topConcept, output2 );
			output2.close();
			
			System.out.println("\nCompleted! Output written to files output.gv and index.txt");
			System.out.println("Use the following command to plot the graph in png format");
			System.out.println("\n\t\tdot -Tpng output.gv -o outputFileName.png\n");

		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

	}
}
