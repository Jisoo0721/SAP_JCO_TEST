package gs.scm;

// import org.quartz.JobBuilder;
// import org.quartz.JobDetail;
// import org.quartz.Scheduler;
// import org.quartz.SchedulerFactory;
// import org.quartz.impl.StdSchedulerFactory;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

public class App {
    
    public static void main(String[] args) {
        //SchedulerFactory scheduleFactory = new StdSchedulerFactory();

        AbstractApplicationContext cxt = new GenericXmlApplicationContext("JobScheduler.xml");

        cxt.close();

        // try {
        //     Scheduler scheduler = scheduleFactory.getScheduler();
            
        //     JobDetail job = JobBuilder.newJob(jobSchedule.class).withIdentity("", Scheduler.DEFAULT_GROUP).build();
            
            

        // } catch (Exception e) {
        //     //TODO: handle exception
        // }
    }
}