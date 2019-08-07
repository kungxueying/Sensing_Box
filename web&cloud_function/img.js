var storageRef = firebase.storage().ref();

var downloadedImg = document.getElementById("mainpic");
console.log("image download");
var pathReference = storageRef.child('2019-07-09-10-28-46-1247733914.jpg');
pathReference.getDownloadURL().then(function(url) {
downloadedImg.src = url;
})