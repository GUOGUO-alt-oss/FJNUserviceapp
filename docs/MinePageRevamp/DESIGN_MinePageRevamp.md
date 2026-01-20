# DESIGN_MinePageRevamp

## 1. System Architecture
### 1.1. Visual Hierarchy
```mermaid
graph TD
    A[MineFragment] --> B[NebulaView (Background)]
    A --> C[AvatarContainer]
    C --> D[EnergyRingView (Halo)]
    C --> E[ImageView (User Avatar)]
    A --> F[LifeStatusPanel (Overlay)]
    A --> G[Stats & Functions (Content)]
```

## 2. Component Design

### 2.1. `EnergyRingView` (Custom View)
- **Responsibility**: Render the dynamic energy ring.
- **Properties**:
  - `rotationSpeed`: float (degrees/frame).
  - `isBreathing`: boolean.
  - `colors`: int[] (Gradient colors).
- **Methods**:
  - `setState(State state)`: Idle, Busy, Exam.
  - `explode()`: Triggers the explosion animation.

### 2.2. `NebulaView` (Enhanced ParticleView)
- **Responsibility**: Render 3-layer starfield with parallax.
- **Enhancements**:
  - `setScrollOffset(int y)`: Shifts particles based on layer depth.
    - Layer 0 (Far): shift = y * 0.1
    - Layer 1 (Mid): shift = y * 0.3
    - Layer 2 (Near): shift = y * 0.5

### 2.3. `LifeStatusPanel` (Layout)
- **File**: `layout_life_status_panel.xml`
- **Structure**:
  - Root: `ConstraintLayout` (Glassmorphism background).
  - Content:
    - `StatusIndicatorView` (Circle with glow).
    - `TextView` (Status Description).
    - `ProgressBar` (Semester Progress).

## 3. Interface Definition

### 3.1. `MineFragment` Logic
```java
// OnScroll
scrollView.setOnScrollChangeListener((v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
    nebulaView.setScrollOffset(scrollY);
});

// OnAvatarClick
avatar.setOnClickListener(v -> {
    energyRing.explode();
    showStatusPanel();
});
```

## 4. Resource Definitions
- **Colors**: Define `neon_cyan`, `neon_purple` in `colors.xml` if not exists.
- **Drawables**: `bg_glass_panel` for the status panel.

## 5. Animation Strategy
- **Ring Rotation**: `invalidate()` loop in `EnergyRingView` with `System.currentTimeMillis()`.
- **Transitions**: `ViewPropertyAnimator` for performance.
