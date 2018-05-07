package com.bby.yishijie.member.ui.login;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ViewFlipper;

import com.bby.yishijie.member.ui.MainActivity;
import com.sunday.common.base.BaseActivity;
import com.sunday.common.utils.SharePerferenceUtils;
import com.sunday.common.utils.StatusBarUtil;
import com.bby.yishijie.R;


import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 刘涛 on 2017/7/20.
 */

public class GuideActivity extends BaseActivity implements GestureDetector.OnGestureListener,View.OnClickListener{


    //实现拖动类效果属性声明
    private ViewFlipper flipper;
    private GestureDetector detector;
    private View pointImageView1;
    private View pointImageView2;
    private View pointImageView3;
    private int flag=1;
    private Button button;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        boolean isFirst = SharePerferenceUtils.getIns(mContext).getBoolean("is_first", true);
        if(!isFirst){
            Intent intent = new Intent(mContext, MainActivity.class);
            startActivity(intent);
            finish();
        }

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_guide);

        pointImageView1 = findViewById(R.id.point1);
        pointImageView2 = findViewById(R.id.point2);
        pointImageView3 = findViewById(R.id.point3);

        Resources resources = getResources();
        //注册一个用于手势识别的类
        detector = new GestureDetector(this);
        flipper = (ViewFlipper)this.findViewById(R.id.Viewflipper);

        flipper.addView(addView(R.layout.layout_guide_one));
        flipper.addView(addView(R.layout.layout_guide_two));
        flipper.addView(addView(R.layout.layout_guide_three));
//        flipper.addView(addView(R.layout.welc_4));
        button = (Button) flipper.findViewById(R.id.jump_login);
        button.setOnClickListener(this);
    }

    //添加视图
    private View addView(int layout){
        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(layout, null);
        return view;
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        return this.detector.onTouchEvent(event);
    }

    //用户轻触触摸屏，由1个MotionEvent ACTION_DOWN触发
    public boolean onDown(MotionEvent arg0) {
        // TODO Auto-generated method stub
//		Toast.makeText(this, "onDown",Toast.LENGTH_SHORT).show();
        return false;
    }

    //用户按下触摸屏，快速移动后松开，由1个MotionEvent ACTION_DOWN,多个ACTION_MOVE,1个ACTION_UP触发
    //参数解释
    //e1:第1个ACTION_DOWN MotionEvent
    //e2:最后一个ACTION_MOVE MotionEvent
    //velocityX:X轴上的移动速度，像素/秒
    //velocityY:Y轴上的移动速度，像素/秒
    //触发条件：X轴的坐标位移大于FLING_MIN_DISTANCE,且移动速度大于FLING_MIN_VELOCITY个像素/秒
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                           float velocityY) {
        // TODO Auto-generated method stub
        if(e1.getX()-e2.getX()>120){
            //Fling left
//			Toast.makeText(this, "Fling Left",Toast.LENGTH_SHORT).show();
            if(flag==3){

            }
            else {
                flag++;
                if (flag==1) {

                }
                if(flag==2){
					pointImageView1.setBackgroundResource(R.drawable.welcome_post);
					pointImageView2.setBackgroundResource(R.drawable.welcome_current);
					pointImageView3.setBackgroundResource(R.drawable.welcome_post);
                }
                if(flag==3){
					pointImageView1.setBackgroundResource(R.drawable.welcome_post);
					pointImageView2.setBackgroundResource(R.drawable.welcome_post);
					pointImageView3.setBackgroundResource(R.drawable.welcome_current);
                }
                this.flipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.push_left_in));
                this.flipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.push_left_out));
                this.flipper.showNext();
            }
            return true;
        }else if(e1.getX() - e2.getX() < -120){
            //Fling right
//			Toast.makeText(this, "Fling Right",Toast.LENGTH_SHORT).show();
            if(flag==1){

            }
            else {
                flag--;
                if (flag==3) {

                }
                if(flag==2){
                    pointImageView1.setBackgroundResource(R.drawable.welcome_post);
                    pointImageView2.setBackgroundResource(R.drawable.welcome_current);
                    pointImageView3.setBackgroundResource(R.drawable.welcome_post);
                }
                if(flag==1){
                    pointImageView1.setBackgroundResource(R.drawable.welcome_current);
                    pointImageView2.setBackgroundResource(R.drawable.welcome_post);
                    pointImageView3.setBackgroundResource(R.drawable.welcome_post);
                }
                this.flipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.push_right_in));
                this.flipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.push_right_out));
                this.flipper.showPrevious();
            }
            return true;
        }
        return false;
    }

    //用户长按触摸屏，由多个MotionEvent ACTION_DOWN触发
    public void onLongPress(MotionEvent e) {
        // TODO Auto-generated method stub

    }

    //用户按下触摸屏，并拖动，由1个Motion ACTION_DOWN,多个ACTION_MOVE触发
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
                            float distanceY) {
        // TODO Auto-generated method stub
        return false;
    }

    //用户轻触触摸屏，尚未松开或拖动，由一个MotionEvent ACTION_DOWN触发
    public void onShowPress(MotionEvent e) {
        // TODO Auto-generated method stub

    }

    //用户（轻触触摸屏后）松开，由一个MotionEvent ACTION_UP触发
    public boolean onSingleTapUp(MotionEvent e) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.jump_login:
                Intent intent = new Intent(mContext,MainActivity.class);
                SharePerferenceUtils.getIns(mContext).putBoolean("is_first",false);
                startActivity(intent);
                finish();
                break;

            default:
                break;
        }
    }
}
