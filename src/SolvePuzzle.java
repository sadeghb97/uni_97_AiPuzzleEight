
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;

public class SolvePuzzle {
    private static int allNodes = 0;
    private static int curNodes = 0;
    private static int maxNodes = 0;
    
    private static void clearNodesStats(){
        allNodes=0;
        curNodes=0;
        maxNodes=0;
    }

    public static int getAllNodes() {return allNodes;}
    public static int getCurNodes() {return curNodes;}
    public static int getMaxNodes() {return maxNodes;}
    
    public static PENode solveWithBFS(PENode node){
        clearNodesStats();
        Queue<PENode> queue = new LinkedList<PENode>();
        queue.add(node);
        allNodes++;
        
        while(queue.size() != 0){
            PENode parent = queue.poll();
            if(parent.isSolved()) return parent;

            if(parent.canLeftMove()){
                queue.add(parent.setLeftMoveChild());
                allNodes++;
            }
            if(parent.canRightMove()){
                queue.add(parent.setRightMoveChild());
                allNodes++;
            }
            if(parent.canUpMove()){
                queue.add(parent.setUpMoveChild());
                allNodes++;
            }
            if(parent.canDownMove()){
                queue.add(parent.setDownMoveChild());
                allNodes++;
            }
        }
        
        return null;
    }
    
    public static PENode solveWithAStar(PENode node){
        clearNodesStats();
        
        int finalState;
        if(node.isSolvableWithMainGoalState()) finalState=PENode.FIRST_FINAL_STATE;
        else finalState = PENode.SECOND_FINAL_STATE;
        
        PriorityQueue<PENode> queue = 
            new PriorityQueue<PENode>(new PENode.AStarComparator());
        queue.add(node);
        allNodes++;
        
        while(queue.size() != 0){
            PENode parent = queue.remove();
            if(parent.isSolved()) return parent;

            if(parent.canLeftMove()){
                queue.add(parent.setLeftMoveChild(true, finalState));
                allNodes++;
            }
            if(parent.canRightMove()){
                queue.add(parent.setRightMoveChild(true, finalState));
                allNodes++;
            }
            if(parent.canUpMove()){
                queue.add(parent.setUpMoveChild(true, finalState));
                allNodes++;
            }
            if(parent.canDownMove()){
                queue.add(parent.setDownMoveChild(true, finalState));
                allNodes++;
            }
        }
        
        return null;
    }
    
    public static PENode solveWithDLS(PENode node, int l){
        clearNodesStats();
        PENode peNode = node;
        peNode.unlink();
        PENode resNode = dlsExpandNode(peNode, l);
        return resNode;
    } 
    
    public static PENode solveWithIDS(PENode node){
        clearNodesStats();
        for(int l=1; true; l++){
            curNodes=0;
            PENode peNode = node;
            peNode.unlink();
            PENode resNode = dlsExpandNode(peNode, l);
            if(resNode!=null) return resNode;
        }
    }
    
    private static PENode dlsExpandNode(PENode node, int l){
        if(node==null) return null;
        allNodes++;
        curNodes++;
        
        if(curNodes>maxNodes) maxNodes=curNodes;
        if(node.isSolved()) return node;
        if(node.getDepth()>=l){
            node.unlink();
            node = null;
            curNodes--;
            return null;
        }
        
        PENode left,right,up,down;
        
        left = dlsExpandNode(node.setLeftMoveChild(), l);
        if(left!=null) return left; 
        else right = dlsExpandNode(node.setRightMoveChild(), l);
        
        if(right!=null) return right; 
        else up = dlsExpandNode(node.setUpMoveChild(), l);
        
        if(up!=null) return up;
        else down = dlsExpandNode(node.setDownMoveChild(), l);
        
        if(down!=null) return down;
        
        //if(node.getFatherNode()!=null) node.getFatherNode().getChildNodes().remove(node);
        node.unlink();
        node = null;
        curNodes--;
        
        return null;
    }
    
    /*public static PENode solveWithIDS(PENode node){
        for(int l=1; true; l++){
            Stack stack = new Stack();
            PENode peNode = node;
            peNode.unlink();
            stack.add(peNode);
            
            while(!stack.isEmpty()){
                PENode head = (PENode) stack.pop();
                if(head.isSolved()) return head;
                
                if(head.getDepth()<l){
                    if(head.canLeftMove()) stack.add(head.setLeftMoveChild());
                    if(head.canRightMove()) stack.add(head.setRightMoveChild());
                    if(head.canUpMove()) stack.add(head.setUpMoveChild());
                    if(head.canDownMove()) stack.add(head.setDownMoveChild());
                }
                else if(head.getFatherNode()!=null)
                    head.getFatherNode().getChildNodes().remove(head);
            }
        }
    }*/
}
