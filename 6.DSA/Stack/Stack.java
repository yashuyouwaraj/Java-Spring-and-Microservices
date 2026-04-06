public class Stack {
    int top = 0;
    int n;
    int stack[];

    public Stack(int n) {
        this.n = n;
        this.stack = new int[n];
    }

    public void push(int data) {
        if (top >= n) {
            System.out.println("Stack overflow");
            return;
        }

        stack[top] = data;
        top++;
    }

    public int pop() {
        if (top <= 0) {
            System.out.println("Stack underflow");
            return -1;
        }

        top--;
        int data = stack[top];
        stack[top] = 0;
        return data;
    }

    public int peek() {
        if (top <= 0) {
            System.out.println("Stack underflow");
            return -1;
        }
        int data = stack[top-1];
        return data;
    }

    public int size(){
        return top;
    }

    public boolean isEmpty(){
        return  top<=0;
    }

    

    public void show() {
        for (int n: stack) {
            System.out.print(n + " ");
        }
    }
}
