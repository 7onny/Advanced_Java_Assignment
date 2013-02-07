package dataStructures;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class implements a square matrix of doubles and some matrix manipulation operations.
 * <p>
 * @author Juan R. Da Costa Martinez
 */
public class Matrix {
    /**
     * Dimension attribute, defining a matrix of NxN elements.
     */
    private final int N;
    /**
     * Data structure for the matrix.
     */
    private double m[][];
    /**
     * Regular constructor.
     * @param N Indicates the creation of a NxN square matrix
     */
    public Matrix(int N){
        this.N=N;
        m=new double[N][N];
    }
    /**
     * Copy constructor performing a deep copy of another {@link Matrix} object.
     * @param p matrix to be copied
     */
    public Matrix(Matrix p){
        this.N=p.N;
        m=new double[N][N];
        for(int i=0; i<N; i++){
            for(int j=0; j<N; j++){
                m[i][j]=p.get(i, j);
            }
        }
    }
    /**
     * Constructor that builds a {@link Matrix} object from a String.
     * @param in String containing the elements to be inserted
     * @throws Exception Thrown if the string does not form a square matrix
     */
    public Matrix(String in) throws Exception{
        ArrayList<Double> aux=new ArrayList<Double>();
        Scanner scan=new Scanner(in);
        //scan.useDelimiter("\s*\n*\s*[,;]\s*\n*\s*");
        while(scan.hasNext()){
            double e=scan.nextDouble();
            aux.add(e);
        }
        scan.close();
        Double n=Math.sqrt(aux.size());
        if(n!=n.intValue()){
            N=0;
            m=null;
            throw new Exception("The A matrix must be a square matrix");
        }
        else{
            N=n.intValue();
            m=new double[N][N];
            for(int i=0; i<N; i++){
                for(int j=0; j<N; j++){
                    m[i][j]=aux.get(i*N+j);
                }
            }
        }
        aux.clear();
    }
    /**
     * Public method to recover the underlying matrix data structure contained in the {@link Matrix} object.
     * @return The matrix of elements
     */
    public double[][] getMatrix(){
        return m;
    }
    /**
     * Public method that delivers dimension parameter N, of a NxN matrix.
     * @return Value of N, of a NxN matrix
     */
    public int size(){
        return N;
    }
    /**
     * Public method to access a given index in the matrix.
     * @param i Row of the accessed element
     * @param j Column of the accessed element
     * @return Value contained in the given index
     */
    public double get(int i, int j){
        return m[i][j];
    }
    /**
     * Public method to assign a value to an index in the matrix.
     * @param i Row of the assignment
     * @param j Column of the assignment
     * @param x Value to be inserted in the given index
     * @return The resulting object
     */
    public Matrix set(int i, int j, double x){
        m[i][j]=x;
        return this;
    }
    /**
     * Public method to multiply a {@link Matrix} object by another.
     * @param P Second matrix operand in the multiplication
     * @return The result of the object multiplied by the given matrix
     * @throws Exception Thrown if dimensions are incompatible
     */
    public Matrix mult(Matrix P) throws Exception{   // operation MxP
        Matrix res;
        if(N!=P.N){
            throw new Exception("Incompatible matrix dimensions for multiplication.");
        }
        res=new Matrix(N);
        for(int i=0; i<N; i++){
            for(int j=0; j<N; j++){
                double rowsum=0;
                for(int k=0; k<N; k++){
                    rowsum+=m[i][k]*P.m[k][j];
                }
                res.m[i][j]=rowsum;
            }
        }
        
        return res;
    }
    /**
     * Public method to perform the LU factorization.
     * @param L {@link Matrix} object that will contain the value of the lower triangular matrix
     * @param U {@link Matrix} object that will contain the value of the upper triangular matrix
     * @throws Exception Thrown if the pivot is zero at any given time
     */
    public void LU(Matrix L, Matrix U) throws Exception{
        Matrix aux=new Matrix(this);
        //Factorization
        for(int k=0; k<N-1; k++){
            for(int i=k+1; i<N; i++){
                if(aux.get(k, k)==0){
                    throw new Exception("Pivot is zero in LU factorization, exiting application.");
                }
                double coef=aux.get(i, k)/aux.get(k, k);
                aux.set(i, k, coef);
                for(int j=k+1; j<N; j++){
                    double current=aux.get(i, j);
                    aux.set(i, j, current-(coef*aux.get(k, j)));
                }
            }
        }
        //Assigning L
        for(int i=0; i<N; i++){
            L.set(i, i, 1.0);
        }
        for(int i=1; i<N; i++){
            for(int j=0; j<i; j++){
                L.set(i, j, aux.get(i, j));
            }
        }
        //Assigning U
        for(int i=0; i<N; i++){
            for(int j=i; j<N; j++){
                U.set(i, j, aux.get(i, j));
            }
        }
        aux=null;
    }
    /**
     * Public method to solve the matrix system given the LU decomposition.
     * @param L Lower triangular matrix
     * @param U Upper triangular matrix
     * @param b Right-hand side Vector 
     * @return Solution to the matrix system
     */
    public Vector solve(Matrix L, Matrix U, Vector b){
        Vector sol=new Vector(N);
        for(int i=0; i<N; i++){
            sol.v[i]=b.get(i);
        }
        //FWD Substitution
        for(int i=0; i<N; i++){
            for(int j=0; j<i; j++){
                sol.v[i]-=(L.get(i, j)*sol.v[j]);
            }
        }
        //BWD Substitution
        for(int i=N-1; i>=0; i--){
            for(int j=i+1; j<N; j++){
                sol.v[i]-=(U.get(i, j)*sol.v[j]);
            }
            sol.v[i]/=U.get(i, i);
        }
        
        return sol;
    }
    /**
     * Public method to perform pivoting.
     * @param piv Is a returnable vector containing the order of the rows after the pivoting
     * @return The P matrix to be premultiplied by A and b
     */
    public Matrix reorder(Vector piv){  //Return the matrix P to be premultiplied by A and b
        Matrix P=new Matrix(N);
        double[] scale=new double[N];
        int[] pvt=new int[N];
        Matrix tmp=new Matrix(this);
        //piv will be returned as the pivot vector
        for(int k=0; k<N; k++){
            pvt[k]=k;
        }
        for(int k=0; k<N; k++){ //Produce scale vector
            scale[k]=0;
            for(int j=0; j<N; j++){
                if(Math.abs(scale[k])<Math.abs(tmp.get(k, j))){
                    scale[k]=Math.abs(tmp.get(k, j));
                }
            }
        }
       
        for(int k=0; k<N-1; k++){
            int pc=k;
            double aet=Math.abs(tmp.get(pvt[k], k)/scale[k]);
            for(int i=k+1; i<N; i++){
                double aux=Math.abs(tmp.get(pvt[i], k)/scale[pvt[i]]);
                if(aux>aet){
                    aet=aux;
                    pc=i;
                }
            }
            if(aet==0){
                System.err.println("Pivot is zero in Reorder(), exiting application");
                System.exit(1);
            }
            if(pc!=k){  // swap pvt[k] and pvt[pc]
                int ii=pvt[k];
                pvt[k]=pvt[pc];
                pvt[pc]=ii;
            }
            
            //Proceed to elimination
            int pvtk=pvt[k];
            for(int i=k+1; i<N; i++){
                int pvti=pvt[i];
                if(tmp.get(pvti, k)!=0){
                    double mult=tmp.get(pvti, k)/tmp.get(pvtk, k);
                    tmp.set(pvti,k,mult);
                    for(int j=k+1; j<N; j++){
                        double current=tmp.get(pvti, j);
                        tmp.set(pvti, j, current-(mult*tmp.get(pvtk, j)));
                    }
                }
            }
        }
        for(int i=0; i<N; i++){
            P.set(i, pvt[i], 1.0);
            piv.set(i, pvt[i]+1);
        }
        tmp=null;
        pvt=null;
        scale=null;
        
        return P;
    } 
    /**
     * Public method that inverts the matrix using the Jordan approach.
     * @return The inverse matrix
     * @throws Exception Thrown if pivot is zero at any given time
     */
    public Matrix Invert() throws Exception{
        Matrix res=new Matrix(N);
        Matrix aux=new Matrix(this);
        //Initializing
        for(int i=0; i<N ;i++){
            for(int j=0; j<N; j++){
                if(j==i){
                    res.m[i][i]=1;
                }
                else{
                    res.m[i][j]=0;
                }
            }
        }
        //Start
        for(int k=0; k<N; k++){
            double piv=aux.m[k][k];
            for(int j=0; j<N; j++){
                if(piv==0){
                    throw new Exception("Pivot is zero in Inverse calculation.");
                }
                aux.m[k][j]=aux.m[k][j]/piv;    //setting pivot to 1
                res.m[k][j]=res.m[k][j]/piv;
            }
            
            for(int i=0; i<N; i++){
                double coef=aux.m[i][k];
                for(int j=0; j<N; j++){
                    if(i!=k){
                        aux.m[i][j]=aux.m[i][j]-(coef*aux.m[k][j]);
                        res.m[i][j]=res.m[i][j]-(coef*res.m[k][j]);
                    }
                }
            }
        }
        aux=null;
        return res;
    }
    /**
     * Public method to print the contents of a matrix into a String.
     * @return The resulting String
     */
    public String toString(){
        StringWriter out=new StringWriter();
        for(int i=0; i<N; i++){
            for(int j=0; j<N; j++){
                double e=m[i][j];
                if(e>=0){
                    out.write(" "+String.format("%.5f", e) +"\t");
                }
                else{
                    out.write(String.format("%.5f", e) +"\t");
                }
            }
            out.write("\n");
        }
        String output=out.toString();
        try {
            out.close();
        } catch (IOException ex) {
            Logger.getLogger(Matrix.class.getName()).log(Level.SEVERE, null, ex);
        }
        return output;
    }
    /**
     * Public recursive method used to calculate the determinant of a matrix. 
     * @param matrix Recursion matrix
     * @return The value of the determinant
     */
    public double determinant(double[][] matrix){ //method sig. takes a matrix (two dimensional array), returns determinant.
    double sum=0; 
    int s;
    if(matrix.length==1){  //bottom case of recursion. size 1 matrix determinant is itself.
      return(matrix[0][0]);
    }
    for(int i=0;i<matrix.length;i++){ //finds determinant using row-by-row expansion
      double[][]smaller= new double[matrix.length-1][matrix.length-1]; //creates smaller matrix- values not in same row, column
      for(int a=1;a<matrix.length;a++){
        for(int b=0;b<matrix.length;b++){
          if(b<i){
            smaller[a-1][b]=matrix[a][b];
          }
          else if(b>i){
            smaller[a-1][b-1]=matrix[a][b];
          }
        }
      }
      if(i%2==0){ //sign changes based on i
        s=1;
      }
      else{
        s=-1;
      }
      sum+=s*matrix[0][i]*(determinant(smaller)); //recursive step: determinant of larger determined by smaller.
    }
    return(sum); //returns determinant value. once stack is finished, returns final determinant.
  }
    /**
     * Public method to calculate the determinant of a matrix using the LU decomposition (i.e. det(LU)=det(L)*det(U)).
     * @return The value of the determinant
     */
    public double LUDeterminant(){
        double det=1;
        for(int k=0; k<N; k++){
            det*=m[k][k];
        }
        return det;
    }
}
