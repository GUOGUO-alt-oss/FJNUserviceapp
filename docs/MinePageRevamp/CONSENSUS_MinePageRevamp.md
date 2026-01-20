# CONSENSUS_MinePageRevamp

## 1. Project Goal
Transform the "Mine" page from a utility tool to a "Personal Status System" (Phase 1: Ignition).
**Key Experience**: "This App knows my state."

## 2. Technical Solution
### 2.1. Component Architecture
- **`MineFragment` (Controller)**:
  - Manages `EnergyRingView` animation states.
  - Manages `NebulaView` scroll parallax (binds to ScrollView).
  - Handles Avatar Click -> Transition Animation -> Show Status Panel.
- **`EnergyRingView` (New Custom View)**:
  - Replaces `iv_avatar_halo`.
  - Draws dynamic gradient ring (SweepGradient).
  - Handles rotation (variable speed) and breathing (scale).
  - Public API: `setSpeed(float)`, `setPulse(boolean)`, `explode()`.
- **`NebulaView` (Enhanced `ParticleView`)**:
  - Adds `setScrollOffsetY(int y)` for parallax effect.
  - Layers: Far (slow parallax), Mid (medium), Near (fast parallax).
- **`LifeStatusPanel` (New Layout)**:
  - `include` layout in `fragment_mine.xml` (initially `GONE`).
  - Contains: Status Circle, Semester Progress (ProgressBar), Text.

### 2.2. Data Flow
1.  **Init**:
    - `ProfileViewModel` loads user data.
    - `MineFragment` calculates "Status State" (Week, Course Count) -> Calls `EnergyRingView.setState(...)`.
2.  **Scroll**:
    - `ScrollView` reports scroll change -> `MineFragment` calls `NebulaView.setParallax(dy)`.
3.  **Interaction**:
    - User clicks Avatar -> `MineFragment` triggers `EnergyRingView.explode()` -> Animates Panel visibility.

## 3. Implementation Details
- **Colors**:
  - Neon Cyan: `#00F2FE`
  - Neon Purple: `#4FACFE`
  - Deep Space: `#050510` (Background overlay)
- **Animation Specs**:
  - Ring Rotation: `ObjectAnimator` or `Choreographer` in View.
  - Explosion: `ViewPropertyAnimator` (Scale up + Fade out).
  - Panel Entrance: TranslationY + Alpha Fade In.

## 4. Boundaries & Limitations
- **Phase 1 Only**: Visuals and basic state mapping.
- **Mock Data**: "Week Number" and "Exam Week" might be mocked if API is missing.
- **Performance**: Ensure `NebulaView` + `EnergyRingView` don't drop frames on mid-range devices (limit particle count if needed).

## 5. Acceptance Criteria
1.  **Avatar Ring**: Rotates continuously, breathing effect visible.
2.  **Background**: Stars drift slowly, move with scroll (parallax).
3.  **Interaction**: Click avatar -> Ring expands -> Status Panel appears.
4.  **Visuals**: Matches "Cyberpunk/Space" aesthetic (Glows, Dark theme).
