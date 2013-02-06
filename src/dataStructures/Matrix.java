package dataStructures;

/**
 *
 * @author Jonny
 */
public class Matrix {
    private final int N;
    private double m[][];
    
    public Matrix(int N){
        this.N=N;
        m=new double[N][N];
    }
    
    public Matrix(Matrix p){
        this.N=p.N;
        m=new double[N][N];
        for(int i=0; i<N; i++){
            for(int j=0; j<N; j++){
                m[i][j]=p.get(i, j);
            }
        }
    }
    
    public int size(){
        return N;
    }
    
    public double get(int i, int j){
        return m[i][j];
    }
    
    public Matrix set(int i, int j, double x){
        m[i][j]=x;
        return this;
    }
    
    public Matrix mult(Matrix P){   // operation MxP
        Matrix res;
        if(N!=P.N){
            System.err.println("Incompatible matrix dimensions for multiplication.");
            System.exit(1);
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
    
    public void LU(Matrix L, Matrix U){
        Matrix aux=new Matrix(this);
        //Factorization
        for(int k=0; k<N-1; k++){
            for(int i=k+1; i<N; i++){
                if(aux.get(k, k)==0){
                    System.err.println("Pivot is zero in LU factorization, exiting application.");
                    System.exit(1);
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
    
    public Matrix reorder(){  //Return the matrix P to be premultiplied by A and b
        Matrix P=new Matrix(N);
        double[] scale=new double[N];
        int[] pvt=new int[N];
        Matrix tmp=new Matrix(this);
        
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
        }
        tmp=null;
        pvt=null;
        scale=null;
        
        return P;
    } 
    
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
    
}
