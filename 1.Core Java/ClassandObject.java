class Calculator {
    public int add(int a, int b){
        return a+b;
    }
    
}



public class ClassandObject {
    public static void main(String[] args) {
        int num1=10;
        int num2=20;

        Calculator objCalculator = new Calculator();
        int result = objCalculator.add(num1, num2);
        System.out.println("The sum of "+num1+" and "+num2+" is: "+result);
    }
}


// Object Oriented programming

// Object - Properties and Beahaviors
//Class - Blueprint of an object
//Object - Instance of a class