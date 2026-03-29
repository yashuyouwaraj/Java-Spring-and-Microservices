class Laptop1{
    String model;
    int price;

    public String toString() {
        return "Model: " + model + ", Price: " + price;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Laptop1 laptop1 = (Laptop1) obj;
        return price == laptop1.price && model.equals(laptop1.model);
    }
}

public class ObjectClassequalstoStringhashCode {

    public static void main(String[] args) {
        Laptop1 obj = new Laptop1();
        obj.model = "Dell";
        obj.price = 50000;

        Laptop1 obj2 = new Laptop1();
        obj2.model = "Lenevo Yoga";
        obj2.price = 1000;

        boolean result = obj.equals(obj2);

    	System.out.println(obj.toString());
//   	System.out.println(obj);
        System.out.println(result);
    }
}