
class ScapeGoatTree {
    SCGNode Root;
    int n;    //n is the size of tree
    int q;  //Overestimate of n

    ScapeGoatTree()
    {
        Root=null;
        n=0;
    }

    boolean IsEmpty()
    {
        if(Root==null)
        {
            return true;
        }
        return false;
    }

    void EmptyTree()
    {
        Root=null;
        n=0;
    }

    int GetSize(SCGNode Rt)
    {
        if(Rt==null)
        {
            return 0;
        }
        else
        {
            return (GetSize(Rt.Left)+1+GetSize(Rt.Right));
        }
    }

    int SearchKey(int Key)
    {
        return SearchKey(Root, Key);
    }

    private int SearchKey(SCGNode Rt, int Key)
    {
        if(Rt==null || Rt.Value==Key)
        {
            return Rt.Value;
        }
        else if(Rt.Value>Key)
        {
            return SearchKey(Rt.Left,Key);
        }
        else
        {
            return SearchKey(Rt.Right,Key);
        }
    }

    private  final int Log32(int Val)
    {
        final double lg32=2.4663034623764317;
        return (int)Math.ceil(lg32*Math.log(Val));
    }

    int InsertWithDepth(SCGNode N)
    {
        SCGNode Rt= Root;
        if(Rt==null)
        {
            Root=N;
            n++;
            q++;
            return 0;
        }
        boolean inserted=false;
        int depth=0;
        do
        {
            if(N.Value<Rt.Value)
            {
                if(Rt.Left==null)
                {
                    Rt.Left=N;
                    N.Parent=Rt;
                    inserted=true;
                }
                else 
                {
                    Rt=Rt.Left;
                }
            }
            else if(N.Value>Rt.Value)
            {
                if(Rt.Right==null)
                {
                    Rt.Right=N;
                    N.Parent=Rt;
                    inserted=true;
                }
                else
                {
                    Rt=Rt.Right;
                }
            }
            else
            {
                return -1;
            }
            depth++;
        }
        while(!inserted);
        n++;
        q++;
        return depth;
    }

    void Insert(int Key)
    {
        SCGNode n= new SCGNode(Key);
        int depth=InsertWithDepth(n);
        if(depth>Log32(q))  //n can be used as well
        {
            //Depth exceeded, find scapegoat
            SCGNode temp= n.Parent;
            while(3*GetSize(temp)<=2*GetSize(temp.Parent))
            {
                temp=temp.Parent;
                Rebuild(temp.Parent);
            }
        }
    }

    void Rebuild(SCGNode N)
    {
        int NodeSize=GetSize(N);
        SCGNode Parent=N.Parent;
        SCGNode[] Arr= new SCGNode[NodeSize];
        PackIntoArr(N,Arr,0);
        System.out.println("enetafd");
        if(Parent==null)
        {
			System.out.println("enetafd");
            Root=BuildBalanced(Arr,0,NodeSize);

            Root.Parent=null;
        }
        else if(Parent.Right==N)
        {
            Parent.Right=BuildBalanced(Arr,0,NodeSize);
            //Parent.Right.Parent=Parent;
            System.out.println(Parent.Right.Value);
        }
        else
        {
            Parent.Left= BuildBalanced(Arr,0,NodeSize);
            //Parent.Left.Parent=Parent;

        }
    }

    int PackIntoArr(SCGNode N, SCGNode[] Arr,int i)
    {
        if(N==null)
        {
            return i;
        }
        i=PackIntoArr(N.Left,Arr,i);
        Arr[i++]=N;

        return PackIntoArr(N.Right,Arr,i);
    }

    SCGNode BuildBalanced(SCGNode[] Arr, int i, int NodeSize)
    {
        if(NodeSize==0)
        {
            return null;
        }
        int m=NodeSize/2;
        Arr[i+m].Left=BuildBalanced(Arr,i,m);
        if(Arr[i+m].Left!=null)
        {
            Arr[i+m].Left.Parent=Arr[i+m];
        }
        Arr[i+m].Right=BuildBalanced(Arr,i+m+1,NodeSize-m-1);
        if(Arr[i+m].Right!=null)
        {
            Arr[i+m].Right.Parent=Arr[i+m];
        }
        return Arr[i+m];
    }

    void PreOrder()
    {
        PreOrder(Root);
    }

    private void PreOrder(SCGNode Rt)
    {
        if(Rt!=null)
        {
            System.out.println(Rt.Value);
            InOrder(Rt.Left);
            InOrder(Rt.Right);
        }
    }

    void InOrder()
    {
        InOrder(Root);
    }

    private void InOrder(SCGNode Rt)
    {
        if(Rt!=null)
        {
            InOrder(Rt.Left);
            System.out.println(Rt.Value);
            InOrder(Rt.Right);
        }
    }

    void Delete(int Key)
    {
        Root=Delete(Root,Key);
    }

    private SCGNode Delete(SCGNode Rt, int Key)
    {
        if(Rt==null)
        {
            return Rt;
        }
        if(Key<Rt.Value)
        {
            Rt.Left=Delete(Rt.Left,Key);
        }
        else if(Key>Rt.Value)
        {
            Rt.Right=Delete(Rt.Right,Key);
        }
        else
        {
            if(Rt.Left==null)
            {
                return Rt.Right;
            }
            else if(Rt.Right==null)
            {
                return Rt.Left;
            }
            Rt.Value=MinVal(Rt.Right);
            Rt.Right=Delete(Rt.Right,Rt.Value);
        }
        return Rt;
    }

    int MinVal(SCGNode Rt)
    {
        int Minimum=Rt.Value;
        while(Rt.Left!=null)
        {
            Minimum=Rt.Left.Value;
            Rt=Rt.Left;
        }
        return Minimum;
    }
}

class Node {
	int key;
    Node right;
    Node left;
    Node parent;

    public Node(int value){
        key = value;
        right = null;
        left = null;
        parent = null;
    }
       public void delete(int delKey){
    	Node root = null;
		Node node = root;
    	Node parent = null;
    	Node delete = null;
    	boolean isLeft = true;
    	Node successor = null;
    	int depth = 0;

    	if (node == null) {
            return;
        }

    	while (node.key != delKey){
            parent = node;
            if (delKey > node.key){
                node = node.right;
                isLeft = false;
            }
            else {
                node = node.left;
                isLeft = true;
            }    
            depth++;
    	}

    	// case 1: Node to be delete has no children
        if (node.left == null && node.right == null){
            //node = null;
        }
        // case 2: Node has only a right child
        else if (node.left == null){
            successor = node.right;
        }
        // case 3: Node has only a left child
        else if (node.right == null){
            successor = node.left;
        }
        else {
        	// find successor
            successor = minimumKey(node.right);
            // the successor is the node's right child 
            if (successor == node.right){
                successor.left = node.left;
            }
            // complicated case
            else {
                successor.left = node.left;
                delete = successor.right;
                successor.right = node.right;
                node.right.left = delete;
            }
        }
        // Replace the node
        if (parent == null)
            root = successor;
        else if (isLeft){
        	node.parent.left = successor;
            parent.left = successor;
        }
        else {
            parent.right = successor;
            node.parent.right = successor;
        }




    }

	private Node minimumKey(Node right)
	{
		// TODO: Implement this method
		return null;
	}

}





class ScapeGoatMainFile {

    /**
     * @param args the command line arguments
     */
    public  void main(String[] args) {
        // TODO code application logic here

        ScapeGoatTree SGT= new ScapeGoatTree();
        SGT.Insert(7);
        SGT.Insert(6);
        SGT.Insert(3);
        SGT.Insert(1);
        SGT.Insert(0);
        SGT.Insert(8);
        SGT.Insert(9);
        SGT.Insert(4);
        SGT.Insert(5);
        SGT.Insert(2);
        SGT.Insert(3);
        SGT.PreOrder();
    }

}
