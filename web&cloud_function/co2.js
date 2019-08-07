  // 建立 Leaflet 地圖
  var map = L.map('mapid');

  // 設定經緯度座標
  map.setView(new L.LatLng(23.558, 120.472), 15);
  
  // 設定圖資來源
  var osmUrl='https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png';
  //var osm = new L.TileLayer(osmUrl, {minZoom: 8, maxZoom: 16});
  
  var Wikimedia = L.tileLayer('https://maps.wikimedia.org/osm-intl/{z}/{x}/{y}{r}.png', {
	attribution: '<a href="https://wikimediafoundation.org/wiki/Maps_Terms_of_Use">Wikimedia</a>',
	minZoom: 1,
	maxZoom: 19
})
  map.addLayer(Wikimedia);
  getco2data();
  //read data from firebase
  var cvalue;
  function getco2data() {
      var path = '/processeddata/co2';
      console.log(path);
      //read data from firebase
      db.ref(path).on('value',e => {
      console.log(e.val());
      cvalue = e.val();
      drawco2marker();
    });
   
  }
  function drawco2marker(){
    var marker = L.marker([23.55842, 120.47205]).addTo(map);
    marker.bindPopup("<strong>BOX1</strong><br>CO2濃度:350ppm。").openPopup();
    for(let ppm in cvalue){
        console.log(ppm,cvalue[ppm]['x'],cvalue[ppm]['y'],cvalue[ppm]['value']);
        L.marker([cvalue[ppm]['x'],cvalue[ppm]['y']]).addTo(map).bindPopup("<strong>"+ppm+"</strong><br>CO2濃度:"+cvalue[ppm]['value']).openPopup();
    }
  }
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  