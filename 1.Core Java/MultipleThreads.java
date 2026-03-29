class MyThread extends Thread{
    public void run(){
        for(int i=0; i<=200; i++){
            System.out.println("Hi");
        }
    }
}

class MyThread2 extends Thread{
    public void run(){
        for(int i=0; i<=200; i++){
            System.out.println("Hello");
        }
    }
}

public class MultipleThreads {
    public static void main(String[] args) {
        MyThread t1 = new MyThread();
        MyThread2 t2 = new MyThread2();

        t1.start();
        t2.start();
        
    }
}
