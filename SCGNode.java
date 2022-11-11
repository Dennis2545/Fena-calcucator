class SCGNode {
    protected SCGNode Right, Left, Parent;
    protected int Value;

    SCGNode(int val)
    {
        Right=Left=Parent=null;
        Value=val;
    }
}

