import java.lang.Math;
//package found at https://farenda.com/java/java-format-double-2-decimal-places/:
import java.text.DecimalFormat;

public class SingleQubit extends ParentQubit{
	/* Default Constructor
	 * Constructor without input arguments
	 * Initialize value to white or |0> with positive phase
	 */
	public SingleQubit(){
		//('this.' necessary?)
		this.value = new float[2];
		this.value[0] = 1;
		this.value[1] = 0;
	}

	/* Constructor with input arguments
	 * Initialize value to inputted value
	 */
	public SingleQubit(float v)
	{
		this.value = new float[2];
		//is there no square root function for floats in java?
		//Set square root of probability of |1>, add phase if necessary
		this.value[1] = fsqrt(Math.abs(v));
		if(v<0) this.value[1]*=-1;
		//Set value for |0>
		this.value[0] = fsqrt(1-Math.abs(v));
	}

	/* Constructor with input arguments
	 * allow other ways of specifying the starting value
	 * initialize: "White" is false, "Black" is true
	 */
	public SingleQubit(String v)
	{
		this.value = new float[2];
		v = v.toLowerCase();
		if((v.equals("white"))){
			this.value[1] = 0;
		}
		else if((v.equals("black"))) this.value[1] = 1;
		else{
			System.out.println("error: improper input. assign only \"white\" or \"black\". Qubit not initialized!");
			System.exit(1);
		}
		//set value for |0>
		this.value[0] = 1-this.value[1];
	}
	
	//for the convenience of testing I'm using the old meaning of setValue for SingleQubit
	public void setValue(float v)
	{  
		//Set square root of probability of |1>, add phase if necessary
		this.value[1] = fsqrt(Math.abs(v));
		if(v<0) this.value[1]*=-1;
		//Set value for |0>
		this.value[0] = fsqrt(1-Math.abs(v));
	}

	ParentQubit mergeQubits(ParentQubit pq) {
		int length = pq.value.length * this.value.length;
		if(length > 4){
        		int len = pq.value.length * this.value.length;
			int size = (int)(Math.log(len)/Math.log(2));
       		 	NQubit retval = new NQubit(size);
                	for(int val2 = 0; val2<this.value.length; val2++){
				for(int val1 = 0; val1<pq.value.length; val1++){
					retval.value[val2*pq.value.length + val1] = this.value[val2] * pq.value[val1];
				}
			}
                	return retval;  
		} else {
			DoubleQubit retval = new DoubleQubit();
                	for(int val2 = 0; val2<this.value.length; val2++){
				for(int val1 = 0; val1<pq.value.length; val1++){
					retval.value[val2*this.value.length + val1] = this.value[val2] * pq.value[val1];
				}
			}
                	return retval;  
		}
	}
	
	public String toBraKet(){
		String retval = "";
		//check for phase value
		String phase = "+";
		if(this.value[0] * this.value[1] < 0) phase = "-";
		
		//check for easy solutions
		if(this.value[0] > .99) return "|0>";
		else if(this.value[1] > .99) return "|1>";
		else if(this.value[0] < -.99) return "-|0>";
		else if(this.value[1] < -.99) return "-|1>";
		
		//use DecimalFormat (credited at the top)
	        DecimalFormat df = new DecimalFormat("#.##");
	        String num1 = df.format(Math.abs(value[0]));
	        String num2 = df.format(Math.abs(value[1]));	        
	        
	        retval = num1+"|0> "+phase+" "+num2+"|1>";
	        
		return retval;
	}

	public void applyNotGate()
	{
		float hold = this.value[1];
		this.value[1] = this.value[0];
		this.value[0] = hold;
			
	}
	
	public void applyNotGate(int qb)
	{
		if(qb != 0) System.out.println("error: only 1 qubit, value 0!");
		else{
			float hold = this.value[1];
			this.value[1] = this.value[0];
			this.value[0] = hold;
		}
	}
	
	public void applyHGate()
	{
		float v0 = this.value[0];
		float v1 = this.value[1];
		this.value[0] = (v0+v1)/fsqrt(2);
		this.value[1] = (v0-v1)/fsqrt(2);
	}
	
	public void applyHGate(int qb)
	{
		if(qb != 0) System.out.println("error: only 1 qubit, value 0!");
		else{
			float v0 = this.value[0];
			float v1 = this.value[1];
			this.value[0] = (v0+v1)/fsqrt(2);
			this.value[1] = (v0-v1)/fsqrt(2);
		}
	}
	
	public void applySwapGate(int qubit1, int qubit2){
		System.out.println("warning: only 1 qubit, no swap!");	
	}
	
	public int measureValue(){
		if(this.value[0] == 0) return 1;
		
		float check = (float) Math.random();
		this.value[0] = fpow(this.value[0], 2);
		if(check<this.value[0]){
			this.value[0] = 1;
			this.value[1] = 0;
			return 0;
		}
		else{
			this.value[0] = 0;
			this.value[1] = 1;
			return 1;
		}
	}


} // end of class


