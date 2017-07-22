package haizi.mr.pre;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

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

import haizi.pojo.PageViewsBean;
import haizi.pojo.WebLogBean;

public class ClickStreamThree {

	static class ClickStreamThreeMapper extends Mapper<LongWritable, Text, Text, WebLogBean> {

		Text k = new Text();
		WebLogBean v = new WebLogBean();

		@Override
		protected void map(LongWritable key, Text value, Mapper<LongWritable, Text, Text, WebLogBean>.Context context)
				throws IOException, InterruptedException {

			// TODO Auto-generated method stub
			String line = value.toString();
			String[] arr = line.split(" ");
			if (arr.length >= 10 || "true".equals(arr[0])) {
				v.set(arr);
				k.set(v.getRemote_addr());
				context.write(k, v);
			}
		}
	}

	static class ClickStreamThreeReduce extends Reducer<Text, WebLogBean, NullWritable, PageViewsBean> {

		NullWritable k = NullWritable.get();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);

		@Override
		protected void reduce(Text key, Iterable<WebLogBean> value,
				Reducer<Text, WebLogBean, NullWritable, PageViewsBean>.Context context)
				throws IOException, InterruptedException {
			// TODO Auto-generated method stub
			Iterator<WebLogBean> weblogs = value.iterator();
			List<PageViewsBean> lists = new ArrayList<PageViewsBean>();
			WebLogBean weblog = null;
			while (weblogs.hasNext()) {
				weblog = weblogs.next();
				PageViewsBean pageView = PageViewsBean.getInstance(weblog);
				if (pageView != null) {
					lists.add(pageView);
				}
			}
			String session = UUID.randomUUID().toString();
			int step = 1;
			int count = lists.size();
			PageViewsBean pageView = new PageViewsBean() ;
			for (int i = 1; i < count; i++) {
				try {
					PageViewsBean bean = lists.get(i - 1);
					long startTime = df.parse(bean.getTimestr()).getTime();
					long end = df.parse(lists.get(i).getTimestr()).getTime();
					bean.setStep(step);
					bean.setSession(session);
					bean.setStaylong(String.valueOf(end - startTime));
					if (end - startTime < 30 * 60 * 1000) {
						step++;
					} else {
						step = 1;
						session = UUID.randomUUID().toString();
					}
					context.write(k, bean);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			session = UUID.randomUUID().toString();
			pageView = lists.get(count-1);
			pageView.setStaylong("60");
			pageView.setSession(session);
			pageView.setStep(step);
			context.write(k, pageView);
			session = UUID.randomUUID().toString();
		}

	}

	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();
		Job job = Job.getInstance(conf);

		job.setJarByClass(ClickStreamThree.class);

		job.setMapperClass(ClickStreamThreeMapper.class);
		job.setReducerClass(ClickStreamThreeReduce.class);

		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(WebLogBean.class);

		job.setOutputKeyClass(NullWritable.class);
		job.setOutputValueClass(PageViewsBean.class);
		
		FileInputFormat.setInputPaths(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
//		FileInputFormat.setInputPaths(job, new Path("/home/haizi/output"));
//	    FileOutputFormat.setOutputPath(job, new Path("/home/haizi/output/output"));
		
		
		job.waitForCompletion(true);

	}

}
