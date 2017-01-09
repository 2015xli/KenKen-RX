package kenkenrx;
import java.util.*;
import java.io.File;

class Problem{

    static Vector<Piece> problem; //a Kenken prolem, consisting of computation pieces
    static int dim;
    
    static class Piece{  //one computation piece in Kenken 
        Op op;
        int  num;
        ArrayList<Point> points;
        Piece(Op o, int n, ArrayList<Point> p)
        {
            op = o; num = n; points = p;
        }	
        void output(){
            System.out.println("Piece: "+op+","+num+",");
            for(Point p : points){
                p.output();
            }
        }
    }

    enum Op{ ADD, SUB, MUL, DIV, NOP}  //operator in one piece

    static boolean match_add(int[][] sol, Piece quest){
        int sum = 0;
        for(Point p : quest.points){
            sum += sol[p.x][p.y]; 
        }
        return (sum == quest.num);
    }

    static boolean match_sub(int[][] sol, Piece quest){
        Point p0 = quest.points.get(0);
        Point p1 = quest.points.get(1);
        
        int v1 = sol[p0.x][p0.y];
        int v2 = sol[p1.x][p1.y];

        return ((v1-v2) == quest.num) || ((v2-v1) == quest.num);
    }

    static boolean match_mul(int[][] sol, Piece quest){
        int prod = 1;
        for(Point p : quest.points){
            prod *= sol[p.x][p.y]; 
        }
        return (prod == quest.num);
    }

    static boolean match_div(int[][] sol, Piece quest){
        Point p0 = quest.points.get(0);
        Point p1 = quest.points.get(1);
        
        int v1 = sol[p0.x][p0.y];
        int v2 = sol[p1.x][p1.y];

        return ((v1/v2) == quest.num) || ((v2/v1) == quest.num);
    }

    static boolean match_nop(int[][] sol, Piece quest){
        Point p = quest.points.get(0);
        return (quest.num == sol[p.x][p.y]); 
    }

    static boolean match_cand(Kenkenrx.Candidate cand, Point point)
    {
    	int x = point.x;
    	int y = point.y;
    	
    	if( matchset[x][y].exist ){
    		return match_piece(cand.result, matchset[x][y].piece);
    	}
    	return true;
    }

    static boolean match_piece(int[][] sol, Piece quest)
    {
        boolean ok = false;
        switch(quest.op){
        case ADD:
            ok = match_add(sol, quest);
            break;
        case SUB:
            ok = match_sub(sol, quest);
            break;
        case MUL:
            ok = match_mul(sol, quest);
            break;
        case DIV:
            ok = match_div(sol, quest);
            break;
        case NOP:
            ok = match_nop(sol, quest);
            break;
        default:
            assert(false);
        }
        return ok;
        
    }
    
    boolean match_solution(int[][] sol, Vector<Piece> quests){
        
        boolean ok = false;
        for(Piece quest : quests){
            ok = match_piece(sol, quest);
            if( !ok ) return false;
        }
        return true;
    }

    public static int readFile(Vector<Piece> prob, File f){

        int col = 0;
        int row = 0;
        int last_col = 0;
        try {
            //read the matrix input
            Map<Character, ArrayList<Point>> map = new HashMap<Character, ArrayList<Point>>(); 
            Scanner sc = new Scanner(f);
            while(sc.hasNextLine()) { //read one row of the matrix
                String line = sc.nextLine();
                if(line.length() == 0) break; 
                System.out.println(line);
                Scanner sl = new Scanner(line); 
                sl.useDelimiter("\\s*");
                while(sl.hasNext()){  //read one column of the row
                    char c = sl.next().charAt(0);
                    ArrayList<Point> points = map.get(c);
                    if(points == null){ //the region has not been read yet, new the region
                        points = new ArrayList<Point>();
                    }
                    Point point = new Point(row, col);
                    points.add(point); 
                    map.put(c, points);
                    col++;
                }
                sl.close();	

                if(last_col != 0) assert(last_col == col);
                else last_col = col;
                col = 0;
                row++;
            }
            
            System.out.printf("===%dx%d===\n", row, last_col);
            assert(last_col == row);
            
            //read the region operation input 
            while(sc.hasNextLine()) {
                String line = sc.nextLine();
                System.out.println(line);
                Scanner sl = new Scanner(line);
                sl.useDelimiter("\\s+");

                String a = sl.next();
                assert(a.length()==1);
                char A = a.charAt(0);

                int num = Integer.parseInt(sl.next());
                assert(num != 0);

                Op op = null;
                if(!sl.hasNext()) 
                op = Op.NOP;
                else{
                    String o = sl.next();
                    assert(o.length()==1);
                    char O = o.charAt(0);
                    switch(O){
                    case '+': op = Op.ADD; break;
                    case '-': op = Op.SUB; break;
                    case '*': op = Op.MUL; break;
                    case '/': op = Op.DIV; break;
                    default: assert(false);
                    }
                }
                sl.close();
                assert(op != null);	
                ArrayList<Point> points = map.get(A);
                assert(points!=null);
                Piece p = new Piece(op, num, points);
                //p.output();
                prob.add(p);
            }


            sc.close();
        } 
        catch (Exception e) {
            assert(false);
        }	

        return row;
    } 

    static Point find_max(ArrayList<Point> points)
    {
        Point max = new Point(0,0);
        for( Point point: points){
            if( max.x == point.x){
                if( max.y < point.y ) max.y = point.y;
            }else if ( max.x < point.x) {
                max.x = point.x;
                max.y = point.y;
            }
        } 
        return max;
    }
    
     static class Problem_at{
        boolean exist;
        Piece piece;        
        Problem_at(boolean e, Piece p)
        {
            exist = e;
            piece = p;
        }
    }
    
    public static Problem_at[][] matchset;
    
    static void problem_opt(Vector<Piece> prob)
    {
        matchset = new Problem_at[dim][dim];
        for(int i=0; i<dim; i++){
            for(int j=0; j<dim; j++){
                matchset[i][j] = new Problem_at(false, null);
            }
        }
        
        for(Piece piece: prob){
            Point max_point = find_max(piece.points);
            matchset[max_point.x][max_point.y] = new Problem_at(true, piece);

            //System.out.println(max_point.x + " : " + max_point.y);
            //matchset[max_point.x][max_point.y].piece.output();
        }
        
    }
    
    public static int input(String... args)
    {
        dim = 0;
        problem = null;
        if(args.length != 0){
            File file = new File(args[0]);
            if(file.exists() && !file.isDirectory()){
                problem = new Vector<Piece>();
                dim = readFile(problem, file);
            }else{
                try{
                    dim = Integer.parseInt(args[0]);
                    if(dim > 15){
                        System.out.println("\nYour input number is too big.\n");
                    }
                }catch(Exception e){
                    System.out.println("\nYour input is not a number or a file name.");
                    dim = 0;
                }
            }
            
        }

        if(dim==0){
            return 0;
        }
        
        if(problem != null){ //find the quests that are ready to match against at each point
            problem_opt(problem);
        }

        return dim;
    }
    
}

