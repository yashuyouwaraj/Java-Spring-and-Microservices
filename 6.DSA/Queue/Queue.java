
public class Queue {

    int queue[];
    int n;
    int size;
    int front;
    int rear;

    public Queue(int par) {
        this.n = par;
        this.queue = new int[n];
    }

    public void enQueue(int data) {
        if (size >= n) {
            System.out.println("Queue is full");
            return;
        }

        queue[rear] = data;
        rear = (rear + 1) % n;
        size++;
    }

    public int deQueue() {
        if (size <= 0) {
            System.out.println("Queue is empty");
            return -1;
        }

        int data = queue[front];
        queue[front] = 0;
        front = (front + 1) % n;
        size--;
        return data;
    }

    public int getSize(){
        return size;
    }

    public boolean isEmpty(){
        return size==0;
    }

    public boolean isFull(){
        return size==n;
    }

    public void show() {
        System.out.print("Elements : ");
        for (int i = 0; i < size; i++) {
            System.out.print(queue[(front + i)%n] + " ");
        }
    }
}
