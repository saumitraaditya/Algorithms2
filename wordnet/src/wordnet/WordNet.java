
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.Digraph;
import java.util.HashMap;
import java.util.ArrayList;
import edu.princeton.cs.algs4.DirectedCycle;

public class WordNet 
{
	private HashMap<String,ArrayList<Integer>>nounMap = new HashMap<String,ArrayList<Integer>>();
	private HashMap<Integer,String>synMap = new HashMap<Integer,String>();
	private Digraph D;
	private SAP sap;
	// takes as input the name of the files.
	public WordNet (String synsets, String hypernyms) 
	{
		if (synsets == null || hypernyms==null) 
			throw new java.lang.NullPointerException();
		readSynsets(synsets);
		readHypernyms(hypernyms);
		int root=0;
		DirectedCycle DC=new DirectedCycle(D);
		for (Integer synsetID:synMap.keySet())
		{
			if(D.outdegree(synsetID)==0)
				root++;
		}
		if(root>1 || DC.hasCycle())
			throw new java.lang.IllegalArgumentException();
		sap = new SAP(D);
	}
	
	private void readHypernyms(String filename)
	{
		In in = new In(filename);
		ArrayList<String> lines = new ArrayList<String>();
		while (in.hasNextLine())
		{
			lines.add(in.readLine());
		}
		
		
		
		for (int i = 0;i<lines.size();i++)
		{
			String[] relations=lines.get(i).split(",");
			for (int j = 1;j<relations.length;j++)
			{
				D.addEdge(Integer.parseInt(relations[0]), Integer.parseInt(relations[j]));
			}
		}
	}
	
	private void readSynsets(String filename)
	{
		In in = new In(filename);
		int numV=0;
		while (in.hasNextLine())
		{
			String line = in.readLine();
			String[]contents=line.split(",");
			String[]nouns = contents[1].split(" ");
			int synsetID=Integer.parseInt(contents[0]);
			synMap.put(synsetID, contents[1]);
			for (int i =0;i<nouns.length;i++)
			{
				//check if noun is in nounMap
				if(!nounMap.containsKey(nouns[i]))
				{
					nounMap.put(nouns[i], new ArrayList<Integer>());
					nounMap.get(nouns[i]).add(synsetID);
				}
				else
				{
					nounMap.get(nouns[i]).add(synsetID);
				}
			}
			numV++;		
		}
		//System.out.println(numV);
		D = new Digraph(numV);
	}
	
	//returns all wordnet nouns
	public Iterable<String> nouns()
	{
		return nounMap.keySet();
	}
	
	//is the word a wordnet noun
	public boolean isNoun(String word)
	{
		if (word == null) 
			throw new java.lang.NullPointerException();
		return nounMap.containsKey(word);
	}
	
	//distance between nounA and nounB (defined below)
	public int distance(String nounA,String nounB)
	{		
		if (nounA == null || nounB== null) 
			throw new java.lang.NullPointerException();
		if (!isNoun(nounA) || !isNoun(nounB))
			throw new java.lang.IllegalArgumentException();
		return sap.length(nounMap.get(nounA), nounMap.get(nounB));
	}
//	
//	//a synset that is common ancestor of nounA and nounB in a shortest ancestral path
	public String sap(String nounA,String nounB)
	{
		if (nounA == null || nounB== null) 
			throw new java.lang.NullPointerException();
		if (!isNoun(nounA) || !isNoun(nounB))
			throw new java.lang.IllegalArgumentException();
		int ancestor = sap.ancestor(nounMap.get(nounA), nounMap.get(nounB));
//		for (Integer I:nounMap.get(nounA))
//			System.out.println(I);
//		for (Integer I:nounMap.get(nounB))
//			System.out.println(I);
		return synMap.get(ancestor);
	}
	
//	private void printShortestPath(String noun,int synsetID)
//	{
//		for(Integer I:sap.pathTo(nounMap.get(noun),synsetID))
//		{
//			System.out.println(synMap.get(I));
//		}
//	}
	
	//unitTest
	public static void main(String[] args)
	{
		WordNet W = new WordNet(args[0],args[1]);
		//System.out.println(W.sap("worm", "bird"));
		//System.out.println(W.distance("worm", "bird"));
		//W.printShortestPath("edible_fruit", 60600);
		//System.out.println(W.distance("worm", "fauna"));
	}

}
