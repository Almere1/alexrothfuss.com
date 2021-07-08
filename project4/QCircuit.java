class QCircuit{
	public static void sameEntangle(DoubleQubit dq){
		//this method will entangle two Qubits, if the first Qubit  is white
		dq.applyHGate(0);
		dq.applyCNotGate();
	}
	public static void bernvaz(NQubit nq, QOracle qo){
		nq.applyHGate();
		qo.probeBernVaz(nq);
		nq.applyHGate();
	}
	public static void archimedes(NQubit nq, QOracle qo){
		nq.applyHGate();
		qo.probeArchimedes(nq);
		nq.applyHGate();
		nq.applyHGate(3);
	}
}
