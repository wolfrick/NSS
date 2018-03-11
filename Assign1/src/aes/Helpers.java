package aes;
public class Helpers 
{
	String[][] sbox;
	String[][] invsbox;
	String[] rcon;
	int[][] forwardMix,backwardMix;
	public Helpers()
	{
		sbox = new String[][]{
				{"63","7C","77","7B","F2","6B","6F","C5","30","01","67","2B","FE","D7","AB","76"},		
				{"CA","82","C9","7D","FA","59","47","F0","AD","D4","A2","AF","9C","A4","72","C0"},
				{"B7","FD","93","26","36","3F","F7","CC","34","A5","E5","F1","71","D8","31","15"},
				{"04","C7","23","C3","18","96","05","9A","07","12","80","E2","EB","27","B2","75"},
				{"09","83","2C","1A","1B","6E","5A","A0","52","3B","D6","B3","29","E3","2F","84"},
				{"53","D1","00","ED","20","FC","B1","5B","6A","CB","BE","39","4A","4C","58","CF"},
				{"D0","EF","AA","FB","43","4D","33","85","45","F9","02","7F","50","3C","9F","A8"},
				{"51","A3","40","8F","92","9D","38","F5","BC","B6","DA","21","10","FF","F3","D2"},
				{"CD","0C","13","EC","5F","97","44","17","C4","A7","7E","3D","64","5D","19","73"},
				{"60","81","4F","DC","22","2A","90","88","46","EE","B8","14","DE","5E","0B","DB"},
				{"E0","32","3A","0A","49","06","24","5C","C2","D3","AC","62","91","95","E4","79"},
				{"E7","C8","37","6D","8D","D5","4E","A9","6C","56","F4","EA","65","7A","AE","08"},
				{"BA","78","25","2E","1C","A6","B4","C6","E8","DD","74","1F","4B","BD","8B","8A"},
				{"70","3E","B5","66","48","03","F6","0E","61","35","57","B9","86","C1","1D","9E"},
				{"E1","F8","98","11","69","D9","8E","94","9B","1E","87","E9","CE","55","28","DF"},
				{"8C","A1","89","0D","BF","E6","42","68","41","99","2D","0F","B0","54","BB","16"}
		};
		invsbox = new String[][]{
				{"52","09","6A","D5","30","36","A5","38","BF","40","A3","9E","81","F3","D7","FB"},
				{"7C","E3","39","82","9B","2F","FF","87","34","8E","43","44","C4","DE","E9","CB"},
				{"54","7B","94","32","A6","C2","23","3D","EE","4C","95","0B","42","FA","C3","4E"},
				{"08","2E","A1","66","28","D9","24","B2","76","5B","A2","49","6D","8B","D1","25"},
				{"72","F8","F6","64","86","68","98","16","D4","A4","5C","CC","5D","65","B6","92"},
				{"6C","70","48","50","FD","ED","B9","DA","5E","15","46","57","A7","8D","9D","84"},
				{"90","D8","AB","00","8C","BC","D3","0A","F7","E4","58","05","B8","B3","45","06"},
				{"D0","2C","1E","8F","CA","3F","0F","02","C1","AF","BD","03","01","13","8A","6B"},
				{"3A","91","11","41","4F","67","DC","EA","97","F2","CF","CE","F0","B4","E6","73"},
				{"96","AC","74","22","E7","AD","35","85","E2","F9","37","E8","1C","75","DF","6E"},
				{"47","F1","1A","71","1D","29","C5","89","6F","B7","62","0E","AA","18","BE","1B"},
				{"FC","56","3E","4B","C6","D2","79","20","9A","DB","C0","FE","78","CD","5A","F4"},
				{"1F","DD","A8","33","88","07","C7","31","B1","12","10","59","27","80","EC","5F"},
				{"60","51","7F","A9","19","B5","4A","0D","2D","E5","7A","9F","93","C9","9C","EF"},
				{"A0","E0","3B","4D","AE","2A","F5","B0","C8","EB","BB","3C","83","53","99","61"},
				{"17","2B","04","7E","BA","77","D6","26","E1","69","14","63","55","21","0C","7D"}
		};
		forwardMix = new int[][]{
				{2,3,1,1},
				{1,2,3,1},
				{1,1,2,3},
				{3,1,1,2}
		};
		backwardMix = new int[][]{
				{14,11,13,9},
				{9,14,11,13},
				{13,9,14,11},
				{11,13,9,14}
		};
		rcon = new String[]{"00000001","00000010","00000100","00001000","00010000","00100000","01000000","10000000","00011011","00110110"};
	}
	
	public String getSVal(String input)
	{
		String index = input;
		int row = (index.charAt(3)-'0')+(index.charAt(2)-'0')*2+(index.charAt(1)-'0')*4+(index.charAt(0)-'0')*8;
		int col = (index.charAt(7)-'0')+(index.charAt(6)-'0')*2+(index.charAt(5)-'0')*4+(index.charAt(4)-'0')*8;
		String result = hexatoBits(sbox[row][col]);
		return result;
	}
	
	public String getInvSVal(String input)
	{
		String index = input;
		int row = (index.charAt(3)-'0')+(index.charAt(2)-'0')*2+(index.charAt(1)-'0')*4+(index.charAt(0)-'0')*8;
		int col = (index.charAt(7)-'0')+(index.charAt(6)-'0')*2+(index.charAt(5)-'0')*4+(index.charAt(4)-'0')*8;
		String result = hexatoBits(invsbox[row][col]);
		return result;
	}
	
	public String getRcon(int index)
	{
		return rcon[index];
	}
	
	public String chartobits(String input)
	{
		int len = input.length();
		String binary = "";
		for(int i=0;i<len;i++)
			binary = binary + charToBinary(input.charAt(i));
		return binary;
	}
	
	public String charToBinary(char character)
    {
		char []ret = {'0','0','0','0','0','0','0','0'};
		int v = (int)character & 0xFFFF;
		for (int idx = 0; v > 0; v >>= 1, idx++)
			if ((v & 1) == 1)
				ret[7-idx] = '1';
		return new String(ret);
    }	
	
	public String hexatoBits(String input)
	{
		String result="";
		int length = input.length();
		for(int i=0;i<length;i++)
			result = result + hexToBits(input.charAt(i));
		return result;
	}
	
	public static String hexToBits(char character)
    {
		switch(character)
		{
			case '0': return "0000";
			case '1': return "0001";
			case '2': return "0010";
			case '3': return "0011";
			case '4': return "0100";
			case '5': return "0101";
			case '6': return "0110";
			case '7': return "0111";
			case '8': return "1000";
			case '9': return "1001";
			case 'A':
			case 'a': return "1010";
			case 'B':
			case 'b': return "1011";
			case 'C':
			case 'c': return "1100";
			case 'D':
			case 'd': return "1101";
			case 'E':
			case 'e': return "1110";
			case 'F':
			case 'f': return "1111";
		}
		System.out.println("Invalid HexaDecimal Value Caught\nExiting...");
		System.exit(0);
		return "";
    }	
	
	public char bitToHex(String s)
	{
		switch(s)
		{
			case "0000":	return '0';
			case "0001":	return '1';
			case "0010":	return '2';
			case "0011":	return '3';
			case "0100":	return '4';
			case "0101":	return '5';
			case "0110":	return '6';
			case "0111":	return '7';
			case "1000":	return '8';
			case "1001":	return '9';
			case "1010":	return 'a';
			case "1011":	return 'b';
			case "1100":	return 'c';
			case "1101":	return 'd';
			case "1110":	return 'e';
			case "1111":	return 'f';
		}
		return '0';
	}
	
	public String stringToHex(String input)
	{
		String result = "";
		int length = input.length();
		for(int i=0;i<length/4;i++)
			result = result+bitToHex(input.substring(i*4, (i+1)*4));
		return result;
	}
	
	public int[][] getForwardMix()
	{
		return forwardMix;
	}
	
	public int[][] getBackwardMix()
	{
		return backwardMix;
	}
	
	public String doXor(String one,String two)
	{
		StringBuilder sb = new StringBuilder();
		int length = one.length();
		String result;
		for(int j = 0; j < length; j++)
		    sb.append(one.charAt(j) ^ two.charAt(j));
		result = sb.toString();
		return result;
	}
	
	public String galoisMul(int a, String b)
	{
		int y = stringToInt(b);
		int z = galoisMultiply(a, y);
		return charToBinary((char)z);
	}
	
	public int galoisMultiply(int a, int b) 
	{    
        int p = 0;    
        for (int n=0; n<8; n++) 
        {     
           p = ((b & 0x01) > 0) ? p^a : p;   
           boolean ho = ((a & 0x80) > 0);
           a = ((a<<1) & 0xFE);
           if (ho)
              a = a ^ 0x1b;
           b = ((b>>1) & 0x7F);
        }
        return p;
    }
	
	public int stringToInt(String input)
	{
		int a = 0;
		int temp = 128;
		for(int i=0;i<8;i++)
		{
			a = a+temp*(input.charAt(i)-'0');
			temp = temp/2;
		}
		return a;
	}
	
}
