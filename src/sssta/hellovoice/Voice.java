package sssta.hellovoice;

public class Voice {
	
	String name;//声音的描述
	int time;//声音长度
	String path="/mnt/sdcard/seemore/"+name+".mp3";//直接使用name作为声音路径

	//构造方法，传入一个string，一个int值，分别赋值给name和time
	public Voice(String a,int b)
	{
		name=a;
		time=b;
	}
}
