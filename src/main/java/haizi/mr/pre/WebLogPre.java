package haizi.mr.pre;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import haizi.pojo.WebLogBean;

public class WebLogPre {

	static class WebLogPreMapper extends Mapper<LongWritable, Text, Text, WebLogBean> {

		private Set<String> pages = new HashSet<String>();
		Text k = new Text();

		@Override
		protected void setup(Mapper<LongWritable, Text, Text, WebLogBean>.Context context)
				throws IOException, InterruptedException {
			// TODO Auto-generated method stub
			// 添加过滤规则
			pages.add("/about");
		}

		@Override
		protected void map(LongWritable key, Text value, Mapper<LongWritable, Text, Text, WebLogBean>.Context context)
				throws IOException, InterruptedException {
			// TODO Auto-generated method stub
			String line = value.toString();
			WebLogBean webLogBean = WebLogBean.build(line, pages);
			if (webLogBean != null) {
				String s = webLogBean.getRemote_addr();
				k.set(s);
				context.write(k, webLogBean);
			}
		}

	}

	static class WebLogPreReduce extends Reducer<Text, WebLogBean, NullWritable, WebLogBean> {
		NullWritable k = NullWritable.get();

		@Override
		protected void reduce(Text key, Iterable<WebLogBean> value,
				Reducer<Text, WebLogBean, NullWritable, WebLogBean>.Context context)
				throws IOException, InterruptedException {
			// TODO Auto-generated method stub
			Iterator<WebLogBean> iteartor = value.iterator();
			while (iteartor.hasNext()) {
				
				WebLogBean bean = iteartor.next();
				context.write(k, bean);
			}
		}

	}

	public static void main(String[] args) throws Exception {
		Configuration config = new Configuration();
		Job job = Job.getInstance(config);
		job.setJarByClass(WebLogPre.class);

		job.setMapperClass(WebLogPreMapper.class);
		job.setReducerClass(WebLogPreReduce.class);

		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(WebLogBean.class);

		job.setOutputKeyClass(NullWritable.class);
		job.setOutputValueClass(WebLogBean.class);

		 FileInputFormat.setInputPaths(job, new Path(args[0]));
		 FileOutputFormat.setOutputPath(job, new Path(args[1]));
//		FileInputFormat.setInputPaths(job, new Path("/home/haizi/input/access.log"));
//		FileOutputFormat.setOutputPath(job, new Path("/home/haizi/output"));
		job.waitForCompletion(true);
	}

}
