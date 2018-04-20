
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

public class SolvePuzzle {
    public static PENode solveWithBFS(PENode node){
        Queue<PENode> queue = new LinkedList<PENode>();
        queue.add(node);
        
        while(true){
            PENode parent = queue.poll();
            if(parent.isSolved()) return parent;

            if(parent.canLeftMove()) queue.add(parent.setLeftMoveChild());
            if(parent.canRightMove()) queue.add(parent.setRightMoveChild());
            if(parent.canUpMove()) queue.add(parent.setUpMoveChild());
            if(parent.canDownMove()) queue.add(parent.setDownMoveChild());
        }
    }
    
    public static PENode solveWithDLS(PENode node, int l){
        PENode peNode = node;
        peNode.unlink();
        PENode resNode = dlsExpandNode(peNode, l);
        return resNode;
    } 
    
    public static PENode solveWithIDS(PENode node){
        for(int l=1; true; l++){
            PENode peNode = node;
            peNode.unlink();
            PENode resNode = dlsExpandNode(peNode, l);
            if(resNode!=null) return resNode;
        }
    }
    
    private static PENode dlsExpandNode(PENode node, int l){
        if(node==null) return null; 
        if(node.isSolved()) return node;
        if(node.getDepth()>=l) return null;
        
        PENode left,right,up,down;
        
        left = dlsExpandNode(node.setLeftMoveChild(), l);
        if(left!=null) return left; 
        else right = dlsExpandNode(node.setRightMoveChild(), l);
        
        if(right!=null) return right; 
        else up = dlsExpandNode(node.setUpMoveChild(), l);
        
        if(up!=null) return up;
        else down = dlsExpandNode(node.setDownMoveChild(), l);
        
        if(down!=null) return down;
        
        if(node.getFatherNode()!=null)
            node.getFatherNode().getChildNodes().remove(node);
        node.unlink();
        node = null;
        
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
