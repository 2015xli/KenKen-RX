package kenkenrx;

public class Point{
    int x;
    int y;
    
    public Point(int r, int c){ x=r; y=c;}
    public void output()
    {
      System.out.println( "(" + x + "," + y + ")" );
    }
    public Point next(int dim)
    {
        if( y+1 == dim && x+1 != dim){
            return new Point(x+1, 0);  //last column while not last row, wrap around to continue next row
        }else if(y+1 != dim){	
            return new Point(x, y+1);   //not last column, continue in next column
        }else{    //(s,t) is in last column and last row. finish this solution finding
            return null;
        }
        
    }
 }
   
