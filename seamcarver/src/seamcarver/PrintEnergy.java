package seamcarver;
import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdOut;

public class PrintEnergy {

    public static void main(String[] args) {
        Picture picture = new Picture(args[0]);
        StdOut.printf("image is %d pixels wide by %d pixels high.\n", picture.width(), picture.height());
        
        SeamCarver sc = new SeamCarver(picture);
        
        StdOut.printf("Printing energy calculated for each pixel.\n");        

        for (int row = 0; row < sc.height(); row++) {
            for (int col = 0; col < sc.width(); col++)
                StdOut.printf("%9.0f ", sc.energy(col, row));
            StdOut.println();
        }
        int[] vseam=sc.findVerticalSeam();
        System.out.println();
        for(int w:vseam)
        {
        	System.out.printf("\t%d",w);
        }
        int[]hseam=sc.findHorizontalSeam();
        System.out.println();
        for(int w:hseam)
        {
        	System.out.printf("\t%d",w);
        }
    }

}