
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
public class SAP 
{
	//constructor that takes a digraph
	private Digraph D;
	//This datastructure caches the computed breadth first paths for future reference
	private HashMap<Integer,BreadthFirstDirectedPaths>paths = new HashMap<Integer,BreadthFirstDirectedPaths>();
	public SAP(Digraph G)
	{
		if (G==null) 
			throw new java.lang.NullPointerException();
		//create a local deep copy of the DiGraph
		D = new Digraph(G);
	}
	
	//length of shortest ancestral path between v and w;-1 if no such path
	public int length(int v,int w)
	{
		if (v<0 || v>D.V()-1)
			throw new java.lang.IndexOutOfBoundsException();
		BreadthFirstDirectedPaths bfs_A;
		BreadthFirstDirectedPaths bfs_B;
		if (paths.containsKey(v))
			bfs_A = paths.get(v);
		else
			bfs_A = new BreadthFirstDirectedPaths(D,v);
		
		if (paths.containsKey(w))
			bfs_B = paths.get(w);
		else
			bfs_B = new BreadthFirstDirectedPaths(D,w);
		int min_dist = Integer.MAX_VALUE;
		for (int i = 0;i<D.V();i++)
		{
			if (bfs_A.hasPathTo(i) && bfs_B.hasPathTo(i))
			{
				if(bfs_A.distTo(i)+bfs_B.distTo(i)<min_dist)
				{
					min_dist = bfs_A.distTo(i)+bfs_B.distTo(i);
				}
			}
		}
		if (min_dist==Integer.MAX_VALUE)
			return -1;
		else
			return min_dist;
	}
	
	public int length(Iterable<Integer> v,Iterable <Integer> w)
	{
		if (v==null || w== null) 
			throw new java.lang.NullPointerException();
		for(Integer n:v)
		{
			if (n<0 || n>D.V()-1)
				throw new java.lang.IndexOutOfBoundsException();
		}
		for(Integer n:w)
		{
			if (n<0 || n>D.V()-1)
				throw new java.lang.IndexOutOfBoundsException();
		}
		BreadthFirstDirectedPaths bfs_A;
		BreadthFirstDirectedPaths bfs_B;
		
		bfs_A = new BreadthFirstDirectedPaths(D,v);
		
		
		bfs_B = new BreadthFirstDirectedPaths(D,w);
		int min_dist = Integer.MAX_VALUE;
		for (int i = 0;i<D.V();i++)
		{
			if (bfs_A.hasPathTo(i) && bfs_B.hasPathTo(i))
			{
				if(bfs_A.distTo(i)+bfs_B.distTo(i)<min_dist)
				{
					min_dist = bfs_A.distTo(i)+bfs_B.distTo(i);
				}
			}
		}
		if (min_dist==Integer.MAX_VALUE)
			return -1;
		else
			return min_dist;
	}
	//a common ancestor of v and w that participates in a shortest ancestral path;-1 if no such path
	public int ancestor(int v,int w)
	{
		if (v<0 || v>D.V()-1)
			throw new java.lang.IndexOutOfBoundsException();
		BreadthFirstDirectedPaths bfs_A;
		BreadthFirstDirectedPaths bfs_B;
		
		bfs_A = new BreadthFirstDirectedPaths(D,v);
		
	
		bfs_B = new BreadthFirstDirectedPaths(D,w);
		int dist_common_ancestor = Integer.MAX_VALUE;
		int common_ancestor = -1;
		for (int i = 0;i<D.V();i++)
		{
			if (bfs_A.hasPathTo(i) && bfs_B.hasPathTo(i))
			{
				if(bfs_A.distTo(i)+bfs_B.distTo(i)<dist_common_ancestor)
				{
					dist_common_ancestor = bfs_A.distTo(i)+bfs_B.distTo(i);
					common_ancestor = i;
				}
			}
		}
		return common_ancestor;
	}
	
	//length of shortest ancestral path between any vertex in v and any vertex in w;-1 if no such path
	public int ancestor(Iterable<Integer>v ,Iterable<Integer> w)
	{
		if (v==null || w== null) 
			throw new java.lang.NullPointerException();
		for(Integer n:v)
		{
			if (n<0 || n>D.V()-1)
				throw new java.lang.IndexOutOfBoundsException();
		}
		for(Integer n:w)
		{
			if (n<0 || n>D.V()-1)
				throw new java.lang.IndexOutOfBoundsException();
		}
		BreadthFirstDirectedPaths bfs_A;
		BreadthFirstDirectedPaths bfs_B;
		
		bfs_A = new BreadthFirstDirectedPaths(D,v);
		
		
		bfs_B = new BreadthFirstDirectedPaths(D,w);
		int dist_common_ancestor = Integer.MAX_VALUE;
		int common_ancestor = -1;
		for (int i = 0;i<D.V();i++)
		{
			if (bfs_A.hasPathTo(i) && bfs_B.hasPathTo(i))
			{
				if(bfs_A.distTo(i)+bfs_B.distTo(i)<dist_common_ancestor)
				{
					dist_common_ancestor = bfs_A.distTo(i)+bfs_B.distTo(i);
					common_ancestor = i;
				}
			}
		}
		return common_ancestor;
	}
	
//	public Iterable<Integer> pathTo(Iterable<Integer>v ,int dst)
//	{
//		BreadthFirstDirectedPaths bfs_A = new BreadthFirstDirectedPaths(D,v);
//		return(bfs_A.pathTo(dst));
//	}
	
	//unitTests
	public static void main(String[] args)
	{
////		In in = new In(args[0]);
////		Digraph G = new Digraph(in);
//		WordNet W = new WordNet(args[0],args[1]);
//		SAP sap = W.sap;
//		int length   = sap.length(1, 6);
//        int ancestor = sap.ancestor(1, 6);
//        StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
//        List<Integer> A=Arrays.asList(81679,81680,81681,81682);
//        List<Integer> B=Arrays.asList(24306,24307,25293,33764,70067);
//        length   = sap.length(A, B);
//        ancestor = sap.ancestor(A, B);
//        StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        
	}
}
