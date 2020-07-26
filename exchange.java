import java.io.FileNotFoundException;
import java.net.URL;
import java.util.*;
import java.io.File;
import java.util.concurrent.*;

public class exchange {
    private final static LinkedBlockingQueue<String> message_queue = new LinkedBlockingQueue<String>();
    public static void main(String[] args) throws InterruptedException {
        Map<String, List<String>> data = new HashMap<String, List<String>>();
        System.out.println("\n** Calls to be made **\n");

        URL path = exchange.class.getResource("calls.txt");
        File file_name = new File(path.getFile());
        Scanner scanner = null;
        try {
            scanner = new Scanner(new File(String.valueOf(file_name)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            ArrayList<String> list=new ArrayList<String>();
            String [] parts1 = line.split(",",2);
            String v1 = parts1[0].replaceAll("\\W", "");
            String v2 = parts1[1].replaceAll("\\W", " ").trim();
            String [] parts2 = v2.split(" ");
            for (int i =0; i<parts2.length;i++){
                list.add(parts2[i]);
            }
            data.put(v1,list);
        }
        //System.out.println(data);
        for (String name: data.keySet()){
            String key = name;
            List<String> value = data.get(name);
            System.out.println(key + ": " + value);
        }
        System.out.println("");
        //System.out.println(init);
        int data_size = data.size();
      //  System.out.println(data_size);
        ExecutorService  eservice = Executors.newFixedThreadPool(data_size);
        for (String name : data.keySet()) {
            //System.out.println(name +"-----"+data.get(name));
            eservice.execute(new call(name,data.get(name)));

        }
        eservice.shutdown();
        long init = System.currentTimeMillis();
        while ( System.currentTimeMillis()-init < 10000) {
            while (!message_queue.isEmpty())
            {
                String message =message_queue.poll();
               // System.out.println("-----"+message);
                String[] mess_parts = message.split(",");
              //  System.out.println("-----"+mess_parts[0]);
                System.out.println(mess_parts[0]+" received "+mess_parts[1]+" message from "+mess_parts[2]+" ["+mess_parts[3]+"]");
                //System.out.println(init);
                init = System.currentTimeMillis();
            }
        }
        System.out.println("\nMaster has received no replies for 10 seconds, ending...");
    }


    private static class call implements Runnable{
        private String name;
        private List<String> friend_list;
        public call(Comparable<String> name, Object strings) {
            this.name= (String) name;
          //  System.out.println(key);
            this.friend_list= (List<String>) strings;
            //System.out.println(strings);

        }

        public void run()
        {
           long current_time = System.currentTimeMillis();
            for(String friend : friend_list){
                    long timestamp =  System.currentTimeMillis();
                    try {
                        introcal(friend, timestamp);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
            }

            while (System.currentTimeMillis()-current_time < 5000){
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("\nProcess " + name + " has received no calls for 5 seconds, ending...");
        }

        private void introcal(String friend, long timestamp) throws InterruptedException {
            Thread.sleep(100);
            String intro_message = friend+","+"intro"+","+name+","+timestamp;
            String reply_message = name+","+"reply"+","+friend+","+timestamp;
            //System.out.println(intro);
            //System.out.println(reply);
            message_queue.put(intro_message);
            Thread.sleep(100);
            message_queue.put(reply_message);
        }

    }

}
