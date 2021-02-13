package Practice_tutorial;

public class practice {

    public void Testing(){

        Runnable task = new Task();
        Thread thread = new Thread(task);

        Thread thread2 = new Thread(new Runnable(){
            public void run(){
                // 작업스레드가 실행할 코드;
            }
        });

        Thread thread3 = new Thread( () -> {
            // 작업쓰레드가 실행할 코드;
        } );

        thread.start();

    }

}
