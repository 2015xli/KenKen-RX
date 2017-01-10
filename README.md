# A simple solver for KenKen (KenDoku) 
 
 There is another version that does not depend on RxJava under 
 
 https://github.com/2015xli/KenKen
 
 This version uses RxJava. To run it, please include RxJava package in the classpath.
 
 For SuDoKu solutions, input an integer that represents the matrix size, such as:
 ```java
 java -cp rxjava.jar:. kenkenrx.Kenkenrx 5
 ```
 It outputs all the SuDoKu solutions for 5x5 matrix.
 
 For KenKen solutions, provide a file that encodes a KenKen problem, such as:
 ```java
 java -cp rxjava.jar:. kenkenrx.Kenkenrx input/kenken.5
 ```
 It uses file ./input/kenken.5 as input and computes its solution.
 
The package includes a few example files for KenKen problems under ./input directory. 
Please just follow them to create your own KenKen problems.
