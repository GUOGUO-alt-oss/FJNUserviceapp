# DESIGN_MinePageRevamp_Phase2

## 1. Component Design

### 1.1. `AliveCardView` (Custom View)
- **Inherits**: `FrameLayout` (or `CardView` if we want easy shadows, but `FrameLayout` gives more control over drawing).
- **Attributes**:
    - `cardElevation`: default elevation.
    - `pressedElevation`: elevation when pressed (lower).
    - `tiltEnabled`: boolean.
- **Internal Logic**:
    - `onTouchEvent`: Calculate offset from center -> set `rotationX` and `rotationY`.
    - `onDraw`: Draw the "Flowing Border" overlay when active.
    - `animateEntrance(delay)`: `translationY` + `alpha` + `scale` animation.

### 1.2. `RollingTextView` (Helper/View)
- A utility method or subclass to animate text changes.
- `RollingTextHelper.animateText(TextView view, float from, float to, String format)`

### 1.3. `EmotionDataMapper`
- **Method**: `getLabelFor(DataType type, float value)`
- **Returns**: `String` (The "Human" text).
- **Types**: GPA, COURSE_COUNT, TERM_PROGRESS, TODAY_COURSE.

## 2. Integration
- **Layout**: Update `item_profile_stat_card.xml` to be root-wrapped by `AliveCardView`.
- **Fragment**:
    - On `onViewCreated`: Find all cards, loop and call `animateEntrance(i * 80)`.
    - On Data Update: Use `RollingTextHelper` and `EmotionDataMapper` to populate views.

## 3. Visual Specs
- **Tilt**: Max 4 degrees.
- **Border Flow**: Cyan/Purple gradient.
- **Entrance**: Start `translationY = 50dp`, `alpha = 0`. End `0`, `1`.
