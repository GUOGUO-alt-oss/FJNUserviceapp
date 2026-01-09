package com.example.fjnuserviceapp.ui.life.detail;

import android.os.Bundle; import android.text.TextUtils; import android.view.inputmethod.EditorInfo; import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity; import androidx.lifecycle.ViewModelProvider; import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.fjnuserviceapp.base.entity.LostAndFound; import com.example.fjnuserviceapp.databinding.ActivityLostSearchBinding; import com.example.fjnuserviceapp.ui.life.adapter.LostAndFoundAdapter;
import com.example.fjnuserviceapp.ui.life.viewmodel.LifeViewModel;

import java.util.ArrayList; import java.util.List;

public class LostSearchActivity extends AppCompatActivity {
    private ActivityLostSearchBinding binding; private LostAndFoundAdapter adapter;
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLostSearchBinding.inflate(getLayoutInflater()); setContentView(binding.getRoot());
        adapter = new LostAndFoundAdapter(this);
        binding.recycler.setLayoutManager(new LinearLayoutManager(this));
        binding.recycler.setAdapter(adapter);
        binding.btnBack.setOnClickListener(v -> finish());
        binding.btnSearch.setOnClickListener(v -> doSearch());
        binding.etKeyword.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) { doSearch(); return true; } return false;
        });
    }
    private void doSearch() {
        String kw = binding.etKeyword.getText().toString().trim();
        if (TextUtils.isEmpty(kw)) { Toast.makeText(this, "请输入关键词", Toast.LENGTH_SHORT).show(); return; }
        LifeViewModel vm = new ViewModelProvider(this).get(LifeViewModel.class);
        vm.getLostAndFound().observe(this, list -> {
            List<LostAndFound> res = new ArrayList<>();
            for (LostAndFound it : list) if (it.getTitle().contains(kw) || it.getDesc().contains(kw)) res.add(it);
            adapter.submitList(res);
        });
    }
}