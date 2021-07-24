import java.lang.Math;
//package found at https://farenda.com/java/java-format-double-2-decimal-places/:
import java.text.DecimalFormat;

abstract class ParentQubit{
	public float value[];		// [1] represents the square root of the probability of qubit 
					// being |1> when measured
	//private float zeroValue;	// represents the square root of the probability of qubit 
					// being |0> when measured (avoids float errors and 
					// simplifies operations)

	public ParentQubit(){
		value = new float[2];
		value[0] = 1;
		value[1] = 0; 
	}

	public ParentQubit(int numQubits){
		value = new float[ipow(2, numQubits)];
		value[0] = 1;
		for(int i = 1; i< value.length; i++) value[i] = 0; 
	}

	/* These are standard "setters" and "getters" except that we
	 * are supporting two types for the setter. Fill these in.
	 */
	public void setValue(float v, int i)
	{
		if(value.length <= i) System.out.println("error: requested array value past array length!");
		else{
			value[1] = fsqrt(Math.abs(v));
			if(v<0) value[i]*=-1;
			
			float sum = 0;
			for(int q = 0; q<value.length; q++) sum+=fpow(value[q], 2); 
			if((sum > 1.01) || (sum < .99)) System.out.println("warning: values do not sum to 1!");
		}
	}

	public void setValues(float[] v)
	{  
		if(value.length < v.length) System.out.println("error: input list too long!");
		else{
			for(int i = 0; i<v.length; i++){
				value[i] = fsqrt(Math.abs(v[i]));
				if(v[i]<0) value[i]*=-1;
			}

			float sum = 0;
			for(int q = 0; q<value.length; q++) sum+=fpow(value[q], 2); 
			if((sum > 1.01) || (sum < .99)) System.out.println("warning: values do not sum to 1!");
		}
	}

	
	public void setValuesDirect(float[] v)
	{  
		if(value.length < v.length) System.out.println("error: input list too long!");
		else{
			for(int i = 0; i<v.length; i++){
				value[i] = v[i];
			}
			float sum = 0;
			for(int q = 0; q<value.length; q++) sum+=fpow(value[q], 2); 
			if((sum > 1.01) || (sum < .99)) System.out.println("warning: values do not sum to 1!");
		}
	}

	public float getValue(int i)
	{  
		if(value.length <= i) System.out.println("error: requested array value past array length!");
		else{
			int phase = 1;
			if(value[i]<0) phase*=-1;
			return phase*value[i]*value[i];
		}
		return -1;
	}

	public float[] getValues()
	{  
		int phase = 1;
		float retval[] = this.value.clone();
		for(float val : retval){
			if(val<0) phase*=-1;
			val = phase*val*val;
		}
		return this.value;
	}

	public void setPhase(int phase, int i){
		if(value.length <= i) System.out.println("error: requested array value past array length!");
		else{
			if(phase == 1){
				this.value[i] = Math.abs(this.value[i]);
			}
			if(phase == -1){
				this.value[i] = -Math.abs(this.value[i]);
			}
			else if((phase != 1) && (phase != -1)) System.out.println("error: improper input. assign only \"1\" or \"-1\". Qubit not altered!");
		}
	}
	
	public void setPhases(int[] phase){
		if(value.length < phase.length) System.out.println("error: requested array value past array length!");
		else{
			for(int i = 0; i<phase.length; i++){
				if(phase[i] == 1){
					this.value[i] = Math.abs(this.value[i]);
				}
				if(phase[i] == -1){
					this.value[i] = -Math.abs(this.value[i]);
				}
				else if((phase[i] != 1) && (phase[i] != -1)) System.out.println("warning: improper input. assign only \"1\" or \"-1\". Qubit[" + i + "] not altered!");
			}
		}
	}

	public int getPhase(int i){
		if(value.length <= i) System.out.println("error: requested array value past array length!");
		else{
			if(this.value[i]<0) return -1;
			else return 1;
		}
		return -1;
	}

	public int getNumQubits(){
		int retval = Math.round((int)(Math.log(value.length)/Math.log(2)));
		if(Math.pow(2, retval) != value.length){
			System.out.println("error: either a floating point error or impossible number of qubits!");
			return -1;
		}
		else return retval;
	}

	abstract ParentQubit mergeQubits(ParentQubit pq);
	// this merges two sets of qubits and returns a new one that has
	// a number of qubits that is the sum of the two. For example, we could
	// merge two SingleQubit objects to become one DoubleQubit object.
	// this is not implemented in ParentQubit but in the subclasses
	abstract String toBraKet();
	// this prints out the state in bra-ket notation, like last week
	abstract void applyNotGate();
	// apply a not gate to each qubit
	abstract void applyNotGate(int qb);
	// apply a not gate to the qubit in position qb, where numbering starts at 0
	abstract void applyHGate();
	// apply an H gate to each qubit
	abstract void applyHGate(int qb);
	// apply an H gate to the qubit in position qb, where numbering starts at 0
	abstract void applySwapGate(int qubit1, int qubit2); 
	
	public float fpow(float f, int i){
		return (float) Math.pow(f, i);
	}
	public float fsqrt(float f){
		return (float) Math.sqrt(f);
	}
	public int ipow(int f, int i){
		return (int) Math.pow(f, i);
	}


	/* These are methods we implement so that we can use Qubit with
	 * standard operations - like System.out.println and comparison 
	 * These are critical for grading, so don't change them!!!
	 */
	public String toString()
	{
	       // we put the "" before value to make it a String.
	       	
		String retval = "";
		for(float val : value) retval += val + " ";
		return retval;
       }

	public static int compare (ParentQubit obj1, ParentQubit obj2)
	{
		// first cast to Qubits - we assume we're comparing Qubits
		int flag = 0;
		if(obj1.value.length != obj2.value.length) flag = -1;
		else for(int i = 0; i<obj1.value.length; i++) if(((obj1.value[i] - obj2.value[i]) > 0.01) || ((obj1.value[i] - obj2.value[i]) < -0.01)) flag = -1;
		return flag;
	}
}
