public class Runner {
    public static void main(String[] args) {
        Queue q = new Queue(5);
        q.enQueue(5);
        q.enQueue(10);
        q.enQueue(15);
        q.enQueue(20);
        q.enQueue(25);

        q.deQueue();
        
        q.enQueue(35);

        System.out.println("Size : "+q.getSize());
        System.out.println("Size is empty :"+q.isEmpty());
        System.out.println("Size is full :"+q.isFull());

        q.show();
    }
}
