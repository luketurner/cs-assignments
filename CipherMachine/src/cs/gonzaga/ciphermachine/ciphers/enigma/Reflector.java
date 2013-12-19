package cs.gonzaga.ciphermachine.ciphers.enigma;

public class Reflector {

	private String wires;
	private int[] map;
	
	public Reflector(String wires) {
		super();
		this.wires = wires;
		
		this.map = new int[26];
		
		for (int iFrom = 0; iFrom < 26; iFrom++) {
			int iTo = Enigma.getIFromCh(wires.charAt(iFrom));
            map[iFrom] = (26 + iTo - iFrom) % 26;
		}
	}

	public String getWires() {
		return wires;
	}
	
	public int[] getMap() {
		return map;
	}
	
}
