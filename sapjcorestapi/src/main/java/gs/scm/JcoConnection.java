package gs.scm;


import java.util.Properties;

import com.sap.conn.jco.ext.DestinationDataProvider;

public class JcoConnection {
	
    public static void main(String args[]) {
        createDestination();
    }
    
    // 접속정보 세팅
	public static void createDestination() {
		Properties connectProperties = new Properties();
		connectProperties.setProperty(DestinationDataProvider.JCO_POOL_CAPACITY, "100");
        connectProperties.setProperty(DestinationDataProvider.JCO_LANG, "KO");
		connectProperties.setProperty(DestinationDataProvider.JCO_MSSERV,   "3670");        
		connectProperties.setProperty(DestinationDataProvider.JCO_ASHOST, "203.245.89.96");
		connectProperties.setProperty(DestinationDataProvider.JCO_SYSNR,  "70");
		connectProperties.setProperty(DestinationDataProvider.JCO_CLIENT, "100");
		connectProperties.setProperty(DestinationDataProvider.JCO_USER,   "ITABAP17");
		connectProperties.setProperty(DestinationDataProvider.JCO_PASSWD, "1qazxsw2");

		gs.scm.CustomDestinationDataProvider.MyDestinationDataProvider myProvider = new CustomDestinationDataProvider.MyDestinationDataProvider();
		
		com.sap.conn.jco.ext.Environment.registerDestinationDataProvider(myProvider);
		myProvider.changePropertiesForABAP_AS_WITH_POOL(connectProperties);
	}
	
	/*
    static 
	{
		CustomDestinationDataProvider provider = CustomDestinationDataProvider.getInstance(); 
		Environment.registerDestinationDataProvider(provider);
        
        Properties connectProperties = new Properties(); 
        connectProperties.setProperty(DestinationDataProvider.JCO_DEST, ""); 
        connectProperties.setProperty(DestinationDataProvider.JCO_ASHOST, "");     
        connectProperties.setProperty(DestinationDataProvider.JCO_SYSNR, "");     
        connectProperties.setProperty(DestinationDataProvider.JCO_CLIENT, "");     
        connectProperties.setProperty(DestinationDataProvider.JCO_USER, "");     
        connectProperties.setProperty(DestinationDataProvider.JCO_PASSWD, "");   
        connectProperties.setProperty(DestinationDataProvider.JCO_LANG, "");
        connectProperties.setProperty(DestinationDataProvider.JCO_POOL_CAPACITY, "");
        connectProperties.setProperty(DestinationDataProvider.JCO_PEAK_LIMIT, "");
             
        createDestinationDataFile(Constants.DESTINATION_NAME, connectProperties);
        
	}
	
    static void createDestinationDataFile(String destinationName, Properties connectProperties)
    {
        File destCfg = new File(destinationName+".jcoDestination");
        try
        {
            FileOutputStream fos = new FileOutputStream(destCfg, false);
            connectProperties.store(fos, "for tests only !");
            fos.close();
        } catch (Exception e)
        {
            throw new RuntimeException("Unable to create the destination files", e);
        }

    }
    */
}
