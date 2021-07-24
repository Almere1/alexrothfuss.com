public class TestQubit
{

	/* TestConstructor
	 * tests constructing
	 *
	 * inputs:
	 *  Qubit expected - the expected end state of the sprite
	 *  string[] args - it contains {val}
	 * outputs:
 	 *  returns 1 for success, 0 for failure
	 *  also prints results to the console
	 */
	public static int TestConstructor(ParentQubit expected, String[] args)
	{
		float val;

		// read the command-line argument
		val = Float.parseFloat(args[0]);
		// perform the operation
		SingleQubit testQubit = new SingleQubit(val);

		// check the result and report
		if (ParentQubit.compare(testQubit, expected) == 0)
		{
			System.out.println("Qubit setValue("+val+" "+
						"): Success!");
			return 1;
		}
		else
		{
			System.out.println("Qubit setValue("+val+
				"): FAIL!");
			System.out.println("Expected: "+expected);
			System.out.println("Actual: "+testQubit);
			return 0;
		}
	}
	
	/* TestMerge
	 * tests merging
	 *
	 * inputs:
	 *  Qubit expected - the expected end state of the sprite
	 *  string[] args - it contains values of doublequbit{}
	 * outputs:
 	 *  returns 1 for success, 0 for failure
	 *  also prints results to the console
	 */
	public static int TestMerge(ParentQubit first, ParentQubit second, String[] args)
	{
		float val;
		// make sure the degrees input is there
		if (args.length < 7)	
		{
			System.out.println("Too few arguments for "+
				"TestsetValueFloat: " +args.length);
			System.out.println("Missing value input");
			System.out.println("Test FAILED");
			return 0;
		}

		// read the command-line argument
		float setter[] = {Float.parseFloat(args[3]),Float.parseFloat(args[4]),Float.parseFloat(args[5]),Float.parseFloat(args[6])};
		DoubleQubit expected = new DoubleQubit(setter);
		// perform the operation
		DoubleQubit set = new DoubleQubit();
		set = (DoubleQubit)first.mergeQubits(second);

		// check the result and report
		if (ParentQubit.compare(set, expected) == 0)
		{
			System.out.println("Success!");
			return 1;
		}
		else
		{
			System.out.println("FAIL!");
			System.out.println("Expected: "+expected);
			System.out.println("Actual: "+set);
			return 0;
		}
	}
	
	
	/* braket
	 * displays the result of toBraKet, and asks whether the value is expected with the listed array
	 */
	public static void TestSinglebraKet(ParentQubit start)
	{
		System.out.println("The value given by toBraKet is '"+start.toBraKet()+"'. Is this expected for: ["+start+"]?");

	}
	
	
	/* TestNot
	 * Checks whether not of start qubit is the same as expected qubit
	 */
	public static int TestSingleNot(SingleQubit start, SingleQubit expected)
	{
		//perform the function
		start.applyNotGate();
		// check the result and report
		if (ParentQubit.compare(start, expected) == 0)
		{
			System.out.println("Success!");
			return 1;
		}
		else
		{
			System.out.println("FAIL!");
			System.out.println("Expected: "+expected);
			System.out.println("Actual: "+start);
			return 0;
		}
	}


	/* hGate
	 * tests whether hgate works
	 */
	public static int TestSingleHGate(SingleQubit start, SingleQubit expected)
	{

		//perform the function
		start.applyHGate();
			
		// check the result and report
		if (ParentQubit.compare(start, expected) == 0)
		{
			System.out.println("Success!");
			return 1;
		}
		else
		{
			System.out.println("FAIL!");
			System.out.println("Expected: "+expected);
			System.out.println("Actual: "+start);
			return 0;
		}
	}
	
	
	/* Swap
	 * Just makes sure swap doesn't result in a crash
	 */
	public static int TestSwap(SingleQubit start)
	{
		start.applySwapGate(0,1);
		return 0;
	}
	
	
		/* braket
	 * displays the result of toBraKet, and asks whether the value is expected with the listed array
	 */
	public static void TestDoubleBraKet(ParentQubit first, ParentQubit second)
	{
		DoubleQubit set = new DoubleQubit();
		set = (DoubleQubit)first.mergeQubits(second);
		System.out.println("The value given by toBraKet is '"+set.toBraKet()+"'. Is this expected for: ["+set+"]?");

	}
	
	
	public static int TestDoubleNot(ParentQubit first, ParentQubit second, String[] args)
	{
		float val;
		// make sure the degrees input is there
		if (args.length < 7)	
		{
			System.out.println("Too few arguments for "+
				"TestsetValueFloat: " +args.length);
			System.out.println("Missing value input");
			System.out.println("Test FAILED");
			return 0;
		}

		// read the command-line argument
		float setter[] = {Float.parseFloat(args[3]),Float.parseFloat(args[4]),Float.parseFloat(args[5]),Float.parseFloat(args[6])};
		DoubleQubit expected = new DoubleQubit(setter);
		// perform the operation
		DoubleQubit set = new DoubleQubit();
		set = (DoubleQubit)first.mergeQubits(second);
		set.applyNotGate();

		// check the result and report
		if (ParentQubit.compare(set, expected) == 0)
		{
			System.out.println("Success!");
			return 1;
		}
		else
		{
			System.out.println("FAIL!");
			System.out.println("Expected: "+expected);
			System.out.println("Actual: "+set);
			return 0;
		}
	}
	
	
	
	public static int TestDoubleIndividualNot(ParentQubit first, ParentQubit second, String[] args)
	{
		float val;
		// make sure the degrees input is there
		if (args.length < 8)	
		{
			System.out.println("Too few arguments for "+
				"TestsetValueFloat: " +args.length);
			System.out.println("Missing value input");
			System.out.println("Test FAILED");
			return 0;
		}

		// read the command-line argument
		float setter[] = {Float.parseFloat(args[3]),Float.parseFloat(args[4]),Float.parseFloat(args[5]),Float.parseFloat(args[6])};
		int qubit = Integer.parseInt(args[7]);
		DoubleQubit expected = new DoubleQubit(setter);
		// perform the operation
		DoubleQubit set = new DoubleQubit();
		set = (DoubleQubit)first.mergeQubits(second);
		set.applyNotGate(qubit);

		// check the result and report
		if (ParentQubit.compare(set, expected) == 0)
		{
			System.out.println("Success!");
			return 1;
		}
		else
		{
			System.out.println("FAIL!");
			System.out.println("Expected: "+expected);
			System.out.println("Actual: "+set);
			return 0;
		}
	}
	
		
	
	public static int TestDoubleHGate(ParentQubit first, ParentQubit second, String[] args)
	{
		float val;
		// make sure the degrees input is there
		if (args.length < 7)	
		{
			System.out.println("Too few arguments for "+
				"TestsetValueFloat: " +args.length);
			System.out.println("Missing value input");
			System.out.println("Test FAILED");
			return 0;
		}

		// read the command-line argument
		float setter[] = {Float.parseFloat(args[3]),Float.parseFloat(args[4]),Float.parseFloat(args[5]),Float.parseFloat(args[6])};
		DoubleQubit expected = new DoubleQubit(setter);
		// perform the operation
		DoubleQubit set = new DoubleQubit();
		set = (DoubleQubit)first.mergeQubits(second);
		set.applyHGate();

		// check the result and report
		if (ParentQubit.compare(set, expected) == 0)
		{
			System.out.println("Success!");
			return 1;
		}
		else
		{
			System.out.println("FAIL!");
			System.out.println("Expected: "+expected);
			System.out.println("Actual: "+set);
			return 0;
		}
	}
	
	
	
	public static int TestDoubleIndividualHGate(ParentQubit first, ParentQubit second, String[] args)
	{
		float val;
		// make sure the degrees input is there
		if (args.length < 8)	
		{
			System.out.println("Too few arguments for "+
				"TestsetValueFloat: " +args.length);
			System.out.println("Missing value input");
			System.out.println("Test FAILED");
			return 0;
		}

		// read the command-line argument
		float setter[] = {Float.parseFloat(args[3]),Float.parseFloat(args[4]),Float.parseFloat(args[5]),Float.parseFloat(args[6])};
		int qubit = Integer.parseInt(args[7]);
		DoubleQubit expected = new DoubleQubit(setter);
		// perform the operation
		DoubleQubit set = new DoubleQubit();
		set = (DoubleQubit)first.mergeQubits(second);
		set.applyHGate(qubit);

		// check the result and report
		if (ParentQubit.compare(set, expected) == 0)
		{
			System.out.println("Success!");
			return 1;
		}
		else
		{
			System.out.println("FAIL!");
			System.out.println("Expected: "+expected);
			System.out.println("Actual: "+set);
			return 0;
		}
	}
	
	public static int TestDoubleSwap(ParentQubit first, ParentQubit second, String[] args)
	{
		float val;
		// make sure the degrees input is there
		if (args.length < 7)	
		{
			System.out.println("Too few arguments for "+
				"TestsetValueFloat: " +args.length);
			System.out.println("Missing value input");
			System.out.println("Test FAILED");
			return 0;
		}

		// read the command-line argument
		float setter[] = {Float.parseFloat(args[3]),Float.parseFloat(args[4]),Float.parseFloat(args[5]),Float.parseFloat(args[6])};
		DoubleQubit expected = new DoubleQubit(setter);
		// perform the operation
		DoubleQubit set = new DoubleQubit();
		set = (DoubleQubit)first.mergeQubits(second);
		set.applySwapGate(0, 1);

		// check the result and report
		if (ParentQubit.compare(set, expected) == 0)
		{
			System.out.println("Success!");
			return 1;
		}
		else
		{
			System.out.println("FAIL!");
			System.out.println("Expected: "+expected);
			System.out.println("Actual: "+set);
			return 0;
		}
	}

	
	public static void TestNBraKet(SingleQubit firstQubit, SingleQubit secondQubit, String[] args){



		if (args.length < 4)	
		{
			System.out.println("Too few arguments for "+
				"TestsetValueFloat: " +args.length);
			System.out.println("Missing value input");
			System.out.println("Test FAILED");
			return;
		}

		NQubit end = new NQubit(3);
		DoubleQubit set = new DoubleQubit();
		SingleQubit add = new SingleQubit(Float.parseFloat(args[3]));
		set = (DoubleQubit)firstQubit.mergeQubits(secondQubit);
		end = (NQubit)set.mergeQubits(add);

		System.out.println("The value given by toBraKet is '"+end.toBraKet()+"'. Is this expected for: ["+end+"]?");

	}

        public static int TestNNot(SingleQubit firstQubit, SingleQubit secondQubit, String[] args){
		if (args.length < 7)	
		{
			System.out.println("Too few arguments for "+
				"TestsetValueFloat: " +args.length);
			System.out.println("Missing value input");
			System.out.println("Test FAILED");
			return 0;
		}
		
		SingleQubit last = new SingleQubit(Float.parseFloat(args[3]));
		SingleQubit test1 = new SingleQubit(Float.parseFloat(args[4]));
		SingleQubit test2 = new SingleQubit(Float.parseFloat(args[5]));
		SingleQubit test3 = new SingleQubit(Float.parseFloat(args[6]));


		NQubit set;
		NQubit expected;

		set = (NQubit)firstQubit.mergeQubits(secondQubit.mergeQubits(last));
		expected = (NQubit)test1.mergeQubits(test2.mergeQubits(test3));

		set.applyNotGate();	

		if (ParentQubit.compare(set, expected) == 0)
		{
			System.out.println("Success!");
			return 1;
		}
		else
		{
			System.out.println("FAIL!");
			System.out.println("Expected: "+expected);
			System.out.println("Actual: "+set);
			return 0;
		}
	}


        public static int TestNHGate(SingleQubit firstQubit, SingleQubit secondQubit, String[] args){
		if (args.length < 7)	
		{
			System.out.println("Too few arguments for "+
				"TestsetValueFloat: " +args.length);
			System.out.println("Missing value input");
			System.out.println("Test FAILED");
			return 0;
		}
		
		SingleQubit last = new SingleQubit(Float.parseFloat(args[3]));
		SingleQubit test1 = new SingleQubit(Float.parseFloat(args[4]));
		SingleQubit test2 = new SingleQubit(Float.parseFloat(args[5]));
		SingleQubit test3 = new SingleQubit(Float.parseFloat(args[6]));


		NQubit set;
		NQubit expected;

		set = (NQubit)firstQubit.mergeQubits(secondQubit.mergeQubits(last));
		expected = (NQubit)test1.mergeQubits(test2.mergeQubits(test3));

		set.applyHGate();	

		if (ParentQubit.compare(set, expected) == 0)
		{
			System.out.println("Success!");
			return 1;
		}
		else
		{
			System.out.println("FAIL!");
			System.out.println("Expected: "+expected);
			System.out.println("Actual: "+set);
			return 0;
		}
	}


        public static int TestSingleNHGate(SingleQubit firstQubit, SingleQubit secondQubit, String[] args){
		if (args.length < 8)	
		{
			System.out.println("Too few arguments for "+
				"TestsetValueFloat: " +args.length);
			System.out.println("Missing value input");
			System.out.println("Test FAILED");
			return 0;
		}
		
		SingleQubit last = new SingleQubit(Float.parseFloat(args[3]));
		SingleQubit test1 = new SingleQubit(Float.parseFloat(args[4]));
		SingleQubit test2 = new SingleQubit(Float.parseFloat(args[5]));
		SingleQubit test3 = new SingleQubit(Float.parseFloat(args[6]));


		NQubit set;
		NQubit expected;

		set = (NQubit)firstQubit.mergeQubits(secondQubit.mergeQubits(last));
		expected = (NQubit)test1.mergeQubits(test2.mergeQubits(test3));

		set.applyHGate(Integer.parseInt(args[7]));	

		if (ParentQubit.compare(set, expected) == 0)
		{
			System.out.println("Success!");
			return 1;
		}
		else
		{
			System.out.println("FAIL!");
			System.out.println("Expected: "+expected);
			System.out.println("Actual: "+set);
			return 0;
		}
	}


        public static int TestNSwap(SingleQubit firstQubit, SingleQubit secondQubit, String[] args){
		if (args.length < 9)	
		{
			System.out.println("Too few arguments for "+
				"TestsetValueFloat: " +args.length);
			System.out.println("Missing value input");
			System.out.println("Test FAILED");
			return 0;
		}
		
		SingleQubit last = new SingleQubit(Float.parseFloat(args[3]));
		SingleQubit test1 = new SingleQubit(Float.parseFloat(args[4]));
		SingleQubit test2 = new SingleQubit(Float.parseFloat(args[5]));
		SingleQubit test3 = new SingleQubit(Float.parseFloat(args[6]));


		NQubit set;
		NQubit expected;

		set = (NQubit)firstQubit.mergeQubits(secondQubit.mergeQubits(last));
		expected = (NQubit)test1.mergeQubits(test2.mergeQubits(test3));

		if(args.length >9){
			SingleQubit actuallast = new SingleQubit(Float.parseFloat(args[9]));
			SingleQubit test4 = new SingleQubit(Float.parseFloat(args[10]));
			set = (NQubit)set.mergeQubits(actuallast);
			expected = (NQubit)expected.mergeQubits(test4);
		}
		set.applySwapGate(Integer.parseInt(args[7]),Integer.parseInt(args[8]));	

		if (ParentQubit.compare(set, expected) == 0)
		{
			System.out.println("Success!");
			return 1;
		}
		else
		{
			System.out.println("FAIL!");
			System.out.println("Expected: "+expected);
			System.out.println("Actual: "+set);
			return 0;
		}
	}


        public static int TestSingleNNot(SingleQubit firstQubit, SingleQubit secondQubit, String[] args){
		if (args.length < 8)	
		{
			System.out.println("Too few arguments for "+
				"TestsetValueFloat: " +args.length);
			System.out.println("Missing value input");
			System.out.println("Test FAILED");
			return 0;
		}
		
		SingleQubit last = new SingleQubit(Float.parseFloat(args[3]));
		SingleQubit test1 = new SingleQubit(Float.parseFloat(args[4]));
		SingleQubit test2 = new SingleQubit(Float.parseFloat(args[5]));
		SingleQubit test3 = new SingleQubit(Float.parseFloat(args[6]));


		NQubit set;
		NQubit expected;

		set = (NQubit)firstQubit.mergeQubits(secondQubit.mergeQubits(last));
		expected = (NQubit)test1.mergeQubits(test2.mergeQubits(test3));

		set.applyNotGate(Integer.parseInt(args[7]));	

		if (ParentQubit.compare(set, expected) == 0)
		{
			System.out.println("Success!");
			return 1;
		}
		else
		{
			System.out.println("FAIL!");
			System.out.println("Expected: "+expected);
			System.out.println("Actual: "+set);
			return 0;
		}
	}

        public static int TestBernVaz(SingleQubit firstQubit, SingleQubit secondQubit, String[] args){
		if (args.length < 10)	
		{
			System.out.println("Too few arguments for "+
				"TestsetValueFloat: " +args.length);
			System.out.println("Missing value input");
			System.out.println("Test FAILED");
			return 0;
		}
		
		
		SingleQubit nextLast = new SingleQubit(Float.parseFloat(args[3]));
		SingleQubit last = new SingleQubit(Float.parseFloat(args[4]));
		SingleQubit test1 = new SingleQubit(Float.parseFloat(args[5]));
		SingleQubit test2 = new SingleQubit(Float.parseFloat(args[6]));
		SingleQubit test3 = new SingleQubit(Float.parseFloat(args[7]));
		SingleQubit test4 = new SingleQubit(Float.parseFloat(args[8]));

		NQubit set;
		NQubit expected;

		set = (NQubit)firstQubit.mergeQubits(secondQubit.mergeQubits(nextLast.mergeQubits(last)));
		expected = (NQubit)test1.mergeQubits(test2.mergeQubits(test3.mergeQubits(test4)));
		QOracle orc = new QOracle();
		orc.setBernVaz(Integer.parseInt(args[9]));

		QCircuit.bernvaz(set, orc);
	
		if (ParentQubit.compare(set, expected) == 0)
		{
			System.out.println("Success!");
			return 1;
		}
		else
		{
			System.out.println("FAIL!");
			System.out.println("Expected: "+expected);
			System.out.println("Actual: "+set);
			return 0;
		}
	}

        public static int TestArchimedes(SingleQubit firstQubit, SingleQubit secondQubit, String[] args){
		if (args.length < 12)	
		{
			System.out.println("Too few arguments for "+
				"TestsetValueFloat: " +args.length);
			System.out.println("Missing value input");
			System.out.println("Test FAILED");
			return 0;
		}
		
		
		SingleQubit nextLast = new SingleQubit(Float.parseFloat(args[3]));
		SingleQubit last = new SingleQubit(Float.parseFloat(args[4]));
		SingleQubit test1 = new SingleQubit(Float.parseFloat(args[5]));
		SingleQubit test2 = new SingleQubit(Float.parseFloat(args[6]));
		SingleQubit test3 = new SingleQubit(Float.parseFloat(args[7]));
		SingleQubit test4 = new SingleQubit(Float.parseFloat(args[8]));

		NQubit set;
		NQubit expected;

		set = (NQubit)firstQubit.mergeQubits(secondQubit.mergeQubits(nextLast.mergeQubits(last)));
		expected = (NQubit)test1.mergeQubits(test2.mergeQubits(test3.mergeQubits(test4)));
		QOracle orc = new QOracle();
		int passwords[] = {Integer.parseInt(args[9]),Integer.parseInt(args[10]),Integer.parseInt(args[11])};
		orc.setArchimedes(passwords);

		QCircuit.archimedes(set, orc);
	
		if (ParentQubit.compare(set, expected) == 0)
		{
			System.out.println("Success!");
			return 1;
		}
		else
		{
			System.out.println("FAIL!");
			System.out.println("Expected: "+expected);
			System.out.println("Actual: "+set);
			return 0;
		}
	}

        public static void TestEntangle(SingleQubit firstQubit, SingleQubit secondQubit){
		DoubleQubit set = (DoubleQubit)firstQubit.mergeQubits(secondQubit);
		QCircuit.sameEntangle(set);

		System.out.println("BraKet - "+set.toBraKet());
	}
	public static void main(String[] args)
	{
		int testNumber = 2;
		SingleQubit firstQubit = new SingleQubit();
		SingleQubit secondQubit = new SingleQubit();
		// make sure there are enough arguments
		if (args.length < 3)	
		{
			System.out.println("Too few arguments: "+args.length);
			System.out.println("Usage: TestQubit.exe start_state "
				+"expected_end_state testNumber inputs " 
				+"expected_ret_val");
			System.exit(0);
		}

		// get the starting state, ending state, and test number
		firstQubit.setValue(Float.parseFloat(args[0]));
		secondQubit.setValue(Float.parseFloat(args[1]));
		testNumber = Integer.parseInt(args[2]);

		// use the testnumber to choose a test functionpuppy
		switch (testNumber)
		{
			case (0):
				TestConstructor(secondQubit, args);
				break;
			case (1):
				TestMerge(firstQubit, secondQubit, args);
				break;
			case (2):
				TestSinglebraKet(firstQubit);
				break;
			case (3):
				TestSingleNot(firstQubit, secondQubit);
				break;
			case (4):
				TestSingleHGate(firstQubit, secondQubit);
				break;
			case (5):
				TestSwap(firstQubit);
				break;
			//DOUBLES
			
			
			    
			case (6):
				TestDoubleBraKet(firstQubit, secondQubit);
				break;
			case (7):
				TestDoubleNot(firstQubit, secondQubit, args);
				break;
			case (8):
				TestDoubleIndividualNot(firstQubit, secondQubit, args);
				break;
			case (9):
				TestDoubleHGate(firstQubit, secondQubit, args);
				break;				
			case (10):
				TestDoubleIndividualHGate(firstQubit, secondQubit, args);
				break;
			case (11):
				TestDoubleSwap(firstQubit, secondQubit, args);
				break;
			//NQubits



			case(12):
				TestNBraKet(firstQubit, secondQubit, args);
				break;

			case(13):
				TestNNot(firstQubit, secondQubit, args);
				break;
			case(14):
				TestSingleNNot(firstQubit, secondQubit, args);
				break;

			case(15):
				TestNHGate(firstQubit, secondQubit, args);
				break;
			case(16):
				TestSingleNHGate(firstQubit, secondQubit, args);
				break;
			case(17):
				TestNSwap(firstQubit, secondQubit, args);
				break;
			case(18):
				TestBernVaz(firstQubit, secondQubit, args);
				break;
			case(19):
				TestArchimedes(firstQubit, secondQubit, args);
				break;
			case(20):
				TestEntangle(firstQubit, secondQubit);
				break;
			default:
				System.out.println("Test "+testNumber+" not supported");
		}
	}
}




