package ThisJava_2;

import Chatting.App;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

public class AppMain extends Application {

    public AppMain(){
        System.out.println("AppMain 생성자 호출 스레드 : " + Thread.currentThread().getName());
//        Set<Thread> threads = Thread.getAllStackTraces().keySet();
//        for(var thread : threads){
//            System.out.printf("이름 : %-20s 그룹 : %-20s %s",
//                    thread.getName(),thread.getThreadGroup(),System.lineSeparator());
//        }
    }

    @Override
    public void init(){
        Parameters parameters = getParameters();
//        Map<String,String> map = parameters.getNamed();
//        for(Map.Entry<String,String> entry : map.entrySet()){
//            System.out.println("key : " + entry.getKey() + " value : " + entry.getValue());
//        }
        List<String> list = parameters.getRaw();
        Stream<String> stringStream = list.stream();
        stringStream.forEach(System.out::println);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        System.out.println("start 호출 스레드 : " + Thread.currentThread().getName());
        primaryStage.show();

    }
    public static void main(String[] args){
        System.out.println("main 호출 스레드 : " + Thread.currentThread().getName());
        launch(args);
    }

    @Override
    public void stop() throws Exception {
        System.out.println("stop 호출 스레드 : " + Thread.currentThread().getName());
        super.stop();

    }
}
