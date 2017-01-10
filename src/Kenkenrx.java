package kenkenrx;

import rx.Observable;
import java.util.ArrayList;

class Kenkenrx{

    static int dim;
    static ArrayList<Candidate> list = new ArrayList<>();
    
    static class Candidate{
        Point point;
        State state;
        int[][] result;

        public Candidate()
        {
           super();
        }
        
        public Candidate(Candidate cand)
        {
        	result = new int[dim][dim];
            arraycopy(result, cand.result);
            state = new State(cand.state);
            point = new Point(cand.point.x, cand.point.y);            
        }
                
        public static Candidate first()
        {
            Candidate cand = new Candidate();
            cand.point = new Point(0,0);
            cand.state = new State();
            cand.result = new int[dim][dim];
            
            for(int i=0; i<dim; i++){
                cand.state.avails = State.INIT_VALS.clone();          
                cand.state.row_avails[i] = State.INIT_VALS.clone();
                cand.state.col_avails[i] = State.INIT_VALS.clone();                
            }
            cand.state.inited = true;
            return cand;
        }
        
        public Candidate tryValue(int i)
        {
            if ( state.avails[i] == 0 )  //value i not available for current point
            	return null;

            int s = point.x; //last point
            int t = point.y;            
            Point nextp = point.next(dim); //last point of the matrix           
            if(nextp == null){
                result[s][t] = i+1; //set point(s,t) with value i, last one.
                this.point = null;
            	return this;
            }

            Candidate next = new Candidate(this);
            next.point = nextp;
            
            next.result[s][t] = i+1; //set point(s,t) with value i
            
            if( Problem.problem != null ){ //enable a piece to match against
                boolean ok = Problem.match_cand(next, point);
                if( !ok ) return null;
            }
            
            next.state.col_avails[t][i] = 0;
            next.state.row_avails[s][i] = 0;

            int x = next.point.x;  //new point
            int y = next.point.y;

            int[] col_avails = next.state.col_avails[y]; 
            int[] row_avails = next.state.row_avails[x];
            int[] avails = next.state.avails; 

            
            //only the values avaliable to both column and row directions are avaliable to point (x, y)
            for(int k=0; k<dim; k++){
                if( row_avails[k]==1 && col_avails[k]==1 ){
                    avails[k] = 1;  //value k+1 is available 
                }                
            }
            
            return next;
        }
        
        void output()
        {
        	System.out.println(" ");
            for(int i=0; i<dim; i++){
                for(int j=0; j<dim; j++){
                    System.out.printf("%2d ", result[i][j]);	
                }
                System.out.println(" ");
            } 
        } 
    }
    
    
    static void arraycopy(int[][] t, int[][] s){
        for(int i=0; i<dim; i++){
            for(int j=0; j<dim; j++)
            t[i][j] = s[i][j];
        }

    }
    
    static Observable<Candidate> find(Candidate cand)
    {        
    	if( cand.point == null) 
    		return Observable.just(cand);
    	
        return Observable.range(0, dim)
						.map(i->cand.tryValue(i))
						.filter(c->c!=null)
						.flatMap(Kenkenrx::find);   
    }
    
    public static void output_help(){	
        System.out.println("");
        System.out.println("\nPlease remember to include RxJava package in your classpath.\n");
        System.out.println("Please input a number for Soduku or input a file for Kenken. For examples:");
        System.out.println("  - To get all the 5x5 Soduku results");
        System.out.println("         java kenkenrx.Kenkenrx 5");
        System.out.println("  - To solve a Kenken problem in file ./input/kenken.5");
        System.out.println("         java kenkenrx.Kenkenrx input/kenken.5");
        System.out.println("");
    }

    public static void main(String... args)
    {
    	dim = Problem.input(args);

        if(dim==0){
        	output_help();
        	return;
        }
        	
        State.init(dim);
                
        Candidate point00 = Candidate.first();

        Observable.just(point00)
        	.flatMap(Kenkenrx::find)
        	.subscribe(l->{ list.add(l); 
        		l.output(); });
        
        System.out.println("\nNumber of solutions:" + list.size());

    }
   
    
}
