package TJ_ChattingProgram.TestingPlace;

import sample.Main;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
* @class : TestCreateStringWithWrongParam.java
* @modifyed : 2021-02-28 오전 1:22
* @usage : 잘못된 파라미터로 String 생성시 발생하는 예외를 조사
**/

public class TestCreateStringWithWrongParam {
    public static void main(String[] args) {

        // 잘못된 파라미터로 String 을 생성하는 작업  --> 예외 발생 유도
        Runnable createStringWithWrongParamTask = ()->{
            String str = new String(new byte[10] , 0, -1, StandardCharsets.UTF_8);
        };

        // 스레드에 작업을 넣어 start
        Thread thread = new Thread(createStringWithWrongParamTask);
        thread.setDaemon(true);
        thread.start();

        // 스레드풀에 작업읗 submit
        ExecutorService es = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        es.submit(createStringWithWrongParamTask);
        es.execute(createStringWithWrongParamTask);

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        es.shutdown();

    }

}
