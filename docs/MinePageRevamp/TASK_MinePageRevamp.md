# TASK_MinePageRevamp

## 1. Task List

### Task 1: Resource Preparation
- **Goal**: Add necessary colors and drawables.
- **Actions**:
  - Update `colors.xml` with `neon_cyan`, `neon_purple`, `neon_blue`.
  - Create `bg_glass_panel.xml` (if not exists or needs update).
- **Output**: Updated resource files.

### Task 2: Implement EnergyRingView
- **Goal**: Create the dynamic ring view.
- **Actions**:
  - Create `ui/widget/EnergyRingView.java`.
  - Implement `onDraw` with `SweepGradient`.
  - Implement rotation logic and breathing logic.
  - Implement `explode()` method.
- **Output**: `EnergyRingView.java`.

### Task 3: Upgrade ParticleView
- **Goal**: Add parallax support.
- **Actions**:
  - Modify `ui/widget/ParticleView.java`.
  - Add `setScrollOffset(int y)` method.
  - Apply offset to particle Y positions in `onDraw` based on layer.
- **Output**: Modified `ParticleView.java`.

### Task 4: Create LifeStatusPanel Layout
- **Goal**: Create the status panel UI.
- **Actions**:
  - Create `layout/layout_life_status_panel.xml`.
  - Design the status indicator, progress bar, and text.
- **Output**: XML file.

### Task 5: Integration & Logic
- **Goal**: Wire everything in `MineFragment`.
- **Actions**:
  - Update `fragment_mine.xml`:
    - Replace `iv_avatar_halo` with `EnergyRingView`.
    - Add `layout_life_status_panel` (included, GONE).
  - Update `MineFragment.java`:
    - Bind `EnergyRingView`.
    - Setup Scroll Listener for `ParticleView`.
    - Setup Click Listener for Avatar -> Explode -> Show Panel.
- **Output**: Updated Fragment and XML.

## 2. Dependencies
- Task 2 and 3 can be parallel.
- Task 4 is independent.
- Task 5 depends on all above.
