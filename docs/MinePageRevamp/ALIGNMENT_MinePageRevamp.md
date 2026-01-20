# ALIGNMENT_MinePageRevamp

## 1. Project Context Analysis
- **Project Type**: Native Android (Java)
- **Current Architecture**: MVVM (ViewModel, ViewBinding, Repository pattern implied)
- **Current "Mine" Page**:
  - `MineFragment.java`: Handles logic, observes `ProfileViewModel`.
  - `fragment_mine.xml`: Layout with `ParticleView` background, avatar header, stats cards, and function entries.
  - `AnimationUtils`: Existing utility for simple animations.
- **Dependencies**: AndroidX, Material Components, ViewBinding.

## 2. Requirement Understanding
**Goal**: Transform the "Mine" page into a "Personal Status System" (Phase 1: Ignition).

### Core Concepts
- **Avatar = Energy Core**: Central visual element.
- **Background = Living Universe**: Immersive, dynamic background.
- **Interaction**: "Click Avatar" reveals "Life Status Panel".

### Specific Requirements (Phase 1)
1.  **Avatar Energy Ring**:
    - Visual: Blue -> Purple -> Cyan gradient.
    - Animation: Non-uniform rotation, breathing scale (0.97-1.03).
    - State-driven: Speed varies by week/courses; "Exam Week" adds pulse; "Empty" is almost static.
2.  **Avatar Click Interaction**:
    - Animation: Ring accelerates (200ms) -> Particles explode (20-30) -> Background dims.
    - Action: Show "Life Status Panel".
3.  **Life Status Panel**:
    - Content: Today's Status (Color), Semester Progress, Status Description.
    - Visual: Floats up from center.
4.  **Background Upgrade**:
    - "Parallax Nebula": 3 layers of stars with different speeds.
    - Scroll interaction: Background moves slightly (parallax) when page scrolls.
    - Constraint: No flashing, no random shaking, smooth "cosmic" movement.

## 3. Ambiguities & Clarifications
1.  **"Non-uniform rotation"**: Does this mean varying speed over time (ease-in-out loop) or just a constant speed that depends on state? 
    - *Assumption*: It implies a natural, organic rotation (e.g., slow-fast-slow loop) plus the state-based base speed.
2.  **"Life Status Panel" Implementation**: Should this be a Dialog, a Fragment overlay, or a View hidden in the layout?
    - *Decision*: A `DialogFragment` or a full-screen overlay `View` within `MineFragment` is best for the "dim background" and "float up" effect. Given the complexity, a custom View overlay in `fragment_mine.xml` (initially `GONE`) might be smoother for transitions than a heavy Fragment transaction.
3.  **"Parallax Nebula" Implementation**: Is `ParticleView` sufficient?
    - *Analysis*: `ParticleView` likely handles simple particles. We might need to enhance it or create `NebulaView` to support 3 layers and scroll offset input.
4.  **State Logic**: Where do we get "Exam Week" or "Week X"?
    - *Assumption*: `ProfileViewModel` or a `ScheduleViewModel` should provide this data. We might need to mock it if not currently available.

## 4. Proposed Strategy
- **Refactor `ParticleView`** (or create `NebulaBackgroundView`) to support 3-layer parallax and scroll listening.
- **Create `EnergyRingView`** (custom View) to handle the gradient drawing and complex rotation/breathing logic, rather than just an ImageView with simple animation.
- **Implement `LifeStatusPanel`** as a custom included layout in `fragment_mine.xml`, managed by `MineFragment` visibility and animations.
- **Update `MineFragment`** to bind scroll events to background and click events to the transition sequence.
