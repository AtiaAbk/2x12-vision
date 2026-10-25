# Reference Ledger — Level 01 campus landmarks

Every geometric or historical claim that the Level 01 scene (`game/src/main/java/bd/july2024/world/CampusWorld.java`)
is built on is listed here with its source and a status:

- **Verified** — stated by a primary or reliable secondary source, or measured from OpenStreetMap geometry.
- **Supported, incomplete** — a source states it, but the detail needed for the model (size, height, material) is missing or sources disagree.
- **Unverified / chosen** — no source found; the value in the scene is a modelling choice and must not be presented as fact.

Nothing in this file is a claim of exact reproduction. All sources were read on 2026-10-25.
Legacy LibGDX-era notes under `docs/research/` are not authoritative for this build.

## Coordinate convention used by the scene

- Origin (0, 0) = the Anti-Terrorism Raju Memorial Sculpture (OSM node 9150960977).
- +X = east, +Z = south, so north is −Z. Units are metres. 1.72 m characters give scale.
- Distances between landmarks are taken **at true scale** from OpenStreetMap, so the TSC union block is about 67 m south of the sculpture and the Central Library is 60–170 m to the north-west. This is deliberate: the earlier scene placed every landmark within 30 m of the sculpture, which no source supports.
- Footprint offsets were computed from OSM node coordinates with a local equirectangular projection (111,320·cos(lat) m per degree of longitude, 110,574 m per degree of latitude). Expected error after rounding: ±2 m.

## Sources

| Key | Source | Used for |
|---|---|---|
| OSM | OpenStreetMap data via the Overpass API (`overpass-api.de`), bounding box 23.730–23.738 N, 90.392–90.400 E, queried 2026-10-25. © OpenStreetMap contributors, ODbL. The two queries and their raw responses are committed under `docs/reference/osm/`. | Footprints, positions, levels, materials tags |
| WP-Raju | https://en.wikipedia.org/wiki/Anti_Terrorism_Raju_Memorial_Sculpture | Sculpture history and composition |
| DS-TSC | https://www.thedailystar.net/node/1616761 (The Daily Star feature on the TSC complex, with the original programme dimensions) | TSC building dimensions, structure, layout |
| WP-TSC | https://en.wikipedia.org/wiki/Teacher-Student_Centre,_University_of_Dhaka | Completion date, court arrangement, roof form |
| BP-MI | https://en.banglapedia.org/index.php/Islam,_Muzharul (Banglapedia entry on architect Muzharul Islam) | Central Library composition |
| UNESCO | https://whc.unesco.org/en/tentativelists/6674/ (tentative-list entry covering Muzharul Islam's early works) | Library construction dates |
| archnet | https://archnet.org/sites/4928 | Not obtained: HTTP 429 on every attempt. Nothing in the scene relies on it. |

## 1. Anti-Terrorism Raju Memorial Sculpture (সন্ত্রাস বিরোধী রাজু স্মারক ভাস্কর্য)

| Claim | Status | Source | How the scene uses it |
|---|---|---|---|
| Stands at the centre of the TSC Mor roundabout island | Verified | OSM node 9150960977 lies inside OSM way 225234308 (`historic=memorial`, `memorial=sculpture`), which is enclosed by the two roundabout ways 24448666 and 1478513859 (`junction=roundabout`, `lanes=4`, `surface=asphalt`, `name:en=TSC Mor`) | Sculpture at the origin; island and ring road built around it |
| Memorial island footprint about 20.6 m east–west by 20.3 m north–south, centre 1.4 m west / 0.5 m north of the sculpture node | Verified (OSM geometry) | OSM way 225234308, 22 nodes | Circular island, radius 10 m, centred on the origin |
| Roundabout carriageway centre-line radius about 18 m (ring spans x −19.5…+16.8, z −18.5…+17.4) | Verified (OSM geometry) | OSM ways 24448666, 1478513859 | Asphalt ring from radius 10 m to 25 m (four lanes assumed 3.5 m each, kerb included) |
| Eight figures standing with linked arms | Verified | WP-Raju | Eight figures in a ring, arms joined |
| Sculptors Shaymol Chowdhury and Gopal Paul; inaugurated 17 September 1997; commemorates Moin Hossain Raju, killed 13 March 1992 | Verified | WP-Raju | Memorial plaque text and documentation only |
| Figures arranged in a ring facing outward | Supported, incomplete | WP-Raju image caption only; no plan or measurement found | Ring of 8, radius 1.9 m, facing outward |
| Overall height, figure height, plinth size, material and colour | Unverified / chosen | No source with measurements found | Figures 2.3 m tall on a 1.1 m two-step plinth (ring top at about 3.6 m); dark bronze-toned material. **Approximate.** |

## 2. Teacher-Student Centre (TSC)

| Claim | Status | Source | How the scene uses it |
|---|---|---|---|
| Student Union block centre about (9.9, 66.8) from the sculpture, footprint 78.3 × 13.5 m, long axis rotated 9° (east end towards north) | Verified (OSM geometry) | OSM way 655714604 "Teacher Student Center", tags `building:levels=5`, `height=15.0`, `building:material=Concrete`, `building:entrances=2` | Position, 9° yaw and 15 m total height of the union block |
| Student Union Building: three storeys, 158 × 37 ft (48.2 × 11.3 m), reinforced-concrete frame with brick walls, double wing-like roof | Verified (DS-TSC) but conflicts with OSM `building:levels=5` | DS-TSC | Three-storey block 48 × 11.3 m with a V-section double roof over a clerestory band (the glazing under the raised roof edges is a modelling choice); the OSM footprint is 30 m longer, which the scene fills with the covered walkways DS-TSC describes. The storey count conflict is unresolved; three storeys used. |
| Auditorium centre about (−8.0, 101.4), footprint 51.7 × 21.2 m, long axis rotated 9°, round roof with two roof levels | Verified (OSM geometry) | OSM way 447734507 "TSC Auditorium", tags `roof:shape=round`, `roof:levels=2`, `building:material=Concrete` | Position and yaw |
| Auditorium 170 × 85 ft (51.8 × 25.9 m) under a reinforced-concrete parabolic vault | Verified (DS-TSC); OSM width 21.2 m disagrees with 25.9 m | DS-TSC | Length 51.8 m used (matches OSM to 0.1 m); width 24 m used as a compromise between the two sources; vault built as a segmented parabolic shell |
| Vault height | Unverified / chosen | — | 14 m to the crown. **Approximate.** |
| Brick jali screens; covered walkways on steel columns link the blocks; buildings aligned east–west; access from the north; cafeteria of two wings around a green mall | Verified | DS-TSC | Perforated-brick panels on the union block, covered walkway along its north side, entrance plaza on the north, two low cafeteria wings |
| Cafeteria wing footprint bounding box x 23.6…90.7, z 109…153.7, one storey | Verified (OSM geometry) | OSM way 1560050747 "TSC Cafeteria Wing", `building:levels=1` | Two single-storey wings inside that box |
| TSC Ground: open field with corners about (17,74), (48,69), (84,108), (23,116) | Verified (OSM geometry) | OSM way 296813434 `landuse=recreation_ground` | Left as grass |
| Construction began March 1962 (DS-TSC); infobox says completed 1961 (WP-TSC); architect Constantinos Doxiadis | Supported, incomplete (dates conflict) | DS-TSC, WP-TSC | Documentation only |
| Butterfly ("double") roof over the front block, buildings around courts | Verified | WP-TSC, DS-TSC | V-section roof on the union block |
| Window pattern, colours, railings, signage | Unverified / chosen | No elevation reference obtained | Painted-plaster and brick surfaces with simple openings. **Approximate.** |

## 3. Dhaka University Central Library (Muzharul Islam)

| Claim | Status | Source | How the scene uses it |
|---|---|---|---|
| Building I footprint: an east–west block x −98…−61, z −169…−148 (37 × 21 m) with a north–south wing x −97…−78, z −148…−92.5 (19 × 56 m); 3 levels; concrete | Verified (OSM geometry) | OSM way 295394032 "Central Library Building I", tags `building:levels=3`, `building:material=concrete`, `building:stairs=yes` | Footprint built from those corners |
| Building II footprint: east–west bar x −92.5…−53.5, z −88.6…−76.4 (39 × 12 m) plus north–south bar x −54…−35, z −121…−63 (19 × 58 m); 3 levels; concrete | Verified (OSM geometry) | OSM way 1152628065 "Central Library Building II" | Footprint built from those corners |
| Two rectangular blocks raised on pilotis; a ramp to the first floor; a third block with a vaulted roof; open ground floor | Verified | BP-MI | Open ground floor on columns on both blocks, external ramp on Building II, vaulted roof on the short east–west bar of Building II |
| Built 1953–1955 | Verified | UNESCO | Documentation only |
| Storey height, column spacing, façade pattern, colours | Unverified / chosen | No measured drawing obtained; archnet unavailable | 4.0 m open ground floor, 3.6 m upper floors, columns on a 6 m grid, exposed concrete with brick infill. **Approximate.** |
| Hakim Chattar at about (−83, −46), between the library and the road | Verified (OSM position) | OSM node 11824686638 "Hakim Chattar" | Tea-stall node and banyan tree placed there |
| Hakim Chattar banyan tree and tea stalls | Unverified / chosen | No source recorded for the tree | Kept as a scene element. **Approximate.** |

## 4. Roads

| Claim | Status | Source | How the scene uses it |
|---|---|---|---|
| Shahid Minar Sarak (শহীদ মিনার সড়ক) leaves the roundabout towards the south-west (bearing about 120° from +X) | Verified (OSM geometry) | OSM ways 1058445483, 1476269787 | South-west road arm |
| Other arms (west towards Nilkhet, east towards Shahbagh, south-east towards Doyel Chattar) | Supported, incomplete | Nearby OSM ways exist but were not traced | Four arms drawn; widths and exact bearings approximate |
| Footpath widths, kerbs, markings | Unverified / chosen | — | Approximate |

## 5. Not modelled, by decision

Dr. Milon memorial (OSM node 3006466329 at about (−30, −28)), the "Bird of Peace" artwork (node 3006309126 at (4, 44)), the Greek Memorial and the WWII memorial east of TSC are in the data but outside this milestone.

## 6. Vegetation

The tree positions in `CampusWorld.buildVegetation` are not surveyed; they are chosen to line the road arms and lawns. Species named in code are the ones commonly cited for the campus (krishnachura / Delonix regia, jarul / Lagerstroemia speciosa, radhachura / Peltophorum pterocarpum, and the banyan by Hakim Chattar), but no per-tree reference exists in this ledger.

| Claim | Status | Source | In scene |
|---|---|---|---|
| Krishnachura, jarul and radhachura line the roads around the roundabout and TSC | Unverified / chosen | General knowledge of the campus, no reference image per tree | 12 + 6 + 4 placements |
| Tree models | Stand-ins, documented | Poly Haven CC0 scans (`docs/ASSET_REGISTER.md`) | island_tree_02 scaled 2.6-3.6x for krishnachura, radhachura, shade; 4.4x for the banyan; jacaranda_tree at 0.55-0.63x for jarul |
| Blossom in late July | Unverified / chosen | Krishnachura flowers mainly April-June, jarul May-July; late flowers are plausible but not sourced for 2024 | 10-15 % of leaf quads tinted scarlet (krishnachura), yellow (radhachura) or mauve (jarul) |
| Banyan at Hakim Chattar | Supported, incomplete | Tree present in photographs of Hakim Chattar; size unmeasured | One large shade-variant tree at (-87, -52); no aerial roots |
