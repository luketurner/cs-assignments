package cs.gonzaga.ciphermachine.ciphers.enigma;

public class Rotor {

	private String wires;
	private char notch;
	private int[] map;
	private int[] mapRev;
	
	public Rotor(String wires, char notch) {
		super();
		this.wires = wires;
		this.notch = notch;
		
		this.map = new int[26];
		this.mapRev = new int[26];
		
		for (int iFrom = 0; iFrom < 26; iFrom++) {
			int iTo = Enigma.getIFromCh(wires.charAt(iFrom));
            map[iFrom] = (26 + iTo - iFrom) % 26;
            mapRev[iTo] = (26 + iFrom - iTo) % 26;
		}
	}

	public String getWires() {
		return wires;
	}

	public char getNotch() {
		return notch;
	}

	public int[] getMap() {
		return map;
	}

	public int[] getMapRev() {
		return mapRev;
	}
	
}
