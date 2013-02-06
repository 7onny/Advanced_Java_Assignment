/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package main;
import dataStructures.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author Jonny
 */
public class ConsoleTest {
    static final String input_file="LU_test.txt";
    //static final String input_file="PIVOT_test.txt";
    static final String output_file="LU_result.txt";
    //static final String output_file="PIVOT_result.txt";
    static final boolean pivot=true;    //enable pivoting 
    
    public static void main(String[] args){
       Matrix A,L,U,P,I;
       Vector b;
       Vector res;
       int N;
        //INPUT
        File input=new File(input_file);
        try {
            Scanner in = new Scanner(input);
            N=in.nextInt();
            if(N<1){
                System.err.println("Invalid N value");
                System.exit(1);
            }
            A=new Matrix(N);
            L=new Matrix(N);
            U=new Matrix(N);
            P=null;
            I=null;
            b=new Vector(N);
            res=new Vector(N);
            for(int i=0; i<N; i++){
                for(int j=0; j<N; j++){
                    A.set(i, j, in.nextDouble());
                }
            }
            for(int i=0; i<N; i++){
                b.set(i,in.nextDouble());
            }
            in.close();
            
            //INPUT Confirmation
            PrintWriter out=new PrintWriter(output_file);
            out.println("A and b-------------------------");
            for(int i=0; i<N; i++){
                for(int j=0; j<N; j++){
                    out.print(A.get(i, j)+"\t");
                }
                out.println("  \t"+b.get(i));
            }

            //PROCESS
            try{
                I=A.Invert();
            } catch(Exception e){
                System.err.println(e.getMessage());
                System.exit(1);
            }
            if(pivot){  //Pivot if enabled
                P=A.reorder();
                A=P.mult(A);    //A=PxA
                b=b.mult(P);    //b=Pxb
            }
            A.LU(L, U);
            res=A.solve(L, U, b);
            
            
            //OUTPUT
            if(pivot){
                out.println("P-----------------------------");
                for(int i=0; i<N; i++){
                    for(int j=0; j<N; j++){
                        out.print(P.get(i, j)+"\t");
                    }
                    out.println("");
                }
            }
            out.println("L-----------------------------");
            for(int i=0; i<N; i++){
                for(int j=0; j<N; j++){
                    out.print(L.get(i, j)+"\t");
                }
                out.println("");
            }
            out.println("\nU-----------------------------");
            for(int i=0; i<N; i++){
                for(int j=0; j<N; j++){
                    out.print(U.get(i, j)+"\t");
                }
                out.println("");
            }
            out.println("\nX-----------------------------");
            for(int i=0; i<N; i++){
                out.println(res.get(i));
            }
            out.println("\nInverse-----------------------------");
            for(int i=0; i<N; i++){
                for(int j=0; j<N; j++){
                    out.print(I.get(i, j)+"\t");
                }
                out.println("");
            }
            out.close();
       
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ConsoleTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}
