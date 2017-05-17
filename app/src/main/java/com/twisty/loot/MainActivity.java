package com.twisty.loot;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.twisty.lootlib.Loot;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    RadioGroup radioGroup1, radioGroup2, radioGroup3;
    EditText maxCountEt;
    Button goButton;
    RecyclerView recyclerView;
    DemoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        radioGroup1 = (RadioGroup) findViewById(R.id.rg1);
        radioGroup2 = (RadioGroup) findViewById(R.id.rg2);
        radioGroup3 = (RadioGroup) findViewById(R.id.rg3);
        View rb31 = findViewById(R.id.rb31);
        View rb32 = findViewById(R.id.rb32);
        maxCountEt = (EditText) findViewById(R.id.maxCountEt);
        goButton = (Button) findViewById(R.id.go);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        goButton.setOnClickListener(v -> Loot.getInstance()
                .setSingle(radioGroup1.getCheckedRadioButtonId() == R.id.rb11)
                .setHasCamera(radioGroup2.getCheckedRadioButtonId() == R.id.rb21)
                .setHasCrop(radioGroup3.getCheckedRadioButtonId() == R.id.rb31)
                .setMaxCount(TextUtils.isEmpty(maxCountEt.getText().toString()) ? 9 : Integer.parseInt(maxCountEt.getText().toString()))
                .start(this, data -> {
                    if (adapter == null) {
                        adapter = new DemoAdapter(data);
                        recyclerView.setAdapter(adapter);
                    } else {
                        adapter.data.addAll(data);
                        adapter.notifyItemRangeInserted(adapter.data.size() - data.size(), data.size());
                    }
                }));

        radioGroup1.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rb11) {
                rb31.setEnabled(true);
                rb32.setEnabled(true);
                maxCountEt.setEnabled(false);
            } else {
                rb31.setEnabled(false);
                rb32.setEnabled(false);
                maxCountEt.setEnabled(true);
            }
        });
    }

    static class DemoAdapter extends RecyclerView.Adapter<IVViewHolder> {
        List<String> data;
        Context context;


        public DemoAdapter(List<String> data) {
            this.data = data;
        }

        @Override
        public IVViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            context = parent.getContext();
            View view = LayoutInflater.from(context).inflate(R.layout.demo_item, parent, false);
            return new IVViewHolder(view);
        }

        @Override
        public void onBindViewHolder(IVViewHolder holder, int position) {
            String imagePath = data.get(position);
            Glide.with(context).load(imagePath).into(holder.iv);
            holder.tv.setText(imagePath);
        }

        @Override
        public int getItemCount() {
            return data.size();
        }
    }

    static class IVViewHolder extends RecyclerView.ViewHolder {
        ImageView iv;
        TextView tv;

        public IVViewHolder(View itemView) {
            super(itemView);
            iv = (ImageView) itemView.findViewById(R.id.iv);
            tv = (TextView) itemView.findViewById(R.id.path);

        }
    }
}
