import java.lang.Math;
//package found at https://farenda.com/java/java-format-double-2-decimal-places/:
import java.text.DecimalFormat;

public class NQubit extends ParentQubit{
	/* Default Constructor
	 * Constructor without input arguments
	 * Initialize value to white or |0> with positive phase
	 */
	private int numberQubits;

	public NQubit(int numqubits){
        // Constructor: initialize all bits to 0
        	int length = (int)Math.pow(2,numqubits);
        	this.value = new float[length];
        	this.value[0] = 1;
		for(int i = 1; i<length; i++) this.value[i] = 0;
		numberQubits = numqubits;
	}
        public ParentQubit mergeQubits(ParentQubit pq){
        // this merges two sets of qubits and returns a new one that has
        // a number of qubits that is the sum of the two.
        //should return value pq as latter value
        	int length = pq.value.length * this.value.length;
		int size = (int)(Math.log(length)/Math.log(2));
        	NQubit retval = new NQubit(size);
               	for(int val2 = 0; val2<this.value.length; val2++){
			for(int val1 = 0; val1<pq.value.length; val1++){
				retval.value[val2*pq.value.length + val1] = this.value[val2] * pq.value[val1];
			}
		}
        	return retval;
        }
        
	public String toBraKet(){
        // this prints out the state in bra-ket notation, like last week
        //Found Integer.toBinaryString function from https://beginnersbook.com/2014/07/java-program-to-convert-decimal-to-binary/
		String retval = "";
		
		//use DecimalFormat (credited at the top)
		float goval;
		String dec = "#.##";
		for(int i = 1; i<this.value.length; i++) dec = "#" + dec;
	        DecimalFormat df = new DecimalFormat(dec);
		int size = (int)(Math.log(this.value.length)/Math.log(2));
		for(int i = 0; i<this.value.length; i++){
			goval = Math.abs(this.value[i]);
			if((goval > .01) && (goval < .99)){
				//System.out.println(goval);
				//check for phase value
				if(this.value[i] < 0) retval += "- ";
				else if((i>0) && retval.length() > 1) retval += "+ ";
				retval+= df.format(goval);
				retval+="|";
				retval += explicitBin(size,i);
				retval+="> ";
			}
			else if(goval > .99){
				if(value[i] < 0) retval+="-";
				retval+="|";
				retval += explicitBin(size,i);
				retval+=">";
				return retval;
			}
		}

		return retval;
	}


	private String explicitBin(int length, int value){
		int max = (int)Math.pow(2, length-1);
		String retval = new String();
		for(int i = max; i > 0; i/=2){
			if(value-i >= 0){
				retval += "1";
				value-=i;
			}
			else retval += "0";
		}
		return retval;
	}
        public void applyNotGate(){
        // apply a not gate to every qubit
        	int length = this.value.length;
		float sendval[][] = new float[length][length];
        	for(int i = 0; i<length; i++){
        		for(int ii = 0; ii<length; ii++){
        			if((length - ii-1) == i) sendval[i][ii] = 1;
        			else sendval[i][ii] = 0;
        		}
        	}
        	
		//printMatrix(sendval);        	
		        	
        	this.value = arrayProduct(1, sendval, this.value);
        }

        public void applyNotGate(int qb){
        // apply a not gate to every qubit
        	int length = this.value.length;
        	int portionlength = length/(int)Math.pow(2,qb+1);
        	float sendval[][] = new float[length][length];
        	int baseblock[][] = makeIdentity(portionlength);
		int padlength = 0;
		
		int x, y;

        	for(int i = 0; i<length; i++){
        		for(int ii = 0; ii<length; ii++){ 
				x = ii - (portionlength + padlength);
				y = i - (padlength);
        			if((x>=0) && (y>=0) && (x<portionlength) && (y<portionlength)){
				       sendval[i][ii] = baseblock[y][x];
				       sendval[ii][i] = baseblock[y][x];
				}
        			else if(sendval[i][ii]!=1) sendval[i][ii] = 0;
        		}
        		if((i +1) == (padlength+portionlength)) padlength += 2*portionlength;
        	}
        	this.value = arrayProduct(1, sendval, this.value);
        }		
        private void printMatrix(float[][] given){
		for(int i = 0; i<given[0].length; i++){
		       	for(int ii = 0; ii<given[0].length; ii++) System.out.print((int)given[i][ii]+" ");
			System.out.print("________________"+this.value[i]);
			System.out.print("\n");
		}
	
		System.out.print("\n");

	}

        private void printMatrix(int[][] given){
		for(int i = 0; i<given[0].length; i++){
		       	for(int ii = 0; ii<given[0].length; ii++) System.out.print(given[i][ii]+" ");

			System.out.print("________________"+this.value[i]);
			System.out.print("\n");
		}

	}
        public void applyHGate(){
		float hold = 1/fsqrt(2);
		float baseH[][] = {{1,1},{1,-1}};
		float expandedH[][] = baseH;
		for(int i = 1; i<numberQubits; i++) expandedH = kronecker(expandedH,baseH);
		this.value = arrayProduct(fpow(hold,numberQubits), expandedH, this.value);
	}
        public void applyHGate(int qb){
        // apply an H gate to the qubit in position qb, where numbering starts at 0
		int length = this.value.length;
        	int lenshort = this.value.length/(int)(Math.pow(2, qb+1));
        	float sendval[][] = new float[length][length];
        	for(int i = 0; i<length; i++){
			if(sendval[i][i] == 0){
				sendval[i][i] = 1;
				sendval[i][i+lenshort] = 1;
				sendval[i+lenshort][i] = 1;
				sendval[i+lenshort][i+lenshort] = -1;
			}
        	}
        	this.value = arrayProduct(1/fsqrt(2), sendval, this.value);
        }
	
        public void applySwapGate(int qubit1, int qubit2){
        // apply a swap gate between qubit1 and qubit2, where numbering starts at 0 
        	if(qubit1 > this.value.length || qubit2 > this.value.length || qubit1 == qubit2) return;
        	if(qubit2 < qubit1){
        		int hold = qubit2;
        		qubit2 = qubit1;
        		qubit1 = hold;
        	}
	 	for(int i = qubit1; i<qubit2; i++){
			//System.out.println("..."+i);
	 		this.value = arrayProduct(1, makeSwap(this.value.length,i),this.value);
	 	}
	 	for(int i = qubit2-2; i>=qubit1; i--){
			//System.out.println("..."+i);
	 		this.value = arrayProduct(1, makeSwap(this.value.length,i),this.value);
	 	}
	 }
	 
	 
	
	private float[][] makeSwap(int size, int start){
 		float[][] retval;
 		float[][] identity = {{1,0},{0,1}};
 		float[][] swap = {{1, 0, 0, 0},{0, 0, 1, 0},{0, 1, 0, 0},{0, 0, 0, 1}};
		if(start == 0) retval = swap.clone();
		else retval = identity.clone();	
		for(int i = 1; i < start;i++) retval = kronecker(retval, identity);
 		if(start !=0) retval = kronecker(retval, swap);
		while(retval[0].length < size) retval = kronecker(retval, identity);
		//printMatrix(retval);
		return retval;
	}
	 
	private int[][] makeIdentity(int size){
		int retval[][] = new int[size][size];
		for(int i = 0; i<size; i++){
			for(int ii = 0; ii<size; ii++){
				if(i == ii) retval[i][ii] = 1;
				else retval[i][ii] = 0;
			}
		}
		return retval;
	}

	private int[][] make_hesque(int size){
		int retval[][] = new int[size][size];
		for(int i = 0; i<size; i++){
			for(int ii = 0; ii<size; ii++){
				if((i >= (size/2)) && (ii >= (size/2))) retval[i][ii] = -1;
				else retval[i][ii] = 1;
			}
		}
		return retval;
	}
	 
	private float[] arrayProduct(float external, float[][] base, float[] multiplier)
	{
		
		float retval[] = new float[multiplier.length];
		for(int i = 0; i<multiplier.length; i++){
			retval[i] = 0;
			for(int ii = 0; ii<multiplier.length; ii++) retval[i] += multiplier[ii] * base[i][ii];
			retval[i] *= external;
		}
		return retval;
	}
	
	private float[][] NxNarrayMultiply(float external, float[][] base, float[][] multiplier)
	{
		int arraylen = multiplier[0].length;
		float retval[][] = new float[arraylen][arraylen];
		
		if(base[0].length != multiplier.length) System.out.println("errr");	
		for(int i = 0; i<arraylen; i++){
			for(int ii = 0; ii<arraylen; ii++){
				for(int iii = 0; iii<arraylen; iii++) retval[i][ii] += base[i][iii] * multiplier[iii][ii];
				retval[i][ii] *= external;
			}
		}
		return retval;
	}
	
	
	private float[][] kronecker(float[][] base, float[][] multiplier)
	{
		int arraylen = (int)multiplier[0].length*base[0].length;
		float retval[][] = new float[arraylen][arraylen];

		for(int b_one= 0; b_one<base[0].length; b_one++){
			for(int b_two= 0; b_two<base[0].length; b_two++){
				for(int m_one= 0; m_one<multiplier[0].length; m_one++){
					for(int m_two= 0; m_two<multiplier[0].length; m_two++){
						retval[m_one+multiplier[0].length*b_one][m_two+multiplier[0].length*b_two] = base[b_one][b_two] * multiplier[m_one][m_two];
					}
				}
			}
		}
		return retval;
	}
	

} // end of class


