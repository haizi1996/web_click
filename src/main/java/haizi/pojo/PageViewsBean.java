package haizi.pojo;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import org.apache.hadoop.io.WritableComparable;

public class PageViewsBean implements WritableComparable<PageViewsBean>{

	private String session;
	private String remote_addr;
	private String timestr;
	private String request;
	private int step;
	private String staylong;
	private String referal;
	private String useragent;
	private String bytes_send;
	private String status;
	
	public void parase(){
		
	}
	
	public static PageViewsBean getInstance(WebLogBean weblog){
		
		PageViewsBean bean = null;
		if(weblog.isValid()){
			bean = new PageViewsBean();
			bean.setRemote_addr(weblog.getRemote_addr());
			bean.setReferal(weblog.getHttp_referer());
			bean.setTimestr(weblog.getTime_local());
			bean.setRequest(weblog.getRequest());
			bean.setUseragent(weblog.getHttp_user_agent());
			bean.setBytes_send(weblog.getBody_bytes_sent());
			bean.setStatus(weblog.getStatus());
		}
		return bean;
	}
	
	
	
	public String getSession() {
		return session;
	}

	public void setSession(String session) {
		this.session = session;
	}

	public String getRemote_addr() {
		return remote_addr;
	}

	public void setRemote_addr(String remote_addr) {
		this.remote_addr = remote_addr;
	}

	public String getTimestr() {
		return timestr;
	}

	public void setTimestr(String timestr) {
		this.timestr = timestr;
	}

	public String getRequest() {
		return request;
	}

	public void setRequest(String request) {
		this.request = request;
	}

	public int getStep() {
		return step;
	}

	public void setStep(int step) {
		this.step = step;
	}

	public String getStaylong() {
		return staylong;
	}

	public void setStaylong(String staylong) {
		this.staylong = staylong;
	}

	public String getReferal() {
		return referal;
	}

	public void setReferal(String referal) {
		this.referal = referal;
	}

	public String getUseragent() {
		return useragent;
	}

	public void setUseragent(String useragent) {
		this.useragent = useragent;
	}

	public String getBytes_send() {
		return bytes_send;
	}

	public void setBytes_send(String bytes_send) {
		this.bytes_send = bytes_send;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}


	

	public int compareTo(PageViewsBean o) {
		// TODO Auto-generated method stub
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
		int cmp = this.remote_addr.compareTo(o.getRemote_addr());
		if(cmp == 0){
			if(this.step != 0 && o.getStep() != 0){
				return new Integer(this.step).compareTo(o.getStep()); 
			}
			try {
				cmp = df.parse(this.getTimestr()).compareTo(df.parse(o.getTimestr()));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return cmp;
	}

	public void write(DataOutput out) throws IOException {
		// TODO Auto-generated method stub
		out.writeUTF(session);
		out.writeUTF(remote_addr);
		out.writeUTF(timestr);
		out.writeUTF(request);
		out.writeInt(step);
		out.writeUTF(staylong);
		out.writeUTF(referal);
		out.writeUTF(useragent);
		out.writeUTF(bytes_send);
		out.writeUTF(status);
	}

	public void readFields(DataInput in) throws IOException {
		// TODO Auto-generated method stub
		this.session = in.readUTF();
		this.remote_addr = in.readUTF();
		this.timestr = in.readUTF();
		this.request = in.readUTF();
		this.step = in.readInt();
		this.staylong = in.readUTF();
		this.referal = in.readUTF();
		this.useragent = in.readUTF();
		this.bytes_send = in.readUTF();
		this.status = in.readUTF();
		
	}

	@Override
	public String toString() {
		return  session + " " + remote_addr + " " + timestr
				+ " " + request + " " + step + " " + staylong + " " + referal
				+ " " + useragent + " " + bytes_send + " " + status;
	}

	public void set(String[] arr) {
		// TODO Auto-generated method stub
		this.session = arr[0];
		this.remote_addr = arr[1];
		this.timestr = arr[2] + " " + arr[3];
		this.request = arr[4];
		this.step = Integer.parseInt(arr[5]);
		this.staylong = arr[6];
		this.referal = arr[7];
		this.useragent = arr[8];
		this.bytes_send = arr[9];
		this.status = arr[10];
		
	}



	

}
