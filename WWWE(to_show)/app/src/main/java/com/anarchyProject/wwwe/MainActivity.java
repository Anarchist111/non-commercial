
/**
*	Copyright © 2019-present Maister Bohdan. All rights reserved.
*/

package com.anarchyProject.wwwe;

import android.app.*;
import android.os.*;
import android.widget.*;
import android.view.View.*;
import android.view.*;
import java.io.*;
import android.content.*;
import java.util.*;

public class MainActivity extends Activity 
{
	Dish dish = new Dish(MainActivity.this);
	
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		
		final TextView mainSign = (TextView) findViewById(R.id.main_sign);
			

		mainSign.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					//Toast.makeText( getApplicationContext() , "Wow, it is works", Toast.LENGTH_SHORT).show();
					//mainSign.setText(dish.addNewDish((Context) this));
//					try {
//						//dish.addNewDish(new String()); /// need to turn the arg
//						dish.showAllTheDishList();
//					}
//					catch (IOException e){
//						mainSign.setTextColor(0xFF_FF_00_00);
//						mainSign.setText("Fail: IO Exception!(File) more: " + e.getMessage() + "    Property_home: " + System.getProperty("user.home") + "Property_dir: " + System.getProperty("user.dir") + "    Separator: " + System.getProperty("line.separator"));
//					}
					mainSign.setText(dish.returnRandomDish());
				}
			
		});
		
		
    }
	
	
	public void onRadioButtonClick(View rButton){
		switch(rButton.getId()){
			case R.id.radio_all:
				dish.section = 0;
				//Toast.makeText(this, "all is checked", Toast.LENGTH_SHORT).show();
				break;
			case R.id.radio_soups:
				dish.section = 1;
				//Toast.makeText(this, "soups is checked", Toast.LENGTH_SHORT).show();
				break;
			case R.id.radio_porriges:
				dish.section = 2;
				//Toast.makeText(this, "porriges is checked", Toast.LENGTH_SHORT).show();
				break;
			case R.id.radio_meats:
				dish.section = 3;
				//Toast.makeText(this, "others is checked", Toast.LENGTH_SHORT).show();
				break;

		}
	}
	
	
}

class Dish
{
	private Context context;
	static File myDir = new File(Environment.getExternalStorageDirectory() + "/WWWE/");
	//static File myDir = new File("storage/emulated/0/WWWE");
	String[] dishList;
	int[] sections;
	public int section = 0;


	Dish(Context context) {
		this.context = context;
		fillDishList();
	}


	String returnRandomDish(){ // here
		Random random = new Random();

		if(dishList.length != 0){
			switch(section){
				case 0:
					return dishList[random.nextInt(dishList.length)];
				default:
					return dishList[sections[section - 1] + (random.nextInt(sections[section] - sections[section - 1]))];
				//default: return "There are not such section";
			}
			
		} else {
	 		return "There are not any dishes in the list!";
		}
	}


	StringBuilder showAllTheDishList(){
		StringBuilder buf = new StringBuilder(dishList.length * 16);

		for(int i = 0; i < dishList.length; i++){
			buf.append(dishList[i] );
			if (i != dishList.length -1){
				buf.append("; \n");
			}
		}

		return buf;
	}

	void fillDishList() {
		File myFile = new File(myDir + "/DishList.txt");

		try{
			if(!myDir.exists())
				myDir.mkdirs();

			if(!myFile.exists()) 
				myFile.createNewFile();

			if(!myFile.exists()) Toast.makeText(context, "File with dishes list is not found!", Toast.LENGTH_LONG).show();


			FileReader fr = new FileReader(myFile);


			/// ! To fix error surrounded with smal number of buffered chars - use class "LineNumberReader" without mark/reset
			BufferedReader br = new BufferedReader(fr);
			br.mark((int)myFile.length()); // marking the begin of file

			int arraySize = 0, sectionsCount = 0;
			
			while(br.ready()){
				if (br.readLine().charAt(0) == ';'){
					sectionsCount++;
				}
				arraySize++;
				
			}

			dishList = new String[arraySize - sectionsCount];
			sections = new int[sectionsCount + 1];

			br.reset(); // jump to mark

			int count = 0;
			for (int i = 0; i != arraySize && br.ready(); i++){
				dishList[i] = new String(br.readLine());
				if (dishList[i].charAt(0) == ';'){
					sections[count++] = i;
					i--;
				}
				
			}
			sections[count] = dishList.length;

			br.close();
			fr.close();	
		}
		catch (FileNotFoundException e){
			Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
		}
		catch(IOException e){
			Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
		}
	}


	void addNewDish(String newSign) throws IOException{
		String state = Environment.getExternalStorageState();

		if (Environment.MEDIA_MOUNTED.equals(state)) {
			File myFile = new File(myDir + "/DishList.txt");

			if(!myDir.exists())
				myDir.mkdirs();

			if(!myFile.exists()) 
				myFile.createNewFile();

			if(myFile.exists()) {
				FileWriter fwr = new FileWriter(myFile);
				fwr.append(newSign + "\n");
				fwr.flush();
				fwr.close();

			}

		}
		else{
			Toast.makeText(context, "fail: can't call to file directory", Toast.LENGTH_SHORT).show();
		}
	}

}
