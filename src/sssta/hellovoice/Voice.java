package sssta.hellovoice;

public class Voice {
	
	String name;//����������
	int time;//��������
	String path="/mnt/sdcard/seemore/"+name+".mp3";//ֱ��ʹ��name��Ϊ����·��

	//���췽��������һ��string��һ��intֵ���ֱ�ֵ��name��time
	public Voice(String a,int b)
	{
		name=a;
		time=b;
	}
}
