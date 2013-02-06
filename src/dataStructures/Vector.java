/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dataStructures;

/**
 *
 * @author Jonny
 */
public class Vector {
    final int N;
    double[] v;
    
    public Vector(int N){
        this.N=N;
        v=new double[N];
    }
    public double get(int i){
        return v[i];
    }
    public Vector set(int i, double x){
        v[i]=x;
        return this;
    }
    
    public Vector mult(Matrix M){   //Mxb
        Vector res;
        if(M.size()!=N){
            System.err.println("Incompatible dimensions for multiplication.");
            System.exit(1);
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
    
}
