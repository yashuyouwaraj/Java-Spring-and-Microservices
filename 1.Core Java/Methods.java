
class Computer {
    public void playMusic(){
        System.out.println("Playing music...");
    }

    public String getMePen(int cost){
        if(cost>100){
            return "I will get you a pen";
        }else{
            return "I will not get you a pen";
        }
    }
    
}

public class Methods {
    public static void main(String[] args) {
        Computer objComputer = new Computer();
        objComputer.playMusic();
        String result = objComputer.getMePen(150);
        System.out.println(result);
    }
}
