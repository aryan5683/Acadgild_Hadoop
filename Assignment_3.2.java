import java.io.IOException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

public class Filter_Rec {

    public static class FilterData extends
            Mapper<LongWritable, Text, Text, IntWritable> {

        // setup , map, run, cleanup

        public void map(LongWritable key, Text value, Context context)
                throws IOException, InterruptedException {
            String line = value.toString();
            String[] elements = line.split(",");



String company=elements[1];
String product=elements[2];

if ( company.equals("NA") || product.equals("NA") )
{

    Text tx = new Text(elements[2]);
                int i = Integer.parseInt(elements[4]);
                IntWritable it = new IntWritable(i);
                context.write(tx, it);
}
        }
    }
/*
    public static class Reduce extends
            Reducer<Text, IntWritable, Text, IntWritable> {

        public void reduce(Text key, Iterable<IntWritable> values,
                Context context) throws IOException, InterruptedException {
            int sum = 0;
            for (IntWritable val : values) {
                sum += val.get();
            }
            context.write(key, new IntWritable(sum));
        }
    }
*/
    public static void main(String[] args) throws Exception {

        if (args.length != 2) {
            System.err.println("Insufficient args");
            System.exit(-1);
        }
        Configuration conf = new Configuration();

        Job job = new Job(conf, "Invalid Record");

        job.setJarByClass(Filter_Rec.class); // class conmtains mapper and
                                                // reducer class

        job.setMapOutputKeyClass(Text.class); // map output key class
        job.setMapOutputValueClass(IntWritable.class);// map output value class
        job.setOutputKeyClass(Text.class); // output key type in reducer
        job.setOutputValueClass(IntWritable.class);// output value type in
                                                    // reducer

        job.setMapperClass(FilterData.class);
       // job.setReducerClass(Reduce.class);
        job.setNumReduceTasks(1);
        job.setInputFormatClass(TextInputFormat.class); // default -- inputkey
                                                        // type -- longwritable
                                                        // : valuetype is text
        job.setOutputFormatClass(TextOutputFormat.class);



        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        job.waitForCompletion(true);

    }

}