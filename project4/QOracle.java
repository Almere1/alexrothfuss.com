class QOracle{
	private float bernVaz[][] = new float[16][16];
	private float archimedes[][] = new float[16][16];

	public void setBernVaz(int code){
		float spots[] = {0,0,0,0,0,0,0,0};
		
		if(code-4>=0){
			for(int i = 4; i<8; i++) spots[i] = 1 - spots[i];
			code-=4;
		}
	       
		if(code-2>=0){
			//this is a risky way of assigning something that could be assigned in four lines. what a pain
			for(int i = 2; i<8; i+= 1+2*spots[i-1]){
				spots[i] = 1 - spots[i];
				if(i == 4) System.out.println("HELP");
			}
			code-=2;
		}

		if(code-1>=0){
			for(int i = 1; i<8; i+= 2){
				spots[i] = 1 - spots[i];
				if(i == 4) System.out.println("HELP");
			}
			code-=1;
		}
		
		for(int i = 0; i<8; i++){
			bernVaz[2*i][2*i] = 1-spots[i];
			bernVaz[2*i][2*i+1] = spots[i];
			bernVaz[2*i+1][2*i+1] = bernVaz[2*i][2*i];
			bernVaz[2*i+1][2*i] = bernVaz[2*i][2*i+1];
		}
	}

	public void setArchimedes(int[] code){
		float spots[] = {0,0,0,0,0,0,0,0};

		for(int i : code){
			spots[i] = 1;
		}
		
		for(int i = 0; i<8; i++){
			archimedes[2*i][2*i] = 1-spots[i];
			archimedes[2*i][2*i+1] = spots[i];
			archimedes[2*i+1][2*i+1] = archimedes[2*i][2*i];
			archimedes[2*i+1][2*i] = archimedes[2*i][2*i+1];
		}
	}

	public void probeBernVaz(NQubit nq){
		nq.setValuesDirect(arrayProduct(1, bernVaz, nq.getValues()));
	}
	public void probeArchimedes(NQubit nq){
		nq.setValuesDirect(arrayProduct(1, archimedes, nq.getValues()));
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




        private void printMatrix(float[][] given){
		for(int i = 0; i<given[0].length; i++){
		       	for(int ii = 0; ii<given[0].length; ii++) System.out.print((int)given[i][ii]+" ");
			System.out.print("\n");
		}
	
		System.out.print("\n");

	}
}
