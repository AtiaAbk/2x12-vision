# DHAKA UNIVERSITY CAMPUS GEOGRAPHY & CARTOGRAPHIC MAP SPECIFICATION
**Project:** Project July 2024  
**Level:** Level 01 — University of Dhaka  
**Authoritative Sources:** OpenStreetMap (OSM), Dhaka University Institutional Records, UNESCO World Heritage Documentation, Institute of Architects Bangladesh (IAB).

---

## 1. Real Geographic Coordinates of Core Level 1 Landmarks

The in-game campus map and 3D level environment are grounded directly in real-world geographic references from OpenStreetMap and institutional architectural surveys:

| Landmark | Real Geographic Coordinates | In-Engine (X, Y, Z) | Orientation / Silhouette |
| :--- | :--- | :--- | :--- |
| **Anti-Terrorism Raju Memorial Sculpture** | `23.73177° N, 90.39578° E` (OSM way 225234308) | `(0.0m, 0.32m, 0.0m)` | Island roundabout, 8 outward-facing student figures |
| **Teacher-Student Centre (TSC)** | `23.73115° N, 90.39620° E` (OSM way 655714604) | `(9.9m, 0.0m, 66.8m)` | 9° yaw, Student Union 158×37 ft, parabolic auditorium vault |
| **Hakim Chattar** | `23.73350° N, 90.39295° E` (OSM node 11824686638) | `(-83.0m, 0.0m, -46.0m)` | Brick pavilion platform, tea stall, gathering tree |
| **Central Library (Old Building)** | `23.73343° N, 90.39526° E` (UNESCO WHC 23°44′00″N) | `(-44.5m, 0.0m, -92.0m)` | Muzharul Islam 1953–55, pilotis, ramp, concrete louvres |
| **Curzon Hall (Faculty of Science)** | `23.72740° N, 90.40189° E` (OSM way 248671404) | `(64.0m, 0.0m, 64.0m)` | -135° yaw, Indo-Saracenic central tower, arched loggias |

---

## 2. Mathematical Coordinate Transformation

In the 3D game engine (jMonkeyEngine 3.7.0):
- **Origin `(0, 0, 0)`:** Center of the Anti-Terrorism Raju Memorial Sculpture.
- **`+X`:** East (towards Suhrawardy Udyan / Science Faculty avenue).
- **`-X`:** West (towards Hakim Chattar / Central Library / Arts Building).
- **`+Z`:** South (towards TSC entrance plaza / Curzon Avenue).
- **`-Z`:** North (towards Shahbagh / North University Avenue).

### Cartographic Screen Projection:
For a map canvas of dimensions $W \times H$:
$$mx = \frac{W}{2} + (X - X_{\text{offset}}) \times S$$
$$my = 0.48 \times H + (Z - Z_{\text{offset}}) \times S$$

Where:
- $S = 2.45\text{ pixels per meter}$
- $X_{\text{offset}} = 0\text{ m}, Z_{\text{offset}} = 12\text{ m}$
- Heading $\theta$ rotates the directional indicator clockwise where $0^\circ$ points North ($-Z$, up on map) and $180^\circ$ points South ($+Z$, down on map).

This guarantees that standard cartographic **North-Up** alignment is strictly preserved across all map rendering.

---

## 3. Historic Citations & Asset Rights

1. **OpenStreetMap Data:**
   - Data © OpenStreetMap contributors, licensed under the Open Database License (ODbL).
   - Features referenced: Raju Memorial (way 225234308), TSC (way 655714604), Curzon Hall (way 248671404), Hakim Chattar (node 11824686638).
2. **TSC Architecture Dossier:**
   - Constantinos A. Doxiadis (1961–1962). Architectural drawings and spatial analysis referenced from Kazi Khaleed Ashraf (The Daily Star, 2020) and Institute of Architects Bangladesh (IAB).
3. **Central Library Architecture Dossier:**
   - Muzharul Islam (1953–1955). UNESCO World Heritage tentative list dossier (*The Architectural Works of Muzharul Islam*).
4. **Curzon Hall Architecture Dossier:**
   - Indo-Saracenic composition (1904–1908). Photographic reference by Biswarup Ganguly (2015, CC BY 3.0) and University of Dhaka Centennial history.
