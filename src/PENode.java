
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.Stack;

public class PENode {
    public static final int FIRST_FINAL_STATE = 1;
    public static final int SECOND_FINAL_STATE = 2;
    
    public static final int[][] FIRST_GOAL_ARRAY = new int[][]{new int[]{1,2,3}, new int[]{4,5,6}, new int[]{7,8,0}};
    public static final int[][] SECOND_GOAL_ARRAY= new int[][]{new int[]{0,1,2}, new int[]{3,4,5}, new int[]{6,7,8}};
    
    private static final String FRAME_COLOR = StylishPrinter.ANSI_BOLD_CYAN; 
    private static final String DIGIT_COLOR = StylishPrinter.ANSI_BOLD_PURPLE; 
    private static final String SPLITTER_COLOR = StylishPrinter.ANSI_BOLD_CYAN; 
    
    //private List<PENode> childNodes;
    private PENode fatherNode;
    private int[][] puzzle;
    private int rowZero;
    private int colZero;
    private int depth;
    private int heuristic;

    public PENode() {
        depth=0;
        //childNodes = new ArrayList<>();
    }

    public PENode(PENode fatherNode, int[][] puzzle, int rowZero, int colZero) {
        depth=0;
        this.fatherNode = fatherNode;
        this.puzzle = puzzle;
        this.rowZero = rowZero;
        this.colZero = colZero;
        //childNodes = new ArrayList<>();
    }

    public PENode getFatherNode() {
        return fatherNode;
    }

    /*public List<PENode> getChildNodes() {
        return childNodes;
    }*/

    public int[][] getPuzzleArray() {
        return puzzle;
    }

    public int getRowZero() {
        return rowZero;
    }

    public int getColZero() {
        return colZero;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }
    
    public int getHeuristic() {
        return heuristic;
    }
    
    public void setHeuristic(int heuristic) {
        this.heuristic = heuristic;
    }
    
    public PENode unlink(){
        //childNodes = new ArrayList<>();
        fatherNode = null;
        return this;
    }
    
    public int[][] getPuzzleArrayClone(){
        int[][] puzzleArray = new int[3][0];
        for(int i=0; 3>i; i++){
            puzzleArray[i] = new int[3];
            for(int j=0; 3>j; j++) puzzleArray[i][j]=puzzle[i][j];
        }
        return puzzleArray;
    }
    
    public void setRandomPuzzle(int finalState){
        System.out.print("\nEnter Number Of Random Puzzle Rotation: ");
        int moveNum = SbproScanner.inputInt(1, 200);
        
        int[][] puzzleArray;
        int row,col;
        
        if(finalState == FIRST_FINAL_STATE){
            puzzleArray = FIRST_GOAL_ARRAY;
            row = 2;
            col = 2;
        }
        else{
            puzzleArray = SECOND_GOAL_ARRAY;
            row=0;
            col=0;
        }
        
        puzzle = puzzleArray;
        rowZero = row;
        colZero = col;
        
        for(int i=0; moveNum>i; i++){
            Random random = new Random();
            int move = random.nextInt(4);
            PENode resNode = null;
            
            if(move == 0){
                if(canLeftMove()) resNode = setLeftMoveChild();
                else i--;
            }
            else if(move == 1){
                if(canRightMove()) resNode = setRightMoveChild();
                else i--;
            }
            else if(move == 2){
                if(canUpMove()) resNode = setUpMoveChild();
                else i--;
            }
            else if(move == 3){
                if(canDownMove()) resNode = setDownMoveChild();
                else i--;
            }
            
            if(resNode!=null){
                puzzle = resNode.getPuzzleArray();
                rowZero = resNode.getRowZero();
                colZero = resNode.getColZero();
            }
        }
        
        //childNodes = new ArrayList<>();
        fatherNode = null;
        StylishPrinter.println("\nGenerated Puzzle:", StylishPrinter.ANSI_BOLD_GREEN);
        printPuzzle();
    }
    
    public void inputPuzzle(){
        puzzle = new int[3][];
        for(int i=0; 3>i; i++) puzzle[i] = new int[3];
        for(int i=0; 3>i; i++) for(int j=0; 3>j; j++) puzzle[i][j] = -1;
        
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please Enter Puzzle Numbers: ");
        for(int i=0; 3>i; i++){
            for(int j=0; 3>j; j++){
                System.out.print((i+1) + "-" + (j+1) + ": ");
                inputCell(i, j);
            }
        }
    }
    
    public void inputCell(int row, int col){
        Scanner scanner = new Scanner(System.in);
        int result;
        
        while(true){
            String input = scanner.next();
            try{result = Integer.valueOf(input);}
            catch(NumberFormatException ex){
                System.out.print("Wrong Format! Try Again: ");
                continue;
            }
            
            if(result<0 || result>8){
                System.out.print("Wrong Range! Try Again: ");
                continue;
            }
            
            for(int i=0; 3>i; i++) 
                for(int j=0; 3>j; j++)
                    if(result == puzzle[i][j]){
                        System.out.print("Duplicate Number! Try Again: ");
                        continue;
                    }
            
            if(result==0){
                rowZero = row;
                colZero = col;
            }
            
            break;
        }
        
        puzzle[row][col] = result;
    }
    
    public int calcHeuristic(){
        int res=0;
        for(int i=0; 3>i; i++){
            for(int j=0; 3>j; j++){
                int plus = 0;
                if(puzzle[i][j]==1) plus=(Math.abs(i-0)+Math.abs(j-0));
                else if(puzzle[i][j]==2) plus=(Math.abs(i-0)+Math.abs(j-1));
                else if(puzzle[i][j]==3) plus=(Math.abs(i-0)+Math.abs(j-2));
                else if(puzzle[i][j]==4) plus=(Math.abs(i-1)+Math.abs(j-0));
                else if(puzzle[i][j]==5) plus=(Math.abs(i-1)+Math.abs(j-1));
                else if(puzzle[i][j]==6) plus=(Math.abs(i-1)+Math.abs(j-2));
                else if(puzzle[i][j]==7) plus=(Math.abs(i-2)+Math.abs(j-0));
                else if(puzzle[i][j]==8) plus=(Math.abs(i-2)+Math.abs(j-1));
                else if(puzzle[i][j]==0) plus=(Math.abs(i-2)+Math.abs(j-2));
                res+=plus;
            }
        }
        
        return res;
    }
    
    public boolean isSolved(){
        boolean firstState=true;
        boolean secondState=true;
            
        for(int i=0; 3>i; i++){
            for(int j=0; 3>j; j++){
                if(puzzle[i][j] != (3*i+j+1)) firstState=false;
                if(puzzle[i][j] != (3*i+j)) secondState=false;
                if((i==2 && j==1)) break;
            }
            if(!firstState && !secondState) break;
        }
        
        return firstState||secondState;
    }
    
    public void printPuzzle(){
        StylishPrinter.println("-------------", FRAME_COLOR);
        for(int i=0; 3>i; i++){
            for(int j=0; 3>j; j++){
                if(j!=0) System.out.print(" ");
                StylishPrinter.print("| ", FRAME_COLOR);
                if(puzzle[i][j] != 0) 
                    StylishPrinter.print(String.valueOf(puzzle[i][j]), DIGIT_COLOR);
                else System.out.print(" ");
            }
            StylishPrinter.println(" |", FRAME_COLOR);
            StylishPrinter.println("-------------", FRAME_COLOR);
        }
    }
    
    public void printRoutine(){
        int lineNum = 5;
        int splitterSpace = 5;
        
        List<PENode> nodes = new ArrayList();
        PENode p = this;
        while(p!=null){
            nodes.add(p);
            p=p.fatherNode;
        }
        
        for(int i=nodes.size()-1; i>=0; i-=lineNum){
            PENode[] lineNodes = new PENode[lineNum];
            for(int j=0; lineNum>j; j++){
                int index = i-j;
                if(index>=0) lineNodes[j] = nodes.get(index);
                else lineNodes[j] = null;
            }
            
            if(i != nodes.size()-1) System.out.println("");
            printRoutinePrintPuzzleHorizontalSplitter(lineNum, lineNodes, splitterSpace);
            
            for(int puzzleCellsRow=0; 3>puzzleCellsRow; puzzleCellsRow++){
                for(int puzzleNumber=0; lineNum>puzzleNumber && lineNodes[puzzleNumber]!=null; puzzleNumber++){
                    int[] puzzleCells = lineNodes[puzzleNumber].getPuzzleArray()[puzzleCellsRow];
                    
                    for(int puzzleCellsCol=0; 3>puzzleCellsCol; puzzleCellsCol++){
                        if(puzzleCellsCol!=0) System.out.print(" ");
                        StylishPrinter.print("| ", FRAME_COLOR);
                        if(puzzleCells[puzzleCellsCol] != 0) 
                            StylishPrinter.print(String.valueOf(puzzleCells[puzzleCellsCol]), DIGIT_COLOR);
                        else System.out.print(" ");
                    }
                    
                    StylishPrinter.print(" |", FRAME_COLOR);
                    if(i-puzzleNumber > 0){
                        if(puzzleCellsRow==1) System.out.print(" --> ");
                        else StylishPrinter.printSpace(splitterSpace);
                    }
                }
                System.out.println("");
                printRoutinePrintPuzzleHorizontalSplitter(lineNum, lineNodes, splitterSpace);
            }
            
        }
    }
    
    private void printRoutinePrintPuzzleHorizontalSplitter(int lineNum, PENode[] lineNodes, int splitterSpace){
        for(int j=0; lineNum>j && lineNodes[j]!=null; j++){
            StylishPrinter.print("-------------", FRAME_COLOR);
            if(lineNum>j+1) StylishPrinter.printSpace(splitterSpace);
        }
        System.out.println("");        
    }
    
    /*public void printRoutine(){
        Stack stack = new Stack();
        PENode p = this;
        while(p!=null){
            stack.push(p);
            p=p.fatherNode;
        }
        
        while(!stack.isEmpty()){
            PENode node = (PENode) stack.pop();
            node.printPuzzle();
            if(!stack.isEmpty()) System.out.println("");
        }
    }*/
    
    public boolean canLeftMove(){
        if(puzzle[0][0] == 0 || puzzle[1][0] == 0 || puzzle[2][0] == 0) return false;
        return true;
    }
    
    public boolean canRightMove(){
        if(puzzle[0][2] == 0 || puzzle[1][2] == 0 || puzzle[2][2] == 0) return false;
        return true;
    }
    
    public boolean canUpMove(){
        if(puzzle[0][0] == 0 || puzzle[0][1] == 0 || puzzle[0][2] == 0) return false;
        return true;
    }
    
    public boolean canDownMove(){
        if(puzzle[2][0] == 0 || puzzle[2][1] == 0 || puzzle[2][2] == 0) return false;
        return true;
    }
    
    public PENode setLeftMoveChild(boolean calcHeuristic){
        if(!canLeftMove()) return null;
        int[][] puzzleArray = getPuzzleArrayClone();
        int row,col;
        
        int temp = puzzleArray[rowZero][colZero-1];
        puzzleArray[rowZero][colZero-1] = puzzleArray[rowZero][colZero];
        puzzleArray[rowZero][colZero] = temp;
        row = rowZero;
        col = colZero-1;
        
        PENode child = new PENode(this, puzzleArray, row, col);
        child.setDepth(depth+1);
        if(calcHeuristic) child.setHeuristic(child.calcHeuristic());
        //childNodes.add(child);
        return child;
    }
    
    public PENode setRightMoveChild(boolean calcHeuristic){
        if(!canRightMove()) return null;
        int[][] puzzleArray = getPuzzleArrayClone();
        int row,col;
        
        int temp = puzzleArray[rowZero][colZero+1];
        puzzleArray[rowZero][colZero+1] = puzzleArray[rowZero][colZero];
        puzzleArray[rowZero][colZero] = temp;
        row = rowZero;
        col = colZero+1;
        
        PENode child = new PENode(this, puzzleArray, row, col);
        //childNodes.add(child);
        child.setDepth(depth+1);
        if(calcHeuristic) child.setHeuristic(child.calcHeuristic());
        return child;
    }
    
    public PENode setUpMoveChild(boolean calcHeuristic){
        if(!canUpMove()) return null;
        int[][] puzzleArray = getPuzzleArrayClone();
        int row,col;
        
        int temp = puzzleArray[rowZero-1][colZero];
        puzzleArray[rowZero-1][colZero] = puzzleArray[rowZero][colZero];
        puzzleArray[rowZero][colZero] = temp;
        row = rowZero-1;
        col = colZero;
        
        PENode child = new PENode(this, puzzleArray, row, col);
        //childNodes.add(child);
        child.setDepth(depth+1);
        if(calcHeuristic) child.setHeuristic(child.calcHeuristic());
        return child;
    }
    
    public PENode setDownMoveChild(boolean calcHeuristic){
        if(!canDownMove()) return null;
        int[][] puzzleArray = getPuzzleArrayClone();
        int row,col;
        
        int temp = puzzleArray[rowZero+1][colZero];
        puzzleArray[rowZero+1][colZero] = puzzleArray[rowZero][colZero];
        puzzleArray[rowZero][colZero] = temp;
        row = rowZero+1;
        col = colZero;
        
        PENode child = new PENode(this, puzzleArray, row, col);
        //childNodes.add(child);
        child.setDepth(depth+1);
        if(calcHeuristic) child.setHeuristic(child.calcHeuristic());
        return child;
    }
    
    public PENode setLeftMoveChild(){ return setLeftMoveChild(false);}
    public PENode setRightMoveChild(){ return setRightMoveChild(false);}
    public PENode setUpMoveChild(){ return setUpMoveChild(false);}
    public PENode setDownMoveChild(){ return setDownMoveChild(false);}
    
    public static class AStarComparator implements Comparator<PENode>{
        @Override
        public int compare(PENode x, PENode y){
            if ((x.getDepth() + x.getHeuristic()) > (y.getDepth() + y.getHeuristic()))
                return 1;
            if ((x.getDepth() + x.getHeuristic()) < (y.getDepth() + y.getHeuristic()))
                return -1;
            return 0;
        }
    }
}
