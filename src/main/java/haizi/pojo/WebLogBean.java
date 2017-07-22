package haizi.pojo;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Set;

import org.apache.hadoop.io.Writable;

public class WebLogBean implements Writable {
	
	public static SimpleDateFormat df1 = new SimpleDateFormat("dd/MMM/yyyy:HH:mm:ss", Locale.US);
	public static SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
	private boolean valid = true;// 判断数据是否合法
	private String remote_addr;// 记录客户端的ip地址
	private String remote_user;// 记录客户端用户名称,忽略属性"-"
	private String time_local;// 记录访问时间与时区
	private String request;// 记录请求的url与http协议
	private String status;// 记录请求状态；成功是200
	private String body_bytes_sent;// 记录发送给客户端文件主体内容大小
	private String http_referer;// 用来记录从那个页面链接访问过来的
	private String http_user_agent;// 记录客户浏览器的相关信息
	
	
	
	public WebLogBean() {
	}
	
	
	public static WebLogBean build(String line,Set<String> pages){
		WebLogBean webLogBean = null;
		if(line == null){
			throw new RuntimeException("line == null");
		}
		String[] arr = line.split(" ");
		if(pages != null && pages.contains(arr[6])){
			return null;
		}
		if (arr.length > 11) {
			webLogBean = new WebLogBean();
			webLogBean.setRemote_addr(arr[0].trim());
			webLogBean.setRemote_user(arr[1]);
			String time_local="-invalid_time-";
			try {
				time_local = df2.format(df1.parse(arr[3].substring(1)));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			webLogBean.setTime_local(time_local);
			webLogBean.setRequest(arr[6]);
			webLogBean.setStatus(arr[8]);
			webLogBean.setBody_bytes_sent(arr[9]);
			webLogBean.setHttp_referer(arr[10]);
			//如果useragent元素较多，拼接useragent
			if (arr.length >12) {
				StringBuilder sb = new StringBuilder();
				for(int i=11;i<arr.length;i++){
					sb.append(arr[i]);
				}
				webLogBean.setHttp_user_agent(sb.toString());
			} else {
				webLogBean.setHttp_user_agent(arr[11]);
			}
			
			if (Integer.parseInt(webLogBean.getStatus()) >= 400) {// 大于400，HTTP错误
				webLogBean.setValid(false);
			}
			
			if("-invalid_time-".equals(webLogBean.getTime_local())){
				webLogBean.setValid(false);
			}
		}
		return webLogBean;
	}
	public boolean isValid() {
		return valid;
	}
	public void setValid(boolean valid) {
		this.valid = valid;
	}
	public String getRemote_addr() {
		return remote_addr;
	}
	public void setRemote_addr(String remote_addr) {
		this.remote_addr = remote_addr;
	}
	public String getRemote_user() {
		return remote_user;
	}
	public void setRemote_user(String remote_user) {
		this.remote_user = remote_user;
	}
	public String getTime_local() {
		return time_local;
	}
	public void setTime_local(String time_local) {
		this.time_local = time_local;
	}
	public String getRequest() {
		return request;
	}
	public void setRequest(String request) {
		this.request = request;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getBody_bytes_sent() {
		return body_bytes_sent;
	}
	public void setBody_bytes_sent(String body_bytes_sent) {
		this.body_bytes_sent = body_bytes_sent;
	}
	public String getHttp_referer() {
		return http_referer;
	}
	public void setHttp_referer(String http_referer) {
		this.http_referer = http_referer;
	}
	public String getHttp_user_agent() {
		return http_user_agent;
	}
	public void setHttp_user_agent(String http_user_agent) {
		this.http_user_agent = http_user_agent;
	}
//	@Override
//	public String toString() {
//		StringBuilder sb = new StringBuilder();
//		sb.append(this.valid);
//		sb.append("\001").append(this.remote_addr);
//		sb.append("\001").append(this.remote_user);
//		sb.append("\001").append(this.time_local);
//		sb.append("\001").append(this.request);
//		sb.append("\001").append(this.status);
//		sb.append("\001").append(this.body_bytes_sent);
//		sb.append("\001").append(this.http_referer);
//		sb.append("\001").append(this.http_user_agent);
//		return sb.toString();
//	}
	
	
	public void write(DataOutput out) throws IOException {
		// TODO Auto-generated method stub
		out.writeBoolean(this.valid);
		out.writeUTF(null==remote_addr?"":remote_addr);
		out.writeUTF(null==remote_user?"":remote_user);
		out.writeUTF(null==time_local?"":time_local);
		out.writeUTF(null==request?"":request);
		out.writeUTF(null==status?"":status);
		out.writeUTF(null==body_bytes_sent?"":body_bytes_sent);
		out.writeUTF(null==http_referer?"":http_referer);
		out.writeUTF(null==http_user_agent?"":http_user_agent);
		
	}
	@Override
	public String toString() {
		return  valid + " " + remote_addr + " " + remote_user
				+ " " + time_local + " " + request + " " + status + " "
				+ body_bytes_sent + " " + http_referer + " " + http_user_agent;
	}


	public void readFields(DataInput in) throws IOException {
		// TODO Auto-generated method stub
		this.valid = in.readBoolean();
		this.remote_addr = in.readUTF();
		this.remote_user = in.readUTF();
		this.time_local = in.readUTF();
		this.request = in.readUTF();
		this.status = in.readUTF();
		this.body_bytes_sent = in.readUTF();
		this.http_referer = in.readUTF();
		this.http_user_agent = in.readUTF();
		
	}


	public void set(String[] arr) {
		// TODO Auto-generated method stub
		this.valid = true;
		this.remote_addr = arr[1];
		this.remote_user = arr[2];
		this.time_local = arr[3]+ " " + arr[4];
		this.request = arr[5];
		this.status = arr[6];
		this.body_bytes_sent = arr[7];
		this.http_referer = arr[8];
		this.http_user_agent = arr[9];
		
	}


	
	
	
}
