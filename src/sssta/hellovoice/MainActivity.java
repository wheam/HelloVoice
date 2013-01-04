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
	
	//ע�������������ĳ��ȱ���һ��
	List<Voice> listVoice;
	List<String> listString;
	ListView listView;
	
	//listview��������
	ArrayAdapter<String> adapter;
	
	SharedPreferences sharedPreferences;//����sharedprefence
	SharedPreferences.Editor editor;//����editor�������ύ���ݵ�sharedprefence������ο��ĵ�

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //��Ӱ�ť
        bn_add=(Button)findViewById(R.id.bn_add);
        //��ȡlistview
        listView=(ListView)findViewById(R.id.list);
        
        //ʹ��getSharedPreferences��ȡ�洢��prefences��һ����ʹ��key-value��ʽ�洢��ʹ��key���ң�֮����һ��mode��MODE_PRIVATE��ʾ��prefencesֻ�ܴ˳���ʹ��
        sharedPreferences=getSharedPreferences("HelloVoice",MODE_PRIVATE);
  
        //ʵ��listVoice��listString
        listVoice=new ArrayList<Voice>();
        listString=new ArrayList<String>();
        
        //���Ȼ�ȡ�ϴ��˳�ʱlistView�ĳ���
        int size=sharedPreferences.getInt("size", 0);
        
        //ѭ���ϴ��˳�ʱlistview���ȴ�
        for(int i=0;i<size;i++)
        {
        	//��ȡ�ϴδ洢��listVoice��ÿһ���name��time
        	String temp_name=sharedPreferences.getString("name"+i, null);
        	int temp_time=sharedPreferences.getInt("time"+i, 0);
        	//ʹ�ù��췽��ֱ�Ӵ������������������µ�Voice
        	listVoice.add(new Voice(temp_name,temp_time));
        	//listView��ʾ��������������ʱ��
        	listString.add(temp_name+"  "+temp_time+"s");
        }
        
        //new ArrayAdapter<String>(context, textViewResourceId, objects)�е���������
        //context �ٷ��ĵ�������The current context. һֱ�벻ͨ�����������this�͸������ˣ���������
        //textViewResourceId��ָListView��ÿһ��Item�Ĳ��֣��Ҵ�����ʹ�õ���Ĭ�ϵĵ������ֲ��֣�û���Զ��壬�����Զ���
        //����objects����������ݣ�һ��String����������������������󲿷�ʾ�������л�ʹ��һ���Զ���getData��������������һ���趨�õ�ArrayList<String>��������ֱ��ʹ�õ��Զ���õ�
        adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,listString);
        
        //����������
        listView.setAdapter(adapter);
        
        //����Ӱ�ť�¼�
        bn_add.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//���˷�������
				AddDialog();
			}
		});
        
        //�趨����listiewÿһ��item���¼�
        listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				//���˷������壬����arg2��������֪��������item����ţ�Ҳ�Ƕ�Ӧ��listString��listVoice�е����
				LongDialog(arg2);
				return false;
			}
		});
      
    }

    protected void onStop()
    {
    	//�ڳ����˳�ʱ���棬���˷�������
    	CommitTo();
    	super.onStop();
    }
    
    private void CommitTo()
    {
    	//��ȡeditor
		editor=sharedPreferences.edit();
		//�Ƚ�listVoice��listview��Ҳ��listString�ĳ��ȴ���
		editor.putInt("size", listVoice.size());
		
		//ѭ�����ȴ���
		for(int i=0;i<listVoice.size();i++)
		{
			//ֱ�ӷֱ����listVoiceÿһ���name��time
			editor.putString("name"+i, listVoice.get(i).name);
			editor.putInt("time"+i, listVoice.get(i).time);
		}
		//�ύ����ʹ�ô˷�������洢
		editor.commit();
    }
    private void VoiceAdd()
    {
    	 //��ȡdialog������edittext������
         String name=dialog_name.getText().toString();
         String timeString=dialog_time.getText().toString();
    	
         //�ж�����Ƿ�ֹedittext�ǿյ�ʱ������˿հ׵Ķ���
		if(name!=null&&timeString!=null&&name.length()!=0&&timeString.length()!=0)
		{
			//ת���û������string���intֵ
			int time=Integer.parseInt(timeString);
			//�ٴ�ʹ�ù���������Voice��������ӵ�listVoice
			listVoice.add(new Voice(name,time));
			//һ���Ľ����������ͳ�����ӵ�listString
			listString.add(name+"  "+time+"s");
			//����listview
			adapter.notifyDataSetChanged();
		}
		
    }
   
    private void AddDialog()
    {
    	//layoutinflater����������Դ�е�xml��Դ����ʾ����
    	LayoutInflater inflater = getLayoutInflater();
    	//��ȡ�Զ���dialog��layout
    	View layout=inflater.inflate(R.layout.dialog, (ViewGroup)findViewById(R.id.dialog));
    	//ע��˴���ȡdialog�Ĳ���xml�еĿؼ���ʱ�򣬱���ʹ��layout.����Ϊfindviewbyid�ǲ��Ҹ�view�е�id������edittext�ĸ�view��dialog����activity
    	dialog_name=(EditText)layout.findViewById(R.id.dialog_name);
        dialog_time=(EditText)layout.findViewById(R.id.dialog_time);
        
        //�½�һ���Ի���
    	new AlertDialog.Builder(this)
    	//����dialog����
    	.setTitle("����µ�����")
    	//����dialog���Զ�����ʽ
    	.setView(layout)
    	//����ȷ������
    	.setPositiveButton("ȷ��",  new DialogInterface.OnClickListener(){  
                public void onClick(  
                    DialogInterface dialoginterface, int i){
                	//����дȷ�������µĴ���  
                	VoiceAdd();
                }  
            })
        //����ȡ�����������ﲻ��Ҫ�����κ�����
        .setNegativeButton("ȡ��",  
                new DialogInterface.OnClickListener(){  
                public void onClick(  
                    DialogInterface dialoginterface, int i){
                	//����дȡ�������µĴ���                          
                	
                }  
            })  
        //�����������Ҫ����ʾdialog
    	.show();
    }
    private void LongDialog( final int arg2){
    	
    	//�½�һ���Ի���
        new AlertDialog.Builder(MainActivity.this)  
        	//����dialog����
        	.setTitle("��ܰ��ʾ")  
        	//����������Ϣ
            .setMessage("�Ƿ�ɾ���������")  
            //����ȷ������
            .setPositiveButton("ȷ��",  
                new DialogInterface.OnClickListener(){  
                public void onClick(  
                    DialogInterface dialoginterface, int i){
                	//����дȷ�������µĴ���  
                	listVoice.remove(arg2);
                	listString.remove(arg2);
                	//CommitTo();
                	adapter.notifyDataSetChanged();
                }  
            })
            //����ȡ�����������ﲻ��Ҫ�����κ�����
            .setNegativeButton("ȡ��",  
                new DialogInterface.OnClickListener(){  
                public void onClick(  
                    DialogInterface dialoginterface, int i){
                	//����дȡ�������µĴ���                          
                	
                }  
            })  
            //�����������Ҫ����ʾdialog
            .show();  
    }  
    
    
}


