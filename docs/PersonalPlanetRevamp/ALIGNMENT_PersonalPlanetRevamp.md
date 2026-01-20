# ALIGNMENT_PersonalPlanetRevamp

## 1. Project Context
- **Phase**: "Personal Planet Home" (个人星球主页) Revamp.
- **Goal**: Transform the current "List + Cards" layout into a "Solar System" concentric orbit layout.
- **Philosophy**: "Closer orbit = Closer to current state".

## 2. Requirement Understanding
### 2.1. Core Structure
- **Central Core (User)**: Avatar, Name, ID, Current Term. Static but breathing. Click -> Status Panel.
- **Orbit 1 (Status Orbit)**: "How am I doing now?".
  - Radius: Smallest.
  - Speed: Fast.
  - Content: Term Progress, GPA, Today's Courses.
- **Orbit 2 (Function Orbit)**: "What can I do?".
  - Radius: Medium.
  - Speed: Medium.
  - Content: Schedule, Grades, Favorites, Import History.
- **Orbit 3 (System Orbit)**: "System Management".
  - Radius: Largest.
  - Speed: Slowest.
  - Content: Edit Profile, Settings, About.

### 2.2. Interaction Rules
- **Rotation**: Swipe left/right rotates the entire system. Inner orbits rotate faster than outer orbits (Parallax rotation).
- **Click**: Planet detaches -> Fly to center -> Expand page. Other planets fade/retreat.
- **Return**: Reverse animation.

## 3. Technical Strategy
- **`SolarSystemView`**: A custom `ViewGroup` that handles:
  - Layout of children in orbits.
  - Touch handling for rotation (GestureDetector).
  - Physics simulation (Inertia, Deceleration).
  - Animations (Expand/Collapse).
- **Planets**: Child Views added to `SolarSystemView`. Can be `View` or `ViewGroup` (for complex planet UI).
- **Orbit Parameters**:
  - Defined in `OrbitConfig` class.
  - Radius, Angular Velocity, Inertia Factor.

## 4. Proposed Architecture
- `ui/widget/planet/SolarSystemView.java`: The main container.
- `ui/widget/planet/PlanetView.java`: The individual item view.
- `ui/widget/planet/OrbitLayout.java`: Helper to manage radius/angle.

## 5. Orbit Parameters Table (Draft)
| Orbit | Radius (dp) | Base Speed (deg/s) | Inertia | Brightness | Scale |
| :--- | :--- | :--- | :--- | :--- | :--- |
| Core | 0 | 0 | 0 | 1.0 | 1.2 |
| Orbit 1 | 120 | 5 | 0.8 | 1.0 | 1.0 |
| Orbit 2 | 200 | 3 | 0.6 | 0.8 | 0.9 |
| Orbit 3 | 280 | 1 | 0.4 | 0.6 | 0.8 |

*Note: Radius needs to adapt to screen width.*

## 6. Implementation Plan
1.  **Create `SolarSystemView`**: Handle drawing orbits and positioning children.
2.  **Implement Rotation**: Touch drag -> update angles.
3.  **Implement Planet Views**: Custom views for "Data Planet", "Function Planet", etc.
4.  **Integrate into `MineFragment`**: Replace existing layout with `SolarSystemView`.
