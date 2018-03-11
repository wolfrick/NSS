package aes;
import aes.Helpers;
public class Keys 
{
	String key;
	String[] words;
	Helpers helper;
	
	public Keys(String input)
	{
		words = new String[44];
		helper = new Helpers();
		key = input;
		keyExpansion();
	}
	
	public String RotWord(String word)
	{
		String temp1 = word.substring(0, 8);
		String temp2 = word.substring(8,32); 
		return temp2+temp1;
	}
	
	public String SubWord(String word)
	{
		String result="";
		for(int i=0;i<4;i++)
			result = result+helper.getSVal(word.substring(i*8, (i+1)*8));
		return result;
	}

	public String functionG(String word,int index)
	{
		String result = SubWord(RotWord(word));
		String temp1 = result.substring(0, 8);
		String temp2 = helper.getRcon(index);
		String temp3 = helper.doXor(temp1, temp2);
		return temp3+result.substring(8, 32);
	}
	
	public void keyExpansion()
	{
		String temp;
		for(int i=0;i<4;i++)
			words[i] = key.substring(i*32, (i+1)*32);
		for(int i=4;i<44;i++)
		{
			temp = words[i-1];
			if(i%4==0)
				temp = functionG(temp,i/4-1);
			words[i] = helper.doXor(temp, words[i-4]);
		}
	}
	
	public String getRoundKey(int round)
	{
		String result = "";
		for(int i=0;i<4;i++)
			result = result + words[i+round*4];
		//System.out.println("Key for round: "+round+" is:"+helper.stringToHex(result));
		return result;
	}
}
