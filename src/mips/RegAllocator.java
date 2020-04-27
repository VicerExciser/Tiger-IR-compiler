package mips;

public class RegAllocator {

	private boolean PRINTS_ENABLED = true;
	
	public enum Mode {
		NAIVE,
		INTRABLOCK;

		private static final int size = Mode.values().length;
	}

	public Mode mode;


	public RegAllocator(int allocationMode) {
		if (allocationMode >= Mode.size || allocationMode < 0) {
			if (PRINTS_ENABLED) {
	            System.out.println("[RegAllocator] INVALID MODE SETTING: "
	            		+ String.valueOf(allocationMode)
	            		+ "\nDEFAULTING ALLOCATION MODE TO '" 
	            		+ Mode.NAIVE + "'\n");
	        }
            this.mode = Mode.NAIVE;
        } else {
            this.mode = Mode.values()[allocationMode];
            if (PRINTS_ENABLED) {
	            System.out.println("[RegAllocator] Allocation Mode set to: '"
	            		+ this.mode.name() + "'\n");
	        }
        }


	}


}
