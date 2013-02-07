package dataStructures;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class implements a vector of doubles with the specific functionality required. <p>
 * 
 * @author Juan R. Da Costa Martinez
 */
public class Vector {
    /**
     * Integer representing the dimension of the vector.
     */
    final int N;
    /**
     * Data structure utilized.
     */
    double[] v;
    /**
     * Regular constructor.
     * @param N Dimension of the vector to be created
     */
    public Vector(int N){
        this.N=N;
        v=new double[N];
    }
    /**
     * Constructor that parses the vector elements out of a String.
     * @param in String containing the vector elements
     */
    public Vector(String in){
        ArrayList<Double> aux=new ArrayList<Double>();
        Scanner scan=new Scanner(in);
        //scan.useDelimiter("\s*\n*\s*[,;]\s*\n*\s*");
        while(scan.hasNext()){
            double e=scan.nextDouble();
            aux.add(e);
        }
        scan.close();
        N=aux.size();
        v=new double[N];
        for(int i=0; i<N; i++){
            v[i]=aux.get(i);
        }
        aux.clear();
    }
    /**
     * Public method delivering the dimension of the vector.
     * @return Returns the dimension of the vector
     */
    public int size(){
        return N;
    } 
    /**
     * Public method to access a particular element of the vector.
     * @param i Indicates the index of the element to be accessed
     * @return Returns the value of the index demanded
     */
    public double get(int i){
        return v[i];
    }
    /**
     * Public method to assign a value to a position of the vector.
     * @param i Index of the assignment
     * @param x Value to be assigned
     * @return Returns the modified object
     */
    public Vector set(int i, double x){
        v[i]=x;
        return this;
    }
    /**
     * Public method to premultiply the object by a given {@link Matrix} object
     * @param M Matrix object to be multiplied
     * @return Result of the multiplication between the matrix and vector 
     * @throws Exception Thrown if the dimensions are incompatible
     * @see Matrix
     */
    public Vector mult(Matrix M) throws Exception{   //Mxb
        Vector res;
        if(M.size()!=N){
            throw new Exception("Incompatible dimensions for multiplication.");
        }
        res=new Vector(N);
        for(int i=0; i<N; i++){
            double rowsum=0;
            for(int k=0; k<N; k++){
                rowsum+=M.get(i, k)*v[k];
            }
            res.v[i]=rowsum;
        }
        
        return res;
    }
    /**
     * Public method to print the elements of a {@link Vector} object into a String.
     * @return String containing all the elements of the vector
     */
    public String toString(){
        StringWriter out=new StringWriter();
        for(int i=0; i<N; i++){
            double e=v[i];
            if(e>=0){
                    out.write(" "+String.format("%.5f", e) +"\t");
                }
                else{
                    out.write(String.format("%.5f", e) +"\t");
                }
        }
        out.write("\n");
        String output=out.toString();
        try {
            out.close();
        } catch (IOException ex) {
            Logger.getLogger(Vector.class.getName()).log(Level.SEVERE, null, ex);
        }
        return output;
    }
    /**
     * Public method to print the integer value of the elements in a {@link Vector} object into a String.
     * @return String containing all the elements of the vector
     */
    public String toStringAsInt(){
        StringWriter out=new StringWriter();
        for(int i=0; i<N; i++){
            double e=v[i];
            if(e>=0){
                    out.write(" "+String.format("%d", (int)e) +"  ");
                }
                else{
                    out.write(String.format("%d", (int)e) +"  ");
                }
        }
        out.write("\n");
        String output=out.toString();
        try {
            out.close();
        } catch (IOException ex) {
            Logger.getLogger(Vector.class.getName()).log(Level.SEVERE, null, ex);
        }
        return output;
    }
}
