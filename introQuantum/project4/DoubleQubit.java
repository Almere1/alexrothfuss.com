import java.lang.Math;
//package found at https://farenda.com/java/java-format-double-2-decimal-places/:
import java.text.DecimalFormat;

public class DoubleQubit extends ParentQubit{
	/* Default Constructor
	 * Constructor without input arguments
	 * Initialize value to white or |0> with positive phase
	 */
	public DoubleQubit(){
		//('this.' necessary?)
		this.value = new float[4];
		this.value[0] = 1;
		this.value[1] = 0;
		this.value[2] = 0;
		this.value[3] = 0;
	}

	public DoubleQubit(float[] v)
	{
		this.value = new float[4];
		this.setValues(v);
	}

	ParentQubit mergeQubits(ParentQubit pq){
		// this merges two sets of qubits and returns a new one that has
		// a number of qubits that is the sum of the two.
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
		for(int i = 0; i<this.value.length; i++){
			goval = Math.abs(value[i]);
			if((goval > .01) && (goval < .99)){
				//check for phase value
				if(this.value[i] < 0) retval += "- ";
				else if((i>0) && retval.length() > 1) retval += "+ ";
				retval+= df.format(goval);
				retval+="|";
				retval += explicitBin(2,i);
				retval+="> ";
			}
			else if(goval > .99){
				if(value[i] < 0) retval+="-";
				retval+="|";
				retval += explicitBin(2,i);
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

	public void applyNotGate()
	{
		// apply a not gate to all qubits
		float notGate[][] =  {	{0, 0, 0, 1},
					{0, 0, 1, 0},
					{0, 1, 0, 0},
					{1, 0, 0, 0}};
		this.value = arrayProduct(1, notGate, this.value);
	}

	public void applyCNotGate()
	{
		// apply a not gate to all qubits
		float notGate[][] =  {	{1, 0, 0, 0},
					{0, 1, 0, 0},
					{0, 0, 0, 1},
					{0, 0, 1, 0}};
		this.value = arrayProduct(1, notGate, this.value);
	}

	public void applyNotGate(int qb)
	{
		float notGate[][];
		if(qb == 0){
			//workaround for dumb java rules. can be changed if performance becomes priority
			float notGate2[][] = {	{0, 0, 1, 0},
						{0, 0, 0, 1},
						{1, 0, 0, 0},
						{0, 1, 0, 0}};
			notGate = notGate2.clone();
		}
		else if(qb == 1){
			float notGate2[][] = {	{0, 1, 0, 0},
						{1, 0, 0, 0},
						{0, 0, 0, 1},
						{0, 0, 1, 0}};
			notGate = notGate2.clone();
		}
		else{
			System.out.println("error: only two qubits!");
			return;
		}
		this.value = arrayProduct(1, notGate, this.value);
	}
	
	public void applyHGate()
	{
		// apply a not gate to all qubits
		//gate layout found on "Hadamard transform" page on wikipedia
		float hGate[][] = {	{1, 1, 1, 1},
					{1, -1, 1, -1},
					{1, 1, -1, -1},
					{1, -1, -1, 1}};
		this.value = arrayProduct(.5F, hGate, this.value);
	}
	
	public void applyHGate(int qb){
		//CHECK! done when VERY sleep deprived
		float hGate[][];
		if(qb == 0){
			float hGate2[][] = 	{{1, 0, 1, 0},
						{0, 1, 0, 1},
						{1, 0, -1, 0},
						{0, 1, 0, -1}};
			hGate = hGate2.clone();
		}
		else if(qb == 1){
			float hGate2[][] = {{1, 1, 0, 0},
					{1, -1, 0, 0},
					{0, 0, 1, 1},
					{0, 0, 1, -1}};
			hGate = hGate2.clone();
		}
		else{
			System.out.println("error: only two qubits!");
			return;
		}
		this.value = arrayProduct(1/fsqrt(2), hGate, this.value);
	}
	
	public void applySwapGate(int qubit1, int qubit2)
	{
		if(((qubit1 == 1) || (qubit1 == 0)) && (qubit1 + qubit2 == 1)){
			// apply a not gate to all qubits
			float swap[][]={{1, 0, 0, 0},
					{0, 0, 1, 0},
					{0, 1, 0, 0},
					{0, 0, 0, 1}};
			this.value = arrayProduct(1, swap, this.value);
		}
		else System.out.println("error: only two qubits!");
	}
	// apply swap gate to the two qubits. Only do so if the two values are 0 and 1 

	/* These are standard "setters" and "getters" except that we
	 * are supporting two types for the setter. Fill these in.
	 */
	 
	 
	private float[] arrayProduct(float external, float[][] sixteen, float[] multiplier)
	{
		
		float retval[] = new float[4];
		for(int i = 0; i<4; i++){
			retval[i] = 0;
			for(int ii = 0; ii<4; ii++) retval[i] += multiplier[ii] * sixteen[i][ii];
			retval[i] *= external;
		}
		return retval;
	}
	

} // end of class


