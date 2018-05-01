package com.bby.yishijie.member.utils;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;


import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class EdittextUtil {
	public static String stringBefore;
	//输入框弹出小键盘后，点击enter键，收回小键盘
	public static void enterHidden(final Context context,final EditText edittext){
		edittext.setOnEditorActionListener(new OnEditorActionListener() {  
		    @Override  
		    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {  
		        if(actionId == EditorInfo.IME_ACTION_DONE || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)){  
		            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);  
		            imm.hideSoftInputFromWindow(edittext.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS); 
		            return true;  
		        }  
		        return false;  
		    }  
		});
	}

	/**
	 * 限制充值数量输入只能是大于0的数
	 * @param numEditText
	 */
	public static void rechargeNumFormat(final EditText numEditText){
		numEditText.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				String ssString = s.toString();

				if (ssString.length()>1&&ssString.startsWith("0")) {
					numEditText.setText(ssString.substring(1));
				}
			}
		});
	}
	public static void priceFormat(final EditText priceEditText){
		priceEditText.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				String ssString = s.toString();

				if (ssString.contains(".")) {
					String [] arrStrings =ssString.split("\\.");
					if(arrStrings.length==2&&ssString.split("\\.")[1].length()>2){
						priceEditText.setText(ssString.substring(0, ssString.length()-1));
						priceEditText.setSelection(ssString.length()-1);
					}
					else if (ssString.equals(".")) {
						priceEditText.setText("");
					}
					else if(ssString.startsWith(".")){
						priceEditText.setText("0"+ssString);
					}
				}
			}
		});
	}
	public static void priceLimitFormat(final EditText priceEditText,final Context context){
		priceEditText.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				stringBefore = s.toString();
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				String ssString = s.toString();
				if (!ssString.trim().equals("")&&!ssString.equals(".")) {
					double priceDouble = Double.parseDouble(ssString);
						if (priceDouble>999999.99) {
							priceEditText.setText(stringBefore);
							priceEditText.setSelection(6);
							Toast.makeText(context, "价格输入值在1-999999.99元之间", Toast.LENGTH_LONG).show();
						}
				}
				else if (ssString.equals(".")) {
					
				}
			}
		});
	}
	public static void priceFormatPrice(final EditText priceEditText,final EditText numEditText,final TextView sumTextView){
		priceEditText.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			@Override
			public void afterTextChanged(Editable s) {
				String ssString = s.toString();
				if (ssString.contains(".")) {
					String [] arrStrings =ssString.split("\\.");
					if(arrStrings.length==2&&ssString.split("\\.")[1].length()>2){
						priceEditText.setText(ssString.substring(0, ssString.length()-1));
						priceEditText.setSelection(ssString.length()-1);
					}
					else if (ssString.equals(".")) {
						priceEditText.setText("");
					}
					else if(ssString.startsWith(".")){
						priceEditText.setText("0"+ssString);
					}
				}
				
				String priceString = priceEditText.getText().toString();
				String numString = numEditText.getText().toString();
				if(priceString.equals("")||numString.equals("")){
					sumTextView.setText("");
				}
				else {
					double price = Double.parseDouble(priceString);
					int num = Integer.parseInt(numString);
					double sum = multiple(price,num);
					sumTextView.setText(String.valueOf(sum));
				}
			}
		});
	}
	public static void watchLength(EditText editText,final TextView numTextView,final int maxLength){
		editText.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				String ssString = s.toString();
				numTextView.setText(ssString.length()+"/"+maxLength);
			}
		});
	}
	public static void phoneFormat(final EditText phoneEditText){
		phoneEditText.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				String ssString = s.toString();
				
				if (ssString.contains(".")) {
					phoneEditText.setText(ssString.replace(".", ""));
					phoneEditText.setSelection(ssString.length()-1);
				}
			}
		});
	}
	public static void lengthFormat(final EditText editText,final int length){
		editText.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				String ssString = s.toString();
				
				if (ssString.length()>length) {
					editText.setText(ssString.substring(0,10));
//					editText.setSelection(ssString.length()-1);
				}
			}
		});
	}
	public static void emojiDelete(final EditText editText){
		editText.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable editable) {
				int index = editText.getSelectionStart() - 1;
				if (editable.toString().length()==1) {
					 if (isEmojiCharacter(editable.charAt(0))) {
					       editText.setText("");
					   }
				}
				else if (index > 0) {
				    if (isEmojiCharacter(editable.charAt(index))) {
				       Editable edit = editText.getText();
				       edit.delete(index, index + 1);
				   }
				}
			}
		});
	}
	public static void specialCharDelete(final EditText editText){
		editText.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				String editable = editText.getText().toString();  
		          String str = StringFilter(editable.toString());
		          if(!editable.equals(str)){
		              editText.setText(str);
		              //设置新的光标所在位置  
		              editText.setSelection(str.length());
		          }
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable editable) {
			}
		});
	}
	private static boolean isEmojiCharacter(char codePoint) {
		return !((codePoint == 0x0) || (codePoint == 0x9) || (codePoint == 0xA) || (codePoint == 0xD) || ((codePoint >= 0x20) && codePoint <= 0xD7FF))|| ((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) || ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF));
	}
	/**
	 * double类型相乘
	 * @param v1 第一个小数
	 * @param v2 第二个小数
	 * @return
	 */
	public static double multiple(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.multiply(b2).doubleValue();
	}
	/**
	 * double和int类型相乘
	 * @param v1 第一个小数
	 * @param v2 第二个小数
	 * @return
	 */
	public static double multiple(double v1, int v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(String.valueOf(v2));
		return b1.multiply(b2).doubleValue();
	}
	
	/**
	 * double类型相乘保留小数
	 * @param v1 第一个小数
	 * @param v2 第二个小数
	 * @param num 保留的位数
	 * @return
	 */
	public static double multiple(double v1, double v2,Integer num) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.multiply(b2).setScale(num, BigDecimal.ROUND_HALF_UP).doubleValue();
	}
	
	 // 过滤特殊字符
	 public static String StringFilter(String str) throws PatternSyntaxException {
	  // 只允许字母和数字
//	  String   regEx  =  "[^a-zA-Z0-9\u4E00-\u9FA5]";
		 String regEx = "[^-a-zA-Z0-9()_\u4E00-\u9FA5]";
	  // 清除掉所有特殊字符
//	  String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……& amp;*（）——+|{}【】‘；：”“’。，、？]";
	  Pattern p = Pattern.compile(regEx);
	  Matcher m = p.matcher(str);
	  return m.replaceAll("").trim();
	 }
}
