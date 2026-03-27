abstract class Car {
    public abstract void drive();

    public abstract void fly();

    public void playMusic() {
        System.out.println("Playing music");
    }
}

abstract class WagnoR extends Car {
    public void drive() {
        System.out.println("Driving WagnoR");
    }
}

class UpdatedWagnoR extends WagnoR { //concrete class
    public void fly() {
        System.out.println("Flying WagnoR");
    }
}

//abstract class cannot be instantiated, we cannot create object of abstract class
// Car obj=new Car(); //error: Car is abstract; cannot be instantiated


public class AbstractKeyword {
    public static void main(String[] args) {
        // Car obj=new Car();
        // Car obj=new WagnoR();
        Car car = new UpdatedWagnoR();
        car.drive();
        car.fly();
        car.playMusic();
    }
}

