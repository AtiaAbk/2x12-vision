[out:json][timeout:60];
(
  way["name"~"Teacher|TSC|Student Centre|Student Center",i](23.730,90.392,23.738,90.400);
  way["name"~"Central Library|Dhaka University Library",i](23.730,90.392,23.738,90.400);
  way["name"~"Raju",i](23.730,90.392,23.738,90.400);
  node["name"~"Raju",i](23.730,90.392,23.738,90.400);
  way["name"~"Curzon",i](23.724,90.395,23.732,90.405);
  way["name"~"Hakim",i](23.730,90.392,23.738,90.400);
  node["name"~"Hakim",i](23.730,90.392,23.738,90.400);
);
out body geom;
