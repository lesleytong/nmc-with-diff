package edu.ustb.sei.mde.compare.nmc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.ustb.sei.mde.compare.Diff;

public class NConflict {
	StringBuilder information;
	Map<Integer, List<Diff>> map;
	
	public NConflict() {
		information = new StringBuilder();
		map = new HashMap<Integer, List<Diff>>();
	}
}
