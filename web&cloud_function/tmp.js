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
      var showAlertElement = document.querySelector("#nodataAlert");
      if(!tvalue){   
          showAlertElement.innerHTML ="There is no data, please change the date or location";
          return;
      }
      showAlertElement.innerHTML ='';
      drawtmpTable();
      var showdataElement = document.querySelector("#showdata");
      //showdataElement.innerHTML ="Successfully obtained data from firebase!";
      var avg = document.querySelector("#avg");
      avg.innerHTML = "Average temperature: "+tvalue["avg"]+"℃";
      var count = document.querySelector("#numberOfData");
      count.innerHTML ="Number of data: 共 "+tvalue["avg"]+" 筆資料";
    });
   
  }
  function gettmppath(){
      var dataType = document.getElementById("type").value;
      var locate = document.getElementById("seleteLocate").value;
      var date = document.getElementById("date").value;
      date = date.replace(/-/g,""); //replace all '-' (from 2019-07-20 to 20190720)
      console.log(date);
      return  dataType +"/"+ locate +"/"+ date;
  }
  function checkValueExist(num){
      if(!tvalue.hasOwnProperty(num)){
        return '';     
      }
      else{
        return tvalue[num];   
      }
  }
  function drawtmpTable(){
      console.log("Draw Table");
      //console.log(tvalue);
    var ctx = document.getElementById('chart').getContext('2d');
    var chart = new Chart(ctx, {
      type: 'line',
      data: {
          labels: ["00:00", "01:00", "02:00", "03:00", "04:00", "05:00","06:00","07:00","08:00","09:00","10:00","11:00","12:00",
          "13:00","14:00","15:00","16:00","17:00","18:00","19:00","20:00","21:00","22:00","23:00"],
          datasets: [{
              label: displaydate+' every hour temperature',
              data: [checkValueExist("00"), checkValueExist("01"), checkValueExist("02"),checkValueExist("03"), checkValueExist("04"),checkValueExist("05"),checkValueExist("06"),checkValueExist("07"),checkValueExist("08"),checkValueExist("09"),checkValueExist("10")
              ,checkValueExist("11"),checkValueExist("12"),checkValueExist("13"),checkValueExist("14"),checkValueExist("15"),checkValueExist("16"),checkValueExist("17"),checkValueExist("18"),checkValueExist("19"),checkValueExist("20")
              ,checkValueExist("21"),checkValueExist("22"),checkValueExist("23")],
              fill: false,
              borderColor:'#DAF7DC',         
                         
          }]
          
      },
      
      options: { 
    legend:{ 
     display: false 
    }, 
    scales: { 
    xAxes: [{
    scaleLabel: {
        display: true,
        labelString: 'time(hr:min)'
        
    },        
    ticks: {                 
        fontColor: 'black'
     },
     gridLines: { 
      show: true, 
      color: '#336699', 
     } 
    }], 
    yAxes: [{ 
    scaleLabel: {
        display: true,
        labelString: 'temperature(℃)'
       
    },
    ticks: {           
        fontColor: 'black'
    },
    gridLines: { 
      show: true, 
      color:  '#336699', 
     } 
    }] 
    }, 

} 
      
    }
    );
  }