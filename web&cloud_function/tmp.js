    var tvalue;
    var displaydate = document.getElementById("date").value;

function gettmpdata() {
      var path = gettmppath();
      path = '/processeddata/temperature/'+path;
      console.log(path);
      //read data from firebase
      
      db.ref(path).on('value',e => {
      console.log(e.val());
      tvalue = e.val();
      drawtmpTable();
      var showdataElement = document.querySelector("#showdata");
      showdataElement.innerHTML ="Successfully obtained data from firebase!";
      var avg = document.querySelector("#avg");
      avg.innerHTML = "Average temperature: "+tvalue["avg"];
      var count = document.querySelector("numberOfData");
    });
   
  }
  function gettmppath(){
      var dataType = document.getElementById("type").value;
      var date = document.getElementById("date").value;
      date = date.replace(/-/g,""); //replace all '-' (from 2019-07-20 to 20190720)
      console.log(date);
      return  dataType +"/"+ date;
  }
  function drawtmpTable(){
      console.log("Table");
      //console.log(tvalue);
    var ctx = document.getElementById('chart').getContext('2d');
    var chart = new Chart(ctx, {
      type: 'line',
      data: {
          labels: ["00:00", "01:00", "02:00", "03:00", "04:00", "05:00","06:00","07:00","08:00","09:00","10:00","11:00","12:00",
          "13:00","14:00","15:00","16:00","17:00","18:00","19:00","20:00","21:00","22:00","23:00"],
          datasets: [{
              label: displaydate+' every hour temperature',
              data: [tvalue["00"], tvalue["01"], tvalue["02"], tvalue["03"], tvalue["04"],tvalue["05"],tvalue["06"],tvalue["07"],tvalue["08"],tvalue["09"],tvalue["10"]
              ,tvalue["11"],tvalue["12"],tvalue["13"],tvalue["14"],tvalue["15"],tvalue["16"],tvalue["17"],tvalue["18"],tvalue["19"],tvalue["20"]
              ,tvalue["21"],tvalue["22"],tvalue["23"]],
              fill: false,
              borderColor:'red'
          }]

      }
    });
  }