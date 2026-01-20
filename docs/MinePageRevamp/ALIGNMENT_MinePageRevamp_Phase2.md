# ALIGNMENT_MinePageRevamp_Phase2

## 1. Project Context
- **Phase**: Phase 2 "Awakening" (觉醒期).
- **Goal**: Transform data display from static numbers to "emotional state expressions".
- **Current State**: Phase 1 completed (Energy Ring, Nebula Background, Status Panel). Stats cards are simple `LinearLayout`s.

## 2. Requirement Understanding
### Core Concepts
- **Information Life Forms**: Cards are not static UI, they enter the scene dynamically.
- **Soulful Interaction**: Physics-based feedback (Tilt, Sink) and visual delight (Border Flow).
- **"Human" Data System**: Text that empathizes with the user, especially in "low" or "empty" states.

### Specific Requirements
1.  **Entrance Animation**:
    - Z-axis float up.
    - Blur -> Clear (might be hard to do real blur animation on Android View, alpha/scale is a good proxy, or `RenderEffect` on Android 12+).
    - Waterfall delay (80ms).
2.  **Interaction**:
    - **Tilt**: 3D rotation on touch (2-4 degrees).
    - **Border Flow**: A light stream passes over the border on interaction.
    - **Click**: Micro-sink + Number Rolling Counter.
3.  **Copywriting**:
    - Dynamic sub-text based on values (e.g., "0.00" -> "Starting from zero is cool").

## 3. Technical Decisions
- **`AliveCardView`**: A custom `FrameLayout` that handles:
    - `Camera` or `rotationX/Y` for 3D tilt.
    - `Canvas` drawing for the border flow (SweepGradient or LinearGradient translation).
    - Entrance animation logic.
- **Blur Effect**: Real-time blur animation is expensive. Will approximate with Alpha + Scale + translationZ. If Android 12+, use `RenderEffect`. For compatibility, stick to Alpha/Scale.
- **Number Rolling**: Use `ValueAnimator` to animate integer/float values and update `TextView`.

## 4. Ambiguities & Clarifications
- **"Border Flow"**: Is it constant or triggered?
    - *Requirement*: "Hover/Touch... Border light flow 300ms pass". Implies triggered on interaction.
- **"Data Speak Human"**: Where to store these strings?
    - *Decision*: Simple static mapping in a helper class `EmotionDataMapper`.

## 5. Strategy
1.  Create `AliveCardView` to wrap existing card layouts.
2.  Create `EmotionDataMapper` for text logic.
3.  Update `MineFragment` to apply animations and use the mapper.
