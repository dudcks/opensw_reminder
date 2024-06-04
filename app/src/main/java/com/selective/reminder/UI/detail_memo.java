package com.selective.reminder.UI;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.selective.reminder.R;
import com.selective.reminder.Util.memoItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class detail_memo extends AppCompatActivity {
    TextView title;

    private RecyclerView memo_list;

    private MemoAdapter memoAdapter;
    TextView text_;

    private List<memoItem> memoItems;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_memo);
        int backId = getIntent().getIntExtra("backId", -1);

        memoItems = new ArrayList<>();
        memo_list = findViewById(R.id.memo_list__);

        text_ = findViewById(R.id.memo_input__);
        title = findViewById(R.id.day_title);

        memoAdapter = new MemoAdapter(getApplicationContext(),memoItems);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        memo_list.setLayoutManager(layoutManager);
        memo_list.setAdapter(memoAdapter);

        load_memo(backId);
    }



    private void load_memo(int back_id){
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        String url  = login.mainurl + "/api/memo/"+back_id;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        save_memo_res(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                SharedPreferences tokensp = getApplicationContext().getSharedPreferences("autoLogin", Activity.MODE_PRIVATE);
                String token = tokensp.getString("token",null);
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };
        queue.add(request);
    }

    public static class MemoAdapter extends RecyclerView.Adapter<MemoAdapter.MemoViewHolder>{
        private List<memoItem> memoItems;
        private Context context;

        public MemoAdapter(Context context,List<memoItem> memoItems) {
            this.context = context;
            this.memoItems = memoItems;
        }

        @NonNull
        @Override
        public MemoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_memo, parent, false);
            return new MemoViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MemoViewHolder holder, int position) {
            //memoItem item = memoItems.get(position);
            int adapterPosition = holder.getAdapterPosition();

            // 아이템이 여전히 유효한지 확인
            if (adapterPosition != RecyclerView.NO_POSITION) {
                memoItem item = memoItems.get(adapterPosition);

                holder.icon.setImageResource(getIconResourceId(item.getIcon_type()));
                holder.memo_edit.setText(item.getMemo());
                String time = String.valueOf(item.gethour()) +':'+ String.valueOf(item.getminute());
                holder.memo_time.setText(time);
                holder.is_done.setChecked(item.getIs_do());
            }
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
        @Override
        public int getItemCount() {
            return memoItems.size();
        }

        public class MemoViewHolder extends RecyclerView.ViewHolder {
            ImageView icon;
            TextView memo_edit;
            TextView memo_time;
            CheckBox is_done;


            public MemoViewHolder(@NonNull View itemView) {
                super(itemView);
                icon= (ImageView) itemView.findViewById(R.id.icon_type);
                memo_edit = (TextView) itemView.findViewById(R.id.memo_edit);
                memo_time = (TextView) itemView.findViewById(R.id.memo_time);
                is_done = (CheckBox) itemView.findViewById(R.id.is_done);
            }
        }
    }

    private void save_memo_res(JSONObject response){
        memoItems.clear();
        memoAdapter.notifyDataSetChanged();
        try {
            String day = response.getString("createyear")+ "." + response.getString("createmonth") + "." + response.getString("createday");
            String text = response.getString("feeling");
            JSONArray memoTextsArray = response.getJSONArray("memoTexts");
            for (int i = 0; i < memoTextsArray.length(); i++) {
                try {
                    JSONObject memoObject = memoTextsArray.getJSONObject(i);
                    int id = memoObject.getInt("memoTextId");
                    boolean is_do = memoObject.getBoolean("_do");
                    String content = memoObject.getString("content");
                    int alarm_hour = memoObject.getInt("alarm_hour");
                    int alarm_minute = memoObject.getInt("alarm_minute");
                    int icon_type = memoObject.getInt("icon");

                    memoItem newItem = new memoItem();
                    newItem.setId(i);
                    newItem.setMemo(content);
                    newItem.setIs_do(is_do);
                    newItem.sethour(alarm_hour);
                    newItem.setminute(alarm_minute);
                    newItem.setIcon_type(icon_type);

                    memoItems.add(newItem);
                }
                catch (JSONException e){
                    throw new RuntimeException(e);
                }
            }
            text_.setText(text);
            title.setText(day);
            memoAdapter.notifyDataSetChanged();
        }
        catch (JSONException e){
            throw new RuntimeException(e);
        }
    }

}