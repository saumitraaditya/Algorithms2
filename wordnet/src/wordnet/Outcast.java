
import java.util.HashMap;

import edu.princeton.cs.algs4.In;
public class Outcast {
	private HashMap<String,Integer>distMap;
	private WordNet W;
	public Outcast(WordNet wordnet)
	{
		if (wordnet== null) 
			throw new java.lang.NullPointerException();
		distMap=new HashMap<String,Integer>();
		W = wordnet;
	}
	
	private int distance(String nounA,String nounB)
	{
		String query=(nounA.compareTo(nounB)>=0) ? (nounA+nounB) :(nounB+nounA);
		if (distMap.containsKey(query))
		{
			return distMap.get(query);
		}
		else
		{		
			distMap.put(query, W.distance(nounA, nounB));
			return distMap.get(query);
		}
	}
	
	public String outcast(String[] nouns)
	{
		if (nouns== null) 
			throw new java.lang.NullPointerException();
		int dist=0;
		int max_dist=Integer.MIN_VALUE;
		String out_cast=null;
		for (int i=0;i<nouns.length;i++)
		{
			for(int j=0;j<nouns.length;j++)
			{
				if (i!=j)
					{
						dist+=distance(nouns[i],nouns[j]);
						//System.out.println(nouns[i]+":"+nouns[j]+String.valueOf(distance(nouns[i],nouns[j])));
					}
			}
			if (dist>=max_dist)
			{
				max_dist=dist;
				out_cast=nouns[i];
			}
			//System.out.println(nouns[i]+" "+String.valueOf(dist));
			dist=0;
		}
		return out_cast;
	}
	
	public static void main(String[] args)
	{
		WordNet wordnet=new WordNet(args[0],args[1]);
		Outcast outcast=new Outcast(wordnet);
		for(int t=2;t<args.length;t++)
		{
			In in =new In(args[t]);
			String[]nouns=in.readAllStrings();
			System.out.println(args[t]+":"+outcast.outcast(nouns));
		}
	}

}
