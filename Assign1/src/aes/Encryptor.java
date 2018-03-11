package aes;
import aes.Helpers;
import aes.Keys;
public class Encryptor 
{
	String plaintext;
	Helpers helper;
	String[][] state;
	Keys keyGetter;
	
	public Encryptor(String input,String key)
	{
		keyGetter = new Keys(key);
		helper = new Helpers();
		plaintext = input;
	}
	
	public String performEncryption()
	{
		addRoundKey(0);
		for(int i=1;i<=9;i++)
		{
			subBytes();
			shiftRows();
			mixColumns();
			addRoundKey(i);
			System.out.println("CipherText after round: "+i+" is: "+helper.stringToHex(plaintext));
		}
		subBytes();
		shiftRows();
		addRoundKey(10);
		System.out.println("CipherText after round: "+10+" is: "+helper.stringToHex(plaintext));
		return plaintext;
	}
	
	public void addRoundKey(int round)
	{
		StringBuilder sb = new StringBuilder();
		String key = keyGetter.getRoundKey(round);
		for(int j = 0; j < 128; j++)
		    sb.append(key.charAt(j) ^ plaintext.charAt(j));
		plaintext = sb.toString();
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
				mat[i][j] = plaintext.substring(j*32+i*8,j*32+(i+1)*8);
		state = mat;
	}
	
	public void subBytes()
	{
		String result = "";
		for(int i=0;i<16;i++)
			result = result + helper.getSVal(plaintext.substring(i*8, (i+1)*8));
		plaintext = result;
		getMatrix();
	}

	public void shiftRows()
	{
		String temp;
		for(int i=0;i<4;i++)
		{
			for(int j=0;j<i;j++)
			{
				temp = state[i][0];
				for(int k=0;k<3;k++)
					state[i][k] = state[i][k+1];
				state[i][3] = temp;
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
		plaintext = result;
	}

	public void mixColumns()
	{
		int[][] mix = helper.getForwardMix();
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
