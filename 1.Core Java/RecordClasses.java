record Alien(int id, String name) {
    public Alien {
        if(id<0){
            throw new IllegalArgumentException("Id cannot be negative");
        }
    }
}


public class RecordClasses {
    public static void main(String[] args) {
        Alien obj1 = new Alien(1, "Yashu");
        Alien obj2= new Alien(2, "Laxmi");
        Alien obj3= new Alien(1, "Yashu");

        System.out.println(obj1.equals(obj3));
        System.out.println(obj1);
        System.out.println(obj2.name());
    }
}
