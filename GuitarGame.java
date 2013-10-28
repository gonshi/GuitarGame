import java.awt.*;
import java.awt.event.*;
import java.applet.*;
import java.applet.Applet;
import java.awt.Image;
import java.applet.AudioClip;
import java.util.Random;
 
public class GuitarGame extends Applet implements MouseMotionListener{
	Image guitar;
        Random rnd = new Random();
        int Cord_index = rnd.nextInt(7);
	static AudioClip[] tone = new AudioClip[14];
	int [] x_array = new int[100];
	int [] y_array = new int[100];
	int flag=0,i,count,test = 0;
	Label anslb;
	Setting set;
 
	public void init(){
		guitar = getImage(getCodeBase(), "DSCN24812.jpg");
		for(i=0;i<14;i++){
			tone[i] = getAudioClip(getDocumentBase(),i+".mid");
		}
		setLayout(new FlowLayout());
		set = new Setting();
		add(set);
		setSize(640, 693);
		setVisible(true);
		addMouseMotionListener(this);
	}
 
        public void paint(Graphics g) {
		Dimension dim = getSize();
           	g.drawImage(guitar, 0, 80, this);
        }
 
 
	public void mouseMoved  (MouseEvent e) {
		setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		flag = 0;
	}
 
	public void mouseDragged(MouseEvent e) {   //    ドラッグ 
		if(test == 0){
			setCursor(new Cursor(Cursor.HAND_CURSOR));
			Point point = e.getPoint();
			if(flag == 0){
				count = 0;
				if(point.x < 456){
					x_array[count] = point.x;
					y_array[count] = point.y;
					flag = 1;		//ギターより右側ならflag 1
				}else if(point.x > 531){
					x_array[count] = point.x;
					y_array[count] = point.y;
					flag = 2;		//ギターより左側ならflag 2
				}
			}
			else if(flag == 1 && point.x > 531){
				int check = 1,j=0;
				while(count >= 0 && j < 6){	//x_array[0]は必ずギター左位置
					if(y_array[count] > 580) check = 0; //ギターの弦より下を通っていたら
					count -= 10;
					j++;	//countを50引くまで
				}
				if(count < 0) count = 0;
				if(check == 1 && x_array[count] < 456){
					tone[Cord_index].play();
					anslb.setText("　　　　　　   　　     　　　");
					test = 1;
					setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
				}
				count = 0;
				flag = 2;
			}
			else if(flag == 2 && point.x < 456){
				int check = 1,j=0;
				while(count >= 0 && j < 6){	//x_array[0]は必ずギター左位置
					if(y_array[count] > 580) check = 0; //ギターの弦より下を通っていたら
					count -= 10;
					j++;	//countを50引くまで
				}
				if(count < 0) count = 0;
				if(check == 1 && x_array[count] > 531){
					tone[Cord_index+7].play();	//アップストローク
					anslb.setText("　　　　　　   　　     　　　");
					test = 1;
					setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
				}
				count = 0;
				flag = 1;
			}
			else{
				count++;
				x_array[count] = point.x;
				y_array[count] = point.y;
			}
			if(count==99) count = 0;
		}
 
	}
 
	class Setting extends Panel implements ActionListener{
		Label lb;
		List cord_lis = new List();
		int Cord_c_index = 0;
		String [] Cord_string = {"C","Dm7","E7","Fmaj7","G","Asus4","Badd9"}; 
		Button sound = new Button("このコードを聞いてみる");
		Button ans = new Button("回答");
		Setting(){
			setLayout(new FlowLayout());
			lb = new Label("コードを選んでね...");
			anslb = new Label("　　　　　　   　　     　　　");
			for (i=0; i<7; i++) {
				cord_lis.add(Cord_string[i]); 
			}
			add(lb);
			add(cord_lis);
			add(sound);
			sound.addActionListener(this);
			add(ans);
			ans.addActionListener(this);
			add(anslb);
		}
 
		public void actionPerformed(ActionEvent e){
			if(e.getSource() == sound) tone[cord_lis.getSelectedIndex()].play();
			else if(e.getSource() == ans){
				if(test == 0) anslb.setText("まずは弾いてみよう。");
				else if(cord_lis.getSelectedIndex() == Cord_index){
					anslb.setText("正解！");
					Cord_index = rnd.nextInt(7);
					test = 0;
				}else{
					anslb.setText("不正解！正解は "+Cord_string[Cord_index]+" でした！");
					Cord_index = rnd.nextInt(7);
					test = 0;
				}
			}
		}
	}
}
 
class quit extends WindowAdapter {
	public void windowClosing(WindowEvent e){
		System.exit(0);
	}
}