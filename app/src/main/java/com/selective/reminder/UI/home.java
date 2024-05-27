package com.selective.reminder.UI;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.SharedPreferences;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.chip.Chip;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.selective.reminder.MainActivity;
import com.selective.reminder.R;
import com.selective.reminder.Util.memoItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class home extends Fragment {
    public static int num;
    private ImageButton add_memo;
    private RecyclerView memo_list;
    private EditText memo_text;
    private MemoAdapter memoAdapter;
    private List<memoItem> memoItems;
    private Button reload;
    private ActivityResultLauncher<Intent> startForResult;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        num=0;


        add_memo = (ImageButton) view.findViewById(R.id.add_memo);
        memo_list = (RecyclerView) view.findViewById(R.id.memo_list);
        memo_text = (EditText) view.findViewById(R.id.memo_input);
        reload = (Button) view.findViewById(R.id.reload);

        memoItems = new ArrayList<>();
        memoAdapter = new MemoAdapter(memoItems);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        memo_list.setLayoutManager(layoutManager);
        memo_list.setAdapter(memoAdapter);

        startForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            //새 엑티비티에서 입력값 받아오기.
                            //로컬 저장소에 추가하
                            String returnedString = data.getStringExtra("resultString");
                            int icon_type = data.getIntExtra("icon_type", 0);
                            String content = data.getStringExtra("memo_content");
                            int hour = data.getIntExtra("hour", 0);
                            int minute = data.getIntExtra("minute", 0);
                            memoItem newItem = new memoItem();
                            newItem.setId(num);
                            newItem.setMemo(content+num);
                            newItem.setIcon_type(icon_type);
                            newItem.sethour(hour);
                            newItem.setminute(minute);
                            newItem.setIs_do(false);

                            JSONObject newmemo = new JSONObject();
                            try {
                                newmemo.put("id",num);
                                newmemo.put("back_id",-1);
                                newmemo.put("icon_type",icon_type);
                                newmemo.put("memo",newItem.getMemo());
                                newmemo.put("is_do",newItem.getIs_do());
                                newmemo.put("h",hour);
                                newmemo.put("m",minute);
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                            String memo = newmemo.toString();

                            SharedPreferences memos = requireContext().getSharedPreferences("memo", Activity.MODE_PRIVATE);
                            SharedPreferences.Editor spEdit = memos.edit();
                            spEdit.putString("memo"+num,memo);
                            num++;
                            spEdit.putInt("Memo_num",num);
                            spEdit.apply();
                            memoItems.add(newItem);
                            memoAdapter.notifyDataSetChanged();
                        }
                    }
                }
            });

        load_page();

        add_memo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), input_memo.class);
                startForResult.launch(intent);
            }
        });
        reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //서버에 업로드 후 로컬 저장소 갱신
                sycn();
            }
        });

        return view;
    }

    public static class MemoAdapter extends RecyclerView.Adapter<MemoAdapter.MemoViewHolder>{
        private List<memoItem> memoItems;

        public MemoAdapter(List<memoItem> memoItems) {
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
            memoItem item = memoItems.get(position);

            holder.icon.setImageResource(R.drawable.test);
            holder.memo_edit.setText(item.getMemo());
            String time = String.valueOf(item.gethour()) +':'+ String.valueOf(item.getminute());
            holder.memo_time.setText(time);
            holder.is_done.setChecked(item.getIs_do());

            holder.is_done.setOnCheckedChangeListener((buttonView, isChecked) -> {
                item.setIs_do(isChecked);
                Toast.makeText(buttonView.getContext(),"눌림"+item.getId(),Toast.LENGTH_SHORT).show();
                SharedPreferences memos = buttonView.getContext().getSharedPreferences("memo", Activity.MODE_PRIVATE);
                SharedPreferences.Editor spEdit = memos.edit();
                String a = memos.getString("memo"+item.getId(),null);
                try {
                    assert a != null;
                    JSONObject newj = new JSONObject(a);
                    newj.put("is_do",isChecked);
                    a = newj.toString();
                    spEdit.putString("memo"+item.getId(),a);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                spEdit.apply();
            });

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
    private void sycn(){
        SharedPreferences memos = requireContext().getSharedPreferences("memo", Activity.MODE_PRIVATE);
        int ex = memos.getInt("Memo_num",-1);

        if(ex > -1){
            //메모가 존재하면 로컬메모를 원본으로 생각..
            JSONObject root = make_json();
            //memo_text.setText(root.toString());
            upload(root);
            //load_memo();//이거는 나중에 삭제해야함..
        }
        else {
            //upload가 아니라 load가 필요함..
            load_memo();
        }
        load_page();
    }

    private JSONObject make_json(){
        JSONObject root = new JSONObject();
        try {

            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH)+1;
            int day = calendar.get(Calendar.DATE);

            root.put("createyear", year);
            root.put("createmonth", month);
            root.put("createday", day);
            root.put("title", "오늘의 일정");
            root.put("feeling", memo_text.getText().toString());

            SharedPreferences memo = requireContext().getSharedPreferences("memo", Activity.MODE_PRIVATE);
            int n = memo.getInt("Memo_num", -1);
            if (n != -1) {
                num = n;
                JSONArray memoTexts = new JSONArray();
                for (int i = 0; i <= n; i++) {
                    String j = memo.getString("memo" + i, null);
                    if (j != null) {
                        try {
                            JSONObject memoObject = new JSONObject();
                            JSONObject a = new JSONObject(j);
                            memoObject.put("content", a.getString("memo"));
                            memoObject.put("_do",a.getBoolean("is_do"));
                            memoObject.put("alarm_hour", a.getInt("h"));
                            memoObject.put("alarm_minute", a.getInt("m"));
                            memoTexts.put(memoObject);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
                root.put("memoTexts", memoTexts);
            }
        }
        catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return root;
    }
    private void upload(JSONObject jsonbody){
        RequestQueue queue = Volley.newRequestQueue(requireContext());

        String url  = login.mainurl + "/api/memo/upload";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonbody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(getContext(),"성공",Toast.LENGTH_SHORT).show();
                        //SharedPreferences sp = requireContext().getSharedPreferences("memo", 0);
                        //SharedPreferences.Editor spEdit = sp.edit();
                        //spEdit.clear();
                        //spEdit.apply();
                        //num=0;
                        //save_memo_res(response);
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
                SharedPreferences tokensp = requireActivity().getSharedPreferences("autoLogin", Activity.MODE_PRIVATE);
                String token = tokensp.getString("token",null);
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };
        queue.add(request);
    }

    private void load_memo(){
        RequestQueue queue = Volley.newRequestQueue(requireContext());

        String url  = login.mainurl + "/api/memo/today";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(getContext(),"성공",Toast.LENGTH_SHORT).show();
                        SharedPreferences sp = requireContext().getSharedPreferences("memo", 0);
                        SharedPreferences.Editor spEdit = sp.edit();
                        spEdit.clear();
                        spEdit.apply();
                        num=0;
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
                SharedPreferences tokensp = requireActivity().getSharedPreferences("autoLogin", Activity.MODE_PRIVATE);
                String token = tokensp.getString("token",null);
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };
        queue.add(request);
    }
    private void load_page(){
        memoItems.clear();
        memoAdapter.notifyDataSetChanged();
        SharedPreferences memo = requireContext().getSharedPreferences("memo", Activity.MODE_PRIVATE);
        int n = memo.getInt("Memo_num",-1);
        if(n!=-1){
            num=n;
            for(int i=0;i<n;i++){
                String j =memo.getString("memo" + i, null);
                if(j!=null){
                    try {
                        JSONObject a = new JSONObject(j);
                        memoItem newItem = new memoItem();
                        newItem.setId(a.getInt("id"));
                        newItem.setBackid(a.getInt("back_id"));
                        newItem.setIcon_type(a.getInt("icon_type"));
                        newItem.setMemo(a.getString("memo"));
                        newItem.sethour(a.getInt("h"));
                        newItem.setminute(a.getInt("m"));
                        newItem.setIs_do(a.getBoolean("is_do"));

                        memoItems.add(newItem);
                        memoAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }

    private void save_memo_res(JSONObject response){
        memoItems.clear();
        memoAdapter.notifyDataSetChanged();
        try {
            JSONArray memoTextsArray = response.getJSONArray("memoTexts");
            for (int i = 0; i < memoTextsArray.length(); i++) {
                try {
                    JSONObject memoObject = memoTextsArray.getJSONObject(i);
                    int id = memoObject.getInt("memoTextId");
                    boolean is_do = memoObject.getBoolean("_do");
                    String content = memoObject.getString("content");
                    int alarm_hour = memoObject.getInt("alarm_hour");
                    int alarm_minute = memoObject.getInt("alarm_minute");

                    memoItem newItem = new memoItem();
                    newItem.setId(num);
                    newItem.setMemo(content);
                    newItem.setIs_do(is_do);
                    newItem.sethour(alarm_hour);
                    newItem.setminute(alarm_minute);

                    JSONObject newmemo = new JSONObject();
                    try {
                        newmemo.put("id",num);
                        newmemo.put("back_id",id);
                        newmemo.put("icon_type",0);
                        newmemo.put("memo",content);
                        newmemo.put("is_do",is_do);
                        newmemo.put("h",alarm_hour);
                        newmemo.put("m",alarm_minute);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    String memo = newmemo.toString();

                    SharedPreferences memos = requireContext().getSharedPreferences("memo", Activity.MODE_PRIVATE);
                    SharedPreferences.Editor spEdit = memos.edit();
                    spEdit.putString("memo"+num,memo);
                    num++;
                    spEdit.putInt("Memo_num",num);
                    spEdit.apply();
                    memoItems.add(newItem);
                    memoAdapter.notifyDataSetChanged();
                }
                catch (JSONException e){
                    throw new RuntimeException(e);
                }
            }
        }
        catch (JSONException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onPause(){
        super.onPause();

        SharedPreferences memos = requireContext().getSharedPreferences("memo", Activity.MODE_PRIVATE);
        SharedPreferences.Editor spEdit = memos.edit();
        spEdit.putString("about_today",memo_text.getText().toString());
        spEdit.apply();
    }
}