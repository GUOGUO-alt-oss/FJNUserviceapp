# TASK_PersonalPlanetRevamp

## 1. Task List

### Task 1: Create `SolarSystemView`
- **Goal**: The container that manages orbits and rotation.
- **Features**:
  - Draw 3 concentric orbit rings.
  - Manage 3 lists of child views (one per orbit).
  - Handle touch rotation with differential speeds.
- **Output**: `ui/widget/planet/SolarSystemView.java`.

### Task 2: Create `PlanetView` and Subclasses
- **Goal**: The visual representation of planets.
- **Features**:
  - Base `PlanetView`.
  - Layouts for Data Planet (Number focused), Function Planet (Icon focused).
- **Output**: `ui/widget/planet/PlanetView.java`, `layout/item_planet.xml`.

### Task 3: Migrate `MineFragment` to Solar System
- **Goal**: Replace the list-based UI with the Solar System.
- **Actions**:
  - Update `fragment_mine.xml` to use `SolarSystemView`.
  - Update `MineFragment.java` to populate planets instead of lists.
  - Map existing data (GPA, Courses) to planets.
- **Output**: Updated Fragment and XML.

### Task 4: Implement Interactions
- **Goal**: Click to expand/navigate.
- **Actions**:
  - Implement "Fly to Center" animation on click.
  - Handle navigation callbacks.

## 2. Dependencies
- Task 1 and 2 can be parallel.
- Task 3 depends on 1 and 2.
- Task 4 is refinement of 3.
