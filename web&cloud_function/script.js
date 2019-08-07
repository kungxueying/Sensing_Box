  // Your web app's Firebase configuration
  var firebaseConfig = {
   apiKey: "AIzaSyA09_y8gMt8z03DNhtXK7Z_jEr1XbKtQfc",
  authDomain: "sensingbox-2a962.firebaseapp.com",
  databaseURL: "https://sensingbox-2a962.firebaseio.com",
  projectId: "sensingbox-2a962",
  storageBucket: "sensingbox-2a962.appspot.com",
  messagingSenderId: "851158753970",
  appId: "1:851158753970:web:ea17d5efd0ac996f"

  };
  console.log(firebase);
  // Initialize Firebase
  firebase.initializeApp(firebaseConfig);
  var isLogin = true;
  firebase.auth().signInWithEmailAndPassword('icedrip7@gmail.com', '123456')
  .catch(function(error) {
        if (error) {
            isLogin = false;
        }
    });
  console.log(isLogin);
  console.log(firebase);
 

  const db = firebase.database();
  console.log(db);
  var tmpbtn = document.querySelector("#TEM");
  var co2btn = document.querySelector("#CO2");
  var imagebtn = document.querySelector("#IMG");
  var tmp_page = document.querySelector("#tem-page");
  var co2_page = document.querySelector("#co2-page");
  var image_page = document.querySelector("#image-page");
  
  tmpbtn.addEventListener('click',showtmp);
  co2btn.addEventListener('click',showco2);
  imagebtn.addEventListener('click',showimage);
 
  
  function showtmp(){
      tmp_page.classList.remove('inactive');
      co2_page.classList.add('inactive');
      image_page.classList.add('inactive'); 
  }
  function showco2(){
      tmp_page.classList.add('inactive');
      co2_page.classList.remove('inactive');
      image_page.classList.add('inactive');    
  }
  function showimage(){
      tmp_page.classList.add('inactive');
      co2_page.classList.add('inactive');
      image_page.classList.remove('inactive');      
  }
