# DESIGN_PersonalPlanetRevamp

## 1. Architecture
### 1.1. Visual Hierarchy
```mermaid
graph TD
    A[MineFragment] --> B[SolarSystemView]
    B --> C[CorePlanet (Avatar)]
    B --> D[Orbit 1 (Status)]
    D --> D1[TermPlanet]
    D --> D2[GPAPlanet]
    D --> D3[TodayPlanet]
    B --> E[Orbit 2 (Function)]
    E --> E1[SchedulePlanet]
    E --> E2[GradesPlanet]
    E --> E3[FavoritesPlanet]
    E --> E4[ImportPlanet]
    B --> F[Orbit 3 (System)]
    F --> F1[EditProfilePlanet]
    F --> F2[SettingsPlanet]
    F --> F3[AboutPlanet]
```

## 2. Component Design

### 2.1. `SolarSystemView`
- **Inheritance**: `ViewGroup`
- **Responsibilities**:
  - `onLayout`: Position children based on `radius` and `angle`.
  - `onDraw`: Draw orbit lines (dashed/solid).
  - `onTouchEvent`: Handle rotation gestures.
  - `addPlanet(View view, int orbitIndex, float startAngle)`: API to add planets.

### 2.2. `PlanetView`
- **Inheritance**: `FrameLayout` (or `ConstraintLayout`)
- **Properties**:
  - `planetType`: DATA, FUNCTION, SYSTEM.
  - `isExpanded`: boolean.
- **Content**: Icon/Value + Label.

## 3. Orbit Parameters (Finalized)
| Parameter | Orbit 1 (Status) | Orbit 2 (Function) | Orbit 3 (System) |
| :--- | :--- | :--- | :--- |
| **Radius (dp)** | 110 | 190 | 270 |
| **Base Scale** | 1.0 | 0.85 | 0.7 |
| **Drag Factor** | 1.5x (Fast) | 1.0x (Normal) | 0.5x (Slow) |
| **Alpha** | 1.0 | 0.8 | 0.6 |
| **Z-Index** | 3 | 2 | 1 |

*Note: Radii are relative to center. If screen width is small, scale down proportionally.*

## 4. Interaction Logic
- **Drag**:
  - `dx` from gesture listener.
  - `angleChange = dx / sensitivity`.
  - `orbit1Angle += angleChange * 1.5`
  - `orbit2Angle += angleChange * 1.0`
  - `orbit3Angle += angleChange * 0.5`
  - `requestLayout()` to update positions.

- **Click**:
  - `TransitionManager.beginDelayedTransition()`
  - Target Planet `translation` to `(0,0)`.
  - Other Planets `alpha` -> 0, `scale` -> 0.
  - Show "Detail Fragment" or "Dialog".

## 5. Animation Details
- **Breathing**: `CorePlanet` scales 1.0 <-> 1.05.
- **Auto-Rotation**: When idle, orbits rotate very slowly (0.1 deg/frame).
