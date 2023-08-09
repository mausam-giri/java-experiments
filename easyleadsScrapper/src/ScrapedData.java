import org.json.JSONArray;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ScrapedData {
    private static String filename = "CompanyData.json";
    private static final Lock fileLock = new ReentrantLock();
    private final JSONArray dataArray;
    public ScrapedData(JSONArray dataArray){
        this.dataArray = dataArray;
    }
    public ScrapedData(JSONArray dataArray, String filename){
        ScrapedData.filename = filename;
        this.dataArray= dataArray;
    }


    public void writeDataToJson(){
        try{
            fileLock.lock();
            File files = new File("files");
            if(!files.exists()){
                files.mkdir();
            }
            File file = new File(files, filename);

            try(FileWriter fileWriter = new FileWriter(file, true)){
                fileWriter.write(dataArray.toString(4) + System.lineSeparator());
                fileWriter.flush();
            }
        }catch(IOException es){
            System.out.println("Error: " + es);
            es.printStackTrace();
        }finally {
            fileLock.unlock();
        }
    }
}
