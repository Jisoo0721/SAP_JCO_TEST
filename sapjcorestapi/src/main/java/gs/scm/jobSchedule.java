package gs.scm;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import gs.scm.ItemMaster.ItemMasterAPI;

@DisallowConcurrentExecution
public class jobSchedule implements Job {

    public void execute(JobExecutionContext context) throws JobExecutionException {
        
        try {
        
            System.out.println("배치 테스트 시작");
            ItemMasterAPI.itemBatchTest();
            System.out.println("배치 테스트 종료");
        } catch (Exception e) {
            //TODO: handle exception
            System.out.println("배치 에러");
        }
        

        
    }
    
}