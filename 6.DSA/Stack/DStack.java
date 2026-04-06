public class DStack {
    int top = 0;
    int capacity=2;
    int stack[]=new int[capacity];

    public void push(int data) {
        if(size()==capacity){
            expand();
        }

        stack[top] = data;
        top++;
    }
    private void expand(){
        int length=size();
        int newStack[] =new int[capacity*2];
        System.arraycopy(stack, 0, newStack, 0, length);
        stack = newStack;
        capacity=capacity*2;
    }

    public int pop() {
        if (top <= 0) {
            System.out.println("Stack underflow");
            return -1;
        }

        top--;
        int data = stack[top];
        stack[top] = 0;
        shrink();
        return data;
    }

    private void shrink() {
        int length = size();
        if (capacity <= 2 || length > capacity / 4) {
            return;
        }

        capacity = capacity / 2;
        int newStack[] = new int[capacity];
        System.arraycopy(stack, 0, newStack, 0, length);
        stack = newStack;
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
