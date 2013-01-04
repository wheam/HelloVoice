package sssta.hellovoice;

import java.util.ArrayList;
import java.util.List;



import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class MainActivity extends Activity {

	Button bn_add;
	EditText dialog_name;
	EditText dialog_time;
	
	//注意这三个东西的长度保持一致
	List<Voice> listVoice;
	List<String> listString;
	ListView listView;
	
	//listview的适配器
	ArrayAdapter<String> adapter;
	
	SharedPreferences sharedPreferences;//定义sharedprefence
	SharedPreferences.Editor editor;//定义editor，用于提交数据到sharedprefence，具体参看文档

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //添加按钮
        bn_add=(Button)findViewById(R.id.bn_add);
        //获取listview
        listView=(ListView)findViewById(R.id.list);
        
        //使用getSharedPreferences获取存储的prefences，一样的使用key-value方式存储，使用key查找，之后是一个mode，MODE_PRIVATE表示此prefences只能此程序使用
        sharedPreferences=getSharedPreferences("HelloVoice",MODE_PRIVATE);
  
        //实例listVoice和listString
        listVoice=new ArrayList<Voice>();
        listString=new ArrayList<String>();
        
        //首先获取上次退出时listView的长度
        int size=sharedPreferences.getInt("size", 0);
        
        //循环上次退出时listview长度次
        for(int i=0;i<size;i++)
        {
        	//获取上次存储的listVoice中每一项的name和time
        	String temp_name=sharedPreferences.getString("name"+i, null);
        	int temp_time=sharedPreferences.getInt("time"+i, 0);
        	//使用构造方法直接传入两个参数，生成新的Voice
        	listVoice.add(new Voice(temp_name,temp_time));
        	//listView显示声音描述和声音时长
        	listString.add(temp_name+"  "+temp_time+"s");
        }
        
        //new ArrayAdapter<String>(context, textViewResourceId, objects)中的三个参数
        //context 官方文档解释是The current context. 一直想不通，看到大家用this就跟着用了，求大神解释
        //textViewResourceId是指ListView中每一个Item的布局，我代码里使用的是默认的单行文字布局，没有自定义，可以自定义
        //最后的objects就是你的数据，一个String数组或者其他数据容器，大部分示例代码中会使用一个自定义getData（）方法来返回一个设定好的ArrayList<String>，我这里直接使用的自定义好的
        adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,listString);
        
        //设置适配器
        listView.setAdapter(adapter);
        
        //绑定添加按钮事件
        bn_add.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//看此方法定义
				AddDialog();
			}
		});
        
        //设定长按listiew每一个item的事件
        listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				//看此方法定义，传入arg2参数即告知被长按的item的序号，也是对应在listString，listVoice中的序号
				LongDialog(arg2);
				return false;
			}
		});
      
    }

    protected void onStop()
    {
    	//在程序退出时保存，看此方法定义
    	CommitTo();
    	super.onStop();
    }
    
    private void CommitTo()
    {
    	//获取editor
		editor=sharedPreferences.edit();
		//先将listVoice即listview，也是listString的长度存入
		editor.putInt("size", listVoice.size());
		
		//循环长度次数
		for(int i=0;i<listVoice.size();i++)
		{
			//直接分别存入listVoice每一项的name和time
			editor.putString("name"+i, listVoice.get(i).name);
			editor.putInt("time"+i, listVoice.get(i).time);
		}
		//提交，不使用此方法不会存储
		editor.commit();
    }
    private void VoiceAdd()
    {
    	 //获取dialog中两个edittext的内容
         String name=dialog_name.getText().toString();
         String timeString=dialog_time.getText().toString();
    	
         //判断语句是防止edittext是空的时候，添加了空白的东西
		if(name!=null&&timeString!=null&&name.length()!=0&&timeString.length()!=0)
		{
			//转换用户输入的string变成int值
			int time=Integer.parseInt(timeString);
			//再次使用构造器生成Voice，并且添加到listVoice
			listVoice.add(new Voice(name,time));
			//一样的将声音描述和长度添加到listString
			listString.add(name+"  "+time+"s");
			//更新listview
			adapter.notifyDataSetChanged();
		}
		
    }
   
    private void AddDialog()
    {
    	//layoutinflater用来查找资源中的xml资源并且示例化
    	LayoutInflater inflater = getLayoutInflater();
    	//获取自定义dialog的layout
    	View layout=inflater.inflate(R.layout.dialog, (ViewGroup)findViewById(R.id.dialog));
    	//注意此处获取dialog的布局xml中的控件的时候，必须使用layout.，因为findviewbyid是查找父view中的id，而此edittext的父view是dialog不是activity
    	dialog_name=(EditText)layout.findViewById(R.id.dialog_name);
        dialog_time=(EditText)layout.findViewById(R.id.dialog_time);
        
        //新建一个对话框
    	new AlertDialog.Builder(this)
    	//设置dialog标题
    	.setTitle("添加新的声音")
    	//设置dialog的自定义样式
    	.setView(layout)
    	//设置确定按键
    	.setPositiveButton("确定",  new DialogInterface.OnClickListener(){  
                public void onClick(  
                    DialogInterface dialoginterface, int i){
                	//这里写确定键按下的处理  
                	VoiceAdd();
                }  
            })
        //设置取消按键，这里不需要填入任何内容
        .setNegativeButton("取消",  
                new DialogInterface.OnClickListener(){  
                public void onClick(  
                    DialogInterface dialoginterface, int i){
                	//这里写取消键按下的处理                          
                	
                }  
            })  
        //这个方法必须要，显示dialog
    	.show();
    }
    private void LongDialog( final int arg2){
    	
    	//新建一个对话框
        new AlertDialog.Builder(MainActivity.this)  
        	//设置dialog标题
        	.setTitle("温馨提示")  
        	//设置提醒消息
            .setMessage("是否删除这个声音")  
            //设置确定按键
            .setPositiveButton("确定",  
                new DialogInterface.OnClickListener(){  
                public void onClick(  
                    DialogInterface dialoginterface, int i){
                	//这里写确定键按下的处理  
                	listVoice.remove(arg2);
                	listString.remove(arg2);
                	//CommitTo();
                	adapter.notifyDataSetChanged();
                }  
            })
            //设置取消按键，这里不需要填入任何内容
            .setNegativeButton("取消",  
                new DialogInterface.OnClickListener(){  
                public void onClick(  
                    DialogInterface dialoginterface, int i){
                	//这里写取消键按下的处理                          
                	
                }  
            })  
            //这个方法必须要，显示dialog
            .show();  
    }  
    
    
}


