[out:json][timeout:90];
(
  node["tourism"="artwork"](23.7300,90.3930,23.7350,90.3980);
  node["historic"](23.7300,90.3930,23.7350,90.3980);
  way["tourism"="artwork"](23.7300,90.3930,23.7350,90.3980);
  way["historic"="memorial"](23.7300,90.3930,23.7350,90.3980);
  node["name"~"ভাস্কর্য|Sculpture|sculpture"](23.7300,90.3930,23.7350,90.3980);
  way["name"~"ভাস্কর্য|Sculpture|sculpture"](23.7300,90.3930,23.7350,90.3980);
  way["junction"="roundabout"](23.7300,90.3930,23.7350,90.3980);
  way["highway"]["name"](23.7310,90.3940,23.7340,90.3970);
);
out body geom;
