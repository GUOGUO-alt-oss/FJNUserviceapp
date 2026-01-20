# TASK_MinePageRevamp_Phase2

## 1. Task List

### Task 1: Create `AliveCardView`
- **Goal**: The container for the "Living" cards.
- **Features**:
  - 3D Tilt on touch.
  - Scale down on click (Micro-sink).
  - Entrance animation method.
  - Border flow drawing (optional: if complex, can use a simple view overlay).
- **Output**: `ui/widget/AliveCardView.java`.

### Task 2: Implement Rolling Text Logic
- **Goal**: Animate numbers.
- **Output**: `utils/AnimationUtils.java` (add `animateRollingNumber` method).

### Task 3: Implement Emotion Logic
- **Goal**: Map data to "Human" strings.
- **Output**: `utils/EmotionDataMapper.java`.

### Task 4: UI Integration
- **Goal**: Update the cards to use `AliveCardView` and hook up data.
- **Actions**:
  - Update `item_profile_stat_card.xml`.
  - Update `MineFragment.java`:
    - Apply entrance animations.
    - Use Emotion Mapper.
    - Use Rolling Text.
- **Output**: Updated XML and Java files.

## 2. Execution Order
1.  Task 1 (View)
2.  Task 3 (Logic)
3.  Task 2 (Utils)
4.  Task 4 (Integration)
