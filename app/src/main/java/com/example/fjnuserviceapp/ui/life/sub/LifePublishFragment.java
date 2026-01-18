package com.example.fjnuserviceapp.ui.life.sub;

import android.os.Bundle;
import android.view.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.fjnuserviceapp.databinding.FragmentLifePublishBinding;

public class LifePublishFragment extends Fragment {
    private FragmentLifePublishBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentLifePublishBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // TODO 下周填发布表单
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}