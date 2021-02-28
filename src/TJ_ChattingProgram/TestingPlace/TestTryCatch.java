package TJ_ChattingProgram.TestingPlace;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
* @class : TestTryCatch.java
* @modifyed : 2021-02-28 오전 4:46
* @usage : try문 안에서 예외 발생시, 예외처리 후 다시 돌아오는지 테스트.
 * 결과 : 안돌아오고 catch문 이후 코드부터 실행
**/
public class TestTryCatch {

    public static void method(){
        try{
            int value = Integer.parseInt("E"); // 예외 발생
        }catch(NumberFormatException e){
            System.out.println("예외처리");
        }
    }

    public static void test(){
        Runnable runnable = ()->{
            System.out.println("어쩌구저쩌구..");
            method();
            System.out.println("예외처리 후 코드");
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }

    public static void main(String[] args) throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(4);
        test();

    }
}
