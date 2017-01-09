 This is a simple solver for KenKen (KenDoku) and SuDoKu.
 
 It uses RxJava. To run it, please include RxJava package in the classpath.
 
 For SuDoKu solutions, input an integer that represents the matrix size, such as:
  		java -cp rxjava.jar:. kenkenrx.Kenkenrx 5
  
 For KenKen solutions, provide a file that encodes a KenKen problem, such as:
 	    java -cp rxjava.jar:. kenkenrx.Kenkenrx input/kenken.5
  
It includes a few example files for KenKen problems under ./input directory. 
Please just follow them to create your own KenKen problems.
