package aes;
import aes.Helpers;
import aes.Keys;
public class Decryptor 
{
	String Ciphertext;
	Helpers helper;
	String[][] state;
	Keys keyGetter;
	
	public Decryptor(String input,String key)
	{
		keyGetter = new Keys(key);
		helper = new Helpers();
		Ciphertext = input;
	}
	
	public String performDecryption()
	{
		addRoundKey(10);
		for(int i=9;i>0;i--)
		{
			invShiftRows();
			invSubBytes();
			System.out.println("Decrypted Text after Round: "+(10-i)+" is: "+helper.stringToHex(Ciphertext));
			addRoundKey(i);
			invMixColumns();
		}
		invShiftRows();
		invSubBytes();
		addRoundKey(0);
		System.out.println("Decrypted Text after Round: "+10+" is: "+helper.stringToHex(Ciphertext));
		return Ciphertext;
	}
	
	public void addRoundKey(int round)
	{
		StringBuilder sb = new StringBuilder();
		String key = keyGetter.getRoundKey(round);
		for(int j = 0; j < 128; j++)
		    sb.append(key.charAt(j) ^ Ciphertext.charAt(j));
		Ciphertext = sb.toString();
		getMatrix();
	}
	
	public void printState()
	{
		for(int i=0;i<4;i++)
		{
			for(int j=0;j<4;j++)
				System.out.print(helper.stringToHex(state[i][j])+" ");
			System.out.println();
		}
	}
	
	public void getMatrix()
	{
		String[][] mat = new String[4][4];
		for(int j=0;j<4;j++)
			for(int i=0;i<4;i++)
				mat[i][j] = Ciphertext.substring(j*32+i*8,j*32+(i+1)*8);
		state = mat;
	}
	
	public void invSubBytes()
	{
		String result = "";
		for(int i=0;i<16;i++)
			result = result + helper.getInvSVal(Ciphertext.substring(i*8, (i+1)*8));
		Ciphertext = result;
		getMatrix();
	}

	public void invShiftRows()
	{
		String temp;
		for(int i=0;i<4;i++)
		{
			for(int j=0;j<i;j++)
			{
				temp = state[i][3];
				for(int k=3;k>0;k--)
					state[i][k] = state[i][k-1];
				state[i][0] = temp;
			}
		}
		getPlain();
	}
	
	public void getPlain()
	{
		String result = "";
		for(int i=0;i<4;i++)
			for(int j=0;j<4;j++)
				result = result + state[j][i];
		Ciphertext = result;
	}

	public void invMixColumns()
	{
		int[][] mix = helper.getBackwardMix();
		String[][] temp = new String[4][4];
		for(int i=0;i<4;i++)
			for(int j=0;j<4;j++)
			{
				temp[i][j]="00000000";
				for(int k=0;k<4;k++)
					temp[i][j] = helper.doXor(temp[i][j], helper.galoisMul(mix[i][k],state[k][j]));
			}
		state = temp;
		getPlain();
	}
		
}
