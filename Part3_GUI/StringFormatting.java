public class StringFormatting {
    public static String pads[] = new String[30];
    
    public static void main(String[] args) {
        initPads();
        System.out.println(pad("hello world this is ori", 10));
    }
    
    public static void initPads() {
        pads[0] = "";
        for (int i = 1; i < pads.length; i++) {
            pads[i] = pads[i-1] + " ";
        }
    }
    
    public static String pad(String str, int numSpaces) {
        // split the string by spaces:
        String[] tokens = str.split(" ");
        
        // create the StringBuffer:
        StringBuffer sb = new StringBuffer();
        
        // add the tokens:
        for (String t : tokens) {
            sb.append(t);
            if (t.length() > numSpaces) continue; // to fix possible index error
            sb.append(pads[numSpaces - t.length()]);
        }
        
        return sb.toString();
    }
}
