package TJ_ChattingProgram;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class TestThreadTimeOut {

    public static ExecutorService createFixedTimeoutExecutorService(
            int poolSize, long keepAliveTime, TimeUnit timeUnit)
    {
        ThreadPoolExecutor e =
                new ThreadPoolExecutor(poolSize, poolSize,
                        keepAliveTime, timeUnit, new LinkedBlockingQueue<Runnable>());
        e.allowCoreThreadTimeOut(true);
        return e;
    }

    // allowCoreThreadTimeOut 을 true 로 줌으로서,
    // main 스레드가 종료되고 난 뒤 keepAliveTime 이 지나면 스레드풀도 자동으로 닫힌다.
    public static void main(String[] args) throws InterruptedException {
        ExecutorService executorService = createFixedTimeoutExecutorService(
                Runtime.getRuntime().availableProcessors(),
                10L, TimeUnit.SECONDS
        );
        int count = 0;
        while(count++<10){
            Runnable runnable =  ()->{
                IntStream.range(1,100).forEach(System.out::print);
            };
            executorService.submit(runnable);
        }

        Thread.sleep(10);
        System.out.println("중단점 삽입");


        try {
            ServerSocket serverSocket = new ServerSocket(); // create
            serverSocket.bind(new InetSocketAddress("localhost",5001)); // bind
            while (true) {
                Socket socket = serverSocket.accept(); // listen & accept()

            }

        } catch (IOException exception) {
            exception.printStackTrace();
        }


    }
}
