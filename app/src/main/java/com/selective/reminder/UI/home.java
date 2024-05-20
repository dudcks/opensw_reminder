package com.selective.reminder.UI;

import android.app.Activity;
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

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.google.gson.JsonObject;
import com.selective.reminder.R;
import com.selective.reminder.Util.memoItem;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class home extends Fragment {
    public static int num;
    private ImageButton add_memo;
    private RecyclerView memo_list;
    private EditText memo_text;
    private MemoAdapter memoAdapter;
    private List<memoItem> memoItems;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        num=0;


        SharedPreferences memos = requireContext().getSharedPreferences("memo", Activity.MODE_PRIVATE);
        SharedPreferences.Editor spEdit = memos.edit();
        spEdit.apply();

        add_memo = (ImageButton) view.findViewById(R.id.add_memo);
        memo_list = (RecyclerView) view.findViewById(R.id.memo_list);
        memo_text = (EditText) view.findViewById(R.id.memo_input);

        memoItems = new ArrayList<>();
        memoAdapter = new MemoAdapter(memoItems);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        memo_list.setLayoutManager(layoutManager);
        memo_list.setAdapter(memoAdapter);

        SharedPreferences memo = requireContext().getSharedPreferences("memo", Activity.MODE_PRIVATE);
        int n = memo.getInt("Memo_num",0);
        if(n!=0){
            num=n+1;
            for(int i=0;i<=n;i++){
                String j =memo.getString("memo" + i, null);
                if(j!=null){
                    try {
                        JSONObject a = new JSONObject(j);
                        memoItem newItem = new memoItem();
                        newItem.setId(a.getInt("id"));
                        newItem.setMemo(a.getString("memo"));
                        newItem.sethour(a.getString("h"));
                        newItem.setminute(a.getString("m"));
                        newItem.setIs_do(a.getBoolean("is_do"));

                        memoItems.add(newItem);
                        memoAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }


        add_memo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //새 엑티비티에서 입력값 받아오기.
                //로컬 저장소에 추가하기
                memoItem newItem = new memoItem();
                newItem.setId(num);
                newItem.setMemo("테스트중임다."+num);
                newItem.setIs_do(false);

                JSONObject newmemo = new JSONObject();
                try {
                    newmemo.put("id",num);
                    newmemo.put("memo",newItem.getMemo());
                    newmemo.put("is_do",newItem.getIs_do());
                    newmemo.put("h","00");
                    newmemo.put("m","00");
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                String memo = newmemo.toString();

                SharedPreferences memos = requireContext().getSharedPreferences("memo", Activity.MODE_PRIVATE);
                SharedPreferences.Editor spEdit = memos.edit();
                spEdit.putInt("Memo_num",num);
                spEdit.putString("memo"+num,memo);
                num++;
                spEdit.apply();

                memoItems.add(newItem);
                memoAdapter.notifyDataSetChanged();
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
            //int currentPosition = holder.getAdapterPosition();

            memoItem item = memoItems.get(position);

            holder.icon.setImageResource(R.drawable.test);
            holder.memo_edit.setText(item.getMemo());
            String time = item.gethour()+':'+item.getminute();
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
            TextView memo_edit; //나중에 그냥 텍스트로
            TextView memo_time; //나중에 그냥 텍스트로
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
}