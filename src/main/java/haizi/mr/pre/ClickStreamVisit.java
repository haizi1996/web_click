package haizi.mr.pre;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.beanutils.BeanUtils;
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
import haizi.pojo.VisitBean;

public class ClickStreamVisit {

	static class ClickStreamVisitMapper extends Mapper<LongWritable, Text, Text, PageViewsBean> {
		Text k = new Text();
		PageViewsBean v = new PageViewsBean();

		@Override
		protected void map(LongWritable key, Text value,
				Mapper<LongWritable, Text, Text, PageViewsBean>.Context context)
				throws IOException, InterruptedException {
			// TODO Auto-generated method stub

			// TODO Auto-generated method stub
			String line = value.toString();
			String[] arr = line.split(" ");
			if (arr.length >= 10) {
				v.set(arr);
				k.set(arr[0]);
				context.write(k, v);
			}

		}

	}

	static class ClickStreamVisitReduce extends Reducer<Text, PageViewsBean, NullWritable, VisitBean> {

		@Override
		protected void reduce(Text key, Iterable<PageViewsBean> value,
				Reducer<Text, PageViewsBean, NullWritable, VisitBean>.Context context)
				throws IOException, InterruptedException {
			// TODO Auto-generated method stub
			ArrayList<PageViewsBean> pvBeansList = new ArrayList<PageViewsBean>();
			for (PageViewsBean pvBean : value) {
				PageViewsBean bean = new PageViewsBean();
				try {
					BeanUtils.copyProperties(bean, pvBean);
					pvBeansList.add(bean);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			// 取这次visit的首尾pageview记录，将数据放入VisitBean中
			VisitBean visitBean = new VisitBean();
			// 取visit的首记录
			visitBean.setInPage(pvBeansList.get(0).getRequest());
			visitBean.setInTime(pvBeansList.get(0).getTimestr());
			// 取visit的尾记录
			visitBean.setOutPage(pvBeansList.get(pvBeansList.size() - 1).getRequest());
			visitBean.setOutTime(pvBeansList.get(pvBeansList.size() - 1).getTimestr());
			// visit访问的页面数
			visitBean.setPageVisits(pvBeansList.size());
			visitBean.setRemote_addr(pvBeansList.get(0).getRemote_addr());
			visitBean.setReferal(pvBeansList.get(pvBeansList.size() - 1).getReferal());
			
			visitBean.setSession(key.toString());
			context.write(NullWritable.get(), visitBean);
		}

	}

	public static void main(String[] args) throws Exception {

		Configuration conf = new Configuration();

		Job job = Job.getInstance(conf);

		job.setJarByClass(ClickStreamVisit.class);
		job.setMapperClass(ClickStreamVisitMapper.class);
		job.setReducerClass(ClickStreamVisitReduce.class);

		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(PageViewsBean.class);
		job.setOutputKeyClass(NullWritable.class);
		job.setOutputValueClass(VisitBean.class);

//		FileInputFormat.setInputPaths(job, new Path("/home/haizi/output/output"));
//		FileOutputFormat.setOutputPath(job, new Path("/home/haizi/output/output/output"));
		FileInputFormat.setInputPaths(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
		
		
		
		boolean result = job.waitForCompletion(true);
		System.exit(result ? 0 : 1);

	}
}
