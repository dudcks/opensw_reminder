package com.selective.reminder.UI;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.selective.reminder.R;
import com.selective.reminder.Util.gridmemo;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class search extends Fragment {

    private static int memo_id =-1;
    private List<gridmemo> memoList = new ArrayList<>();

    private memogridAdapter adapter;

    Button load_btn;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        memo_id = -1;

        load_btn = view.findViewById(R.id.loadMoreButton);

        final GridView gv = view.findViewById(R.id.grid);

        adapter = new memogridAdapter(memoList, getContext());
        gv.setAdapter(adapter);

        load();

        gv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // 아무것도 하지 않음
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int lastVisibleItem = firstVisibleItem + visibleItemCount;
                if (lastVisibleItem == totalItemCount) {
                    // 화면에 보이는 마지막 아이템이 전체 아이템의 수와 같을 때
                    load_btn.setVisibility(View.VISIBLE);
                } else {
                    // 그 외의 경우
                    load_btn.setVisibility(View.GONE);
                }
            }
        });

        load_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                load();
            }
        });

        adapter.notifyDataSetChanged();
        return view;
    }

    public class memogridAdapter extends BaseAdapter {
        private List<gridmemo> memoList;
        private Context context;

        public memogridAdapter(List<gridmemo> memoList, Context mContext) {
            this.memoList = memoList;
            this.context = mContext;
        }

        @Override
        public int getCount() {
            return memoList.size();
        }

        @Override
        public Object getItem(int position) {
            return memoList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;

            gridmemo item = memoList.get(position);

            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.see_pre_memo, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.maintext = convertView.findViewById(R.id.maintext);
                viewHolder.daytext = convertView.findViewById(R.id.daytext);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

// 아이템의 위치를 태그로 설정
            viewHolder.maintext.setTag(position);
            viewHolder.maintext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // 클릭된 뷰의 위치를 가져옴
                    int position = (int) view.getTag();
                    // 해당 위치의 아이템을 가져옴
                    gridmemo clickedItem = memoList.get(position);
                    // 클릭된 아이템의 backid를 가져옴
                    int backId = clickedItem.getBackid();

                    // 디테일 액티비티로 이동하는 인텐트 생성 및 실행
                    Intent intent = new Intent(getContext(), detail_memo.class);
                    intent.putExtra("backId", backId);
                    startActivity(intent);
                }
            });

            viewHolder.maintext.setText(item.getMemo());
            viewHolder.daytext.setText(item.getDay());

            return convertView;
        }

        public class ViewHolder {
            TextView maintext;
            TextView daytext;
        }
    }

    public void load(){
        String url = login.mainurl + "/api/memo/ordered?last="+memo_id;
        RequestQueue queue = Volley.newRequestQueue(requireContext());
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if(response!=null){
                        download(response);
                        }
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

    public void download(JSONArray response){
        try {
            for (int i = 0; i < response.length(); i++) {
                memo_id = response.getJSONObject(i).getInt("memoId");
                StringBuilder memo = new StringBuilder();
                String day = response.getJSONObject(i).getString("createyear")+ "." + response.getJSONObject(i).getString("createmonth") + "." + response.getJSONObject(i).getString("createday");;
                JSONArray memoArray = response.getJSONObject(i).getJSONArray("memoTexts");
                for(int j=0;j<memoArray.length();j++){
                       memo.append(j+1).append(". ").append(memoArray.getJSONObject(j).getString("content")).append("\n\n");
                }
                gridmemo newa = new gridmemo();
                newa.setBackid(memo_id);
                newa.setMemo(memo.toString());
                newa.setDay(day);
                memo_id--;
                memoList.add(newa);
            }
            adapter.notifyDataSetChanged();
        }
        catch (JSONException e){
            throw new RuntimeException(e);
        }
    }
}