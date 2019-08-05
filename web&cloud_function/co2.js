  // 建立 Leaflet 地圖
  var map = L.map('mapid');

  // 設定經緯度座標
  map.setView(new L.LatLng(23.558421, 120.472055), 15);
  
  // 設定圖資來源
  var osmUrl='https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png';
  var osm = new L.TileLayer(osmUrl, {minZoom: 8, maxZoom: 16});
  map.addLayer(osm);