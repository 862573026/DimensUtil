package com.newx.dimens.ui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JRadioButton;

import org.dom4j.DocumentException;

import com.newx.dimens.common.ComFun;
import com.newx.dimens.common.LogPrint;
import com.newx.dimens.customcomponent.ScrollSelector;
import com.newx.dimens.xmlutil.DimensOperateTool;
import com.newx.dimens.xmlutil.DomXmlUtil;
import com.newx.dimens.xmlutil.ItemsFilter;
import com.newx.dimens.xmlutil.StartEndFilterImpl;
import com.newx.dimens.xmlutil.StartMatchFilterImpl;

import java.awt.SystemColor;
import java.awt.Font;

public class DimensUtil extends JFrame implements ActionListener, ScrollSelector.onSelectListener {

    private static final long serialVersionUID = 1L;
    private static final double DEFAUT_BASE_VALUE = 320.0;
    private static final String NIVAILID_MATCHER = "NIVAILID_MATCHER";
    private JPanel contentPane;

    private JTextArea txNameStartWith, txProjectPath, txBaseFold, txNameTobeChanged, txValueChange2, txStartName, txEndName, txNewFoldName, txNewScale, txNewDimenName, txNewDimenValue, txNameTobeDelete;
    private JLabel labelLog;
    private File srcFile;
    private JButton btnStartClone, btnProjectPath, btnAddFold, btnDeleteFold, btnBaseFold, btnAddDimen, btnChangeValue, btnDeleteVariable;
    private JRadioButton rbtnFilter1, rbtnFilter2;
    private ScrollSelector selector;

    private List<ItemsFilter> mFilterList = new ArrayList<ItemsFilter>();

    private List<File> mFileList = new ArrayList<File>();
    private static String projectPath = "";
    private String mMatcher, startItemName, endItemName;
    private String baseFold = "values";
    private int filterIndex = 1;

    private Map<String, Double> selMap;
    private List<String> foldsName = new ArrayList<String>();
    private List<Double> scales = new ArrayList<Double>();

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    DimensUtil frame = new DimensUtil();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     */
    public DimensUtil() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 607, 743);
        contentPane = new JPanel();
        contentPane.setBackground(SystemColor.scrollbar);
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);

        JLabel lblDimens = new JLabel("dimens\u5DE5\u5177");
        lblDimens.setFont(new Font("Arial Unicode MS", Font.PLAIN, 11));
        lblDimens.setBounds(0, 0, 95, 15);

        rbtnFilter1 = new JRadioButton("");
        rbtnFilter1.setBackground(SystemColor.scrollbar);
        rbtnFilter1.setBounds(27, 540, 21, 23);
        rbtnFilter1.setSelected(true);//默认选择过滤器1
        rbtnFilter1.addActionListener(this);

        rbtnFilter2 = new JRadioButton("");
        rbtnFilter2.setBackground(SystemColor.scrollbar);
        rbtnFilter2.setBounds(27, 595, 21, 23);
        rbtnFilter2.addActionListener(this);

        contentPane.setLayout(null);
        contentPane.add(lblDimens);

        ButtonGroup group = new ButtonGroup();
        group.add(rbtnFilter1);
        group.add(rbtnFilter2);


        contentPane.add(rbtnFilter1);
        contentPane.add(rbtnFilter2);

        JLabel label_2 = new JLabel("\u8282\u70B9\u540D\u5F00\u5934\uFF1A");
        label_2.setBounds(59, 540, 85, 23);
        contentPane.add(label_2);

        txNameStartWith = new JTextArea();
        txNameStartWith.setBounds(150, 539, 319, 24);
        txNameStartWith.setFont(new Font("Monospaced", Font.PLAIN, 14));
        contentPane.add(txNameStartWith);

        JLabel label_4 = new JLabel("\u8D77\u59CB\u8282\u70B9\uFF1A");
        label_4.setBounds(59, 595, 77, 23);
        contentPane.add(label_4);

        txStartName = new JTextArea();
        txStartName.setBounds(150, 593, 319, 24);
        txStartName.setFont(new Font("Monospaced", Font.PLAIN, 14));
        contentPane.add(txStartName);

        JLabel label_5 = new JLabel("\u7ED3\u675F\u8282\u70B9\uFF1A");
        label_5.setBounds(59, 627, 69, 23);
        contentPane.add(label_5);

        txEndName = new JTextArea();
        txEndName.setBounds(150, 626, 319, 24);
        txEndName.setFont(new Font("Monospaced", Font.PLAIN, 14));
        contentPane.add(txEndName);

        btnStartClone = new JButton("\u590D\u5236");
        btnStartClone.setBounds(494, 627, 72, 23);
        btnStartClone.addActionListener(this);
        contentPane.add(btnStartClone);

        JLabel label_7 = new JLabel("\u5185\u5BB9\u8FC7\u6EE4\u89C4\u5219\uFF1A");
        label_7.setFont(new Font("宋体", Font.PLAIN, 11));
        label_7.setBounds(27, 520, 85, 15);
        contentPane.add(label_7);

        JLabel label_6 = new JLabel("\u4ECE\u57FA\u51C6\u6587\u4EF6\u5411\u76EE\u6807\u6587\u4EF6\u590D\u5236\u5185\u5BB9\uFF1A");
        label_6.setBounds(27, 490, 216, 20);
        contentPane.add(label_6);

        JLabel label_8 = new JLabel("\u5DE5\u7A0B\u8DEF\u5F84\uFF1A");
        label_8.setBounds(27, 31, 68, 15);
        contentPane.add(label_8);

        txProjectPath = new JTextArea();
        txProjectPath.setFont(new Font("Monospaced", Font.PLAIN, 14));
        txProjectPath.setBounds(93, 26, 376, 43);
        txProjectPath.setLineWrap(true);
        contentPane.add(txProjectPath);


        selector = new ScrollSelector(foldsName, scales);
        selector.setBounds(141, 109, 328, 139);
        selector.setChangeSelListener(this);
        contentPane.add(selector);

        JLabel label_9 = new JLabel("\u76EE\u6807\u6587\u4EF6\u9009\u62E9\uFF1A");
        label_9.setBounds(26, 114, 111, 15);
        contentPane.add(label_9);

        btnAddFold = new JButton("\u65B0\u589E\u6587\u4EF6");
        btnAddFold.addActionListener(this);
        btnAddFold.setBounds(27, 259, 101, 23);
        contentPane.add(btnAddFold);

        txNewFoldName = new JTextArea();
        txNewFoldName.setFont(new Font("Monospaced", Font.PLAIN, 14));
        txNewFoldName.setBounds(143, 258, 265, 24);
        contentPane.add(txNewFoldName);

        txNewScale = new JTextArea();
        txNewScale.setFont(new Font("Monospaced", Font.PLAIN, 14));
        txNewScale.setBounds(418, 258, 51, 24);
        contentPane.add(txNewScale);

        JLabel label = new JLabel("\u57FA\u51C6\u6587\u4EF6\u8BBE\u7F6E\uFF1A");
        label.setBounds(27, 84, 110, 15);
        contentPane.add(label);

        txBaseFold = new JTextArea();
        txBaseFold.setText("values");
        txBaseFold.setFont(new Font("Monospaced", Font.PLAIN, 14));
        txBaseFold.setBounds(141, 79, 328, 24);
        contentPane.add(txBaseFold);

        btnProjectPath = new JButton("\u8BBE\u7F6E");
        btnProjectPath.addActionListener(this);
        btnProjectPath.setBounds(494, 27, 72, 23);
        contentPane.add(btnProjectPath);

        JLabel label_1 = new JLabel("\u5411\u76EE\u6807\u6587\u4EF6\u4E2D\u65B0\u589E\u5185\u5BB9\uFF1A");
        label_1.setBounds(27, 316, 145, 15);
        contentPane.add(label_1);

        JLabel label_3 = new JLabel("\u540D\uFF1A");
        label_3.setBounds(31, 341, 35, 15);
        contentPane.add(label_3);

        txNewDimenName = new JTextArea();
        txNewDimenName.setFont(new Font("Monospaced", Font.PLAIN, 14));
        txNewDimenName.setBounds(78, 337, 216, 24);
        contentPane.add(txNewDimenName);

        JLabel label_10 = new JLabel("\u503C\uFF1A");
        label_10.setBounds(324, 341, 35, 15);
        contentPane.add(label_10);

        txNewDimenValue = new JTextArea();
        txNewDimenValue.setFont(new Font("Monospaced", Font.PLAIN, 14));
        txNewDimenValue.setBounds(369, 337, 100, 24);
        contentPane.add(txNewDimenValue);

        btnAddDimen = new JButton("\u65B0\u589E");
        btnAddDimen.addActionListener(this);
        btnAddDimen.setBounds(494, 337, 72, 23);
        contentPane.add(btnAddDimen);

        JLabel lblLog = new JLabel("LOG\uFF1A");
        lblLog.setFont(new Font("宋体", Font.PLAIN, 14));
        lblLog.setBounds(27, 672, 69, 23);
        contentPane.add(lblLog);

        labelLog = new JLabel("\u6B64\u5904\u663E\u793A\u64CD\u4F5C\u72B6\u6001\u6216\u8005\u5F02\u5E38");
        labelLog.setBounds(89, 673, 400, 20);
        contentPane.add(labelLog);

        btnDeleteFold = new JButton("\u5220\u9664\u6587\u4EF6");
        btnDeleteFold.setBounds(27, 225, 101, 23);
        btnDeleteFold.addActionListener(this);
        contentPane.add(btnDeleteFold);

        btnBaseFold = new JButton("\u8BBE\u7F6E");
        btnBaseFold.setBounds(494, 80, 72, 23);
        btnBaseFold.addActionListener(this);
        contentPane.add(btnBaseFold);

        JLabel label_11 = new JLabel("\u540D\uFF1A");
        label_11.setBounds(30, 396, 35, 15);
        contentPane.add(label_11);

        JLabel label_12 = new JLabel("\u4FEE\u6539\u76EE\u6807\u6587\u4EF6\u6307\u5B9A\u5185\u5BB9\uFF1A");
        label_12.setBounds(26, 371, 162, 15);
        contentPane.add(label_12);

        txNameTobeChanged = new JTextArea();
        txNameTobeChanged.setFont(new Font("Monospaced", Font.PLAIN, 14));
        txNameTobeChanged.setBounds(77, 392, 216, 24);
        contentPane.add(txNameTobeChanged);

        txValueChange2 = new JTextArea();
        txValueChange2.setFont(new Font("Monospaced", Font.PLAIN, 14));
        txValueChange2.setBounds(368, 392, 100, 24);
        contentPane.add(txValueChange2);

        JLabel label_13 = new JLabel("\u503C\uFF1A");
        label_13.setBounds(323, 396, 35, 15);
        contentPane.add(label_13);

        btnChangeValue = new JButton("\u4FEE\u6539");
        btnChangeValue.setBounds(493, 392, 72, 23);
        btnChangeValue.addActionListener(this);
        contentPane.add(btnChangeValue);

        JLabel label_14 = new JLabel("\u540D\uFF1A");
        label_14.setBounds(31, 451, 35, 15);
        contentPane.add(label_14);

        JLabel label_15 = new JLabel("\u5220\u9664\u76EE\u6807\u6587\u4EF6\u4E2D\u6307\u5B9A\u5185\u5BB9\uFF1A");
        label_15.setBounds(27, 426, 161, 15);
        contentPane.add(label_15);

        txNameTobeDelete = new JTextArea();
        txNameTobeDelete.setFont(new Font("Monospaced", Font.PLAIN, 14));
        txNameTobeDelete.setBounds(78, 447, 216, 24);
        contentPane.add(txNameTobeDelete);

        btnDeleteVariable = new JButton("\u5220\u9664");
        btnDeleteVariable.setBounds(494, 447, 72, 23);
        btnDeleteVariable.addActionListener(this);
        contentPane.add(btnDeleteVariable);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
        if (e.getSource() == btnStartClone) {//复制按钮响应
            /**此处编写生成逻辑**/
            if (!isBaseFoldExist())
                return;
            if (preparForCopyOrAdd() == 0)
                return;
            DimensOperateTool tools = new DimensOperateTool(mFilterList, srcFile, mFileList,
                    new ArrayList<Double>(selMap.values()));
            try {
                tools.dimensClone();
                setLog("复制成功");
            } catch (DocumentException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                setLog("复制失败");
            }
        } else if (e.getSource() == rbtnFilter1) {//内容选择器1
            filterIndex = 1;
        } else if (e.getSource() == rbtnFilter2) {//内容选择器2
            filterIndex = 2;
        } else if (e.getSource() == btnProjectPath) {//工程目录设置按钮
            initFoldsDepensProjectPath();
        } else if (e.getSource() == btnAddFold) {//增加文件选项
            addNewFold();
        } else if (e.getSource() == btnDeleteFold) {//删除选中文件
            deleteFolds();
        } else if (e.getSource() == btnBaseFold) {//设置基准文件
            setBaseFold();
        } else if (e.getSource() == btnAddDimen) {//向选中文件中添加新的定义
            String strName = txNewDimenName.getText().trim();
            String strValue = txNewDimenValue.getText().trim();
            if (strName.equals("") || strValue.equals("")) {
                setLog("变量名和值不能为空");
                return;
            }
            if (
//					ComFun.getNumFromDimStr(strValue)==ComFun.INVALID||
                    ComFun.getUnit(strValue).equals("")) {
                setLog("新增项的值格式错误");
                return;
            }
            if (!isBaseFoldExist())
                return;
            if (preparForCopyOrAdd() == 0)
                return;
            DimensOperateTool tools = new DimensOperateTool(srcFile, mFileList
                    , new ArrayList<Double>(selMap.values()));
            try {
                tools.dimensAdd(strName, strValue);
                setLog("添加成功");
            } catch (DocumentException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                setLog("添加失败");
            }
        } else if (e.getSource() == btnChangeValue) {//修改值
            if (!isBaseFoldExist())
                return;
            if (preparForCopyOrAdd() == 0)
                return;
            DimensOperateTool tools = new DimensOperateTool(srcFile, mFileList
                    , new ArrayList<Double>(selMap.values()));
            String name = txNameTobeChanged.getText().trim();
            String value = txValueChange2.getText().trim();
            if (name.equals("") || value.equals("")) {
                setLog("待修改项 名字和值不能为空");
                return;
            }
            if (ComFun.getNumFromDimStr(value) == ComFun.INVALID || ComFun.getUnit(value).equals("")) {
                setLog("修改项的值格式错误");
                return;
            }
            try {
                tools.modItem(name, value);
                setLog("修改成功");
            } catch (DocumentException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                setLog("修改失败");
            }
        } else if (e.getSource() == btnDeleteVariable) {//删除值
            if (!isBaseFoldExist())
                return;
            if (preparForCopyOrAdd() == 0)
                return;
            DimensOperateTool tools = new DimensOperateTool(srcFile, mFileList
                    , new ArrayList<Double>(selMap.values()));
            String name = txNameTobeDelete.getText().trim();
            if (name.equals("")) {
                setLog("待删除项 名字不能为空");
                return;
            }
            try {
                tools.deleteItem(name);
                setLog("删除成功");
            } catch (DocumentException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                setLog("删除失败");
            }
        }
    }

    //检查源文件和目标文件是否存在，并做相应处理
    private int preparForCopyOrAdd() {
        int listSize = 0;
        mFileList.clear();
        mFilterList.clear();
        if (selMap != null && selMap.size() > 0) {
            for (String selfoldName : selMap.keySet()) {
                try {
                    mFileList.add(new File(getFilePathByFoldName(selfoldName)));
                    mFilterList.add(initFilter(selMap.get(selfoldName)));
                    listSize = mFileList.size();
                } catch (NullPointerException ex) {
                    ex.printStackTrace();
                    return listSize;
                }
            }
        }
        if (mFileList.size() == 0) {
            setLog("请选择至少一个目标文件");
        }
        return listSize;
    }

    private ItemsFilter initFilter(double scale) {
        ItemsFilter mFilter = null;
        switch (filterIndex) {
            case 1:
                mMatcher = txNameStartWith.getText().trim();
                if (!mMatcher.equals(""))
                    mFilter = new StartMatchFilterImpl(mMatcher);
                break;
            case 2:
                startItemName = txStartName.getText().trim();
                endItemName = txEndName.getText().trim();
                mFilter = new StartEndFilterImpl(startItemName, endItemName);
                break;
            default://未选择匹配规则
                mFilter = new StartMatchFilterImpl(NIVAILID_MATCHER);
                break;
        }
        return mFilter;
    }

    @Override
    public void onSelectedChanged(Map<String, Double> selMap) {
        // TODO Auto-generated method stub
        this.selMap = selMap;
        System.out.println(selMap);
    }

    public String correctPath(String path) {
        if (path.equals(""))
            return "";
        int resIndex = -1;
        if ((resIndex = path.indexOf("/res")) == -1) {
            resIndex = path.indexOf("\\res");
        }
        if (resIndex != -1) {
            path = path.substring(0, resIndex + 4);
        } else
            path = path + "/res";
        return path;
    }

    public void updateScrollSelector() {
        remove(selector);
        selector = new ScrollSelector(foldsName, scales);
        selector.setBounds(141, 109, 328, 139);
        selector.setChangeSelListener(this);
        contentPane.add(selector);
        invalidate();
        validate();
        repaint();
    }

    //根据文件夹名和基准文件名（/sw值）获取scale
    public double getScaleFromFoldName(String foldName, double baseValue) {
        return ComFun.round(getValueFromFoldName(foldName) / baseValue, 2, BigDecimal.ROUND_HALF_UP);
    }

    public double getScaleFromFoldName(String foldName, String baseName) {
        return ComFun.round(getValueFromFoldName(foldName) / getValueFromFoldName(baseName), 2, BigDecimal.ROUND_HALF_UP);
    }

    public double getValueFromFoldName(String foldName) {
        double value = ComFun.getVluesSw(foldName);
        value = (value == -1) ? DEFAUT_BASE_VALUE : value;
        return value;
    }

    public void setLog(String str) {
        LogPrint.logWarn(labelLog, str);
    }

    public void initFoldsDepensProjectPath() {
        projectPath = correctPath(txProjectPath.getText().trim());
        if (projectPath.equals("")) {
            setLog("工程路径为空");
            foldsName.clear();
            scales.clear();
            if (selMap != null)
                selMap.clear();
            updateScrollSelector();
            return;
        }
        txProjectPath.setText(projectPath.substring(0, projectPath.indexOf("res") - 1));
        File resFile = new File(projectPath);
        foldsName.clear();
        scales.clear();
        if (selMap != null)
            selMap.clear();
        if (resFile.exists()) {
            String[] tempFolds = resFile.list();
            for (String name : tempFolds) {//设置values文件夹列表,含有dimens文件的文件夹才会被加入
                if (name.startsWith("values")) {
                    File checkFile = new File(getFilePathByFoldName(name));
                    if (checkFile.exists())
                        foldsName.add(name);
                }
            }
            sortStrList(foldsName);
            if (foldsName.size() > 0) {//根据文件夹列表初始化缩放比率
                double baseValue = getValueFromFoldName(baseFold);
                for (String name : foldsName) {
                    scales.add(getScaleFromFoldName(name, baseValue));
                }
            }
            updateScrollSelector();
            setLog("工程路径载入成功");
        } else {
            setLog("工程路径不存在,请重新设置");
        }
    }

    public void addNewFold() {
        String newFoldName = txNewFoldName.getText().trim();
        if (newFoldName.equals("")) {
            setLog("文件夹名字为空，请输入");
            return;
        }
        try {
            File newFold = new File(getFilePathByFoldName(newFoldName));
            if (newFold.exists()) {
                setLog("文件已存在，无需添加");
                return;
            }

            ComFun.createFile(newFold);
            LinkedHashMap<String, String> gapMap = new LinkedHashMap<String, String>();
            DomXmlUtil.writeXml(gapMap, newFold);
            setLog("添加成功");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            setLog("添加文件失败，请检查路径");
            return;
        }
        foldsName.add(newFoldName);
        double newScale = ComFun.parseFromStr(txNewScale.getText().trim());
        if (newScale == -1) {
            newScale = getScaleFromFoldName(newFoldName, baseFold);
        }
        scales.add(newScale);
        updateScrollSelector();
    }

    public void deleteFolds() {//删除文件/文件夹
        for (String name : selMap.keySet()) {
            File tempFile = new File(getFilePathByFoldName(name));
            if (tempFile.exists()) {//若文件存在，则删除文件
                tempFile.delete();
            }
            File parentFile = new File(tempFile.getParent());//若文件夹为空，则删除该文件夹
            if (parentFile.list().length == 0)
                parentFile.delete();
            if (name.equals(baseFold)) {//若删除的文件是基准文件则恢复默认基准文件为 values
                baseFold = "values";
                txBaseFold.setText(baseFold);
            }
        }
        initFoldsDepensProjectPath();
    }

    public void setBaseFold() {
        baseFold = txBaseFold.getText().trim();
        if (!isBaseFoldExist())
            return;
        if (foldsName.size() > 0) {//根据文件夹列表初始化缩放比率
            scales.clear();
            double baseValue = getValueFromFoldName(baseFold);
            for (String name : foldsName) {
                scales.add(getScaleFromFoldName(name, baseValue));
            }
        }
        setLog("设置基准文件成功");
        if (selMap != null && selMap.size() > 0)
            selMap.clear();
        updateScrollSelector();
    }

    public String getFilePathByFoldName(String FoldName) {
        if (!projectPath.equals(""))
            return projectPath + "/" + FoldName + "/" + "dimens.xml";
        else {
            setLog("请设置正确的工程路径");
            return null;
        }
    }

    public boolean isBaseFoldExist() {
        try {
            srcFile = new File(getFilePathByFoldName(baseFold));
            if (!srcFile.exists()) {
                setLog("基准文件为空，请先创建");
                return false;
            }
            return true;
        } catch (NullPointerException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public void sortStrList(List<String> list) {
        Collections.sort(list, new Comparator<String>() {

            @Override
            public int compare(String str1, String str2) {
                // TODO Auto-generated method stub
                return str1.compareTo(str2);
            }
        });
    }
}
