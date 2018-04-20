
import java.text.DecimalFormat;
import java.util.Scanner;

public class PuzzleEight {
    public static PENode currentPuzzle;
    
    public static void main(String[] args) {
        currentPuzzle = new PENode();
        setPuzzle();
        
        while(menu());
        return;
    }
    
    public static void setPuzzle(){
        System.out.print("\nEnter Number Of Random Puzzle Rotation: ");
        int rotateNumber = inputInt(1, 100);
        currentPuzzle.setRandomPuzzle(rotateNumber, PENode.FIRST_FINAL_STATE);
    }
    
    public static void bfsSolvePuzzle(){
        long startTime = System.nanoTime();
        PENode resultNode = SolvePuzzle.solveWithBFS(currentPuzzle.unlink());
        long endTime = System.nanoTime();
        double secTime = (double)(endTime - startTime)/1000000000;
        
        StylishPrinter.println("\nPuzzle Solve Successfuly Using BFS!", StylishPrinter.ANSI_BOLD_GREEN);
        System.out.println("Solving Time: " + new DecimalFormat("#.###").format(secTime) + "s");
        StylishPrinter.println("\nSolving Routine:", StylishPrinter.ANSI_BOLD_GREEN);
        resultNode.printRoutine();
    }
    
    public static void idsSolvePuzzle(){
        long startTime = System.nanoTime();
        PENode resultNode = SolvePuzzle.solveWithIDS(currentPuzzle.unlink());
        long endTime = System.nanoTime();
        double secTime = (double)(endTime - startTime)/1000000000;
        
        StylishPrinter.println("\nPuzzle Solve Successfuly Using IDS!", StylishPrinter.ANSI_BOLD_GREEN);
        System.out.println("Solving Time: " + new DecimalFormat("#.###").format(secTime) + "s");
        StylishPrinter.println("\nSolving Routine:", StylishPrinter.ANSI_BOLD_GREEN);
        resultNode.printRoutine();
    }
    
    public static void dlsSolvePuzzle(){
        System.out.println("\nEnter Limiting Depth: ");
        int l = inputInt(1, 100);
        
        long startTime = System.nanoTime();
        PENode resultNode = SolvePuzzle.solveWithDLS(currentPuzzle.unlink(), l);
        long endTime = System.nanoTime();
        double secTime = (double)(endTime - startTime)/1000000000;
        
        if(resultNode!=null){
            StylishPrinter.println("\nPuzzle Solve Successfuly Using DLS!", StylishPrinter.ANSI_BOLD_GREEN);
            System.out.println("Solving Time: " + new DecimalFormat("#.###").format(secTime) + "s");
            StylishPrinter.println("\nSolving Routine:", StylishPrinter.ANSI_BOLD_GREEN);
            resultNode.printRoutine();
        }
        else{
            StylishPrinter.println("\nCant Solve Puzzle With this Limiting Depth!", StylishPrinter.ANSI_BOLD_YELLOW);
            System.out.println("RunTime: " + new DecimalFormat("#.###").format(secTime) + "s");
        }
    }
    
    public static boolean menu(){
        StylishPrinter.println("\nMenu:", StylishPrinter.ANSI_BOLD_RED);
        System.out.println("1: Show Current Puzzle");
        System.out.println("2: Generate new Random Puzzle");
        System.out.println("3: Solve Puzzle With BFS");
        System.out.println("4: Solve Puzzle With IDS");
        System.out.println("5: Solve Puzzle With DLS");
        System.out.println("6: Exit");
        System.out.print("\nEnter Your Choice: ");
        int choice = inputInt(1, 6);
        
        if(choice==1){
            StylishPrinter.println("\nCurrent Puzzle:", StylishPrinter.ANSI_BOLD_RED);
            currentPuzzle.printPuzzle();
        }
        else if(choice==2) setPuzzle();
        else if(choice==3) bfsSolvePuzzle();
        else if(choice==4) idsSolvePuzzle();
        else if(choice==5) dlsSolvePuzzle();
        else if(choice==6) return false;
        
        return true;
    }
    
    public static int inputInt(int min, int max){
        Scanner scanner = new Scanner(System.in);
        int result;
        
        while(true){
            String input = scanner.next();
            try{result = Integer.valueOf(input);}
            catch(NumberFormatException ex){
                System.out.print("Wrong format! Try Again: ");
                continue;
            }
            
            if(result<min || result>max){
                System.out.print("Wrong range! Try Again: ");
                continue;
            }
            
            return result;
        }
    }
}