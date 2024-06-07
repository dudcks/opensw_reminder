package com.selective.reminder.UI;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TimePicker;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.selective.reminder.R;

import java.util.ArrayList;
import java.util.List;

public class input_memo extends AppCompatActivity {

    int icon_type;
    EditText text;
    Button btn_save;

    TimePicker timePicker;

    GridView iconGridView;
    IconAdapter iconAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_memo);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#EFEFEF")); // #EFEFEF 색상으로 변경

            View decor = getWindow().getDecorView();
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);// 어두운 텍스트 색상
        }

        iconGridView = findViewById(R.id.icon_grid_view);

        List<Integer> iconList = new ArrayList<>();
        iconList.add(0);
        iconList.add(1);
        iconList.add(2);
        iconList.add(3);
        iconList.add(4);
        iconList.add(5);

        iconAdapter = new IconAdapter(this, iconList);
        iconGridView.setAdapter(iconAdapter);

        iconGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 아이콘을 클릭했을 때 선택된 아이콘의 인덱스를 업데이트
                iconAdapter.setSelectedIcon(position);
            }
        });

        timePicker = (TimePicker) findViewById(R.id.simple_timepicker);
        timePicker.setIs24HourView(true);

        text = (EditText) findViewById(R.id.input_text);
        btn_save = (Button) findViewById(R.id.save_btn);

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hour =0;
                int minute =0;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    hour = timePicker.getHour();
                } else {
                    hour = timePicker.getCurrentHour();
                }
                minute =0 ;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    minute = timePicker.getMinute();
                } else {
                    minute = timePicker.getCurrentMinute();
                }

                Intent resultIntent = new Intent();
                resultIntent.putExtra("icon", icon_type);
                resultIntent.putExtra("memo_content", text.getText().toString());
                resultIntent.putExtra("hour", hour);
                resultIntent.putExtra("minute", minute);
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        });
    }

    public class IconAdapter extends BaseAdapter {
        private Context mContext;
        private List<Integer> mIconList; // 아이콘 리소스 ID 리스트
        private int mSelectedIconIndex = -1; // 선택된 아이콘의 인덱스 (-1은 선택되지 않음을 나타냄)

        public IconAdapter(Context context, List<Integer> iconList) {
            mContext = context;
            mIconList = iconList;
        }

        public void setSelectedIcon(int position) {
            mSelectedIconIndex = position;
            notifyDataSetChanged(); // 아이콘 선택이 변경되었음을 알림
        }

        @Override
        public int getCount() {
            return mIconList.size();
        }

        @Override
        public Object getItem(int position) {
            return mIconList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView;
            if (convertView == null) {
                imageView = new ImageView(mContext);
                DisplayMetrics displayMetrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                int screenWidth = displayMetrics.widthPixels;

// 이미지 뷰의 크기를 설정
                int cellWidth = (int) ((screenWidth / 3)*0.8); // 가로 크기를 화면의 1/3로 설정
                int cellHeight = cellWidth; // 세로 크기를 가로 크기와 동일하게 설정
                GridView.LayoutParams params = new GridView.LayoutParams(cellWidth, cellHeight);
                imageView.setLayoutParams(params);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            } else {
                imageView = (ImageView) convertView;
            }

            imageView.setImageResource(getIconResourceId(mIconList.get(position)));

            // 선택된 아이콘 표시를 위한 로직
            if (position == mSelectedIconIndex) {
                icon_type = mIconList.get(position);
                imageView.setBackgroundResource(R.drawable.rounded_background); // 선택된 아이콘 배경 색상 설정
            } else {
                imageView.setBackgroundColor(Color.TRANSPARENT); // 선택되지 않은 경우 배경을 투명하게 설정
            }

            return imageView;
        }

        private int getIconResourceId(int iconType) {
            switch (iconType) {
                case 1:
                    return R.drawable.type1;
                case 2:
                    return R.drawable.type2;
                case 3:
                    return R.drawable.type3;
                case 4:
                    return R.drawable.type4;
                case 5:
                    return R.drawable.type5;
                default:
                    return R.drawable.test; // 기본 아이콘 리소스 ID 반환
            }
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View focusView = getCurrentFocus();
        if (focusView != null) {
            Rect rect = new Rect();
            focusView.getGlobalVisibleRect(rect);
            int x = (int) ev.getX(), y = (int) ev.getY();
            if (!rect.contains(x, y)) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                if (imm != null)
                    imm.hideSoftInputFromWindow(focusView.getWindowToken(), 0);
                focusView.clearFocus();
            }
        }
        return super.dispatchTouchEvent(ev);
    }

}