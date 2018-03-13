package com.newx.dimens.customcomponent;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import javax.swing.JScrollPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.newx.dimens.common.ComFun;

public class ScrollSelector extends JScrollPane implements ActionListener{
	
	private List<JCheckBox> checkBoxList=new ArrayList<JCheckBox>();
	private List<JTextArea> txScalesList=new ArrayList<JTextArea>();
	private Map<String,Double> selectedMap=new HashMap<String,Double>();
	private JCheckBox allSelBox;
	private List<String> foldsName;
	private List<Double> scales;
	
	private onSelectListener changeSelListener;
	
	public ScrollSelector(List<String> foldsName,List<Double> scales){
		super();
		this.foldsName=foldsName;
		this.scales=scales;
		initComponents();		
	}
	
	public void initComponents(){
		JPanel scrollSubView = new JPanel();
		scrollSubView.setLayout(new GridLayout(foldsName.size()+1,2));
		Font f = new Font("仿宋",Font.PLAIN,13);
		
		JPanel scrollHeader=new JPanel();			
		scrollHeader.setLayout(new BorderLayout(10, 1));
		allSelBox=new JCheckBox("全选");
		allSelBox.addActionListener(this);
		JLabel scaleLabel=new JLabel("缩放率");
		scaleLabel.setBackground(new Color(0xffDBDBDB));
		allSelBox.setFont(f);
		scaleLabel.setFont(f);
		scrollHeader.add(allSelBox,BorderLayout.LINE_START);
		scrollHeader.add(scaleLabel,BorderLayout.LINE_END);
		if(foldsName.size()>0)
			scrollSubView.add(scrollHeader);
		for(int i=0;i<foldsName.size();i++){
			JPanel scrollItem=new JPanel();			
			scrollItem.setLayout(new BorderLayout(10, 1));
			
			JCheckBox itemBox=new JCheckBox(foldsName.get(i));
			itemBox.setFont(f);
//			itemBox.setBackground(Color.BLUE);
			
			JTextArea itemTex=new JTextArea(1,5);
//			itemTex.setBackground(new Color(0xffDBDBDB));
			itemTex.setText(String.valueOf((double)scales.get(i)));
			itemTex.setFont(f);
			
			itemBox.addActionListener(new ItemSeclectListener(i));
			itemTex.getDocument().addDocumentListener(new JTextListener(i));
			
			scrollItem.add(itemBox,BorderLayout.LINE_START);
			scrollItem.add(itemTex,BorderLayout.LINE_END);
//			scrollItem.setBackground(Color.BLACK);
			
			scrollSubView.add(scrollItem);
			
			checkBoxList.add(itemBox);
			txScalesList.add(itemTex);			
		}		
		setViewportView(scrollSubView);
	}
	
	public class ItemSeclectListener implements ActionListener{
		int itemIndex=0;
		
		public ItemSeclectListener(int index){
			this.itemIndex=index;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			if(checkBoxList.get(itemIndex).isSelected()){
				double scale=ComFun.parseFromStr(txScalesList.get(itemIndex).getText().trim());
				if(scale!=ComFun.INVALID)
					scales.set(itemIndex, scale);
				selectedMap.put(foldsName.get(itemIndex), scales.get(itemIndex));
			}else{
				selectedMap.remove(foldsName.get(itemIndex));
				if(allSelBox.isSelected())//保证全选状态正确
					allSelBox.setSelected(false);
			}
			if(changeSelListener!=null)
					changeSelListener.onSelectedChanged(selectedMap);			
		}
	}
	
	public class JTextListener implements DocumentListener{

		int itemIndex=0;
		
		public JTextListener(int index){
			this.itemIndex=index;
		}
		
		@Override
		public void insertUpdate(DocumentEvent e) {
			// TODO Auto-generated method stub
			changeDeal();
		}

		@Override
		public void removeUpdate(DocumentEvent e) {
			// TODO Auto-generated method stub
			changeDeal();
		}

		@Override
		public void changedUpdate(DocumentEvent e) {
			// TODO Auto-generated method stub
			changeDeal();
		}
		
		public void changeDeal(){
			double scale=ComFun.parseFromStr(txScalesList.get(itemIndex).getText().trim());
			if(scale!=ComFun.INVALID && scale!=scales.get(itemIndex)){
				scales.set(itemIndex, scale);
				if(checkBoxList.get(itemIndex).isSelected()){
					selectedMap.put(foldsName.get(itemIndex),scales.get(itemIndex));
					if(changeSelListener!=null)
						changeSelListener.onSelectedChanged(selectedMap);
				}
			}else{
//				txScalesList.get(itemIndex).setText(String.valueOf(scales[itemIndex]));
			}
		}
		
	}
	
	public void setChangeSelListener(onSelectListener changeSelListener) {
		this.changeSelListener = changeSelListener;
	}

	public interface onSelectListener{
		public void onSelectedChanged(Map<String, Double> selMap);
	}

	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
//		System.out.println("allSelBox has been triggled");
		boolean isAllSel=allSelBox.isSelected();
		if(isAllSel){
			for(int i=0;i<foldsName.size();i++){
				checkBoxList.get(i).setSelected(true);
				selectedMap.put(foldsName.get(i),scales.get(i));
			}
		}else{
			for(int i=0;i<foldsName.size();i++){
				checkBoxList.get(i).setSelected(false);
			}
			selectedMap.clear();
		}
		if(changeSelListener!=null)
			changeSelListener.onSelectedChanged(selectedMap);
	}
	
}