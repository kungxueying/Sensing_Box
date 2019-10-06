var storageRef = firebase.storage().ref();

var downloadedImg = document.getElementById("mainpic");
var leftbtn = document.getElementById("leftbtn");
var rightbtn = document.getElementById("rightbtn");
leftbtn.addEventListener('click',changePrePic);
rightbtn.addEventListener('click',changeNextPic);
var imgvalue;
var imgfilename = new Array();
var imgnum=0;
function changePrePic(){
         console.log(imgfilename);
         imgnum = imgnum - 1;
         if(imgnum<0) imgnum = 0;
  var pathReference = storageRef.child('民雄/'+imgfilename[imgnum]+'.jpg');
            pathReference.getDownloadURL().then(function(url) {
            downloadedImg.src = url;
        })
      var imgpagenumElement = document.querySelector("#imgpagenum");
      imgpagenumElement.innerHTML =(imgnum+1)+'/'+imgfilename.length+"頁";

}
function changeNextPic(){
         console.log(imgfilename);
          imgnum = imgnum + 1;
          if(imgnum>=imgfilename.length) imgnum = imgfilename.length - 1;
  var pathReference = storageRef.child('民雄/'+imgfilename[imgnum]+'.jpg');
            pathReference.getDownloadURL().then(function(url) {
            downloadedImg.src = url;
        })
        var imgpagenumElement = document.querySelector("#imgpagenum");
      imgpagenumElement.innerHTML =(imgnum+1)+'/'+imgfilename.length+"頁";
}
function getimgdata(){
    imgnum = 0;
    var locateimg = '民雄';
    
    console.log("image download");
        var path = '2019080502';
      path = '/processeddata/image/'+locateimg+'/'+path;
 
      //read data from firebase

      db.ref(path).on('value',e => {
      console.log(e.val());
      imgvalue = e.val();
        for(filename in imgvalue){
          console.log(filename);
         imgfilename[imgnum]=filename;
         imgnum = imgnum + 1;
        }
        imgnum = 0;
        showfirstimg();
      });
function showfirstimg(){
   console.log(imgfilename);
    var pathReference = storageRef.child('民雄/'+imgfilename[0]+'.jpg');
            pathReference.getDownloadURL().then(function(url) {
            downloadedImg.src = url;
        })
   var imgpagenumElement = document.querySelector("#imgpagenum");
      imgpagenumElement.innerHTML =(imgnum+1)+'/'+imgfilename.length+"頁";
}    

    
}