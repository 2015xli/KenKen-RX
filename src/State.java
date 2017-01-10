package kenkenrx;
import java.util.*;

public class State{    
	static int dim;
    public int[] avails;       //array recording the available values that can be used by the point
    public int[][] col_avails;   //array recording the used values in the same column of the point
    public int[][] row_avails;   //array recording the used values in the same row of ths point
    public boolean inited;

    public void output(){
        System.out.println("    row avail:" + Arrays.deepToString(row_avails));
        System.out.println("    col avail:" + Arrays.deepToString(col_avails));
        System.out.println("    avail val:" + Arrays.toString(avails));
    }
    
    public static int[] INIT_VALS;
    
    
    public State(State old)
    {
        avails = new int[dim];
        col_avails = new int[dim][dim];
        row_avails = new int[dim][dim];
        
        Kenkenrx.arraycopy(col_avails, old.col_avails);
        Kenkenrx.arraycopy(row_avails, old.row_avails);
        inited = true;
    }

    public State()
    {
        col_avails = new int[dim][];
        row_avails = new int[dim][];
    }

    public static void init(int inp)
    {
    	dim = inp;
        INIT_VALS = new int[dim];
        for(int i=0; i<dim; i++){  //initialize the default available values
            INIT_VALS[i] = 1;
        }
    }

}