public class Runner {
    public static void main(String[] args) {
        Stack nums = new Stack(5);
        nums.push(15);
        nums.push(8);
        nums.push(10);
        System.out.println(nums.peek());
        nums.push(23);
        nums.push(12);
        System.out.println("size is :"+nums.size());
        System.out.println(nums.pop());
        System.out.println("size is :"+nums.size());
        System.out.println("stack is empty :"+nums.isEmpty());
        nums.show();

        DStack nums1 = new DStack();
        nums1.push(20);
        nums1.push(34);
        nums1.push(124);
        nums1.push(209);
        nums1.push(267);
        nums1.push(254);
        nums1.push(212);
        nums1.pop();
        nums1.pop();
        nums1.pop();
        nums1.pop();
        nums1.pop();
                


        nums1.show();
    }
}
