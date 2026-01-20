package com.example.fjnuserviceapp.ui.mine.engine;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

/**
 * Identity Engine (Core)
 * Manages the User's Identity State, Weights, and Achievements.
 */
public class IdentityEngine {

    private static IdentityEngine instance;

    // Core Identity State
    private final MutableLiveData<IdentityState> identityState = new MutableLiveData<>();

    private IdentityEngine() {
        // Init with default state
        IdentityState state = new IdentityState();
        state.currentMode = IdentityMode.STUDENT;
        state.studyWeight = 0.8f;
        state.socialWeight = 0.4f;
        state.creativeWeight = 0.2f;
        state.floatingStatus = "🔥 冲刺期末";
        identityState.setValue(state);
    }

    public static synchronized IdentityEngine getInstance() {
        if (instance == null) {
            instance = new IdentityEngine();
        }
        return instance;
    }

    public LiveData<IdentityState> getIdentityState() {
        return identityState;
    }

    public void switchMode(IdentityMode newMode) {
        IdentityState current = identityState.getValue();
        if (current != null) {
            current.currentMode = newMode;
            // Adjust weights based on mode (Mock Logic)
            switch (newMode) {
                case STUDENT:
                    current.studyWeight = 0.9f;
                    current.socialWeight = 0.3f;
                    current.creativeWeight = 0.1f;
                    current.floatingStatus = "📚 专注模式";
                    break;
                case CREATOR:
                    current.studyWeight = 0.3f;
                    current.socialWeight = 0.5f;
                    current.creativeWeight = 0.9f;
                    current.floatingStatus = "🎨 灵感爆发";
                    break;
                case SOCIAL:
                    current.studyWeight = 0.4f;
                    current.socialWeight = 0.9f;
                    current.creativeWeight = 0.5f;
                    current.floatingStatus = "🤝 扩列中";
                    break;
            }
            identityState.setValue(current);
        }
    }

    public void updateFloatingStatus(String status) {
        IdentityState current = identityState.getValue();
        if (current != null) {
            current.floatingStatus = status;
            identityState.setValue(current);
        }
    }

    // --- Models ---

    public enum IdentityMode {
        STUDENT, CREATOR, SOCIAL, HIDDEN
    }

    public static class IdentityState {
        public IdentityMode currentMode;
        public float studyWeight;
        public float socialWeight;
        public float creativeWeight;
        public String floatingStatus;
        public List<Achievement> unlockedAchievements = new ArrayList<>();
    }

    public static class Achievement {
        public String id;
        public String name;
        public String icon; // Resource name or URL

        public Achievement(String id, String name, String icon) {
            this.id = id;
            this.name = name;
            this.icon = icon;
        }
    }
}
