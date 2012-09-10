import java.io.IOException;

import global.SystemDefs;
import heap.HFBufMgrException;
import heap.HFDiskMgrException;
import heap.HFException;
import heap.Heapfile;

public class Main {

	public static void main(String[] args) throws HFException,
			HFBufMgrException, HFDiskMgrException, IOException {
		SystemDefs sysdef = new SystemDefs("/tmp/" + "lol"
				+ System.getProperty("user.name") + ".minibase-db", 100, 100,
				"Clock");
		Heapfile hf = new Heapfile("lol");
	}
}

