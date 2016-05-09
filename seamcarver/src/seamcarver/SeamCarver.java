package seamcarver;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Picture;
import java.awt.Color;
import java.util.*;


public class SeamCarver {
	
	private Picture current;
	private double[][] energy_matrix; 
	public SeamCarver(Picture picture)
	{
		current = new Picture(picture);
		updateEnergy();
	}
	
	public Picture picture()
	{
		Picture temp = current;
		current = new Picture(current);
		return temp;
	}
	
	public int width()
	{
		return current.width();
	}
	
	public int height()
	{
		return current.height();
	}
	
	public double energy(int x, int y)
	{
		if (x<0 || x >= current.width() || y < 0 || y >= current.height())
			throw new java.lang.IndexOutOfBoundsException();
		if (x==0 || x == current.width()-1 || y == 0 || y == current.height()-1)
			return 1000;
		return Math.sqrt(delta_x(x,y)+delta_y(x,y));
	}
	
	private double delta_x(int x,int y)
	{
		Color right = current.get(x+1, y);
		Color left = current.get(x-1, y);
		double delta_red = right.getRed() - left.getRed();
		double delta_blue = right.getBlue() - left.getBlue();
		double delta_green = right.getGreen() - left.getGreen();
		return Math.pow(delta_red, 2) + Math.pow(delta_blue, 2) + Math.pow(delta_green, 2);	
	}
	
	private double delta_y(int x,int y)
	{
		Color below = current.get(x, y+1);
		Color top = current.get(x, y-1);
		double delta_red = below.getRed() - top.getRed();
		double delta_blue = below.getBlue() - top.getBlue();
		double delta_green = below.getGreen() - top.getGreen();	
		return Math.pow(delta_red, 2) + Math.pow(delta_blue, 2) + Math.pow(delta_green, 2);		
	}
	
	private void updateEnergy()
	{
		this.energy_matrix = new double[current.width()][current.height()];
		for (int i = 0;i<current.width();i++)
		{
			for (int j = 0;j<current.height();j++)
			{
				this.energy_matrix[i][j]=energy(i,j);
			}
		}
	}
	
	public int[] findVerticalSeam()
	{
		/*update enegyMatrix*/
		updateEnergy();
		int[]vSeam = new int[current.height()];
		/*considering the energyMatrix as a graph, topological order is from left to right, top to bottom*/
		double[][]distTo = new double[current.width()][current.height()];
		int[][]edgeTo = new int[current.width()][current.height()];
		
		int edgeToSink=9999;
		double costToSink=Double.POSITIVE_INFINITY;
		/* Initiall set distTo all vertices as infinity*/
		for (int i = 0 ;i<current.width();i++)
		{
			for (int j = 0;j<current.height();j++)
			{
				distTo[i][j]=Double.POSITIVE_INFINITY;
			}
		}
		/* Imagining a virtual source connected to all vertices in the top row */
		for (int i = 0;i< current.width();i++)
			{
				edgeTo[i][0]= -9999;
				distTo[i][0]= this.energy_matrix[i][0];
			}
		/* Start relaxing vertices in 1st row onwards*/
		for (int j = 0;j<current.height();j++)
		{
			for (int i = 0 ;i<current.width();i++)
			{
				/*Relax logic here- Relax all vertices reachable from me*/
				//can I go south, if yes relax vertex to the south
				if (j<current.height()-1)
				{
					if (distTo[i][j+1]>distTo[i][j]+this.energy_matrix[i][j+1])
						{
							distTo[i][j+1]=distTo[i][j]+this.energy_matrix[i][j+1];
							edgeTo[i][j+1]=i;
						}
				}
				// can I go south-west
				if ( j < current.height()-1  && i > 0 )
				{
					if (distTo[i-1][j+1]>distTo[i][j]+this.energy_matrix[i-1][j+1])
						{
							distTo[i-1][j+1]=distTo[i][j]+this.energy_matrix[i-1][j+1];
							edgeTo[i-1][j+1]=i;
						}
				}
				// can I go south East
				if ( j < current.height()-1  && i < current.width()-1 )
				{
					if (distTo[i+1][j+1]>distTo[i][j]+this.energy_matrix[i+1][j+1])
						{
							distTo[i+1][j+1]=distTo[i][j]+this.energy_matrix[i+1][j+1];
							edgeTo[i+1][j+1]=i;
						}
				}
			}
			
		}
		/* Relax cost to reach sink*/
		for (int j=0;j<current.width();j++)
		{
			if (costToSink > distTo[j][current.height()-1])
			{
				costToSink=distTo[j][current.height()-1];
				edgeToSink=j;
			}
		}
		/*Prepare a array to return*/
		if (costToSink != Double.POSITIVE_INFINITY)
		{
			int source = edgeToSink;
			int row = current.height()-1;
			while (row>=0)
			{
				vSeam[row]=source;
				source=edgeTo[source][row];
				row = row-1;
			}
			return vSeam;
		}
		else
			return null;	
	}
	
	private void transpose()
	{
		Picture temp = new Picture(current.height(),current.width());
		for (int i =0;i<current.height();i++)
		{
			for (int j =0;j<current.width();j++)
			{
				temp.set(i, j, current.get(j, i));
			}
		}
		current=temp;
	}
	
	public int[] findHorizontalSeam()
	{
		transpose();
		int[] hseam=findVerticalSeam();
		transpose();
		return hseam;	
	}
	
	public void removeVerticalSeam(int[] seam) 
	{
		/*sanity checking, every value in seam should be between 0 and width*/
		for(int val:seam)
		{
			if (val <0 || val>=current.width())
				throw new java.lang.IllegalArgumentException();	
		}
		/*size of seam should be equal to height*/
		if (seam.length!=current.height())
			throw new java.lang.IllegalArgumentException();	
		/*check that any two elemnts in seam are part of the digraph path*/
		for (int i = 0;i<seam.length-1;i++)
		{
			if (Math.abs(seam[i]-seam[i+1])>=2)
				throw new java.lang.IllegalArgumentException();	
		}
		/*width of the picture is reduced by one after removal of the 
		 * vertical seam, pixel in the seam are not copied over into the new picture*/
		Picture temp = new Picture(current.width()-1,current.height());
		int target_slot=0;
		for (int i = 0;i<current.height();i++)
		{
			for (int j =0;j<current.width();j++)
			{
				if(j!=seam[i])
				{
					temp.set(target_slot, i, current.get(j, i));
					target_slot++;
				}
			}
			/*Refresh after each row*/
			target_slot=0;
		}
		current=temp;
	}
	public void removeHorizontalSeam(int[] seam)
	{
		/*sanity checking, every value in seam should be between 0 and height*/
		for(int val:seam)
		{
			if (val <0 || val>=current.height())
				throw new java.lang.IllegalArgumentException();
		}
		/*size of seam should be equal to height*/
		if (seam.length!=current.width())
			throw new java.lang.IllegalArgumentException();	
		/*check that any two elements in seam are part of the digraph path*/
		for (int i = 0;i<seam.length-1;i++)
		{
			if (Math.abs(seam[i]-seam[i+1])>=2)
				throw new java.lang.IllegalArgumentException();	
		}
		/*Height of the picture will be reduced by 1
		 * after removal of horizontal seam, pixel in the seam
		 * are not copied over into the new picture*/
		Picture temp = new Picture(current.width(),current.height()-1);
		int target_slot=0;
		for(int col=0;col<current.width();col++)
		{
			for(int row=0;row<current.height();row++)
			{
				if(row!=seam[col])
				{
					temp.set(col, target_slot, current.get(col, row));
					target_slot++;
				}
			}
			/*after each col target_slot is refreshed*/
			target_slot=0;
		}
		current=temp;
	}

}
