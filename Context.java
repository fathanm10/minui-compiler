/** 
 * @class: Context
 * This class constructs Context object that has attributes : 
 * 1. lexicalLevel    : current lexical level
 * 2. orderNumber     : current order number
 * 3. symbolHash      : hash table of symbols
 * 4. symbolStack     : stack to keep symbol's name
 * 4. typeStack       : stack to keep symbol's type
 * 4. printSymbols    : choice of printing symbols
 * 4. errorCount      : error counter of context checking
 *
 * @author: DAJI Group (Dalton E. Pelawi & Jimmy)
 */

import java.util.Stack;

class Context
{
    public Context()
    {
        lexicalLevel = -1;
        orderNumber = 0;
        symbolHash = new Hash(HASH_SIZE);
        symbolStack = new Stack();
        typeStack = new Stack();
        orderNumberStack = new Stack<Integer>();
        printSymbols = false;
        errorCount = 0;
        currentStr = "";
    }

    /**
     * This method chooses which action to be taken
     * @input : ruleNo(type:int)
     * @output: -(type:void)
     */
    public void C(int ruleNo)
    {
        boolean error = false;
        Bucket currentNode;
        int paramNum;

        switch(ruleNo)
        {
            case 0:
                lexicalLevel++;
                orderNumber = 0;
                break;
            case 1:
                if (printSymbols)
                    symbolHash.print(lexicalLevel);
                break;
            case 2:
                symbolHash.delete(lexicalLevel);
                lexicalLevel--;
                break;
            case 3:
                if (symbolHash.isExist(currentStr, lexicalLevel))
                {
                    System.out.println("Variable declared at line " + currentLine + ": " + currentStr);
                    errorCount++;
                    System.err.println("\nProcess terminated.\nAt least " + (errorCount + parser.yylex.num_error)
                                       + " error(s) detected.");
                    System.exit(1);
                }
                else
                {
                    symbolHash.insert(new Bucket(currentStr));
                }
                symbolStack.push(currentStr);
                break;
            case 4:
                symbolHash.find(currentStr).setLLON(lexicalLevel, orderNumber);
                break;
            case 5:
                symbolHash.find(currentStr).setIdType(((Integer)typeStack.peek()).intValue());
                break;
            case 6:
                if (!symbolHash.isExist(currentStr))
                {
                    System.out.println("Variable undeclared at line " + currentLine + ": " + currentStr);
                    errorCount++;
                    System.err.println("\nProcess terminated.\nAt least " + (errorCount + parser.yylex.num_error)
                                       + " error(s) detected.");
                    System.exit(1);
                }
                else
                {
                    symbolStack.push(currentStr);
                }
                break;
            case 7:
                symbolStack.pop();
                break;
            case 8:
                typeStack.push(new Integer(symbolHash.find(currentStr).getIdType()));
                break;
            case 9:
                typeStack.push(new Integer(Bucket.INTEGER));
                break;
            case 10:
                typeStack.push(new Integer(Bucket.BOOLEAN));
                break;
            case 11:
                typeStack.pop();
                break;
            case 12:
                switch (((Integer)typeStack.peek()).intValue())
                {
                    case Bucket.INTEGER:
                        break;
                    case Bucket.UNDEFINED:
                        System.out.println("Undefined type at line " + currentLine + ": " + currentStr);
                        errorCount++;
                        break;
                    default:
                        System.out.println("Type of integer expected at line " + currentLine + ": " + currentStr);
                        errorCount++;
                        break;
                }
                break;
            case 13:
                switch (((Integer)typeStack.peek()).intValue())
                {
                    case Bucket.BOOLEAN:
                        break;
                    case Bucket.UNDEFINED:
                        System.out.println("Undefined type at line " + currentLine + ": " + currentStr);
                        errorCount++;
                        break;
                    default:
                        System.out.println("Type of boolean expected at line " + currentLine + ": " + currentStr);
                        errorCount++;
                        break;
                }
                break;
            case 14:
                temp = ((Integer)typeStack.pop()).intValue();
                if (temp != ((Integer)typeStack.peek()).intValue())
                {
                    System.out.println("Unmatched type at line " + currentLine + ": " + currentStr + " C14");
                    errorCount++;
                }
                typeStack.push(new Integer(temp));
                break;
            case 15:
                temp = ((Integer)typeStack.pop()).intValue();
                if ((temp != Bucket.INTEGER) && ((Integer)typeStack.peek()).intValue() != Bucket.INTEGER)
                {
                    System.out.println("Unmatched type at line " + currentLine + ": " + currentStr + " C15");
                    errorCount++;
                }
                typeStack.push(new Integer(temp));
                break;
            case 16:
                temp = symbolHash.find((String)symbolStack.peek()).getIdType();
                if (temp != ((Integer)typeStack.peek()).intValue())
                {
                    System.out.println("Unmatched type at line " + currentLine + ": " + currentStr + " C16. Expected" + ((Integer)typeStack.peek()).intValue() + ", but found " + temp);
                    errorCount++;
                }
                break;
            case 17:
                temp = symbolHash.find((String)symbolStack.peek()).getIdType();
                if (temp != Bucket.INTEGER)
                {
                    System.out.println("Type of integer expected at line " + currentLine + ": " + currentStr);
                    errorCount++;
                }
                break;
            case 18:
                symbolHash.find(currentStr).setIdKind(Bucket.SCALAR);
                orderNumber++;
                break;
            case 19:
                symbolHash.find(currentStr).setIdKind(Bucket.ARRAY);
                orderNumber += 3;
                break;
            case 20:
                switch (symbolHash.find((String)symbolStack.peek()).getIdKind())
                {
                    case Bucket.SCALAR:
                        break;
                    case Bucket.UNDEFINED:
                        System.out.println("Variable not fully defined at line " + currentLine + ": " + currentStr);
                        errorCount++;
                        break;
                    case Bucket.ARRAY:
                        System.out.println("Scalar variable expected at line " + currentLine + ": " + currentStr);
                        errorCount++;
                        break;
                }
                break;
            case 21:
                switch (symbolHash.find((String)symbolStack.peek()).getIdKind())
                {
                    case Bucket.ARRAY:
                        break;
                    case Bucket.UNDEFINED:
                        System.out.println("Variable not fully defined at line " + currentLine + ": " + currentStr);
                        errorCount++;
                        break;
                    case Bucket.SCALAR:
                        System.out.println("Array variable expected at line " + currentLine + ": " + currentStr);
                        errorCount++;
                        break;
                }
                break;
            case 22:
                symbolHash.find(currentStr).setLLON(lexicalLevel, orderNumber);
                break;
            case 23:
                symbolHash.find(currentStr).setIdType(((Integer) typeStack.peek()).intValue());
                break;
            case 24:
                symbolHash.find(currentStr).setIdKind(Bucket.PROCEDURE);
                orderNumberStack.push(orderNumber);
                orderNumber++;
                break;
            case 25:
                // Simbol-simbol parameter berada setelah simbol fungsi yang bersangkutan di stack, satu ll di atas fungsi tersebut
                symbolHash.find(currentStr).setIdKind(Bucket.PARAMETER);
                orderNumber++;
                
                symbolStack.pop();  // current parameter
                temp = ((Integer) symbolStack.pop()).intValue();  // paramNum
                currentNode = symbolHash.find((String) symbolStack.peek());
                while (currentNode.getNextBucket() != null) {
                    currentNode=currentNode.getNextBucket();
                }
                
                currentNode.setNextBucket(symbolHash.find(currentStr));
                symbolStack.push(temp);
                symbolStack.push(currentStr);
                break;
            case 26:
                symbolHash.find(currentStr).setIdKind(Bucket.FUNCTION);
                orderNumberStack.push(orderNumber);
                orderNumber++;
                break;
            case 27:
                if (!orderNumberStack.empty()) {
                    orderNumber = orderNumberStack.pop();
                }
                symbolHash.delete(lexicalLevel);
                lexicalLevel--;
                break;
            case 28:
                if (symbolHash.find((String) symbolStack.peek()).getIdKind() == Bucket.UNDEFINED) {
                    System.out.println("Variable not fully defined at line " + currentLine + ": " + currentStr);
                    errorCount++;
                } else if (symbolHash.find((String) symbolStack.peek()).getIdKind() != Bucket.PROCEDURE) {
                    System.out.println("Procedure expected at line " + currentLine + ": " + currentStr);
                    errorCount++;
                }
                break;
            case 29:
                if (symbolHash.find((String) symbolStack.peek()).getParamNum() != 0) {
                    System.out.println("Function/Procedure call at line " + currentLine + " does not take 0 argument");
                    errorCount++;
                }
                break;
            case 30:
                symbolStack.push(0);
                break;
            case 31:
                temp = ((Integer) symbolStack.pop()).intValue();
                currentNode = symbolHash.find((String) symbolStack.peek());
                for (int i=0; i<temp; i++) {
                    currentNode = currentNode.getNextBucket();
                    if (currentNode == null) {
                        System.out.println("Number of arguments exceeds the number of required parameters on line " + currentLine + ": " + symbolHash.find((String) symbolStack.peek()).getIdName());
                        errorCount++;
                        break;
                    }
                }
                if (currentNode != null) {
                    if (currentNode.getIdType() != ((Integer) typeStack.peek()).intValue()) {
                        System.out.println("Argument type mismatch on line " + currentLine + ": " + currentStr);
                        errorCount++;
                    }
                }
                symbolStack.push(temp);
                break;
            case 32:
                temp = ((Integer) symbolStack.pop()).intValue();
                currentNode = symbolHash.find((String) symbolStack.peek());
                if (currentNode.getIdKind() != Bucket.FUNCTION && currentNode.getIdKind() != Bucket.PROCEDURE) {
                    symbolStack.push(temp);
                    break;
                }
                if (currentNode.getParamNum() != temp) {
                    System.out.println("Number of parameter required is " + currentNode.getParamNum());
                    errorCount++;
                }
                break;
            case 33:
                if ((symbolHash.find((String) symbolStack.peek()).getIdKind() == Bucket.UNDEFINED)) {
                    System.out.println("Variable not fully defined at line " + currentLine + ": " + currentStr);
                    errorCount++;
                    break;
                } else if ((symbolHash.find((String) symbolStack.peek()).getIdKind() != Bucket.FUNCTION)) {
                    System.out.println("Function expected at line " + currentLine + ": " + currentStr);
                    errorCount++;
                    break;
                }
                break;
            case 34:
                paramNum = ((Integer) symbolStack.pop()).intValue();
                symbolStack.push(paramNum+1);
                break;
            case 35:
                paramNum = ((Integer) symbolStack.pop()).intValue();
                currentNode = symbolHash.find((String) symbolStack.peek());
                currentNode.setParamNum(paramNum);
                temp = currentNode.getIdKind() == Bucket.FUNCTION ? 3 : 2;
                while((currentNode = currentNode.getNextBucket()) != null) {
                    if (currentNode.getIdKind() == Bucket.PARAMETER) {
                        currentNode.setOrderNum(currentNode.getOrderNum() - paramNum - temp);
                    }
                }
                break;
            case 36:
                temp = ((Integer) typeStack.pop()).intValue();
                if (temp != ((Integer) typeStack.peek()).intValue()) {
                    System.out.println("Unmatched type at line " + currentLine + ": " + currentStr + " C36");
                    errorCount++;
                }
                typeStack.push(temp);
                break;
            case 37:
                if (symbolHash.find((String) symbolStack.peek()).getIdKind() == Bucket.FUNCTION)
                    C(33);
                else
                    C(20);
                break;
            case 38:
                typeStack.push(new Integer(symbolHash.find((String) symbolStack.peek()).getIdType()));
                break;
        }
    }

    /**
     * This method sets the current token and line
     * @input : str(type:int), line(type:int)
     * @output: -(type:void)
     */
    public void setCurrent(String str, int line)
    {
        currentStr = str;
        currentLine = line;
    }

    /**
     * This method sets symbol printing option
     * @input : bool(type:boolean)
     * @output: -(type:void)
     */
    public void setPrint(boolean bool)
    {
        printSymbols = bool;
    }

    private final int HASH_SIZE = 211;

    public static int lexicalLevel;
    public static int orderNumber;
    public static Hash symbolHash;
    private Stack symbolStack;
    private Stack typeStack;
    public static Stack<Integer> orderNumberStack;
    public static String currentStr;
    public static int currentLine;
    private boolean printSymbols;
    public int errorCount;
    private int temp;
}