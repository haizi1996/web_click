package haizi.pojo;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Writable;

public class VisitBean implements Writable{
	
	private String session;
	private String remote_addr;
	private String inTime;
	private String outTime;
	private String inPage;
	private String outPage;
	private String referal;
	private int pageVisits;

	public String getSession() {
		return session;
	}

	public String getRemote_addr() {
		return remote_addr;
	}

	public String getInTime() {
		return inTime;
	}

	public String getOutTime() {
		return outTime;
	}

	public void setSession(String session) {
		this.session = session;
	}

	public void setRemote_addr(String remote_addr) {
		this.remote_addr = remote_addr;
	}

	public void setInTime(String inTime) {
		this.inTime = inTime;
	}

	public void setOutTime(String outTime) {
		this.outTime = outTime;
	}

	public void setInPage(String inPage) {
		this.inPage = inPage;
	}

	public void setOutPage(String outPage) {
		this.outPage = outPage;
	}

	public void setReferal(String referal) {
		this.referal = referal;
	}

	public void setPageVisits(int pageVisits) {
		this.pageVisits = pageVisits;
	}

	public String getInPage() {
		return inPage;
	}

	public String getOutPage() {
		return outPage;
	}

	public String getReferal() {
		return referal;
	}

	public int getPageVisits() {
		return pageVisits;
	}

	public void write(DataOutput out) throws IOException {
		// TODO Auto-generated method stub
		out.writeUTF(session);
		out.writeUTF(remote_addr);
		out.writeUTF(inTime);
		out.writeUTF(outTime);
		out.writeUTF(inPage);
		out.writeUTF(outPage);
		out.writeUTF(referal);
		out.writeInt(pageVisits);
		
	}

	public void readFields(DataInput in) throws IOException {
		// TODO Auto-generated method stub
		this.session = in.readUTF();
		this.remote_addr = in.readUTF();
		this.inTime = in.readUTF();
		this.outTime = in.readUTF();
		this.inPage = in.readUTF();
		this.outPage = in.readUTF();
		this.referal = in.readUTF();
		this.pageVisits = in.readInt();
	}
	@Override
	public String toString() {
		return session + "\001" + remote_addr + "\001" + inTime + "\001" + outTime + "\001" + inPage + "\001" + outPage + "\001" + referal + "\001" + pageVisits;
	}

	public void set(String[] arr) {
		// TODO Auto-generated method stub
		this.session = arr[0];
		this.remote_addr = arr[1];
		this.inTime = arr[0];
		this.outTime = arr[0];
		this.inPage = arr[0];
		this.outPage = arr[0];
		this.referal = arr[0];
	}

}
