import java.util.Scanner;

public class Game {
    public static void main(String[] args) {
        Solver.loop();
        boolean test = true;
        while(test == true){
            System.out.println("Voulez vous rejouer(Oui: Y/Non : N)");
            Scanner sc =  new Scanner(System.in);
            String input = sc.nextLine();
            if (input.equals("Y")){
                Solver.loop();
            }
            else if (input.equals("N")){
                System.out.println("Fin du jeu");
                test = false;
            }else{
                break;
            }
        sc.close();
        }
       
    }

}
